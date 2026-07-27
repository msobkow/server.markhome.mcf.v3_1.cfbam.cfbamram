
// Description: Java 25 in-memory RAM DbIO implementation for EnumTag.

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
 *	CFBamRamEnumTagTable in-memory RAM DbIO implementation
 *	for EnumTag.
 */
public class CFBamRamEnumTagTable
	implements ICFBamEnumTagTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffEnumTag > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffEnumTag >();
	private Map< CFBamBuffEnumTagByEnumIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >> dictByEnumIdx
		= new HashMap< CFBamBuffEnumTagByEnumIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >>();
	private Map< CFBamBuffEnumTagByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffEnumTagByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >>();
	private Map< CFBamBuffEnumTagByEnumNameIdxKey,
			CFBamBuffEnumTag > dictByEnumNameIdx
		= new HashMap< CFBamBuffEnumTagByEnumNameIdxKey,
			CFBamBuffEnumTag >();
	private Map< CFBamBuffEnumTagByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >> dictByPrevIdx
		= new HashMap< CFBamBuffEnumTagByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >>();
	private Map< CFBamBuffEnumTagByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >> dictByNextIdx
		= new HashMap< CFBamBuffEnumTagByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >>();

	public CFBamRamEnumTagTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffEnumTag ensureRec(ICFBamEnumTag rec) {
		return (((CFBamBuffEnumTagFactoryService)(schema.getCFBamBuffFactory().getFactoryEnumTag())).ensureRec(rec));
	}

	@Override
	public ICFBamEnumTag createEnumTag( ICFSecAuthorization Authorization,
		ICFBamEnumTag iBuff )
	{
		final String S_ProcName = "createEnumTag";
		
		CFBamBuffEnumTag Buff = (CFBamBuffEnumTag)ensureRec(iBuff);
			ICFBamEnumTag tail = null;

			ICFBamEnumTag[] siblings = schema.getTableEnumTag().readDerivedByEnumIdx( Authorization,
				Buff.getRequiredEnumId() );
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
		pkey = schema.nextEnumTagIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffEnumTagByEnumIdxKey keyEnumIdx = (CFBamBuffEnumTagByEnumIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumIdxKey();
		keyEnumIdx.setRequiredEnumId( Buff.getRequiredEnumId() );

		CFBamBuffEnumTagByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffEnumTagByEnumNameIdxKey keyEnumNameIdx = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumNameIdxKey();
		keyEnumNameIdx.setRequiredEnumId( Buff.getRequiredEnumId() );
		keyEnumNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffEnumTagByPrevIdxKey keyPrevIdx = (CFBamBuffEnumTagByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffEnumTagByNextIdxKey keyNextIdx = (CFBamBuffEnumTagByNextIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByEnumNameIdx.containsKey( keyEnumNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"EnumTagEnumNameIdx",
				"EnumTagEnumNameIdx",
				keyEnumNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredEnumId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"EnumDef",
						"EnumDef",
						"EnumDef",
						"EnumDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictEnumIdx;
		if( dictByEnumIdx.containsKey( keyEnumIdx ) ) {
			subdictEnumIdx = dictByEnumIdx.get( keyEnumIdx );
		}
		else {
			subdictEnumIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByEnumIdx.put( keyEnumIdx, subdictEnumIdx );
		}
		subdictEnumIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByEnumNameIdx.put( keyEnumNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			ICFBamEnumTag tailEdit = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			tailEdit.set( (ICFBamEnumTag)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
			schema.getTableEnumTag().updateEnumTag( Authorization, tailEdit );
		}
		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamEnumTag.CLASS_CODE) {
				CFBamBuffEnumTag retbuff = ((CFBamBuffEnumTag)(schema.getCFBamBuffFactory().getFactoryEnumTag().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamEnumTag readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerived";
		ICFBamEnumTag buff;
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
	public ICFBamEnumTag lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.lockDerived";
		ICFBamEnumTag buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamEnumTag[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamEnumTag.readAllDerived";
		ICFBamEnumTag[] retList = new ICFBamEnumTag[ dictByPKey.values().size() ];
		Iterator< CFBamBuffEnumTag > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamEnumTag[] readDerivedByEnumIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 EnumId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByEnumIdx";
		CFBamBuffEnumTagByEnumIdxKey key = (CFBamBuffEnumTagByEnumIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumIdxKey();

		key.setRequiredEnumId( EnumId );
		ICFBamEnumTag[] recArray;
		if( dictByEnumIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictEnumIdx
				= dictByEnumIdx.get( key );
			recArray = new ICFBamEnumTag[ subdictEnumIdx.size() ];
			Iterator< CFBamBuffEnumTag > iter = subdictEnumIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictEnumIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByEnumIdx.put( key, subdictEnumIdx );
			recArray = new ICFBamEnumTag[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamEnumTag[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByDefSchemaIdx";
		CFBamBuffEnumTagByDefSchemaIdxKey key = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamEnumTag[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamEnumTag[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffEnumTag > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamEnumTag[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamEnumTag readDerivedByEnumNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 EnumId,
		String Name )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByEnumNameIdx";
		CFBamBuffEnumTagByEnumNameIdxKey key = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumNameIdxKey();

		key.setRequiredEnumId( EnumId );
		key.setRequiredName( Name );
		ICFBamEnumTag buff;
		if( dictByEnumNameIdx.containsKey( key ) ) {
			buff = dictByEnumNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamEnumTag[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByPrevIdx";
		CFBamBuffEnumTagByPrevIdxKey key = (CFBamBuffEnumTagByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByPrevIdxKey();

		key.setOptionalPrevId( PrevId );
		ICFBamEnumTag[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamEnumTag[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffEnumTag > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamEnumTag[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamEnumTag[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByNextIdx";
		CFBamBuffEnumTagByNextIdxKey key = (CFBamBuffEnumTagByNextIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByNextIdxKey();

		key.setOptionalNextId( NextId );
		ICFBamEnumTag[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamEnumTag[ subdictNextIdx.size() ];
			Iterator< CFBamBuffEnumTag > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamEnumTag[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamEnumTag readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByIdIdx() ";
		ICFBamEnumTag buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamEnumTag readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.readRec";
		ICFBamEnumTag buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamEnumTag.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamEnumTag lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamEnumTag buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamEnumTag.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamEnumTag[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamEnumTag.readAllRec";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	@Override
	public ICFBamEnumTag readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamEnumTag.readRecByIdIdx() ";
		ICFBamEnumTag buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
			return( (ICFBamEnumTag)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamEnumTag[] readRecByEnumIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 EnumId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readRecByEnumIdx() ";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readDerivedByEnumIdx( Authorization,
			EnumId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( (ICFBamEnumTag)buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	@Override
	public ICFBamEnumTag[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readRecByDefSchemaIdx() ";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( (ICFBamEnumTag)buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	@Override
	public ICFBamEnumTag readRecByEnumNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 EnumId,
		String Name )
	{
		final String S_ProcName = "CFBamRamEnumTag.readRecByEnumNameIdx() ";
		ICFBamEnumTag buff = readDerivedByEnumNameIdx( Authorization,
			EnumId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
			return( (ICFBamEnumTag)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamEnumTag[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readRecByPrevIdx() ";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( (ICFBamEnumTag)buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	@Override
	public ICFBamEnumTag[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readRecByNextIdx() ";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( (ICFBamEnumTag)buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamEnumTag moveRecUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecUp";

		ICFBamEnumTag grandprev = null;
		ICFBamEnumTag prev = null;
		ICFBamEnumTag cur = null;
		ICFBamEnumTag next = null;

		cur = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffEnumTag)cur );
		}

		prev = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() ));
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
			}
		}

		int classCode = prev.getClassCode();
		ICFBamEnumTag newInstance;
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffEnumTag editPrev = (CFBamBuffEnumTag)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffEnumTag editCur = (CFBamBuffEnumTag)newInstance;
		editCur.set( cur );

		CFBamBuffEnumTag editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = (CFBamBuffEnumTag)newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffEnumTag editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext = (CFBamBuffEnumTag)newInstance;
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
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffEnumTag)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamEnumTag moveRecDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecDown";

		CFBamBuffEnumTag prev = null;
		CFBamBuffEnumTag cur = null;
		CFBamBuffEnumTag next = null;
		CFBamBuffEnumTag grandnext = null;

		cur = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx(Authorization, Id));
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffEnumTag)cur );
		}

		next = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx(Authorization, next.getOptionalNextId() ));
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
			}
		}

		int classCode = cur.getClassCode();
		ICFBamEnumTag newInstance;
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffEnumTag editCur = (CFBamBuffEnumTag)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffEnumTag editNext = (CFBamBuffEnumTag)newInstance;
		editNext.set( next );

		CFBamBuffEnumTag editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = (CFBamBuffEnumTag)newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffEnumTag editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryEnumTag().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev = (CFBamBuffEnumTag)newInstance;
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
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffEnumTag)editCur );
	}

	public ICFBamEnumTag updateEnumTag( ICFSecAuthorization Authorization,
		ICFBamEnumTag iBuff )
	{
		CFBamBuffEnumTag Buff = (CFBamBuffEnumTag)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffEnumTag existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateEnumTag",
				"Existing record not found",
				"Existing record not found",
				"EnumTag",
				"EnumTag",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateEnumTag",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffEnumTagByEnumIdxKey existingKeyEnumIdx = (CFBamBuffEnumTagByEnumIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumIdxKey();
		existingKeyEnumIdx.setRequiredEnumId( existing.getRequiredEnumId() );

		CFBamBuffEnumTagByEnumIdxKey newKeyEnumIdx = (CFBamBuffEnumTagByEnumIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumIdxKey();
		newKeyEnumIdx.setRequiredEnumId( Buff.getRequiredEnumId() );

		CFBamBuffEnumTagByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffEnumTagByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffEnumTagByEnumNameIdxKey existingKeyEnumNameIdx = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumNameIdxKey();
		existingKeyEnumNameIdx.setRequiredEnumId( existing.getRequiredEnumId() );
		existingKeyEnumNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffEnumTagByEnumNameIdxKey newKeyEnumNameIdx = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumNameIdxKey();
		newKeyEnumNameIdx.setRequiredEnumId( Buff.getRequiredEnumId() );
		newKeyEnumNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffEnumTagByPrevIdxKey existingKeyPrevIdx = (CFBamBuffEnumTagByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffEnumTagByPrevIdxKey newKeyPrevIdx = (CFBamBuffEnumTagByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffEnumTagByNextIdxKey existingKeyNextIdx = (CFBamBuffEnumTagByNextIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffEnumTagByNextIdxKey newKeyNextIdx = (CFBamBuffEnumTagByNextIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyEnumNameIdx.equals( newKeyEnumNameIdx ) ) {
			if( dictByEnumNameIdx.containsKey( newKeyEnumNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateEnumTag",
					"EnumTagEnumNameIdx",
					"EnumTagEnumNameIdx",
					newKeyEnumNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredEnumId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateEnumTag",
						"Container",
						"Container",
						"EnumDef",
						"EnumDef",
						"EnumDef",
						"EnumDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByEnumIdx.get( existingKeyEnumIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByEnumIdx.containsKey( newKeyEnumIdx ) ) {
			subdict = dictByEnumIdx.get( newKeyEnumIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByEnumIdx.put( newKeyEnumIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByEnumNameIdx.remove( existingKeyEnumNameIdx );
		dictByEnumNameIdx.put( newKeyEnumNameIdx, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteEnumTag( ICFSecAuthorization Authorization,
		ICFBamEnumTag iBuff )
	{
		final String S_ProcName = "CFBamRamEnumTagTable.deleteEnumTag() ";
		CFBamBuffEnumTag Buff = (CFBamBuffEnumTag)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffEnumTag existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteEnumTag",
				pkey );
		}
		CFLibDbKeyHash256 varEnumId = existing.getRequiredEnumId();
		CFBamBuffEnumDef container = (CFBamBuffEnumDef)(schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
			varEnumId ));
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamBuffEnumTag prev = null;
		if( ( prevId != null ) )
		{
			prev = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				prevId ));
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamBuffEnumTag editPrev;
			classCode = prev.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				editPrev = (CFBamBuffEnumTag)(schema.getCFBamBuffFactory().getFactoryEnumTag().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalLookupNext(nextId);
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffEnumTag next = null;
		if( ( nextId != null ) )
		{
			next = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				nextId ));
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamBuffEnumTag editNext;
			classCode = next.getClassCode();
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				editNext = (CFBamBuffEnumTag)(schema.getCFBamBuffFactory().getFactoryEnumTag().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalLookupPrev(prevId);
			if( classCode == ICFBamEnumTag.CLASS_CODE ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffEnumTagByEnumIdxKey keyEnumIdx = (CFBamBuffEnumTagByEnumIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumIdxKey();
		keyEnumIdx.setRequiredEnumId( existing.getRequiredEnumId() );

		CFBamBuffEnumTagByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffEnumTagByEnumNameIdxKey keyEnumNameIdx = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumNameIdxKey();
		keyEnumNameIdx.setRequiredEnumId( existing.getRequiredEnumId() );
		keyEnumNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffEnumTagByPrevIdxKey keyPrevIdx = (CFBamBuffEnumTagByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffEnumTagByNextIdxKey keyNextIdx = (CFBamBuffEnumTagByNextIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByEnumIdx.get( keyEnumIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByEnumNameIdx.remove( keyEnumNameIdx );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

	}
	@Override
	public void deleteEnumTagByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffEnumTag cur;
		LinkedList<CFBamBuffEnumTag> matchSet = new LinkedList<CFBamBuffEnumTag>();
		Iterator<CFBamBuffEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteEnumTag( Authorization, cur );
		}
	}

	@Override
	public void deleteEnumTagByEnumIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argEnumId )
	{
		CFBamBuffEnumTagByEnumIdxKey key = (CFBamBuffEnumTagByEnumIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumIdxKey();
		key.setRequiredEnumId( argEnumId );
		deleteEnumTagByEnumIdx( Authorization, key );
	}

	@Override
	public void deleteEnumTagByEnumIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByEnumIdxKey argKey )
	{
		CFBamBuffEnumTag cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffEnumTag> matchSet = new LinkedList<CFBamBuffEnumTag>();
		Iterator<CFBamBuffEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteEnumTag( Authorization, cur );
		}
	}

	@Override
	public void deleteEnumTagByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffEnumTagByDefSchemaIdxKey key = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteEnumTagByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteEnumTagByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByDefSchemaIdxKey argKey )
	{
		CFBamBuffEnumTag cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffEnumTag> matchSet = new LinkedList<CFBamBuffEnumTag>();
		Iterator<CFBamBuffEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteEnumTag( Authorization, cur );
		}
	}

	@Override
	public void deleteEnumTagByEnumNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argEnumId,
		String argName )
	{
		CFBamBuffEnumTagByEnumNameIdxKey key = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByEnumNameIdxKey();
		key.setRequiredEnumId( argEnumId );
		key.setRequiredName( argName );
		deleteEnumTagByEnumNameIdx( Authorization, key );
	}

	@Override
	public void deleteEnumTagByEnumNameIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByEnumNameIdxKey argKey )
	{
		CFBamBuffEnumTag cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffEnumTag> matchSet = new LinkedList<CFBamBuffEnumTag>();
		Iterator<CFBamBuffEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteEnumTag( Authorization, cur );
		}
	}

	@Override
	public void deleteEnumTagByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffEnumTagByPrevIdxKey key = (CFBamBuffEnumTagByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteEnumTagByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteEnumTagByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByPrevIdxKey argKey )
	{
		CFBamBuffEnumTag cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffEnumTag> matchSet = new LinkedList<CFBamBuffEnumTag>();
		Iterator<CFBamBuffEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteEnumTag( Authorization, cur );
		}
	}

	@Override
	public void deleteEnumTagByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffEnumTagByNextIdxKey key = (CFBamBuffEnumTagByNextIdxKey)schema.getCFBamBuffFactory().getFactoryEnumTag().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteEnumTagByNextIdx( Authorization, key );
	}

	@Override
	public void deleteEnumTagByNextIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByNextIdxKey argKey )
	{
		CFBamBuffEnumTag cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffEnumTag> matchSet = new LinkedList<CFBamBuffEnumTag>();
		Iterator<CFBamBuffEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffEnumTag)(schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteEnumTag( Authorization, cur );
		}
	}
}
