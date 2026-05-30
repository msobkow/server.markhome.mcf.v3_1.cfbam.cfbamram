
// Description: Java 25 in-memory RAM DbIO implementation for RelationCol.

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
 *	CFBamRamRelationColTable in-memory RAM DbIO implementation
 *	for RelationCol.
 */
public class CFBamRamRelationColTable
	implements ICFBamRelationColTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffRelationCol > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffRelationCol >();
	private Map< CFBamBuffRelationColByUNameIdxKey,
			CFBamBuffRelationCol > dictByUNameIdx
		= new HashMap< CFBamBuffRelationColByUNameIdxKey,
			CFBamBuffRelationCol >();
	private Map< CFBamBuffRelationColByRelationIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByRelationIdx
		= new HashMap< CFBamBuffRelationColByRelationIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffRelationColByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByFromColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByFromColIdx
		= new HashMap< CFBamBuffRelationColByFromColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByToColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByToColIdx
		= new HashMap< CFBamBuffRelationColByToColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByPrevIdx
		= new HashMap< CFBamBuffRelationColByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByNextIdx
		= new HashMap< CFBamBuffRelationColByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByRelPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByRelPrevIdx
		= new HashMap< CFBamBuffRelationColByRelPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();
	private Map< CFBamBuffRelationColByRelNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >> dictByRelNextIdx
		= new HashMap< CFBamBuffRelationColByRelNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelationCol >>();

	public CFBamRamRelationColTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffRelationCol ensureRec(ICFBamRelationCol rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			if (classCode == ICFBamRelationCol.CLASS_CODE) {
				return( ((CFBamBuffRelationColDefaultFactory)(schema.getFactoryRelationCol())).ensureRec((ICFBamRelationCol)rec) );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), "ensureRec", "rec", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamRelationCol createRelationCol( ICFSecAuthorization Authorization,
		ICFBamRelationCol iBuff )
	{
		final String S_ProcName = "createRelationCol";
		
		CFBamBuffRelationCol Buff = (CFBamBuffRelationCol)ensureRec(iBuff);
			ICFBamRelationCol tail = null;

			ICFBamRelationCol[] siblings = schema.getTableRelationCol().readDerivedByRelationIdx( Authorization,
				Buff.getRequiredRelationId() );
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
		pkey = schema.nextRelationColIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffRelationColByUNameIdxKey keyUNameIdx = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		keyUNameIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffRelationColByRelationIdxKey keyRelationIdx = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffRelationColByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffRelationColByFromColIdxKey keyFromColIdx = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		keyFromColIdx.setRequiredFromColId( Buff.getRequiredFromColId() );

		CFBamBuffRelationColByToColIdxKey keyToColIdx = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		keyToColIdx.setRequiredToColId( Buff.getRequiredToColId() );

		CFBamBuffRelationColByPrevIdxKey keyPrevIdx = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffRelationColByNextIdxKey keyNextIdx = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffRelationColByRelPrevIdxKey keyRelPrevIdx = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		keyRelPrevIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		keyRelPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffRelationColByRelNextIdxKey keyRelNextIdx = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		keyRelNextIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		keyRelNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"RelationColUNameIdx",
				"RelationColUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"Relation",
						"Relation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
						Buff.getRequiredFromColId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"LookupFromCol",
						"LookupFromCol",
						"IndexCol",
						"IndexCol",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToColId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"LookupToCol",
						"LookupToCol",
						"IndexCol",
						"IndexCol",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelationIdx;
		if( dictByRelationIdx.containsKey( keyRelationIdx ) ) {
			subdictRelationIdx = dictByRelationIdx.get( keyRelationIdx );
		}
		else {
			subdictRelationIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelationIdx.put( keyRelationIdx, subdictRelationIdx );
		}
		subdictRelationIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictFromColIdx;
		if( dictByFromColIdx.containsKey( keyFromColIdx ) ) {
			subdictFromColIdx = dictByFromColIdx.get( keyFromColIdx );
		}
		else {
			subdictFromColIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByFromColIdx.put( keyFromColIdx, subdictFromColIdx );
		}
		subdictFromColIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictToColIdx;
		if( dictByToColIdx.containsKey( keyToColIdx ) ) {
			subdictToColIdx = dictByToColIdx.get( keyToColIdx );
		}
		else {
			subdictToColIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByToColIdx.put( keyToColIdx, subdictToColIdx );
		}
		subdictToColIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelPrevIdx;
		if( dictByRelPrevIdx.containsKey( keyRelPrevIdx ) ) {
			subdictRelPrevIdx = dictByRelPrevIdx.get( keyRelPrevIdx );
		}
		else {
			subdictRelPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelPrevIdx.put( keyRelPrevIdx, subdictRelPrevIdx );
		}
		subdictRelPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelNextIdx;
		if( dictByRelNextIdx.containsKey( keyRelNextIdx ) ) {
			subdictRelNextIdx = dictByRelNextIdx.get( keyRelNextIdx );
		}
		else {
			subdictRelNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelNextIdx.put( keyRelNextIdx, subdictRelNextIdx );
		}
		subdictRelNextIdx.put( pkey, Buff );

		if( tail != null ) {
			ICFBamRelationCol tailEdit = schema.getFactoryRelationCol().newRec();
			tailEdit.set( (ICFBamRelationCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
			schema.getTableRelationCol().updateRelationCol( Authorization, tailEdit );
		}
		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamRelationCol.CLASS_CODE) {
				CFBamBuffRelationCol retbuff = ((CFBamBuffRelationCol)(schema.getFactoryRelationCol().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamRelationCol readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerived";
		ICFBamRelationCol buff;
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
	public ICFBamRelationCol lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.lockDerived";
		ICFBamRelationCol buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelationCol[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamRelationCol.readAllDerived";
		ICFBamRelationCol[] retList = new ICFBamRelationCol[ dictByPKey.values().size() ];
		Iterator< CFBamBuffRelationCol > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamRelationCol readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByUNameIdx";
		CFBamBuffRelationColByUNameIdxKey key = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();

		key.setRequiredRelationId( RelationId );
		key.setRequiredName( Name );
		ICFBamRelationCol buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelationCol[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelationIdx";
		CFBamBuffRelationColByRelationIdxKey key = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();

		key.setRequiredRelationId( RelationId );
		ICFBamRelationCol[] recArray;
		if( dictByRelationIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelationIdx
				= dictByRelationIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictRelationIdx.size() ];
			Iterator< CFBamBuffRelationCol > iter = subdictRelationIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelationIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelationIdx.put( key, subdictRelationIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelationCol[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByDefSchemaIdx";
		CFBamBuffRelationColByDefSchemaIdxKey key = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamRelationCol[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffRelationCol > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelationCol[] readDerivedByFromColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByFromColIdx";
		CFBamBuffRelationColByFromColIdxKey key = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();

		key.setRequiredFromColId( FromColId );
		ICFBamRelationCol[] recArray;
		if( dictByFromColIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictFromColIdx
				= dictByFromColIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictFromColIdx.size() ];
			Iterator< CFBamBuffRelationCol > iter = subdictFromColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictFromColIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByFromColIdx.put( key, subdictFromColIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelationCol[] readDerivedByToColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByToColIdx";
		CFBamBuffRelationColByToColIdxKey key = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();

		key.setRequiredToColId( ToColId );
		ICFBamRelationCol[] recArray;
		if( dictByToColIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictToColIdx
				= dictByToColIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictToColIdx.size() ];
			Iterator< CFBamBuffRelationCol > iter = subdictToColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictToColIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByToColIdx.put( key, subdictToColIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelationCol[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByPrevIdx";
		CFBamBuffRelationColByPrevIdxKey key = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();

		key.setOptionalPrevId( PrevId );
		ICFBamRelationCol[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffRelationCol > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelationCol[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByNextIdx";
		CFBamBuffRelationColByNextIdxKey key = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();

		key.setOptionalNextId( NextId );
		ICFBamRelationCol[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictNextIdx.size() ];
			Iterator< CFBamBuffRelationCol > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelationCol[] readDerivedByRelPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelPrevIdx";
		CFBamBuffRelationColByRelPrevIdxKey key = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();

		key.setRequiredRelationId( RelationId );
		key.setOptionalPrevId( PrevId );
		ICFBamRelationCol[] recArray;
		if( dictByRelPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelPrevIdx
				= dictByRelPrevIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictRelPrevIdx.size() ];
			Iterator< CFBamBuffRelationCol > iter = subdictRelPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelPrevIdx.put( key, subdictRelPrevIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelationCol[] readDerivedByRelNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByRelNextIdx";
		CFBamBuffRelationColByRelNextIdxKey key = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();

		key.setRequiredRelationId( RelationId );
		key.setOptionalNextId( NextId );
		ICFBamRelationCol[] recArray;
		if( dictByRelNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelNextIdx
				= dictByRelNextIdx.get( key );
			recArray = new ICFBamRelationCol[ subdictRelNextIdx.size() ];
			Iterator< CFBamBuffRelationCol > iter = subdictRelNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdictRelNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelNextIdx.put( key, subdictRelNextIdx );
			recArray = new ICFBamRelationCol[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelationCol readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRelationCol.readDerivedByIdIdx() ";
		ICFBamRelationCol buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelationCol readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRec";
		ICFBamRelationCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamRelationCol.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelationCol lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamRelationCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamRelationCol.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelationCol[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamRelationCol.readAllRec";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	@Override
	public ICFBamRelationCol readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByIdIdx() ";
		ICFBamRelationCol buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
			return( (ICFBamRelationCol)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamRelationCol readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByUNameIdx() ";
		ICFBamRelationCol buff = readDerivedByUNameIdx( Authorization,
			RelationId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
			return( (ICFBamRelationCol)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamRelationCol[] readRecByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByRelationIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	@Override
	public ICFBamRelationCol[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByDefSchemaIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	@Override
	public ICFBamRelationCol[] readRecByFromColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByFromColIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByFromColIdx( Authorization,
			FromColId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	@Override
	public ICFBamRelationCol[] readRecByToColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToColId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByToColIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByToColIdx( Authorization,
			ToColId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	@Override
	public ICFBamRelationCol[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByPrevIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	@Override
	public ICFBamRelationCol[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByNextIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	@Override
	public ICFBamRelationCol[] readRecByRelPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByRelPrevIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByRelPrevIdx( Authorization,
			RelationId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	@Override
	public ICFBamRelationCol[] readRecByRelNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamRelationCol.readRecByRelNextIdx() ";
		ICFBamRelationCol buff;
		ArrayList<ICFBamRelationCol> filteredList = new ArrayList<ICFBamRelationCol>();
		ICFBamRelationCol[] buffList = readDerivedByRelNextIdx( Authorization,
			RelationId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelationCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelationCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelationCol[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamRelationCol moveRecUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecUp";

		ICFBamRelationCol grandprev = null;
		ICFBamRelationCol prev = null;
		ICFBamRelationCol cur = null;
		ICFBamRelationCol next = null;

		cur = schema.getTableRelationCol().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffRelationCol)cur );
		}

		prev = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() ));
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
			}
		}

		int classCode = prev.getClassCode();
		ICFBamRelationCol newInstance;
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffRelationCol editPrev = (CFBamBuffRelationCol)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffRelationCol editCur = (CFBamBuffRelationCol)newInstance;
		editCur.set( cur );

		CFBamBuffRelationCol editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = (CFBamBuffRelationCol)newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffRelationCol editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext = (CFBamBuffRelationCol)newInstance;
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
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffRelationCol)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamRelationCol moveRecDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecDown";

		CFBamBuffRelationCol prev = null;
		CFBamBuffRelationCol cur = null;
		CFBamBuffRelationCol next = null;
		CFBamBuffRelationCol grandnext = null;

		cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx(Authorization, Id));
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffRelationCol)cur );
		}

		next = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx(Authorization, next.getOptionalNextId() ));
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
			}
		}

		int classCode = cur.getClassCode();
		ICFBamRelationCol newInstance;
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffRelationCol editCur = (CFBamBuffRelationCol)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffRelationCol editNext = (CFBamBuffRelationCol)newInstance;
		editNext.set( next );

		CFBamBuffRelationCol editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = (CFBamBuffRelationCol)newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffRelationCol editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				newInstance = schema.getFactoryRelationCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev = (CFBamBuffRelationCol)newInstance;
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
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffRelationCol)editCur );
	}

	public ICFBamRelationCol updateRelationCol( ICFSecAuthorization Authorization,
		ICFBamRelationCol iBuff )
	{
		CFBamBuffRelationCol Buff = (CFBamBuffRelationCol)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffRelationCol existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateRelationCol",
				"Existing record not found",
				"Existing record not found",
				"RelationCol",
				"RelationCol",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateRelationCol",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffRelationColByUNameIdxKey existingKeyUNameIdx = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRelationColByUNameIdxKey newKeyUNameIdx = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffRelationColByRelationIdxKey existingKeyRelationIdx = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		existingKeyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffRelationColByRelationIdxKey newKeyRelationIdx = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		newKeyRelationIdx.setRequiredRelationId( Buff.getRequiredRelationId() );

		CFBamBuffRelationColByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffRelationColByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffRelationColByFromColIdxKey existingKeyFromColIdx = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		existingKeyFromColIdx.setRequiredFromColId( existing.getRequiredFromColId() );

		CFBamBuffRelationColByFromColIdxKey newKeyFromColIdx = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		newKeyFromColIdx.setRequiredFromColId( Buff.getRequiredFromColId() );

		CFBamBuffRelationColByToColIdxKey existingKeyToColIdx = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		existingKeyToColIdx.setRequiredToColId( existing.getRequiredToColId() );

		CFBamBuffRelationColByToColIdxKey newKeyToColIdx = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		newKeyToColIdx.setRequiredToColId( Buff.getRequiredToColId() );

		CFBamBuffRelationColByPrevIdxKey existingKeyPrevIdx = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffRelationColByPrevIdxKey newKeyPrevIdx = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffRelationColByNextIdxKey existingKeyNextIdx = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffRelationColByNextIdxKey newKeyNextIdx = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffRelationColByRelPrevIdxKey existingKeyRelPrevIdx = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		existingKeyRelPrevIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyRelPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffRelationColByRelPrevIdxKey newKeyRelPrevIdx = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		newKeyRelPrevIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyRelPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffRelationColByRelNextIdxKey existingKeyRelNextIdx = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		existingKeyRelNextIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		existingKeyRelNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffRelationColByRelNextIdxKey newKeyRelNextIdx = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		newKeyRelNextIdx.setRequiredRelationId( Buff.getRequiredRelationId() );
		newKeyRelNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRelationCol",
					"RelationColUNameIdx",
					"RelationColUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelationCol",
						"Container",
						"Container",
						"Relation",
						"Relation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
						Buff.getRequiredFromColId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelationCol",
						"Lookup",
						"Lookup",
						"LookupFromCol",
						"LookupFromCol",
						"IndexCol",
						"IndexCol",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToColId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelationCol",
						"Lookup",
						"Lookup",
						"LookupToCol",
						"LookupToCol",
						"IndexCol",
						"IndexCol",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByRelationIdx.get( existingKeyRelationIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelationIdx.containsKey( newKeyRelationIdx ) ) {
			subdict = dictByRelationIdx.get( newKeyRelationIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelationIdx.put( newKeyRelationIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByFromColIdx.get( existingKeyFromColIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByFromColIdx.containsKey( newKeyFromColIdx ) ) {
			subdict = dictByFromColIdx.get( newKeyFromColIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByFromColIdx.put( newKeyFromColIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByToColIdx.get( existingKeyToColIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByToColIdx.containsKey( newKeyToColIdx ) ) {
			subdict = dictByToColIdx.get( newKeyToColIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByToColIdx.put( newKeyToColIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByRelPrevIdx.get( existingKeyRelPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelPrevIdx.containsKey( newKeyRelPrevIdx ) ) {
			subdict = dictByRelPrevIdx.get( newKeyRelPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelPrevIdx.put( newKeyRelPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByRelNextIdx.get( existingKeyRelNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelNextIdx.containsKey( newKeyRelNextIdx ) ) {
			subdict = dictByRelNextIdx.get( newKeyRelNextIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelationCol >();
			dictByRelNextIdx.put( newKeyRelNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteRelationCol( ICFSecAuthorization Authorization,
		ICFBamRelationCol iBuff )
	{
		final String S_ProcName = "CFBamRamRelationColTable.deleteRelationCol() ";
		CFBamBuffRelationCol Buff = (CFBamBuffRelationCol)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffRelationCol existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteRelationCol",
				pkey );
		}
		CFLibDbKeyHash256 varRelationId = existing.getRequiredRelationId();
		CFBamBuffRelation container = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
			varRelationId ));
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamBuffRelationCol prev = null;
		if( ( prevId != null ) )
		{
			prev = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				prevId ));
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamBuffRelationCol editPrev;
			classCode = prev.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				editPrev = (CFBamBuffRelationCol)(schema.getFactoryRelationCol().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalLookupNext(nextId);
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffRelationCol next = null;
		if( ( nextId != null ) )
		{
			next = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				nextId ));
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamBuffRelationCol editNext;
			classCode = next.getClassCode();
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				editNext = (CFBamBuffRelationCol)(schema.getFactoryRelationCol().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalLookupPrev(prevId);
			if( classCode == ICFBamRelationCol.CLASS_CODE ) {
				schema.getTableRelationCol().updateRelationCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffRelationColByUNameIdxKey keyUNameIdx = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		keyUNameIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRelationColByRelationIdxKey keyRelationIdx = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		keyRelationIdx.setRequiredRelationId( existing.getRequiredRelationId() );

		CFBamBuffRelationColByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffRelationColByFromColIdxKey keyFromColIdx = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		keyFromColIdx.setRequiredFromColId( existing.getRequiredFromColId() );

		CFBamBuffRelationColByToColIdxKey keyToColIdx = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		keyToColIdx.setRequiredToColId( existing.getRequiredToColId() );

		CFBamBuffRelationColByPrevIdxKey keyPrevIdx = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffRelationColByNextIdxKey keyNextIdx = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffRelationColByRelPrevIdxKey keyRelPrevIdx = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		keyRelPrevIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyRelPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffRelationColByRelNextIdxKey keyRelNextIdx = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		keyRelNextIdx.setRequiredRelationId( existing.getRequiredRelationId() );
		keyRelNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffRelationCol > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByRelationIdx.get( keyRelationIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByFromColIdx.get( keyFromColIdx );
		subdict.remove( pkey );

		subdict = dictByToColIdx.get( keyToColIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		subdict = dictByRelPrevIdx.get( keyRelPrevIdx );
		subdict.remove( pkey );

		subdict = dictByRelNextIdx.get( keyRelNextIdx );
		subdict.remove( pkey );

	}
	@Override
	public void deleteRelationColByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffRelationCol cur;
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		String argName )
	{
		CFBamBuffRelationColByUNameIdxKey key = (CFBamBuffRelationColByUNameIdxKey)schema.getFactoryRelationCol().newByUNameIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setRequiredName( argName );
		deleteRelationColByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByUNameIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffRelationColByRelationIdxKey key = (CFBamBuffRelationColByRelationIdxKey)schema.getFactoryRelationCol().newByRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteRelationColByRelationIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByRelationIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffRelationColByDefSchemaIdxKey key = (CFBamBuffRelationColByDefSchemaIdxKey)schema.getFactoryRelationCol().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteRelationColByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByDefSchemaIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByFromColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argFromColId )
	{
		CFBamBuffRelationColByFromColIdxKey key = (CFBamBuffRelationColByFromColIdxKey)schema.getFactoryRelationCol().newByFromColIdxKey();
		key.setRequiredFromColId( argFromColId );
		deleteRelationColByFromColIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByFromColIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByFromColIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByToColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argToColId )
	{
		CFBamBuffRelationColByToColIdxKey key = (CFBamBuffRelationColByToColIdxKey)schema.getFactoryRelationCol().newByToColIdxKey();
		key.setRequiredToColId( argToColId );
		deleteRelationColByToColIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByToColIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByToColIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffRelationColByPrevIdxKey key = (CFBamBuffRelationColByPrevIdxKey)schema.getFactoryRelationCol().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteRelationColByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByPrevIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffRelationColByNextIdxKey key = (CFBamBuffRelationColByNextIdxKey)schema.getFactoryRelationCol().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteRelationColByNextIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByNextIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByNextIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByRelPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffRelationColByRelPrevIdxKey key = (CFBamBuffRelationColByRelPrevIdxKey)schema.getFactoryRelationCol().newByRelPrevIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setOptionalPrevId( argPrevId );
		deleteRelationColByRelPrevIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByRelPrevIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByRelPrevIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationColByRelNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffRelationColByRelNextIdxKey key = (CFBamBuffRelationColByRelNextIdxKey)schema.getFactoryRelationCol().newByRelNextIdxKey();
		key.setRequiredRelationId( argRelationId );
		key.setOptionalNextId( argNextId );
		deleteRelationColByRelNextIdx( Authorization, key );
	}

	@Override
	public void deleteRelationColByRelNextIdx( ICFSecAuthorization Authorization,
		ICFBamRelationColByRelNextIdxKey argKey )
	{
		CFBamBuffRelationCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelationCol> matchSet = new LinkedList<CFBamBuffRelationCol>();
		Iterator<CFBamBuffRelationCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelationCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelationCol)(schema.getTableRelationCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelationCol( Authorization, cur );
		}
	}
}
