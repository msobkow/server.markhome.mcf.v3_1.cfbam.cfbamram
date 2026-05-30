
// Description: Java 25 in-memory RAM DbIO implementation for ServerMethod.

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
 *	CFBamRamServerMethodTable in-memory RAM DbIO implementation
 *	for ServerMethod.
 */
public class CFBamRamServerMethodTable
	implements ICFBamServerMethodTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffServerMethod > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffServerMethod >();
	private Map< CFBamBuffServerMethodByUNameIdxKey,
			CFBamBuffServerMethod > dictByUNameIdx
		= new HashMap< CFBamBuffServerMethodByUNameIdxKey,
			CFBamBuffServerMethod >();
	private Map< CFBamBuffServerMethodByMethTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerMethod >> dictByMethTableIdx
		= new HashMap< CFBamBuffServerMethodByMethTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerMethod >>();
	private Map< CFBamBuffServerMethodByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerMethod >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffServerMethodByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerMethod >>();

	public CFBamRamServerMethodTable( ICFBamSchema argSchema ) {
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
	public ICFBamServerMethod createServerMethod( ICFSecAuthorization Authorization,
		ICFBamServerMethod iBuff )
	{
		final String S_ProcName = "createServerMethod";
		
		CFBamBuffServerMethod Buff = (CFBamBuffServerMethod)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffServerMethodByUNameIdxKey keyUNameIdx = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffServerMethodByMethTableIdxKey keyMethTableIdx = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		keyMethTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffServerMethodByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ServerMethodUNameIdx",
				"ServerMethodUNameIdx",
				keyUNameIdx );
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
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"ForTable",
						"ForTable",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffServerMethod > subdictMethTableIdx;
		if( dictByMethTableIdx.containsKey( keyMethTableIdx ) ) {
			subdictMethTableIdx = dictByMethTableIdx.get( keyMethTableIdx );
		}
		else {
			subdictMethTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffServerMethod >();
			dictByMethTableIdx.put( keyMethTableIdx, subdictMethTableIdx );
		}
		subdictMethTableIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffServerMethod > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffServerMethod >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamServerMethod.CLASS_CODE) {
				CFBamBuffServerMethod retbuff = ((CFBamBuffServerMethod)(schema.getFactoryServerMethod().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerObjFunc.CLASS_CODE) {
				CFBamBuffServerObjFunc retbuff = ((CFBamBuffServerObjFunc)(schema.getFactoryServerObjFunc().newRec()));
				retbuff.set((ICFBamServerObjFunc)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerProc.CLASS_CODE) {
				CFBamBuffServerProc retbuff = ((CFBamBuffServerProc)(schema.getFactoryServerProc().newRec()));
				retbuff.set((ICFBamServerProc)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerListFunc.CLASS_CODE) {
				CFBamBuffServerListFunc retbuff = ((CFBamBuffServerListFunc)(schema.getFactoryServerListFunc().newRec()));
				retbuff.set((ICFBamServerListFunc)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamServerMethod readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerived";
		ICFBamServerMethod buff;
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
	public ICFBamServerMethod lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerMethod.lockDerived";
		ICFBamServerMethod buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerMethod[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerMethod.readAllDerived";
		ICFBamServerMethod[] retList = new ICFBamServerMethod[ dictByPKey.values().size() ];
		Iterator< CFBamBuffServerMethod > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamServerMethod[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerMethod> filteredList = new ArrayList<ICFBamServerMethod>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerMethod ) ) {
					filteredList.add( (ICFBamServerMethod)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerMethod[0] ) );
		}
	}

	@Override
	public ICFBamServerMethod readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByUNameIdx";
		CFBamBuffServerMethodByUNameIdxKey key = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();

		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );
		ICFBamServerMethod buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerMethod[] readDerivedByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethTableIdx";
		CFBamBuffServerMethodByMethTableIdxKey key = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();

		key.setRequiredTableId( TableId );
		ICFBamServerMethod[] recArray;
		if( dictByMethTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffServerMethod > subdictMethTableIdx
				= dictByMethTableIdx.get( key );
			recArray = new ICFBamServerMethod[ subdictMethTableIdx.size() ];
			Iterator< CFBamBuffServerMethod > iter = subdictMethTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffServerMethod > subdictMethTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffServerMethod >();
			dictByMethTableIdx.put( key, subdictMethTableIdx );
			recArray = new ICFBamServerMethod[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamServerMethod[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByDefSchemaIdx";
		CFBamBuffServerMethodByDefSchemaIdxKey key = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamServerMethod[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffServerMethod > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamServerMethod[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffServerMethod > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffServerMethod > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffServerMethod >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamServerMethod[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamServerMethod readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamServerMethod buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerMethod readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRec";
		ICFBamServerMethod buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerMethod.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerMethod lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamServerMethod buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerMethod.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerMethod[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerMethod.readAllRec";
		ICFBamServerMethod buff;
		ArrayList<ICFBamServerMethod> filteredList = new ArrayList<ICFBamServerMethod>();
		ICFBamServerMethod[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerMethod[0] ) );
	}

	@Override
	public ICFBamServerMethod readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamServerMethod buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamServerMethod)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamServerMethod[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamServerMethod buff;
		ArrayList<ICFBamServerMethod> filteredList = new ArrayList<ICFBamServerMethod>();
		ICFBamServerMethod[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerMethod)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerMethod[0] ) );
	}

	@Override
	public ICFBamServerMethod readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByUNameIdx() ";
		ICFBamServerMethod buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
			return( (ICFBamServerMethod)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamServerMethod[] readRecByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByMethTableIdx() ";
		ICFBamServerMethod buff;
		ArrayList<ICFBamServerMethod> filteredList = new ArrayList<ICFBamServerMethod>();
		ICFBamServerMethod[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerMethod)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerMethod[0] ) );
	}

	@Override
	public ICFBamServerMethod[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByDefSchemaIdx() ";
		ICFBamServerMethod buff;
		ArrayList<ICFBamServerMethod> filteredList = new ArrayList<ICFBamServerMethod>();
		ICFBamServerMethod[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerMethod)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerMethod[0] ) );
	}

	public ICFBamServerMethod updateServerMethod( ICFSecAuthorization Authorization,
		ICFBamServerMethod iBuff )
	{
		CFBamBuffServerMethod Buff = (CFBamBuffServerMethod)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffServerMethod existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerMethod",
				"Existing record not found",
				"Existing record not found",
				"ServerMethod",
				"ServerMethod",
				pkey );
		}
		CFBamBuffServerMethodByUNameIdxKey existingKeyUNameIdx = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffServerMethodByUNameIdxKey newKeyUNameIdx = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffServerMethodByMethTableIdxKey existingKeyMethTableIdx = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		existingKeyMethTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffServerMethodByMethTableIdxKey newKeyMethTableIdx = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		newKeyMethTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffServerMethodByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffServerMethodByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateServerMethod",
					"ServerMethodUNameIdx",
					"ServerMethodUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerMethod",
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
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerMethod",
						"Container",
						"Container",
						"ForTable",
						"ForTable",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffServerMethod > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByMethTableIdx.get( existingKeyMethTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByMethTableIdx.containsKey( newKeyMethTableIdx ) ) {
			subdict = dictByMethTableIdx.get( newKeyMethTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffServerMethod >();
			dictByMethTableIdx.put( newKeyMethTableIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffServerMethod >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteServerMethod( ICFSecAuthorization Authorization,
		ICFBamServerMethod iBuff )
	{
		final String S_ProcName = "CFBamRamServerMethodTable.deleteServerMethod() ";
		CFBamBuffServerMethod Buff = (CFBamBuffServerMethod)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffServerMethod existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteServerMethod",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckParams[] = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckParams.length > 0 ) {
			schema.getTableParam().deleteParamByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffServerMethodByUNameIdxKey keyUNameIdx = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffServerMethodByMethTableIdxKey keyMethTableIdx = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		keyMethTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffServerMethodByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableServerObjFunc().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteServerMethod",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ServerObjFunc",
				"ServerObjFunc",
				pkey );
		}

		if( schema.getTableServerProc().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteServerMethod",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ServerProc",
				"ServerProc",
				pkey );
		}

		if( schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteServerMethod",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ServerListFunc",
				"ServerListFunc",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffServerMethod > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByMethTableIdx.get( keyMethTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deleteServerMethodByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffServerMethodByUNameIdxKey key = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerMethodByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteServerMethodByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByUNameIdx";
		CFBamBuffServerMethod cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerMethod> matchSet = new LinkedList<CFBamBuffServerMethod>();
		Iterator<CFBamBuffServerMethod> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerMethod> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerMethod)(schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( ICFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( ICFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( ICFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteServerMethodByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffServerMethodByMethTableIdxKey key = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerMethodByMethTableIdx( Authorization, key );
	}

	@Override
	public void deleteServerMethodByMethTableIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByMethTableIdxKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByMethTableIdx";
		CFBamBuffServerMethod cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerMethod> matchSet = new LinkedList<CFBamBuffServerMethod>();
		Iterator<CFBamBuffServerMethod> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerMethod> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerMethod)(schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( ICFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( ICFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( ICFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteServerMethodByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffServerMethodByDefSchemaIdxKey key = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerMethodByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteServerMethodByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByDefSchemaIdx";
		CFBamBuffServerMethod cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerMethod> matchSet = new LinkedList<CFBamBuffServerMethod>();
		Iterator<CFBamBuffServerMethod> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerMethod> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerMethod)(schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( ICFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( ICFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( ICFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteServerMethodByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteServerMethodByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffServerMethod cur;
		LinkedList<CFBamBuffServerMethod> matchSet = new LinkedList<CFBamBuffServerMethod>();
		Iterator<CFBamBuffServerMethod> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerMethod> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerMethod)(schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( ICFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( ICFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( ICFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteServerMethodByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerMethodByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteServerMethodByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteServerMethodByTenantIdx";
		CFBamBuffServerMethod cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerMethod> matchSet = new LinkedList<CFBamBuffServerMethod>();
		Iterator<CFBamBuffServerMethod> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerMethod> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerMethod)(schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, cur );
			}
			else if( ICFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( ICFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( ICFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
