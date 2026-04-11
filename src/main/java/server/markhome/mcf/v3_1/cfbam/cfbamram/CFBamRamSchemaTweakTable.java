
// Description: Java 25 in-memory RAM DbIO implementation for SchemaTweak.

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
 *	CFBamRamSchemaTweakTable in-memory RAM DbIO implementation
 *	for SchemaTweak.
 */
public class CFBamRamSchemaTweakTable
	implements ICFBamSchemaTweakTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffSchemaTweak > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffSchemaTweak >();
	private Map< CFBamBuffSchemaTweakBySchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaTweak >> dictBySchemaIdx
		= new HashMap< CFBamBuffSchemaTweakBySchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaTweak >>();

	public CFBamRamSchemaTweakTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffTweak ensureRec(ICFBamTweak rec) {
		if (rec == null) {
			return( null );
		}
		else {
			return ((CFBamRamTweakTable)(schema.getTableTweak())).ensureRec((ICFBamTweak)rec);
		}
	}

	@Override
	public ICFBamSchemaTweak createSchemaTweak( ICFSecAuthorization Authorization,
		ICFBamSchemaTweak iBuff )
	{
		final String S_ProcName = "createSchemaTweak";
		
		CFBamBuffSchemaTweak Buff = (CFBamBuffSchemaTweak)(schema.getTableTweak().createTweak( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffSchemaTweakBySchemaIdxKey keySchemaIdx = (CFBamBuffSchemaTweakBySchemaIdxKey)schema.getFactorySchemaTweak().newBySchemaIdxKey();
		keySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTweak().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"Tweak",
						"Tweak",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"Schema",
						"Schema",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaTweak > subdictSchemaIdx;
		if( dictBySchemaIdx.containsKey( keySchemaIdx ) ) {
			subdictSchemaIdx = dictBySchemaIdx.get( keySchemaIdx );
		}
		else {
			subdictSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaTweak >();
			dictBySchemaIdx.put( keySchemaIdx, subdictSchemaIdx );
		}
		subdictSchemaIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamSchemaTweak.CLASS_CODE) {
				CFBamBuffSchemaTweak retbuff = ((CFBamBuffSchemaTweak)(schema.getFactorySchemaTweak().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamSchemaTweak readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readDerived";
		ICFBamSchemaTweak buff;
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
	public ICFBamSchemaTweak lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.lockDerived";
		ICFBamSchemaTweak buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaTweak[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaTweak.readAllDerived";
		ICFBamSchemaTweak[] retList = new ICFBamSchemaTweak[ dictByPKey.values().size() ];
		Iterator< CFBamBuffSchemaTweak > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamSchemaTweak readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByUNameIdx";
		ICFBamTweak buff = schema.getTableTweak().readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamSchemaTweak ) {
			return( (ICFBamSchemaTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaTweak[] readDerivedByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByValTentIdx";
		ICFBamTweak buffList[] = schema.getTableTweak().readDerivedByValTentIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamTweak buff;
			ArrayList<ICFBamSchemaTweak> filteredList = new ArrayList<ICFBamSchemaTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamSchemaTweak ) ) {
					filteredList.add( (ICFBamSchemaTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamSchemaTweak[0] ) );
		}
	}

	@Override
	public ICFBamSchemaTweak[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByScopeIdx";
		ICFBamTweak buffList[] = schema.getTableTweak().readDerivedByScopeIdx( Authorization,
			ScopeId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamTweak buff;
			ArrayList<ICFBamSchemaTweak> filteredList = new ArrayList<ICFBamSchemaTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamSchemaTweak ) ) {
					filteredList.add( (ICFBamSchemaTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamSchemaTweak[0] ) );
		}
	}

	@Override
	public ICFBamSchemaTweak[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByDefSchemaIdx";
		ICFBamTweak buffList[] = schema.getTableTweak().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamTweak buff;
			ArrayList<ICFBamSchemaTweak> filteredList = new ArrayList<ICFBamSchemaTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamSchemaTweak ) ) {
					filteredList.add( (ICFBamSchemaTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamSchemaTweak[0] ) );
		}
	}

	@Override
	public ICFBamSchemaTweak readDerivedByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaTenantId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByUDefIdx";
		ICFBamTweak buff = schema.getTableTweak().readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamSchemaTweak ) {
			return( (ICFBamSchemaTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaTweak[] readDerivedBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readDerivedBySchemaIdx";
		CFBamBuffSchemaTweakBySchemaIdxKey key = (CFBamBuffSchemaTweakBySchemaIdxKey)schema.getFactorySchemaTweak().newBySchemaIdxKey();

		key.setRequiredSchemaDefId( SchemaDefId );
		ICFBamSchemaTweak[] recArray;
		if( dictBySchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaTweak > subdictSchemaIdx
				= dictBySchemaIdx.get( key );
			recArray = new ICFBamSchemaTweak[ subdictSchemaIdx.size() ];
			Iterator< CFBamBuffSchemaTweak > iter = subdictSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaTweak > subdictSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaTweak >();
			dictBySchemaIdx.put( key, subdictSchemaIdx );
			recArray = new ICFBamSchemaTweak[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaTweak readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByIdIdx() ";
		ICFBamSchemaTweak buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaTweak readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readRec";
		ICFBamSchemaTweak buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamSchemaTweak.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaTweak lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamSchemaTweak buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamSchemaTweak.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaTweak[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readAllRec";
		ICFBamSchemaTweak buff;
		ArrayList<ICFBamSchemaTweak> filteredList = new ArrayList<ICFBamSchemaTweak>();
		ICFBamSchemaTweak[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaTweak.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaTweak[0] ) );
	}

	@Override
	public ICFBamSchemaTweak readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByIdIdx() ";
		ICFBamSchemaTweak buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamSchemaTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaTweak readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByUNameIdx() ";
		ICFBamSchemaTweak buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamSchemaTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaTweak[] readRecByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByValTentIdx() ";
		ICFBamSchemaTweak buff;
		ArrayList<ICFBamSchemaTweak> filteredList = new ArrayList<ICFBamSchemaTweak>();
		ICFBamSchemaTweak[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaTweak[0] ) );
	}

	@Override
	public ICFBamSchemaTweak[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByScopeIdx() ";
		ICFBamSchemaTweak buff;
		ArrayList<ICFBamSchemaTweak> filteredList = new ArrayList<ICFBamSchemaTweak>();
		ICFBamSchemaTweak[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaTweak[0] ) );
	}

	@Override
	public ICFBamSchemaTweak[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByDefSchemaIdx() ";
		ICFBamSchemaTweak buff;
		ArrayList<ICFBamSchemaTweak> filteredList = new ArrayList<ICFBamSchemaTweak>();
		ICFBamSchemaTweak[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaTweak[0] ) );
	}

	@Override
	public ICFBamSchemaTweak readRecByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaTenantId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByUDefIdx() ";
		ICFBamSchemaTweak buff = readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamSchemaTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaTweak[] readRecBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamSchemaTweak.readRecBySchemaIdx() ";
		ICFBamSchemaTweak buff;
		ArrayList<ICFBamSchemaTweak> filteredList = new ArrayList<ICFBamSchemaTweak>();
		ICFBamSchemaTweak[] buffList = readDerivedBySchemaIdx( Authorization,
			SchemaDefId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaTweak[0] ) );
	}

	public ICFBamSchemaTweak updateSchemaTweak( ICFSecAuthorization Authorization,
		ICFBamSchemaTweak iBuff )
	{
		CFBamBuffSchemaTweak Buff = (CFBamBuffSchemaTweak)(schema.getTableTweak().updateTweak( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		CFBamBuffSchemaTweak existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaTweak",
				"Existing record not found",
				"Existing record not found",
				"SchemaTweak",
				"SchemaTweak",
				pkey );
		}
		CFBamBuffSchemaTweakBySchemaIdxKey existingKeySchemaIdx = (CFBamBuffSchemaTweakBySchemaIdxKey)schema.getFactorySchemaTweak().newBySchemaIdxKey();
		existingKeySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamBuffSchemaTweakBySchemaIdxKey newKeySchemaIdx = (CFBamBuffSchemaTweakBySchemaIdxKey)schema.getFactorySchemaTweak().newBySchemaIdxKey();
		newKeySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTweak().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaTweak",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"Tweak",
						"Tweak",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaTweak",
						"Container",
						"Container",
						"Schema",
						"Schema",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffSchemaTweak > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictBySchemaIdx.get( existingKeySchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaIdx.containsKey( newKeySchemaIdx ) ) {
			subdict = dictBySchemaIdx.get( newKeySchemaIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaTweak >();
			dictBySchemaIdx.put( newKeySchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteSchemaTweak( ICFSecAuthorization Authorization,
		ICFBamSchemaTweak iBuff )
	{
		final String S_ProcName = "CFBamRamSchemaTweakTable.deleteSchemaTweak() ";
		CFBamBuffSchemaTweak Buff = (CFBamBuffSchemaTweak)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffSchemaTweak existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaTweak",
				pkey );
		}
		CFBamBuffSchemaTweakBySchemaIdxKey keySchemaIdx = (CFBamBuffSchemaTweakBySchemaIdxKey)schema.getFactorySchemaTweak().newBySchemaIdxKey();
		keySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffSchemaTweak > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaIdx.get( keySchemaIdx );
		subdict.remove( pkey );

		schema.getTableTweak().deleteTweak( Authorization,
			Buff );
	}
	@Override
	public void deleteSchemaTweakBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId )
	{
		CFBamBuffSchemaTweakBySchemaIdxKey key = (CFBamBuffSchemaTweakBySchemaIdxKey)schema.getFactorySchemaTweak().newBySchemaIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		deleteSchemaTweakBySchemaIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaTweakBySchemaIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaTweakBySchemaIdxKey argKey )
	{
		CFBamBuffSchemaTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaTweak> matchSet = new LinkedList<CFBamBuffSchemaTweak>();
		Iterator<CFBamBuffSchemaTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaTweak)(schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaTweakByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffSchemaTweak cur;
		LinkedList<CFBamBuffSchemaTweak> matchSet = new LinkedList<CFBamBuffSchemaTweak>();
		Iterator<CFBamBuffSchemaTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaTweak)(schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaTweakByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffTweakByUNameIdxKey key = (CFBamBuffTweakByUNameIdxKey)schema.getFactoryTweak().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteSchemaTweakByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaTweakByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByUNameIdxKey argKey )
	{
		CFBamBuffSchemaTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaTweak> matchSet = new LinkedList<CFBamBuffSchemaTweak>();
		Iterator<CFBamBuffSchemaTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaTweak)(schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaTweakByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffTweakByValTentIdxKey key = (CFBamBuffTweakByValTentIdxKey)schema.getFactoryTweak().newByValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteSchemaTweakByValTentIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaTweakByValTentIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByValTentIdxKey argKey )
	{
		CFBamBuffSchemaTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaTweak> matchSet = new LinkedList<CFBamBuffSchemaTweak>();
		Iterator<CFBamBuffSchemaTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaTweak)(schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaTweakByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffTweakByScopeIdxKey key = (CFBamBuffTweakByScopeIdxKey)schema.getFactoryTweak().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteSchemaTweakByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaTweakByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByScopeIdxKey argKey )
	{
		CFBamBuffSchemaTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaTweak> matchSet = new LinkedList<CFBamBuffSchemaTweak>();
		Iterator<CFBamBuffSchemaTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaTweak)(schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaTweakByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffTweakByDefSchemaIdxKey key = (CFBamBuffTweakByDefSchemaIdxKey)schema.getFactoryTweak().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteSchemaTweakByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaTweakByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByDefSchemaIdxKey argKey )
	{
		CFBamBuffSchemaTweak cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaTweak> matchSet = new LinkedList<CFBamBuffSchemaTweak>();
		Iterator<CFBamBuffSchemaTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaTweak)(schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaTweakByUDefIdx( ICFSecAuthorization Authorization,
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
		deleteSchemaTweakByUDefIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaTweakByUDefIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByUDefIdxKey argKey )
	{
		CFBamBuffSchemaTweak cur;
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
		LinkedList<CFBamBuffSchemaTweak> matchSet = new LinkedList<CFBamBuffSchemaTweak>();
		Iterator<CFBamBuffSchemaTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaTweak)(schema.getTableSchemaTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaTweak( Authorization, cur );
		}
	}
}
