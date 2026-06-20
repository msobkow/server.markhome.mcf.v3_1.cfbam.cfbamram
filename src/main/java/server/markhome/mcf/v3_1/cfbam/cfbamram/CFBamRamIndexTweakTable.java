
// Description: Java 25 in-memory RAM DbIO implementation for IndexTweak.

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
 *	CFBamRamIndexTweakTable in-memory RAM DbIO implementation
 *	for IndexTweak.
 */
public class CFBamRamIndexTweakTable
	implements ICFBamIndexTweakTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffIndexTweak > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffIndexTweak >();
	private Map< CFBamBuffIndexTweakByIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexTweak >> dictByIndexIdx
		= new HashMap< CFBamBuffIndexTweakByIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexTweak >>();

	public CFBamRamIndexTweakTable( ICFBamSchema argSchema ) {
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
	public ICFBamIndexTweak createIndexTweak( ICFSecAuthorization Authorization,
		ICFBamIndexTweak iBuff )
	{
		final String S_ProcName = "createIndexTweak";
		
		CFBamBuffIndexTweak Buff = (CFBamBuffIndexTweak)(schema.getTableTweak().createTweak( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffIndexTweakByIndexIdxKey keyIndexIdx = (CFBamBuffIndexTweakByIndexIdxKey)schema.getCFBamFactory().getFactoryIndexTweak().newByIndexIdxKey();
		keyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

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
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"Index",
						"Index",
						"Index",
						"Index",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexTweak > subdictIndexIdx;
		if( dictByIndexIdx.containsKey( keyIndexIdx ) ) {
			subdictIndexIdx = dictByIndexIdx.get( keyIndexIdx );
		}
		else {
			subdictIndexIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexTweak >();
			dictByIndexIdx.put( keyIndexIdx, subdictIndexIdx );
		}
		subdictIndexIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamIndexTweak.CLASS_CODE) {
				CFBamBuffIndexTweak retbuff = ((CFBamBuffIndexTweak)(schema.getCFBamFactory().getFactoryIndexTweak().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamIndexTweak readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readDerived";
		ICFBamIndexTweak buff;
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
	public ICFBamIndexTweak lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexTweak.lockDerived";
		ICFBamIndexTweak buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexTweak[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndexTweak.readAllDerived";
		ICFBamIndexTweak[] retList = new ICFBamIndexTweak[ dictByPKey.values().size() ];
		Iterator< CFBamBuffIndexTweak > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamIndexTweak readDerivedByUNameIdx( ICFSecAuthorization Authorization,
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
		else if( buff instanceof ICFBamIndexTweak ) {
			return( (ICFBamIndexTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndexTweak[] readDerivedByValTentIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamIndexTweak> filteredList = new ArrayList<ICFBamIndexTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamIndexTweak ) ) {
					filteredList.add( (ICFBamIndexTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamIndexTweak[0] ) );
		}
	}

	@Override
	public ICFBamIndexTweak[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamIndexTweak> filteredList = new ArrayList<ICFBamIndexTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamIndexTweak ) ) {
					filteredList.add( (ICFBamIndexTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamIndexTweak[0] ) );
		}
	}

	@Override
	public ICFBamIndexTweak[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamIndexTweak> filteredList = new ArrayList<ICFBamIndexTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamIndexTweak ) ) {
					filteredList.add( (ICFBamIndexTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamIndexTweak[0] ) );
		}
	}

	@Override
	public ICFBamIndexTweak readDerivedByUDefIdx( ICFSecAuthorization Authorization,
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
		else if( buff instanceof ICFBamIndexTweak ) {
			return( (ICFBamIndexTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndexTweak[] readDerivedByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readDerivedByIndexIdx";
		CFBamBuffIndexTweakByIndexIdxKey key = (CFBamBuffIndexTweakByIndexIdxKey)schema.getCFBamFactory().getFactoryIndexTweak().newByIndexIdxKey();

		key.setRequiredIndexId( IndexId );
		ICFBamIndexTweak[] recArray;
		if( dictByIndexIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexTweak > subdictIndexIdx
				= dictByIndexIdx.get( key );
			recArray = new ICFBamIndexTweak[ subdictIndexIdx.size() ];
			Iterator< CFBamBuffIndexTweak > iter = subdictIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexTweak > subdictIndexIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexTweak >();
			dictByIndexIdx.put( key, subdictIndexIdx );
			recArray = new ICFBamIndexTweak[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndexTweak readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByIdIdx() ";
		ICFBamIndexTweak buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexTweak readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readRec";
		ICFBamIndexTweak buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndexTweak.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexTweak lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamIndexTweak buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndexTweak.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexTweak[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readAllRec";
		ICFBamIndexTweak buff;
		ArrayList<ICFBamIndexTweak> filteredList = new ArrayList<ICFBamIndexTweak>();
		ICFBamIndexTweak[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexTweak.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexTweak[0] ) );
	}

	@Override
	public ICFBamIndexTweak readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByIdIdx() ";
		ICFBamIndexTweak buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamIndexTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndexTweak readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByUNameIdx() ";
		ICFBamIndexTweak buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamIndexTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndexTweak[] readRecByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByValTentIdx() ";
		ICFBamIndexTweak buff;
		ArrayList<ICFBamIndexTweak> filteredList = new ArrayList<ICFBamIndexTweak>();
		ICFBamIndexTweak[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexTweak[0] ) );
	}

	@Override
	public ICFBamIndexTweak[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByScopeIdx() ";
		ICFBamIndexTweak buff;
		ArrayList<ICFBamIndexTweak> filteredList = new ArrayList<ICFBamIndexTweak>();
		ICFBamIndexTweak[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexTweak[0] ) );
	}

	@Override
	public ICFBamIndexTweak[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByDefSchemaIdx() ";
		ICFBamIndexTweak buff;
		ArrayList<ICFBamIndexTweak> filteredList = new ArrayList<ICFBamIndexTweak>();
		ICFBamIndexTweak[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexTweak[0] ) );
	}

	@Override
	public ICFBamIndexTweak readRecByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaTenantId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByUDefIdx() ";
		ICFBamIndexTweak buff = readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamIndexTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndexTweak[] readRecByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId )
	{
		final String S_ProcName = "CFBamRamIndexTweak.readRecByIndexIdx() ";
		ICFBamIndexTweak buff;
		ArrayList<ICFBamIndexTweak> filteredList = new ArrayList<ICFBamIndexTweak>();
		ICFBamIndexTweak[] buffList = readDerivedByIndexIdx( Authorization,
			IndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexTweak[0] ) );
	}

	public ICFBamIndexTweak updateIndexTweak( ICFSecAuthorization Authorization,
		ICFBamIndexTweak iBuff )
	{
		CFBamBuffIndexTweak Buff = (CFBamBuffIndexTweak)(schema.getTableTweak().updateTweak( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffIndexTweak existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndexTweak",
				"Existing record not found",
				"Existing record not found",
				"IndexTweak",
				"IndexTweak",
				pkey );
		}
		CFBamBuffIndexTweakByIndexIdxKey existingKeyIndexIdx = (CFBamBuffIndexTweakByIndexIdxKey)schema.getCFBamFactory().getFactoryIndexTweak().newByIndexIdxKey();
		existingKeyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		CFBamBuffIndexTweakByIndexIdxKey newKeyIndexIdx = (CFBamBuffIndexTweakByIndexIdxKey)schema.getCFBamFactory().getFactoryIndexTweak().newByIndexIdxKey();
		newKeyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTweak().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexTweak",
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
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexTweak",
						"Container",
						"Container",
						"Index",
						"Index",
						"Index",
						"Index",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffIndexTweak > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByIndexIdx.get( existingKeyIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIndexIdx.containsKey( newKeyIndexIdx ) ) {
			subdict = dictByIndexIdx.get( newKeyIndexIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexTweak >();
			dictByIndexIdx.put( newKeyIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteIndexTweak( ICFSecAuthorization Authorization,
		ICFBamIndexTweak iBuff )
	{
		final String S_ProcName = "CFBamRamIndexTweakTable.deleteIndexTweak() ";
		CFBamBuffIndexTweak Buff = (CFBamBuffIndexTweak)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffIndexTweak existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteIndexTweak",
				pkey );
		}
		CFBamBuffIndexTweakByIndexIdxKey keyIndexIdx = (CFBamBuffIndexTweakByIndexIdxKey)schema.getCFBamFactory().getFactoryIndexTweak().newByIndexIdxKey();
		keyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffIndexTweak > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByIndexIdx.get( keyIndexIdx );
		subdict.remove( pkey );

		schema.getTableTweak().deleteTweak( Authorization,
			Buff );
	}
	@Override
	public void deleteIndexTweakByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId )
	{
		CFBamBuffIndexTweakByIndexIdxKey key = (CFBamBuffIndexTweakByIndexIdxKey)schema.getCFBamFactory().getFactoryIndexTweak().newByIndexIdxKey();
		key.setRequiredIndexId( argIndexId );
		deleteIndexTweakByIndexIdx( Authorization, key );
	}

	@Override
	public void deleteIndexTweakByIndexIdx( ICFSecAuthorization Authorization,
		ICFBamIndexTweakByIndexIdxKey argKey )
	{
		CFBamBuffIndexTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexTweak> matchSet = new LinkedList<CFBamBuffIndexTweak>();
		Iterator<CFBamBuffIndexTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexTweak)(schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexTweakByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffIndexTweak cur;
		LinkedList<CFBamBuffIndexTweak> matchSet = new LinkedList<CFBamBuffIndexTweak>();
		Iterator<CFBamBuffIndexTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexTweak)(schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexTweakByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffTweakByUNameIdxKey key = (CFBamBuffTweakByUNameIdxKey)schema.getCFBamFactory().getFactoryTweak().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteIndexTweakByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteIndexTweakByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByUNameIdxKey argKey )
	{
		CFBamBuffIndexTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexTweak> matchSet = new LinkedList<CFBamBuffIndexTweak>();
		Iterator<CFBamBuffIndexTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexTweak)(schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexTweakByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffTweakByValTentIdxKey key = (CFBamBuffTweakByValTentIdxKey)schema.getCFBamFactory().getFactoryTweak().newByValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteIndexTweakByValTentIdx( Authorization, key );
	}

	@Override
	public void deleteIndexTweakByValTentIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByValTentIdxKey argKey )
	{
		CFBamBuffIndexTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexTweak> matchSet = new LinkedList<CFBamBuffIndexTweak>();
		Iterator<CFBamBuffIndexTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexTweak)(schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexTweakByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffTweakByScopeIdxKey key = (CFBamBuffTweakByScopeIdxKey)schema.getCFBamFactory().getFactoryTweak().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteIndexTweakByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteIndexTweakByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByScopeIdxKey argKey )
	{
		CFBamBuffIndexTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexTweak> matchSet = new LinkedList<CFBamBuffIndexTweak>();
		Iterator<CFBamBuffIndexTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexTweak)(schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexTweakByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffTweakByDefSchemaIdxKey key = (CFBamBuffTweakByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryTweak().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexTweakByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteIndexTweakByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByDefSchemaIdxKey argKey )
	{
		CFBamBuffIndexTweak cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexTweak> matchSet = new LinkedList<CFBamBuffIndexTweak>();
		Iterator<CFBamBuffIndexTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexTweak)(schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexTweakByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argDefSchemaTenantId,
		CFLibDbKeyHash256 argDefSchemaId,
		String argName )
	{
		CFBamBuffTweakByUDefIdxKey key = (CFBamBuffTweakByUDefIdxKey)schema.getCFBamFactory().getFactoryTweak().newByUDefIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredScopeId( argScopeId );
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		key.setRequiredName( argName );
		deleteIndexTweakByUDefIdx( Authorization, key );
	}

	@Override
	public void deleteIndexTweakByUDefIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByUDefIdxKey argKey )
	{
		CFBamBuffIndexTweak cur;
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
		LinkedList<CFBamBuffIndexTweak> matchSet = new LinkedList<CFBamBuffIndexTweak>();
		Iterator<CFBamBuffIndexTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexTweak)(schema.getTableIndexTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexTweak( Authorization, cur );
		}
	}
}
