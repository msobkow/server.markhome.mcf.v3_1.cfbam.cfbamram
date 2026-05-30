
// Description: Java 25 in-memory RAM DbIO implementation for ClearDep.

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
 *	CFBamRamClearDepTable in-memory RAM DbIO implementation
 *	for ClearDep.
 */
public class CFBamRamClearDepTable
	implements ICFBamClearDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffClearDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffClearDep >();
	private Map< CFBamBuffClearDepByClearDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearDep >> dictByClearDepIdx
		= new HashMap< CFBamBuffClearDepByClearDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearDep >>();
	private Map< CFBamBuffClearDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearDep >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffClearDepByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearDep >>();

	public CFBamRamClearDepTable( ICFBamSchema argSchema ) {
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
	public ICFBamClearDep createClearDep( ICFSecAuthorization Authorization,
		ICFBamClearDep iBuff )
	{
		final String S_ProcName = "createClearDep";
		
		CFBamBuffClearDep Buff = (CFBamBuffClearDep)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearDepByClearDepIdxKey keyClearDepIdx = (CFBamBuffClearDepByClearDepIdxKey)schema.getFactoryClearDep().newByClearDepIdxKey();
		keyClearDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffClearDepByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getFactoryClearDep().newByDefSchemaIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffClearDep > subdictClearDepIdx;
		if( dictByClearDepIdx.containsKey( keyClearDepIdx ) ) {
			subdictClearDepIdx = dictByClearDepIdx.get( keyClearDepIdx );
		}
		else {
			subdictClearDepIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearDep >();
			dictByClearDepIdx.put( keyClearDepIdx, subdictClearDepIdx );
		}
		subdictClearDepIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffClearDep > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearDep >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamClearDep.CLASS_CODE) {
				CFBamBuffClearDep retbuff = ((CFBamBuffClearDep)(schema.getFactoryClearDep().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep1.CLASS_CODE) {
				CFBamBuffClearSubDep1 retbuff = ((CFBamBuffClearSubDep1)(schema.getFactoryClearSubDep1().newRec()));
				retbuff.set((ICFBamClearSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep2.CLASS_CODE) {
				CFBamBuffClearSubDep2 retbuff = ((CFBamBuffClearSubDep2)(schema.getFactoryClearSubDep2().newRec()));
				retbuff.set((ICFBamClearSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep3.CLASS_CODE) {
				CFBamBuffClearSubDep3 retbuff = ((CFBamBuffClearSubDep3)(schema.getFactoryClearSubDep3().newRec()));
				retbuff.set((ICFBamClearSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearTopDep.CLASS_CODE) {
				CFBamBuffClearTopDep retbuff = ((CFBamBuffClearTopDep)(schema.getFactoryClearTopDep().newRec()));
				retbuff.set((ICFBamClearTopDep)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamClearDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerived";
		ICFBamClearDep buff;
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
	public ICFBamClearDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearDep.lockDerived";
		ICFBamClearDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearDep.readAllDerived";
		ICFBamClearDep[] retList = new ICFBamClearDep[ dictByPKey.values().size() ];
		Iterator< CFBamBuffClearDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamClearDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearDep> filteredList = new ArrayList<ICFBamClearDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearDep ) ) {
					filteredList.add( (ICFBamClearDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearDep[0] ) );
		}
	}

	@Override
	public ICFBamClearDep[] readDerivedByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		CFBamBuffClearDepByClearDepIdxKey key = (CFBamBuffClearDepByClearDepIdxKey)schema.getFactoryClearDep().newByClearDepIdxKey();

		key.setRequiredRelationId( RelationId );
		ICFBamClearDep[] recArray;
		if( dictByClearDepIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearDep > subdictClearDepIdx
				= dictByClearDepIdx.get( key );
			recArray = new ICFBamClearDep[ subdictClearDepIdx.size() ];
			Iterator< CFBamBuffClearDep > iter = subdictClearDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearDep > subdictClearDepIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearDep >();
			dictByClearDepIdx.put( key, subdictClearDepIdx );
			recArray = new ICFBamClearDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamClearDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		CFBamBuffClearDepByDefSchemaIdxKey key = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getFactoryClearDep().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamClearDep[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearDep > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamClearDep[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffClearDep > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearDep > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearDep >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamClearDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamClearDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamClearDep buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearDep readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearDep.readRec";
		ICFBamClearDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearDep lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamClearDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearDep[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearDep.readAllRec";
		ICFBamClearDep buff;
		ArrayList<ICFBamClearDep> filteredList = new ArrayList<ICFBamClearDep>();
		ICFBamClearDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearDep[0] ) );
	}

	@Override
	public ICFBamClearDep readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamClearDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamClearDep)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamClearDep[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamClearDep buff;
		ArrayList<ICFBamClearDep> filteredList = new ArrayList<ICFBamClearDep>();
		ICFBamClearDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearDep[0] ) );
	}

	@Override
	public ICFBamClearDep[] readRecByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByClearDepIdx() ";
		ICFBamClearDep buff;
		ArrayList<ICFBamClearDep> filteredList = new ArrayList<ICFBamClearDep>();
		ICFBamClearDep[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearDep[0] ) );
	}

	@Override
	public ICFBamClearDep[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByDefSchemaIdx() ";
		ICFBamClearDep buff;
		ArrayList<ICFBamClearDep> filteredList = new ArrayList<ICFBamClearDep>();
		ICFBamClearDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearDep[0] ) );
	}

	public ICFBamClearDep updateClearDep( ICFSecAuthorization Authorization,
		ICFBamClearDep iBuff )
	{
		CFBamBuffClearDep Buff = (CFBamBuffClearDep)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearDep",
				"Existing record not found",
				"Existing record not found",
				"ClearDep",
				"ClearDep",
				pkey );
		}
		CFBamBuffClearDepByClearDepIdxKey existingKeyClearDepIdx = (CFBamBuffClearDepByClearDepIdxKey)schema.getFactoryClearDep().newByClearDepIdxKey();
		existingKeyClearDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffClearDepByClearDepIdxKey newKeyClearDepIdx = (CFBamBuffClearDepByClearDepIdxKey)schema.getFactoryClearDep().newByClearDepIdxKey();
		newKeyClearDepIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffClearDepByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getFactoryClearDep().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffClearDepByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getFactoryClearDep().newByDefSchemaIdxKey();
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
						"updateClearDep",
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
						"updateClearDep",
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

		Map< CFLibDbKeyHash256, CFBamBuffClearDep > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearDepIdx.get( existingKeyClearDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearDepIdx.containsKey( newKeyClearDepIdx ) ) {
			subdict = dictByClearDepIdx.get( newKeyClearDepIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearDep >();
			dictByClearDepIdx.put( newKeyClearDepIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearDep >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteClearDep( ICFSecAuthorization Authorization,
		ICFBamClearDep iBuff )
	{
		final String S_ProcName = "CFBamRamClearDepTable.deleteClearDep() ";
		CFBamBuffClearDep Buff = (CFBamBuffClearDep)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffClearDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearDep",
				pkey );
		}
		CFBamBuffClearDepByClearDepIdxKey keyClearDepIdx = (CFBamBuffClearDepByClearDepIdxKey)schema.getFactoryClearDep().newByClearDepIdxKey();
		keyClearDepIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffClearDepByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getFactoryClearDep().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteClearDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ClearSubDep1",
				"ClearSubDep1",
				pkey );
		}

		if( schema.getTableClearSubDep2().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteClearDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ClearSubDep2",
				"ClearSubDep2",
				pkey );
		}

		if( schema.getTableClearSubDep3().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteClearDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ClearSubDep3",
				"ClearSubDep3",
				pkey );
		}

		if( schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteClearDep",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ClearTopDep",
				"ClearTopDep",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffClearDep > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearDepIdx.get( keyClearDepIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deleteClearDepByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffClearDepByClearDepIdxKey key = (CFBamBuffClearDepByClearDepIdxKey)schema.getFactoryClearDep().newByClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearDepByClearDepIdx( Authorization, key );
	}

	@Override
	public void deleteClearDepByClearDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByClearDepIdxKey argKey )
	{
		final String S_ProcName = "deleteClearDepByClearDepIdx";
		CFBamBuffClearDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearDep> matchSet = new LinkedList<CFBamBuffClearDep>();
		Iterator<CFBamBuffClearDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearDep)(schema.getTableClearDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamClearDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearDep().deleteClearDep( Authorization, cur );
			}
			else if( ICFBamClearSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( ICFBamClearSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( ICFBamClearSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( ICFBamClearTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteClearDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffClearDepByDefSchemaIdxKey key = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getFactoryClearDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearDepByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteClearDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteClearDepByDefSchemaIdx";
		CFBamBuffClearDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearDep> matchSet = new LinkedList<CFBamBuffClearDep>();
		Iterator<CFBamBuffClearDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearDep)(schema.getTableClearDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamClearDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearDep().deleteClearDep( Authorization, cur );
			}
			else if( ICFBamClearSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( ICFBamClearSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( ICFBamClearSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( ICFBamClearTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteClearDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteClearDepByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffClearDep cur;
		LinkedList<CFBamBuffClearDep> matchSet = new LinkedList<CFBamBuffClearDep>();
		Iterator<CFBamBuffClearDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearDep)(schema.getTableClearDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamClearDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearDep().deleteClearDep( Authorization, cur );
			}
			else if( ICFBamClearSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( ICFBamClearSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( ICFBamClearSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( ICFBamClearTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteClearDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearDepByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteClearDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteClearDepByTenantIdx";
		CFBamBuffClearDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearDep> matchSet = new LinkedList<CFBamBuffClearDep>();
		Iterator<CFBamBuffClearDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearDep)(schema.getTableClearDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamClearDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearDep().deleteClearDep( Authorization, cur );
			}
			else if( ICFBamClearSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( ICFBamClearSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( ICFBamClearSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( ICFBamClearTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
