
// Description: Java 25 in-memory RAM DbIO implementation for DelTopDep.

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
 *	CFBamRamDelTopDepTable in-memory RAM DbIO implementation
 *	for DelTopDep.
 */
public class CFBamRamDelTopDepTable
	implements ICFBamDelTopDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDelTopDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDelTopDep >();
	private Map< CFBamBuffDelTopDepByDelTopDepTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >> dictByDelTopDepTblIdx
		= new HashMap< CFBamBuffDelTopDepByDelTopDepTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >>();
	private Map< CFBamBuffDelTopDepByUNameIdxKey,
			CFBamBuffDelTopDep > dictByUNameIdx
		= new HashMap< CFBamBuffDelTopDepByUNameIdxKey,
			CFBamBuffDelTopDep >();
	private Map< CFBamBuffDelTopDepByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >> dictByPrevIdx
		= new HashMap< CFBamBuffDelTopDepByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >>();
	private Map< CFBamBuffDelTopDepByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >> dictByNextIdx
		= new HashMap< CFBamBuffDelTopDepByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >>();

	public CFBamRamDelTopDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffScope ensureRec(ICFBamScope rec) {
		if (rec == null) {
			return( null );
		}
		else {
			return (((CFBamBuffScopeFactoryService)(schema.getCFBamBuffFactory().getFactoryScope())).ensureRec(rec));
		}
	}

	@Override
	public ICFBamDelTopDep createDelTopDep( ICFSecAuthorization Authorization,
		ICFBamDelTopDep iBuff )
	{
		final String S_ProcName = "createDelTopDep";
		
		CFBamBuffDelTopDep Buff = (CFBamBuffDelTopDep)(schema.getTableDelDep().createDelDep( Authorization,
			iBuff ));
		ICFBamDelTopDep tail = null;
		if( Buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) {
			ICFBamDelTopDep[] siblings = schema.getTableDelTopDep().readDerivedByDelTopDepTblIdx( Authorization,
				Buff.getRequiredTableId() );
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
		}
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelTopDepByDelTopDepTblIdxKey keyDelTopDepTblIdx = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		keyDelTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffDelTopDepByUNameIdxKey keyUNameIdx = (CFBamBuffDelTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffDelTopDepByPrevIdxKey keyPrevIdx = (CFBamBuffDelTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffDelTopDepByNextIdxKey keyNextIdx = (CFBamBuffDelTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelTopDepUNameIdx",
				"DelTopDepUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"DelDep",
						"DelDep",
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

		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictDelTopDepTblIdx;
		if( dictByDelTopDepTblIdx.containsKey( keyDelTopDepTblIdx ) ) {
			subdictDelTopDepTblIdx = dictByDelTopDepTblIdx.get( keyDelTopDepTblIdx );
		}
		else {
			subdictDelTopDepTblIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByDelTopDepTblIdx.put( keyDelTopDepTblIdx, subdictDelTopDepTblIdx );
		}
		subdictDelTopDepTblIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			int tailClassCode = tail.getClassCode();
			if( tailClassCode == ICFBamDelTopDep.CLASS_CODE ) {
				ICFBamDelTopDep tailEdit = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
				tailEdit.set( (ICFBamDelTopDep)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDelTopDep().updateDelTopDep( Authorization, tailEdit );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-table-chain-link-tail-", (Integer)tailClassCode, "Classcode not recognized: " + Integer.toString(tailClassCode));
			}
		}
		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamDelTopDep.CLASS_CODE) {
				CFBamBuffDelTopDep retbuff = ((CFBamBuffDelTopDep)(schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamDelTopDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerived";
		ICFBamDelTopDep buff;
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
	public ICFBamDelTopDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.lockDerived";
		ICFBamDelTopDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelTopDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelTopDep.readAllDerived";
		ICFBamDelTopDep[] retList = new ICFBamDelTopDep[ dictByPKey.values().size() ];
		Iterator< CFBamBuffDelTopDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamDelTopDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelTopDep ) ) {
					filteredList.add( (ICFBamDelTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
		}
	}

	@Override
	public ICFBamDelTopDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		ICFBamDelDep buffList[] = schema.getTableDelDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamDelDep buff;
			ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelTopDep ) ) {
					filteredList.add( (ICFBamDelTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
		}
	}

	@Override
	public ICFBamDelTopDep[] readDerivedByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		ICFBamDelDep buffList[] = schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamDelDep buff;
			ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelTopDep ) ) {
					filteredList.add( (ICFBamDelTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
		}
	}

	@Override
	public ICFBamDelTopDep[] readDerivedByDelTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByDelTopDepTblIdx";
		CFBamBuffDelTopDepByDelTopDepTblIdxKey key = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByDelTopDepTblIdxKey();

		key.setRequiredTableId( TableId );
		ICFBamDelTopDep[] recArray;
		if( dictByDelTopDepTblIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictDelTopDepTblIdx
				= dictByDelTopDepTblIdx.get( key );
			recArray = new ICFBamDelTopDep[ subdictDelTopDepTblIdx.size() ];
			Iterator< CFBamBuffDelTopDep > iter = subdictDelTopDepTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictDelTopDepTblIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByDelTopDepTblIdx.put( key, subdictDelTopDepTblIdx );
			recArray = new ICFBamDelTopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDelTopDep readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByUNameIdx";
		CFBamBuffDelTopDepByUNameIdxKey key = (CFBamBuffDelTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByUNameIdxKey();

		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );
		ICFBamDelTopDep buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelTopDep[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByPrevIdx";
		CFBamBuffDelTopDepByPrevIdxKey key = (CFBamBuffDelTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByPrevIdxKey();

		key.setOptionalPrevId( PrevId );
		ICFBamDelTopDep[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamDelTopDep[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffDelTopDep > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamDelTopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDelTopDep[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByNextIdx";
		CFBamBuffDelTopDepByNextIdxKey key = (CFBamBuffDelTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByNextIdxKey();

		key.setOptionalNextId( NextId );
		ICFBamDelTopDep[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamDelTopDep[ subdictNextIdx.size() ];
			Iterator< CFBamBuffDelTopDep > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamDelTopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDelTopDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamDelTopDep buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelTopDep readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readRec";
		ICFBamDelTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelTopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelTopDep lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamDelTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelTopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDelTopDep[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readAllRec";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	@Override
	public ICFBamDelTopDep readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamDelTopDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamDelTopDep)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDelTopDep[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	@Override
	public ICFBamDelTopDep[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDefSchemaIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	@Override
	public ICFBamDelTopDep[] readRecByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readRecByDelDepIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	@Override
	public ICFBamDelTopDep[] readRecByDelTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readRecByDelTopDepTblIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByDelTopDepTblIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	@Override
	public ICFBamDelTopDep readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readRecByUNameIdx() ";
		ICFBamDelTopDep buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
			return( (ICFBamDelTopDep)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDelTopDep[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readRecByPrevIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	@Override
	public ICFBamDelTopDep[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readRecByNextIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamDelTopDep moveRecUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecUp";

		ICFBamDelTopDep grandprev = null;
		ICFBamDelTopDep prev = null;
		ICFBamDelTopDep cur = null;
		ICFBamDelTopDep next = null;

		cur = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffDelTopDep)cur );
		}

		prev = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() ));
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
			}
		}

		int classCode = prev.getClassCode();
		ICFBamDelTopDep newInstance;
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffDelTopDep editPrev = (CFBamBuffDelTopDep)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffDelTopDep editCur = (CFBamBuffDelTopDep)newInstance;
		editCur.set( cur );

		CFBamBuffDelTopDep editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = (CFBamBuffDelTopDep)newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffDelTopDep editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext = (CFBamBuffDelTopDep)newInstance;
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
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffDelTopDep)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamDelTopDep moveRecDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecDown";

		CFBamBuffDelTopDep prev = null;
		CFBamBuffDelTopDep cur = null;
		CFBamBuffDelTopDep next = null;
		CFBamBuffDelTopDep grandnext = null;

		cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, Id));
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffDelTopDep)cur );
		}

		next = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, next.getOptionalNextId() ));
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
			}
		}

		int classCode = cur.getClassCode();
		ICFBamDelTopDep newInstance;
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffDelTopDep editCur = (CFBamBuffDelTopDep)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffDelTopDep editNext = (CFBamBuffDelTopDep)newInstance;
		editNext.set( next );

		CFBamBuffDelTopDep editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = (CFBamBuffDelTopDep)newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffDelTopDep editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev = (CFBamBuffDelTopDep)newInstance;
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
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffDelTopDep)editCur );
	}

	public ICFBamDelTopDep updateDelTopDep( ICFSecAuthorization Authorization,
		ICFBamDelTopDep iBuff )
	{
		CFBamBuffDelTopDep Buff = (CFBamBuffDelTopDep)(schema.getTableDelDep().updateDelDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDelTopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelTopDep",
				"Existing record not found",
				"Existing record not found",
				"DelTopDep",
				"DelTopDep",
				pkey );
		}
		CFBamBuffDelTopDepByDelTopDepTblIdxKey existingKeyDelTopDepTblIdx = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		existingKeyDelTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffDelTopDepByDelTopDepTblIdxKey newKeyDelTopDepTblIdx = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		newKeyDelTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffDelTopDepByUNameIdxKey existingKeyUNameIdx = (CFBamBuffDelTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffDelTopDepByUNameIdxKey newKeyUNameIdx = (CFBamBuffDelTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffDelTopDepByPrevIdxKey existingKeyPrevIdx = (CFBamBuffDelTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffDelTopDepByPrevIdxKey newKeyPrevIdx = (CFBamBuffDelTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffDelTopDepByNextIdxKey existingKeyNextIdx = (CFBamBuffDelTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffDelTopDepByNextIdxKey newKeyNextIdx = (CFBamBuffDelTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelTopDep",
					"DelTopDepUNameIdx",
					"DelTopDepUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelTopDep",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"DelDep",
						"DelDep",
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
						"updateDelTopDep",
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

		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDelTopDepTblIdx.get( existingKeyDelTopDepTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelTopDepTblIdx.containsKey( newKeyDelTopDepTblIdx ) ) {
			subdict = dictByDelTopDepTblIdx.get( newKeyDelTopDepTblIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByDelTopDepTblIdx.put( newKeyDelTopDepTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteDelTopDep( ICFSecAuthorization Authorization,
		ICFBamDelTopDep iBuff )
	{
		final String S_ProcName = "CFBamRamDelTopDepTable.deleteDelTopDep() ";
		CFBamBuffDelTopDep Buff = (CFBamBuffDelTopDep)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffDelTopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelTopDep",
				pkey );
		}
		CFLibDbKeyHash256 varTableId = existing.getRequiredTableId();
		CFBamBuffTable container = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
			varTableId ));
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamBuffDelTopDep prev = null;
		if( ( prevId != null ) )
		{
			prev = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				prevId ));
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamBuffDelTopDep editPrev;
			classCode = prev.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				editPrev = (CFBamBuffDelTopDep)(schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalLookupNext(nextId);
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffDelTopDep next = null;
		if( ( nextId != null ) )
		{
			next = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				nextId ));
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamBuffDelTopDep editNext;
			classCode = next.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				editNext = (CFBamBuffDelTopDep)(schema.getCFBamBuffFactory().getFactoryDelTopDep().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalLookupPrev(prevId);
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckDelDep[] = schema.getTableDelSubDep1().readDerivedByDelTopDepIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckDelDep.length > 0 ) {
			schema.getTableDelSubDep1().deleteDelSubDep1ByDelTopDepIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffDelTopDepByDelTopDepTblIdxKey keyDelTopDepTblIdx = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		keyDelTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffDelTopDepByUNameIdxKey keyUNameIdx = (CFBamBuffDelTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffDelTopDepByPrevIdxKey keyPrevIdx = (CFBamBuffDelTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffDelTopDepByNextIdxKey keyNextIdx = (CFBamBuffDelTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDelTopDepTblIdx.get( keyDelTopDepTblIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	@Override
	public void deleteDelTopDepByDelTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffDelTopDepByDelTopDepTblIdxKey key = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		key.setRequiredTableId( argTableId );
		deleteDelTopDepByDelTopDepTblIdx( Authorization, key );
	}

	@Override
	public void deleteDelTopDepByDelTopDepTblIdx( ICFSecAuthorization Authorization,
		ICFBamDelTopDepByDelTopDepTblIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteDelTopDepByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffDelTopDepByUNameIdxKey key = (CFBamBuffDelTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteDelTopDepByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteDelTopDepByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamDelTopDepByUNameIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteDelTopDepByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffDelTopDepByPrevIdxKey key = (CFBamBuffDelTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteDelTopDepByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteDelTopDepByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamDelTopDepByPrevIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteDelTopDepByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffDelTopDepByNextIdxKey key = (CFBamBuffDelTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryDelTopDep().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteDelTopDepByNextIdx( Authorization, key );
	}

	@Override
	public void deleteDelTopDepByNextIdx( ICFSecAuthorization Authorization,
		ICFBamDelTopDepByNextIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteDelTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffDelDepByDefSchemaIdxKey key = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryDelDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelTopDepByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteDelTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteDelTopDepByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffDelDepByDelDepIdxKey key = (CFBamBuffDelDepByDelDepIdxKey)schema.getCFBamBuffFactory().getFactoryDelDep().newByDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelTopDepByDelDepIdx( Authorization, key );
	}

	@Override
	public void deleteDelTopDepByDelDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteDelTopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffDelTopDep cur;
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteDelTopDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamBuffFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelTopDepByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteDelTopDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}
}
