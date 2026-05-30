
// Description: Java 25 in-memory RAM DbIO implementation for Param.

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
 *	CFBamRamParamTable in-memory RAM DbIO implementation
 *	for Param.
 */
public class CFBamRamParamTable
	implements ICFBamParamTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffParam > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffParam >();
	private Map< CFBamBuffParamByUNameIdxKey,
			CFBamBuffParam > dictByUNameIdx
		= new HashMap< CFBamBuffParamByUNameIdxKey,
			CFBamBuffParam >();
	private Map< CFBamBuffParamByServerMethodIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByServerMethodIdx
		= new HashMap< CFBamBuffParamByServerMethodIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffParamByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByServerTypeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByServerTypeIdx
		= new HashMap< CFBamBuffParamByServerTypeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByPrevIdx
		= new HashMap< CFBamBuffParamByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByNextIdx
		= new HashMap< CFBamBuffParamByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByContPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByContPrevIdx
		= new HashMap< CFBamBuffParamByContPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByContNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByContNextIdx
		= new HashMap< CFBamBuffParamByContNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();

	public CFBamRamParamTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffParam ensureRec(ICFBamParam rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			if (classCode == ICFBamParam.CLASS_CODE) {
				return( ((CFBamBuffParamDefaultFactory)(schema.getFactoryParam())).ensureRec((ICFBamParam)rec) );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), "ensureRec", "rec", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamParam createParam( ICFSecAuthorization Authorization,
		ICFBamParam iBuff )
	{
		final String S_ProcName = "createParam";
		
		CFBamBuffParam Buff = (CFBamBuffParam)ensureRec(iBuff);
			ICFBamParam tail = null;

			ICFBamParam[] siblings = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
				Buff.getRequiredServerMethodId() );
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
		pkey = schema.nextParamIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffParamByUNameIdxKey keyUNameIdx = (CFBamBuffParamByUNameIdxKey)schema.getFactoryParam().newByUNameIdxKey();
		keyUNameIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffParamByServerMethodIdxKey keyServerMethodIdx = (CFBamBuffParamByServerMethodIdxKey)schema.getFactoryParam().newByServerMethodIdxKey();
		keyServerMethodIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );

		CFBamBuffParamByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffParamByDefSchemaIdxKey)schema.getFactoryParam().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffParamByServerTypeIdxKey keyServerTypeIdx = (CFBamBuffParamByServerTypeIdxKey)schema.getFactoryParam().newByServerTypeIdxKey();
		keyServerTypeIdx.setOptionalTypeId( Buff.getOptionalTypeId() );

		CFBamBuffParamByPrevIdxKey keyPrevIdx = (CFBamBuffParamByPrevIdxKey)schema.getFactoryParam().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffParamByNextIdxKey keyNextIdx = (CFBamBuffParamByNextIdxKey)schema.getFactoryParam().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffParamByContPrevIdxKey keyContPrevIdx = (CFBamBuffParamByContPrevIdxKey)schema.getFactoryParam().newByContPrevIdxKey();
		keyContPrevIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffParamByContNextIdxKey keyContNextIdx = (CFBamBuffParamByContNextIdxKey)schema.getFactoryParam().newByContNextIdxKey();
		keyContNextIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyContNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ParamUNameIdx",
				"ParamUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredServerMethodId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"ServerMethod",
						"ServerMethod",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			if( Buff.getOptionalTypeId() != null ) {
				allNull = false;
			}
			if( ! allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getOptionalTypeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Lookup",
						"Type",
						"Type",
						"Value",
						"Value",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerMethodIdx;
		if( dictByServerMethodIdx.containsKey( keyServerMethodIdx ) ) {
			subdictServerMethodIdx = dictByServerMethodIdx.get( keyServerMethodIdx );
		}
		else {
			subdictServerMethodIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerMethodIdx.put( keyServerMethodIdx, subdictServerMethodIdx );
		}
		subdictServerMethodIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerTypeIdx;
		if( dictByServerTypeIdx.containsKey( keyServerTypeIdx ) ) {
			subdictServerTypeIdx = dictByServerTypeIdx.get( keyServerTypeIdx );
		}
		else {
			subdictServerTypeIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerTypeIdx.put( keyServerTypeIdx, subdictServerTypeIdx );
		}
		subdictServerTypeIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContPrevIdx;
		if( dictByContPrevIdx.containsKey( keyContPrevIdx ) ) {
			subdictContPrevIdx = dictByContPrevIdx.get( keyContPrevIdx );
		}
		else {
			subdictContPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContPrevIdx.put( keyContPrevIdx, subdictContPrevIdx );
		}
		subdictContPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContNextIdx;
		if( dictByContNextIdx.containsKey( keyContNextIdx ) ) {
			subdictContNextIdx = dictByContNextIdx.get( keyContNextIdx );
		}
		else {
			subdictContNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContNextIdx.put( keyContNextIdx, subdictContNextIdx );
		}
		subdictContNextIdx.put( pkey, Buff );

		if( tail != null ) {
			ICFBamParam tailEdit = schema.getFactoryParam().newRec();
			tailEdit.set( (ICFBamParam)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
			schema.getTableParam().updateParam( Authorization, tailEdit );
		}
		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamParam.CLASS_CODE) {
				CFBamBuffParam retbuff = ((CFBamBuffParam)(schema.getFactoryParam().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamParam readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamParam.readDerived";
		ICFBamParam buff;
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
	public ICFBamParam lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamParam.lockDerived";
		ICFBamParam buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamParam[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamParam.readAllDerived";
		ICFBamParam[] retList = new ICFBamParam[ dictByPKey.values().size() ];
		Iterator< CFBamBuffParam > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamParam readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		String Name )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByUNameIdx";
		CFBamBuffParamByUNameIdxKey key = (CFBamBuffParamByUNameIdxKey)schema.getFactoryParam().newByUNameIdxKey();

		key.setRequiredServerMethodId( ServerMethodId );
		key.setRequiredName( Name );
		ICFBamParam buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamParam[] readDerivedByServerMethodIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByServerMethodIdx";
		CFBamBuffParamByServerMethodIdxKey key = (CFBamBuffParamByServerMethodIdxKey)schema.getFactoryParam().newByServerMethodIdxKey();

		key.setRequiredServerMethodId( ServerMethodId );
		ICFBamParam[] recArray;
		if( dictByServerMethodIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerMethodIdx
				= dictByServerMethodIdx.get( key );
			recArray = new ICFBamParam[ subdictServerMethodIdx.size() ];
			Iterator< CFBamBuffParam > iter = subdictServerMethodIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerMethodIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerMethodIdx.put( key, subdictServerMethodIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamParam[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByDefSchemaIdx";
		CFBamBuffParamByDefSchemaIdxKey key = (CFBamBuffParamByDefSchemaIdxKey)schema.getFactoryParam().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamParam[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamParam[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffParam > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamParam[] readDerivedByServerTypeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TypeId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByServerTypeIdx";
		CFBamBuffParamByServerTypeIdxKey key = (CFBamBuffParamByServerTypeIdxKey)schema.getFactoryParam().newByServerTypeIdxKey();

		key.setOptionalTypeId( TypeId );
		ICFBamParam[] recArray;
		if( dictByServerTypeIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerTypeIdx
				= dictByServerTypeIdx.get( key );
			recArray = new ICFBamParam[ subdictServerTypeIdx.size() ];
			Iterator< CFBamBuffParam > iter = subdictServerTypeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerTypeIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerTypeIdx.put( key, subdictServerTypeIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamParam[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByPrevIdx";
		CFBamBuffParamByPrevIdxKey key = (CFBamBuffParamByPrevIdxKey)schema.getFactoryParam().newByPrevIdxKey();

		key.setOptionalPrevId( PrevId );
		ICFBamParam[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamParam[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffParam > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamParam[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByNextIdx";
		CFBamBuffParamByNextIdxKey key = (CFBamBuffParamByNextIdxKey)schema.getFactoryParam().newByNextIdxKey();

		key.setOptionalNextId( NextId );
		ICFBamParam[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamParam[ subdictNextIdx.size() ];
			Iterator< CFBamBuffParam > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamParam[] readDerivedByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByContPrevIdx";
		CFBamBuffParamByContPrevIdxKey key = (CFBamBuffParamByContPrevIdxKey)schema.getFactoryParam().newByContPrevIdxKey();

		key.setRequiredServerMethodId( ServerMethodId );
		key.setOptionalPrevId( PrevId );
		ICFBamParam[] recArray;
		if( dictByContPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContPrevIdx
				= dictByContPrevIdx.get( key );
			recArray = new ICFBamParam[ subdictContPrevIdx.size() ];
			Iterator< CFBamBuffParam > iter = subdictContPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContPrevIdx.put( key, subdictContPrevIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamParam[] readDerivedByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByContNextIdx";
		CFBamBuffParamByContNextIdxKey key = (CFBamBuffParamByContNextIdxKey)schema.getFactoryParam().newByContNextIdxKey();

		key.setRequiredServerMethodId( ServerMethodId );
		key.setOptionalNextId( NextId );
		ICFBamParam[] recArray;
		if( dictByContNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContNextIdx
				= dictByContNextIdx.get( key );
			recArray = new ICFBamParam[ subdictContNextIdx.size() ];
			Iterator< CFBamBuffParam > iter = subdictContNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContNextIdx.put( key, subdictContNextIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamParam readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByIdIdx() ";
		ICFBamParam buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamParam readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamParam.readRec";
		ICFBamParam buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamParam.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamParam lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamParam buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamParam.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamParam[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamParam.readAllRec";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	@Override
	public ICFBamParam readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamParam.readRecByIdIdx() ";
		ICFBamParam buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
			return( (ICFBamParam)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamParam readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		String Name )
	{
		final String S_ProcName = "CFBamRamParam.readRecByUNameIdx() ";
		ICFBamParam buff = readDerivedByUNameIdx( Authorization,
			ServerMethodId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
			return( (ICFBamParam)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamParam[] readRecByServerMethodIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId )
	{
		final String S_ProcName = "CFBamRamParam.readRecByServerMethodIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByServerMethodIdx( Authorization,
			ServerMethodId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	@Override
	public ICFBamParam[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamParam.readRecByDefSchemaIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	@Override
	public ICFBamParam[] readRecByServerTypeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TypeId )
	{
		final String S_ProcName = "CFBamRamParam.readRecByServerTypeIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByServerTypeIdx( Authorization,
			TypeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	@Override
	public ICFBamParam[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readRecByPrevIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	@Override
	public ICFBamParam[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readRecByNextIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	@Override
	public ICFBamParam[] readRecByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readRecByContPrevIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByContPrevIdx( Authorization,
			ServerMethodId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	@Override
	public ICFBamParam[] readRecByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readRecByContNextIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByContNextIdx( Authorization,
			ServerMethodId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamParam.CLASS_CODE ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamParam moveRecUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecUp";

		ICFBamParam grandprev = null;
		ICFBamParam prev = null;
		ICFBamParam cur = null;
		ICFBamParam next = null;

		cur = schema.getTableParam().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffParam)cur );
		}

		prev = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() ));
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
			}
		}

		int classCode = prev.getClassCode();
		ICFBamParam newInstance;
			if( classCode == ICFBamParam.CLASS_CODE ) {
				newInstance = schema.getFactoryParam().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffParam editPrev = (CFBamBuffParam)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				newInstance = schema.getFactoryParam().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffParam editCur = (CFBamBuffParam)newInstance;
		editCur.set( cur );

		CFBamBuffParam editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				newInstance = schema.getFactoryParam().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = (CFBamBuffParam)newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffParam editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				newInstance = schema.getFactoryParam().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext = (CFBamBuffParam)newInstance;
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
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffParam)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamParam moveRecDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecDown";

		CFBamBuffParam prev = null;
		CFBamBuffParam cur = null;
		CFBamBuffParam next = null;
		CFBamBuffParam grandnext = null;

		cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx(Authorization, Id));
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffParam)cur );
		}

		next = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx(Authorization, next.getOptionalNextId() ));
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
			}
		}

		int classCode = cur.getClassCode();
		ICFBamParam newInstance;
			if( classCode == ICFBamParam.CLASS_CODE ) {
				newInstance = schema.getFactoryParam().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffParam editCur = (CFBamBuffParam)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				newInstance = schema.getFactoryParam().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffParam editNext = (CFBamBuffParam)newInstance;
		editNext.set( next );

		CFBamBuffParam editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				newInstance = schema.getFactoryParam().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = (CFBamBuffParam)newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffParam editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				newInstance = schema.getFactoryParam().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev = (CFBamBuffParam)newInstance;
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
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffParam)editCur );
	}

	public ICFBamParam updateParam( ICFSecAuthorization Authorization,
		ICFBamParam iBuff )
	{
		CFBamBuffParam Buff = (CFBamBuffParam)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffParam existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateParam",
				"Existing record not found",
				"Existing record not found",
				"Param",
				"Param",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateParam",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffParamByUNameIdxKey existingKeyUNameIdx = (CFBamBuffParamByUNameIdxKey)schema.getFactoryParam().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffParamByUNameIdxKey newKeyUNameIdx = (CFBamBuffParamByUNameIdxKey)schema.getFactoryParam().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffParamByServerMethodIdxKey existingKeyServerMethodIdx = (CFBamBuffParamByServerMethodIdxKey)schema.getFactoryParam().newByServerMethodIdxKey();
		existingKeyServerMethodIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );

		CFBamBuffParamByServerMethodIdxKey newKeyServerMethodIdx = (CFBamBuffParamByServerMethodIdxKey)schema.getFactoryParam().newByServerMethodIdxKey();
		newKeyServerMethodIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );

		CFBamBuffParamByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffParamByDefSchemaIdxKey)schema.getFactoryParam().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffParamByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffParamByDefSchemaIdxKey)schema.getFactoryParam().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffParamByServerTypeIdxKey existingKeyServerTypeIdx = (CFBamBuffParamByServerTypeIdxKey)schema.getFactoryParam().newByServerTypeIdxKey();
		existingKeyServerTypeIdx.setOptionalTypeId( existing.getOptionalTypeId() );

		CFBamBuffParamByServerTypeIdxKey newKeyServerTypeIdx = (CFBamBuffParamByServerTypeIdxKey)schema.getFactoryParam().newByServerTypeIdxKey();
		newKeyServerTypeIdx.setOptionalTypeId( Buff.getOptionalTypeId() );

		CFBamBuffParamByPrevIdxKey existingKeyPrevIdx = (CFBamBuffParamByPrevIdxKey)schema.getFactoryParam().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffParamByPrevIdxKey newKeyPrevIdx = (CFBamBuffParamByPrevIdxKey)schema.getFactoryParam().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffParamByNextIdxKey existingKeyNextIdx = (CFBamBuffParamByNextIdxKey)schema.getFactoryParam().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffParamByNextIdxKey newKeyNextIdx = (CFBamBuffParamByNextIdxKey)schema.getFactoryParam().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffParamByContPrevIdxKey existingKeyContPrevIdx = (CFBamBuffParamByContPrevIdxKey)schema.getFactoryParam().newByContPrevIdxKey();
		existingKeyContPrevIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffParamByContPrevIdxKey newKeyContPrevIdx = (CFBamBuffParamByContPrevIdxKey)schema.getFactoryParam().newByContPrevIdxKey();
		newKeyContPrevIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffParamByContNextIdxKey existingKeyContNextIdx = (CFBamBuffParamByContNextIdxKey)schema.getFactoryParam().newByContNextIdxKey();
		existingKeyContNextIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffParamByContNextIdxKey newKeyContNextIdx = (CFBamBuffParamByContNextIdxKey)schema.getFactoryParam().newByContNextIdxKey();
		newKeyContNextIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyContNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateParam",
					"ParamUNameIdx",
					"ParamUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredServerMethodId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateParam",
						"Container",
						"Container",
						"ServerMethod",
						"ServerMethod",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			if( Buff.getOptionalTypeId() != null ) {
				allNull = false;
			}
			if( allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getOptionalTypeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateParam",
						"Lookup",
						"Lookup",
						"Type",
						"Type",
						"Value",
						"Value",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByServerMethodIdx.get( existingKeyServerMethodIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByServerMethodIdx.containsKey( newKeyServerMethodIdx ) ) {
			subdict = dictByServerMethodIdx.get( newKeyServerMethodIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerMethodIdx.put( newKeyServerMethodIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByServerTypeIdx.get( existingKeyServerTypeIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByServerTypeIdx.containsKey( newKeyServerTypeIdx ) ) {
			subdict = dictByServerTypeIdx.get( newKeyServerTypeIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerTypeIdx.put( newKeyServerTypeIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByContPrevIdx.get( existingKeyContPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContPrevIdx.containsKey( newKeyContPrevIdx ) ) {
			subdict = dictByContPrevIdx.get( newKeyContPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContPrevIdx.put( newKeyContPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByContNextIdx.get( existingKeyContNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContNextIdx.containsKey( newKeyContNextIdx ) ) {
			subdict = dictByContNextIdx.get( newKeyContNextIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContNextIdx.put( newKeyContNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteParam( ICFSecAuthorization Authorization,
		ICFBamParam iBuff )
	{
		final String S_ProcName = "CFBamRamParamTable.deleteParam() ";
		CFBamBuffParam Buff = (CFBamBuffParam)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffParam existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteParam",
				pkey );
		}
		CFLibDbKeyHash256 varServerMethodId = existing.getRequiredServerMethodId();
		CFBamBuffServerMethod container = (CFBamBuffServerMethod)(schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
			varServerMethodId ));
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamBuffParam prev = null;
		if( ( prevId != null ) )
		{
			prev = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				prevId ));
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamBuffParam editPrev;
			classCode = prev.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				editPrev = (CFBamBuffParam)(schema.getFactoryParam().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalLookupNext(nextId);
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffParam next = null;
		if( ( nextId != null ) )
		{
			next = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				nextId ));
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamBuffParam editNext;
			classCode = next.getClassCode();
			if( classCode == ICFBamParam.CLASS_CODE ) {
				editNext = (CFBamBuffParam)(schema.getFactoryParam().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalLookupPrev(prevId);
			if( classCode == ICFBamParam.CLASS_CODE ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffParamByUNameIdxKey keyUNameIdx = (CFBamBuffParamByUNameIdxKey)schema.getFactoryParam().newByUNameIdxKey();
		keyUNameIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffParamByServerMethodIdxKey keyServerMethodIdx = (CFBamBuffParamByServerMethodIdxKey)schema.getFactoryParam().newByServerMethodIdxKey();
		keyServerMethodIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );

		CFBamBuffParamByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffParamByDefSchemaIdxKey)schema.getFactoryParam().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffParamByServerTypeIdxKey keyServerTypeIdx = (CFBamBuffParamByServerTypeIdxKey)schema.getFactoryParam().newByServerTypeIdxKey();
		keyServerTypeIdx.setOptionalTypeId( existing.getOptionalTypeId() );

		CFBamBuffParamByPrevIdxKey keyPrevIdx = (CFBamBuffParamByPrevIdxKey)schema.getFactoryParam().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffParamByNextIdxKey keyNextIdx = (CFBamBuffParamByNextIdxKey)schema.getFactoryParam().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffParamByContPrevIdxKey keyContPrevIdx = (CFBamBuffParamByContPrevIdxKey)schema.getFactoryParam().newByContPrevIdxKey();
		keyContPrevIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffParamByContNextIdxKey keyContNextIdx = (CFBamBuffParamByContNextIdxKey)schema.getFactoryParam().newByContNextIdxKey();
		keyContNextIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffParam > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByServerMethodIdx.get( keyServerMethodIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByServerTypeIdx.get( keyServerTypeIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		subdict = dictByContPrevIdx.get( keyContPrevIdx );
		subdict.remove( pkey );

		subdict = dictByContNextIdx.get( keyContNextIdx );
		subdict.remove( pkey );

	}
	@Override
	public void deleteParamByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffParam cur;
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}

	@Override
	public void deleteParamByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		String argName )
	{
		CFBamBuffParamByUNameIdxKey key = (CFBamBuffParamByUNameIdxKey)schema.getFactoryParam().newByUNameIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setRequiredName( argName );
		deleteParamByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteParamByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamParamByUNameIdxKey argKey )
	{
		CFBamBuffParam cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}

	@Override
	public void deleteParamByServerMethodIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId )
	{
		CFBamBuffParamByServerMethodIdxKey key = (CFBamBuffParamByServerMethodIdxKey)schema.getFactoryParam().newByServerMethodIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		deleteParamByServerMethodIdx( Authorization, key );
	}

	@Override
	public void deleteParamByServerMethodIdx( ICFSecAuthorization Authorization,
		ICFBamParamByServerMethodIdxKey argKey )
	{
		CFBamBuffParam cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}

	@Override
	public void deleteParamByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffParamByDefSchemaIdxKey key = (CFBamBuffParamByDefSchemaIdxKey)schema.getFactoryParam().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteParamByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteParamByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamParamByDefSchemaIdxKey argKey )
	{
		CFBamBuffParam cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}

	@Override
	public void deleteParamByServerTypeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTypeId )
	{
		CFBamBuffParamByServerTypeIdxKey key = (CFBamBuffParamByServerTypeIdxKey)schema.getFactoryParam().newByServerTypeIdxKey();
		key.setOptionalTypeId( argTypeId );
		deleteParamByServerTypeIdx( Authorization, key );
	}

	@Override
	public void deleteParamByServerTypeIdx( ICFSecAuthorization Authorization,
		ICFBamParamByServerTypeIdxKey argKey )
	{
		CFBamBuffParam cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalTypeId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}

	@Override
	public void deleteParamByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffParamByPrevIdxKey key = (CFBamBuffParamByPrevIdxKey)schema.getFactoryParam().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteParamByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteParamByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamParamByPrevIdxKey argKey )
	{
		CFBamBuffParam cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}

	@Override
	public void deleteParamByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffParamByNextIdxKey key = (CFBamBuffParamByNextIdxKey)schema.getFactoryParam().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteParamByNextIdx( Authorization, key );
	}

	@Override
	public void deleteParamByNextIdx( ICFSecAuthorization Authorization,
		ICFBamParamByNextIdxKey argKey )
	{
		CFBamBuffParam cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}

	@Override
	public void deleteParamByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffParamByContPrevIdxKey key = (CFBamBuffParamByContPrevIdxKey)schema.getFactoryParam().newByContPrevIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setOptionalPrevId( argPrevId );
		deleteParamByContPrevIdx( Authorization, key );
	}

	@Override
	public void deleteParamByContPrevIdx( ICFSecAuthorization Authorization,
		ICFBamParamByContPrevIdxKey argKey )
	{
		CFBamBuffParam cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}

	@Override
	public void deleteParamByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffParamByContNextIdxKey key = (CFBamBuffParamByContNextIdxKey)schema.getFactoryParam().newByContNextIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setOptionalNextId( argNextId );
		deleteParamByContNextIdx( Authorization, key );
	}

	@Override
	public void deleteParamByContNextIdx( ICFSecAuthorization Authorization,
		ICFBamParamByContNextIdxKey argKey )
	{
		CFBamBuffParam cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffParam> matchSet = new LinkedList<CFBamBuffParam>();
		Iterator<CFBamBuffParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffParam)(schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteParam( Authorization, cur );
		}
	}
}
