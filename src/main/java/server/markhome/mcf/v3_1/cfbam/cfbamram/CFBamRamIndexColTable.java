
// Description: Java 25 in-memory RAM DbIO implementation for IndexCol.

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
 *	CFBamRamIndexColTable in-memory RAM DbIO implementation
 *	for IndexCol.
 */
public class CFBamRamIndexColTable
	implements ICFBamIndexColTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffIndexCol > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffIndexCol >();
	private Map< CFBamBuffIndexColByUNameIdxKey,
			CFBamBuffIndexCol > dictByUNameIdx
		= new HashMap< CFBamBuffIndexColByUNameIdxKey,
			CFBamBuffIndexCol >();
	private Map< CFBamBuffIndexColByIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByIndexIdx
		= new HashMap< CFBamBuffIndexColByIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffIndexColByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByColIdx
		= new HashMap< CFBamBuffIndexColByColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByPrevIdx
		= new HashMap< CFBamBuffIndexColByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByNextIdx
		= new HashMap< CFBamBuffIndexColByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByIdxPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByIdxPrevIdx
		= new HashMap< CFBamBuffIndexColByIdxPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByIdxNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByIdxNextIdx
		= new HashMap< CFBamBuffIndexColByIdxNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();

	public CFBamRamIndexColTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffIndexCol ensureRec(ICFBamIndexCol rec) {
		return (((CFBamBuffIndexColFactoryService)(schema.getCFBamBuffFactory().getFactoryIndexCol())).ensureRec(rec));
	}

	@Override
	public ICFBamIndexCol createIndexCol( ICFSecAuthorization Authorization,
		ICFBamIndexCol iBuff )
	{
		final String S_ProcName = "createIndexCol";
		
		CFBamBuffIndexCol Buff = (CFBamBuffIndexCol)ensureRec(iBuff);
			ICFBamIndexCol tail = null;

			ICFBamIndexCol[] siblings = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
				Buff.getRequiredIndexId() );
			for( int idx = 0; ( tail == null ) && ( idx < siblings.length ); idx ++ ) {
				if( ( siblings[idx].getOptionalNextId() == null ) )
				{
					tail = siblings[idx];
				}
			}
			if( tail != null ) {
				Buff.setOptionalLookupPrev(tail.getRequiredId());
			}
			else {
				Buff.setOptionalLookupPrev((CFLibDbKeyHash256)null);
			}
		
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextIndexColIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffIndexColByUNameIdxKey keyUNameIdx = (CFBamBuffIndexColByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByUNameIdxKey();
		keyUNameIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffIndexColByIndexIdxKey keyIndexIdx = (CFBamBuffIndexColByIndexIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIndexIdxKey();
		keyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

		CFBamBuffIndexColByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffIndexColByColIdxKey keyColIdx = (CFBamBuffIndexColByColIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByColIdxKey();
		keyColIdx.setRequiredColumnId( Buff.getRequiredColumnId() );

		CFBamBuffIndexColByPrevIdxKey keyPrevIdx = (CFBamBuffIndexColByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffIndexColByNextIdxKey keyNextIdx = (CFBamBuffIndexColByNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffIndexColByIdxPrevIdxKey keyIdxPrevIdx = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxPrevIdxKey();
		keyIdxPrevIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyIdxPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffIndexColByIdxNextIdxKey keyIdxNextIdx = (CFBamBuffIndexColByIdxNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxNextIdxKey();
		keyIdxNextIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyIdxNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"IndexColUNameIdx",
				"IndexColUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

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

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getRequiredColumnId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"Column",
						"Column",
						"Value",
						"Value",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIndexIdx;
		if( dictByIndexIdx.containsKey( keyIndexIdx ) ) {
			subdictIndexIdx = dictByIndexIdx.get( keyIndexIdx );
		}
		else {
			subdictIndexIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIndexIdx.put( keyIndexIdx, subdictIndexIdx );
		}
		subdictIndexIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictColIdx;
		if( dictByColIdx.containsKey( keyColIdx ) ) {
			subdictColIdx = dictByColIdx.get( keyColIdx );
		}
		else {
			subdictColIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByColIdx.put( keyColIdx, subdictColIdx );
		}
		subdictColIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxPrevIdx;
		if( dictByIdxPrevIdx.containsKey( keyIdxPrevIdx ) ) {
			subdictIdxPrevIdx = dictByIdxPrevIdx.get( keyIdxPrevIdx );
		}
		else {
			subdictIdxPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxPrevIdx.put( keyIdxPrevIdx, subdictIdxPrevIdx );
		}
		subdictIdxPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxNextIdx;
		if( dictByIdxNextIdx.containsKey( keyIdxNextIdx ) ) {
			subdictIdxNextIdx = dictByIdxNextIdx.get( keyIdxNextIdx );
		}
		else {
			subdictIdxNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxNextIdx.put( keyIdxNextIdx, subdictIdxNextIdx );
		}
		subdictIdxNextIdx.put( pkey, Buff );

		if( tail != null ) {
			ICFBamIndexCol tailEdit = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			tailEdit.set( (ICFBamIndexCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
			schema.getTableIndexCol().updateIndexCol( Authorization, tailEdit );
		}
		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamIndexCol.CLASS_CODE) {
				CFBamBuffIndexCol retbuff = ((CFBamBuffIndexCol)(schema.getCFBamBuffFactory().getFactoryIndexCol().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamIndexCol readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerived";
		ICFBamIndexCol buff;
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
	public ICFBamIndexCol lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.lockDerived";
		ICFBamIndexCol buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexCol[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndexCol.readAllDerived";
		ICFBamIndexCol[] retList = new ICFBamIndexCol[ dictByPKey.values().size() ];
		Iterator< CFBamBuffIndexCol > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamIndexCol readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByUNameIdx";
		CFBamBuffIndexColByUNameIdxKey key = (CFBamBuffIndexColByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByUNameIdxKey();

		key.setRequiredIndexId( IndexId );
		key.setRequiredName( Name );
		ICFBamIndexCol buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexCol[] readDerivedByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIndexIdx";
		CFBamBuffIndexColByIndexIdxKey key = (CFBamBuffIndexColByIndexIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIndexIdxKey();

		key.setRequiredIndexId( IndexId );
		ICFBamIndexCol[] recArray;
		if( dictByIndexIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIndexIdx
				= dictByIndexIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictIndexIdx.size() ];
			Iterator< CFBamBuffIndexCol > iter = subdictIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIndexIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIndexIdx.put( key, subdictIndexIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndexCol[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByDefSchemaIdx";
		CFBamBuffIndexColByDefSchemaIdxKey key = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamIndexCol[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffIndexCol > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndexCol[] readDerivedByColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ColumnId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByColIdx";
		CFBamBuffIndexColByColIdxKey key = (CFBamBuffIndexColByColIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByColIdxKey();

		key.setRequiredColumnId( ColumnId );
		ICFBamIndexCol[] recArray;
		if( dictByColIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictColIdx
				= dictByColIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictColIdx.size() ];
			Iterator< CFBamBuffIndexCol > iter = subdictColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictColIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByColIdx.put( key, subdictColIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndexCol[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByPrevIdx";
		CFBamBuffIndexColByPrevIdxKey key = (CFBamBuffIndexColByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByPrevIdxKey();

		key.setOptionalPrevId( PrevId );
		ICFBamIndexCol[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffIndexCol > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndexCol[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByNextIdx";
		CFBamBuffIndexColByNextIdxKey key = (CFBamBuffIndexColByNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByNextIdxKey();

		key.setOptionalNextId( NextId );
		ICFBamIndexCol[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictNextIdx.size() ];
			Iterator< CFBamBuffIndexCol > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndexCol[] readDerivedByIdxPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdxPrevIdx";
		CFBamBuffIndexColByIdxPrevIdxKey key = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxPrevIdxKey();

		key.setRequiredIndexId( IndexId );
		key.setOptionalPrevId( PrevId );
		ICFBamIndexCol[] recArray;
		if( dictByIdxPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxPrevIdx
				= dictByIdxPrevIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictIdxPrevIdx.size() ];
			Iterator< CFBamBuffIndexCol > iter = subdictIdxPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxPrevIdx.put( key, subdictIdxPrevIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndexCol[] readDerivedByIdxNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdxNextIdx";
		CFBamBuffIndexColByIdxNextIdxKey key = (CFBamBuffIndexColByIdxNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxNextIdxKey();

		key.setRequiredIndexId( IndexId );
		key.setOptionalNextId( NextId );
		ICFBamIndexCol[] recArray;
		if( dictByIdxNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxNextIdx
				= dictByIdxNextIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictIdxNextIdx.size() ];
			Iterator< CFBamBuffIndexCol > iter = subdictIdxNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxNextIdx.put( key, subdictIdxNextIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamIndexCol readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdIdx() ";
		ICFBamIndexCol buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexCol readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRec";
		ICFBamIndexCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndexCol.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexCol lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamIndexCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndexCol.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamIndexCol[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndexCol.readAllRec";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	@Override
	public ICFBamIndexCol readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByIdIdx() ";
		ICFBamIndexCol buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
			return( (ICFBamIndexCol)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndexCol readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByUNameIdx() ";
		ICFBamIndexCol buff = readDerivedByUNameIdx( Authorization,
			IndexId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
			return( (ICFBamIndexCol)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamIndexCol[] readRecByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByIndexIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByIndexIdx( Authorization,
			IndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	@Override
	public ICFBamIndexCol[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByDefSchemaIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	@Override
	public ICFBamIndexCol[] readRecByColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ColumnId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByColIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByColIdx( Authorization,
			ColumnId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	@Override
	public ICFBamIndexCol[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByPrevIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	@Override
	public ICFBamIndexCol[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByNextIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	@Override
	public ICFBamIndexCol[] readRecByIdxPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByIdxPrevIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByIdxPrevIdx( Authorization,
			IndexId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	@Override
	public ICFBamIndexCol[] readRecByIdxNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readRecByIdxNextIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByIdxNextIdx( Authorization,
			IndexId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamIndexCol moveRecUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecUp";

		ICFBamIndexCol grandprev = null;
		ICFBamIndexCol prev = null;
		ICFBamIndexCol cur = null;
		ICFBamIndexCol next = null;

		cur = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffIndexCol)cur );
		}

		prev = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() ));
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
			}
		}

		int classCode = prev.getClassCode();
		ICFBamIndexCol newInstance;
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffIndexCol editPrev = (CFBamBuffIndexCol)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffIndexCol editCur = (CFBamBuffIndexCol)newInstance;
		editCur.set( cur );

		CFBamBuffIndexCol editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = (CFBamBuffIndexCol)newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffIndexCol editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext = (CFBamBuffIndexCol)newInstance;
			editNext.set( next );
		}

		if( editGrandprev != null ) {
			editGrandprev.setOptionalLookupNext(cur.getRequiredId());
			editCur.setOptionalLookupPrev(grandprev.getRequiredId());
		}
		else {
			editCur.setOptionalLookupPrev((CFLibDbKeyHash256)null);
		}

			editPrev.setOptionalLookupPrev(cur.getRequiredId());

			editCur.setOptionalLookupNext(prev.getRequiredId());

		if( next != null ) {
			editPrev.setOptionalLookupNext(next.getRequiredId());
			editNext.setOptionalLookupPrev(prev.getRequiredId());
		}
		else {
			editPrev.setOptionalLookupNext((CFLibDbKeyHash256)null);
		}

		if( editGrandprev != null ) {
			classCode = editGrandprev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffIndexCol)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamIndexCol moveRecDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecDown";

		CFBamBuffIndexCol prev = null;
		CFBamBuffIndexCol cur = null;
		CFBamBuffIndexCol next = null;
		CFBamBuffIndexCol grandnext = null;

		cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx(Authorization, Id));
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffIndexCol)cur );
		}

		next = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx(Authorization, next.getOptionalNextId() ));
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
			}
		}

		int classCode = cur.getClassCode();
		ICFBamIndexCol newInstance;
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffIndexCol editCur = (CFBamBuffIndexCol)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffIndexCol editNext = (CFBamBuffIndexCol)newInstance;
		editNext.set( next );

		CFBamBuffIndexCol editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = (CFBamBuffIndexCol)newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffIndexCol editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryIndexCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev = (CFBamBuffIndexCol)newInstance;
			editPrev.set( prev );
		}

		if( prev != null ) {
			editPrev.setOptionalLookupNext(next.getRequiredId());
			editNext.setOptionalLookupPrev(prev.getRequiredId());
		}
		else {
			editNext.setOptionalLookupPrev((CFLibDbKeyHash256)null);
		}

			editCur.setOptionalLookupPrev(next.getRequiredId());

			editNext.setOptionalLookupNext(cur.getRequiredId());

		if( editGrandnext != null ) {
			editCur.setOptionalLookupNext(grandnext.getRequiredId());
			editGrandnext.setOptionalLookupPrev(cur.getRequiredId());
		}
		else {
			editCur.setOptionalLookupNext((CFLibDbKeyHash256)null);
		}

		if( editPrev != null ) {
			classCode = editPrev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffIndexCol)editCur );
	}

	public ICFBamIndexCol updateIndexCol( ICFSecAuthorization Authorization,
		ICFBamIndexCol iBuff )
	{
		CFBamBuffIndexCol Buff = (CFBamBuffIndexCol)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffIndexCol existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndexCol",
				"Existing record not found",
				"Existing record not found",
				"IndexCol",
				"IndexCol",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateIndexCol",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffIndexColByUNameIdxKey existingKeyUNameIdx = (CFBamBuffIndexColByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffIndexColByUNameIdxKey newKeyUNameIdx = (CFBamBuffIndexColByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffIndexColByIndexIdxKey existingKeyIndexIdx = (CFBamBuffIndexColByIndexIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIndexIdxKey();
		existingKeyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		CFBamBuffIndexColByIndexIdxKey newKeyIndexIdx = (CFBamBuffIndexColByIndexIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIndexIdxKey();
		newKeyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

		CFBamBuffIndexColByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffIndexColByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffIndexColByColIdxKey existingKeyColIdx = (CFBamBuffIndexColByColIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByColIdxKey();
		existingKeyColIdx.setRequiredColumnId( existing.getRequiredColumnId() );

		CFBamBuffIndexColByColIdxKey newKeyColIdx = (CFBamBuffIndexColByColIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByColIdxKey();
		newKeyColIdx.setRequiredColumnId( Buff.getRequiredColumnId() );

		CFBamBuffIndexColByPrevIdxKey existingKeyPrevIdx = (CFBamBuffIndexColByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffIndexColByPrevIdxKey newKeyPrevIdx = (CFBamBuffIndexColByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffIndexColByNextIdxKey existingKeyNextIdx = (CFBamBuffIndexColByNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffIndexColByNextIdxKey newKeyNextIdx = (CFBamBuffIndexColByNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffIndexColByIdxPrevIdxKey existingKeyIdxPrevIdx = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxPrevIdxKey();
		existingKeyIdxPrevIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyIdxPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffIndexColByIdxPrevIdxKey newKeyIdxPrevIdx = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxPrevIdxKey();
		newKeyIdxPrevIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyIdxPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffIndexColByIdxNextIdxKey existingKeyIdxNextIdx = (CFBamBuffIndexColByIdxNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxNextIdxKey();
		existingKeyIdxNextIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyIdxNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffIndexColByIdxNextIdxKey newKeyIdxNextIdx = (CFBamBuffIndexColByIdxNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxNextIdxKey();
		newKeyIdxNextIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyIdxNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateIndexCol",
					"IndexColUNameIdx",
					"IndexColUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexCol",
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

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getRequiredColumnId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexCol",
						"Lookup",
						"Lookup",
						"Column",
						"Column",
						"Value",
						"Value",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByIndexIdx.get( existingKeyIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIndexIdx.containsKey( newKeyIndexIdx ) ) {
			subdict = dictByIndexIdx.get( newKeyIndexIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIndexIdx.put( newKeyIndexIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByColIdx.get( existingKeyColIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByColIdx.containsKey( newKeyColIdx ) ) {
			subdict = dictByColIdx.get( newKeyColIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByColIdx.put( newKeyColIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByPrevIdx.put( newKeyPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextIdx.get( existingKeyNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextIdx.containsKey( newKeyNextIdx ) ) {
			subdict = dictByNextIdx.get( newKeyNextIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxPrevIdx.get( existingKeyIdxPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxPrevIdx.containsKey( newKeyIdxPrevIdx ) ) {
			subdict = dictByIdxPrevIdx.get( newKeyIdxPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxPrevIdx.put( newKeyIdxPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxNextIdx.get( existingKeyIdxNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxNextIdx.containsKey( newKeyIdxNextIdx ) ) {
			subdict = dictByIdxNextIdx.get( newKeyIdxNextIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxNextIdx.put( newKeyIdxNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteIndexCol( ICFSecAuthorization Authorization,
		ICFBamIndexCol iBuff )
	{
		final String S_ProcName = "CFBamRamIndexColTable.deleteIndexCol() ";
		CFBamBuffIndexCol Buff = (CFBamBuffIndexCol)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffIndexCol existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteIndexCol",
				pkey );
		}
		CFLibDbKeyHash256 varIndexId = existing.getRequiredIndexId();
		CFBamBuffIndex container = (CFBamBuffIndex)(schema.getTableIndex().readDerivedByIdIdx( Authorization,
			varIndexId ));
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamBuffIndexCol prev = null;
		if( ( prevId != null ) )
		{
			prev = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				prevId ));
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamBuffIndexCol editPrev;
			classCode = prev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				editPrev = (CFBamBuffIndexCol)(schema.getCFBamBuffFactory().getFactoryIndexCol().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalLookupNext(nextId);
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffIndexCol next = null;
		if( ( nextId != null ) )
		{
			next = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				nextId ));
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamBuffIndexCol editNext;
			classCode = next.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				editNext = (CFBamBuffIndexCol)(schema.getCFBamBuffFactory().getFactoryIndexCol().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalLookupPrev(prevId);
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffIndexColByUNameIdxKey keyUNameIdx = (CFBamBuffIndexColByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByUNameIdxKey();
		keyUNameIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffIndexColByIndexIdxKey keyIndexIdx = (CFBamBuffIndexColByIndexIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIndexIdxKey();
		keyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		CFBamBuffIndexColByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffIndexColByColIdxKey keyColIdx = (CFBamBuffIndexColByColIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByColIdxKey();
		keyColIdx.setRequiredColumnId( existing.getRequiredColumnId() );

		CFBamBuffIndexColByPrevIdxKey keyPrevIdx = (CFBamBuffIndexColByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffIndexColByNextIdxKey keyNextIdx = (CFBamBuffIndexColByNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffIndexColByIdxPrevIdxKey keyIdxPrevIdx = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxPrevIdxKey();
		keyIdxPrevIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyIdxPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffIndexColByIdxNextIdxKey keyIdxNextIdx = (CFBamBuffIndexColByIdxNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxNextIdxKey();
		keyIdxNextIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyIdxNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		if( schema.getTableRelationCol().readDerivedByFromColIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndexCol",
				"Lookup",
				"Lookup",
				"LookupFromCol",
				"LookupFromCol",
				"RelationCol",
				"RelationCol",
				pkey );
		}

		if( schema.getTableRelationCol().readDerivedByToColIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndexCol",
				"Lookup",
				"Lookup",
				"LookupToCol",
				"LookupToCol",
				"RelationCol",
				"RelationCol",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByIndexIdx.get( keyIndexIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByColIdx.get( keyColIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		subdict = dictByIdxPrevIdx.get( keyIdxPrevIdx );
		subdict.remove( pkey );

		subdict = dictByIdxNextIdx.get( keyIdxNextIdx );
		subdict.remove( pkey );

	}
	@Override
	public void deleteIndexColByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffIndexCol cur;
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexColByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId,
		String argName )
	{
		CFBamBuffIndexColByUNameIdxKey key = (CFBamBuffIndexColByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByUNameIdxKey();
		key.setRequiredIndexId( argIndexId );
		key.setRequiredName( argName );
		deleteIndexColByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteIndexColByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByUNameIdxKey argKey )
	{
		CFBamBuffIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexColByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId )
	{
		CFBamBuffIndexColByIndexIdxKey key = (CFBamBuffIndexColByIndexIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIndexIdxKey();
		key.setRequiredIndexId( argIndexId );
		deleteIndexColByIndexIdx( Authorization, key );
	}

	@Override
	public void deleteIndexColByIndexIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByIndexIdxKey argKey )
	{
		CFBamBuffIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexColByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffIndexColByDefSchemaIdxKey key = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexColByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteIndexColByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByDefSchemaIdxKey argKey )
	{
		CFBamBuffIndexCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexColByColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argColumnId )
	{
		CFBamBuffIndexColByColIdxKey key = (CFBamBuffIndexColByColIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByColIdxKey();
		key.setRequiredColumnId( argColumnId );
		deleteIndexColByColIdx( Authorization, key );
	}

	@Override
	public void deleteIndexColByColIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByColIdxKey argKey )
	{
		CFBamBuffIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexColByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffIndexColByPrevIdxKey key = (CFBamBuffIndexColByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteIndexColByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteIndexColByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByPrevIdxKey argKey )
	{
		CFBamBuffIndexCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexColByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffIndexColByNextIdxKey key = (CFBamBuffIndexColByNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteIndexColByNextIdx( Authorization, key );
	}

	@Override
	public void deleteIndexColByNextIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByNextIdxKey argKey )
	{
		CFBamBuffIndexCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexColByIdxPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffIndexColByIdxPrevIdxKey key = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxPrevIdxKey();
		key.setRequiredIndexId( argIndexId );
		key.setOptionalPrevId( argPrevId );
		deleteIndexColByIdxPrevIdx( Authorization, key );
	}

	@Override
	public void deleteIndexColByIdxPrevIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByIdxPrevIdxKey argKey )
	{
		CFBamBuffIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}

	@Override
	public void deleteIndexColByIdxNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffIndexColByIdxNextIdxKey key = (CFBamBuffIndexColByIdxNextIdxKey)schema.getCFBamBuffFactory().getFactoryIndexCol().newByIdxNextIdxKey();
		key.setRequiredIndexId( argIndexId );
		key.setOptionalNextId( argNextId );
		deleteIndexColByIdxNextIdx( Authorization, key );
	}

	@Override
	public void deleteIndexColByIdxNextIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByIdxNextIdxKey argKey )
	{
		CFBamBuffIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffIndexCol> matchSet = new LinkedList<CFBamBuffIndexCol>();
		Iterator<CFBamBuffIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffIndexCol)(schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteIndexCol( Authorization, cur );
		}
	}
}
