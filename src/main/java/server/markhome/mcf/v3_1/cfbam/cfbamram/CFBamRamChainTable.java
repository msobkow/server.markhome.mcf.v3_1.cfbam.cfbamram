
// Description: Java 25 in-memory RAM DbIO implementation for Chain.

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
 *	CFBamRamChainTable in-memory RAM DbIO implementation
 *	for Chain.
 */
public class CFBamRamChainTable
	implements ICFBamChainTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffChain > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffChain >();
	private Map< CFBamBuffChainByChainTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >> dictByChainTableIdx
		= new HashMap< CFBamBuffChainByChainTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >>();
	private Map< CFBamBuffChainByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffChainByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >>();
	private Map< CFBamBuffChainByUNameIdxKey,
			CFBamBuffChain > dictByUNameIdx
		= new HashMap< CFBamBuffChainByUNameIdxKey,
			CFBamBuffChain >();
	private Map< CFBamBuffChainByPrevRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >> dictByPrevRelIdx
		= new HashMap< CFBamBuffChainByPrevRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >>();
	private Map< CFBamBuffChainByNextRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >> dictByNextRelIdx
		= new HashMap< CFBamBuffChainByNextRelIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffChain >>();

	public CFBamRamChainTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffChain ensureRec(ICFBamChain rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			switch (classCode) {
				case ICFBamChain.CLASS_CODE:
					return(((CFBamBuffChainFactoryService)(schema.getCFBamFactory().getFactoryChain())).ensureRec((ICFBamChain)rec) );
				default:
					throw new CFLibUnsupportedClassException(getClass(), "ensureRec", "rec", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamChain createChain( ICFSecAuthorization Authorization,
		ICFBamChain iBuff )
	{
		final String S_ProcName = "createChain";
		
		CFBamBuffChain Buff = (CFBamBuffChain)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextChainIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffChainByChainTableIdxKey keyChainTableIdx = (CFBamBuffChainByChainTableIdxKey)schema.getCFBamFactory().getFactoryChain().newByChainTableIdxKey();
		keyChainTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffChainByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffChainByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryChain().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffChainByUNameIdxKey keyUNameIdx = (CFBamBuffChainByUNameIdxKey)schema.getCFBamFactory().getFactoryChain().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffChainByPrevRelIdxKey keyPrevRelIdx = (CFBamBuffChainByPrevRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByPrevRelIdxKey();
		keyPrevRelIdx.setRequiredPrevRelationId( Buff.getRequiredPrevRelationId() );

		CFBamBuffChainByNextRelIdxKey keyNextRelIdx = (CFBamBuffChainByNextRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByNextRelIdxKey();
		keyNextRelIdx.setRequiredNextRelationId( Buff.getRequiredNextRelationId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ChainUNameIdx",
				"ChainUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

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

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPrevRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"PrevRelation",
						"PrevRelation",
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
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredNextRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"NextRelation",
						"NextRelation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdictChainTableIdx;
		if( dictByChainTableIdx.containsKey( keyChainTableIdx ) ) {
			subdictChainTableIdx = dictByChainTableIdx.get( keyChainTableIdx );
		}
		else {
			subdictChainTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByChainTableIdx.put( keyChainTableIdx, subdictChainTableIdx );
		}
		subdictChainTableIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdictPrevRelIdx;
		if( dictByPrevRelIdx.containsKey( keyPrevRelIdx ) ) {
			subdictPrevRelIdx = dictByPrevRelIdx.get( keyPrevRelIdx );
		}
		else {
			subdictPrevRelIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByPrevRelIdx.put( keyPrevRelIdx, subdictPrevRelIdx );
		}
		subdictPrevRelIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdictNextRelIdx;
		if( dictByNextRelIdx.containsKey( keyNextRelIdx ) ) {
			subdictNextRelIdx = dictByNextRelIdx.get( keyNextRelIdx );
		}
		else {
			subdictNextRelIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByNextRelIdx.put( keyNextRelIdx, subdictNextRelIdx );
		}
		subdictNextRelIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamChain.CLASS_CODE) {
				CFBamBuffChain retbuff = ((CFBamBuffChain)(schema.getCFBamFactory().getFactoryChain().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamChain readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamChain.readDerived";
		ICFBamChain buff;
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
	public ICFBamChain lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamChain.lockDerived";
		ICFBamChain buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamChain[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamChain.readAllDerived";
		ICFBamChain[] retList = new ICFBamChain[ dictByPKey.values().size() ];
		Iterator< CFBamBuffChain > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamChain[] readDerivedByChainTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByChainTableIdx";
		CFBamBuffChainByChainTableIdxKey key = (CFBamBuffChainByChainTableIdxKey)schema.getCFBamFactory().getFactoryChain().newByChainTableIdxKey();

		key.setRequiredTableId( TableId );
		ICFBamChain[] recArray;
		if( dictByChainTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictChainTableIdx
				= dictByChainTableIdx.get( key );
			recArray = new ICFBamChain[ subdictChainTableIdx.size() ];
			Iterator< CFBamBuffChain > iter = subdictChainTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictChainTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByChainTableIdx.put( key, subdictChainTableIdx );
			recArray = new ICFBamChain[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamChain[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByDefSchemaIdx";
		CFBamBuffChainByDefSchemaIdxKey key = (CFBamBuffChainByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryChain().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamChain[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamChain[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffChain > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamChain[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamChain readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByUNameIdx";
		CFBamBuffChainByUNameIdxKey key = (CFBamBuffChainByUNameIdxKey)schema.getCFBamFactory().getFactoryChain().newByUNameIdxKey();

		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );
		ICFBamChain buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamChain[] readDerivedByPrevRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByPrevRelIdx";
		CFBamBuffChainByPrevRelIdxKey key = (CFBamBuffChainByPrevRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByPrevRelIdxKey();

		key.setRequiredPrevRelationId( PrevRelationId );
		ICFBamChain[] recArray;
		if( dictByPrevRelIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictPrevRelIdx
				= dictByPrevRelIdx.get( key );
			recArray = new ICFBamChain[ subdictPrevRelIdx.size() ];
			Iterator< CFBamBuffChain > iter = subdictPrevRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictPrevRelIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByPrevRelIdx.put( key, subdictPrevRelIdx );
			recArray = new ICFBamChain[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamChain[] readDerivedByNextRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByNextRelIdx";
		CFBamBuffChainByNextRelIdxKey key = (CFBamBuffChainByNextRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByNextRelIdxKey();

		key.setRequiredNextRelationId( NextRelationId );
		ICFBamChain[] recArray;
		if( dictByNextRelIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictNextRelIdx
				= dictByNextRelIdx.get( key );
			recArray = new ICFBamChain[ subdictNextRelIdx.size() ];
			Iterator< CFBamBuffChain > iter = subdictNextRelIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffChain > subdictNextRelIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByNextRelIdx.put( key, subdictNextRelIdx );
			recArray = new ICFBamChain[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamChain readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamChain.readDerivedByIdIdx() ";
		ICFBamChain buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamChain readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamChain.readRec";
		ICFBamChain buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamChain.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamChain lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamChain buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamChain.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamChain[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamChain.readAllRec";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamChain.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	@Override
	public ICFBamChain readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamChain.readRecByIdIdx() ";
		ICFBamChain buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamChain.CLASS_CODE ) ) {
			return( (ICFBamChain)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamChain[] readRecByChainTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamChain.readRecByChainTableIdx() ";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readDerivedByChainTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamChain.CLASS_CODE ) ) {
				filteredList.add( (ICFBamChain)buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	@Override
	public ICFBamChain[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamChain.readRecByDefSchemaIdx() ";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamChain.CLASS_CODE ) ) {
				filteredList.add( (ICFBamChain)buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	@Override
	public ICFBamChain readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamChain.readRecByUNameIdx() ";
		ICFBamChain buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamChain.CLASS_CODE ) ) {
			return( (ICFBamChain)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamChain[] readRecByPrevRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readRecByPrevRelIdx() ";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readDerivedByPrevRelIdx( Authorization,
			PrevRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamChain.CLASS_CODE ) ) {
				filteredList.add( (ICFBamChain)buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	@Override
	public ICFBamChain[] readRecByNextRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextRelationId )
	{
		final String S_ProcName = "CFBamRamChain.readRecByNextRelIdx() ";
		ICFBamChain buff;
		ArrayList<ICFBamChain> filteredList = new ArrayList<ICFBamChain>();
		ICFBamChain[] buffList = readDerivedByNextRelIdx( Authorization,
			NextRelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamChain.CLASS_CODE ) ) {
				filteredList.add( (ICFBamChain)buff );
			}
		}
		return( filteredList.toArray( new ICFBamChain[0] ) );
	}

	public ICFBamChain updateChain( ICFSecAuthorization Authorization,
		ICFBamChain iBuff )
	{
		CFBamBuffChain Buff = (CFBamBuffChain)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffChain existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateChain",
				"Existing record not found",
				"Existing record not found",
				"Chain",
				"Chain",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateChain",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffChainByChainTableIdxKey existingKeyChainTableIdx = (CFBamBuffChainByChainTableIdxKey)schema.getCFBamFactory().getFactoryChain().newByChainTableIdxKey();
		existingKeyChainTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffChainByChainTableIdxKey newKeyChainTableIdx = (CFBamBuffChainByChainTableIdxKey)schema.getCFBamFactory().getFactoryChain().newByChainTableIdxKey();
		newKeyChainTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffChainByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffChainByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryChain().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffChainByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffChainByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryChain().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffChainByUNameIdxKey existingKeyUNameIdx = (CFBamBuffChainByUNameIdxKey)schema.getCFBamFactory().getFactoryChain().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffChainByUNameIdxKey newKeyUNameIdx = (CFBamBuffChainByUNameIdxKey)schema.getCFBamFactory().getFactoryChain().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffChainByPrevRelIdxKey existingKeyPrevRelIdx = (CFBamBuffChainByPrevRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByPrevRelIdxKey();
		existingKeyPrevRelIdx.setRequiredPrevRelationId( existing.getRequiredPrevRelationId() );

		CFBamBuffChainByPrevRelIdxKey newKeyPrevRelIdx = (CFBamBuffChainByPrevRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByPrevRelIdxKey();
		newKeyPrevRelIdx.setRequiredPrevRelationId( Buff.getRequiredPrevRelationId() );

		CFBamBuffChainByNextRelIdxKey existingKeyNextRelIdx = (CFBamBuffChainByNextRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByNextRelIdxKey();
		existingKeyNextRelIdx.setRequiredNextRelationId( existing.getRequiredNextRelationId() );

		CFBamBuffChainByNextRelIdxKey newKeyNextRelIdx = (CFBamBuffChainByNextRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByNextRelIdxKey();
		newKeyNextRelIdx.setRequiredNextRelationId( Buff.getRequiredNextRelationId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateChain",
					"ChainUNameIdx",
					"ChainUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
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

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPrevRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
						"Lookup",
						"Lookup",
						"PrevRelation",
						"PrevRelation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRelation().readDerivedByIdIdx( Authorization,
						Buff.getRequiredNextRelationId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateChain",
						"Lookup",
						"Lookup",
						"NextRelation",
						"NextRelation",
						"Relation",
						"Relation",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffChain > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByChainTableIdx.get( existingKeyChainTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByChainTableIdx.containsKey( newKeyChainTableIdx ) ) {
			subdict = dictByChainTableIdx.get( newKeyChainTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByChainTableIdx.put( newKeyChainTableIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByPrevRelIdx.get( existingKeyPrevRelIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevRelIdx.containsKey( newKeyPrevRelIdx ) ) {
			subdict = dictByPrevRelIdx.get( newKeyPrevRelIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByPrevRelIdx.put( newKeyPrevRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextRelIdx.get( existingKeyNextRelIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextRelIdx.containsKey( newKeyNextRelIdx ) ) {
			subdict = dictByNextRelIdx.get( newKeyNextRelIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffChain >();
			dictByNextRelIdx.put( newKeyNextRelIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteChain( ICFSecAuthorization Authorization,
		ICFBamChain iBuff )
	{
		final String S_ProcName = "CFBamRamChainTable.deleteChain() ";
		CFBamBuffChain Buff = (CFBamBuffChain)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffChain existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteChain",
				pkey );
		}
		CFBamBuffChainByChainTableIdxKey keyChainTableIdx = (CFBamBuffChainByChainTableIdxKey)schema.getCFBamFactory().getFactoryChain().newByChainTableIdxKey();
		keyChainTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffChainByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffChainByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryChain().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffChainByUNameIdxKey keyUNameIdx = (CFBamBuffChainByUNameIdxKey)schema.getCFBamFactory().getFactoryChain().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffChainByPrevRelIdxKey keyPrevRelIdx = (CFBamBuffChainByPrevRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByPrevRelIdxKey();
		keyPrevRelIdx.setRequiredPrevRelationId( existing.getRequiredPrevRelationId() );

		CFBamBuffChainByNextRelIdxKey keyNextRelIdx = (CFBamBuffChainByNextRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByNextRelIdxKey();
		keyNextRelIdx.setRequiredNextRelationId( existing.getRequiredNextRelationId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffChain > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByChainTableIdx.get( keyChainTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByPrevRelIdx.get( keyPrevRelIdx );
		subdict.remove( pkey );

		subdict = dictByNextRelIdx.get( keyNextRelIdx );
		subdict.remove( pkey );

	}
	@Override
	public void deleteChainByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffChain cur;
		LinkedList<CFBamBuffChain> matchSet = new LinkedList<CFBamBuffChain>();
		Iterator<CFBamBuffChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffChain)(schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteChain( Authorization, cur );
		}
	}

	@Override
	public void deleteChainByChainTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffChainByChainTableIdxKey key = (CFBamBuffChainByChainTableIdxKey)schema.getCFBamFactory().getFactoryChain().newByChainTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteChainByChainTableIdx( Authorization, key );
	}

	@Override
	public void deleteChainByChainTableIdx( ICFSecAuthorization Authorization,
		ICFBamChainByChainTableIdxKey argKey )
	{
		CFBamBuffChain cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffChain> matchSet = new LinkedList<CFBamBuffChain>();
		Iterator<CFBamBuffChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffChain)(schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteChain( Authorization, cur );
		}
	}

	@Override
	public void deleteChainByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffChainByDefSchemaIdxKey key = (CFBamBuffChainByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryChain().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteChainByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteChainByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamChainByDefSchemaIdxKey argKey )
	{
		CFBamBuffChain cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffChain> matchSet = new LinkedList<CFBamBuffChain>();
		Iterator<CFBamBuffChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffChain)(schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteChain( Authorization, cur );
		}
	}

	@Override
	public void deleteChainByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffChainByUNameIdxKey key = (CFBamBuffChainByUNameIdxKey)schema.getCFBamFactory().getFactoryChain().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteChainByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteChainByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamChainByUNameIdxKey argKey )
	{
		CFBamBuffChain cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffChain> matchSet = new LinkedList<CFBamBuffChain>();
		Iterator<CFBamBuffChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffChain)(schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteChain( Authorization, cur );
		}
	}

	@Override
	public void deleteChainByPrevRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevRelationId )
	{
		CFBamBuffChainByPrevRelIdxKey key = (CFBamBuffChainByPrevRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByPrevRelIdxKey();
		key.setRequiredPrevRelationId( argPrevRelationId );
		deleteChainByPrevRelIdx( Authorization, key );
	}

	@Override
	public void deleteChainByPrevRelIdx( ICFSecAuthorization Authorization,
		ICFBamChainByPrevRelIdxKey argKey )
	{
		CFBamBuffChain cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffChain> matchSet = new LinkedList<CFBamBuffChain>();
		Iterator<CFBamBuffChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffChain)(schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteChain( Authorization, cur );
		}
	}

	@Override
	public void deleteChainByNextRelIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextRelationId )
	{
		CFBamBuffChainByNextRelIdxKey key = (CFBamBuffChainByNextRelIdxKey)schema.getCFBamFactory().getFactoryChain().newByNextRelIdxKey();
		key.setRequiredNextRelationId( argNextRelationId );
		deleteChainByNextRelIdx( Authorization, key );
	}

	@Override
	public void deleteChainByNextRelIdx( ICFSecAuthorization Authorization,
		ICFBamChainByNextRelIdxKey argKey )
	{
		CFBamBuffChain cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffChain> matchSet = new LinkedList<CFBamBuffChain>();
		Iterator<CFBamBuffChain> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffChain> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffChain)(schema.getTableChain().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteChain( Authorization, cur );
		}
	}
}
