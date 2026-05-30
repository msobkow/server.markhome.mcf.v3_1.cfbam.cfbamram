
// Description: Java 25 in-memory RAM DbIO implementation for PopDep.

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
 *	CFBamRamPopDepTable in-memory RAM DbIO implementation
 *	for PopDep.
 */
public class CFBamRamPopDepTable
	implements ICFBamPopDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffPopDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffPopDep >();
	private Map< CFBamBuffPopDepByRelationIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopDep >> dictByRelationIdx
		= new HashMap< CFBamBuffPopDepByRelationIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopDep >>();
	private Map< CFBamBuffPopDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopDep >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffPopDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopDep >>();

	public CFBamRamPopDepTable( ICFBamSchema argSchema ) {
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
	public ICFBamPopDep createPopDep( ICFSecAuthorization Authorization,
		ICFBamPopDep iBuff )
	{
		final String S_ProcName = "createPopDep";
		
		CFBamBuffPopDep Buff = (CFBamBuffPopDep)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopDepByRelationIdxKey keyRelationIdx = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffPopDepByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

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

		Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictRelationIdx;
		if( dictByRelationIdx.containsKey( keyRelationIdx ) ) {
			subdictRelationIdx = dictByRelationIdx.get( keyRelationIdx );
		}
		else {
			subdictRelationIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByRelationIdx.put( keyRelationIdx, subdictRelationIdx );
		}
		subdictRelationIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamPopDep.CLASS_CODE) {
				CFBamBuffPopDep retbuff = ((CFBamBuffPopDep)(schema.getFactoryPopDep().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep1.CLASS_CODE) {
				CFBamBuffPopSubDep1 retbuff = ((CFBamBuffPopSubDep1)(schema.getFactoryPopSubDep1().newRec()));
				retbuff.set((ICFBamPopSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep2.CLASS_CODE) {
				CFBamBuffPopSubDep2 retbuff = ((CFBamBuffPopSubDep2)(schema.getFactoryPopSubDep2().newRec()));
				retbuff.set((ICFBamPopSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep3.CLASS_CODE) {
				CFBamBuffPopSubDep3 retbuff = ((CFBamBuffPopSubDep3)(schema.getFactoryPopSubDep3().newRec()));
				retbuff.set((ICFBamPopSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopTopDep.CLASS_CODE) {
				CFBamBuffPopTopDep retbuff = ((CFBamBuffPopTopDep)(schema.getFactoryPopTopDep().newRec()));
				retbuff.set((ICFBamPopTopDep)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamPopDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerived";
		ICFBamPopDep buff;
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
	public ICFBamPopDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.lockDerived";
		ICFBamPopDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopDep.readAllDerived";
		ICFBamPopDep[] retList = new ICFBamPopDep[ dictByPKey.values().size() ];
		Iterator< CFBamBuffPopDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamPopDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopDep ) ) {
					filteredList.add( (ICFBamPopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopDep[0] ) );
		}
	}

	@Override
	public ICFBamPopDep[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		CFBamBuffPopDepByRelationIdxKey key = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();

		key.setRequiredRelationId( RelationId );
		ICFBamPopDep[] recArray;
		if( dictByRelationIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictRelationIdx
				= dictByRelationIdx.get( key );
			recArray = new ICFBamPopDep[ subdictRelationIdx.size() ];
			Iterator< CFBamBuffPopDep > iter = subdictRelationIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictRelationIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByRelationIdx.put( key, subdictRelationIdx );
			recArray = new ICFBamPopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamPopDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		CFBamBuffPopDepByDefSchemaIdxKey key = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamPopDep[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamPopDep[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffPopDep > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamPopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamPopDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamPopDep buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopDep readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopDep.readRec";
		ICFBamPopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopDep lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamPopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopDep[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopDep.readAllRec";
		ICFBamPopDep buff;
		ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
		ICFBamPopDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopDep[0] ) );
	}

	@Override
	public ICFBamPopDep readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamPopDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamPopDep)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamPopDep[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamPopDep buff;
		ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
		ICFBamPopDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopDep[0] ) );
	}

	@Override
	public ICFBamPopDep[] readRecByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByRelationIdx() ";
		ICFBamPopDep buff;
		ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
		ICFBamPopDep[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopDep[0] ) );
	}

	@Override
	public ICFBamPopDep[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByDefSchemaIdx() ";
		ICFBamPopDep buff;
		ArrayList<ICFBamPopDep> filteredList = new ArrayList<ICFBamPopDep>();
		ICFBamPopDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopDep[0] ) );
	}

	public ICFBamPopDep updatePopDep( ICFSecAuthorization Authorization,
		ICFBamPopDep iBuff )
	{
		CFBamBuffPopDep Buff = (CFBamBuffPopDep)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopDep",
				"Existing record not found",
				"Existing record not found",
				"PopDep",
				"PopDep",
				pkey );
		}
		CFBamBuffPopDepByRelationIdxKey existingKeyRelationIdx = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();
		existingKeyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffPopDepByRelationIdxKey newKeyRelationIdx = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();
		newKeyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffPopDepByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffPopDepByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopDep",
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
						"updatePopDep",
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

		Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByRelationIdx.get( existingKeyRelationIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelationIdx.containsKey( newKeyRelationIdx ) ) {
			subdict = dictByRelationIdx.get( newKeyRelationIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByRelationIdx.put( newKeyRelationIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopDep >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deletePopDep( ICFSecAuthorization Authorization,
		ICFBamPopDep iBuff )
	{
		final String S_ProcName = "CFBamRamPopDepTable.deletePopDep() ";
		CFBamBuffPopDep Buff = (CFBamBuffPopDep)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffPopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopDep",
				pkey );
		}
		CFBamBuffPopDepByRelationIdxKey keyRelationIdx = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffPopDepByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deletePopDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"PopSubDep1",
				"PopSubDep1",
				pkey );
		}

		if( schema.getTablePopSubDep2().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deletePopDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"PopSubDep2",
				"PopSubDep2",
				pkey );
		}

		if( schema.getTablePopSubDep3().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deletePopDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"PopSubDep3",
				"PopSubDep3",
				pkey );
		}

		if( schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deletePopDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"PopTopDep",
				"PopTopDep",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffPopDep > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByRelationIdx.get( keyRelationIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deletePopDepByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffPopDepByRelationIdxKey key = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopDepByRelationIdx( Authorization, key );
	}

	@Override
	public void deletePopDepByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByRelationIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByRelationIdx";
		CFBamBuffPopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopDep> matchSet = new LinkedList<CFBamBuffPopDep>();
		Iterator<CFBamBuffPopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopDep)(schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamPopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( ICFBamPopSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( ICFBamPopSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( ICFBamPopSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( ICFBamPopTopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deletePopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffPopDepByDefSchemaIdxKey key = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopDepByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deletePopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByDefSchemaIdx";
		CFBamBuffPopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopDep> matchSet = new LinkedList<CFBamBuffPopDep>();
		Iterator<CFBamBuffPopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopDep)(schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamPopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( ICFBamPopSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( ICFBamPopSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( ICFBamPopSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( ICFBamPopTopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deletePopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deletePopDepByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffPopDep cur;
		LinkedList<CFBamBuffPopDep> matchSet = new LinkedList<CFBamBuffPopDep>();
		Iterator<CFBamBuffPopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopDep)(schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamPopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( ICFBamPopSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( ICFBamPopSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( ICFBamPopSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( ICFBamPopTopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deletePopDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopDepByTenantIdx( Authorization, key );
	}

	@Override
	public void deletePopDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deletePopDepByTenantIdx";
		CFBamBuffPopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopDep> matchSet = new LinkedList<CFBamBuffPopDep>();
		Iterator<CFBamBuffPopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopDep)(schema.getTablePopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamPopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopDep().deletePopDep( Authorization, cur );
			}
			else if( ICFBamPopSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( ICFBamPopSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( ICFBamPopSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( ICFBamPopTopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
