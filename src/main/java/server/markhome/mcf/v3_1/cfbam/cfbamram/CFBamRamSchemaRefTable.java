
// Description: Java 25 in-memory RAM DbIO implementation for SchemaRef.

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
 *	CFBamRamSchemaRefTable in-memory RAM DbIO implementation
 *	for SchemaRef.
 */
public class CFBamRamSchemaRefTable
	implements ICFBamSchemaRefTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffSchemaRef > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffSchemaRef >();
	private Map< CFBamBuffSchemaRefBySchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRef >> dictBySchemaIdx
		= new HashMap< CFBamBuffSchemaRefBySchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRef >>();
	private Map< CFBamBuffSchemaRefByUNameIdxKey,
			CFBamBuffSchemaRef > dictByUNameIdx
		= new HashMap< CFBamBuffSchemaRefByUNameIdxKey,
			CFBamBuffSchemaRef >();
	private Map< CFBamBuffSchemaRefByRefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRef >> dictByRefSchemaIdx
		= new HashMap< CFBamBuffSchemaRefByRefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRef >>();
	private Map< CFBamBuffSchemaRefByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRef >> dictByPrevIdx
		= new HashMap< CFBamBuffSchemaRefByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRef >>();
	private Map< CFBamBuffSchemaRefByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRef >> dictByNextIdx
		= new HashMap< CFBamBuffSchemaRefByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRef >>();

	public CFBamRamSchemaRefTable( ICFBamSchema argSchema ) {
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
	public ICFBamSchemaRef createSchemaRef( ICFSecAuthorization Authorization,
		ICFBamSchemaRef iBuff )
	{
		final String S_ProcName = "createSchemaRef";
		
		CFBamBuffSchemaRef Buff = (CFBamBuffSchemaRef)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		ICFBamSchemaRef tail = null;
		if( Buff.getClassCode() == ICFBamSchemaRef.CLASS_CODE ) {
			ICFBamSchemaRef[] siblings = schema.getTableSchemaRef().readDerivedBySchemaIdx( Authorization,
				Buff.getRequiredSchemaId() );
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
		CFBamBuffSchemaRefBySchemaIdxKey keySchemaIdx = (CFBamBuffSchemaRefBySchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newBySchemaIdxKey();
		keySchemaIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );

		CFBamBuffSchemaRefByUNameIdxKey keyUNameIdx = (CFBamBuffSchemaRefByUNameIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByUNameIdxKey();
		keyUNameIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffSchemaRefByRefSchemaIdxKey keyRefSchemaIdx = (CFBamBuffSchemaRefByRefSchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByRefSchemaIdxKey();
		keyRefSchemaIdx.setOptionalRefSchemaId( Buff.getOptionalRefSchemaId() );

		CFBamBuffSchemaRefByPrevIdxKey keyPrevIdx = (CFBamBuffSchemaRefByPrevIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffSchemaRefByNextIdxKey keyNextIdx = (CFBamBuffSchemaRefByNextIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"SchemaRefUNameIdx",
				"SchemaRefUNameIdx",
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredSchemaId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"Schema",
						"Schema",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictSchemaIdx;
		if( dictBySchemaIdx.containsKey( keySchemaIdx ) ) {
			subdictSchemaIdx = dictBySchemaIdx.get( keySchemaIdx );
		}
		else {
			subdictSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictBySchemaIdx.put( keySchemaIdx, subdictSchemaIdx );
		}
		subdictSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictRefSchemaIdx;
		if( dictByRefSchemaIdx.containsKey( keyRefSchemaIdx ) ) {
			subdictRefSchemaIdx = dictByRefSchemaIdx.get( keyRefSchemaIdx );
		}
		else {
			subdictRefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictByRefSchemaIdx.put( keyRefSchemaIdx, subdictRefSchemaIdx );
		}
		subdictRefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			int tailClassCode = tail.getClassCode();
			if( tailClassCode == ICFBamSchemaRef.CLASS_CODE ) {
				ICFBamSchemaRef tailEdit = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
				tailEdit.set( (ICFBamSchemaRef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableSchemaRef().updateSchemaRef( Authorization, tailEdit );
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
			if (classCode == ICFBamSchemaRef.CLASS_CODE) {
				CFBamBuffSchemaRef retbuff = ((CFBamBuffSchemaRef)(schema.getCFBamBuffFactory().getFactorySchemaRef().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamSchemaRef readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerived";
		ICFBamSchemaRef buff;
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
	public ICFBamSchemaRef lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.lockDerived";
		ICFBamSchemaRef buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRef[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaRef.readAllDerived";
		ICFBamSchemaRef[] retList = new ICFBamSchemaRef[ dictByPKey.values().size() ];
		Iterator< CFBamBuffSchemaRef > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamSchemaRef[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamSchemaRef> filteredList = new ArrayList<ICFBamSchemaRef>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamSchemaRef ) ) {
					filteredList.add( (ICFBamSchemaRef)buff );
				}
			}
			return( filteredList.toArray( new ICFBamSchemaRef[0] ) );
		}
	}

	@Override
	public ICFBamSchemaRef[] readDerivedBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedBySchemaIdx";
		CFBamBuffSchemaRefBySchemaIdxKey key = (CFBamBuffSchemaRefBySchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newBySchemaIdxKey();

		key.setRequiredSchemaId( SchemaId );
		ICFBamSchemaRef[] recArray;
		if( dictBySchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictSchemaIdx
				= dictBySchemaIdx.get( key );
			recArray = new ICFBamSchemaRef[ subdictSchemaIdx.size() ];
			Iterator< CFBamBuffSchemaRef > iter = subdictSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictBySchemaIdx.put( key, subdictSchemaIdx );
			recArray = new ICFBamSchemaRef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaRef readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByUNameIdx";
		CFBamBuffSchemaRefByUNameIdxKey key = (CFBamBuffSchemaRefByUNameIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByUNameIdxKey();

		key.setRequiredSchemaId( SchemaId );
		key.setRequiredName( Name );
		ICFBamSchemaRef buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRef[] readDerivedByRefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RefSchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByRefSchemaIdx";
		CFBamBuffSchemaRefByRefSchemaIdxKey key = (CFBamBuffSchemaRefByRefSchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByRefSchemaIdxKey();

		key.setOptionalRefSchemaId( RefSchemaId );
		ICFBamSchemaRef[] recArray;
		if( dictByRefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictRefSchemaIdx
				= dictByRefSchemaIdx.get( key );
			recArray = new ICFBamSchemaRef[ subdictRefSchemaIdx.size() ];
			Iterator< CFBamBuffSchemaRef > iter = subdictRefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictRefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictByRefSchemaIdx.put( key, subdictRefSchemaIdx );
			recArray = new ICFBamSchemaRef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaRef[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByPrevIdx";
		CFBamBuffSchemaRefByPrevIdxKey key = (CFBamBuffSchemaRefByPrevIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByPrevIdxKey();

		key.setOptionalPrevId( PrevId );
		ICFBamSchemaRef[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamSchemaRef[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffSchemaRef > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamSchemaRef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaRef[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByNextIdx";
		CFBamBuffSchemaRefByNextIdxKey key = (CFBamBuffSchemaRefByNextIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByNextIdxKey();

		key.setOptionalNextId( NextId );
		ICFBamSchemaRef[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamSchemaRef[ subdictNextIdx.size() ];
			Iterator< CFBamBuffSchemaRef > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamSchemaRef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaRef readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamSchemaRef buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRef readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readRec";
		ICFBamSchemaRef buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamSchemaRef.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRef lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamSchemaRef buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamSchemaRef.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRef[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readAllRec";
		ICFBamSchemaRef buff;
		ArrayList<ICFBamSchemaRef> filteredList = new ArrayList<ICFBamSchemaRef>();
		ICFBamSchemaRef[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRef.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRef[0] ) );
	}

	@Override
	public ICFBamSchemaRef readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamSchemaRef buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamSchemaRef)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaRef[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamSchemaRef buff;
		ArrayList<ICFBamSchemaRef> filteredList = new ArrayList<ICFBamSchemaRef>();
		ICFBamSchemaRef[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRef[0] ) );
	}

	@Override
	public ICFBamSchemaRef[] readRecBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readRecBySchemaIdx() ";
		ICFBamSchemaRef buff;
		ArrayList<ICFBamSchemaRef> filteredList = new ArrayList<ICFBamSchemaRef>();
		ICFBamSchemaRef[] buffList = readDerivedBySchemaIdx( Authorization,
			SchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRef[0] ) );
	}

	@Override
	public ICFBamSchemaRef readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readRecByUNameIdx() ";
		ICFBamSchemaRef buff = readDerivedByUNameIdx( Authorization,
			SchemaId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRef.CLASS_CODE ) ) {
			return( (ICFBamSchemaRef)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaRef[] readRecByRefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RefSchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readRecByRefSchemaIdx() ";
		ICFBamSchemaRef buff;
		ArrayList<ICFBamSchemaRef> filteredList = new ArrayList<ICFBamSchemaRef>();
		ICFBamSchemaRef[] buffList = readDerivedByRefSchemaIdx( Authorization,
			RefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRef[0] ) );
	}

	@Override
	public ICFBamSchemaRef[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readRecByPrevIdx() ";
		ICFBamSchemaRef buff;
		ArrayList<ICFBamSchemaRef> filteredList = new ArrayList<ICFBamSchemaRef>();
		ICFBamSchemaRef[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRef[0] ) );
	}

	@Override
	public ICFBamSchemaRef[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readRecByNextIdx() ";
		ICFBamSchemaRef buff;
		ArrayList<ICFBamSchemaRef> filteredList = new ArrayList<ICFBamSchemaRef>();
		ICFBamSchemaRef[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRef[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamSchemaRef moveRecUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecUp";

		ICFBamSchemaRef grandprev = null;
		ICFBamSchemaRef prev = null;
		ICFBamSchemaRef cur = null;
		ICFBamSchemaRef next = null;

		cur = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffSchemaRef)cur );
		}

		prev = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() ));
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
			}
		}

		int classCode = prev.getClassCode();
		ICFBamSchemaRef newInstance;
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffSchemaRef editPrev = (CFBamBuffSchemaRef)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffSchemaRef editCur = (CFBamBuffSchemaRef)newInstance;
		editCur.set( cur );

		CFBamBuffSchemaRef editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = (CFBamBuffSchemaRef)newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffSchemaRef editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext = (CFBamBuffSchemaRef)newInstance;
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
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffSchemaRef)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamSchemaRef moveRecDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecDown";

		CFBamBuffSchemaRef prev = null;
		CFBamBuffSchemaRef cur = null;
		CFBamBuffSchemaRef next = null;
		CFBamBuffSchemaRef grandnext = null;

		cur = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, Id));
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffSchemaRef)cur );
		}

		next = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, next.getOptionalNextId() ));
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
			}
		}

		int classCode = cur.getClassCode();
		ICFBamSchemaRef newInstance;
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffSchemaRef editCur = (CFBamBuffSchemaRef)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffSchemaRef editNext = (CFBamBuffSchemaRef)newInstance;
		editNext.set( next );

		CFBamBuffSchemaRef editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = (CFBamBuffSchemaRef)newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffSchemaRef editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				newInstance = schema.getCFBamBuffFactory().getFactorySchemaRef().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev = (CFBamBuffSchemaRef)newInstance;
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
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffSchemaRef)editCur );
	}

	public ICFBamSchemaRef updateSchemaRef( ICFSecAuthorization Authorization,
		ICFBamSchemaRef iBuff )
	{
		CFBamBuffSchemaRef Buff = (CFBamBuffSchemaRef)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffSchemaRef existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaRef",
				"Existing record not found",
				"Existing record not found",
				"SchemaRef",
				"SchemaRef",
				pkey );
		}
		CFBamBuffSchemaRefBySchemaIdxKey existingKeySchemaIdx = (CFBamBuffSchemaRefBySchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newBySchemaIdxKey();
		existingKeySchemaIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );

		CFBamBuffSchemaRefBySchemaIdxKey newKeySchemaIdx = (CFBamBuffSchemaRefBySchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newBySchemaIdxKey();
		newKeySchemaIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );

		CFBamBuffSchemaRefByUNameIdxKey existingKeyUNameIdx = (CFBamBuffSchemaRefByUNameIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffSchemaRefByUNameIdxKey newKeyUNameIdx = (CFBamBuffSchemaRefByUNameIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffSchemaRefByRefSchemaIdxKey existingKeyRefSchemaIdx = (CFBamBuffSchemaRefByRefSchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByRefSchemaIdxKey();
		existingKeyRefSchemaIdx.setOptionalRefSchemaId( existing.getOptionalRefSchemaId() );

		CFBamBuffSchemaRefByRefSchemaIdxKey newKeyRefSchemaIdx = (CFBamBuffSchemaRefByRefSchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByRefSchemaIdxKey();
		newKeyRefSchemaIdx.setOptionalRefSchemaId( Buff.getOptionalRefSchemaId() );

		CFBamBuffSchemaRefByPrevIdxKey existingKeyPrevIdx = (CFBamBuffSchemaRefByPrevIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffSchemaRefByPrevIdxKey newKeyPrevIdx = (CFBamBuffSchemaRefByPrevIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffSchemaRefByNextIdxKey existingKeyNextIdx = (CFBamBuffSchemaRefByNextIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffSchemaRefByNextIdxKey newKeyNextIdx = (CFBamBuffSchemaRefByNextIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateSchemaRef",
					"SchemaRefUNameIdx",
					"SchemaRefUNameIdx",
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
						"updateSchemaRef",
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
						Buff.getRequiredSchemaId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaRef",
						"Container",
						"Container",
						"Schema",
						"Schema",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictBySchemaIdx.get( existingKeySchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaIdx.containsKey( newKeySchemaIdx ) ) {
			subdict = dictBySchemaIdx.get( newKeySchemaIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictBySchemaIdx.put( newKeySchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByRefSchemaIdx.get( existingKeyRefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRefSchemaIdx.containsKey( newKeyRefSchemaIdx ) ) {
			subdict = dictByRefSchemaIdx.get( newKeyRefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictByRefSchemaIdx.put( newKeyRefSchemaIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRef >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteSchemaRef( ICFSecAuthorization Authorization,
		ICFBamSchemaRef iBuff )
	{
		final String S_ProcName = "CFBamRamSchemaRefTable.deleteSchemaRef() ";
		CFBamBuffSchemaRef Buff = (CFBamBuffSchemaRef)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffSchemaRef existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaRef",
				pkey );
		}
		CFLibDbKeyHash256 varSchemaId = existing.getRequiredSchemaId();
		CFBamBuffSchemaDef container = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
			varSchemaId ));
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamBuffSchemaRef prev = null;
		if( ( prevId != null ) )
		{
			prev = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				prevId ));
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamBuffSchemaRef editPrev;
			classCode = prev.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				editPrev = (CFBamBuffSchemaRef)(schema.getCFBamBuffFactory().getFactorySchemaRef().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalLookupNext(nextId);
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffSchemaRef next = null;
		if( ( nextId != null ) )
		{
			next = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				nextId ));
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamBuffSchemaRef editNext;
			classCode = next.getClassCode();
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				editNext = (CFBamBuffSchemaRef)(schema.getCFBamBuffFactory().getFactorySchemaRef().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalLookupPrev(prevId);
			if( classCode == ICFBamSchemaRef.CLASS_CODE ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffSchemaRefBySchemaIdxKey keySchemaIdx = (CFBamBuffSchemaRefBySchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newBySchemaIdxKey();
		keySchemaIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );

		CFBamBuffSchemaRefByUNameIdxKey keyUNameIdx = (CFBamBuffSchemaRefByUNameIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByUNameIdxKey();
		keyUNameIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffSchemaRefByRefSchemaIdxKey keyRefSchemaIdx = (CFBamBuffSchemaRefByRefSchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByRefSchemaIdxKey();
		keyRefSchemaIdx.setOptionalRefSchemaId( existing.getOptionalRefSchemaId() );

		CFBamBuffSchemaRefByPrevIdxKey keyPrevIdx = (CFBamBuffSchemaRefByPrevIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffSchemaRefByNextIdxKey keyNextIdx = (CFBamBuffSchemaRefByNextIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffSchemaRef > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaIdx.get( keySchemaIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByRefSchemaIdx.get( keyRefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deleteSchemaRefBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaId )
	{
		CFBamBuffSchemaRefBySchemaIdxKey key = (CFBamBuffSchemaRefBySchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newBySchemaIdxKey();
		key.setRequiredSchemaId( argSchemaId );
		deleteSchemaRefBySchemaIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRefBySchemaIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaRefBySchemaIdxKey argKey )
	{
		CFBamBuffSchemaRef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRef> matchSet = new LinkedList<CFBamBuffSchemaRef>();
		Iterator<CFBamBuffSchemaRef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRefByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaId,
		String argName )
	{
		CFBamBuffSchemaRefByUNameIdxKey key = (CFBamBuffSchemaRefByUNameIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByUNameIdxKey();
		key.setRequiredSchemaId( argSchemaId );
		key.setRequiredName( argName );
		deleteSchemaRefByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRefByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaRefByUNameIdxKey argKey )
	{
		CFBamBuffSchemaRef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRef> matchSet = new LinkedList<CFBamBuffSchemaRef>();
		Iterator<CFBamBuffSchemaRef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRefByRefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRefSchemaId )
	{
		CFBamBuffSchemaRefByRefSchemaIdxKey key = (CFBamBuffSchemaRefByRefSchemaIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByRefSchemaIdxKey();
		key.setOptionalRefSchemaId( argRefSchemaId );
		deleteSchemaRefByRefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRefByRefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaRefByRefSchemaIdxKey argKey )
	{
		CFBamBuffSchemaRef cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalRefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRef> matchSet = new LinkedList<CFBamBuffSchemaRef>();
		Iterator<CFBamBuffSchemaRef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRefByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffSchemaRefByPrevIdxKey key = (CFBamBuffSchemaRefByPrevIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteSchemaRefByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRefByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaRefByPrevIdxKey argKey )
	{
		CFBamBuffSchemaRef cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRef> matchSet = new LinkedList<CFBamBuffSchemaRef>();
		Iterator<CFBamBuffSchemaRef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRefByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffSchemaRefByNextIdxKey key = (CFBamBuffSchemaRefByNextIdxKey)schema.getCFBamBuffFactory().getFactorySchemaRef().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteSchemaRefByNextIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRefByNextIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaRefByNextIdxKey argKey )
	{
		CFBamBuffSchemaRef cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRef> matchSet = new LinkedList<CFBamBuffSchemaRef>();
		Iterator<CFBamBuffSchemaRef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRefByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffSchemaRef cur;
		LinkedList<CFBamBuffSchemaRef> matchSet = new LinkedList<CFBamBuffSchemaRef>();
		Iterator<CFBamBuffSchemaRef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRefByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamBuffFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteSchemaRefByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRefByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffSchemaRef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRef> matchSet = new LinkedList<CFBamBuffSchemaRef>();
		Iterator<CFBamBuffSchemaRef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRef)(schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRef( Authorization, cur );
		}
	}
}
