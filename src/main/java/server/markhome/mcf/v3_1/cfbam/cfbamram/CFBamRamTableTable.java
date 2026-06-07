
// Description: Java 25 in-memory RAM DbIO implementation for Table.

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
 *	CFBamRamTableTable in-memory RAM DbIO implementation
 *	for Table.
 */
public class CFBamRamTableTable
	implements ICFBamTableTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffTable > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffTable >();
	private Map< CFBamBuffTableBySchemaDefIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >> dictBySchemaDefIdx
		= new HashMap< CFBamBuffTableBySchemaDefIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >>();
	private Map< CFBamBuffTableByCodeVisIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >> dictByCodeVisIdx
		= new HashMap< CFBamBuffTableByCodeVisIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >>();
	private Map< CFBamBuffTableBySchemaCodeVisIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >> dictBySchemaCodeVisIdx
		= new HashMap< CFBamBuffTableBySchemaCodeVisIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >>();
	private Map< CFBamBuffTableByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffTableByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >>();
	private Map< CFBamBuffTableByUNameIdxKey,
			CFBamBuffTable > dictByUNameIdx
		= new HashMap< CFBamBuffTableByUNameIdxKey,
			CFBamBuffTable >();
	private Map< CFBamBuffTableBySchemaCdIdxKey,
			CFBamBuffTable > dictBySchemaCdIdx
		= new HashMap< CFBamBuffTableBySchemaCdIdxKey,
			CFBamBuffTable >();
	private Map< CFBamBuffTableByPrimaryIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >> dictByPrimaryIndexIdx
		= new HashMap< CFBamBuffTableByPrimaryIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >>();
	private Map< CFBamBuffTableByLookupIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >> dictByLookupIndexIdx
		= new HashMap< CFBamBuffTableByLookupIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >>();
	private Map< CFBamBuffTableByAltIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >> dictByAltIndexIdx
		= new HashMap< CFBamBuffTableByAltIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >>();
	private Map< CFBamBuffTableByQualTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >> dictByQualTableIdx
		= new HashMap< CFBamBuffTableByQualTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffTable >>();

	public CFBamRamTableTable( ICFBamSchema argSchema ) {
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
	public ICFBamTable createTable( ICFSecAuthorization Authorization,
		ICFBamTable iBuff )
	{
		final String S_ProcName = "createTable";
		
		CFBamBuffTable Buff = (CFBamBuffTable)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffTableBySchemaDefIdxKey keySchemaDefIdx = (CFBamBuffTableBySchemaDefIdxKey)schema.getFactoryTable().newBySchemaDefIdxKey();
		keySchemaDefIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		CFBamBuffTableByCodeVisIdxKey keyCodeVisIdx = (CFBamBuffTableByCodeVisIdxKey)schema.getFactoryTable().newByCodeVisIdxKey();
		keyCodeVisIdx.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffTableBySchemaCodeVisIdxKey keySchemaCodeVisIdx = (CFBamBuffTableBySchemaCodeVisIdxKey)schema.getFactoryTable().newBySchemaCodeVisIdxKey();
		keySchemaCodeVisIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		keySchemaCodeVisIdx.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffTableByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffTableByDefSchemaIdxKey)schema.getFactoryTable().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffTableByUNameIdxKey keyUNameIdx = (CFBamBuffTableByUNameIdxKey)schema.getFactoryTable().newByUNameIdxKey();
		keyUNameIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffTableBySchemaCdIdxKey keySchemaCdIdx = (CFBamBuffTableBySchemaCdIdxKey)schema.getFactoryTable().newBySchemaCdIdxKey();
		keySchemaCdIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		keySchemaCdIdx.setRequiredTableClassCode( Buff.getRequiredTableClassCode() );

		CFBamBuffTableByPrimaryIndexIdxKey keyPrimaryIndexIdx = (CFBamBuffTableByPrimaryIndexIdxKey)schema.getFactoryTable().newByPrimaryIndexIdxKey();
		keyPrimaryIndexIdx.setOptionalPrimaryIndexId( Buff.getOptionalPrimaryIndexId() );

		CFBamBuffTableByLookupIndexIdxKey keyLookupIndexIdx = (CFBamBuffTableByLookupIndexIdxKey)schema.getFactoryTable().newByLookupIndexIdxKey();
		keyLookupIndexIdx.setOptionalLookupIndexId( Buff.getOptionalLookupIndexId() );

		CFBamBuffTableByAltIndexIdxKey keyAltIndexIdx = (CFBamBuffTableByAltIndexIdxKey)schema.getFactoryTable().newByAltIndexIdxKey();
		keyAltIndexIdx.setOptionalAltIndexId( Buff.getOptionalAltIndexId() );

		CFBamBuffTableByQualTableIdxKey keyQualTableIdx = (CFBamBuffTableByQualTableIdxKey)schema.getFactoryTable().newByQualTableIdxKey();
		keyQualTableIdx.setOptionalQualifyingTableId( Buff.getOptionalQualifyingTableId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"TableUNameIdx",
				"TableUNameIdx",
				keyUNameIdx );
		}

		if( dictBySchemaCdIdx.containsKey( keySchemaCdIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"TableSchemaCodeIdx",
				"TableSchemaCodeIdx",
				keySchemaCdIdx );
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"SchemaDef",
						"SchemaDef",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdictSchemaDefIdx;
		if( dictBySchemaDefIdx.containsKey( keySchemaDefIdx ) ) {
			subdictSchemaDefIdx = dictBySchemaDefIdx.get( keySchemaDefIdx );
		}
		else {
			subdictSchemaDefIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictBySchemaDefIdx.put( keySchemaDefIdx, subdictSchemaDefIdx );
		}
		subdictSchemaDefIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdictCodeVisIdx;
		if( dictByCodeVisIdx.containsKey( keyCodeVisIdx ) ) {
			subdictCodeVisIdx = dictByCodeVisIdx.get( keyCodeVisIdx );
		}
		else {
			subdictCodeVisIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByCodeVisIdx.put( keyCodeVisIdx, subdictCodeVisIdx );
		}
		subdictCodeVisIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdictSchemaCodeVisIdx;
		if( dictBySchemaCodeVisIdx.containsKey( keySchemaCodeVisIdx ) ) {
			subdictSchemaCodeVisIdx = dictBySchemaCodeVisIdx.get( keySchemaCodeVisIdx );
		}
		else {
			subdictSchemaCodeVisIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictBySchemaCodeVisIdx.put( keySchemaCodeVisIdx, subdictSchemaCodeVisIdx );
		}
		subdictSchemaCodeVisIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		dictBySchemaCdIdx.put( keySchemaCdIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdictPrimaryIndexIdx;
		if( dictByPrimaryIndexIdx.containsKey( keyPrimaryIndexIdx ) ) {
			subdictPrimaryIndexIdx = dictByPrimaryIndexIdx.get( keyPrimaryIndexIdx );
		}
		else {
			subdictPrimaryIndexIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByPrimaryIndexIdx.put( keyPrimaryIndexIdx, subdictPrimaryIndexIdx );
		}
		subdictPrimaryIndexIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdictLookupIndexIdx;
		if( dictByLookupIndexIdx.containsKey( keyLookupIndexIdx ) ) {
			subdictLookupIndexIdx = dictByLookupIndexIdx.get( keyLookupIndexIdx );
		}
		else {
			subdictLookupIndexIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByLookupIndexIdx.put( keyLookupIndexIdx, subdictLookupIndexIdx );
		}
		subdictLookupIndexIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdictAltIndexIdx;
		if( dictByAltIndexIdx.containsKey( keyAltIndexIdx ) ) {
			subdictAltIndexIdx = dictByAltIndexIdx.get( keyAltIndexIdx );
		}
		else {
			subdictAltIndexIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByAltIndexIdx.put( keyAltIndexIdx, subdictAltIndexIdx );
		}
		subdictAltIndexIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdictQualTableIdx;
		if( dictByQualTableIdx.containsKey( keyQualTableIdx ) ) {
			subdictQualTableIdx = dictByQualTableIdx.get( keyQualTableIdx );
		}
		else {
			subdictQualTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByQualTableIdx.put( keyQualTableIdx, subdictQualTableIdx );
		}
		subdictQualTableIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamTable.CLASS_CODE) {
				CFBamBuffTable retbuff = ((CFBamBuffTable)(schema.getFactoryTable().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamTable readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTable.readDerived";
		ICFBamTable buff;
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
	public ICFBamTable lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTable.lockDerived";
		ICFBamTable buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTable[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamTable.readAllDerived";
		ICFBamTable[] retList = new ICFBamTable[ dictByPKey.values().size() ];
		Iterator< CFBamBuffTable > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamTable[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamTable ) ) {
					filteredList.add( (ICFBamTable)buff );
				}
			}
			return( filteredList.toArray( new ICFBamTable[0] ) );
		}
	}

	@Override
	public ICFBamTable[] readDerivedBySchemaDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedBySchemaDefIdx";
		CFBamBuffTableBySchemaDefIdxKey key = (CFBamBuffTableBySchemaDefIdxKey)schema.getFactoryTable().newBySchemaDefIdxKey();

		key.setRequiredSchemaDefId( SchemaDefId );
		ICFBamTable[] recArray;
		if( dictBySchemaDefIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictSchemaDefIdx
				= dictBySchemaDefIdx.get( key );
			recArray = new ICFBamTable[ subdictSchemaDefIdx.size() ];
			Iterator< CFBamBuffTable > iter = subdictSchemaDefIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictSchemaDefIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictBySchemaDefIdx.put( key, subdictSchemaDefIdx );
			recArray = new ICFBamTable[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTable[] readDerivedByCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByCodeVisIdx";
		CFBamBuffTableByCodeVisIdxKey key = (CFBamBuffTableByCodeVisIdxKey)schema.getFactoryTable().newByCodeVisIdxKey();

		key.setRequiredCodeVis( CodeVis );
		ICFBamTable[] recArray;
		if( dictByCodeVisIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictCodeVisIdx
				= dictByCodeVisIdx.get( key );
			recArray = new ICFBamTable[ subdictCodeVisIdx.size() ];
			Iterator< CFBamBuffTable > iter = subdictCodeVisIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictCodeVisIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByCodeVisIdx.put( key, subdictCodeVisIdx );
			recArray = new ICFBamTable[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTable[] readDerivedBySchemaCodeVisIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedBySchemaCodeVisIdx";
		CFBamBuffTableBySchemaCodeVisIdxKey key = (CFBamBuffTableBySchemaCodeVisIdxKey)schema.getFactoryTable().newBySchemaCodeVisIdxKey();

		key.setRequiredSchemaDefId( SchemaDefId );
		key.setRequiredCodeVis( CodeVis );
		ICFBamTable[] recArray;
		if( dictBySchemaCodeVisIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictSchemaCodeVisIdx
				= dictBySchemaCodeVisIdx.get( key );
			recArray = new ICFBamTable[ subdictSchemaCodeVisIdx.size() ];
			Iterator< CFBamBuffTable > iter = subdictSchemaCodeVisIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictSchemaCodeVisIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictBySchemaCodeVisIdx.put( key, subdictSchemaCodeVisIdx );
			recArray = new ICFBamTable[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTable[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByDefSchemaIdx";
		CFBamBuffTableByDefSchemaIdxKey key = (CFBamBuffTableByDefSchemaIdxKey)schema.getFactoryTable().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamTable[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamTable[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffTable > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamTable[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTable readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByUNameIdx";
		CFBamBuffTableByUNameIdxKey key = (CFBamBuffTableByUNameIdxKey)schema.getFactoryTable().newByUNameIdxKey();

		key.setRequiredSchemaDefId( SchemaDefId );
		key.setRequiredName( Name );
		ICFBamTable buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTable readDerivedBySchemaCdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		String TableClassCode )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedBySchemaCdIdx";
		CFBamBuffTableBySchemaCdIdxKey key = (CFBamBuffTableBySchemaCdIdxKey)schema.getFactoryTable().newBySchemaCdIdxKey();

		key.setRequiredSchemaDefId( SchemaDefId );
		key.setRequiredTableClassCode( TableClassCode );
		ICFBamTable buff;
		if( dictBySchemaCdIdx.containsKey( key ) ) {
			buff = dictBySchemaCdIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTable[] readDerivedByPrimaryIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrimaryIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByPrimaryIndexIdx";
		CFBamBuffTableByPrimaryIndexIdxKey key = (CFBamBuffTableByPrimaryIndexIdxKey)schema.getFactoryTable().newByPrimaryIndexIdxKey();

		key.setOptionalPrimaryIndexId( PrimaryIndexId );
		ICFBamTable[] recArray;
		if( dictByPrimaryIndexIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictPrimaryIndexIdx
				= dictByPrimaryIndexIdx.get( key );
			recArray = new ICFBamTable[ subdictPrimaryIndexIdx.size() ];
			Iterator< CFBamBuffTable > iter = subdictPrimaryIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictPrimaryIndexIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByPrimaryIndexIdx.put( key, subdictPrimaryIndexIdx );
			recArray = new ICFBamTable[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTable[] readDerivedByLookupIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 LookupIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByLookupIndexIdx";
		CFBamBuffTableByLookupIndexIdxKey key = (CFBamBuffTableByLookupIndexIdxKey)schema.getFactoryTable().newByLookupIndexIdxKey();

		key.setOptionalLookupIndexId( LookupIndexId );
		ICFBamTable[] recArray;
		if( dictByLookupIndexIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictLookupIndexIdx
				= dictByLookupIndexIdx.get( key );
			recArray = new ICFBamTable[ subdictLookupIndexIdx.size() ];
			Iterator< CFBamBuffTable > iter = subdictLookupIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictLookupIndexIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByLookupIndexIdx.put( key, subdictLookupIndexIdx );
			recArray = new ICFBamTable[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTable[] readDerivedByAltIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 AltIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByAltIndexIdx";
		CFBamBuffTableByAltIndexIdxKey key = (CFBamBuffTableByAltIndexIdxKey)schema.getFactoryTable().newByAltIndexIdxKey();

		key.setOptionalAltIndexId( AltIndexId );
		ICFBamTable[] recArray;
		if( dictByAltIndexIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictAltIndexIdx
				= dictByAltIndexIdx.get( key );
			recArray = new ICFBamTable[ subdictAltIndexIdx.size() ];
			Iterator< CFBamBuffTable > iter = subdictAltIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictAltIndexIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByAltIndexIdx.put( key, subdictAltIndexIdx );
			recArray = new ICFBamTable[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTable[] readDerivedByQualTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 QualifyingTableId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByQualTableIdx";
		CFBamBuffTableByQualTableIdxKey key = (CFBamBuffTableByQualTableIdxKey)schema.getFactoryTable().newByQualTableIdxKey();

		key.setOptionalQualifyingTableId( QualifyingTableId );
		ICFBamTable[] recArray;
		if( dictByQualTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictQualTableIdx
				= dictByQualTableIdx.get( key );
			recArray = new ICFBamTable[ subdictQualTableIdx.size() ];
			Iterator< CFBamBuffTable > iter = subdictQualTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffTable > subdictQualTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByQualTableIdx.put( key, subdictQualTableIdx );
			recArray = new ICFBamTable[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamTable readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamTable buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTable readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamTable.readRec";
		ICFBamTable buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamTable.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTable lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamTable buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamTable.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamTable[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamTable.readAllRec";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamTable buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamTable)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTable[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable[] readRecBySchemaDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamTable.readRecBySchemaDefIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedBySchemaDefIdx( Authorization,
			SchemaDefId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable[] readRecByCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamTable.readRecByCodeVisIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedByCodeVisIdx( Authorization,
			CodeVis );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable[] readRecBySchemaCodeVisIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamTable.readRecBySchemaCodeVisIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedBySchemaCodeVisIdx( Authorization,
			SchemaDefId,
			CodeVis );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTable.readRecByDefSchemaIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTable.readRecByUNameIdx() ";
		ICFBamTable buff = readDerivedByUNameIdx( Authorization,
			SchemaDefId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
			return( (ICFBamTable)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTable readRecBySchemaCdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		String TableClassCode )
	{
		final String S_ProcName = "CFBamRamTable.readRecBySchemaCdIdx() ";
		ICFBamTable buff = readDerivedBySchemaCdIdx( Authorization,
			SchemaDefId,
			TableClassCode );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
			return( (ICFBamTable)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamTable[] readRecByPrimaryIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrimaryIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readRecByPrimaryIndexIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedByPrimaryIndexIdx( Authorization,
			PrimaryIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable[] readRecByLookupIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 LookupIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readRecByLookupIndexIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedByLookupIndexIdx( Authorization,
			LookupIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable[] readRecByAltIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 AltIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readRecByAltIndexIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedByAltIndexIdx( Authorization,
			AltIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	@Override
	public ICFBamTable[] readRecByQualTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 QualifyingTableId )
	{
		final String S_ProcName = "CFBamRamTable.readRecByQualTableIdx() ";
		ICFBamTable buff;
		ArrayList<ICFBamTable> filteredList = new ArrayList<ICFBamTable>();
		ICFBamTable[] buffList = readDerivedByQualTableIdx( Authorization,
			QualifyingTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamTable.CLASS_CODE ) ) {
				filteredList.add( (ICFBamTable)buff );
			}
		}
		return( filteredList.toArray( new ICFBamTable[0] ) );
	}

	public ICFBamTable updateTable( ICFSecAuthorization Authorization,
		ICFBamTable iBuff )
	{
		CFBamBuffTable Buff = (CFBamBuffTable)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffTable existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateTable",
				"Existing record not found",
				"Existing record not found",
				"Table",
				"Table",
				pkey );
		}
		CFBamBuffTableBySchemaDefIdxKey existingKeySchemaDefIdx = (CFBamBuffTableBySchemaDefIdxKey)schema.getFactoryTable().newBySchemaDefIdxKey();
		existingKeySchemaDefIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamBuffTableBySchemaDefIdxKey newKeySchemaDefIdx = (CFBamBuffTableBySchemaDefIdxKey)schema.getFactoryTable().newBySchemaDefIdxKey();
		newKeySchemaDefIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		CFBamBuffTableByCodeVisIdxKey existingKeyCodeVisIdx = (CFBamBuffTableByCodeVisIdxKey)schema.getFactoryTable().newByCodeVisIdxKey();
		existingKeyCodeVisIdx.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffTableByCodeVisIdxKey newKeyCodeVisIdx = (CFBamBuffTableByCodeVisIdxKey)schema.getFactoryTable().newByCodeVisIdxKey();
		newKeyCodeVisIdx.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffTableBySchemaCodeVisIdxKey existingKeySchemaCodeVisIdx = (CFBamBuffTableBySchemaCodeVisIdxKey)schema.getFactoryTable().newBySchemaCodeVisIdxKey();
		existingKeySchemaCodeVisIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		existingKeySchemaCodeVisIdx.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffTableBySchemaCodeVisIdxKey newKeySchemaCodeVisIdx = (CFBamBuffTableBySchemaCodeVisIdxKey)schema.getFactoryTable().newBySchemaCodeVisIdxKey();
		newKeySchemaCodeVisIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		newKeySchemaCodeVisIdx.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffTableByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffTableByDefSchemaIdxKey)schema.getFactoryTable().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffTableByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffTableByDefSchemaIdxKey)schema.getFactoryTable().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffTableByUNameIdxKey existingKeyUNameIdx = (CFBamBuffTableByUNameIdxKey)schema.getFactoryTable().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffTableByUNameIdxKey newKeyUNameIdx = (CFBamBuffTableByUNameIdxKey)schema.getFactoryTable().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffTableBySchemaCdIdxKey existingKeySchemaCdIdx = (CFBamBuffTableBySchemaCdIdxKey)schema.getFactoryTable().newBySchemaCdIdxKey();
		existingKeySchemaCdIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		existingKeySchemaCdIdx.setRequiredTableClassCode( existing.getRequiredTableClassCode() );

		CFBamBuffTableBySchemaCdIdxKey newKeySchemaCdIdx = (CFBamBuffTableBySchemaCdIdxKey)schema.getFactoryTable().newBySchemaCdIdxKey();
		newKeySchemaCdIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		newKeySchemaCdIdx.setRequiredTableClassCode( Buff.getRequiredTableClassCode() );

		CFBamBuffTableByPrimaryIndexIdxKey existingKeyPrimaryIndexIdx = (CFBamBuffTableByPrimaryIndexIdxKey)schema.getFactoryTable().newByPrimaryIndexIdxKey();
		existingKeyPrimaryIndexIdx.setOptionalPrimaryIndexId( existing.getOptionalPrimaryIndexId() );

		CFBamBuffTableByPrimaryIndexIdxKey newKeyPrimaryIndexIdx = (CFBamBuffTableByPrimaryIndexIdxKey)schema.getFactoryTable().newByPrimaryIndexIdxKey();
		newKeyPrimaryIndexIdx.setOptionalPrimaryIndexId( Buff.getOptionalPrimaryIndexId() );

		CFBamBuffTableByLookupIndexIdxKey existingKeyLookupIndexIdx = (CFBamBuffTableByLookupIndexIdxKey)schema.getFactoryTable().newByLookupIndexIdxKey();
		existingKeyLookupIndexIdx.setOptionalLookupIndexId( existing.getOptionalLookupIndexId() );

		CFBamBuffTableByLookupIndexIdxKey newKeyLookupIndexIdx = (CFBamBuffTableByLookupIndexIdxKey)schema.getFactoryTable().newByLookupIndexIdxKey();
		newKeyLookupIndexIdx.setOptionalLookupIndexId( Buff.getOptionalLookupIndexId() );

		CFBamBuffTableByAltIndexIdxKey existingKeyAltIndexIdx = (CFBamBuffTableByAltIndexIdxKey)schema.getFactoryTable().newByAltIndexIdxKey();
		existingKeyAltIndexIdx.setOptionalAltIndexId( existing.getOptionalAltIndexId() );

		CFBamBuffTableByAltIndexIdxKey newKeyAltIndexIdx = (CFBamBuffTableByAltIndexIdxKey)schema.getFactoryTable().newByAltIndexIdxKey();
		newKeyAltIndexIdx.setOptionalAltIndexId( Buff.getOptionalAltIndexId() );

		CFBamBuffTableByQualTableIdxKey existingKeyQualTableIdx = (CFBamBuffTableByQualTableIdxKey)schema.getFactoryTable().newByQualTableIdxKey();
		existingKeyQualTableIdx.setOptionalQualifyingTableId( existing.getOptionalQualifyingTableId() );

		CFBamBuffTableByQualTableIdxKey newKeyQualTableIdx = (CFBamBuffTableByQualTableIdxKey)schema.getFactoryTable().newByQualTableIdxKey();
		newKeyQualTableIdx.setOptionalQualifyingTableId( Buff.getOptionalQualifyingTableId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateTable",
					"TableUNameIdx",
					"TableUNameIdx",
					newKeyUNameIdx );
			}
		}

		if( ! existingKeySchemaCdIdx.equals( newKeySchemaCdIdx ) ) {
			if( dictBySchemaCdIdx.containsKey( newKeySchemaCdIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateTable",
					"TableSchemaCodeIdx",
					"TableSchemaCodeIdx",
					newKeySchemaCdIdx );
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
						"updateTable",
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTable",
						"Container",
						"Container",
						"SchemaDef",
						"SchemaDef",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffTable > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictBySchemaDefIdx.get( existingKeySchemaDefIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaDefIdx.containsKey( newKeySchemaDefIdx ) ) {
			subdict = dictBySchemaDefIdx.get( newKeySchemaDefIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictBySchemaDefIdx.put( newKeySchemaDefIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByCodeVisIdx.get( existingKeyCodeVisIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByCodeVisIdx.containsKey( newKeyCodeVisIdx ) ) {
			subdict = dictByCodeVisIdx.get( newKeyCodeVisIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByCodeVisIdx.put( newKeyCodeVisIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictBySchemaCodeVisIdx.get( existingKeySchemaCodeVisIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaCodeVisIdx.containsKey( newKeySchemaCodeVisIdx ) ) {
			subdict = dictBySchemaCodeVisIdx.get( newKeySchemaCodeVisIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictBySchemaCodeVisIdx.put( newKeySchemaCodeVisIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		dictBySchemaCdIdx.remove( existingKeySchemaCdIdx );
		dictBySchemaCdIdx.put( newKeySchemaCdIdx, Buff );

		subdict = dictByPrimaryIndexIdx.get( existingKeyPrimaryIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrimaryIndexIdx.containsKey( newKeyPrimaryIndexIdx ) ) {
			subdict = dictByPrimaryIndexIdx.get( newKeyPrimaryIndexIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByPrimaryIndexIdx.put( newKeyPrimaryIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByLookupIndexIdx.get( existingKeyLookupIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByLookupIndexIdx.containsKey( newKeyLookupIndexIdx ) ) {
			subdict = dictByLookupIndexIdx.get( newKeyLookupIndexIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByLookupIndexIdx.put( newKeyLookupIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByAltIndexIdx.get( existingKeyAltIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByAltIndexIdx.containsKey( newKeyAltIndexIdx ) ) {
			subdict = dictByAltIndexIdx.get( newKeyAltIndexIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByAltIndexIdx.put( newKeyAltIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByQualTableIdx.get( existingKeyQualTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByQualTableIdx.containsKey( newKeyQualTableIdx ) ) {
			subdict = dictByQualTableIdx.get( newKeyQualTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffTable >();
			dictByQualTableIdx.put( newKeyQualTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteTable( ICFSecAuthorization Authorization,
		ICFBamTable iBuff )
	{
		final String S_ProcName = "CFBamRamTableTable.deleteTable() ";
		CFBamBuffTable Buff = (CFBamBuffTable)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffTable existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteTable",
				pkey );
		}
					{
						CFBamBuffTable editBuff = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
						existing.getRequiredId() ));
						editBuff.setOptionalLookupPrimaryIndex((CFLibDbKeyHash256)null);
						classCode = editBuff.getClassCode();
						if( classCode == ICFBamTable.CLASS_CODE ) {
							schema.getTableTable().updateTable( Authorization, editBuff );
						}
						else {
							throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-clear-top-dep-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
						}
					}
		CFBamBuffTable editSubobj = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
			existing.getRequiredId() ));
			editSubobj.setOptionalLookupLookupIndex((CFLibDbKeyHash256)null);
			editSubobj.setOptionalLookupAltIndex((CFLibDbKeyHash256)null);
			editSubobj.setOptionalLookupPrimaryIndex((CFLibDbKeyHash256)null);
		classCode = editSubobj.getClassCode();
		if( classCode == ICFBamTable.CLASS_CODE ) {
			schema.getTableTable().updateTable( Authorization, editSubobj );
		}
		else {
			throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-clear-root-subobject-refs-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
		}
		existing = editSubobj;
					schema.getTableServerMethod().deleteServerMethodByMethTableIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableDelTopDep().deleteDelTopDepByDelTopDepTblIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableClearTopDep().deleteClearTopDepByClrTopDepTblIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableChain().deleteChainByChainTableIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffRelation buffDelTableRelationPopDep;
		ICFBamRelation arrDelTableRelationPopDep[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationPopDep = 0; idxDelTableRelationPopDep < arrDelTableRelationPopDep.length; idxDelTableRelationPopDep++ ) {
			buffDelTableRelationPopDep = (CFBamBuffRelation)(arrDelTableRelationPopDep[idxDelTableRelationPopDep]);
					schema.getTablePopTopDep().deletePopTopDepByContRelIdx( Authorization,
						buffDelTableRelationPopDep.getRequiredId() );
		}
		CFBamBuffRelation buffDelTableRelationCol;
		ICFBamRelation arrDelTableRelationCol[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationCol = 0; idxDelTableRelationCol < arrDelTableRelationCol.length; idxDelTableRelationCol++ ) {
			buffDelTableRelationCol = (CFBamBuffRelation)(arrDelTableRelationCol[idxDelTableRelationCol]);
					schema.getTableRelationCol().deleteRelationColByRelationIdx( Authorization,
						buffDelTableRelationCol.getRequiredId() );
		}
					schema.getTableRelation().deleteRelationByRelTableIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffIndex buffDelTableIndexRefRelFmCol;
		ICFBamIndex arrDelTableIndexRefRelFmCol[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexRefRelFmCol = 0; idxDelTableIndexRefRelFmCol < arrDelTableIndexRefRelFmCol.length; idxDelTableIndexRefRelFmCol++ ) {
			buffDelTableIndexRefRelFmCol = (CFBamBuffIndex)(arrDelTableIndexRefRelFmCol[idxDelTableIndexRefRelFmCol]);
			CFBamBuffIndexCol buffColumns;
			ICFBamIndexCol arrColumns[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
				buffDelTableIndexRefRelFmCol.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = (CFBamBuffIndexCol)(arrColumns[idxColumns]);
					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
		}
		CFBamBuffIndex buffDelTableIndexRefRelToCol;
		ICFBamIndex arrDelTableIndexRefRelToCol[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexRefRelToCol = 0; idxDelTableIndexRefRelToCol < arrDelTableIndexRefRelToCol.length; idxDelTableIndexRefRelToCol++ ) {
			buffDelTableIndexRefRelToCol = (CFBamBuffIndex)(arrDelTableIndexRefRelToCol[idxDelTableIndexRefRelToCol]);
			CFBamBuffIndexCol buffColumns;
			ICFBamIndexCol arrColumns[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
				buffDelTableIndexRefRelToCol.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = (CFBamBuffIndexCol)(arrColumns[idxColumns]);
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
		}
		CFBamBuffIndex buffDelTableIndexCol;
		ICFBamIndex arrDelTableIndexCol[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexCol = 0; idxDelTableIndexCol < arrDelTableIndexCol.length; idxDelTableIndexCol++ ) {
			buffDelTableIndexCol = (CFBamBuffIndex)(arrDelTableIndexCol[idxDelTableIndexCol]);
					schema.getTableIndexCol().deleteIndexColByIndexIdx( Authorization,
						buffDelTableIndexCol.getRequiredId() );
		}
					schema.getTableIndex().deleteIndexByIdxTableIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffValue buffDelTableRefIndexColumns;
		ICFBamValue arrDelTableRefIndexColumns[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRefIndexColumns = 0; idxDelTableRefIndexColumns < arrDelTableRefIndexColumns.length; idxDelTableRefIndexColumns++ ) {
			buffDelTableRefIndexColumns = (CFBamBuffValue)(arrDelTableRefIndexColumns[idxDelTableRefIndexColumns]);
					schema.getTableIndexCol().deleteIndexColByColIdx( Authorization,
						buffDelTableRefIndexColumns.getRequiredId() );
		}
					schema.getTableValue().deleteValueByScopeIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffTableBySchemaDefIdxKey keySchemaDefIdx = (CFBamBuffTableBySchemaDefIdxKey)schema.getFactoryTable().newBySchemaDefIdxKey();
		keySchemaDefIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamBuffTableByCodeVisIdxKey keyCodeVisIdx = (CFBamBuffTableByCodeVisIdxKey)schema.getFactoryTable().newByCodeVisIdxKey();
		keyCodeVisIdx.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffTableBySchemaCodeVisIdxKey keySchemaCodeVisIdx = (CFBamBuffTableBySchemaCodeVisIdxKey)schema.getFactoryTable().newBySchemaCodeVisIdxKey();
		keySchemaCodeVisIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		keySchemaCodeVisIdx.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffTableByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffTableByDefSchemaIdxKey)schema.getFactoryTable().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffTableByUNameIdxKey keyUNameIdx = (CFBamBuffTableByUNameIdxKey)schema.getFactoryTable().newByUNameIdxKey();
		keyUNameIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffTableBySchemaCdIdxKey keySchemaCdIdx = (CFBamBuffTableBySchemaCdIdxKey)schema.getFactoryTable().newBySchemaCdIdxKey();
		keySchemaCdIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		keySchemaCdIdx.setRequiredTableClassCode( existing.getRequiredTableClassCode() );

		CFBamBuffTableByPrimaryIndexIdxKey keyPrimaryIndexIdx = (CFBamBuffTableByPrimaryIndexIdxKey)schema.getFactoryTable().newByPrimaryIndexIdxKey();
		keyPrimaryIndexIdx.setOptionalPrimaryIndexId( existing.getOptionalPrimaryIndexId() );

		CFBamBuffTableByLookupIndexIdxKey keyLookupIndexIdx = (CFBamBuffTableByLookupIndexIdxKey)schema.getFactoryTable().newByLookupIndexIdxKey();
		keyLookupIndexIdx.setOptionalLookupIndexId( existing.getOptionalLookupIndexId() );

		CFBamBuffTableByAltIndexIdxKey keyAltIndexIdx = (CFBamBuffTableByAltIndexIdxKey)schema.getFactoryTable().newByAltIndexIdxKey();
		keyAltIndexIdx.setOptionalAltIndexId( existing.getOptionalAltIndexId() );

		CFBamBuffTableByQualTableIdxKey keyQualTableIdx = (CFBamBuffTableByQualTableIdxKey)schema.getFactoryTable().newByQualTableIdxKey();
		keyQualTableIdx.setOptionalQualifyingTableId( existing.getOptionalQualifyingTableId() );

		// Validate reverse foreign keys

		if( schema.getTableRelation().readDerivedByToTblIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteTable",
				"Lookup",
				"Lookup",
				"ToTable",
				"ToTable",
				"Relation",
				"Relation",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffTable > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaDefIdx.get( keySchemaDefIdx );
		subdict.remove( pkey );

		subdict = dictByCodeVisIdx.get( keyCodeVisIdx );
		subdict.remove( pkey );

		subdict = dictBySchemaCodeVisIdx.get( keySchemaCodeVisIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		dictBySchemaCdIdx.remove( keySchemaCdIdx );

		subdict = dictByPrimaryIndexIdx.get( keyPrimaryIndexIdx );
		subdict.remove( pkey );

		subdict = dictByLookupIndexIdx.get( keyLookupIndexIdx );
		subdict.remove( pkey );

		subdict = dictByAltIndexIdx.get( keyAltIndexIdx );
		subdict.remove( pkey );

		subdict = dictByQualTableIdx.get( keyQualTableIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deleteTableBySchemaDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId )
	{
		CFBamBuffTableBySchemaDefIdxKey key = (CFBamBuffTableBySchemaDefIdxKey)schema.getFactoryTable().newBySchemaDefIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		deleteTableBySchemaDefIdx( Authorization, key );
	}

	@Override
	public void deleteTableBySchemaDefIdx( ICFSecAuthorization Authorization,
		ICFBamTableBySchemaDefIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum argCodeVis )
	{
		CFBamBuffTableByCodeVisIdxKey key = (CFBamBuffTableByCodeVisIdxKey)schema.getFactoryTable().newByCodeVisIdxKey();
		key.setRequiredCodeVis( argCodeVis );
		deleteTableByCodeVisIdx( Authorization, key );
	}

	@Override
	public void deleteTableByCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamTableByCodeVisIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableBySchemaCodeVisIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId,
		ICFBamSchema.CodeVisibilityEnum argCodeVis )
	{
		CFBamBuffTableBySchemaCodeVisIdxKey key = (CFBamBuffTableBySchemaCodeVisIdxKey)schema.getFactoryTable().newBySchemaCodeVisIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		key.setRequiredCodeVis( argCodeVis );
		deleteTableBySchemaCodeVisIdx( Authorization, key );
	}

	@Override
	public void deleteTableBySchemaCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamTableBySchemaCodeVisIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffTableByDefSchemaIdxKey key = (CFBamBuffTableByDefSchemaIdxKey)schema.getFactoryTable().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteTableByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteTableByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamTableByDefSchemaIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId,
		String argName )
	{
		CFBamBuffTableByUNameIdxKey key = (CFBamBuffTableByUNameIdxKey)schema.getFactoryTable().newByUNameIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		key.setRequiredName( argName );
		deleteTableByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteTableByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamTableByUNameIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableBySchemaCdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId,
		String argTableClassCode )
	{
		CFBamBuffTableBySchemaCdIdxKey key = (CFBamBuffTableBySchemaCdIdxKey)schema.getFactoryTable().newBySchemaCdIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		key.setRequiredTableClassCode( argTableClassCode );
		deleteTableBySchemaCdIdx( Authorization, key );
	}

	@Override
	public void deleteTableBySchemaCdIdx( ICFSecAuthorization Authorization,
		ICFBamTableBySchemaCdIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByPrimaryIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrimaryIndexId )
	{
		CFBamBuffTableByPrimaryIndexIdxKey key = (CFBamBuffTableByPrimaryIndexIdxKey)schema.getFactoryTable().newByPrimaryIndexIdxKey();
		key.setOptionalPrimaryIndexId( argPrimaryIndexId );
		deleteTableByPrimaryIndexIdx( Authorization, key );
	}

	@Override
	public void deleteTableByPrimaryIndexIdx( ICFSecAuthorization Authorization,
		ICFBamTableByPrimaryIndexIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrimaryIndexId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByLookupIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argLookupIndexId )
	{
		CFBamBuffTableByLookupIndexIdxKey key = (CFBamBuffTableByLookupIndexIdxKey)schema.getFactoryTable().newByLookupIndexIdxKey();
		key.setOptionalLookupIndexId( argLookupIndexId );
		deleteTableByLookupIndexIdx( Authorization, key );
	}

	@Override
	public void deleteTableByLookupIndexIdx( ICFSecAuthorization Authorization,
		ICFBamTableByLookupIndexIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalLookupIndexId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByAltIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argAltIndexId )
	{
		CFBamBuffTableByAltIndexIdxKey key = (CFBamBuffTableByAltIndexIdxKey)schema.getFactoryTable().newByAltIndexIdxKey();
		key.setOptionalAltIndexId( argAltIndexId );
		deleteTableByAltIndexIdx( Authorization, key );
	}

	@Override
	public void deleteTableByAltIndexIdx( ICFSecAuthorization Authorization,
		ICFBamTableByAltIndexIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalAltIndexId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByQualTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argQualifyingTableId )
	{
		CFBamBuffTableByQualTableIdxKey key = (CFBamBuffTableByQualTableIdxKey)schema.getFactoryTable().newByQualTableIdxKey();
		key.setOptionalQualifyingTableId( argQualifyingTableId );
		deleteTableByQualTableIdx( Authorization, key );
	}

	@Override
	public void deleteTableByQualTableIdx( ICFSecAuthorization Authorization,
		ICFBamTableByQualTableIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalQualifyingTableId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffTable cur;
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}

	@Override
	public void deleteTableByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteTableByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteTableByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffTable cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffTable> matchSet = new LinkedList<CFBamBuffTable>();
		Iterator<CFBamBuffTable> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffTable> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteTable( Authorization, cur );
		}
	}
}
