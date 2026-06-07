
// Description: Java 25 in-memory RAM DbIO implementation for ServerListFunc.

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
 *	CFBamRamServerListFuncTable in-memory RAM DbIO implementation
 *	for ServerListFunc.
 */
public class CFBamRamServerListFuncTable
	implements ICFBamServerListFuncTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffServerListFunc > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffServerListFunc >();
	private Map< CFBamBuffServerListFuncByRetTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerListFunc >> dictByRetTblIdx
		= new HashMap< CFBamBuffServerListFuncByRetTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffServerListFunc >>();

	public CFBamRamServerListFuncTable( ICFBamSchema argSchema ) {
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
	public ICFBamServerListFunc createServerListFunc( ICFSecAuthorization Authorization,
		ICFBamServerListFunc iBuff )
	{
		final String S_ProcName = "createServerListFunc";
		
		CFBamBuffServerListFunc Buff = (CFBamBuffServerListFunc)(schema.getTableServerMethod().createServerMethod( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffServerListFuncByRetTblIdxKey keyRetTblIdx = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		keyRetTblIdx.setOptionalRetTableId( Buff.getOptionalRetTableId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdictRetTblIdx;
		if( dictByRetTblIdx.containsKey( keyRetTblIdx ) ) {
			subdictRetTblIdx = dictByRetTblIdx.get( keyRetTblIdx );
		}
		else {
			subdictRetTblIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffServerListFunc >();
			dictByRetTblIdx.put( keyRetTblIdx, subdictRetTblIdx );
		}
		subdictRetTblIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamServerListFunc.CLASS_CODE) {
				CFBamBuffServerListFunc retbuff = ((CFBamBuffServerListFunc)(schema.getFactoryServerListFunc().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamServerListFunc readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readDerived";
		ICFBamServerListFunc buff;
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
	public ICFBamServerListFunc lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.lockDerived";
		ICFBamServerListFunc buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerListFunc[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerListFunc.readAllDerived";
		ICFBamServerListFunc[] retList = new ICFBamServerListFunc[ dictByPKey.values().size() ];
		Iterator< CFBamBuffServerListFunc > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamServerListFunc[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerListFunc ) ) {
					filteredList.add( (ICFBamServerListFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
		}
	}

	@Override
	public ICFBamServerListFunc readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByUNameIdx";
		ICFBamServerMethod buff = schema.getTableServerMethod().readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamServerListFunc ) {
			return( (ICFBamServerListFunc)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamServerListFunc[] readDerivedByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethTableIdx";
		ICFBamServerMethod buffList[] = schema.getTableServerMethod().readDerivedByMethTableIdx( Authorization,
			TableId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamServerMethod buff;
			ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerListFunc ) ) {
					filteredList.add( (ICFBamServerListFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
		}
	}

	@Override
	public ICFBamServerListFunc[] readDerivedByMethCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethCodeVisIdx";
		ICFBamServerMethod buffList[] = schema.getTableServerMethod().readDerivedByMethCodeVisIdx( Authorization,
			CodeVis );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamServerMethod buff;
			ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerListFunc ) ) {
					filteredList.add( (ICFBamServerListFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
		}
	}

	@Override
	public ICFBamServerListFunc[] readDerivedByMethTableVisIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethTableVisIdx";
		ICFBamServerMethod buffList[] = schema.getTableServerMethod().readDerivedByMethTableVisIdx( Authorization,
			TableId,
			CodeVis );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamServerMethod buff;
			ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerListFunc ) ) {
					filteredList.add( (ICFBamServerListFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
		}
	}

	@Override
	public ICFBamServerListFunc[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByDefSchemaIdx";
		ICFBamServerMethod buffList[] = schema.getTableServerMethod().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamServerMethod buff;
			ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerListFunc ) ) {
					filteredList.add( (ICFBamServerListFunc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
		}
	}

	@Override
	public ICFBamServerListFunc[] readDerivedByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readDerivedByRetTblIdx";
		CFBamBuffServerListFuncByRetTblIdxKey key = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();

		key.setOptionalRetTableId( RetTableId );
		ICFBamServerListFunc[] recArray;
		if( dictByRetTblIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdictRetTblIdx
				= dictByRetTblIdx.get( key );
			recArray = new ICFBamServerListFunc[ subdictRetTblIdx.size() ];
			Iterator< CFBamBuffServerListFunc > iter = subdictRetTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdictRetTblIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffServerListFunc >();
			dictByRetTblIdx.put( key, subdictRetTblIdx );
			recArray = new ICFBamServerListFunc[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamServerListFunc readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamServerListFunc buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerListFunc readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readRec";
		ICFBamServerListFunc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerListFunc.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerListFunc lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamServerListFunc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerListFunc.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamServerListFunc[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readAllRec";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerListFunc.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	@Override
	public ICFBamServerListFunc readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamServerListFunc buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamServerListFunc)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamServerListFunc[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	@Override
	public ICFBamServerListFunc readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByUNameIdx() ";
		ICFBamServerListFunc buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
			return( (ICFBamServerListFunc)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamServerListFunc[] readRecByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByMethTableIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	@Override
	public ICFBamServerListFunc[] readRecByMethCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByMethCodeVisIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByMethCodeVisIdx( Authorization,
			CodeVis );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	@Override
	public ICFBamServerListFunc[] readRecByMethTableVisIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByMethTableVisIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByMethTableVisIdx( Authorization,
			TableId,
			CodeVis );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	@Override
	public ICFBamServerListFunc[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByDefSchemaIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	@Override
	public ICFBamServerListFunc[] readRecByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RetTableId )
	{
		final String S_ProcName = "CFBamRamServerListFunc.readRecByRetTblIdx() ";
		ICFBamServerListFunc buff;
		ArrayList<ICFBamServerListFunc> filteredList = new ArrayList<ICFBamServerListFunc>();
		ICFBamServerListFunc[] buffList = readDerivedByRetTblIdx( Authorization,
			RetTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerListFunc.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerListFunc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerListFunc[0] ) );
	}

	public ICFBamServerListFunc updateServerListFunc( ICFSecAuthorization Authorization,
		ICFBamServerListFunc iBuff )
	{
		CFBamBuffServerListFunc Buff = (CFBamBuffServerListFunc)(schema.getTableServerMethod().updateServerMethod( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffServerListFunc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerListFunc",
				"Existing record not found",
				"Existing record not found",
				"ServerListFunc",
				"ServerListFunc",
				pkey );
		}
		CFBamBuffServerListFuncByRetTblIdxKey existingKeyRetTblIdx = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		existingKeyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		CFBamBuffServerListFuncByRetTblIdxKey newKeyRetTblIdx = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		newKeyRetTblIdx.setOptionalRetTableId( Buff.getOptionalRetTableId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerListFunc",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByRetTblIdx.get( existingKeyRetTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRetTblIdx.containsKey( newKeyRetTblIdx ) ) {
			subdict = dictByRetTblIdx.get( newKeyRetTblIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffServerListFunc >();
			dictByRetTblIdx.put( newKeyRetTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteServerListFunc( ICFSecAuthorization Authorization,
		ICFBamServerListFunc iBuff )
	{
		final String S_ProcName = "CFBamRamServerListFuncTable.deleteServerListFunc() ";
		CFBamBuffServerListFunc Buff = (CFBamBuffServerListFunc)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffServerListFunc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteServerListFunc",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckParams[] = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckParams.length > 0 ) {
			schema.getTableParam().deleteParamByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffServerListFuncByRetTblIdxKey keyRetTblIdx = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		keyRetTblIdx.setOptionalRetTableId( existing.getOptionalRetTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffServerListFunc > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByRetTblIdx.get( keyRetTblIdx );
		subdict.remove( pkey );

		schema.getTableServerMethod().deleteServerMethod( Authorization,
			Buff );
	}
	@Override
	public void deleteServerListFuncByRetTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRetTableId )
	{
		CFBamBuffServerListFuncByRetTblIdxKey key = (CFBamBuffServerListFuncByRetTblIdxKey)schema.getFactoryServerListFunc().newByRetTblIdxKey();
		key.setOptionalRetTableId( argRetTableId );
		deleteServerListFuncByRetTblIdx( Authorization, key );
	}

	@Override
	public void deleteServerListFuncByRetTblIdx( ICFSecAuthorization Authorization,
		ICFBamServerListFuncByRetTblIdxKey argKey )
	{
		CFBamBuffServerListFunc cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalRetTableId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerListFunc> matchSet = new LinkedList<CFBamBuffServerListFunc>();
		Iterator<CFBamBuffServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerListFunc)(schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerListFunc( Authorization, cur );
		}
	}

	@Override
	public void deleteServerListFuncByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffServerMethodByUNameIdxKey key = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerListFuncByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteServerListFuncByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByUNameIdxKey argKey )
	{
		CFBamBuffServerListFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerListFunc> matchSet = new LinkedList<CFBamBuffServerListFunc>();
		Iterator<CFBamBuffServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerListFunc)(schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerListFunc( Authorization, cur );
		}
	}

	@Override
	public void deleteServerListFuncByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffServerMethodByMethTableIdxKey key = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerListFuncByMethTableIdx( Authorization, key );
	}

	@Override
	public void deleteServerListFuncByMethTableIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByMethTableIdxKey argKey )
	{
		CFBamBuffServerListFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerListFunc> matchSet = new LinkedList<CFBamBuffServerListFunc>();
		Iterator<CFBamBuffServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerListFunc)(schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerListFunc( Authorization, cur );
		}
	}

	@Override
	public void deleteServerListFuncByMethCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum argCodeVis )
	{
		CFBamBuffServerMethodByMethCodeVisIdxKey key = (CFBamBuffServerMethodByMethCodeVisIdxKey)schema.getFactoryServerMethod().newByMethCodeVisIdxKey();
		key.setRequiredCodeVis( argCodeVis );
		deleteServerListFuncByMethCodeVisIdx( Authorization, key );
	}

	@Override
	public void deleteServerListFuncByMethCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByMethCodeVisIdxKey argKey )
	{
		CFBamBuffServerListFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerListFunc> matchSet = new LinkedList<CFBamBuffServerListFunc>();
		Iterator<CFBamBuffServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerListFunc)(schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerListFunc( Authorization, cur );
		}
	}

	@Override
	public void deleteServerListFuncByMethTableVisIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		ICFBamSchema.CodeVisibilityEnum argCodeVis )
	{
		CFBamBuffServerMethodByMethTableVisIdxKey key = (CFBamBuffServerMethodByMethTableVisIdxKey)schema.getFactoryServerMethod().newByMethTableVisIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredCodeVis( argCodeVis );
		deleteServerListFuncByMethTableVisIdx( Authorization, key );
	}

	@Override
	public void deleteServerListFuncByMethTableVisIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByMethTableVisIdxKey argKey )
	{
		CFBamBuffServerListFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerListFunc> matchSet = new LinkedList<CFBamBuffServerListFunc>();
		Iterator<CFBamBuffServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerListFunc)(schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerListFunc( Authorization, cur );
		}
	}

	@Override
	public void deleteServerListFuncByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffServerMethodByDefSchemaIdxKey key = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerListFuncByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteServerListFuncByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByDefSchemaIdxKey argKey )
	{
		CFBamBuffServerListFunc cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerListFunc> matchSet = new LinkedList<CFBamBuffServerListFunc>();
		Iterator<CFBamBuffServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerListFunc)(schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerListFunc( Authorization, cur );
		}
	}

	@Override
	public void deleteServerListFuncByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffServerListFunc cur;
		LinkedList<CFBamBuffServerListFunc> matchSet = new LinkedList<CFBamBuffServerListFunc>();
		Iterator<CFBamBuffServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerListFunc)(schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerListFunc( Authorization, cur );
		}
	}

	@Override
	public void deleteServerListFuncByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerListFuncByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteServerListFuncByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffServerListFunc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerListFunc> matchSet = new LinkedList<CFBamBuffServerListFunc>();
		Iterator<CFBamBuffServerListFunc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerListFunc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerListFunc)(schema.getTableServerListFunc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerListFunc( Authorization, cur );
		}
	}
}
