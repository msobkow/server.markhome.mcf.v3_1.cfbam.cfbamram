
// Description: Java 25 in-memory RAM DbIO implementation for ClearSubDep3.

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
 *	CFBamRamClearSubDep3Table in-memory RAM DbIO implementation
 *	for ClearSubDep3.
 */
public class CFBamRamClearSubDep3Table
	implements ICFBamClearSubDep3Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffClearSubDep3 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffClearSubDep3 >();
	private Map< CFBamBuffClearSubDep3ByClearSubDep2IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep3 >> dictByClearSubDep2Idx
		= new HashMap< CFBamBuffClearSubDep3ByClearSubDep2IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearSubDep3 >>();
	private Map< CFBamBuffClearSubDep3ByUNameIdxKey,
			CFBamBuffClearSubDep3 > dictByUNameIdx
		= new HashMap< CFBamBuffClearSubDep3ByUNameIdxKey,
			CFBamBuffClearSubDep3 >();

	public CFBamRamClearSubDep3Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffScope ensureRec(ICFBamScope rec) {
		if (rec == null) {
			return( null );
		}
		else {
			return ((CFBamRamScopeTable)(schema.getTableScope())).ensureRec((ICFBamScope)rec);
		}
	}

	@Override
	public ICFBamClearSubDep3 createClearSubDep3( ICFSecAuthorization Authorization,
		ICFBamClearSubDep3 iBuff )
	{
		final String S_ProcName = "createClearSubDep3";
		
		CFBamBuffClearSubDep3 Buff = (CFBamBuffClearSubDep3)(schema.getTableClearDep().createClearDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearSubDep3ByClearSubDep2IdxKey keyClearSubDep2Idx = (CFBamBuffClearSubDep3ByClearSubDep2IdxKey)schema.getFactoryClearSubDep3().newByClearSubDep2IdxKey();
		keyClearSubDep2Idx.setRequiredClearSubDep2Id( Buff.getRequiredClearSubDep2Id() );

		CFBamBuffClearSubDep3ByUNameIdxKey keyUNameIdx = (CFBamBuffClearSubDep3ByUNameIdxKey)schema.getFactoryClearSubDep3().newByUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep2Id( Buff.getRequiredClearSubDep2Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearSubDep3UNameIdx",
				"ClearSubDep3UNameIdx",
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
				if( null == schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"ClearSubDep2",
						"ClearSubDep2",
						"ClearSubDep2",
						"ClearSubDep2",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep3 > subdictClearSubDep2Idx;
		if( dictByClearSubDep2Idx.containsKey( keyClearSubDep2Idx ) ) {
			subdictClearSubDep2Idx = dictByClearSubDep2Idx.get( keyClearSubDep2Idx );
		}
		else {
			subdictClearSubDep2Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep3 >();
			dictByClearSubDep2Idx.put( keyClearSubDep2Idx, subdictClearSubDep2Idx );
		}
		subdictClearSubDep2Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamClearSubDep3.CLASS_CODE) {
				CFBamBuffClearSubDep3 retbuff = ((CFBamBuffClearSubDep3)(schema.getFactoryClearSubDep3().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamClearSubDep3 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readDerived";
		ICFBamClearSubDep3 buff;
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
	public ICFBamClearSubDep3 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.lockDerived";
		ICFBamClearSubDep3 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep3[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep3.readAllDerived";
		ICFBamClearSubDep3[] retList = new ICFBamClearSubDep3[ dictByPKey.values().size() ];
		Iterator< CFBamBuffClearSubDep3 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamClearSubDep3[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep3> filteredList = new ArrayList<ICFBamClearSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep3 ) ) {
					filteredList.add( (ICFBamClearSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep3[] readDerivedByClearDepIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep3> filteredList = new ArrayList<ICFBamClearSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep3 ) ) {
					filteredList.add( (ICFBamClearSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep3[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearSubDep3> filteredList = new ArrayList<ICFBamClearSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearSubDep3 ) ) {
					filteredList.add( (ICFBamClearSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamClearSubDep3[] readDerivedByClearSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readDerivedByClearSubDep2Idx";
		CFBamBuffClearSubDep3ByClearSubDep2IdxKey key = (CFBamBuffClearSubDep3ByClearSubDep2IdxKey)schema.getFactoryClearSubDep3().newByClearSubDep2IdxKey();

		key.setRequiredClearSubDep2Id( ClearSubDep2Id );
		ICFBamClearSubDep3[] recArray;
		if( dictByClearSubDep2Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep3 > subdictClearSubDep2Idx
				= dictByClearSubDep2Idx.get( key );
			recArray = new ICFBamClearSubDep3[ subdictClearSubDep2Idx.size() ];
			Iterator< CFBamBuffClearSubDep3 > iter = subdictClearSubDep2Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearSubDep3 > subdictClearSubDep2Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep3 >();
			dictByClearSubDep2Idx.put( key, subdictClearSubDep2Idx );
			recArray = new ICFBamClearSubDep3[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamClearSubDep3 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readDerivedByUNameIdx";
		CFBamBuffClearSubDep3ByUNameIdxKey key = (CFBamBuffClearSubDep3ByUNameIdxKey)schema.getFactoryClearSubDep3().newByUNameIdxKey();

		key.setRequiredClearSubDep2Id( ClearSubDep2Id );
		key.setRequiredName( Name );
		ICFBamClearSubDep3 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep3 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamClearSubDep3 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep3 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readRec";
		ICFBamClearSubDep3 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearSubDep3.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep3 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamClearSubDep3 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearSubDep3.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearSubDep3[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readAllRec";
		ICFBamClearSubDep3 buff;
		ArrayList<ICFBamClearSubDep3> filteredList = new ArrayList<ICFBamClearSubDep3>();
		ICFBamClearSubDep3[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep3.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep3[0] ) );
	}

	@Override
	public ICFBamClearSubDep3 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamClearSubDep3 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamClearSubDep3)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamClearSubDep3[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamClearSubDep3 buff;
		ArrayList<ICFBamClearSubDep3> filteredList = new ArrayList<ICFBamClearSubDep3>();
		ICFBamClearSubDep3[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep3[0] ) );
	}

	@Override
	public ICFBamClearSubDep3[] readRecByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByClearDepIdx() ";
		ICFBamClearSubDep3 buff;
		ArrayList<ICFBamClearSubDep3> filteredList = new ArrayList<ICFBamClearSubDep3>();
		ICFBamClearSubDep3[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep3[0] ) );
	}

	@Override
	public ICFBamClearSubDep3[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByDefSchemaIdx() ";
		ICFBamClearSubDep3 buff;
		ArrayList<ICFBamClearSubDep3> filteredList = new ArrayList<ICFBamClearSubDep3>();
		ICFBamClearSubDep3[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep3[0] ) );
	}

	@Override
	public ICFBamClearSubDep3[] readRecByClearSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readRecByClearSubDep2Idx() ";
		ICFBamClearSubDep3 buff;
		ArrayList<ICFBamClearSubDep3> filteredList = new ArrayList<ICFBamClearSubDep3>();
		ICFBamClearSubDep3[] buffList = readDerivedByClearSubDep2Idx( Authorization,
			ClearSubDep2Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep3.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearSubDep3[0] ) );
	}

	@Override
	public ICFBamClearSubDep3 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ClearSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep3.readRecByUNameIdx() ";
		ICFBamClearSubDep3 buff = readDerivedByUNameIdx( Authorization,
			ClearSubDep2Id,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearSubDep3.CLASS_CODE ) ) {
			return( (ICFBamClearSubDep3)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamClearSubDep3 updateClearSubDep3( ICFSecAuthorization Authorization,
		ICFBamClearSubDep3 iBuff )
	{
		CFBamBuffClearSubDep3 Buff = (CFBamBuffClearSubDep3)(schema.getTableClearDep().updateClearDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearSubDep3 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep3",
				"Existing record not found",
				"Existing record not found",
				"ClearSubDep3",
				"ClearSubDep3",
				pkey );
		}
		CFBamBuffClearSubDep3ByClearSubDep2IdxKey existingKeyClearSubDep2Idx = (CFBamBuffClearSubDep3ByClearSubDep2IdxKey)schema.getFactoryClearSubDep3().newByClearSubDep2IdxKey();
		existingKeyClearSubDep2Idx.setRequiredClearSubDep2Id( existing.getRequiredClearSubDep2Id() );

		CFBamBuffClearSubDep3ByClearSubDep2IdxKey newKeyClearSubDep2Idx = (CFBamBuffClearSubDep3ByClearSubDep2IdxKey)schema.getFactoryClearSubDep3().newByClearSubDep2IdxKey();
		newKeyClearSubDep2Idx.setRequiredClearSubDep2Id( Buff.getRequiredClearSubDep2Id() );

		CFBamBuffClearSubDep3ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffClearSubDep3ByUNameIdxKey)schema.getFactoryClearSubDep3().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearSubDep2Id( existing.getRequiredClearSubDep2Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearSubDep3ByUNameIdxKey newKeyUNameIdx = (CFBamBuffClearSubDep3ByUNameIdxKey)schema.getFactoryClearSubDep3().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredClearSubDep2Id( Buff.getRequiredClearSubDep2Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearSubDep3",
					"ClearSubDep3UNameIdx",
					"ClearSubDep3UNameIdx",
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
						"updateClearSubDep3",
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
				if( null == schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep3",
						"Container",
						"Container",
						"ClearSubDep2",
						"ClearSubDep2",
						"ClearSubDep2",
						"ClearSubDep2",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep3 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearSubDep2Idx.get( existingKeyClearSubDep2Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearSubDep2Idx.containsKey( newKeyClearSubDep2Idx ) ) {
			subdict = dictByClearSubDep2Idx.get( newKeyClearSubDep2Idx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearSubDep3 >();
			dictByClearSubDep2Idx.put( newKeyClearSubDep2Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteClearSubDep3( ICFSecAuthorization Authorization,
		ICFBamClearSubDep3 iBuff )
	{
		final String S_ProcName = "CFBamRamClearSubDep3Table.deleteClearSubDep3() ";
		CFBamBuffClearSubDep3 Buff = (CFBamBuffClearSubDep3)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffClearSubDep3 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearSubDep3",
				pkey );
		}
		CFBamBuffClearSubDep3ByClearSubDep2IdxKey keyClearSubDep2Idx = (CFBamBuffClearSubDep3ByClearSubDep2IdxKey)schema.getFactoryClearSubDep3().newByClearSubDep2IdxKey();
		keyClearSubDep2Idx.setRequiredClearSubDep2Id( existing.getRequiredClearSubDep2Id() );

		CFBamBuffClearSubDep3ByUNameIdxKey keyUNameIdx = (CFBamBuffClearSubDep3ByUNameIdxKey)schema.getFactoryClearSubDep3().newByUNameIdxKey();
		keyUNameIdx.setRequiredClearSubDep2Id( existing.getRequiredClearSubDep2Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffClearSubDep3 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearSubDep2Idx.get( keyClearSubDep2Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	@Override
	public void deleteClearSubDep3ByClearSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep2Id )
	{
		CFBamBuffClearSubDep3ByClearSubDep2IdxKey key = (CFBamBuffClearSubDep3ByClearSubDep2IdxKey)schema.getFactoryClearSubDep3().newByClearSubDep2IdxKey();
		key.setRequiredClearSubDep2Id( argClearSubDep2Id );
		deleteClearSubDep3ByClearSubDep2Idx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep3ByClearSubDep2Idx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep3ByClearSubDep2IdxKey argKey )
	{
		CFBamBuffClearSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep3> matchSet = new LinkedList<CFBamBuffClearSubDep3>();
		Iterator<CFBamBuffClearSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep3)(schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep3ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argClearSubDep2Id,
		String argName )
	{
		CFBamBuffClearSubDep3ByUNameIdxKey key = (CFBamBuffClearSubDep3ByUNameIdxKey)schema.getFactoryClearSubDep3().newByUNameIdxKey();
		key.setRequiredClearSubDep2Id( argClearSubDep2Id );
		key.setRequiredName( argName );
		deleteClearSubDep3ByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep3ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamClearSubDep3ByUNameIdxKey argKey )
	{
		CFBamBuffClearSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep3> matchSet = new LinkedList<CFBamBuffClearSubDep3>();
		Iterator<CFBamBuffClearSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep3)(schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep3ByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffClearDepByClearDepIdxKey key = (CFBamBuffClearDepByClearDepIdxKey)schema.getFactoryClearDep().newByClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep3ByClearDepIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep3ByClearDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamBuffClearSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep3> matchSet = new LinkedList<CFBamBuffClearSubDep3>();
		Iterator<CFBamBuffClearSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep3)(schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep3ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffClearDepByDefSchemaIdxKey key = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getFactoryClearDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep3ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep3ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffClearSubDep3 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep3> matchSet = new LinkedList<CFBamBuffClearSubDep3>();
		Iterator<CFBamBuffClearSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep3)(schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep3ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffClearSubDep3 cur;
		LinkedList<CFBamBuffClearSubDep3> matchSet = new LinkedList<CFBamBuffClearSubDep3>();
		Iterator<CFBamBuffClearSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep3)(schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteClearSubDep3ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep3ByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteClearSubDep3ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffClearSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearSubDep3> matchSet = new LinkedList<CFBamBuffClearSubDep3>();
		Iterator<CFBamBuffClearSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearSubDep3)(schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearSubDep3( Authorization, cur );
		}
	}
}
