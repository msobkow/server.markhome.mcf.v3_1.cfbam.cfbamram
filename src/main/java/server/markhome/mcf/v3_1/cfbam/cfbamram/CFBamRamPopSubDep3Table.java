
// Description: Java 25 in-memory RAM DbIO implementation for PopSubDep3.

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
 *	CFBamRamPopSubDep3Table in-memory RAM DbIO implementation
 *	for PopSubDep3.
 */
public class CFBamRamPopSubDep3Table
	implements ICFBamPopSubDep3Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffPopSubDep3 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffPopSubDep3 >();
	private Map< CFBamBuffPopSubDep3ByPopSubDep2IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopSubDep3 >> dictByPopSubDep2Idx
		= new HashMap< CFBamBuffPopSubDep3ByPopSubDep2IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopSubDep3 >>();
	private Map< CFBamBuffPopSubDep3ByUNameIdxKey,
			CFBamBuffPopSubDep3 > dictByUNameIdx
		= new HashMap< CFBamBuffPopSubDep3ByUNameIdxKey,
			CFBamBuffPopSubDep3 >();

	public CFBamRamPopSubDep3Table( ICFBamSchema argSchema ) {
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
	public ICFBamPopSubDep3 createPopSubDep3( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3 iBuff )
	{
		final String S_ProcName = "createPopSubDep3";
		
		CFBamBuffPopSubDep3 Buff = (CFBamBuffPopSubDep3)(schema.getTablePopDep().createPopDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey keyPopSubDep2Idx = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		keyPopSubDep2Idx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );

		CFBamBuffPopSubDep3ByUNameIdxKey keyUNameIdx = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopSubDep3UNameIdx",
				"PopSubDep3UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"PopDep",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"PopSubDep2",
						"PopSubDep2",
						"PopSubDep2",
						"PopSubDep2",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdictPopSubDep2Idx;
		if( dictByPopSubDep2Idx.containsKey( keyPopSubDep2Idx ) ) {
			subdictPopSubDep2Idx = dictByPopSubDep2Idx.get( keyPopSubDep2Idx );
		}
		else {
			subdictPopSubDep2Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep3 >();
			dictByPopSubDep2Idx.put( keyPopSubDep2Idx, subdictPopSubDep2Idx );
		}
		subdictPopSubDep2Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamPopSubDep3.CLASS_CODE) {
				CFBamBuffPopSubDep3 retbuff = ((CFBamBuffPopSubDep3)(schema.getCFBamFactory().getFactoryPopSubDep3().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamPopSubDep3 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerived";
		ICFBamPopSubDep3 buff;
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
	public ICFBamPopSubDep3 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.lockDerived";
		ICFBamPopSubDep3 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep3[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopSubDep3.readAllDerived";
		ICFBamPopSubDep3[] retList = new ICFBamPopSubDep3[ dictByPKey.values().size() ];
		Iterator< CFBamBuffPopSubDep3 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamPopSubDep3[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep3 ) ) {
					filteredList.add( (ICFBamPopSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep3[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		ICFBamPopDep buffList[] = schema.getTablePopDep().readDerivedByRelationIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamPopDep buff;
			ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep3 ) ) {
					filteredList.add( (ICFBamPopSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep3[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		ICFBamPopDep buffList[] = schema.getTablePopDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamPopDep buff;
			ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep3 ) ) {
					filteredList.add( (ICFBamPopSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep3[] readDerivedByPopSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerivedByPopSubDep2Idx";
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey key = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByPopSubDep2IdxKey();

		key.setRequiredPopSubDep2Id( PopSubDep2Id );
		ICFBamPopSubDep3[] recArray;
		if( dictByPopSubDep2Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdictPopSubDep2Idx
				= dictByPopSubDep2Idx.get( key );
			recArray = new ICFBamPopSubDep3[ subdictPopSubDep2Idx.size() ];
			Iterator< CFBamBuffPopSubDep3 > iter = subdictPopSubDep2Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdictPopSubDep2Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep3 >();
			dictByPopSubDep2Idx.put( key, subdictPopSubDep2Idx );
			recArray = new ICFBamPopSubDep3[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamPopSubDep3 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readDerivedByUNameIdx";
		CFBamBuffPopSubDep3ByUNameIdxKey key = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByUNameIdxKey();

		key.setRequiredPopSubDep2Id( PopSubDep2Id );
		key.setRequiredName( Name );
		ICFBamPopSubDep3 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep3 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamPopSubDep3 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep3 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readRec";
		ICFBamPopSubDep3 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopSubDep3.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep3 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamPopSubDep3 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopSubDep3.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep3[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readAllRec";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep3.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	@Override
	public ICFBamPopSubDep3 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamPopSubDep3 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamPopSubDep3)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamPopSubDep3[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	@Override
	public ICFBamPopSubDep3[] readRecByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByRelationIdx() ";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	@Override
	public ICFBamPopSubDep3[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByDefSchemaIdx() ";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	@Override
	public ICFBamPopSubDep3[] readRecByPopSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readRecByPopSubDep2Idx() ";
		ICFBamPopSubDep3 buff;
		ArrayList<ICFBamPopSubDep3> filteredList = new ArrayList<ICFBamPopSubDep3>();
		ICFBamPopSubDep3[] buffList = readDerivedByPopSubDep2Idx( Authorization,
			PopSubDep2Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep3.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep3[0] ) );
	}

	@Override
	public ICFBamPopSubDep3 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep3.readRecByUNameIdx() ";
		ICFBamPopSubDep3 buff = readDerivedByUNameIdx( Authorization,
			PopSubDep2Id,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep3.CLASS_CODE ) ) {
			return( (ICFBamPopSubDep3)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamPopSubDep3 updatePopSubDep3( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3 iBuff )
	{
		CFBamBuffPopSubDep3 Buff = (CFBamBuffPopSubDep3)(schema.getTablePopDep().updatePopDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopSubDep3 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopSubDep3",
				"Existing record not found",
				"Existing record not found",
				"PopSubDep3",
				"PopSubDep3",
				pkey );
		}
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey existingKeyPopSubDep2Idx = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		existingKeyPopSubDep2Idx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );

		CFBamBuffPopSubDep3ByPopSubDep2IdxKey newKeyPopSubDep2Idx = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		newKeyPopSubDep2Idx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );

		CFBamBuffPopSubDep3ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffPopSubDep3ByUNameIdxKey newKeyUNameIdx = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredPopSubDep2Id( Buff.getRequiredPopSubDep2Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopSubDep3",
					"PopSubDep3UNameIdx",
					"PopSubDep3UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep3",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"PopDep",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep3",
						"Container",
						"Container",
						"PopSubDep2",
						"PopSubDep2",
						"PopSubDep2",
						"PopSubDep2",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByPopSubDep2Idx.get( existingKeyPopSubDep2Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPopSubDep2Idx.containsKey( newKeyPopSubDep2Idx ) ) {
			subdict = dictByPopSubDep2Idx.get( newKeyPopSubDep2Idx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep3 >();
			dictByPopSubDep2Idx.put( newKeyPopSubDep2Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deletePopSubDep3( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3 iBuff )
	{
		final String S_ProcName = "CFBamRamPopSubDep3Table.deletePopSubDep3() ";
		CFBamBuffPopSubDep3 Buff = (CFBamBuffPopSubDep3)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffPopSubDep3 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopSubDep3",
				pkey );
		}
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey keyPopSubDep2Idx = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		keyPopSubDep2Idx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );

		CFBamBuffPopSubDep3ByUNameIdxKey keyUNameIdx = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep2Id( existing.getRequiredPopSubDep2Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep3 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByPopSubDep2Idx.get( keyPopSubDep2Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	@Override
	public void deletePopSubDep3ByPopSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopSubDep2Id )
	{
		CFBamBuffPopSubDep3ByPopSubDep2IdxKey key = (CFBamBuffPopSubDep3ByPopSubDep2IdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByPopSubDep2IdxKey();
		key.setRequiredPopSubDep2Id( argPopSubDep2Id );
		deletePopSubDep3ByPopSubDep2Idx( Authorization, key );
	}

	@Override
	public void deletePopSubDep3ByPopSubDep2Idx( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3ByPopSubDep2IdxKey argKey )
	{
		CFBamBuffPopSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep3> matchSet = new LinkedList<CFBamBuffPopSubDep3>();
		Iterator<CFBamBuffPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep3)(schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep3ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopSubDep2Id,
		String argName )
	{
		CFBamBuffPopSubDep3ByUNameIdxKey key = (CFBamBuffPopSubDep3ByUNameIdxKey)schema.getCFBamFactory().getFactoryPopSubDep3().newByUNameIdxKey();
		key.setRequiredPopSubDep2Id( argPopSubDep2Id );
		key.setRequiredName( argName );
		deletePopSubDep3ByUNameIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep3ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamPopSubDep3ByUNameIdxKey argKey )
	{
		CFBamBuffPopSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep3> matchSet = new LinkedList<CFBamBuffPopSubDep3>();
		Iterator<CFBamBuffPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep3)(schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep3ByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffPopDepByRelationIdxKey key = (CFBamBuffPopDepByRelationIdxKey)schema.getCFBamFactory().getFactoryPopDep().newByRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopSubDep3ByRelationIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep3ByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByRelationIdxKey argKey )
	{
		CFBamBuffPopSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep3> matchSet = new LinkedList<CFBamBuffPopSubDep3>();
		Iterator<CFBamBuffPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep3)(schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep3ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffPopDepByDefSchemaIdxKey key = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryPopDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopSubDep3ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep3ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffPopSubDep3 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep3> matchSet = new LinkedList<CFBamBuffPopSubDep3>();
		Iterator<CFBamBuffPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep3)(schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep3ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffPopSubDep3 cur;
		LinkedList<CFBamBuffPopSubDep3> matchSet = new LinkedList<CFBamBuffPopSubDep3>();
		Iterator<CFBamBuffPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep3)(schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep3ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopSubDep3ByTenantIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep3ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffPopSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep3> matchSet = new LinkedList<CFBamBuffPopSubDep3>();
		Iterator<CFBamBuffPopSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep3)(schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep3( Authorization, cur );
		}
	}
}
