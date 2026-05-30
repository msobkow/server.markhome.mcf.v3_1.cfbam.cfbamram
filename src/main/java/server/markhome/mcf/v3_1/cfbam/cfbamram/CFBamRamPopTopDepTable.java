
// Description: Java 25 in-memory RAM DbIO implementation for PopTopDep.

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
 *	CFBamRamPopTopDepTable in-memory RAM DbIO implementation
 *	for PopTopDep.
 */
public class CFBamRamPopTopDepTable
	implements ICFBamPopTopDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffPopTopDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffPopTopDep >();
	private Map< CFBamBuffPopTopDepByContRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopTopDep >> dictByContRelIdx
		= new HashMap< CFBamBuffPopTopDepByContRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopTopDep >>();
	private Map< CFBamBuffPopTopDepByUNameIdxKey,
			CFBamBuffPopTopDep > dictByUNameIdx
		= new HashMap< CFBamBuffPopTopDepByUNameIdxKey,
			CFBamBuffPopTopDep >();

	public CFBamRamPopTopDepTable( ICFBamSchema argSchema ) {
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
	public ICFBamPopTopDep createPopTopDep( ICFSecAuthorization Authorization,
		ICFBamPopTopDep iBuff )
	{
		final String S_ProcName = "createPopTopDep";
		
		CFBamBuffPopTopDep Buff = (CFBamBuffPopTopDep)(schema.getTablePopDep().createPopDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopTopDepByContRelIdxKey keyContRelIdx = (CFBamBuffPopTopDepByContRelIdxKey)schema.getFactoryPopTopDep().newByContRelIdxKey();
		keyContRelIdx.setRequiredContRelationId( Buff.getRequiredContRelationId() );

		CFBamBuffPopTopDepByUNameIdxKey keyUNameIdx = (CFBamBuffPopTopDepByUNameIdxKey)schema.getFactoryPopTopDep().newByUNameIdxKey();
		keyUNameIdx.setRequiredContRelationId( Buff.getRequiredContRelationId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopTopDepUNameIdx",
				"PopTopDepUNameIdx",
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
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredContRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"ContRelation",
						"ContRelation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffPopTopDep > subdictContRelIdx;
		if( dictByContRelIdx.containsKey( keyContRelIdx ) ) {
			subdictContRelIdx = dictByContRelIdx.get( keyContRelIdx );
		}
		else {
			subdictContRelIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopTopDep >();
			dictByContRelIdx.put( keyContRelIdx, subdictContRelIdx );
		}
		subdictContRelIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamPopTopDep.CLASS_CODE) {
				CFBamBuffPopTopDep retbuff = ((CFBamBuffPopTopDep)(schema.getFactoryPopTopDep().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamPopTopDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readDerived";
		ICFBamPopTopDep buff;
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
	public ICFBamPopTopDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopTopDep.lockDerived";
		ICFBamPopTopDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopTopDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopTopDep.readAllDerived";
		ICFBamPopTopDep[] retList = new ICFBamPopTopDep[ dictByPKey.values().size() ];
		Iterator< CFBamBuffPopTopDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamPopTopDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopTopDep> filteredList = new ArrayList<ICFBamPopTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopTopDep ) ) {
					filteredList.add( (ICFBamPopTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopTopDep[0] ) );
		}
	}

	@Override
	public ICFBamPopTopDep[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopTopDep> filteredList = new ArrayList<ICFBamPopTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopTopDep ) ) {
					filteredList.add( (ICFBamPopTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopTopDep[0] ) );
		}
	}

	@Override
	public ICFBamPopTopDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopTopDep> filteredList = new ArrayList<ICFBamPopTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopTopDep ) ) {
					filteredList.add( (ICFBamPopTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopTopDep[0] ) );
		}
	}

	@Override
	public ICFBamPopTopDep[] readDerivedByContRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readDerivedByContRelIdx";
		CFBamBuffPopTopDepByContRelIdxKey key = (CFBamBuffPopTopDepByContRelIdxKey)schema.getFactoryPopTopDep().newByContRelIdxKey();

		key.setRequiredContRelationId( ContRelationId );
		ICFBamPopTopDep[] recArray;
		if( dictByContRelIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopTopDep > subdictContRelIdx
				= dictByContRelIdx.get( key );
			recArray = new ICFBamPopTopDep[ subdictContRelIdx.size() ];
			Iterator< CFBamBuffPopTopDep > iter = subdictContRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopTopDep > subdictContRelIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopTopDep >();
			dictByContRelIdx.put( key, subdictContRelIdx );
			recArray = new ICFBamPopTopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamPopTopDep readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readDerivedByUNameIdx";
		CFBamBuffPopTopDepByUNameIdxKey key = (CFBamBuffPopTopDepByUNameIdxKey)schema.getFactoryPopTopDep().newByUNameIdxKey();

		key.setRequiredContRelationId( ContRelationId );
		key.setRequiredName( Name );
		ICFBamPopTopDep buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopTopDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamPopTopDep buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopTopDep readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readRec";
		ICFBamPopTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopTopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopTopDep lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamPopTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopTopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopTopDep[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readAllRec";
		ICFBamPopTopDep buff;
		ArrayList<ICFBamPopTopDep> filteredList = new ArrayList<ICFBamPopTopDep>();
		ICFBamPopTopDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopTopDep.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopTopDep[0] ) );
	}

	@Override
	public ICFBamPopTopDep readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamPopTopDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamPopTopDep)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamPopTopDep[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamPopTopDep buff;
		ArrayList<ICFBamPopTopDep> filteredList = new ArrayList<ICFBamPopTopDep>();
		ICFBamPopTopDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopTopDep[0] ) );
	}

	@Override
	public ICFBamPopTopDep[] readRecByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByRelationIdx() ";
		ICFBamPopTopDep buff;
		ArrayList<ICFBamPopTopDep> filteredList = new ArrayList<ICFBamPopTopDep>();
		ICFBamPopTopDep[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopTopDep[0] ) );
	}

	@Override
	public ICFBamPopTopDep[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByDefSchemaIdx() ";
		ICFBamPopTopDep buff;
		ArrayList<ICFBamPopTopDep> filteredList = new ArrayList<ICFBamPopTopDep>();
		ICFBamPopTopDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopTopDep[0] ) );
	}

	@Override
	public ICFBamPopTopDep[] readRecByContRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readRecByContRelIdx() ";
		ICFBamPopTopDep buff;
		ArrayList<ICFBamPopTopDep> filteredList = new ArrayList<ICFBamPopTopDep>();
		ICFBamPopTopDep[] buffList = readDerivedByContRelIdx( Authorization,
			ContRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopTopDep[0] ) );
	}

	@Override
	public ICFBamPopTopDep readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ContRelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopTopDep.readRecByUNameIdx() ";
		ICFBamPopTopDep buff = readDerivedByUNameIdx( Authorization,
			ContRelationId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopTopDep.CLASS_CODE ) ) {
			return( (ICFBamPopTopDep)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamPopTopDep updatePopTopDep( ICFSecAuthorization Authorization,
		ICFBamPopTopDep iBuff )
	{
		CFBamBuffPopTopDep Buff = (CFBamBuffPopTopDep)(schema.getTablePopDep().updatePopDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopTopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopTopDep",
				"Existing record not found",
				"Existing record not found",
				"PopTopDep",
				"PopTopDep",
				pkey );
		}
		CFBamBuffPopTopDepByContRelIdxKey existingKeyContRelIdx = (CFBamBuffPopTopDepByContRelIdxKey)schema.getFactoryPopTopDep().newByContRelIdxKey();
		existingKeyContRelIdx.setRequiredContRelationId( existing.getRequiredContRelationId() );

		CFBamBuffPopTopDepByContRelIdxKey newKeyContRelIdx = (CFBamBuffPopTopDepByContRelIdxKey)schema.getFactoryPopTopDep().newByContRelIdxKey();
		newKeyContRelIdx.setRequiredContRelationId( Buff.getRequiredContRelationId() );

		CFBamBuffPopTopDepByUNameIdxKey existingKeyUNameIdx = (CFBamBuffPopTopDepByUNameIdxKey)schema.getFactoryPopTopDep().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredContRelationId( existing.getRequiredContRelationId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffPopTopDepByUNameIdxKey newKeyUNameIdx = (CFBamBuffPopTopDepByUNameIdxKey)schema.getFactoryPopTopDep().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredContRelationId( Buff.getRequiredContRelationId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopTopDep",
					"PopTopDepUNameIdx",
					"PopTopDepUNameIdx",
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
						"updatePopTopDep",
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
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredContRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopTopDep",
						"Container",
						"Container",
						"ContRelation",
						"ContRelation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffPopTopDep > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByContRelIdx.get( existingKeyContRelIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContRelIdx.containsKey( newKeyContRelIdx ) ) {
			subdict = dictByContRelIdx.get( newKeyContRelIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopTopDep >();
			dictByContRelIdx.put( newKeyContRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deletePopTopDep( ICFSecAuthorization Authorization,
		ICFBamPopTopDep iBuff )
	{
		final String S_ProcName = "CFBamRamPopTopDepTable.deletePopTopDep() ";
		CFBamBuffPopTopDep Buff = (CFBamBuffPopTopDep)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffPopTopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopTopDep",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckPopDep[] = schema.getTablePopSubDep1().readDerivedByPopTopDepIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckPopDep.length > 0 ) {
			schema.getTablePopSubDep1().deletePopSubDep1ByPopTopDepIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffPopTopDepByContRelIdxKey keyContRelIdx = (CFBamBuffPopTopDepByContRelIdxKey)schema.getFactoryPopTopDep().newByContRelIdxKey();
		keyContRelIdx.setRequiredContRelationId( existing.getRequiredContRelationId() );

		CFBamBuffPopTopDepByUNameIdxKey keyUNameIdx = (CFBamBuffPopTopDepByUNameIdxKey)schema.getFactoryPopTopDep().newByUNameIdxKey();
		keyUNameIdx.setRequiredContRelationId( existing.getRequiredContRelationId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffPopTopDep > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByContRelIdx.get( keyContRelIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	@Override
	public void deletePopTopDepByContRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argContRelationId )
	{
		CFBamBuffPopTopDepByContRelIdxKey key = (CFBamBuffPopTopDepByContRelIdxKey)schema.getFactoryPopTopDep().newByContRelIdxKey();
		key.setRequiredContRelationId( argContRelationId );
		deletePopTopDepByContRelIdx( Authorization, key );
	}

	@Override
	public void deletePopTopDepByContRelIdx( ICFSecAuthorization Authorization,
		ICFBamPopTopDepByContRelIdxKey argKey )
	{
		CFBamBuffPopTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopTopDep> matchSet = new LinkedList<CFBamBuffPopTopDep>();
		Iterator<CFBamBuffPopTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopTopDep)(schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopTopDep( Authorization, cur );
		}
	}

	@Override
	public void deletePopTopDepByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argContRelationId,
		String argName )
	{
		CFBamBuffPopTopDepByUNameIdxKey key = (CFBamBuffPopTopDepByUNameIdxKey)schema.getFactoryPopTopDep().newByUNameIdxKey();
		key.setRequiredContRelationId( argContRelationId );
		key.setRequiredName( argName );
		deletePopTopDepByUNameIdx( Authorization, key );
	}

	@Override
	public void deletePopTopDepByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamPopTopDepByUNameIdxKey argKey )
	{
		CFBamBuffPopTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopTopDep> matchSet = new LinkedList<CFBamBuffPopTopDep>();
		Iterator<CFBamBuffPopTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopTopDep)(schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopTopDep( Authorization, cur );
		}
	}

	@Override
	public void deletePopTopDepByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffPopDepByRelationIdxKey key = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopTopDepByRelationIdx( Authorization, key );
	}

	@Override
	public void deletePopTopDepByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByRelationIdxKey argKey )
	{
		CFBamBuffPopTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopTopDep> matchSet = new LinkedList<CFBamBuffPopTopDep>();
		Iterator<CFBamBuffPopTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopTopDep)(schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopTopDep( Authorization, cur );
		}
	}

	@Override
	public void deletePopTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffPopDepByDefSchemaIdxKey key = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopTopDepByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deletePopTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffPopTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopTopDep> matchSet = new LinkedList<CFBamBuffPopTopDep>();
		Iterator<CFBamBuffPopTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopTopDep)(schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopTopDep( Authorization, cur );
		}
	}

	@Override
	public void deletePopTopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffPopTopDep cur;
		LinkedList<CFBamBuffPopTopDep> matchSet = new LinkedList<CFBamBuffPopTopDep>();
		Iterator<CFBamBuffPopTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopTopDep)(schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopTopDep( Authorization, cur );
		}
	}

	@Override
	public void deletePopTopDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopTopDepByTenantIdx( Authorization, key );
	}

	@Override
	public void deletePopTopDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffPopTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopTopDep> matchSet = new LinkedList<CFBamBuffPopTopDep>();
		Iterator<CFBamBuffPopTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopTopDep)(schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopTopDep( Authorization, cur );
		}
	}
}
