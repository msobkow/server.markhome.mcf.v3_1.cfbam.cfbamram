
// Description: Java 25 in-memory RAM DbIO implementation for TableTweak.

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
 *	CFBamRamTableTweakTable in-memory RAM DbIO implementation
 *	for TableTweak.
 */
public class CFBamRamTableTweakTable
	implements ICFBamTableTweakTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffTableTweak > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffTableTweak >();
	private Map< CFBamBuffTableTweakByTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTableTweak >> dictByTableIdx
		= new HashMap< CFBamBuffTableTweakByTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTableTweak >>();

	public CFBamRamTableTweakTable( ICFBamSchema argSchema ) {
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
	public ICFBamTableTweak createTableTweak( ICFSecAuthorization Authorization,
		ICFBamTableTweak iBuff )
	{
		final String S_ProcName = "createTableTweak";
		
		CFBamBuffTableTweak Buff = (CFBamBuffTableTweak)(schema.getTableTweak().createTweak( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffTableTweakByTableIdxKey keyTableIdx = (CFBamBuffTableTweakByTableIdxKey)schema.getCFBamFactory().getFactoryTableTweak().newByTableIdxKey();
		keyTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

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
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"Table",
						"Table",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTableTweak > subdictTableIdx;
		if( dictByTableIdx.containsKey( keyTableIdx ) ) {
			subdictTableIdx = dictByTableIdx.get( keyTableIdx );
		}
		else {
			subdictTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTableTweak >();
			dictByTableIdx.put( keyTableIdx, subdictTableIdx );
		}
		subdictTableIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamTableTweak.CLASS_CODE) {
				CFBamBuffTableTweak retbuff = ((CFBamBuffTableTweak)(schema.getCFBamFactory().getFactoryTableTweak().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamTableTweak readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTableTweak.readDerived";
		ICFBamTableTweak buff;
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
	public ICFBamTableTweak lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTableTweak.lockDerived";
		ICFBamTableTweak buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTableTweak[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamTableTweak.readAllDerived";
		ICFBamTableTweak[] retList = new ICFBamTableTweak[ dictByPKey.values().size() ];
		Iterator< CFBamBuffTableTweak > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamTableTweak readDerivedByUNameIdx( ICFSecAuthorization Authorization,
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
		else if( buff instanceof ICFBamTableTweak ) {
			return( (ICFBamTableTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTableTweak[] readDerivedByValTentIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTableTweak> filteredList = new ArrayList<ICFBamTableTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTableTweak ) ) {
					filteredList.add( (ICFBamTableTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTableTweak[0] ) );
		}
	}

	@Override
	public ICFBamTableTweak[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTableTweak> filteredList = new ArrayList<ICFBamTableTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTableTweak ) ) {
					filteredList.add( (ICFBamTableTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTableTweak[0] ) );
		}
	}

	@Override
	public ICFBamTableTweak[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTableTweak> filteredList = new ArrayList<ICFBamTableTweak>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTableTweak ) ) {
					filteredList.add( (ICFBamTableTweak)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTableTweak[0] ) );
		}
	}

	@Override
	public ICFBamTableTweak readDerivedByUDefIdx( ICFSecAuthorization Authorization,
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
		else if( buff instanceof ICFBamTableTweak ) {
			return( (ICFBamTableTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTableTweak[] readDerivedByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamTableTweak.readDerivedByTableIdx";
		CFBamBuffTableTweakByTableIdxKey key = (CFBamBuffTableTweakByTableIdxKey)schema.getCFBamFactory().getFactoryTableTweak().newByTableIdxKey();

		key.setRequiredTableId( TableId );
		ICFBamTableTweak[] recArray;
		if( dictByTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTableTweak > subdictTableIdx
				= dictByTableIdx.get( key );
			recArray = new ICFBamTableTweak[ subdictTableIdx.size() ];
			Iterator< CFBamBuffTableTweak > iter = subdictTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTableTweak > subdictTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTableTweak >();
			dictByTableIdx.put( key, subdictTableIdx );
			recArray = new ICFBamTableTweak[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTableTweak readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTweak.readDerivedByIdIdx() ";
		ICFBamTableTweak buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTableTweak readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTableTweak.readRec";
		ICFBamTableTweak buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamTableTweak.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTableTweak lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamTableTweak buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamTableTweak.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTableTweak[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamTableTweak.readAllRec";
		ICFBamTableTweak buff;
		ArrayList<ICFBamTableTweak> filteredList = new ArrayList<ICFBamTableTweak>();
		ICFBamTableTweak[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTableTweak.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamTableTweak[0] ) );
	}

	@Override
	public ICFBamTableTweak readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByIdIdx() ";
		ICFBamTableTweak buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamTableTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTableTweak readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByUNameIdx() ";
		ICFBamTableTweak buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamTableTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTableTweak[] readRecByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByValTentIdx() ";
		ICFBamTableTweak buff;
		ArrayList<ICFBamTableTweak> filteredList = new ArrayList<ICFBamTableTweak>();
		ICFBamTableTweak[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTableTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTableTweak[0] ) );
	}

	@Override
	public ICFBamTableTweak[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByScopeIdx() ";
		ICFBamTableTweak buff;
		ArrayList<ICFBamTableTweak> filteredList = new ArrayList<ICFBamTableTweak>();
		ICFBamTableTweak[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTableTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTableTweak[0] ) );
	}

	@Override
	public ICFBamTableTweak[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByDefSchemaIdx() ";
		ICFBamTableTweak buff;
		ArrayList<ICFBamTableTweak> filteredList = new ArrayList<ICFBamTableTweak>();
		ICFBamTableTweak[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTableTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTableTweak[0] ) );
	}

	@Override
	public ICFBamTableTweak readRecByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaTenantId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTweak.readRecByUDefIdx() ";
		ICFBamTableTweak buff = readDerivedByUDefIdx( Authorization,
			TenantId,
			ScopeId,
			DefSchemaTenantId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTweak.CLASS_CODE ) ) {
			return( (ICFBamTableTweak)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTableTweak[] readRecByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamTableTweak.readRecByTableIdx() ";
		ICFBamTableTweak buff;
		ArrayList<ICFBamTableTweak> filteredList = new ArrayList<ICFBamTableTweak>();
		ICFBamTableTweak[] buffList = readDerivedByTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTableTweak.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTableTweak)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTableTweak[0] ) );
	}

	public ICFBamTableTweak updateTableTweak( ICFSecAuthorization Authorization,
		ICFBamTableTweak iBuff )
	{
		CFBamBuffTableTweak Buff = (CFBamBuffTableTweak)(schema.getTableTweak().updateTweak( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffTableTweak existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateTableTweak",
				"Existing record not found",
				"Existing record not found",
				"TableTweak",
				"TableTweak",
				pkey );
		}
		CFBamBuffTableTweakByTableIdxKey existingKeyTableIdx = (CFBamBuffTableTweakByTableIdxKey)schema.getCFBamFactory().getFactoryTableTweak().newByTableIdxKey();
		existingKeyTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffTableTweakByTableIdxKey newKeyTableIdx = (CFBamBuffTableTweakByTableIdxKey)schema.getCFBamFactory().getFactoryTableTweak().newByTableIdxKey();
		newKeyTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTweak().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTableTweak",
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
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTableTweak",
						"Container",
						"Container",
						"Table",
						"Table",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffTableTweak > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByTableIdx.get( existingKeyTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByTableIdx.containsKey( newKeyTableIdx ) ) {
			subdict = dictByTableIdx.get( newKeyTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTableTweak >();
			dictByTableIdx.put( newKeyTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteTableTweak( ICFSecAuthorization Authorization,
		ICFBamTableTweak iBuff )
	{
		final String S_ProcName = "CFBamRamTableTweakTable.deleteTableTweak() ";
		CFBamBuffTableTweak Buff = (CFBamBuffTableTweak)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffTableTweak existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteTableTweak",
				pkey );
		}
		CFBamBuffTableTweakByTableIdxKey keyTableIdx = (CFBamBuffTableTweakByTableIdxKey)schema.getCFBamFactory().getFactoryTableTweak().newByTableIdxKey();
		keyTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffTableTweak > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByTableIdx.get( keyTableIdx );
		subdict.remove( pkey );

		schema.getTableTweak().deleteTweak( Authorization,
			Buff );
	}
	@Override
	public void deleteTableTweakByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffTableTweakByTableIdxKey key = (CFBamBuffTableTweakByTableIdxKey)schema.getCFBamFactory().getFactoryTableTweak().newByTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteTableTweakByTableIdx( Authorization, key );
	}

	@Override
	public void deleteTableTweakByTableIdx( ICFSecAuthorization Authorization,
		ICFBamTableTweakByTableIdxKey argKey )
	{
		CFBamBuffTableTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTableTweak> matchSet = new LinkedList<CFBamBuffTableTweak>();
		Iterator<CFBamBuffTableTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTableTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTableTweak)(schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTableTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteTableTweakByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffTableTweak cur;
		LinkedList<CFBamBuffTableTweak> matchSet = new LinkedList<CFBamBuffTableTweak>();
		Iterator<CFBamBuffTableTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTableTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTableTweak)(schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTableTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteTableTweakByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffTweakByUNameIdxKey key = (CFBamBuffTweakByUNameIdxKey)schema.getCFBamFactory().getFactoryTweak().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteTableTweakByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteTableTweakByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByUNameIdxKey argKey )
	{
		CFBamBuffTableTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTableTweak> matchSet = new LinkedList<CFBamBuffTableTweak>();
		Iterator<CFBamBuffTableTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTableTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTableTweak)(schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTableTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteTableTweakByValTentIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffTweakByValTentIdxKey key = (CFBamBuffTweakByValTentIdxKey)schema.getCFBamFactory().getFactoryTweak().newByValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteTableTweakByValTentIdx( Authorization, key );
	}

	@Override
	public void deleteTableTweakByValTentIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByValTentIdxKey argKey )
	{
		CFBamBuffTableTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTableTweak> matchSet = new LinkedList<CFBamBuffTableTweak>();
		Iterator<CFBamBuffTableTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTableTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTableTweak)(schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTableTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteTableTweakByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffTweakByScopeIdxKey key = (CFBamBuffTweakByScopeIdxKey)schema.getCFBamFactory().getFactoryTweak().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteTableTweakByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteTableTweakByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByScopeIdxKey argKey )
	{
		CFBamBuffTableTweak cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTableTweak> matchSet = new LinkedList<CFBamBuffTableTweak>();
		Iterator<CFBamBuffTableTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTableTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTableTweak)(schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTableTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteTableTweakByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffTweakByDefSchemaIdxKey key = (CFBamBuffTweakByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryTweak().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteTableTweakByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteTableTweakByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByDefSchemaIdxKey argKey )
	{
		CFBamBuffTableTweak cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTableTweak> matchSet = new LinkedList<CFBamBuffTableTweak>();
		Iterator<CFBamBuffTableTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTableTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTableTweak)(schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTableTweak( Authorization, cur );
		}
	}

	@Override
	public void deleteTableTweakByUDefIdx( ICFSecAuthorization Authorization,
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
		deleteTableTweakByUDefIdx( Authorization, key );
	}

	@Override
	public void deleteTableTweakByUDefIdx( ICFSecAuthorization Authorization,
		ICFBamTweakByUDefIdxKey argKey )
	{
		CFBamBuffTableTweak cur;
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
		LinkedList<CFBamBuffTableTweak> matchSet = new LinkedList<CFBamBuffTableTweak>();
		Iterator<CFBamBuffTableTweak> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTableTweak> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTableTweak)(schema.getTableTableTweak().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTableTweak( Authorization, cur );
		}
	}
}
