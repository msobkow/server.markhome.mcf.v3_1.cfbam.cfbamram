
// Description: Java 25 in-memory RAM DbIO implementation for ClearSubDep1.

/*
 *	server.markhome.mcf.CFBam
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal CFBam 3.1 Business Application Model
 *	
 *	Copyright 2016-2026 Mark Stephen Sobkow
 *	
 *	This file is part of Mark's Code Fractal CFBam.
 *	
 *	Mark's Code Fractal CFBam is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU General Public License,
 *	Version 3 or later with classpath and static linking exceptions.
 *	
 *	As a special exception, Mark Sobkow gives you permission to link this library
 *	with independent modules to produce an executable, provided that none of them
 *	conflict with the intent of the GPLv3; that is, you are not allowed to invoke
 *	the methods of this library from non-GPLv3-compatibly licensed code. You may not
 *	implement an LPGLv3 "wedge" to try to bypass this restriction. That said, code which
 *	does not rely on this library is free to specify whatever license its authors decide
 *	to use. Mark Sobkow specifically rejects the infectious nature of the GPLv3, and
 *	considers the mere act of including GPLv3 modules in an executable to be perfectly
 *	reasonable given tools like modern Java's single-jar deployment options.
 *	
 *	Mark's Code Fractal CFBam is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFBam is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with Mark's Code Fractal CFBam.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 */

package server.markhome.mcf.v3_1.cfbam.cfbamram;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;

import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfbam.cfbam.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.buff.*;
import server.markhome.mcf.v3_1.cfint.cfint.buff.*;
import server.markhome.mcf.v3_1.cfbam.cfbam.buff.*;
import server.markhome.mcf.v3_1.cfsec.cfsecobj.*;
import server.markhome.mcf.v3_1.cfint.cfintobj.*;
import server.markhome.mcf.v3_1.cfbam.cfbamobj.*;

/*
 *	CFBamRamClearSubDep1Table in-memory RAM DbIO implementation
 *	for ClearSubDep1.
 */
