
// Description: Java 25 in-memory RAM DbIO implementation for DelSubDep3.

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
 *	CFBamRamDelSubDep3Table in-memory RAM DbIO implementation
 *	for DelSubDep3.
 */
public class CFBamRamDelSubDep3Table
	implements ICFBamDelSubDep3Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDelSubDep3 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDelSubDep3 >();
	private Map< CFBamBuffDelSubDep3ByDelSubDep2IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelSubDep3 >> dictByDelSubDep2Idx
		= new HashMap< CFBamBuffDelSubDep3ByDelSubDep2IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelSubDep3 >>();
	private Map< CFBamBuffDelSubDep3ByUNameIdxKey,
			CFBamBuffDelSubDep3 > dictByUNameIdx
		= new HashMap< CFBamBuffDelSubDep3ByUNameIdxKey,
			CFBamBuffDelSubDep3 >();

	public CFBamRamDelSubDep3Table( ICFBamSchema argSchema ) {
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
	public ICFBamDelSubDep3 createDelSubDep3( ICFSecAuthorization Authorization,
		ICFBamDelSubDep3 iBuff )
	{
		final String S_ProcName = "createDelSubDep3";
		
		CFBamBuffDelSubDep3 Buff = (CFBamBuffDelSubDep3)(schema.getTableDelDep().createDelDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelSubDep3ByDelSubDep2IdxKey keyDelSubDep2Idx = (CFBamBuffDelSubDep3ByDelSubDep2IdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByDelSubDep2IdxKey();
		keyDelSubDep2Idx.setRequiredDelSubDep2Id( Buff.getRequiredDelSubDep2Id() );

		CFBamBuffDelSubDep3ByUNameIdxKey keyUNameIdx = (CFBamBuffDelSubDep3ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep2Id( Buff.getRequiredDelSubDep2Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelSubDep3UNameIdx",
				"DelSubDep3UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"DelDep",
						"DelDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"DelSubDep2",
						"DelSubDep2",
						"DelSubDep2",
						"DelSubDep2",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep3 > subdictDelSubDep2Idx;
		if( dictByDelSubDep2Idx.containsKey( keyDelSubDep2Idx ) ) {
			subdictDelSubDep2Idx = dictByDelSubDep2Idx.get( keyDelSubDep2Idx );
		}
		else {
			subdictDelSubDep2Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep3 >();
			dictByDelSubDep2Idx.put( keyDelSubDep2Idx, subdictDelSubDep2Idx );
		}
		subdictDelSubDep2Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamDelSubDep3.CLASS_CODE) {
				CFBamBuffDelSubDep3 retbuff = ((CFBamBuffDelSubDep3)(schema.getCFBamBuffFactory().getFactoryDelSubDep3().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamDelSubDep3 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readDerived";
		ICFBamDelSubDep3 buff;
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
	public ICFBamDelSubDep3 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.lockDerived";
		ICFBamDelSubDep3 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep3[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelSubDep3.readAllDerived";
		ICFBamDelSubDep3[] retList = new ICFBamDelSubDep3[ dictByPKey.values().size() ];
		Iterator< CFBamBuffDelSubDep3 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamDelSubDep3[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelSubDep3> filteredList = new ArrayList<ICFBamDelSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep3 ) ) {
					filteredList.add( (ICFBamDelSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep3[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		ICFBamDelDep buffList[] = schema.getTableDelDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamDelDep buff;
			ArrayList<ICFBamDelSubDep3> filteredList = new ArrayList<ICFBamDelSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep3 ) ) {
					filteredList.add( (ICFBamDelSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep3[] readDerivedByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		ICFBamDelDep buffList[] = schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamDelDep buff;
			ArrayList<ICFBamDelSubDep3> filteredList = new ArrayList<ICFBamDelSubDep3>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep3 ) ) {
					filteredList.add( (ICFBamDelSubDep3)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep3[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep3[] readDerivedByDelSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep2Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readDerivedByDelSubDep2Idx";
		CFBamBuffDelSubDep3ByDelSubDep2IdxKey key = (CFBamBuffDelSubDep3ByDelSubDep2IdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByDelSubDep2IdxKey();

		key.setRequiredDelSubDep2Id( DelSubDep2Id );
		ICFBamDelSubDep3[] recArray;
		if( dictByDelSubDep2Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelSubDep3 > subdictDelSubDep2Idx
				= dictByDelSubDep2Idx.get( key );
			recArray = new ICFBamDelSubDep3[ subdictDelSubDep2Idx.size() ];
			Iterator< CFBamBuffDelSubDep3 > iter = subdictDelSubDep2Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelSubDep3 > subdictDelSubDep2Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep3 >();
			dictByDelSubDep2Idx.put( key, subdictDelSubDep2Idx );
			recArray = new ICFBamDelSubDep3[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDelSubDep3 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readDerivedByUNameIdx";
		CFBamBuffDelSubDep3ByUNameIdxKey key = (CFBamBuffDelSubDep3ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByUNameIdxKey();

		key.setRequiredDelSubDep2Id( DelSubDep2Id );
		key.setRequiredName( Name );
		ICFBamDelSubDep3 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep3 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamDelSubDep3 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep3 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readRec";
		ICFBamDelSubDep3 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelSubDep3.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep3 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamDelSubDep3 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelSubDep3.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep3[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readAllRec";
		ICFBamDelSubDep3 buff;
		ArrayList<ICFBamDelSubDep3> filteredList = new ArrayList<ICFBamDelSubDep3>();
		ICFBamDelSubDep3[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep3.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep3[0] ) );
	}

	@Override
	public ICFBamDelSubDep3 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamDelSubDep3 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamDelSubDep3)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDelSubDep3[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamDelSubDep3 buff;
		ArrayList<ICFBamDelSubDep3> filteredList = new ArrayList<ICFBamDelSubDep3>();
		ICFBamDelSubDep3[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep3[0] ) );
	}

	@Override
	public ICFBamDelSubDep3[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDefSchemaIdx() ";
		ICFBamDelSubDep3 buff;
		ArrayList<ICFBamDelSubDep3> filteredList = new ArrayList<ICFBamDelSubDep3>();
		ICFBamDelSubDep3[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep3[0] ) );
	}

	@Override
	public ICFBamDelSubDep3[] readRecByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDelDepIdx() ";
		ICFBamDelSubDep3 buff;
		ArrayList<ICFBamDelSubDep3> filteredList = new ArrayList<ICFBamDelSubDep3>();
		ICFBamDelSubDep3[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep3[0] ) );
	}

	@Override
	public ICFBamDelSubDep3[] readRecByDelSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep2Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readRecByDelSubDep2Idx() ";
		ICFBamDelSubDep3 buff;
		ArrayList<ICFBamDelSubDep3> filteredList = new ArrayList<ICFBamDelSubDep3>();
		ICFBamDelSubDep3[] buffList = readDerivedByDelSubDep2Idx( Authorization,
			DelSubDep2Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep3.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep3)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep3[0] ) );
	}

	@Override
	public ICFBamDelSubDep3 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep2Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep3.readRecByUNameIdx() ";
		ICFBamDelSubDep3 buff = readDerivedByUNameIdx( Authorization,
			DelSubDep2Id,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep3.CLASS_CODE ) ) {
			return( (ICFBamDelSubDep3)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamDelSubDep3 updateDelSubDep3( ICFSecAuthorization Authorization,
		ICFBamDelSubDep3 iBuff )
	{
		CFBamBuffDelSubDep3 Buff = (CFBamBuffDelSubDep3)(schema.getTableDelDep().updateDelDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelSubDep3 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelSubDep3",
				"Existing record not found",
				"Existing record not found",
				"DelSubDep3",
				"DelSubDep3",
				pkey );
		}
		CFBamBuffDelSubDep3ByDelSubDep2IdxKey existingKeyDelSubDep2Idx = (CFBamBuffDelSubDep3ByDelSubDep2IdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByDelSubDep2IdxKey();
		existingKeyDelSubDep2Idx.setRequiredDelSubDep2Id( existing.getRequiredDelSubDep2Id() );

		CFBamBuffDelSubDep3ByDelSubDep2IdxKey newKeyDelSubDep2Idx = (CFBamBuffDelSubDep3ByDelSubDep2IdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByDelSubDep2IdxKey();
		newKeyDelSubDep2Idx.setRequiredDelSubDep2Id( Buff.getRequiredDelSubDep2Id() );

		CFBamBuffDelSubDep3ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffDelSubDep3ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredDelSubDep2Id( existing.getRequiredDelSubDep2Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffDelSubDep3ByUNameIdxKey newKeyUNameIdx = (CFBamBuffDelSubDep3ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredDelSubDep2Id( Buff.getRequiredDelSubDep2Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelSubDep3",
					"DelSubDep3UNameIdx",
					"DelSubDep3UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep3",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"DelDep",
						"DelDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep2Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep3",
						"Container",
						"Container",
						"DelSubDep2",
						"DelSubDep2",
						"DelSubDep2",
						"DelSubDep2",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep3 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDelSubDep2Idx.get( existingKeyDelSubDep2Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelSubDep2Idx.containsKey( newKeyDelSubDep2Idx ) ) {
			subdict = dictByDelSubDep2Idx.get( newKeyDelSubDep2Idx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep3 >();
			dictByDelSubDep2Idx.put( newKeyDelSubDep2Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteDelSubDep3( ICFSecAuthorization Authorization,
		ICFBamDelSubDep3 iBuff )
	{
		final String S_ProcName = "CFBamRamDelSubDep3Table.deleteDelSubDep3() ";
		CFBamBuffDelSubDep3 Buff = (CFBamBuffDelSubDep3)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffDelSubDep3 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelSubDep3",
				pkey );
		}
		CFBamBuffDelSubDep3ByDelSubDep2IdxKey keyDelSubDep2Idx = (CFBamBuffDelSubDep3ByDelSubDep2IdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByDelSubDep2IdxKey();
		keyDelSubDep2Idx.setRequiredDelSubDep2Id( existing.getRequiredDelSubDep2Id() );

		CFBamBuffDelSubDep3ByUNameIdxKey keyUNameIdx = (CFBamBuffDelSubDep3ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep2Id( existing.getRequiredDelSubDep2Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep3 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDelSubDep2Idx.get( keyDelSubDep2Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	@Override
	public void deleteDelSubDep3ByDelSubDep2Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDelSubDep2Id )
	{
		CFBamBuffDelSubDep3ByDelSubDep2IdxKey key = (CFBamBuffDelSubDep3ByDelSubDep2IdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByDelSubDep2IdxKey();
		key.setRequiredDelSubDep2Id( argDelSubDep2Id );
		deleteDelSubDep3ByDelSubDep2Idx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep3ByDelSubDep2Idx( ICFSecAuthorization Authorization,
		ICFBamDelSubDep3ByDelSubDep2IdxKey argKey )
	{
		CFBamBuffDelSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep3> matchSet = new LinkedList<CFBamBuffDelSubDep3>();
		Iterator<CFBamBuffDelSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep3)(schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep3ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDelSubDep2Id,
		String argName )
	{
		CFBamBuffDelSubDep3ByUNameIdxKey key = (CFBamBuffDelSubDep3ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep3().newByUNameIdxKey();
		key.setRequiredDelSubDep2Id( argDelSubDep2Id );
		key.setRequiredName( argName );
		deleteDelSubDep3ByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep3ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamDelSubDep3ByUNameIdxKey argKey )
	{
		CFBamBuffDelSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep3> matchSet = new LinkedList<CFBamBuffDelSubDep3>();
		Iterator<CFBamBuffDelSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep3)(schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep3ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffDelDepByDefSchemaIdxKey key = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryDelDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelSubDep3ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep3ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffDelSubDep3 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep3> matchSet = new LinkedList<CFBamBuffDelSubDep3>();
		Iterator<CFBamBuffDelSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep3)(schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep3ByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffDelDepByDelDepIdxKey key = (CFBamBuffDelDepByDelDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelDep().newByDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelSubDep3ByDelDepIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep3ByDelDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamBuffDelSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep3> matchSet = new LinkedList<CFBamBuffDelSubDep3>();
		Iterator<CFBamBuffDelSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep3)(schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep3ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffDelSubDep3 cur;
		LinkedList<CFBamBuffDelSubDep3> matchSet = new LinkedList<CFBamBuffDelSubDep3>();
		Iterator<CFBamBuffDelSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep3)(schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep3( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep3ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamBuffFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelSubDep3ByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep3ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffDelSubDep3 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep3> matchSet = new LinkedList<CFBamBuffDelSubDep3>();
		Iterator<CFBamBuffDelSubDep3> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep3> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep3)(schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep3( Authorization, cur );
		}
	}
}
