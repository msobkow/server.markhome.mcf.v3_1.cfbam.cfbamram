
// Description: Java 25 in-memory RAM DbIO implementation for DelSubDep2.

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
 *	CFBamRamDelSubDep2Table in-memory RAM DbIO implementation
 *	for DelSubDep2.
 */
public class CFBamRamDelSubDep2Table
	implements ICFBamDelSubDep2Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDelSubDep2 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDelSubDep2 >();
	private Map< CFBamBuffDelSubDep2ByContDelDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelSubDep2 >> dictByContDelDep1Idx
		= new HashMap< CFBamBuffDelSubDep2ByContDelDep1IdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelSubDep2 >>();
	private Map< CFBamBuffDelSubDep2ByUNameIdxKey,
			CFBamBuffDelSubDep2 > dictByUNameIdx
		= new HashMap< CFBamBuffDelSubDep2ByUNameIdxKey,
			CFBamBuffDelSubDep2 >();

	public CFBamRamDelSubDep2Table( ICFBamSchema argSchema ) {
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
	public ICFBamDelSubDep2 createDelSubDep2( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2 iBuff )
	{
		final String S_ProcName = "createDelSubDep2";
		
		CFBamBuffDelSubDep2 Buff = (CFBamBuffDelSubDep2)(schema.getTableDelDep().createDelDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelSubDep2ByContDelDep1IdxKey keyContDelDep1Idx = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByContDelDep1IdxKey();
		keyContDelDep1Idx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );

		CFBamBuffDelSubDep2ByUNameIdxKey keyUNameIdx = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelSubDep2UNameIdx",
				"DelSubDep2UNameIdx",
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
				if( null == schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"DelSubDep1",
						"DelSubDep1",
						"DelSubDep1",
						"DelSubDep1",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdictContDelDep1Idx;
		if( dictByContDelDep1Idx.containsKey( keyContDelDep1Idx ) ) {
			subdictContDelDep1Idx = dictByContDelDep1Idx.get( keyContDelDep1Idx );
		}
		else {
			subdictContDelDep1Idx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep2 >();
			dictByContDelDep1Idx.put( keyContDelDep1Idx, subdictContDelDep1Idx );
		}
		subdictContDelDep1Idx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamDelSubDep2.CLASS_CODE) {
				CFBamBuffDelSubDep2 retbuff = ((CFBamBuffDelSubDep2)(schema.getCFBamFactory().getFactoryDelSubDep2().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamDelSubDep2 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerived";
		ICFBamDelSubDep2 buff;
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
	public ICFBamDelSubDep2 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.lockDerived";
		ICFBamDelSubDep2 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep2[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelSubDep2.readAllDerived";
		ICFBamDelSubDep2[] retList = new ICFBamDelSubDep2[ dictByPKey.values().size() ];
		Iterator< CFBamBuffDelSubDep2 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamDelSubDep2[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep2 ) ) {
					filteredList.add( (ICFBamDelSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep2[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep2 ) ) {
					filteredList.add( (ICFBamDelSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep2[] readDerivedByDelDepIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep2 ) ) {
					filteredList.add( (ICFBamDelSubDep2)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep2[] readDerivedByContDelDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerivedByContDelDep1Idx";
		CFBamBuffDelSubDep2ByContDelDep1IdxKey key = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByContDelDep1IdxKey();

		key.setRequiredDelSubDep1Id( DelSubDep1Id );
		ICFBamDelSubDep2[] recArray;
		if( dictByContDelDep1Idx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdictContDelDep1Idx
				= dictByContDelDep1Idx.get( key );
			recArray = new ICFBamDelSubDep2[ subdictContDelDep1Idx.size() ];
			Iterator< CFBamBuffDelSubDep2 > iter = subdictContDelDep1Idx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdictContDelDep1Idx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep2 >();
			dictByContDelDep1Idx.put( key, subdictContDelDep1Idx );
			recArray = new ICFBamDelSubDep2[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDelSubDep2 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readDerivedByUNameIdx";
		CFBamBuffDelSubDep2ByUNameIdxKey key = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByUNameIdxKey();

		key.setRequiredDelSubDep1Id( DelSubDep1Id );
		key.setRequiredName( Name );
		ICFBamDelSubDep2 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep2 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamDelSubDep2 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep2 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readRec";
		ICFBamDelSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelSubDep2.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep2 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamDelSubDep2 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelSubDep2.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep2[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readAllRec";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep2.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	@Override
	public ICFBamDelSubDep2 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamDelSubDep2 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamDelSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDelSubDep2[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	@Override
	public ICFBamDelSubDep2[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDefSchemaIdx() ";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	@Override
	public ICFBamDelSubDep2[] readRecByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDelDepIdx() ";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	@Override
	public ICFBamDelSubDep2[] readRecByContDelDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readRecByContDelDep1Idx() ";
		ICFBamDelSubDep2 buff;
		ArrayList<ICFBamDelSubDep2> filteredList = new ArrayList<ICFBamDelSubDep2>();
		ICFBamDelSubDep2[] buffList = readDerivedByContDelDep1Idx( Authorization,
			DelSubDep1Id );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep2.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep2)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep2[0] ) );
	}

	@Override
	public ICFBamDelSubDep2 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelSubDep1Id,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep2.readRecByUNameIdx() ";
		ICFBamDelSubDep2 buff = readDerivedByUNameIdx( Authorization,
			DelSubDep1Id,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep2.CLASS_CODE ) ) {
			return( (ICFBamDelSubDep2)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamDelSubDep2 updateDelSubDep2( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2 iBuff )
	{
		CFBamBuffDelSubDep2 Buff = (CFBamBuffDelSubDep2)(schema.getTableDelDep().updateDelDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelSubDep2",
				"Existing record not found",
				"Existing record not found",
				"DelSubDep2",
				"DelSubDep2",
				pkey );
		}
		CFBamBuffDelSubDep2ByContDelDep1IdxKey existingKeyContDelDep1Idx = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByContDelDep1IdxKey();
		existingKeyContDelDep1Idx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );

		CFBamBuffDelSubDep2ByContDelDep1IdxKey newKeyContDelDep1Idx = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByContDelDep1IdxKey();
		newKeyContDelDep1Idx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );

		CFBamBuffDelSubDep2ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffDelSubDep2ByUNameIdxKey newKeyUNameIdx = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredDelSubDep1Id( Buff.getRequiredDelSubDep1Id() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelSubDep2",
					"DelSubDep2UNameIdx",
					"DelSubDep2UNameIdx",
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
						"updateDelSubDep2",
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
				if( null == schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelSubDep1Id() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep2",
						"Container",
						"Container",
						"DelSubDep1",
						"DelSubDep1",
						"DelSubDep1",
						"DelSubDep1",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByContDelDep1Idx.get( existingKeyContDelDep1Idx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContDelDep1Idx.containsKey( newKeyContDelDep1Idx ) ) {
			subdict = dictByContDelDep1Idx.get( newKeyContDelDep1Idx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep2 >();
			dictByContDelDep1Idx.put( newKeyContDelDep1Idx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteDelSubDep2( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2 iBuff )
	{
		final String S_ProcName = "CFBamRamDelSubDep2Table.deleteDelSubDep2() ";
		CFBamBuffDelSubDep2 Buff = (CFBamBuffDelSubDep2)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffDelSubDep2 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelSubDep2",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckDelDep[] = schema.getTableDelSubDep3().readDerivedByDelSubDep2Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckDelDep.length > 0 ) {
			schema.getTableDelSubDep3().deleteDelSubDep3ByDelSubDep2Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffDelSubDep2ByContDelDep1IdxKey keyContDelDep1Idx = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByContDelDep1IdxKey();
		keyContDelDep1Idx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );

		CFBamBuffDelSubDep2ByUNameIdxKey keyUNameIdx = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByUNameIdxKey();
		keyUNameIdx.setRequiredDelSubDep1Id( existing.getRequiredDelSubDep1Id() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep2 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByContDelDep1Idx.get( keyContDelDep1Idx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	@Override
	public void deleteDelSubDep2ByContDelDep1Idx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDelSubDep1Id )
	{
		CFBamBuffDelSubDep2ByContDelDep1IdxKey key = (CFBamBuffDelSubDep2ByContDelDep1IdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByContDelDep1IdxKey();
		key.setRequiredDelSubDep1Id( argDelSubDep1Id );
		deleteDelSubDep2ByContDelDep1Idx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep2ByContDelDep1Idx( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2ByContDelDep1IdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDelSubDep1Id,
		String argName )
	{
		CFBamBuffDelSubDep2ByUNameIdxKey key = (CFBamBuffDelSubDep2ByUNameIdxKey)schema.getCFBamFactory().getFactoryDelSubDep2().newByUNameIdxKey();
		key.setRequiredDelSubDep1Id( argDelSubDep1Id );
		key.setRequiredName( argName );
		deleteDelSubDep2ByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep2ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamDelSubDep2ByUNameIdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffDelDepByDefSchemaIdxKey key = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryDelDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelSubDep2ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep2ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep2ByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffDelDepByDelDepIdxKey key = (CFBamBuffDelDepByDelDepIdxKey)schema.getCFBamFactory().getFactoryDelDep().newByDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelSubDep2ByDelDepIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep2ByDelDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep2ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffDelSubDep2 cur;
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelSubDep2ByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep2ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffDelSubDep2 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep2> matchSet = new LinkedList<CFBamBuffDelSubDep2>();
		Iterator<CFBamBuffDelSubDep2> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep2> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep2)(schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep2( Authorization, cur );
		}
	}
}