public class CFBamRamClearSubDep1Table
	implements ICFBamClearSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffClearSubDep1 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffClearSubDep1 >();
	private Map< CFBamBuffClearSubDep1ByClearTopDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep1 >> dictByClearTopDepIdx
		= new HashMap< CFBamBuffClearSubDep1ByClearTopDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep1 >>();
	private Map< CFBamBuffClearSubDep1ByUNameIdxKey,
			CFBamBuffClearSubDep1 > dictByUNameIdx
		= new HashMap< CFBamBuffClearSubDep1ByUNameIdxKey,
			CFBamBuffClearSubDep1 >();

	public CFBamRamClearSubDep1Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffScope ensureRec(ICFBamScope rec) {
		if (rec == null) {
			return( null );
		}
		else {
			return (((CFBamBuffScopeFactoryService)(schema.getCFBamBuffFactory().getFactoryScope())).ensureRec(rec));
		}
	}

	@Override
	public ICFBamClearSubDep1 createClearSubDep1( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1 iBuff )
	{
		final String S_ProcName = "createClearSubDep1";
		
		CFBamBuffClearSubDep1 Buff = (CFBamBuffClearSubDep1)(schema.getTableClearDep().createClearDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearSubDep1ByClearTopDepIdxKey keyClearTopDepIdx = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByClearTopDepIdxKey();
		keyClearTopDepIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );

		CFBamBuffClearSubDep1ByUNameIdxKey keyUNameIdx = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByUNameIdxKey();
		keyUNameIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearSubDep1UNameIdx",
				"ClearSubDep1UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"ClearDep",
						"ClearDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"ClearTopDep",
						"ClearTopDep",
						"ClearTopDep",
						"ClearTopDep",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdictClearTopDepIdx;
		if( dictByClearTopDepIdx.containsKey( keyClearTopDepIdx ) ) {
			subdictClearTopDepIdx = dictByClearTopDepIdx.get( keyClearTopDepIdx );
		}
		else {
			subdictClearTopDepIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep1 >();
			dictByClearTopDepIdx.put( keyClearTopDepIdx, subdictClearTopDepIdx );
		}
		subdictClearTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamClearSubDep1.CLASS_CODE) {
				CFBamBuffClearSubDep1 retbuff = ((CFBamBuffClearSubDep1)(schema.getCFBamBuffFactory().getFactoryClearSubDep1().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamClearSubDep1 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerived";
		ICFBamClearSubDep1 buff;
		if( PKey == null ) {
			return( null );
		}
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep1 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.lockDerived";
		ICFBamClearSubDep1 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep1[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep1.readAllDerived";
		ICFBamClearSubDep1[] retList = new ICFBamClearSubDep1[ dictByPKey.values().size() ];
		Iterator< CFBamBuffClearSubDep1 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamClearSubDep1[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		ICFBamScope buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamScope buff;
			ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep1 ) ) {
					filteredList.add( (ICFBamClearSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep1[] readDerivedByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		ICFBamClearDep buffList[] = schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamClearDep buff;
			ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep1 ) ) {
					filteredList.add( (ICFBamClearSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep1[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		ICFBamClearDep buffList[] = schema.getTableClearDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamClearDep buff;
			ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep1 ) ) {
					filteredList.add( (ICFBamClearSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep1[] readDerivedByClearTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerivedByClearTopDepIdx";
		CFBamBuffClearSubDep1ByClearTopDepIdxKey key = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByClearTopDepIdxKey();

		key.setRequiredClearTopDepId( ClearTopDepId );
		ICFBamClearSubDep1[] recArray;
		if( dictByClearTopDepIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdictClearTopDepIdx
				= dictByClearTopDepIdx.get( key );
			recArray = new ICFBamClearSubDep1[ subdictClearTopDepIdx.size() ];
			Iterator< CFBamBuffClearSubDep1 > iter = subdictClearTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdictClearTopDepIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep1 >();
			dictByClearTopDepIdx.put( key, subdictClearTopDepIdx );
			recArray = new ICFBamClearSubDep1[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamClearSubDep1 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerivedByUNameIdx";
		CFBamBuffClearSubDep1ByUNameIdxKey key = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByUNameIdxKey();

		key.setRequiredClearTopDepId( ClearTopDepId );
		key.setRequiredName( Name );
		ICFBamClearSubDep1 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep1 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamClearSubDep1 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep1 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readRec";
		ICFBamClearSubDep1 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearSubDep1.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep1 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamClearSubDep1 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearSubDep1.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep1[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readAllRec";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep1.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	@Override
	public ICFBamClearSubDep1 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamClearSubDep1 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamClearSubDep1)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamClearSubDep1[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	@Override
	public ICFBamClearSubDep1[] readRecByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByClearDepIdx() ";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	@Override
	public ICFBamClearSubDep1[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByDefSchemaIdx() ";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	@Override
	public ICFBamClearSubDep1[] readRecByClearTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readRecByClearTopDepIdx() ";
		ICFBamClearSubDep1 buff;
		ArrayList<ICFBamClearSubDep1> filteredList = new ArrayList<ICFBamClearSubDep1>();
		ICFBamClearSubDep1[] buffList = readDerivedByClearTopDepIdx( Authorization,
			ClearTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep1.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep1[0] ) );
	}

	@Override
	public ICFBamClearSubDep1 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readRecByUNameIdx() ";
		ICFBamClearSubDep1 buff = readDerivedByUNameIdx( Authorization,
			ClearTopDepId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep1.CLASS_CODE ) ) {
			return( (ICFBamClearSubDep1)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamClearSubDep1 updateClearSubDep1( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1 iBuff )
	{
		CFBamBuffClearSubDep1 Buff = (CFBamBuffClearSubDep1)(schema.getTableClearDep().updateClearDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearSubDep1 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep1",
				"Existing record not found",
				"Existing record not found",
				"ClearSubDep1",
				"ClearSubDep1",
				pkey );
		}
		CFBamBuffClearSubDep1ByClearTopDepIdxKey existingKeyClearTopDepIdx = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByClearTopDepIdxKey();
		existingKeyClearTopDepIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );

		CFBamBuffClearSubDep1ByClearTopDepIdxKey newKeyClearTopDepIdx = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByClearTopDepIdxKey();
		newKeyClearTopDepIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );

		CFBamBuffClearSubDep1ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearSubDep1ByUNameIdxKey newKeyUNameIdx = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearSubDep1",
					"ClearSubDep1UNameIdx",
					"ClearSubDep1UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep1",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"ClearDep",
						"ClearDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep1",
						"Container",
						"Container",
						"ClearTopDep",
						"ClearTopDep",
						"ClearTopDep",
						"ClearTopDep",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearTopDepIdx.get( existingKeyClearTopDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearTopDepIdx.containsKey( newKeyClearTopDepIdx ) ) {
			subdict = dictByClearTopDepIdx.get( newKeyClearTopDepIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep1 >();
			dictByClearTopDepIdx.put( newKeyClearTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteClearSubDep1( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1 iBuff )
	{
		final String S_ProcName = "CFBamRamClearSubDep1Table.deleteClearSubDep1() ";
		CFBamBuffClearSubDep1 Buff = (CFBamBuffClearSubDep1)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffClearSubDep1 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearSubDep1",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckClearDep[] = schema.getTableClearSubDep2().readDerivedByClearSubDep1Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckClearDep.length > 0 ) {
			schema.getTableClearSubDep2().deleteClearSubDep2ByClearSubDep1Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffClearSubDep1ByClearTopDepIdxKey keyClearTopDepIdx = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByClearTopDepIdxKey();
		keyClearTopDepIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );

		CFBamBuffClearSubDep1ByUNameIdxKey keyUNameIdx = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByUNameIdxKey();
		keyUNameIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep1 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearTopDepIdx.get( keyClearTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	@Override
	public void deleteClearSubDep1ByClearTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearTopDepId )
	{
		CFBamBuffClearSubDep1ByClearTopDepIdxKey key = (CFBamBuffClearSubDep1ByClearTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByClearTopDepIdxKey();
		key.setRequiredClearTopDepId( argClearTopDepId );
		deleteClearSubDep1ByClearTopDepIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep1ByClearTopDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1ByClearTopDepIdxKey argKey )
	{
		CFBamBuffClearSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep1> matchSet = new LinkedList<CFBamBuffClearSubDep1>();
		Iterator<CFBamBuffClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep1)(schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep1ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearTopDepId,
		String argName )
	{
		CFBamBuffClearSubDep1ByUNameIdxKey key = (CFBamBuffClearSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep1().newByUNameIdxKey();
		key.setRequiredClearTopDepId( argClearTopDepId );
		key.setRequiredName( argName );
		deleteClearSubDep1ByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep1ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep1ByUNameIdxKey argKey )
	{
		CFBamBuffClearSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep1> matchSet = new LinkedList<CFBamBuffClearSubDep1>();
		Iterator<CFBamBuffClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep1)(schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep1ByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffClearDepByClearDepIdxKey key = (CFBamBuffClearDepByClearDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearDep().newByClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep1ByClearDepIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep1ByClearDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamBuffClearSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep1> matchSet = new LinkedList<CFBamBuffClearSubDep1>();
		Iterator<CFBamBuffClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep1)(schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep1ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffClearDepByDefSchemaIdxKey key = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryClearDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep1ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep1ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffClearSubDep1 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep1> matchSet = new LinkedList<CFBamBuffClearSubDep1>();
		Iterator<CFBamBuffClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep1)(schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep1ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffClearSubDep1 cur;
		LinkedList<CFBamBuffClearSubDep1> matchSet = new LinkedList<CFBamBuffClearSubDep1>();
		Iterator<CFBamBuffClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep1)(schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep1ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamBuffFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep1ByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep1ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffClearSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep1> matchSet = new LinkedList<CFBamBuffClearSubDep1>();
		Iterator<CFBamBuffClearSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep1)(schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep1( Authorization, cur );
		}
	}
}
