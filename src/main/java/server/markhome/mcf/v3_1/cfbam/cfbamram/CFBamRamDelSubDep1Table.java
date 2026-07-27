
// Description: Java 25 in-memory RAM DbIO implementation for DelSubDep1.

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
 *	CFBamRamDelSubDep1Table in-memory RAM DbIO implementation
 *	for DelSubDep1.
 */
public class CFBamRamDelSubDep1Table
	implements ICFBamDelSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDelSubDep1 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDelSubDep1 >();
	private Map< CFBamBuffDelSubDep1ByDelTopDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelSubDep1 >> dictByDelTopDepIdx
		= new HashMap< CFBamBuffDelSubDep1ByDelTopDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelSubDep1 >>();
	private Map< CFBamBuffDelSubDep1ByUNameIdxKey,
			CFBamBuffDelSubDep1 > dictByUNameIdx
		= new HashMap< CFBamBuffDelSubDep1ByUNameIdxKey,
			CFBamBuffDelSubDep1 >();

	public CFBamRamDelSubDep1Table( ICFBamSchema argSchema ) {
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
	public ICFBamDelSubDep1 createDelSubDep1( ICFSecAuthorization Authorization,
		ICFBamDelSubDep1 iBuff )
	{
		final String S_ProcName = "createDelSubDep1";
		
		CFBamBuffDelSubDep1 Buff = (CFBamBuffDelSubDep1)(schema.getTableDelDep().createDelDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelSubDep1ByDelTopDepIdxKey keyDelTopDepIdx = (CFBamBuffDelSubDep1ByDelTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByDelTopDepIdxKey();
		keyDelTopDepIdx.setRequiredDelTopDepId( Buff.getRequiredDelTopDepId() );

		CFBamBuffDelSubDep1ByUNameIdxKey keyUNameIdx = (CFBamBuffDelSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByUNameIdxKey();
		keyUNameIdx.setRequiredDelTopDepId( Buff.getRequiredDelTopDepId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelSubDep1UNameIdx",
				"DelSubDep1UNameIdx",
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
				if( null == schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"DelTopDep",
						"DelTopDep",
						"DelTopDep",
						"DelTopDep",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep1 > subdictDelTopDepIdx;
		if( dictByDelTopDepIdx.containsKey( keyDelTopDepIdx ) ) {
			subdictDelTopDepIdx = dictByDelTopDepIdx.get( keyDelTopDepIdx );
		}
		else {
			subdictDelTopDepIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep1 >();
			dictByDelTopDepIdx.put( keyDelTopDepIdx, subdictDelTopDepIdx );
		}
		subdictDelTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamDelSubDep1.CLASS_CODE) {
				CFBamBuffDelSubDep1 retbuff = ((CFBamBuffDelSubDep1)(schema.getCFBamBuffFactory().getFactoryDelSubDep1().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamDelSubDep1 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readDerived";
		ICFBamDelSubDep1 buff;
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
	public ICFBamDelSubDep1 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.lockDerived";
		ICFBamDelSubDep1 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep1[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelSubDep1.readAllDerived";
		ICFBamDelSubDep1[] retList = new ICFBamDelSubDep1[ dictByPKey.values().size() ];
		Iterator< CFBamBuffDelSubDep1 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamDelSubDep1[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelSubDep1> filteredList = new ArrayList<ICFBamDelSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep1 ) ) {
					filteredList.add( (ICFBamDelSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep1[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelSubDep1> filteredList = new ArrayList<ICFBamDelSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep1 ) ) {
					filteredList.add( (ICFBamDelSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep1[] readDerivedByDelDepIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelSubDep1> filteredList = new ArrayList<ICFBamDelSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelSubDep1 ) ) {
					filteredList.add( (ICFBamDelSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamDelSubDep1[] readDerivedByDelTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelTopDepId )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readDerivedByDelTopDepIdx";
		CFBamBuffDelSubDep1ByDelTopDepIdxKey key = (CFBamBuffDelSubDep1ByDelTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByDelTopDepIdxKey();

		key.setRequiredDelTopDepId( DelTopDepId );
		ICFBamDelSubDep1[] recArray;
		if( dictByDelTopDepIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelSubDep1 > subdictDelTopDepIdx
				= dictByDelTopDepIdx.get( key );
			recArray = new ICFBamDelSubDep1[ subdictDelTopDepIdx.size() ];
			Iterator< CFBamBuffDelSubDep1 > iter = subdictDelTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelSubDep1 > subdictDelTopDepIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep1 >();
			dictByDelTopDepIdx.put( key, subdictDelTopDepIdx );
			recArray = new ICFBamDelSubDep1[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDelSubDep1 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readDerivedByUNameIdx";
		CFBamBuffDelSubDep1ByUNameIdxKey key = (CFBamBuffDelSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByUNameIdxKey();

		key.setRequiredDelTopDepId( DelTopDepId );
		key.setRequiredName( Name );
		ICFBamDelSubDep1 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep1 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamDelSubDep1 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep1 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readRec";
		ICFBamDelSubDep1 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelSubDep1.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep1 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamDelSubDep1 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelSubDep1.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelSubDep1[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readAllRec";
		ICFBamDelSubDep1 buff;
		ArrayList<ICFBamDelSubDep1> filteredList = new ArrayList<ICFBamDelSubDep1>();
		ICFBamDelSubDep1[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep1.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep1[0] ) );
	}

	@Override
	public ICFBamDelSubDep1 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamDelSubDep1 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamDelSubDep1)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDelSubDep1[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamDelSubDep1 buff;
		ArrayList<ICFBamDelSubDep1> filteredList = new ArrayList<ICFBamDelSubDep1>();
		ICFBamDelSubDep1[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep1[0] ) );
	}

	@Override
	public ICFBamDelSubDep1[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDefSchemaIdx() ";
		ICFBamDelSubDep1 buff;
		ArrayList<ICFBamDelSubDep1> filteredList = new ArrayList<ICFBamDelSubDep1>();
		ICFBamDelSubDep1[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep1[0] ) );
	}

	@Override
	public ICFBamDelSubDep1[] readRecByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDelDepIdx() ";
		ICFBamDelSubDep1 buff;
		ArrayList<ICFBamDelSubDep1> filteredList = new ArrayList<ICFBamDelSubDep1>();
		ICFBamDelSubDep1[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep1[0] ) );
	}

	@Override
	public ICFBamDelSubDep1[] readRecByDelTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelTopDepId )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readRecByDelTopDepIdx() ";
		ICFBamDelSubDep1 buff;
		ArrayList<ICFBamDelSubDep1> filteredList = new ArrayList<ICFBamDelSubDep1>();
		ICFBamDelSubDep1[] buffList = readDerivedByDelTopDepIdx( Authorization,
			DelTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep1.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelSubDep1[0] ) );
	}

	@Override
	public ICFBamDelSubDep1 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DelTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readRecByUNameIdx() ";
		ICFBamDelSubDep1 buff = readDerivedByUNameIdx( Authorization,
			DelTopDepId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelSubDep1.CLASS_CODE ) ) {
			return( (ICFBamDelSubDep1)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamDelSubDep1 updateDelSubDep1( ICFSecAuthorization Authorization,
		ICFBamDelSubDep1 iBuff )
	{
		CFBamBuffDelSubDep1 Buff = (CFBamBuffDelSubDep1)(schema.getTableDelDep().updateDelDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelSubDep1 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelSubDep1",
				"Existing record not found",
				"Existing record not found",
				"DelSubDep1",
				"DelSubDep1",
				pkey );
		}
		CFBamBuffDelSubDep1ByDelTopDepIdxKey existingKeyDelTopDepIdx = (CFBamBuffDelSubDep1ByDelTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByDelTopDepIdxKey();
		existingKeyDelTopDepIdx.setRequiredDelTopDepId( existing.getRequiredDelTopDepId() );

		CFBamBuffDelSubDep1ByDelTopDepIdxKey newKeyDelTopDepIdx = (CFBamBuffDelSubDep1ByDelTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByDelTopDepIdxKey();
		newKeyDelTopDepIdx.setRequiredDelTopDepId( Buff.getRequiredDelTopDepId() );

		CFBamBuffDelSubDep1ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffDelSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredDelTopDepId( existing.getRequiredDelTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffDelSubDep1ByUNameIdxKey newKeyUNameIdx = (CFBamBuffDelSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredDelTopDepId( Buff.getRequiredDelTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelSubDep1",
					"DelSubDep1UNameIdx",
					"DelSubDep1UNameIdx",
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
						"updateDelSubDep1",
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
				if( null == schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep1",
						"Container",
						"Container",
						"DelTopDep",
						"DelTopDep",
						"DelTopDep",
						"DelTopDep",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep1 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDelTopDepIdx.get( existingKeyDelTopDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelTopDepIdx.containsKey( newKeyDelTopDepIdx ) ) {
			subdict = dictByDelTopDepIdx.get( newKeyDelTopDepIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelSubDep1 >();
			dictByDelTopDepIdx.put( newKeyDelTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteDelSubDep1( ICFSecAuthorization Authorization,
		ICFBamDelSubDep1 iBuff )
	{
		final String S_ProcName = "CFBamRamDelSubDep1Table.deleteDelSubDep1() ";
		CFBamBuffDelSubDep1 Buff = (CFBamBuffDelSubDep1)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffDelSubDep1 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelSubDep1",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckDelDep[] = schema.getTableDelSubDep2().readDerivedByContDelDep1Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckDelDep.length > 0 ) {
			schema.getTableDelSubDep2().deleteDelSubDep2ByContDelDep1Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffDelSubDep1ByDelTopDepIdxKey keyDelTopDepIdx = (CFBamBuffDelSubDep1ByDelTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByDelTopDepIdxKey();
		keyDelTopDepIdx.setRequiredDelTopDepId( existing.getRequiredDelTopDepId() );

		CFBamBuffDelSubDep1ByUNameIdxKey keyUNameIdx = (CFBamBuffDelSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByUNameIdxKey();
		keyUNameIdx.setRequiredDelTopDepId( existing.getRequiredDelTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDelSubDep1 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDelTopDepIdx.get( keyDelTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	@Override
	public void deleteDelSubDep1ByDelTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDelTopDepId )
	{
		CFBamBuffDelSubDep1ByDelTopDepIdxKey key = (CFBamBuffDelSubDep1ByDelTopDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByDelTopDepIdxKey();
		key.setRequiredDelTopDepId( argDelTopDepId );
		deleteDelSubDep1ByDelTopDepIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep1ByDelTopDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelSubDep1ByDelTopDepIdxKey argKey )
	{
		CFBamBuffDelSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep1> matchSet = new LinkedList<CFBamBuffDelSubDep1>();
		Iterator<CFBamBuffDelSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep1)(schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep1ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDelTopDepId,
		String argName )
	{
		CFBamBuffDelSubDep1ByUNameIdxKey key = (CFBamBuffDelSubDep1ByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelSubDep1().newByUNameIdxKey();
		key.setRequiredDelTopDepId( argDelTopDepId );
		key.setRequiredName( argName );
		deleteDelSubDep1ByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep1ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamDelSubDep1ByUNameIdxKey argKey )
	{
		CFBamBuffDelSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep1> matchSet = new LinkedList<CFBamBuffDelSubDep1>();
		Iterator<CFBamBuffDelSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep1)(schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep1ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffDelDepByDefSchemaIdxKey key = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryDelDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelSubDep1ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep1ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffDelSubDep1 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep1> matchSet = new LinkedList<CFBamBuffDelSubDep1>();
		Iterator<CFBamBuffDelSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep1)(schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep1ByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffDelDepByDelDepIdxKey key = (CFBamBuffDelDepByDelDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelDep().newByDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelSubDep1ByDelDepIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep1ByDelDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamBuffDelSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep1> matchSet = new LinkedList<CFBamBuffDelSubDep1>();
		Iterator<CFBamBuffDelSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep1)(schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep1ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffDelSubDep1 cur;
		LinkedList<CFBamBuffDelSubDep1> matchSet = new LinkedList<CFBamBuffDelSubDep1>();
		Iterator<CFBamBuffDelSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep1)(schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deleteDelSubDep1ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamBuffFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelSubDep1ByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteDelSubDep1ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffDelSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelSubDep1> matchSet = new LinkedList<CFBamBuffDelSubDep1>();
		Iterator<CFBamBuffDelSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelSubDep1)(schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelSubDep1( Authorization, cur );
		}
	}
}
