
// Description: Java 25 in-memory RAM DbIO implementation for ClearTopDep.

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
 *	CFBamRamClearTopDepTable in-memory RAM DbIO implementation
 *	for ClearTopDep.
 */
public class CFBamRamClearTopDepTable
	implements ICFBamClearTopDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffClearTopDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffClearTopDep >();
	private Map< CFBamBuffClearTopDepByClrTopDepTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >> dictByClrTopDepTblIdx
		= new HashMap< CFBamBuffClearTopDepByClrTopDepTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >>();
	private Map< CFBamBuffClearTopDepByUNameIdxKey,
			CFBamBuffClearTopDep > dictByUNameIdx
		= new HashMap< CFBamBuffClearTopDepByUNameIdxKey,
			CFBamBuffClearTopDep >();
	private Map< CFBamBuffClearTopDepByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >> dictByPrevIdx
		= new HashMap< CFBamBuffClearTopDepByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >>();
	private Map< CFBamBuffClearTopDepByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >> dictByNextIdx
		= new HashMap< CFBamBuffClearTopDepByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >>();

	public CFBamRamClearTopDepTable( ICFBamSchema argSchema ) {
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
	public ICFBamClearTopDep createClearTopDep( ICFSecAuthorization Authorization,
		ICFBamClearTopDep iBuff )
	{
		final String S_ProcName = "createClearTopDep";
		
		CFBamBuffClearTopDep Buff = (CFBamBuffClearTopDep)(schema.getTableClearDep().createClearDep( Authorization,
			iBuff ));
		ICFBamClearTopDep tail = null;
		if( Buff.getClassCode() == ICFBamClearTopDep.CLASS_CODE ) {
			ICFBamClearTopDep[] siblings = schema.getTableClearTopDep().readDerivedByClrTopDepTblIdx( Authorization,
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
		CFBamBuffClearTopDepByClrTopDepTblIdxKey keyClrTopDepTblIdx = (CFBamBuffClearTopDepByClrTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByClrTopDepTblIdxKey();
		keyClrTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffClearTopDepByUNameIdxKey keyUNameIdx = (CFBamBuffClearTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffClearTopDepByPrevIdxKey keyPrevIdx = (CFBamBuffClearTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffClearTopDepByNextIdxKey keyNextIdx = (CFBamBuffClearTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearTopDepUNameIdx",
				"ClearTopDepUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"ClearDep",
						"ClearDep",
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

		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictClrTopDepTblIdx;
		if( dictByClrTopDepTblIdx.containsKey( keyClrTopDepTblIdx ) ) {
			subdictClrTopDepTblIdx = dictByClrTopDepTblIdx.get( keyClrTopDepTblIdx );
		}
		else {
			subdictClrTopDepTblIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByClrTopDepTblIdx.put( keyClrTopDepTblIdx, subdictClrTopDepTblIdx );
		}
		subdictClrTopDepTblIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			int tailClassCode = tail.getClassCode();
			if( tailClassCode == ICFBamClearTopDep.CLASS_CODE ) {
				ICFBamClearTopDep tailEdit = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
				tailEdit.set( (ICFBamClearTopDep)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableClearTopDep().updateClearTopDep( Authorization, tailEdit );
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
			if (classCode == ICFBamClearTopDep.CLASS_CODE) {
				CFBamBuffClearTopDep retbuff = ((CFBamBuffClearTopDep)(schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamClearTopDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerived";
		ICFBamClearTopDep buff;
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
	public ICFBamClearTopDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.lockDerived";
		ICFBamClearTopDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearTopDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearTopDep.readAllDerived";
		ICFBamClearTopDep[] retList = new ICFBamClearTopDep[ dictByPKey.values().size() ];
		Iterator< CFBamBuffClearTopDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamClearTopDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearTopDep ) ) {
					filteredList.add( (ICFBamClearTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
		}
	}

	@Override
	public ICFBamClearTopDep[] readDerivedByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		ICFBamClearDep buffList[] = schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamClearDep buff;
			ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearTopDep ) ) {
					filteredList.add( (ICFBamClearTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
		}
	}

	@Override
	public ICFBamClearTopDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		ICFBamClearDep buffList[] = schema.getTableClearDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamClearDep buff;
			ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearTopDep ) ) {
					filteredList.add( (ICFBamClearTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
		}
	}

	@Override
	public ICFBamClearTopDep[] readDerivedByClrTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByClrTopDepTblIdx";
		CFBamBuffClearTopDepByClrTopDepTblIdxKey key = (CFBamBuffClearTopDepByClrTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByClrTopDepTblIdxKey();

		key.setRequiredTableId( TableId );
		ICFBamClearTopDep[] recArray;
		if( dictByClrTopDepTblIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictClrTopDepTblIdx
				= dictByClrTopDepTblIdx.get( key );
			recArray = new ICFBamClearTopDep[ subdictClrTopDepTblIdx.size() ];
			Iterator< CFBamBuffClearTopDep > iter = subdictClrTopDepTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictClrTopDepTblIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByClrTopDepTblIdx.put( key, subdictClrTopDepTblIdx );
			recArray = new ICFBamClearTopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamClearTopDep readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByUNameIdx";
		CFBamBuffClearTopDepByUNameIdxKey key = (CFBamBuffClearTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByUNameIdxKey();

		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );
		ICFBamClearTopDep buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearTopDep[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByPrevIdx";
		CFBamBuffClearTopDepByPrevIdxKey key = (CFBamBuffClearTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByPrevIdxKey();

		key.setOptionalPrevId( PrevId );
		ICFBamClearTopDep[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamClearTopDep[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffClearTopDep > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamClearTopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamClearTopDep[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByNextIdx";
		CFBamBuffClearTopDepByNextIdxKey key = (CFBamBuffClearTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByNextIdxKey();

		key.setOptionalNextId( NextId );
		ICFBamClearTopDep[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamClearTopDep[ subdictNextIdx.size() ];
			Iterator< CFBamBuffClearTopDep > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamClearTopDep[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamClearTopDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamClearTopDep buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearTopDep readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readRec";
		ICFBamClearTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearTopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearTopDep lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamClearTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamClearTopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamClearTopDep[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readAllRec";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearTopDep.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	@Override
	public ICFBamClearTopDep readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamClearTopDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamClearTopDep)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamClearTopDep[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	@Override
	public ICFBamClearTopDep[] readRecByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByClearDepIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	@Override
	public ICFBamClearTopDep[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readRecByDefSchemaIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	@Override
	public ICFBamClearTopDep[] readRecByClrTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readRecByClrTopDepTblIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByClrTopDepTblIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	@Override
	public ICFBamClearTopDep readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readRecByUNameIdx() ";
		ICFBamClearTopDep buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearTopDep.CLASS_CODE ) ) {
			return( (ICFBamClearTopDep)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamClearTopDep[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readRecByPrevIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	@Override
	public ICFBamClearTopDep[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readRecByNextIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamClearTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamClearTopDep moveRecUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecUp";

		ICFBamClearTopDep grandprev = null;
		ICFBamClearTopDep prev = null;
		ICFBamClearTopDep cur = null;
		ICFBamClearTopDep next = null;

		cur = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffClearTopDep)cur );
		}

		prev = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() ));
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
			}
		}

		int classCode = prev.getClassCode();
		ICFBamClearTopDep newInstance;
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffClearTopDep editPrev = (CFBamBuffClearTopDep)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffClearTopDep editCur = (CFBamBuffClearTopDep)newInstance;
		editCur.set( cur );

		CFBamBuffClearTopDep editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = (CFBamBuffClearTopDep)newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffClearTopDep editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext = (CFBamBuffClearTopDep)newInstance;
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
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffClearTopDep)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamClearTopDep moveRecDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecDown";

		CFBamBuffClearTopDep prev = null;
		CFBamBuffClearTopDep cur = null;
		CFBamBuffClearTopDep next = null;
		CFBamBuffClearTopDep grandnext = null;

		cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, Id));
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffClearTopDep)cur );
		}

		next = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, next.getOptionalNextId() ));
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
			}
		}

		int classCode = cur.getClassCode();
		ICFBamClearTopDep newInstance;
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffClearTopDep editCur = (CFBamBuffClearTopDep)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffClearTopDep editNext = (CFBamBuffClearTopDep)newInstance;
		editNext.set( next );

		CFBamBuffClearTopDep editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = (CFBamBuffClearTopDep)newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffClearTopDep editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev = (CFBamBuffClearTopDep)newInstance;
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
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffClearTopDep)editCur );
	}

	public ICFBamClearTopDep updateClearTopDep( ICFSecAuthorization Authorization,
		ICFBamClearTopDep iBuff )
	{
		CFBamBuffClearTopDep Buff = (CFBamBuffClearTopDep)(schema.getTableClearDep().updateClearDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffClearTopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearTopDep",
				"Existing record not found",
				"Existing record not found",
				"ClearTopDep",
				"ClearTopDep",
				pkey );
		}
		CFBamBuffClearTopDepByClrTopDepTblIdxKey existingKeyClrTopDepTblIdx = (CFBamBuffClearTopDepByClrTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByClrTopDepTblIdxKey();
		existingKeyClrTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffClearTopDepByClrTopDepTblIdxKey newKeyClrTopDepTblIdx = (CFBamBuffClearTopDepByClrTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByClrTopDepTblIdxKey();
		newKeyClrTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffClearTopDepByUNameIdxKey existingKeyUNameIdx = (CFBamBuffClearTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearTopDepByUNameIdxKey newKeyUNameIdx = (CFBamBuffClearTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffClearTopDepByPrevIdxKey existingKeyPrevIdx = (CFBamBuffClearTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffClearTopDepByPrevIdxKey newKeyPrevIdx = (CFBamBuffClearTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffClearTopDepByNextIdxKey existingKeyNextIdx = (CFBamBuffClearTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffClearTopDepByNextIdxKey newKeyNextIdx = (CFBamBuffClearTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearTopDep",
					"ClearTopDepUNameIdx",
					"ClearTopDepUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearTopDep",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"ClearDep",
						"ClearDep",
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
						"updateClearTopDep",
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

		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClrTopDepTblIdx.get( existingKeyClrTopDepTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClrTopDepTblIdx.containsKey( newKeyClrTopDepTblIdx ) ) {
			subdict = dictByClrTopDepTblIdx.get( newKeyClrTopDepTblIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByClrTopDepTblIdx.put( newKeyClrTopDepTblIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteClearTopDep( ICFSecAuthorization Authorization,
		ICFBamClearTopDep iBuff )
	{
		final String S_ProcName = "CFBamRamClearTopDepTable.deleteClearTopDep() ";
		CFBamBuffClearTopDep Buff = (CFBamBuffClearTopDep)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffClearTopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearTopDep",
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

		CFBamBuffClearTopDep prev = null;
		if( ( prevId != null ) )
		{
			prev = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				prevId ));
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamBuffClearTopDep editPrev;
			classCode = prev.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				editPrev = (CFBamBuffClearTopDep)(schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalLookupNext(nextId);
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffClearTopDep next = null;
		if( ( nextId != null ) )
		{
			next = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				nextId ));
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamBuffClearTopDep editNext;
			classCode = next.getClassCode();
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				editNext = (CFBamBuffClearTopDep)(schema.getCFBamBuffFactory().getFactoryClearTopDep().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalLookupPrev(prevId);
			if( classCode == ICFBamClearTopDep.CLASS_CODE ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckClearDep[] = schema.getTableClearSubDep1().readDerivedByClearTopDepIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckClearDep.length > 0 ) {
			schema.getTableClearSubDep1().deleteClearSubDep1ByClearTopDepIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffClearTopDepByClrTopDepTblIdxKey keyClrTopDepTblIdx = (CFBamBuffClearTopDepByClrTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByClrTopDepTblIdxKey();
		keyClrTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffClearTopDepByUNameIdxKey keyUNameIdx = (CFBamBuffClearTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearTopDepByPrevIdxKey keyPrevIdx = (CFBamBuffClearTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffClearTopDepByNextIdxKey keyNextIdx = (CFBamBuffClearTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClrTopDepTblIdx.get( keyClrTopDepTblIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	@Override
	public void deleteClearTopDepByClrTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffClearTopDepByClrTopDepTblIdxKey key = (CFBamBuffClearTopDepByClrTopDepTblIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByClrTopDepTblIdxKey();
		key.setRequiredTableId( argTableId );
		deleteClearTopDepByClrTopDepTblIdx( Authorization, key );
	}

	@Override
	public void deleteClearTopDepByClrTopDepTblIdx( ICFSecAuthorization Authorization,
		ICFBamClearTopDepByClrTopDepTblIdxKey argKey )
	{
		CFBamBuffClearTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearTopDep> matchSet = new LinkedList<CFBamBuffClearTopDep>();
		Iterator<CFBamBuffClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteClearTopDepByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffClearTopDepByUNameIdxKey key = (CFBamBuffClearTopDepByUNameIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteClearTopDepByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteClearTopDepByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamClearTopDepByUNameIdxKey argKey )
	{
		CFBamBuffClearTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearTopDep> matchSet = new LinkedList<CFBamBuffClearTopDep>();
		Iterator<CFBamBuffClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteClearTopDepByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffClearTopDepByPrevIdxKey key = (CFBamBuffClearTopDepByPrevIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteClearTopDepByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteClearTopDepByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamClearTopDepByPrevIdxKey argKey )
	{
		CFBamBuffClearTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearTopDep> matchSet = new LinkedList<CFBamBuffClearTopDep>();
		Iterator<CFBamBuffClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteClearTopDepByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffClearTopDepByNextIdxKey key = (CFBamBuffClearTopDepByNextIdxKey)schema.getCFBamBuffFactory().getFactoryClearTopDep().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteClearTopDepByNextIdx( Authorization, key );
	}

	@Override
	public void deleteClearTopDepByNextIdx( ICFSecAuthorization Authorization,
		ICFBamClearTopDepByNextIdxKey argKey )
	{
		CFBamBuffClearTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearTopDep> matchSet = new LinkedList<CFBamBuffClearTopDep>();
		Iterator<CFBamBuffClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteClearTopDepByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffClearDepByClearDepIdxKey key = (CFBamBuffClearDepByClearDepIdxKey)schema.getCFBamBuffFactory().getFactoryClearDep().newByClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearTopDepByClearDepIdx( Authorization, key );
	}

	@Override
	public void deleteClearTopDepByClearDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamBuffClearTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearTopDep> matchSet = new LinkedList<CFBamBuffClearTopDep>();
		Iterator<CFBamBuffClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteClearTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffClearDepByDefSchemaIdxKey key = (CFBamBuffClearDepByDefSchemaIdxKey)schema.getCFBamBuffFactory().getFactoryClearDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearTopDepByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteClearTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffClearTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearTopDep> matchSet = new LinkedList<CFBamBuffClearTopDep>();
		Iterator<CFBamBuffClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteClearTopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffClearTopDep cur;
		LinkedList<CFBamBuffClearTopDep> matchSet = new LinkedList<CFBamBuffClearTopDep>();
		Iterator<CFBamBuffClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearTopDep( Authorization, cur );
		}
	}

	@Override
	public void deleteClearTopDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamBuffFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearTopDepByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteClearTopDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffClearTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffClearTopDep> matchSet = new LinkedList<CFBamBuffClearTopDep>();
		Iterator<CFBamBuffClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffClearTopDep)(schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteClearTopDep( Authorization, cur );
		}
	}
}
