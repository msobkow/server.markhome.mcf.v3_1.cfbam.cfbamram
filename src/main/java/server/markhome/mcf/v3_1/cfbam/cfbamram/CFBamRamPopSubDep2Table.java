
// Description: Java 25 in-memory RAM DbIO implementation for PopSubDep2.

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
 *	CFBamRamPopSubDep2Table in-memory RAM DbIO implementation
 *	for PopSubDep2.
 */
public class CFBamRamPopSubDep2Table
	implements ICFBamPopSubDep2Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffPopSubDep2 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffPopSubDep2 >();
	private Map< CFBamBuffPopSubDep2ByPopSubDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopSubDep2 >> dictByPopSubDep1Idx
		= new HashMap< CFBamBuffPopSubDep2ByPopSubDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopSubDep2 >>();
	private Map< CFBamBuffPopSubDep2ByUNameIdxKey,
			CFBamBuffPopSubDep2 > dictByUNameIdx
		= new HashMap< CFBamBuffPopSubDep2ByUNameIdxKey,
			CFBamBuffPopSubDep2 >();

	public CFBamRamPopSubDep2Table( ICFBamSchema argSchema ) {
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
	public ICFBamPopSubDep2 createPopSubDep2( ICFSecAuthorization Authorization,
		ICFBamPopSubDep2 iBuff )
	{
		final String S_ProcName = "createPopSubDep2";
		
		CFBamBuffPopSubDep2 Buff = (CFBamBuffPopSubDep2)(schema.getTablePopDep().createPopDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopSubDep2ByPopSubDep1IdxKey keyPopSubDep1Idx = (CFBamBuffPopSubDep2ByPopSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByPopSubDep1IdxKey();
		keyPopSubDep1Idx.setRequiredPopSubDep1Id( Buff.getRequiredPopSubDep1Id() );

		CFBamBuffPopSubDep2ByUNameIdxKey keyUNameIdx = (CFBamBuffPopSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep1Id( Buff.getRequiredPopSubDep1Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopSubDep2UNameIdx",
				"PopSubDep2UNameIdx",
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
				if( null == schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"PopSubDep1",
						"PopSubDep1",
						"PopSubDep1",
						"PopSubDep1",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep2 > subdictPopSubDep1Idx;
		if( dictByPopSubDep1Idx.containsKey( keyPopSubDep1Idx ) ) {
			subdictPopSubDep1Idx = dictByPopSubDep1Idx.get( keyPopSubDep1Idx );
		}
		else {
			subdictPopSubDep1Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep2 >();
			dictByPopSubDep1Idx.put( keyPopSubDep1Idx, subdictPopSubDep1Idx );
		}
		subdictPopSubDep1Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamPopSubDep2.CLASS_CODE) {
				CFBamBuffPopSubDep2 retbuff = ((CFBamBuffPopSubDep2)(schema.getCFBamBuffFactory().getFactoryPopSubDep2().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamPopSubDep2 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readDerived";
		ICFBamPopSubDep2 buff;
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
	public ICFBamPopSubDep2 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.lockDerived";
		ICFBamPopSubDep2 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep2[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopSubDep2.readAllDerived";
		ICFBamPopSubDep2[] retList = new ICFBamPopSubDep2[ dictByPKey.values().size() ];
		Iterator< CFBamBuffPopSubDep2 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamPopSubDep2[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopSubDep2> filteredList = new ArrayList<ICFBamPopSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep2 ) ) {
					filteredList.add( (ICFBamPopSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep2[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopSubDep2> filteredList = new ArrayList<ICFBamPopSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep2 ) ) {
					filteredList.add( (ICFBamPopSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep2[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopSubDep2> filteredList = new ArrayList<ICFBamPopSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep2 ) ) {
					filteredList.add( (ICFBamPopSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep2[] readDerivedByPopSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readDerivedByPopSubDep1Idx";
		CFBamBuffPopSubDep2ByPopSubDep1IdxKey key = (CFBamBuffPopSubDep2ByPopSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByPopSubDep1IdxKey();

		key.setRequiredPopSubDep1Id( PopSubDep1Id );
		ICFBamPopSubDep2[] recArray;
		if( dictByPopSubDep1Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopSubDep2 > subdictPopSubDep1Idx
				= dictByPopSubDep1Idx.get( key );
			recArray = new ICFBamPopSubDep2[ subdictPopSubDep1Idx.size() ];
			Iterator< CFBamBuffPopSubDep2 > iter = subdictPopSubDep1Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopSubDep2 > subdictPopSubDep1Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep2 >();
			dictByPopSubDep1Idx.put( key, subdictPopSubDep1Idx );
			recArray = new ICFBamPopSubDep2[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamPopSubDep2 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readDerivedByUNameIdx";
		CFBamBuffPopSubDep2ByUNameIdxKey key = (CFBamBuffPopSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByUNameIdxKey();

		key.setRequiredPopSubDep1Id( PopSubDep1Id );
		key.setRequiredName( Name );
		ICFBamPopSubDep2 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep2 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamPopSubDep2 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep2 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readRec";
		ICFBamPopSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopSubDep2.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep2 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamPopSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopSubDep2.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep2[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readAllRec";
		ICFBamPopSubDep2 buff;
		ArrayList<ICFBamPopSubDep2> filteredList = new ArrayList<ICFBamPopSubDep2>();
		ICFBamPopSubDep2[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep2.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep2[0] ) );
	}

	@Override
	public ICFBamPopSubDep2 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamPopSubDep2 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamPopSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamPopSubDep2[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamPopSubDep2 buff;
		ArrayList<ICFBamPopSubDep2> filteredList = new ArrayList<ICFBamPopSubDep2>();
		ICFBamPopSubDep2[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep2[0] ) );
	}

	@Override
	public ICFBamPopSubDep2[] readRecByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByRelationIdx() ";
		ICFBamPopSubDep2 buff;
		ArrayList<ICFBamPopSubDep2> filteredList = new ArrayList<ICFBamPopSubDep2>();
		ICFBamPopSubDep2[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep2[0] ) );
	}

	@Override
	public ICFBamPopSubDep2[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByDefSchemaIdx() ";
		ICFBamPopSubDep2 buff;
		ArrayList<ICFBamPopSubDep2> filteredList = new ArrayList<ICFBamPopSubDep2>();
		ICFBamPopSubDep2[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep2[0] ) );
	}

	@Override
	public ICFBamPopSubDep2[] readRecByPopSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readRecByPopSubDep1Idx() ";
		ICFBamPopSubDep2 buff;
		ArrayList<ICFBamPopSubDep2> filteredList = new ArrayList<ICFBamPopSubDep2>();
		ICFBamPopSubDep2[] buffList = readDerivedByPopSubDep1Idx( Authorization,
			PopSubDep1Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep2.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep2[0] ) );
	}

	@Override
	public ICFBamPopSubDep2 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep2.readRecByUNameIdx() ";
		ICFBamPopSubDep2 buff = readDerivedByUNameIdx( Authorization,
			PopSubDep1Id,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep2.CLASS_CODE ) ) {
			return( (ICFBamPopSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamPopSubDep2 updatePopSubDep2( ICFSecAuthorization Authorization,
		ICFBamPopSubDep2 iBuff )
	{
		CFBamBuffPopSubDep2 Buff = (CFBamBuffPopSubDep2)(schema.getTablePopDep().updatePopDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopSubDep2",
				"Existing record not found",
				"Existing record not found",
				"PopSubDep2",
				"PopSubDep2",
				pkey );
		}
		CFBamBuffPopSubDep2ByPopSubDep1IdxKey existingKeyPopSubDep1Idx = (CFBamBuffPopSubDep2ByPopSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByPopSubDep1IdxKey();
		existingKeyPopSubDep1Idx.setRequiredPopSubDep1Id( existing.getRequiredPopSubDep1Id() );

		CFBamBuffPopSubDep2ByPopSubDep1IdxKey newKeyPopSubDep1Idx = (CFBamBuffPopSubDep2ByPopSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByPopSubDep1IdxKey();
		newKeyPopSubDep1Idx.setRequiredPopSubDep1Id( Buff.getRequiredPopSubDep1Id() );

		CFBamBuffPopSubDep2ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffPopSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredPopSubDep1Id( existing.getRequiredPopSubDep1Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffPopSubDep2ByUNameIdxKey newKeyUNameIdx = (CFBamBuffPopSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredPopSubDep1Id( Buff.getRequiredPopSubDep1Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopSubDep2",
					"PopSubDep2UNameIdx",
					"PopSubDep2UNameIdx",
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
						"updatePopSubDep2",
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
				if( null == schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep2",
						"Container",
						"Container",
						"PopSubDep1",
						"PopSubDep1",
						"PopSubDep1",
						"PopSubDep1",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep2 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByPopSubDep1Idx.get( existingKeyPopSubDep1Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPopSubDep1Idx.containsKey( newKeyPopSubDep1Idx ) ) {
			subdict = dictByPopSubDep1Idx.get( newKeyPopSubDep1Idx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep2 >();
			dictByPopSubDep1Idx.put( newKeyPopSubDep1Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deletePopSubDep2( ICFSecAuthorization Authorization,
		ICFBamPopSubDep2 iBuff )
	{
		final String S_ProcName = "CFBamRamPopSubDep2Table.deletePopSubDep2() ";
		CFBamBuffPopSubDep2 Buff = (CFBamBuffPopSubDep2)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffPopSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopSubDep2",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckPopDep[] = schema.getTablePopSubDep3().readDerivedByPopSubDep2Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckPopDep.length > 0 ) {
			schema.getTablePopSubDep3().deletePopSubDep3ByPopSubDep2Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffPopSubDep2ByPopSubDep1IdxKey keyPopSubDep1Idx = (CFBamBuffPopSubDep2ByPopSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByPopSubDep1IdxKey();
		keyPopSubDep1Idx.setRequiredPopSubDep1Id( existing.getRequiredPopSubDep1Id() );

		CFBamBuffPopSubDep2ByUNameIdxKey keyUNameIdx = (CFBamBuffPopSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByUNameIdxKey();
		keyUNameIdx.setRequiredPopSubDep1Id( existing.getRequiredPopSubDep1Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep2 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByPopSubDep1Idx.get( keyPopSubDep1Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	@Override
	public void deletePopSubDep2ByPopSubDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopSubDep1Id )
	{
		CFBamBuffPopSubDep2ByPopSubDep1IdxKey key = (CFBamBuffPopSubDep2ByPopSubDep1IdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByPopSubDep1IdxKey();
		key.setRequiredPopSubDep1Id( argPopSubDep1Id );
		deletePopSubDep2ByPopSubDep1Idx( Authorization, key );
	}

	@Override
	public void deletePopSubDep2ByPopSubDep1Idx( ICFSecAuthorization Authorization,
		ICFBamPopSubDep2ByPopSubDep1IdxKey argKey )
	{
		CFBamBuffPopSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep2> matchSet = new LinkedList<CFBamBuffPopSubDep2>();
		Iterator<CFBamBuffPopSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep2)(schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopSubDep1Id,
		String argName )
	{
		CFBamBuffPopSubDep2ByUNameIdxKey key = (CFBamBuffPopSubDep2ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryPopSubDep2().newByUNameIdxKey();
		key.setRequiredPopSubDep1Id( argPopSubDep1Id );
		key.setRequiredName( argName );
		deletePopSubDep2ByUNameIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamPopSubDep2ByUNameIdxKey argKey )
	{
		CFBamBuffPopSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep2> matchSet = new LinkedList<CFBamBuffPopSubDep2>();
		Iterator<CFBamBuffPopSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep2)(schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep2ByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffPopDepByRelationIdxKey key = (CFBamBuffPopDepByRelationIdxKey)schema.getCFBamBuffFactory().getFactoryPopDep().newByRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopSubDep2ByRelationIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep2ByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByRelationIdxKey argKey )
	{
		CFBamBuffPopSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep2> matchSet = new LinkedList<CFBamBuffPopSubDep2>();
		Iterator<CFBamBuffPopSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep2)(schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffPopDepByDefSchemaIdxKey key = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryPopDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopSubDep2ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffPopSubDep2 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep2> matchSet = new LinkedList<CFBamBuffPopSubDep2>();
		Iterator<CFBamBuffPopSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep2)(schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep2ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffPopSubDep2 cur;
		LinkedList<CFBamBuffPopSubDep2> matchSet = new LinkedList<CFBamBuffPopSubDep2>();
		Iterator<CFBamBuffPopSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep2)(schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamBuffFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopSubDep2ByTenantIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffPopSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep2> matchSet = new LinkedList<CFBamBuffPopSubDep2>();
		Iterator<CFBamBuffPopSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep2)(schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep2( Authorization, cur );
		}
	}
}
