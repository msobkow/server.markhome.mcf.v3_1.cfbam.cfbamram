
// Description: Java 25 in-memory RAM DbIO implementation for ClearSubDep2.

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
 *	CFBamRamClearSubDep2Table in-memory RAM DbIO implementation
 *	for ClearSubDep2.
 */
public class CFBamRamClearSubDep2Table
	implements ICFBamClearSubDep2Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffClearSubDep2 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffClearSubDep2 >();
	private Map< CFBamBuffClearSubDep2ByClearSubDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep2 >> dictByClearSubDep1Idx
		= new HashMap< CFBamBuffClearSubDep2ByClearSubDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep2 >>();
	private Map< CFBamBuffClearSubDep2ByUNameIdxKey,
			CFBamBuffClearSubDep2 > dictByUNameIdx
		= new HashMap< CFBamBuffClearSubDep2ByUNameIdxKey,
			CFBamBuffClearSubDep2 >();

	public CFBamRamClearSubDep2Table( ICFBamSchema argSchema ) {
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
	public ICFBamClearSubDep2 createClearSubDep2( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2 iBuff )
	{
		final String S_ProcName = "createClearSubDep2";
		
		CFBamBuffClearSubDep2 Buff = (CFBamBuffClearSubDep2)(schema.getTableClearDep().createClearDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey keyClearSubDep1Idx = (CFBamBuffClearSubDep2ByClearSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByClearSubDep1IdxKey();
		keyClearSubDep1Idx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );

		CFBamBuffClearSubDep2ByUNameIdxKey keyUNameIdx = (CFBamBuffClearSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearSubDep2UNameIdx",
				"ClearSubDep2UNameIdx",
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
				if( null == schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"ClearSubDep1",
						"ClearSubDep1",
						"ClearSubDep1",
						"ClearSubDep1",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdictClearSubDep1Idx;
		if( dictByClearSubDep1Idx.containsKey( keyClearSubDep1Idx ) ) {
			subdictClearSubDep1Idx = dictByClearSubDep1Idx.get( keyClearSubDep1Idx );
		}
		else {
			subdictClearSubDep1Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep2 >();
			dictByClearSubDep1Idx.put( keyClearSubDep1Idx, subdictClearSubDep1Idx );
		}
		subdictClearSubDep1Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamClearSubDep2.CLASS_CODE) {
				CFBamBuffClearSubDep2 retbuff = ((CFBamBuffClearSubDep2)(schema.getCFBamBuffFactory().getFactoryClearSubDep2().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamClearSubDep2 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerived";
		ICFBamClearSubDep2 buff;
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
	public ICFBamClearSubDep2 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.lockDerived";
		ICFBamClearSubDep2 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep2[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep2.readAllDerived";
		ICFBamClearSubDep2[] retList = new ICFBamClearSubDep2[ dictByPKey.values().size() ];
		Iterator< CFBamBuffClearSubDep2 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamClearSubDep2[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep2 ) ) {
					filteredList.add( (ICFBamClearSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep2[] readDerivedByClearDepIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep2 ) ) {
					filteredList.add( (ICFBamClearSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep2[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep2 ) ) {
					filteredList.add( (ICFBamClearSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep2[] readDerivedByClearSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerivedByClearSubDep1Idx";
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey key = (CFBamBuffClearSubDep2ByClearSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByClearSubDep1IdxKey();

		key.setRequiredClearSubDep1Id( ClearSubDep1Id );
		ICFBamClearSubDep2[] recArray;
		if( dictByClearSubDep1Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdictClearSubDep1Idx
				= dictByClearSubDep1Idx.get( key );
			recArray = new ICFBamClearSubDep2[ subdictClearSubDep1Idx.size() ];
			Iterator< CFBamBuffClearSubDep2 > iter = subdictClearSubDep1Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdictClearSubDep1Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep2 >();
			dictByClearSubDep1Idx.put( key, subdictClearSubDep1Idx );
			recArray = new ICFBamClearSubDep2[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamClearSubDep2 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readDerivedByUNameIdx";
		CFBamBuffClearSubDep2ByUNameIdxKey key = (CFBamBuffClearSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByUNameIdxKey();

		key.setRequiredClearSubDep1Id( ClearSubDep1Id );
		key.setRequiredName( Name );
		ICFBamClearSubDep2 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep2 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamClearSubDep2 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep2 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readRec";
		ICFBamClearSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearSubDep2.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep2 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamClearSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearSubDep2.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep2[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readAllRec";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep2.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	@Override
	public ICFBamClearSubDep2 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamClearSubDep2 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamClearSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamClearSubDep2[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	@Override
	public ICFBamClearSubDep2[] readRecByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByClearDepIdx() ";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	@Override
	public ICFBamClearSubDep2[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByDefSchemaIdx() ";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	@Override
	public ICFBamClearSubDep2[] readRecByClearSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readRecByClearSubDep1Idx() ";
		ICFBamClearSubDep2 buff;
		ArrayList<ICFBamClearSubDep2> filteredList = new ArrayList<ICFBamClearSubDep2>();
		ICFBamClearSubDep2[] buffList = readDerivedByClearSubDep1Idx( Authorization,
			ClearSubDep1Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep2.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep2[0] ) );
	}

	@Override
	public ICFBamClearSubDep2 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep2.readRecByUNameIdx() ";
		ICFBamClearSubDep2 buff = readDerivedByUNameIdx( Authorization,
			ClearSubDep1Id,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep2.CLASS_CODE ) ) {
			return( (ICFBamClearSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamClearSubDep2 updateClearSubDep2( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2 iBuff )
	{
		CFBamBuffClearSubDep2 Buff = (CFBamBuffClearSubDep2)(schema.getTableClearDep().updateClearDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep2",
				"Existing record not found",
				"Existing record not found",
				"ClearSubDep2",
				"ClearSubDep2",
				pkey );
		}
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey existingKeyClearSubDep1Idx = (CFBamBuffClearSubDep2ByClearSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByClearSubDep1IdxKey();
		existingKeyClearSubDep1Idx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );

		CFBamBuffClearSubDep2ByClearSubDep1IdxKey newKeyClearSubDep1Idx = (CFBamBuffClearSubDep2ByClearSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByClearSubDep1IdxKey();
		newKeyClearSubDep1Idx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );

		CFBamBuffClearSubDep2ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffClearSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearSubDep2ByUNameIdxKey newKeyUNameIdx = (CFBamBuffClearSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredClearSubDep1Id( Buff.getRequiredClearSubDep1Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearSubDep2",
					"ClearSubDep2UNameIdx",
					"ClearSubDep2UNameIdx",
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
						"updateClearSubDep2",
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
				if( null == schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep2",
						"Container",
						"Container",
						"ClearSubDep1",
						"ClearSubDep1",
						"ClearSubDep1",
						"ClearSubDep1",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearSubDep1Idx.get( existingKeyClearSubDep1Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearSubDep1Idx.containsKey( newKeyClearSubDep1Idx ) ) {
			subdict = dictByClearSubDep1Idx.get( newKeyClearSubDep1Idx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep2 >();
			dictByClearSubDep1Idx.put( newKeyClearSubDep1Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteClearSubDep2( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2 iBuff )
	{
		final String S_ProcName = "CFBamRamClearSubDep2Table.deleteClearSubDep2() ";
		CFBamBuffClearSubDep2 Buff = (CFBamBuffClearSubDep2)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffClearSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearSubDep2",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckClearDep[] = schema.getTableClearSubDep3().readDerivedByClearSubDep2Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckClearDep.length > 0 ) {
			schema.getTableClearSubDep3().deleteClearSubDep3ByClearSubDep2Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey keyClearSubDep1Idx = (CFBamBuffClearSubDep2ByClearSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByClearSubDep1IdxKey();
		keyClearSubDep1Idx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );

		CFBamBuffClearSubDep2ByUNameIdxKey keyUNameIdx = (CFBamBuffClearSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep1Id( existing.getRequiredClearSubDep1Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep2 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearSubDep1Idx.get( keyClearSubDep1Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	@Override
	public void deleteClearSubDep2ByClearSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep1Id )
	{
		CFBamBuffClearSubDep2ByClearSubDep1IdxKey key = (CFBamBuffClearSubDep2ByClearSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByClearSubDep1IdxKey();
		key.setRequiredClearSubDep1Id( argClearSubDep1Id );
		deleteClearSubDep2ByClearSubDep1Idx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep2ByClearSubDep1Idx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2ByClearSubDep1IdxKey argKey )
	{
		CFBamBuffClearSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep2> matchSet = new LinkedList<CFBamBuffClearSubDep2>();
		Iterator<CFBamBuffClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep2)(schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep1Id,
		String argName )
	{
		CFBamBuffClearSubDep2ByUNameIdxKey key = (CFBamBuffClearSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearSubDep2().newByUNameIdxKey();
		key.setRequiredClearSubDep1Id( argClearSubDep1Id );
		key.setRequiredName( argName );
		deleteClearSubDep2ByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep2ByUNameIdxKey argKey )
	{
		CFBamBuffClearSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep2> matchSet = new LinkedList<CFBamBuffClearSubDep2>();
		Iterator<CFBamBuffClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep2)(schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep2ByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffClearDepByClearDepIdxKey key = (CFBamBuffClearDepByClearDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearDep().newByClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep2ByClearDepIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep2ByClearDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamBuffClearSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep2> matchSet = new LinkedList<CFBamBuffClearSubDep2>();
		Iterator<CFBamBuffClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep2)(schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffClearDepByDefSchemaIdxKey key = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryClearDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep2ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffClearSubDep2 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep2> matchSet = new LinkedList<CFBamBuffClearSubDep2>();
		Iterator<CFBamBuffClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep2)(schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep2ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffClearSubDep2 cur;
		LinkedList<CFBamBuffClearSubDep2> matchSet = new LinkedList<CFBamBuffClearSubDep2>();
		Iterator<CFBamBuffClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep2)(schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamBuffFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep2ByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffClearSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep2> matchSet = new LinkedList<CFBamBuffClearSubDep2>();
		Iterator<CFBamBuffClearSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep2)(schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep2( Authorization, cur );
		}
	}
}
