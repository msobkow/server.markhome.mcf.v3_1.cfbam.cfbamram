
// Description: Java 25 in-memory RAM DbIO implementation for Relation.

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
 *	CFBamRamRelationTable in-memory RAM DbIO implementation
 *	for Relation.
 */
public class CFBamRamRelationTable
	implements ICFBamRelationTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffRelation > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffRelation >();
	private Map< CFBamBuffRelationByUNameIdxKey,
			CFBamBuffRelation > dictByUNameIdx
		= new HashMap< CFBamBuffRelationByUNameIdxKey,
			CFBamBuffRelation >();
	private Map< CFBamBuffRelationByRelTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >> dictByRelTableIdx
		= new HashMap< CFBamBuffRelationByRelTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >>();
	private Map< CFBamBuffRelationByRelCodeVisIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >> dictByRelCodeVisIdx
		= new HashMap< CFBamBuffRelationByRelCodeVisIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >>();
	private Map< CFBamBuffRelationByRelTableCodeVisXKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >> dictByRelTableCodeVisX
		= new HashMap< CFBamBuffRelationByRelTableCodeVisXKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >>();
	private Map< CFBamBuffRelationByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffRelationByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >>();
	private Map< CFBamBuffRelationByFromKeyIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >> dictByFromKeyIdx
		= new HashMap< CFBamBuffRelationByFromKeyIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >>();
	private Map< CFBamBuffRelationByToTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >> dictByToTblIdx
		= new HashMap< CFBamBuffRelationByToTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >>();
	private Map< CFBamBuffRelationByToKeyIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >> dictByToKeyIdx
		= new HashMap< CFBamBuffRelationByToKeyIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >>();
	private Map< CFBamBuffRelationByNarrowedIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >> dictByNarrowedIdx
		= new HashMap< CFBamBuffRelationByNarrowedIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffRelation >>();

	public CFBamRamRelationTable( ICFBamSchema argSchema ) {
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
	public ICFBamRelation createRelation( ICFSecAuthorization Authorization,
		ICFBamRelation iBuff )
	{
		final String S_ProcName = "createRelation";
		
		CFBamBuffRelation Buff = (CFBamBuffRelation)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffRelationByUNameIdxKey keyUNameIdx = (CFBamBuffRelationByUNameIdxKey)schema.getCFBamFactory().getFactoryRelation().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffRelationByRelTableIdxKey keyRelTableIdx = (CFBamBuffRelationByRelTableIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableIdxKey();
		keyRelTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffRelationByRelCodeVisIdxKey keyRelCodeVisIdx = (CFBamBuffRelationByRelCodeVisIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelCodeVisIdxKey();
		keyRelCodeVisIdx.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffRelationByRelTableCodeVisXKey keyRelTableCodeVisX = (CFBamBuffRelationByRelTableCodeVisXKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableCodeVisXKey();
		keyRelTableCodeVisX.setRequiredTableId( Buff.getRequiredTableId() );
		keyRelTableCodeVisX.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffRelationByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffRelationByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryRelation().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffRelationByFromKeyIdxKey keyFromKeyIdx = (CFBamBuffRelationByFromKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByFromKeyIdxKey();
		keyFromKeyIdx.setRequiredFromIndexId( Buff.getRequiredFromIndexId() );

		CFBamBuffRelationByToTblIdxKey keyToTblIdx = (CFBamBuffRelationByToTblIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToTblIdxKey();
		keyToTblIdx.setRequiredToTableId( Buff.getRequiredToTableId() );

		CFBamBuffRelationByToKeyIdxKey keyToKeyIdx = (CFBamBuffRelationByToKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToKeyIdxKey();
		keyToKeyIdx.setRequiredToIndexId( Buff.getRequiredToIndexId() );

		CFBamBuffRelationByNarrowedIdxKey keyNarrowedIdx = (CFBamBuffRelationByNarrowedIdxKey)schema.getCFBamFactory().getFactoryRelation().newByNarrowedIdxKey();
		keyNarrowedIdx.setOptionalNarrowedId( Buff.getOptionalNarrowedId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"RelationUNameIdx",
				"RelationUNameIdx",
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
						"FromTable",
						"FromTable",
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
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredFromIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"FromIndex",
						"FromIndex",
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
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"ToTable",
						"ToTable",
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
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"ToIndex",
						"ToIndex",
						"Index",
						"Index",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelTableIdx;
		if( dictByRelTableIdx.containsKey( keyRelTableIdx ) ) {
			subdictRelTableIdx = dictByRelTableIdx.get( keyRelTableIdx );
		}
		else {
			subdictRelTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelTableIdx.put( keyRelTableIdx, subdictRelTableIdx );
		}
		subdictRelTableIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelCodeVisIdx;
		if( dictByRelCodeVisIdx.containsKey( keyRelCodeVisIdx ) ) {
			subdictRelCodeVisIdx = dictByRelCodeVisIdx.get( keyRelCodeVisIdx );
		}
		else {
			subdictRelCodeVisIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelCodeVisIdx.put( keyRelCodeVisIdx, subdictRelCodeVisIdx );
		}
		subdictRelCodeVisIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelTableCodeVisX;
		if( dictByRelTableCodeVisX.containsKey( keyRelTableCodeVisX ) ) {
			subdictRelTableCodeVisX = dictByRelTableCodeVisX.get( keyRelTableCodeVisX );
		}
		else {
			subdictRelTableCodeVisX = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelTableCodeVisX.put( keyRelTableCodeVisX, subdictRelTableCodeVisX );
		}
		subdictRelTableCodeVisX.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictFromKeyIdx;
		if( dictByFromKeyIdx.containsKey( keyFromKeyIdx ) ) {
			subdictFromKeyIdx = dictByFromKeyIdx.get( keyFromKeyIdx );
		}
		else {
			subdictFromKeyIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByFromKeyIdx.put( keyFromKeyIdx, subdictFromKeyIdx );
		}
		subdictFromKeyIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictToTblIdx;
		if( dictByToTblIdx.containsKey( keyToTblIdx ) ) {
			subdictToTblIdx = dictByToTblIdx.get( keyToTblIdx );
		}
		else {
			subdictToTblIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByToTblIdx.put( keyToTblIdx, subdictToTblIdx );
		}
		subdictToTblIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictToKeyIdx;
		if( dictByToKeyIdx.containsKey( keyToKeyIdx ) ) {
			subdictToKeyIdx = dictByToKeyIdx.get( keyToKeyIdx );
		}
		else {
			subdictToKeyIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByToKeyIdx.put( keyToKeyIdx, subdictToKeyIdx );
		}
		subdictToKeyIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictNarrowedIdx;
		if( dictByNarrowedIdx.containsKey( keyNarrowedIdx ) ) {
			subdictNarrowedIdx = dictByNarrowedIdx.get( keyNarrowedIdx );
		}
		else {
			subdictNarrowedIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByNarrowedIdx.put( keyNarrowedIdx, subdictNarrowedIdx );
		}
		subdictNarrowedIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamRelation.CLASS_CODE) {
				CFBamBuffRelation retbuff = ((CFBamBuffRelation)(schema.getCFBamFactory().getFactoryRelation().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamRelation readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelation.readDerived";
		ICFBamRelation buff;
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
	public ICFBamRelation lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelation.lockDerived";
		ICFBamRelation buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelation[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamRelation.readAllDerived";
		ICFBamRelation[] retList = new ICFBamRelation[ dictByPKey.values().size() ];
		Iterator< CFBamBuffRelation > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamRelation[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamRelation ) ) {
					filteredList.add( (ICFBamRelation)buff );
				}
			}
			return( filteredList.toArray( new ICFBamRelation[0] ) );
		}
	}

	@Override
	public ICFBamRelation readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByUNameIdx";
		CFBamBuffRelationByUNameIdxKey key = (CFBamBuffRelationByUNameIdxKey)schema.getCFBamFactory().getFactoryRelation().newByUNameIdxKey();

		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );
		ICFBamRelation buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelation[] readDerivedByRelTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByRelTableIdx";
		CFBamBuffRelationByRelTableIdxKey key = (CFBamBuffRelationByRelTableIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableIdxKey();

		key.setRequiredTableId( TableId );
		ICFBamRelation[] recArray;
		if( dictByRelTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelTableIdx
				= dictByRelTableIdx.get( key );
			recArray = new ICFBamRelation[ subdictRelTableIdx.size() ];
			Iterator< CFBamBuffRelation > iter = subdictRelTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelTableIdx.put( key, subdictRelTableIdx );
			recArray = new ICFBamRelation[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelation[] readDerivedByRelCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByRelCodeVisIdx";
		CFBamBuffRelationByRelCodeVisIdxKey key = (CFBamBuffRelationByRelCodeVisIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelCodeVisIdxKey();

		key.setRequiredCodeVis( CodeVis );
		ICFBamRelation[] recArray;
		if( dictByRelCodeVisIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelCodeVisIdx
				= dictByRelCodeVisIdx.get( key );
			recArray = new ICFBamRelation[ subdictRelCodeVisIdx.size() ];
			Iterator< CFBamBuffRelation > iter = subdictRelCodeVisIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelCodeVisIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelCodeVisIdx.put( key, subdictRelCodeVisIdx );
			recArray = new ICFBamRelation[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelation[] readDerivedByRelTableCodeVisX( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByRelTableCodeVisX";
		CFBamBuffRelationByRelTableCodeVisXKey key = (CFBamBuffRelationByRelTableCodeVisXKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableCodeVisXKey();

		key.setRequiredTableId( TableId );
		key.setRequiredCodeVis( CodeVis );
		ICFBamRelation[] recArray;
		if( dictByRelTableCodeVisX.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelTableCodeVisX
				= dictByRelTableCodeVisX.get( key );
			recArray = new ICFBamRelation[ subdictRelTableCodeVisX.size() ];
			Iterator< CFBamBuffRelation > iter = subdictRelTableCodeVisX.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictRelTableCodeVisX
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelTableCodeVisX.put( key, subdictRelTableCodeVisX );
			recArray = new ICFBamRelation[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelation[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByDefSchemaIdx";
		CFBamBuffRelationByDefSchemaIdxKey key = (CFBamBuffRelationByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryRelation().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamRelation[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamRelation[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffRelation > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamRelation[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelation[] readDerivedByFromKeyIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromIndexId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByFromKeyIdx";
		CFBamBuffRelationByFromKeyIdxKey key = (CFBamBuffRelationByFromKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByFromKeyIdxKey();

		key.setRequiredFromIndexId( FromIndexId );
		ICFBamRelation[] recArray;
		if( dictByFromKeyIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictFromKeyIdx
				= dictByFromKeyIdx.get( key );
			recArray = new ICFBamRelation[ subdictFromKeyIdx.size() ];
			Iterator< CFBamBuffRelation > iter = subdictFromKeyIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictFromKeyIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByFromKeyIdx.put( key, subdictFromKeyIdx );
			recArray = new ICFBamRelation[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelation[] readDerivedByToTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToTableId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByToTblIdx";
		CFBamBuffRelationByToTblIdxKey key = (CFBamBuffRelationByToTblIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToTblIdxKey();

		key.setRequiredToTableId( ToTableId );
		ICFBamRelation[] recArray;
		if( dictByToTblIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictToTblIdx
				= dictByToTblIdx.get( key );
			recArray = new ICFBamRelation[ subdictToTblIdx.size() ];
			Iterator< CFBamBuffRelation > iter = subdictToTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictToTblIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByToTblIdx.put( key, subdictToTblIdx );
			recArray = new ICFBamRelation[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelation[] readDerivedByToKeyIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToIndexId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByToKeyIdx";
		CFBamBuffRelationByToKeyIdxKey key = (CFBamBuffRelationByToKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToKeyIdxKey();

		key.setRequiredToIndexId( ToIndexId );
		ICFBamRelation[] recArray;
		if( dictByToKeyIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictToKeyIdx
				= dictByToKeyIdx.get( key );
			recArray = new ICFBamRelation[ subdictToKeyIdx.size() ];
			Iterator< CFBamBuffRelation > iter = subdictToKeyIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictToKeyIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByToKeyIdx.put( key, subdictToKeyIdx );
			recArray = new ICFBamRelation[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelation[] readDerivedByNarrowedIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NarrowedId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByNarrowedIdx";
		CFBamBuffRelationByNarrowedIdxKey key = (CFBamBuffRelationByNarrowedIdxKey)schema.getCFBamFactory().getFactoryRelation().newByNarrowedIdxKey();

		key.setOptionalNarrowedId( NarrowedId );
		ICFBamRelation[] recArray;
		if( dictByNarrowedIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictNarrowedIdx
				= dictByNarrowedIdx.get( key );
			recArray = new ICFBamRelation[ subdictNarrowedIdx.size() ];
			Iterator< CFBamBuffRelation > iter = subdictNarrowedIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffRelation > subdictNarrowedIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByNarrowedIdx.put( key, subdictNarrowedIdx );
			recArray = new ICFBamRelation[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamRelation readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamRelation buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelation readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamRelation.readRec";
		ICFBamRelation buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamRelation.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelation lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamRelation buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamRelation.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamRelation[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamRelation.readAllRec";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamRelation buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamRelation)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamRelation[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByUNameIdx() ";
		ICFBamRelation buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
			return( (ICFBamRelation)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamRelation[] readRecByRelTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByRelTableIdx() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByRelTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation[] readRecByRelCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByRelCodeVisIdx() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByRelCodeVisIdx( Authorization,
			CodeVis );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation[] readRecByRelTableCodeVisX( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		ICFBamSchema.CodeVisibilityEnum CodeVis )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByRelTableCodeVisX() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByRelTableCodeVisX( Authorization,
			TableId,
			CodeVis );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByDefSchemaIdx() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation[] readRecByFromKeyIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromIndexId )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByFromKeyIdx() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByFromKeyIdx( Authorization,
			FromIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation[] readRecByToTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToTableId )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByToTblIdx() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByToTblIdx( Authorization,
			ToTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation[] readRecByToKeyIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToIndexId )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByToKeyIdx() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByToKeyIdx( Authorization,
			ToIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	@Override
	public ICFBamRelation[] readRecByNarrowedIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NarrowedId )
	{
		final String S_ProcName = "CFBamRamRelation.readRecByNarrowedIdx() ";
		ICFBamRelation buff;
		ArrayList<ICFBamRelation> filteredList = new ArrayList<ICFBamRelation>();
		ICFBamRelation[] buffList = readDerivedByNarrowedIdx( Authorization,
			NarrowedId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRelation.CLASS_CODE ) ) {
				filteredList.add( (ICFBamRelation)buff );
			}
		}
		return( filteredList.toArray( new ICFBamRelation[0] ) );
	}

	public ICFBamRelation updateRelation( ICFSecAuthorization Authorization,
		ICFBamRelation iBuff )
	{
		CFBamBuffRelation Buff = (CFBamBuffRelation)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffRelation existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateRelation",
				"Existing record not found",
				"Existing record not found",
				"Relation",
				"Relation",
				pkey );
		}
		CFBamBuffRelationByUNameIdxKey existingKeyUNameIdx = (CFBamBuffRelationByUNameIdxKey)schema.getCFBamFactory().getFactoryRelation().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRelationByUNameIdxKey newKeyUNameIdx = (CFBamBuffRelationByUNameIdxKey)schema.getCFBamFactory().getFactoryRelation().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffRelationByRelTableIdxKey existingKeyRelTableIdx = (CFBamBuffRelationByRelTableIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableIdxKey();
		existingKeyRelTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffRelationByRelTableIdxKey newKeyRelTableIdx = (CFBamBuffRelationByRelTableIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableIdxKey();
		newKeyRelTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffRelationByRelCodeVisIdxKey existingKeyRelCodeVisIdx = (CFBamBuffRelationByRelCodeVisIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelCodeVisIdxKey();
		existingKeyRelCodeVisIdx.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffRelationByRelCodeVisIdxKey newKeyRelCodeVisIdx = (CFBamBuffRelationByRelCodeVisIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelCodeVisIdxKey();
		newKeyRelCodeVisIdx.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffRelationByRelTableCodeVisXKey existingKeyRelTableCodeVisX = (CFBamBuffRelationByRelTableCodeVisXKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableCodeVisXKey();
		existingKeyRelTableCodeVisX.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyRelTableCodeVisX.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffRelationByRelTableCodeVisXKey newKeyRelTableCodeVisX = (CFBamBuffRelationByRelTableCodeVisXKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableCodeVisXKey();
		newKeyRelTableCodeVisX.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyRelTableCodeVisX.setRequiredCodeVis( Buff.getRequiredCodeVis() );

		CFBamBuffRelationByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffRelationByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryRelation().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffRelationByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffRelationByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryRelation().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffRelationByFromKeyIdxKey existingKeyFromKeyIdx = (CFBamBuffRelationByFromKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByFromKeyIdxKey();
		existingKeyFromKeyIdx.setRequiredFromIndexId( existing.getRequiredFromIndexId() );

		CFBamBuffRelationByFromKeyIdxKey newKeyFromKeyIdx = (CFBamBuffRelationByFromKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByFromKeyIdxKey();
		newKeyFromKeyIdx.setRequiredFromIndexId( Buff.getRequiredFromIndexId() );

		CFBamBuffRelationByToTblIdxKey existingKeyToTblIdx = (CFBamBuffRelationByToTblIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToTblIdxKey();
		existingKeyToTblIdx.setRequiredToTableId( existing.getRequiredToTableId() );

		CFBamBuffRelationByToTblIdxKey newKeyToTblIdx = (CFBamBuffRelationByToTblIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToTblIdxKey();
		newKeyToTblIdx.setRequiredToTableId( Buff.getRequiredToTableId() );

		CFBamBuffRelationByToKeyIdxKey existingKeyToKeyIdx = (CFBamBuffRelationByToKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToKeyIdxKey();
		existingKeyToKeyIdx.setRequiredToIndexId( existing.getRequiredToIndexId() );

		CFBamBuffRelationByToKeyIdxKey newKeyToKeyIdx = (CFBamBuffRelationByToKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToKeyIdxKey();
		newKeyToKeyIdx.setRequiredToIndexId( Buff.getRequiredToIndexId() );

		CFBamBuffRelationByNarrowedIdxKey existingKeyNarrowedIdx = (CFBamBuffRelationByNarrowedIdxKey)schema.getCFBamFactory().getFactoryRelation().newByNarrowedIdxKey();
		existingKeyNarrowedIdx.setOptionalNarrowedId( existing.getOptionalNarrowedId() );

		CFBamBuffRelationByNarrowedIdxKey newKeyNarrowedIdx = (CFBamBuffRelationByNarrowedIdxKey)schema.getCFBamFactory().getFactoryRelation().newByNarrowedIdxKey();
		newKeyNarrowedIdx.setOptionalNarrowedId( Buff.getOptionalNarrowedId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRelation",
					"RelationUNameIdx",
					"RelationUNameIdx",
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
						"updateRelation",
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
						"updateRelation",
						"Container",
						"Container",
						"FromTable",
						"FromTable",
						"Table",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredFromIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelation",
						"Lookup",
						"Lookup",
						"FromIndex",
						"FromIndex",
						"Index",
						"Index",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelation",
						"Lookup",
						"Lookup",
						"ToTable",
						"ToTable",
						"Table",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelation",
						"Lookup",
						"Lookup",
						"ToIndex",
						"ToIndex",
						"Index",
						"Index",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByRelTableIdx.get( existingKeyRelTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelTableIdx.containsKey( newKeyRelTableIdx ) ) {
			subdict = dictByRelTableIdx.get( newKeyRelTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelTableIdx.put( newKeyRelTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByRelCodeVisIdx.get( existingKeyRelCodeVisIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelCodeVisIdx.containsKey( newKeyRelCodeVisIdx ) ) {
			subdict = dictByRelCodeVisIdx.get( newKeyRelCodeVisIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelCodeVisIdx.put( newKeyRelCodeVisIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByRelTableCodeVisX.get( existingKeyRelTableCodeVisX );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelTableCodeVisX.containsKey( newKeyRelTableCodeVisX ) ) {
			subdict = dictByRelTableCodeVisX.get( newKeyRelTableCodeVisX );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByRelTableCodeVisX.put( newKeyRelTableCodeVisX, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByFromKeyIdx.get( existingKeyFromKeyIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByFromKeyIdx.containsKey( newKeyFromKeyIdx ) ) {
			subdict = dictByFromKeyIdx.get( newKeyFromKeyIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByFromKeyIdx.put( newKeyFromKeyIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByToTblIdx.get( existingKeyToTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByToTblIdx.containsKey( newKeyToTblIdx ) ) {
			subdict = dictByToTblIdx.get( newKeyToTblIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByToTblIdx.put( newKeyToTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByToKeyIdx.get( existingKeyToKeyIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByToKeyIdx.containsKey( newKeyToKeyIdx ) ) {
			subdict = dictByToKeyIdx.get( newKeyToKeyIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByToKeyIdx.put( newKeyToKeyIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNarrowedIdx.get( existingKeyNarrowedIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNarrowedIdx.containsKey( newKeyNarrowedIdx ) ) {
			subdict = dictByNarrowedIdx.get( newKeyNarrowedIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffRelation >();
			dictByNarrowedIdx.put( newKeyNarrowedIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteRelation( ICFSecAuthorization Authorization,
		ICFBamRelation iBuff )
	{
		final String S_ProcName = "CFBamRamRelationTable.deleteRelation() ";
		CFBamBuffRelation Buff = (CFBamBuffRelation)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffRelation existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteRelation",
				pkey );
		}
					schema.getTablePopTopDep().deletePopTopDepByContRelIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableRelationCol().deleteRelationColByRelationIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffRelationByUNameIdxKey keyUNameIdx = (CFBamBuffRelationByUNameIdxKey)schema.getCFBamFactory().getFactoryRelation().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffRelationByRelTableIdxKey keyRelTableIdx = (CFBamBuffRelationByRelTableIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableIdxKey();
		keyRelTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffRelationByRelCodeVisIdxKey keyRelCodeVisIdx = (CFBamBuffRelationByRelCodeVisIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelCodeVisIdxKey();
		keyRelCodeVisIdx.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffRelationByRelTableCodeVisXKey keyRelTableCodeVisX = (CFBamBuffRelationByRelTableCodeVisXKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableCodeVisXKey();
		keyRelTableCodeVisX.setRequiredTableId( existing.getRequiredTableId() );
		keyRelTableCodeVisX.setRequiredCodeVis( existing.getRequiredCodeVis() );

		CFBamBuffRelationByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffRelationByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryRelation().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffRelationByFromKeyIdxKey keyFromKeyIdx = (CFBamBuffRelationByFromKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByFromKeyIdxKey();
		keyFromKeyIdx.setRequiredFromIndexId( existing.getRequiredFromIndexId() );

		CFBamBuffRelationByToTblIdxKey keyToTblIdx = (CFBamBuffRelationByToTblIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToTblIdxKey();
		keyToTblIdx.setRequiredToTableId( existing.getRequiredToTableId() );

		CFBamBuffRelationByToKeyIdxKey keyToKeyIdx = (CFBamBuffRelationByToKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToKeyIdxKey();
		keyToKeyIdx.setRequiredToIndexId( existing.getRequiredToIndexId() );

		CFBamBuffRelationByNarrowedIdxKey keyNarrowedIdx = (CFBamBuffRelationByNarrowedIdxKey)schema.getCFBamFactory().getFactoryRelation().newByNarrowedIdxKey();
		keyNarrowedIdx.setOptionalNarrowedId( existing.getOptionalNarrowedId() );

		// Validate reverse foreign keys

		if( schema.getTableChain().readDerivedByPrevRelIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"Lookup",
				"PrevRelation",
				"PrevRelation",
				"Chain",
				"Chain",
				pkey );
		}

		if( schema.getTableChain().readDerivedByNextRelIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"Lookup",
				"NextRelation",
				"NextRelation",
				"Chain",
				"Chain",
				pkey );
		}

		if( schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"Lookup",
				"Relation",
				"Relation",
				"ClearDep",
				"ClearDep",
				pkey );
		}

		if( schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"Lookup",
				"Relation",
				"Relation",
				"DelDep",
				"DelDep",
				pkey );
		}

		if( schema.getTablePopDep().readDerivedByRelationIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"Lookup",
				"Relation",
				"Relation",
				"PopDep",
				"PopDep",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffRelation > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByRelTableIdx.get( keyRelTableIdx );
		subdict.remove( pkey );

		subdict = dictByRelCodeVisIdx.get( keyRelCodeVisIdx );
		subdict.remove( pkey );

		subdict = dictByRelTableCodeVisX.get( keyRelTableCodeVisX );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByFromKeyIdx.get( keyFromKeyIdx );
		subdict.remove( pkey );

		subdict = dictByToTblIdx.get( keyToTblIdx );
		subdict.remove( pkey );

		subdict = dictByToKeyIdx.get( keyToKeyIdx );
		subdict.remove( pkey );

		subdict = dictByNarrowedIdx.get( keyNarrowedIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deleteRelationByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffRelationByUNameIdxKey key = (CFBamBuffRelationByUNameIdxKey)schema.getCFBamFactory().getFactoryRelation().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteRelationByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamRelationByUNameIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByRelTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffRelationByRelTableIdxKey key = (CFBamBuffRelationByRelTableIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteRelationByRelTableIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByRelTableIdx( ICFSecAuthorization Authorization,
		ICFBamRelationByRelTableIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByRelCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.CodeVisibilityEnum argCodeVis )
	{
		CFBamBuffRelationByRelCodeVisIdxKey key = (CFBamBuffRelationByRelCodeVisIdxKey)schema.getCFBamFactory().getFactoryRelation().newByRelCodeVisIdxKey();
		key.setRequiredCodeVis( argCodeVis );
		deleteRelationByRelCodeVisIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByRelCodeVisIdx( ICFSecAuthorization Authorization,
		ICFBamRelationByRelCodeVisIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByRelTableCodeVisX( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		ICFBamSchema.CodeVisibilityEnum argCodeVis )
	{
		CFBamBuffRelationByRelTableCodeVisXKey key = (CFBamBuffRelationByRelTableCodeVisXKey)schema.getCFBamFactory().getFactoryRelation().newByRelTableCodeVisXKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredCodeVis( argCodeVis );
		deleteRelationByRelTableCodeVisX( Authorization, key );
	}

	@Override
	public void deleteRelationByRelTableCodeVisX( ICFSecAuthorization Authorization,
		ICFBamRelationByRelTableCodeVisXKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffRelationByDefSchemaIdxKey key = (CFBamBuffRelationByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryRelation().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteRelationByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamRelationByDefSchemaIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByFromKeyIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argFromIndexId )
	{
		CFBamBuffRelationByFromKeyIdxKey key = (CFBamBuffRelationByFromKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByFromKeyIdxKey();
		key.setRequiredFromIndexId( argFromIndexId );
		deleteRelationByFromKeyIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByFromKeyIdx( ICFSecAuthorization Authorization,
		ICFBamRelationByFromKeyIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByToTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argToTableId )
	{
		CFBamBuffRelationByToTblIdxKey key = (CFBamBuffRelationByToTblIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToTblIdxKey();
		key.setRequiredToTableId( argToTableId );
		deleteRelationByToTblIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByToTblIdx( ICFSecAuthorization Authorization,
		ICFBamRelationByToTblIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByToKeyIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argToIndexId )
	{
		CFBamBuffRelationByToKeyIdxKey key = (CFBamBuffRelationByToKeyIdxKey)schema.getCFBamFactory().getFactoryRelation().newByToKeyIdxKey();
		key.setRequiredToIndexId( argToIndexId );
		deleteRelationByToKeyIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByToKeyIdx( ICFSecAuthorization Authorization,
		ICFBamRelationByToKeyIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByNarrowedIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNarrowedId )
	{
		CFBamBuffRelationByNarrowedIdxKey key = (CFBamBuffRelationByNarrowedIdxKey)schema.getCFBamFactory().getFactoryRelation().newByNarrowedIdxKey();
		key.setOptionalNarrowedId( argNarrowedId );
		deleteRelationByNarrowedIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByNarrowedIdx( ICFSecAuthorization Authorization,
		ICFBamRelationByNarrowedIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNarrowedId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffRelation cur;
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}

	@Override
	public void deleteRelationByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteRelationByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteRelationByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffRelation cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffRelation> matchSet = new LinkedList<CFBamBuffRelation>();
		Iterator<CFBamBuffRelation> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffRelation> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteRelation( Authorization, cur );
		}
	}
}
