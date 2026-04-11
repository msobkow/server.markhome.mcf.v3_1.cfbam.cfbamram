
// Description: Java 25 in-memory RAM DbIO implementation for Tweak.

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
 *	CFBamRamTweakTable in-memory RAM DbIO implementation
 *	for Tweak.
 */
public class CFBamRamTweakTable
	implements ICFBamTweakTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffTweak > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffTweak >();
	private Map< CFBamBuffTweakByUNameIdxKey,
			CFBamBuffTweak > dictByUNameIdx
		= new HashMap< CFBamBuffTweakByUNameIdxKey,
			CFBamBuffTweak >();
	private Map< CFBamBuffTweakByValTentIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTweak >> dictByValTentIdx
		= new HashMap< CFBamBuffTweakByValTentIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTweak >>();
	private Map< CFBamBuffTweakByScopeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTweak >> dictByScopeIdx
		= new HashMap< CFBamBuffTweakByScopeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTweak >>();
	private Map< CFBamBuffTweakByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTweak >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffTweakByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTweak >>();
	private Map< CFBamBuffTweakByUDefIdxKey,
			CFBamBuffTweak > dictByUDefIdx
		= new HashMap< CFBamBuffTweakByUDefIdxKey,
			CFBamBuffTweak >();

	public CFBamRamTweakTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffTweak ensureRec(ICFBamTweak rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			if (classCode == ICFBamTweak.CLASS_CODE) {
				return( ((CFBamBuffTweakDefaultFactory)(schema.getFactoryTweak())).ensureRec((ICFBamTweak)rec) );
			}
			else if (classCode == ICFBamTableTweak.CLASS_CODE) {
				return( ((CFBamBuffTableTweakDefaultFactory)(schema.getFactoryTableTweak())).ensureRec((ICFBamTableTweak)rec) );
			}
			else if (classCode == ICFBamSchemaTweak.CLASS_CODE) {
				return( ((CFBamBuffSchemaTweakDefaultFactory)(schema.getFactorySchemaTweak())).ensureRec((ICFBamSchemaTweak)rec) );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), "ensureRec", "rec", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamTweak createTweak( ICFSecAuthorization Authorization,
		ICFBamTweak iBuff )
	{
		final String S_ProcName = "createTweak";
		
		CFBamBuffTweak Buff = (CFBamBuffTweak)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextTweakIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffTweakByUNameIdxKey keyUNameIdx = (CFBamBuffTweakByUNameIdxKey)schema.getFactoryTweak().newByUNameIdxKey();
		keyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffTweakByValTentIdxKey keyValTentIdx = (CFBamBuffTweakByValTentIdxKey)schema.getFactoryTweak().newByValTentIdxKey();
		keyValTentIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamBuffTweakByScopeIdxKey keyScopeIdx = (CFBamBuffTweakByScopeIdxKey)schema.getFactoryTweak().newByScopeIdxKey();
		keyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamBuffTweakByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffTweakByDefSchemaIdxKey)schema.getFactoryTweak().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffTweakByUDefIdxKey keyUDefIdx = (CFBamBuffTweakByUDefIdxKey)schema.getFactoryTweak().newByUDefIdxKey();
		keyUDefIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUDefIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyUDefIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyUDefIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );
		keyUDefIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"TweakUNameIdx",
				"TweakUNameIdx",
				keyUNameIdx );
		}

		if( dictByUDefIdx.containsKey( keyUDefIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"TweakUDefIdx",
				"TweakUDefIdx",
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

		Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictValTentIdx;
		if( dictByValTentIdx.containsKey( keyValTentIdx ) ) {
			subdictValTentIdx = dictByValTentIdx.get( keyValTentIdx );
		}
		else {
			subdictValTentIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
			dictByValTentIdx.put( keyValTentIdx, subdictValTentIdx );
		}
		subdictValTentIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictScopeIdx;
		if( dictByScopeIdx.containsKey( keyScopeIdx ) ) {
			subdictScopeIdx = dictByScopeIdx.get( keyScopeIdx );
		}
		else {
			subdictScopeIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
			dictByScopeIdx.put( keyScopeIdx, subdictScopeIdx );
		}
		subdictScopeIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByUDefIdx.put( keyUDefIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamTweak.CLASS_CODE) {
				CFBamBuffTweak retbuff = ((CFBamBuffTweak)(schema.getFactoryTweak().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTableTweak.CLASS_CODE) {
				CFBamBuffTableTweak retbuff = ((CFBamBuffTableTweak)(schema.getFactoryTableTweak().newRec()));
				retbuff.set((ICFBamTableTweak)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamSchemaTweak.CLASS_CODE) {
				CFBamBuffSchemaTweak retbuff = ((CFBamBuffSchemaTweak)(schema.getFactorySchemaTweak().newRec()));
				retbuff.set((ICFBamSchemaTweak)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamTweak readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTweak.readDerived";
		ICFBamTweak buff;
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
	public ICFBamTweak lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTweak.lockDerived";
		ICFBamTweak buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTweak[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamTweak.readAllDerived";
		ICFBamTweak[] retList = new ICFBamTweak[ dictByPKey.values().size() ];
		Iterator< CFBamBuffTweak > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamTweak readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByUNameIdx";
		CFBamBuffTweakByUNameIdxKey key = (CFBamBuffTweakByUNameIdxKey)schema.getFactoryTweak().newByUNameIdxKey();

		key.setRequiredScopeId( ScopeId );
		key.setRequiredName( Name );
		ICFBamTweak buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTweak[] readDerivedByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByValTentIdx";
		CFBamBuffTweakByValTentIdxKey key = (CFBamBuffTweakByValTentIdxKey)schema.getFactoryTweak().newByValTentIdxKey();

		key.setRequiredTenantId( TenantId );
		ICFBamTweak[] recArray;
		if( dictByValTentIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictValTentIdx
				= dictByValTentIdx.get( key );
			recArray = new ICFBamTweak[ subdictValTentIdx.size() ];
			Iterator< CFBamBuffTweak > iter = subdictValTentIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictValTentIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
			dictByValTentIdx.put( key, subdictValTentIdx );
			recArray = new ICFBamTweak[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTweak[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByScopeIdx";
		CFBamBuffTweakByScopeIdxKey key = (CFBamBuffTweakByScopeIdxKey)schema.getFactoryTweak().newByScopeIdxKey();

		key.setRequiredScopeId( ScopeId );
		ICFBamTweak[] recArray;
		if( dictByScopeIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictScopeIdx
				= dictByScopeIdx.get( key );
			recArray = new ICFBamTweak[ subdictScopeIdx.size() ];
			Iterator< CFBamBuffTweak > iter = subdictScopeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictScopeIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
			dictByScopeIdx.put( key, subdictScopeIdx );
			recArray = new ICFBamTweak[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTweak[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByDefSchemaIdx";
		CFBamBuffTweakByDefSchemaIdxKey key = (CFBamBuffTweakByDefSchemaIdxKey)schema.getFactoryTweak().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamTweak[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamTweak[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffTweak > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTweak > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamTweak[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTweak readDerivedByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaTenantId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByUDefIdx";
		CFBamBuffTweakByUDefIdxKey key = (CFBamBuffTweakByUDefIdxKey)schema.getFactoryTweak().newByUDefIdxKey();

		key.setRequiredTenantId( TenantId );
		key.setRequiredScopeId( ScopeId );
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );
		key.setRequiredName( Name );
		ICFBamTweak buff;
		if( dictByUDefIdx.containsKey( key ) ) {
			buff = dictByUDefIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTweak readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByIdIdx() ";
		ICFBamTweak buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTweak readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTweak.readRec";
		ICFBamTweak buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamTweak.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTweak lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamTweak buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamTweak.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTweak[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamTweak.readAllRec";
		ICFBamTweak buff;
		ArrayList<ICFBamTweak> filteredList = new ArrayList<ICFBamTweak>();
		ICFBamTweak[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamTweak[0] ) );
	}

	@Override
	public ICFBamTweak readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByIdIdx() ";
		ICFBamTweak buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTweak readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByUNameIdx() ";
		ICFBamTweak buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTweak[] readRecByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByValTentIdx() ";
		ICFBamTweak buff;
		ArrayList<ICFBamTweak> filteredList = new ArrayList<ICFBamTweak>();
		ICFBamTweak[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTweak[0] ) );
	}

	@Override
	public ICFBamTweak[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByScopeIdx() ";
		ICFBamTweak buff;
		ArrayList<ICFBamTweak> filteredList = new ArrayList<ICFBamTweak>();
		ICFBamTweak[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTweak[0] ) );
	}

	@Override
	public ICFBamTweak[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByDefSchemaIdx() ";
		ICFBamTweak buff;
		ArrayList<ICFBamTweak> filteredList = new ArrayList<ICFBamTweak>();
		ICFBamTweak[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTweak[0] ) );
	}

	@Override
	public ICFBamTweak readRecByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaTenantId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByUDefIdx() ";
		ICFBamTweak buff = readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamTweak)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamTweak updateTweak( ICFSecAuthorization Authorization,
		ICFBamTweak iBuff )
	{
		CFBamBuffTweak Buff = (CFBamBuffTweak)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		CFBamBuffTweak existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateTweak",
				"Existing record not found",
				"Existing record not found",
				"Tweak",
				"Tweak",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateTweak",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffTweakByUNameIdxKey existingKeyUNameIdx = (CFBamBuffTweakByUNameIdxKey)schema.getFactoryTweak().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffTweakByUNameIdxKey newKeyUNameIdx = (CFBamBuffTweakByUNameIdxKey)schema.getFactoryTweak().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffTweakByValTentIdxKey existingKeyValTentIdx = (CFBamBuffTweakByValTentIdxKey)schema.getFactoryTweak().newByValTentIdxKey();
		existingKeyValTentIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamBuffTweakByValTentIdxKey newKeyValTentIdx = (CFBamBuffTweakByValTentIdxKey)schema.getFactoryTweak().newByValTentIdxKey();
		newKeyValTentIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamBuffTweakByScopeIdxKey existingKeyScopeIdx = (CFBamBuffTweakByScopeIdxKey)schema.getFactoryTweak().newByScopeIdxKey();
		existingKeyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamBuffTweakByScopeIdxKey newKeyScopeIdx = (CFBamBuffTweakByScopeIdxKey)schema.getFactoryTweak().newByScopeIdxKey();
		newKeyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamBuffTweakByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffTweakByDefSchemaIdxKey)schema.getFactoryTweak().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffTweakByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffTweakByDefSchemaIdxKey)schema.getFactoryTweak().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffTweakByUDefIdxKey existingKeyUDefIdx = (CFBamBuffTweakByUDefIdxKey)schema.getFactoryTweak().newByUDefIdxKey();
		existingKeyUDefIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUDefIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyUDefIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyUDefIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );
		existingKeyUDefIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffTweakByUDefIdxKey newKeyUDefIdx = (CFBamBuffTweakByUDefIdxKey)schema.getFactoryTweak().newByUDefIdxKey();
		newKeyUDefIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUDefIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyUDefIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyUDefIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );
		newKeyUDefIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateTweak",
					"TweakUNameIdx",
					"TweakUNameIdx",
					newKeyUNameIdx );
			}
		}

		if( ! existingKeyUDefIdx.equals( newKeyUDefIdx ) ) {
			if( dictByUDefIdx.containsKey( newKeyUDefIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateTweak",
					"TweakUDefIdx",
					"TweakUDefIdx",
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
						"updateTweak",
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

		Map< CFLibDbKeyHash256, CFBamBuffTweak > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByValTentIdx.get( existingKeyValTentIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByValTentIdx.containsKey( newKeyValTentIdx ) ) {
			subdict = dictByValTentIdx.get( newKeyValTentIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
			dictByValTentIdx.put( newKeyValTentIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByScopeIdx.get( existingKeyScopeIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByScopeIdx.containsKey( newKeyScopeIdx ) ) {
			subdict = dictByScopeIdx.get( newKeyScopeIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTweak >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUDefIdx.remove( existingKeyUDefIdx );
		dictByUDefIdx.put( newKeyUDefIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteTweak( ICFSecAuthorization Authorization,
		ICFBamTweak iBuff )
	{
		final String S_ProcName = "CFBamRamTweakTable.deleteTweak() ";
		CFBamBuffTweak Buff = (CFBamBuffTweak)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffTweak existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteTweak",
				pkey );
		}
		CFBamBuffTweakByUNameIdxKey keyUNameIdx = (CFBamBuffTweakByUNameIdxKey)schema.getFactoryTweak().newByUNameIdxKey();
		keyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffTweakByValTentIdxKey keyValTentIdx = (CFBamBuffTweakByValTentIdxKey)schema.getFactoryTweak().newByValTentIdxKey();
		keyValTentIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamBuffTweakByScopeIdxKey keyScopeIdx = (CFBamBuffTweakByScopeIdxKey)schema.getFactoryTweak().newByScopeIdxKey();
		keyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamBuffTweakByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffTweakByDefSchemaIdxKey)schema.getFactoryTweak().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffTweakByUDefIdxKey keyUDefIdx = (CFBamBuffTweakByUDefIdxKey)schema.getFactoryTweak().newByUDefIdxKey();
		keyUDefIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUDefIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyUDefIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyUDefIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );
		keyUDefIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		if( schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteTweak",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"TableTweak",
				"TableTweak",
				pkey );
		}

		if( schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteTweak",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"SchemaTweak",
				"SchemaTweak",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffTweak > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByValTentIdx.get( keyValTentIdx );
		subdict.remove( pkey );

		subdict = dictByScopeIdx.get( keyScopeIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByUDefIdx.remove( keyUDefIdx );

	}
	@Override
	public void deleteTweakByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteTweakByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffTweak cur;
		LinkedList<CFBamBuffTweak> matchSet = new LinkedList<CFBamBuffTweak>();
		Iterator<CFBamBuffTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTweak)(schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( ICFBamTableTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (ICFBamTableTweak)cur );
			}
			else if( ICFBamSchemaTweak.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (ICFBamSchemaTweak)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteTweakByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffTweakByUNameIdxKey key = (CFBamBuffTweakByUNameIdxKey)schema.getFactoryTweak().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteTweakByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteTweakByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByUNameIdx";
		CFBamBuffTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTweak> matchSet = new LinkedList<CFBamBuffTweak>();
		Iterator<CFBamBuffTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTweak)(schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( ICFBamTableTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (ICFBamTableTweak)cur );
			}
			else if( ICFBamSchemaTweak.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (ICFBamSchemaTweak)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteTweakByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffTweakByValTentIdxKey key = (CFBamBuffTweakByValTentIdxKey)schema.getFactoryTweak().newByValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteTweakByValTentIdx( Authorization, key );
	}

	@Override
	public void deleteTweakByValTentIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByValTentIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByValTentIdx";
		CFBamBuffTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTweak> matchSet = new LinkedList<CFBamBuffTweak>();
		Iterator<CFBamBuffTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTweak)(schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( ICFBamTableTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (ICFBamTableTweak)cur );
			}
			else if( ICFBamSchemaTweak.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (ICFBamSchemaTweak)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteTweakByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffTweakByScopeIdxKey key = (CFBamBuffTweakByScopeIdxKey)schema.getFactoryTweak().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteTweakByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteTweakByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByScopeIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByScopeIdx";
		CFBamBuffTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTweak> matchSet = new LinkedList<CFBamBuffTweak>();
		Iterator<CFBamBuffTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTweak)(schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( ICFBamTableTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (ICFBamTableTweak)cur );
			}
			else if( ICFBamSchemaTweak.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (ICFBamSchemaTweak)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteTweakByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffTweakByDefSchemaIdxKey key = (CFBamBuffTweakByDefSchemaIdxKey)schema.getFactoryTweak().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteTweakByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteTweakByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByDefSchemaIdx";
		CFBamBuffTweak cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTweak> matchSet = new LinkedList<CFBamBuffTweak>();
		Iterator<CFBamBuffTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTweak)(schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( ICFBamTableTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (ICFBamTableTweak)cur );
			}
			else if( ICFBamSchemaTweak.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (ICFBamSchemaTweak)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteTweakByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argDefSchemaTenantId,
		CFLibDbKeyHash256 argDefSchemaId,
		String argName )
	{
		CFBamBuffTweakByUDefIdxKey key = (CFBamBuffTweakByUDefIdxKey)schema.getFactoryTweak().newByUDefIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		key.setRequiredName( argName );
		deleteTweakByUDefIdx( Authorization, key );
	}

	@Override
	public void deleteTweakByUDefIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByUDefIdxKey argKey )
	{
		final String S_ProcName = "deleteTweakByUDefIdx";
		CFBamBuffTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( argKey.getOptionalDefSchemaTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTweak> matchSet = new LinkedList<CFBamBuffTweak>();
		Iterator<CFBamBuffTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTweak)(schema.getTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTweak().deleteTweak( Authorization, cur );
			}
			else if( ICFBamTableTweak.CLASS_CODE == subClassCode ) {
				schema.getTableTableTweak().deleteTableTweak( Authorization, (ICFBamTableTweak)cur );
			}
			else if( ICFBamSchemaTweak.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaTweak().deleteSchemaTweak( Authorization, (ICFBamSchemaTweak)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
