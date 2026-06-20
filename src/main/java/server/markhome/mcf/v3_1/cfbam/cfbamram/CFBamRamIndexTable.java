
// Description: Java 25 in-memory RAM DbIO implementation for Index.

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
 *	CFBamRamIndexTable in-memory RAM DbIO implementation
 *	for Index.
 */
public class CFBamRamIndexTable
	implements ICFBamIndexTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffIndex > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffIndex >();
	private Map< CFBamBuffIndexByUNameIdxKey,
			CFBamBuffIndex > dictByUNameIdx
		= new HashMap< CFBamBuffIndexByUNameIdxKey,
			CFBamBuffIndex >();
	private Map< CFBamBuffIndexByIdxTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >> dictByIdxTableIdx
		= new HashMap< CFBamBuffIndexByIdxTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >>();
	private Map< CFBamBuffIndexByIdxCodeVisIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >> dictByIdxCodeVisIdx
		= new HashMap< CFBamBuffIndexByIdxCodeVisIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >>();
	private Map< CFBamBuffIndexByIdxTblCdVisXKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >> dictByIdxTblCdVisX
		= new HashMap< CFBamBuffIndexByIdxTblCdVisXKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >>();
	private Map< CFBamBuffIndexByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffIndexByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndex >>();

	public CFBamRamIndexTable( ICFBamSchema argSchema ) {
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
	public ICFBamIndex createIndex( ICFSecAuthorization Authorization,
		ICFBamIndex iBuff )
	{
		final String S_ProcName = "createIndex";
		
		CFBamBuffIndex Buff = (CFBamBuffIndex)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffIndexByUNameIdxKey keyUNameIdx = (CFBamBuffIndexByUNameIdxKey)schema.getCFBamFactory().getFactoryIndex().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffIndexByIdxTableIdxKey keyIdxTableIdx = (CFBamBuffIndexByIdxTableIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTableIdxKey();
		keyIdxTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffIndexByIdxCodeVisIdxKey keyIdxCodeVisIdx = (CFBamBuffIndexByIdxCodeVisIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxCodeVisIdxKey();
		keyIdxCodeVisIdx.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffIndexByIdxTblCdVisXKey keyIdxTblCdVisX = (CFBamBuffIndexByIdxTblCdVisXKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTblCdVisXKey();
		keyIdxTblCdVisX.setRequiredTableId( Buff.getRequiredTableId() );
		keyIdxTblCdVisX.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffIndexByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffIndexByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryIndex().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"IndexUNameIdx",
				"IndexUNameIdx",
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

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTableIdx;
		if( dictByIdxTableIdx.containsKey( keyIdxTableIdx ) ) {
			subdictIdxTableIdx = dictByIdxTableIdx.get( keyIdxTableIdx );
		}
		else {
			subdictIdxTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxTableIdx.put( keyIdxTableIdx, subdictIdxTableIdx );
		}
		subdictIdxTableIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxCodeVisIdx;
		if( dictByIdxCodeVisIdx.containsKey( keyIdxCodeVisIdx ) ) {
			subdictIdxCodeVisIdx = dictByIdxCodeVisIdx.get( keyIdxCodeVisIdx );
		}
		else {
			subdictIdxCodeVisIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxCodeVisIdx.put( keyIdxCodeVisIdx, subdictIdxCodeVisIdx );
		}
		subdictIdxCodeVisIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTblCdVisX;
		if( dictByIdxTblCdVisX.containsKey( keyIdxTblCdVisX ) ) {
			subdictIdxTblCdVisX = dictByIdxTblCdVisX.get( keyIdxTblCdVisX );
		}
		else {
			subdictIdxTblCdVisX = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxTblCdVisX.put( keyIdxTblCdVisX, subdictIdxTblCdVisX );
		}
		subdictIdxTblCdVisX.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamIndex.CLASS_CODE) {
				CFBamBuffIndex retbuff = ((CFBamBuffIndex)(schema.getCFBamFactory().getFactoryIndex().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamIndex readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readDerived";
		ICFBamIndex buff;
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
	public ICFBamIndex lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndex.lockDerived";
		ICFBamIndex buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndex[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndex.readAllDerived";
		ICFBamIndex[] retList = new ICFBamIndex[ dictByPKey.values().size() ];
		Iterator< CFBamBuffIndex > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamIndex[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamIndex ) ) {
					filteredList.add( (ICFBamIndex)buff );
				}
			}
			return( filteredList.toArray( new ICFBamIndex[0] ) );
		}
	}

	@Override
	public ICFBamIndex readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByUNameIdx";
		CFBamBuffIndexByUNameIdxKey key = (CFBamBuffIndexByUNameIdxKey)schema.getCFBamFactory().getFactoryIndex().newByUNameIdxKey();

		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );
		ICFBamIndex buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndex[] readDerivedByIdxTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByIdxTableIdx";
		CFBamBuffIndexByIdxTableIdxKey key = (CFBamBuffIndexByIdxTableIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTableIdxKey();

		key.setRequiredTableId( TableId );
		ICFBamIndex[] recArray;
		if( dictByIdxTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTableIdx
				= dictByIdxTableIdx.get( key );
			recArray = new ICFBamIndex[ subdictIdxTableIdx.size() ];
			Iterator< CFBamBuffIndex > iter = subdictIdxTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxTableIdx.put( key, subdictIdxTableIdx );
			recArray = new ICFBamIndex[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndex[] readDerivedByIdxCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByIdxCodeVisIdx";
		CFBamBuffIndexByIdxCodeVisIdxKey key = (CFBamBuffIndexByIdxCodeVisIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxCodeVisIdxKey();

		key.setRequiredCodeVis( CodeVis );
		ICFBamIndex[] recArray;
		if( dictByIdxCodeVisIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxCodeVisIdx
				= dictByIdxCodeVisIdx.get( key );
			recArray = new ICFBamIndex[ subdictIdxCodeVisIdx.size() ];
			Iterator< CFBamBuffIndex > iter = subdictIdxCodeVisIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxCodeVisIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxCodeVisIdx.put( key, subdictIdxCodeVisIdx );
			recArray = new ICFBamIndex[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndex[] readDerivedByIdxTblCdVisX( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByIdxTblCdVisX";
		CFBamBuffIndexByIdxTblCdVisXKey key = (CFBamBuffIndexByIdxTblCdVisXKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTblCdVisXKey();

		key.setRequiredTableId( TableId );
		key.setRequiredCodeVis( CodeVis );
		ICFBamIndex[] recArray;
		if( dictByIdxTblCdVisX.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTblCdVisX
				= dictByIdxTblCdVisX.get( key );
			recArray = new ICFBamIndex[ subdictIdxTblCdVisX.size() ];
			Iterator< CFBamBuffIndex > iter = subdictIdxTblCdVisX.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictIdxTblCdVisX
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxTblCdVisX.put( key, subdictIdxTblCdVisX );
			recArray = new ICFBamIndex[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndex[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByDefSchemaIdx";
		CFBamBuffIndexByDefSchemaIdxKey key = (CFBamBuffIndexByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryIndex().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamIndex[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamIndex[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffIndex > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndex > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamIndex[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndex readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamIndex buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndex readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readRec";
		ICFBamIndex buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndex.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndex lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamIndex buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndex.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndex[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndex.readAllRec";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	@Override
	public ICFBamIndex readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamIndex buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamIndex)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndex[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndex)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	@Override
	public ICFBamIndex readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndex.readRecByUNameIdx() ";
		ICFBamIndex buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
			return( (ICFBamIndex)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndex[] readRecByIdxTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamIndex.readRecByIdxTableIdx() ";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readDerivedByIdxTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndex)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	@Override
	public ICFBamIndex[] readRecByIdxCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamIndex.readRecByIdxCodeVisIdx() ";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readDerivedByIdxCodeVisIdx( Authorization,
			CodeVis );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndex)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	@Override
	public ICFBamIndex[] readRecByIdxTblCdVisX( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamIndex.readRecByIdxTblCdVisX() ";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readDerivedByIdxTblCdVisX( Authorization,
			TableId,
			CodeVis );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndex)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	@Override
	public ICFBamIndex[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndex.readRecByDefSchemaIdx() ";
		ICFBamIndex buff;
		ArrayList<ICFBamIndex> filteredList = new ArrayList<ICFBamIndex>();
		ICFBamIndex[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndex.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndex)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndex[0] ) );
	}

	public ICFBamIndex updateIndex( ICFSecAuthorization Authorization,
		ICFBamIndex iBuff )
	{
		CFBamBuffIndex Buff = (CFBamBuffIndex)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffIndex existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndex",
				"Existing record not found",
				"Existing record not found",
				"Index",
				"Index",
				pkey );
		}
		CFBamBuffIndexByUNameIdxKey existingKeyUNameIdx = (CFBamBuffIndexByUNameIdxKey)schema.getCFBamFactory().getFactoryIndex().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffIndexByUNameIdxKey newKeyUNameIdx = (CFBamBuffIndexByUNameIdxKey)schema.getCFBamFactory().getFactoryIndex().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffIndexByIdxTableIdxKey existingKeyIdxTableIdx = (CFBamBuffIndexByIdxTableIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTableIdxKey();
		existingKeyIdxTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffIndexByIdxTableIdxKey newKeyIdxTableIdx = (CFBamBuffIndexByIdxTableIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTableIdxKey();
		newKeyIdxTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffIndexByIdxCodeVisIdxKey existingKeyIdxCodeVisIdx = (CFBamBuffIndexByIdxCodeVisIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxCodeVisIdxKey();
		existingKeyIdxCodeVisIdx.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffIndexByIdxCodeVisIdxKey newKeyIdxCodeVisIdx = (CFBamBuffIndexByIdxCodeVisIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxCodeVisIdxKey();
		newKeyIdxCodeVisIdx.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffIndexByIdxTblCdVisXKey existingKeyIdxTblCdVisX = (CFBamBuffIndexByIdxTblCdVisXKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTblCdVisXKey();
		existingKeyIdxTblCdVisX.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyIdxTblCdVisX.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffIndexByIdxTblCdVisXKey newKeyIdxTblCdVisX = (CFBamBuffIndexByIdxTblCdVisXKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTblCdVisXKey();
		newKeyIdxTblCdVisX.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyIdxTblCdVisX.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffIndexByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffIndexByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryIndex().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffIndexByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffIndexByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryIndex().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateIndex",
					"IndexUNameIdx",
					"IndexUNameIdx",
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
						"updateIndex",
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
						"updateIndex",
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

		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByIdxTableIdx.get( existingKeyIdxTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxTableIdx.containsKey( newKeyIdxTableIdx ) ) {
			subdict = dictByIdxTableIdx.get( newKeyIdxTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxTableIdx.put( newKeyIdxTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxCodeVisIdx.get( existingKeyIdxCodeVisIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxCodeVisIdx.containsKey( newKeyIdxCodeVisIdx ) ) {
			subdict = dictByIdxCodeVisIdx.get( newKeyIdxCodeVisIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxCodeVisIdx.put( newKeyIdxCodeVisIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxTblCdVisX.get( existingKeyIdxTblCdVisX );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxTblCdVisX.containsKey( newKeyIdxTblCdVisX ) ) {
			subdict = dictByIdxTblCdVisX.get( newKeyIdxTblCdVisX );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByIdxTblCdVisX.put( newKeyIdxTblCdVisX, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndex >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteIndex( ICFSecAuthorization Authorization,
		ICFBamIndex iBuff )
	{
		final String S_ProcName = "CFBamRamIndexTable.deleteIndex() ";
		CFBamBuffIndex Buff = (CFBamBuffIndex)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffIndex existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteIndex",
				pkey );
		}
		CFBamBuffIndexCol buffDelIndexRefRelFromCols;
		ICFBamIndexCol arrDelIndexRefRelFromCols[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelIndexRefRelFromCols = 0; idxDelIndexRefRelFromCols < arrDelIndexRefRelFromCols.length; idxDelIndexRefRelFromCols++ ) {
			buffDelIndexRefRelFromCols = (CFBamBuffIndexCol)(arrDelIndexRefRelFromCols[idxDelIndexRefRelFromCols]);
					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						buffDelIndexRefRelFromCols.getRequiredId() );
		}
		CFBamBuffIndexCol buffDelIndexRefRelToCols;
		ICFBamIndexCol arrDelIndexRefRelToCols[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelIndexRefRelToCols = 0; idxDelIndexRefRelToCols < arrDelIndexRefRelToCols.length; idxDelIndexRefRelToCols++ ) {
			buffDelIndexRefRelToCols = (CFBamBuffIndexCol)(arrDelIndexRefRelToCols[idxDelIndexRefRelToCols]);
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						buffDelIndexRefRelToCols.getRequiredId() );
		}
					schema.getTableIndexCol().deleteIndexColByIndexIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffIndexByUNameIdxKey keyUNameIdx = (CFBamBuffIndexByUNameIdxKey)schema.getCFBamFactory().getFactoryIndex().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffIndexByIdxTableIdxKey keyIdxTableIdx = (CFBamBuffIndexByIdxTableIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTableIdxKey();
		keyIdxTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffIndexByIdxCodeVisIdxKey keyIdxCodeVisIdx = (CFBamBuffIndexByIdxCodeVisIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxCodeVisIdxKey();
		keyIdxCodeVisIdx.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffIndexByIdxTblCdVisXKey keyIdxTblCdVisX = (CFBamBuffIndexByIdxTblCdVisXKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTblCdVisXKey();
		keyIdxTblCdVisX.setRequiredTableId( existing.getRequiredTableId() );
		keyIdxTblCdVisX.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffIndexByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffIndexByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryIndex().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableRelation().readDerivedByFromKeyIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndex",
				"Lookup",
				"Lookup",
				"FromIndex",
				"FromIndex",
				"Relation",
				"Relation",
				pkey );
		}

		if( schema.getTableRelation().readDerivedByToKeyIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndex",
				"Lookup",
				"Lookup",
				"ToIndex",
				"ToIndex",
				"Relation",
				"Relation",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffIndex > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByIdxTableIdx.get( keyIdxTableIdx );
		subdict.remove( pkey );

		subdict = dictByIdxCodeVisIdx.get( keyIdxCodeVisIdx );
		subdict.remove( pkey );

		subdict = dictByIdxTblCdVisX.get( keyIdxTblCdVisX );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deleteIndexByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffIndexByUNameIdxKey key = (CFBamBuffIndexByUNameIdxKey)schema.getCFBamFactory().getFactoryIndex().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteIndexByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteIndexByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamIndexByUNameIdxKey argKey )
	{
		CFBamBuffIndex cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndex> matchSet = new LinkedList<CFBamBuffIndex>();
		Iterator<CFBamBuffIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndex)(schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndex( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexByIdxTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffIndexByIdxTableIdxKey key = (CFBamBuffIndexByIdxTableIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteIndexByIdxTableIdx( Authorization, key );
	}

	@Override
	public void deleteIndexByIdxTableIdx( ICFSecAuthorization Authorization,
		ICFBamIndexByIdxTableIdxKey argKey )
	{
		CFBamBuffIndex cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndex> matchSet = new LinkedList<CFBamBuffIndex>();
		Iterator<CFBamBuffIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndex)(schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndex( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexByIdxCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum argCodeVis )
	{
		CFBamBuffIndexByIdxCodeVisIdxKey key = (CFBamBuffIndexByIdxCodeVisIdxKey)schema.getCFBamFactory().getFactoryIndex().newByIdxCodeVisIdxKey();
		key.setRequiredCodeVis( argCodeVis );
		deleteIndexByIdxCodeVisIdx( Authorization, key );
	}

	@Override
	public void deleteIndexByIdxCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamIndexByIdxCodeVisIdxKey argKey )
	{
		CFBamBuffIndex cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndex> matchSet = new LinkedList<CFBamBuffIndex>();
		Iterator<CFBamBuffIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndex)(schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndex( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexByIdxTblCdVisX( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		ICFBamSchema.CodeVisibilityEnum argCodeVis )
	{
		CFBamBuffIndexByIdxTblCdVisXKey key = (CFBamBuffIndexByIdxTblCdVisXKey)schema.getCFBamFactory().getFactoryIndex().newByIdxTblCdVisXKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredCodeVis( argCodeVis );
		deleteIndexByIdxTblCdVisX( Authorization, key );
	}

	@Override
	public void deleteIndexByIdxTblCdVisX( ICFSecAuthorization Authorization,
		ICFBamIndexByIdxTblCdVisXKey argKey )
	{
		CFBamBuffIndex cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndex> matchSet = new LinkedList<CFBamBuffIndex>();
		Iterator<CFBamBuffIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndex)(schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndex( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffIndexByDefSchemaIdxKey key = (CFBamBuffIndexByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryIndex().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteIndexByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamIndexByDefSchemaIdxKey argKey )
	{
		CFBamBuffIndex cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndex> matchSet = new LinkedList<CFBamBuffIndex>();
		Iterator<CFBamBuffIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndex)(schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndex( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffIndex cur;
		LinkedList<CFBamBuffIndex> matchSet = new LinkedList<CFBamBuffIndex>();
		Iterator<CFBamBuffIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndex)(schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndex( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteIndexByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteIndexByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffIndex cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndex> matchSet = new LinkedList<CFBamBuffIndex>();
		Iterator<CFBamBuffIndex> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndex> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndex)(schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndex( Authorization, cur );
		}
	}
}
