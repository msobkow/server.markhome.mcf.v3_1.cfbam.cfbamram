
// Description: Java 25 in-memory RAM DbIO implementation for DelDep.

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
 *	CFBamRamDelDepTable in-memory RAM DbIO implementation
 *	for DelDep.
 */
public class CFBamRamDelDepTable
	implements ICFBamDelDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDelDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDelDep >();
	private Map< CFBamBuffDelDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelDep >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffDelDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelDep >>();
	private Map< CFBamBuffDelDepByDelDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelDep >> dictByDelDepIdx
		= new HashMap< CFBamBuffDelDepByDelDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelDep >>();

	public CFBamRamDelDepTable( ICFBamSchema argSchema ) {
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
	public ICFBamDelDep createDelDep( ICFSecAuthorization Authorization,
		ICFBamDelDep iBuff )
	{
		final String S_ProcName = "createDelDep";
		
		CFBamBuffDelDep Buff = (CFBamBuffDelDep)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelDepByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getFactoryDelDep().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffDelDepByDelDepIdxKey keyDelDepIdx = (CFBamBuffDelDepByDelDepIdxKey)schema.getFactoryDelDep().newByDelDepIdxKey();
		keyDelDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"Relation",
						"Relation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDelDepIdx;
		if( dictByDelDepIdx.containsKey( keyDelDepIdx ) ) {
			subdictDelDepIdx = dictByDelDepIdx.get( keyDelDepIdx );
		}
		else {
			subdictDelDepIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDelDepIdx.put( keyDelDepIdx, subdictDelDepIdx );
		}
		subdictDelDepIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamDelDep.CLASS_CODE) {
				CFBamBuffDelDep retbuff = ((CFBamBuffDelDep)(schema.getFactoryDelDep().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep1.CLASS_CODE) {
				CFBamBuffDelSubDep1 retbuff = ((CFBamBuffDelSubDep1)(schema.getFactoryDelSubDep1().newRec()));
				retbuff.set((ICFBamDelSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep2.CLASS_CODE) {
				CFBamBuffDelSubDep2 retbuff = ((CFBamBuffDelSubDep2)(schema.getFactoryDelSubDep2().newRec()));
				retbuff.set((ICFBamDelSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep3.CLASS_CODE) {
				CFBamBuffDelSubDep3 retbuff = ((CFBamBuffDelSubDep3)(schema.getFactoryDelSubDep3().newRec()));
				retbuff.set((ICFBamDelSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelTopDep.CLASS_CODE) {
				CFBamBuffDelTopDep retbuff = ((CFBamBuffDelTopDep)(schema.getFactoryDelTopDep().newRec()));
				retbuff.set((ICFBamDelTopDep)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamDelDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerived";
		ICFBamDelDep buff;
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
	public ICFBamDelDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.lockDerived";
		ICFBamDelDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelDep.readAllDerived";
		ICFBamDelDep[] retList = new ICFBamDelDep[ dictByPKey.values().size() ];
		Iterator< CFBamBuffDelDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamDelDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelDep ) ) {
					filteredList.add( (ICFBamDelDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelDep[0] ) );
		}
	}

	@Override
	public ICFBamDelDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		CFBamBuffDelDepByDefSchemaIdxKey key = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getFactoryDelDep().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamDelDep[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamDelDep[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffDelDep > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamDelDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDelDep[] readDerivedByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		CFBamBuffDelDepByDelDepIdxKey key = (CFBamBuffDelDepByDelDepIdxKey)schema.getFactoryDelDep().newByDelDepIdxKey();

		key.setRequiredRelationId( RelationId );
		ICFBamDelDep[] recArray;
		if( dictByDelDepIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDelDepIdx
				= dictByDelDepIdx.get( key );
			recArray = new ICFBamDelDep[ subdictDelDepIdx.size() ];
			Iterator< CFBamBuffDelDep > iter = subdictDelDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdictDelDepIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDelDepIdx.put( key, subdictDelDepIdx );
			recArray = new ICFBamDelDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDelDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamDelDep buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelDep readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelDep.readRec";
		ICFBamDelDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelDep lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamDelDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelDep[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelDep.readAllRec";
		ICFBamDelDep buff;
		ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
		ICFBamDelDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelDep[0] ) );
	}

	@Override
	public ICFBamDelDep readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamDelDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamDelDep)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDelDep[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamDelDep buff;
		ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
		ICFBamDelDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelDep[0] ) );
	}

	@Override
	public ICFBamDelDep[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDefSchemaIdx() ";
		ICFBamDelDep buff;
		ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
		ICFBamDelDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelDep[0] ) );
	}

	@Override
	public ICFBamDelDep[] readRecByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDelDepIdx() ";
		ICFBamDelDep buff;
		ArrayList<ICFBamDelDep> filteredList = new ArrayList<ICFBamDelDep>();
		ICFBamDelDep[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelDep[0] ) );
	}

	public ICFBamDelDep updateDelDep( ICFSecAuthorization Authorization,
		ICFBamDelDep iBuff )
	{
		CFBamBuffDelDep Buff = (CFBamBuffDelDep)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelDep",
				"Existing record not found",
				"Existing record not found",
				"DelDep",
				"DelDep",
				pkey );
		}
		CFBamBuffDelDepByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getFactoryDelDep().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffDelDepByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getFactoryDelDep().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffDelDepByDelDepIdxKey existingKeyDelDepIdx = (CFBamBuffDelDepByDelDepIdxKey)schema.getFactoryDelDep().newByDelDepIdxKey();
		existingKeyDelDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffDelDepByDelDepIdxKey newKeyDelDepIdx = (CFBamBuffDelDepByDelDepIdxKey)schema.getFactoryDelDep().newByDelDepIdxKey();
		newKeyDelDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelDep",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelDep",
						"Lookup",
						"Lookup",
						"Relation",
						"Relation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDelDepIdx.get( existingKeyDelDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelDepIdx.containsKey( newKeyDelDepIdx ) ) {
			subdict = dictByDelDepIdx.get( newKeyDelDepIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelDep >();
			dictByDelDepIdx.put( newKeyDelDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteDelDep( ICFSecAuthorization Authorization,
		ICFBamDelDep iBuff )
	{
		final String S_ProcName = "CFBamRamDelDepTable.deleteDelDep() ";
		CFBamBuffDelDep Buff = (CFBamBuffDelDep)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffDelDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelDep",
				pkey );
		}
		CFBamBuffDelDepByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getFactoryDelDep().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffDelDepByDelDepIdxKey keyDelDepIdx = (CFBamBuffDelDepByDelDepIdxKey)schema.getFactoryDelDep().newByDelDepIdxKey();
		keyDelDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		// Validate reverse foreign keys

		if( schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"DelSubDep1",
				"DelSubDep1",
				pkey );
		}

		if( schema.getTableDelSubDep2().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"DelSubDep2",
				"DelSubDep2",
				pkey );
		}

		if( schema.getTableDelSubDep3().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"DelSubDep3",
				"DelSubDep3",
				pkey );
		}

		if( schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDelDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"DelTopDep",
				"DelTopDep",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDelDep > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByDelDepIdx.get( keyDelDepIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deleteDelDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffDelDepByDefSchemaIdxKey key = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getFactoryDelDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelDepByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteDelDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByDefSchemaIdx";
		CFBamBuffDelDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelDep> matchSet = new LinkedList<CFBamBuffDelDep>();
		Iterator<CFBamBuffDelDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelDep)(schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDelDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( ICFBamDelSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( ICFBamDelSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( ICFBamDelSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( ICFBamDelTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDelDepByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffDelDepByDelDepIdxKey key = (CFBamBuffDelDepByDelDepIdxKey)schema.getFactoryDelDep().newByDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelDepByDelDepIdx( Authorization, key );
	}

	@Override
	public void deleteDelDepByDelDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDelDepIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByDelDepIdx";
		CFBamBuffDelDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelDep> matchSet = new LinkedList<CFBamBuffDelDep>();
		Iterator<CFBamBuffDelDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelDep)(schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDelDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( ICFBamDelSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( ICFBamDelSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( ICFBamDelSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( ICFBamDelTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDelDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteDelDepByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffDelDep cur;
		LinkedList<CFBamBuffDelDep> matchSet = new LinkedList<CFBamBuffDelDep>();
		Iterator<CFBamBuffDelDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelDep)(schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDelDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( ICFBamDelSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( ICFBamDelSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( ICFBamDelSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( ICFBamDelTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDelDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelDepByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteDelDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteDelDepByTenantIdx";
		CFBamBuffDelDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelDep> matchSet = new LinkedList<CFBamBuffDelDep>();
		Iterator<CFBamBuffDelDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelDep)(schema.getTableDelDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDelDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelDep().deleteDelDep( Authorization, cur );
			}
			else if( ICFBamDelSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( ICFBamDelSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( ICFBamDelSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( ICFBamDelTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
