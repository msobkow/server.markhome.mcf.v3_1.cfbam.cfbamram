
// Description: Java 25 in-memory RAM DbIO implementation for RoleDef.

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
 *	CFBamRamRoleDefTable in-memory RAM DbIO implementation
 *	for RoleDef.
 */
public class CFBamRamRoleDefTable
	implements ICFBamRoleDefTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffRoleDef > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffRoleDef >();
	private Map< CFBamBuffRoleDefByUNameIdxKey,
			CFBamBuffRoleDef > dictByUNameIdx
		= new HashMap< CFBamBuffRoleDefByUNameIdxKey,
			CFBamBuffRoleDef >();
	private Map< CFBamBuffRoleDefByScopeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRoleDef >> dictByScopeIdx
		= new HashMap< CFBamBuffRoleDefByScopeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRoleDef >>();
	private Map< CFBamBuffRoleDefByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRoleDef >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffRoleDefByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRoleDef >>();
	private Map< CFBamBuffRoleDefByUDefIdxKey,
			CFBamBuffRoleDef > dictByUDefIdx
		= new HashMap< CFBamBuffRoleDefByUDefIdxKey,
			CFBamBuffRoleDef >();

	public CFBamRamRoleDefTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffRoleDef ensureRec(ICFBamRoleDef rec) {
		return (((CFBamBuffRoleDefFactoryService)(schema.getCFBamBuffFactory().getFactoryRoleDef())).ensureRec(rec));
	}

	@Override
	public ICFBamRoleDef createRoleDef( ICFSecAuthorization Authorization,
		ICFBamRoleDef iBuff )
	{
		final String S_ProcName = "createRoleDef";
		
		CFBamBuffRoleDef Buff = (CFBamBuffRoleDef)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextRoleIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffRoleDefByUNameIdxKey keyUNameIdx = (CFBamBuffRoleDefByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUNameIdxKey();
		keyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffRoleDefByScopeIdxKey keyScopeIdx = (CFBamBuffRoleDefByScopeIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByScopeIdxKey();
		keyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamBuffRoleDefByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffRoleDefByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffRoleDefByUDefIdxKey keyUDefIdx = (CFBamBuffRoleDefByUDefIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUDefIdxKey();
		keyUDefIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyUDefIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );
		keyUDefIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"RoleDefUNameIdx",
				"RoleDefUNameIdx",
				keyUNameIdx );
		}

		if( dictByUDefIdx.containsKey( keyUDefIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"RoleDefUDefIdx",
				"RoleDefUDefIdx",
				keyUDefIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredScopeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"Scope",
						"Scope",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRoleDef > subdictScopeIdx;
		if( dictByScopeIdx.containsKey( keyScopeIdx ) ) {
			subdictScopeIdx = dictByScopeIdx.get( keyScopeIdx );
		}
		else {
			subdictScopeIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRoleDef >();
			dictByScopeIdx.put( keyScopeIdx, subdictScopeIdx );
		}
		subdictScopeIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRoleDef > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRoleDef >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByUDefIdx.put( keyUDefIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamRoleDef.CLASS_CODE) {
				CFBamBuffRoleDef retbuff = ((CFBamBuffRoleDef)(schema.getCFBamBuffFactory().getFactoryRoleDef().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamSchemaRole.CLASS_CODE) {
				CFBamBuffSchemaRole retbuff = ((CFBamBuffSchemaRole)(schema.getCFBamBuffFactory().getFactorySchemaRole().newRec()));
				retbuff.set((ICFBamSchemaRole)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamRoleDef readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerived";
		ICFBamRoleDef buff;
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
	public ICFBamRoleDef lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRoleDef.lockDerived";
		ICFBamRoleDef buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRoleDef[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamRoleDef.readAllDerived";
		ICFBamRoleDef[] retList = new ICFBamRoleDef[ dictByPKey.values().size() ];
		Iterator< CFBamBuffRoleDef > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamRoleDef readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByUNameIdx";
		CFBamBuffRoleDefByUNameIdxKey key = (CFBamBuffRoleDefByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUNameIdxKey();

		key.setRequiredScopeId( ScopeId );
		key.setRequiredName( Name );
		ICFBamRoleDef buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRoleDef[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByScopeIdx";
		CFBamBuffRoleDefByScopeIdxKey key = (CFBamBuffRoleDefByScopeIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByScopeIdxKey();

		key.setRequiredScopeId( ScopeId );
		ICFBamRoleDef[] recArray;
		if( dictByScopeIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRoleDef > subdictScopeIdx
				= dictByScopeIdx.get( key );
			recArray = new ICFBamRoleDef[ subdictScopeIdx.size() ];
			Iterator< CFBamBuffRoleDef > iter = subdictScopeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRoleDef > subdictScopeIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRoleDef >();
			dictByScopeIdx.put( key, subdictScopeIdx );
			recArray = new ICFBamRoleDef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRoleDef[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByDefSchemaIdx";
		CFBamBuffRoleDefByDefSchemaIdxKey key = (CFBamBuffRoleDefByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamRoleDef[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRoleDef > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamRoleDef[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffRoleDef > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRoleDef > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRoleDef >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamRoleDef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRoleDef readDerivedByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByUDefIdx";
		CFBamBuffRoleDefByUDefIdxKey key = (CFBamBuffRoleDefByUDefIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUDefIdxKey();

		key.setRequiredScopeId( ScopeId );
		key.setOptionalDefSchemaId( DefSchemaId );
		key.setRequiredName( Name );
		ICFBamRoleDef buff;
		if( dictByUDefIdx.containsKey( key ) ) {
			buff = dictByUDefIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRoleDef readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByIdIdx() ";
		ICFBamRoleDef buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRoleDef readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRec";
		ICFBamRoleDef buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamRoleDef.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRoleDef lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamRoleDef buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamRoleDef.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRoleDef[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamRoleDef.readAllRec";
		ICFBamRoleDef buff;
		ArrayList<ICFBamRoleDef> filteredList = new ArrayList<ICFBamRoleDef>();
		ICFBamRoleDef[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamRoleDef[0] ) );
	}

	@Override
	public ICFBamRoleDef readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByIdIdx() ";
		ICFBamRoleDef buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
			return( (ICFBamRoleDef)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamRoleDef readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByUNameIdx() ";
		ICFBamRoleDef buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
			return( (ICFBamRoleDef)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamRoleDef[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByScopeIdx() ";
		ICFBamRoleDef buff;
		ArrayList<ICFBamRoleDef> filteredList = new ArrayList<ICFBamRoleDef>();
		ICFBamRoleDef[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRoleDef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRoleDef[0] ) );
	}

	@Override
	public ICFBamRoleDef[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByDefSchemaIdx() ";
		ICFBamRoleDef buff;
		ArrayList<ICFBamRoleDef> filteredList = new ArrayList<ICFBamRoleDef>();
		ICFBamRoleDef[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRoleDef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRoleDef[0] ) );
	}

	@Override
	public ICFBamRoleDef readRecByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByUDefIdx() ";
		ICFBamRoleDef buff = readDerivedByUDefIdx( Authorization,
			ScopeId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
			return( (ICFBamRoleDef)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamRoleDef updateRoleDef( ICFSecAuthorization Authorization,
		ICFBamRoleDef iBuff )
	{
		CFBamBuffRoleDef Buff = (CFBamBuffRoleDef)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffRoleDef existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateRoleDef",
				"Existing record not found",
				"Existing record not found",
				"RoleDef",
				"RoleDef",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateRoleDef",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffRoleDefByUNameIdxKey existingKeyUNameIdx = (CFBamBuffRoleDefByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRoleDefByUNameIdxKey newKeyUNameIdx = (CFBamBuffRoleDefByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffRoleDefByScopeIdxKey existingKeyScopeIdx = (CFBamBuffRoleDefByScopeIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByScopeIdxKey();
		existingKeyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamBuffRoleDefByScopeIdxKey newKeyScopeIdx = (CFBamBuffRoleDefByScopeIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByScopeIdxKey();
		newKeyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamBuffRoleDefByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffRoleDefByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffRoleDefByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffRoleDefByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffRoleDefByUDefIdxKey existingKeyUDefIdx = (CFBamBuffRoleDefByUDefIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUDefIdxKey();
		existingKeyUDefIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyUDefIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );
		existingKeyUDefIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRoleDefByUDefIdxKey newKeyUDefIdx = (CFBamBuffRoleDefByUDefIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUDefIdxKey();
		newKeyUDefIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyUDefIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );
		newKeyUDefIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRoleDef",
					"RoleDefUNameIdx",
					"RoleDefUNameIdx",
					newKeyUNameIdx );
			}
		}

		if( ! existingKeyUDefIdx.equals( newKeyUDefIdx ) ) {
			if( dictByUDefIdx.containsKey( newKeyUDefIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRoleDef",
					"RoleDefUDefIdx",
					"RoleDefUDefIdx",
					newKeyUDefIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredScopeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRoleDef",
						"Container",
						"Container",
						"Scope",
						"Scope",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffRoleDef > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByScopeIdx.get( existingKeyScopeIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByScopeIdx.containsKey( newKeyScopeIdx ) ) {
			subdict = dictByScopeIdx.get( newKeyScopeIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRoleDef >();
			dictByScopeIdx.put( newKeyScopeIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRoleDef >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUDefIdx.remove( existingKeyUDefIdx );
		dictByUDefIdx.put( newKeyUDefIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteRoleDef( ICFSecAuthorization Authorization,
		ICFBamRoleDef iBuff )
	{
		final String S_ProcName = "CFBamRamRoleDefTable.deleteRoleDef() ";
		CFBamBuffRoleDef Buff = (CFBamBuffRoleDef)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffRoleDef existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteRoleDef",
				pkey );
		}
		CFBamBuffRoleDefByUNameIdxKey keyUNameIdx = (CFBamBuffRoleDefByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUNameIdxKey();
		keyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRoleDefByScopeIdxKey keyScopeIdx = (CFBamBuffRoleDefByScopeIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByScopeIdxKey();
		keyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamBuffRoleDefByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffRoleDefByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffRoleDefByUDefIdxKey keyUDefIdx = (CFBamBuffRoleDefByUDefIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUDefIdxKey();
		keyUDefIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyUDefIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );
		keyUDefIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		if( schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRoleDef",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"SchemaRole",
				"SchemaRole",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffRoleDef > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByScopeIdx.get( keyScopeIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByUDefIdx.remove( keyUDefIdx );

	}
	@Override
	public void deleteRoleDefByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteRoleDefByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffRoleDef cur;
		LinkedList<CFBamBuffRoleDef> matchSet = new LinkedList<CFBamBuffRoleDef>();
		Iterator<CFBamBuffRoleDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRoleDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRoleDef)(schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamRoleDef.CLASS_CODE == subClassCode ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( ICFBamSchemaRole.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (ICFBamSchemaRole)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteRoleDefByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffRoleDefByUNameIdxKey key = (CFBamBuffRoleDefByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteRoleDefByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteRoleDefByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamRoleDefByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByUNameIdx";
		CFBamBuffRoleDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRoleDef> matchSet = new LinkedList<CFBamBuffRoleDef>();
		Iterator<CFBamBuffRoleDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRoleDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRoleDef)(schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamRoleDef.CLASS_CODE == subClassCode ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( ICFBamSchemaRole.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (ICFBamSchemaRole)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteRoleDefByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffRoleDefByScopeIdxKey key = (CFBamBuffRoleDefByScopeIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteRoleDefByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteRoleDefByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamRoleDefByScopeIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByScopeIdx";
		CFBamBuffRoleDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRoleDef> matchSet = new LinkedList<CFBamBuffRoleDef>();
		Iterator<CFBamBuffRoleDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRoleDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRoleDef)(schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamRoleDef.CLASS_CODE == subClassCode ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( ICFBamSchemaRole.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (ICFBamSchemaRole)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteRoleDefByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffRoleDefByDefSchemaIdxKey key = (CFBamBuffRoleDefByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteRoleDefByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteRoleDefByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamRoleDefByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByDefSchemaIdx";
		CFBamBuffRoleDef cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRoleDef> matchSet = new LinkedList<CFBamBuffRoleDef>();
		Iterator<CFBamBuffRoleDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRoleDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRoleDef)(schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamRoleDef.CLASS_CODE == subClassCode ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( ICFBamSchemaRole.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (ICFBamSchemaRole)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteRoleDefByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argDefSchemaId,
		String argName )
	{
		CFBamBuffRoleDefByUDefIdxKey key = (CFBamBuffRoleDefByUDefIdxKey)schema.getCFBamBuffFactory().getFactoryRoleDef().newByUDefIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		key.setRequiredName( argName );
		deleteRoleDefByUDefIdx( Authorization, key );
	}

	@Override
	public void deleteRoleDefByUDefIdx( ICFSecAuthorization Authorization,
		ICFBamRoleDefByUDefIdxKey argKey )
	{
		final String S_ProcName = "deleteRoleDefByUDefIdx";
		CFBamBuffRoleDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRoleDef> matchSet = new LinkedList<CFBamBuffRoleDef>();
		Iterator<CFBamBuffRoleDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRoleDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRoleDef)(schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamRoleDef.CLASS_CODE == subClassCode ) {
				schema.getTableRoleDef().deleteRoleDef( Authorization, cur );
			}
			else if( ICFBamSchemaRole.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRole().deleteSchemaRole( Authorization, (ICFBamSchemaRole)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
