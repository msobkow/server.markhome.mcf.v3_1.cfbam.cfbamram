
// Description: Java 25 in-memory RAM DbIO implementation for SchemaRole.

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
 *	CFBamRamSchemaRoleTable in-memory RAM DbIO implementation
 *	for SchemaRole.
 */
public class CFBamRamSchemaRoleTable
	implements ICFBamSchemaRoleTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffSchemaRole > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffSchemaRole >();
	private Map< CFBamBuffSchemaRoleBySchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRole >> dictBySchemaIdx
		= new HashMap< CFBamBuffSchemaRoleBySchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRole >>();
	private Map< CFBamBuffSchemaRoleByRoleScopeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRole >> dictByRoleScopeIdx
		= new HashMap< CFBamBuffSchemaRoleByRoleScopeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRole >>();
	private Map< CFBamBuffSchemaRoleBySchRoleScpIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRole >> dictBySchRoleScpIdx
		= new HashMap< CFBamBuffSchemaRoleBySchRoleScpIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaRole >>();

	public CFBamRamSchemaRoleTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffRoleDef ensureRec(ICFBamRoleDef rec) {
		if (rec == null) {
			return( null );
		}
		else {
			return ((CFBamRamRoleDefTable)(schema.getTableRoleDef())).ensureRec((ICFBamRoleDef)rec);
		}
	}

	@Override
	public ICFBamSchemaRole createSchemaRole( ICFSecAuthorization Authorization,
		ICFBamSchemaRole iBuff )
	{
		final String S_ProcName = "createSchemaRole";
		
		CFBamBuffSchemaRole Buff = (CFBamBuffSchemaRole)(schema.getTableRoleDef().createRoleDef( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffSchemaRoleBySchemaIdxKey keySchemaIdx = (CFBamBuffSchemaRoleBySchemaIdxKey)schema.getFactorySchemaRole().newBySchemaIdxKey();
		keySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		CFBamBuffSchemaRoleByRoleScopeIdxKey keyRoleScopeIdx = (CFBamBuffSchemaRoleByRoleScopeIdxKey)schema.getFactorySchemaRole().newByRoleScopeIdxKey();
		keyRoleScopeIdx.setRequiredRoleScope( Buff.getRequiredRoleScope() );

		CFBamBuffSchemaRoleBySchRoleScpIdxKey keySchRoleScpIdx = (CFBamBuffSchemaRoleBySchRoleScpIdxKey)schema.getFactorySchemaRole().newBySchRoleScpIdxKey();
		keySchRoleScpIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		keySchRoleScpIdx.setRequiredRoleScope( Buff.getRequiredRoleScope() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"RoleDef",
						"RoleDef",
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

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictSchemaIdx;
		if( dictBySchemaIdx.containsKey( keySchemaIdx ) ) {
			subdictSchemaIdx = dictBySchemaIdx.get( keySchemaIdx );
		}
		else {
			subdictSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictBySchemaIdx.put( keySchemaIdx, subdictSchemaIdx );
		}
		subdictSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictRoleScopeIdx;
		if( dictByRoleScopeIdx.containsKey( keyRoleScopeIdx ) ) {
			subdictRoleScopeIdx = dictByRoleScopeIdx.get( keyRoleScopeIdx );
		}
		else {
			subdictRoleScopeIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictByRoleScopeIdx.put( keyRoleScopeIdx, subdictRoleScopeIdx );
		}
		subdictRoleScopeIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictSchRoleScpIdx;
		if( dictBySchRoleScpIdx.containsKey( keySchRoleScpIdx ) ) {
			subdictSchRoleScpIdx = dictBySchRoleScpIdx.get( keySchRoleScpIdx );
		}
		else {
			subdictSchRoleScpIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictBySchRoleScpIdx.put( keySchRoleScpIdx, subdictSchRoleScpIdx );
		}
		subdictSchRoleScpIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamSchemaRole.CLASS_CODE) {
				CFBamBuffSchemaRole retbuff = ((CFBamBuffSchemaRole)(schema.getFactorySchemaRole().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamSchemaRole readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readDerived";
		ICFBamSchemaRole buff;
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
	public ICFBamSchemaRole lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRole.lockDerived";
		ICFBamSchemaRole buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRole[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaRole.readAllDerived";
		ICFBamSchemaRole[] retList = new ICFBamSchemaRole[ dictByPKey.values().size() ];
		Iterator< CFBamBuffSchemaRole > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamSchemaRole readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByUNameIdx";
		ICFBamRoleDef buff = schema.getTableRoleDef().readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamSchemaRole ) {
			return( (ICFBamSchemaRole)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaRole[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByScopeIdx";
		ICFBamRoleDef buffList[] = schema.getTableRoleDef().readDerivedByScopeIdx( Authorization,
			ScopeId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamRoleDef buff;
			ArrayList<ICFBamSchemaRole> filteredList = new ArrayList<ICFBamSchemaRole>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamSchemaRole ) ) {
					filteredList.add( (ICFBamSchemaRole)buff );
				}
			}
			return( filteredList.toArray( new ICFBamSchemaRole[0] ) );
		}
	}

	@Override
	public ICFBamSchemaRole[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByDefSchemaIdx";
		ICFBamRoleDef buffList[] = schema.getTableRoleDef().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamRoleDef buff;
			ArrayList<ICFBamSchemaRole> filteredList = new ArrayList<ICFBamSchemaRole>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamSchemaRole ) ) {
					filteredList.add( (ICFBamSchemaRole)buff );
				}
			}
			return( filteredList.toArray( new ICFBamSchemaRole[0] ) );
		}
	}

	@Override
	public ICFBamSchemaRole readDerivedByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByUDefIdx";
		ICFBamRoleDef buff = schema.getTableRoleDef().readDerivedByUDefIdx( Authorization,
			ScopeId,
			DefSchemaId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamSchemaRole ) {
			return( (ICFBamSchemaRole)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaRole[] readDerivedBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readDerivedBySchemaIdx";
		CFBamBuffSchemaRoleBySchemaIdxKey key = (CFBamBuffSchemaRoleBySchemaIdxKey)schema.getFactorySchemaRole().newBySchemaIdxKey();

		key.setRequiredSchemaDefId( SchemaDefId );
		ICFBamSchemaRole[] recArray;
		if( dictBySchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictSchemaIdx
				= dictBySchemaIdx.get( key );
			recArray = new ICFBamSchemaRole[ subdictSchemaIdx.size() ];
			Iterator< CFBamBuffSchemaRole > iter = subdictSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictBySchemaIdx.put( key, subdictSchemaIdx );
			recArray = new ICFBamSchemaRole[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaRole[] readDerivedByRoleScopeIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.RoleScopeEnum RoleScope )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readDerivedByRoleScopeIdx";
		CFBamBuffSchemaRoleByRoleScopeIdxKey key = (CFBamBuffSchemaRoleByRoleScopeIdxKey)schema.getFactorySchemaRole().newByRoleScopeIdxKey();

		key.setRequiredRoleScope( RoleScope );
		ICFBamSchemaRole[] recArray;
		if( dictByRoleScopeIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictRoleScopeIdx
				= dictByRoleScopeIdx.get( key );
			recArray = new ICFBamSchemaRole[ subdictRoleScopeIdx.size() ];
			Iterator< CFBamBuffSchemaRole > iter = subdictRoleScopeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictRoleScopeIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictByRoleScopeIdx.put( key, subdictRoleScopeIdx );
			recArray = new ICFBamSchemaRole[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaRole[] readDerivedBySchRoleScpIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		ICFBamSchema.RoleScopeEnum RoleScope )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readDerivedBySchRoleScpIdx";
		CFBamBuffSchemaRoleBySchRoleScpIdxKey key = (CFBamBuffSchemaRoleBySchRoleScpIdxKey)schema.getFactorySchemaRole().newBySchRoleScpIdxKey();

		key.setRequiredSchemaDefId( SchemaDefId );
		key.setRequiredRoleScope( RoleScope );
		ICFBamSchemaRole[] recArray;
		if( dictBySchRoleScpIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictSchRoleScpIdx
				= dictBySchRoleScpIdx.get( key );
			recArray = new ICFBamSchemaRole[ subdictSchRoleScpIdx.size() ];
			Iterator< CFBamBuffSchemaRole > iter = subdictSchRoleScpIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdictSchRoleScpIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictBySchRoleScpIdx.put( key, subdictSchRoleScpIdx );
			recArray = new ICFBamSchemaRole[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaRole readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRoleDef.readDerivedByIdIdx() ";
		ICFBamSchemaRole buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRole readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readRec";
		ICFBamSchemaRole buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamSchemaRole.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRole lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamSchemaRole buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamSchemaRole.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaRole[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readAllRec";
		ICFBamSchemaRole buff;
		ArrayList<ICFBamSchemaRole> filteredList = new ArrayList<ICFBamSchemaRole>();
		ICFBamSchemaRole[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRole.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRole[0] ) );
	}

	@Override
	public ICFBamSchemaRole readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByIdIdx() ";
		ICFBamSchemaRole buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
			return( (ICFBamSchemaRole)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaRole readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByUNameIdx() ";
		ICFBamSchemaRole buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
			return( (ICFBamSchemaRole)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaRole[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByScopeIdx() ";
		ICFBamSchemaRole buff;
		ArrayList<ICFBamSchemaRole> filteredList = new ArrayList<ICFBamSchemaRole>();
		ICFBamSchemaRole[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRole)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRole[0] ) );
	}

	@Override
	public ICFBamSchemaRole[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByDefSchemaIdx() ";
		ICFBamSchemaRole buff;
		ArrayList<ICFBamSchemaRole> filteredList = new ArrayList<ICFBamSchemaRole>();
		ICFBamSchemaRole[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRole)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRole[0] ) );
	}

	@Override
	public ICFBamSchemaRole readRecByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 DefSchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRoleDef.readRecByUDefIdx() ";
		ICFBamSchemaRole buff = readDerivedByUDefIdx( Authorization,
			ScopeId,
			DefSchemaId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamRoleDef.CLASS_CODE ) ) {
			return( (ICFBamSchemaRole)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaRole[] readRecBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readRecBySchemaIdx() ";
		ICFBamSchemaRole buff;
		ArrayList<ICFBamSchemaRole> filteredList = new ArrayList<ICFBamSchemaRole>();
		ICFBamSchemaRole[] buffList = readDerivedBySchemaIdx( Authorization,
			SchemaDefId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRole.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRole)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRole[0] ) );
	}

	@Override
	public ICFBamSchemaRole[] readRecByRoleScopeIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.RoleScopeEnum RoleScope )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readRecByRoleScopeIdx() ";
		ICFBamSchemaRole buff;
		ArrayList<ICFBamSchemaRole> filteredList = new ArrayList<ICFBamSchemaRole>();
		ICFBamSchemaRole[] buffList = readDerivedByRoleScopeIdx( Authorization,
			RoleScope );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRole.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRole)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRole[0] ) );
	}

	@Override
	public ICFBamSchemaRole[] readRecBySchRoleScpIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		ICFBamSchema.RoleScopeEnum RoleScope )
	{
		final String S_ProcName = "CFBamRamSchemaRole.readRecBySchRoleScpIdx() ";
		ICFBamSchemaRole buff;
		ArrayList<ICFBamSchemaRole> filteredList = new ArrayList<ICFBamSchemaRole>();
		ICFBamSchemaRole[] buffList = readDerivedBySchRoleScpIdx( Authorization,
			SchemaDefId,
			RoleScope );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaRole.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaRole)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaRole[0] ) );
	}

	public ICFBamSchemaRole updateSchemaRole( ICFSecAuthorization Authorization,
		ICFBamSchemaRole iBuff )
	{
		CFBamBuffSchemaRole Buff = (CFBamBuffSchemaRole)(schema.getTableRoleDef().updateRoleDef( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		CFBamBuffSchemaRole existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaRole",
				"Existing record not found",
				"Existing record not found",
				"SchemaRole",
				"SchemaRole",
				pkey );
		}
		CFBamBuffSchemaRoleBySchemaIdxKey existingKeySchemaIdx = (CFBamBuffSchemaRoleBySchemaIdxKey)schema.getFactorySchemaRole().newBySchemaIdxKey();
		existingKeySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamBuffSchemaRoleBySchemaIdxKey newKeySchemaIdx = (CFBamBuffSchemaRoleBySchemaIdxKey)schema.getFactorySchemaRole().newBySchemaIdxKey();
		newKeySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		CFBamBuffSchemaRoleByRoleScopeIdxKey existingKeyRoleScopeIdx = (CFBamBuffSchemaRoleByRoleScopeIdxKey)schema.getFactorySchemaRole().newByRoleScopeIdxKey();
		existingKeyRoleScopeIdx.setRequiredRoleScope( existing.getRequiredRoleScope() );

		CFBamBuffSchemaRoleByRoleScopeIdxKey newKeyRoleScopeIdx = (CFBamBuffSchemaRoleByRoleScopeIdxKey)schema.getFactorySchemaRole().newByRoleScopeIdxKey();
		newKeyRoleScopeIdx.setRequiredRoleScope( Buff.getRequiredRoleScope() );

		CFBamBuffSchemaRoleBySchRoleScpIdxKey existingKeySchRoleScpIdx = (CFBamBuffSchemaRoleBySchRoleScpIdxKey)schema.getFactorySchemaRole().newBySchRoleScpIdxKey();
		existingKeySchRoleScpIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		existingKeySchRoleScpIdx.setRequiredRoleScope( existing.getRequiredRoleScope() );

		CFBamBuffSchemaRoleBySchRoleScpIdxKey newKeySchRoleScpIdx = (CFBamBuffSchemaRoleBySchRoleScpIdxKey)schema.getFactorySchemaRole().newBySchRoleScpIdxKey();
		newKeySchRoleScpIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		newKeySchRoleScpIdx.setRequiredRoleScope( Buff.getRequiredRoleScope() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableRoleDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaRole",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"RoleDef",
						"RoleDef",
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
						"updateSchemaRole",
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

		Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictBySchemaIdx.put( newKeySchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByRoleScopeIdx.get( existingKeyRoleScopeIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRoleScopeIdx.containsKey( newKeyRoleScopeIdx ) ) {
			subdict = dictByRoleScopeIdx.get( newKeyRoleScopeIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictByRoleScopeIdx.put( newKeyRoleScopeIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictBySchRoleScpIdx.get( existingKeySchRoleScpIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchRoleScpIdx.containsKey( newKeySchRoleScpIdx ) ) {
			subdict = dictBySchRoleScpIdx.get( newKeySchRoleScpIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaRole >();
			dictBySchRoleScpIdx.put( newKeySchRoleScpIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteSchemaRole( ICFSecAuthorization Authorization,
		ICFBamSchemaRole iBuff )
	{
		final String S_ProcName = "CFBamRamSchemaRoleTable.deleteSchemaRole() ";
		CFBamBuffSchemaRole Buff = (CFBamBuffSchemaRole)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffSchemaRole existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaRole",
				pkey );
		}
		CFBamBuffSchemaRoleBySchemaIdxKey keySchemaIdx = (CFBamBuffSchemaRoleBySchemaIdxKey)schema.getFactorySchemaRole().newBySchemaIdxKey();
		keySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamBuffSchemaRoleByRoleScopeIdxKey keyRoleScopeIdx = (CFBamBuffSchemaRoleByRoleScopeIdxKey)schema.getFactorySchemaRole().newByRoleScopeIdxKey();
		keyRoleScopeIdx.setRequiredRoleScope( existing.getRequiredRoleScope() );

		CFBamBuffSchemaRoleBySchRoleScpIdxKey keySchRoleScpIdx = (CFBamBuffSchemaRoleBySchRoleScpIdxKey)schema.getFactorySchemaRole().newBySchRoleScpIdxKey();
		keySchRoleScpIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		keySchRoleScpIdx.setRequiredRoleScope( existing.getRequiredRoleScope() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffSchemaRole > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaIdx.get( keySchemaIdx );
		subdict.remove( pkey );

		subdict = dictByRoleScopeIdx.get( keyRoleScopeIdx );
		subdict.remove( pkey );

		subdict = dictBySchRoleScpIdx.get( keySchRoleScpIdx );
		subdict.remove( pkey );

		schema.getTableRoleDef().deleteRoleDef( Authorization,
			Buff );
	}
	@Override
	public void deleteSchemaRoleBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId )
	{
		CFBamBuffSchemaRoleBySchemaIdxKey key = (CFBamBuffSchemaRoleBySchemaIdxKey)schema.getFactorySchemaRole().newBySchemaIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		deleteSchemaRoleBySchemaIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRoleBySchemaIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaRoleBySchemaIdxKey argKey )
	{
		CFBamBuffSchemaRole cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRole> matchSet = new LinkedList<CFBamBuffSchemaRole>();
		Iterator<CFBamBuffSchemaRole> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRole> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRole)(schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRole( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRoleByRoleScopeIdx( ICFSecAuthorization Authorization,
		ICFBamSchema.RoleScopeEnum argRoleScope )
	{
		CFBamBuffSchemaRoleByRoleScopeIdxKey key = (CFBamBuffSchemaRoleByRoleScopeIdxKey)schema.getFactorySchemaRole().newByRoleScopeIdxKey();
		key.setRequiredRoleScope( argRoleScope );
		deleteSchemaRoleByRoleScopeIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRoleByRoleScopeIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaRoleByRoleScopeIdxKey argKey )
	{
		CFBamBuffSchemaRole cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRole> matchSet = new LinkedList<CFBamBuffSchemaRole>();
		Iterator<CFBamBuffSchemaRole> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRole> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRole)(schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRole( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRoleBySchRoleScpIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId,
		ICFBamSchema.RoleScopeEnum argRoleScope )
	{
		CFBamBuffSchemaRoleBySchRoleScpIdxKey key = (CFBamBuffSchemaRoleBySchRoleScpIdxKey)schema.getFactorySchemaRole().newBySchRoleScpIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		key.setRequiredRoleScope( argRoleScope );
		deleteSchemaRoleBySchRoleScpIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRoleBySchRoleScpIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaRoleBySchRoleScpIdxKey argKey )
	{
		CFBamBuffSchemaRole cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRole> matchSet = new LinkedList<CFBamBuffSchemaRole>();
		Iterator<CFBamBuffSchemaRole> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRole> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRole)(schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRole( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRoleByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffSchemaRole cur;
		LinkedList<CFBamBuffSchemaRole> matchSet = new LinkedList<CFBamBuffSchemaRole>();
		Iterator<CFBamBuffSchemaRole> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRole> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRole)(schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRole( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRoleByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffRoleDefByUNameIdxKey key = (CFBamBuffRoleDefByUNameIdxKey)schema.getFactoryRoleDef().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteSchemaRoleByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRoleByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamRoleDefByUNameIdxKey argKey )
	{
		CFBamBuffSchemaRole cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRole> matchSet = new LinkedList<CFBamBuffSchemaRole>();
		Iterator<CFBamBuffSchemaRole> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRole> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRole)(schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRole( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRoleByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffRoleDefByScopeIdxKey key = (CFBamBuffRoleDefByScopeIdxKey)schema.getFactoryRoleDef().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteSchemaRoleByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRoleByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamRoleDefByScopeIdxKey argKey )
	{
		CFBamBuffSchemaRole cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRole> matchSet = new LinkedList<CFBamBuffSchemaRole>();
		Iterator<CFBamBuffSchemaRole> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRole> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRole)(schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRole( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRoleByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffRoleDefByDefSchemaIdxKey key = (CFBamBuffRoleDefByDefSchemaIdxKey)schema.getFactoryRoleDef().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteSchemaRoleByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRoleByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamRoleDefByDefSchemaIdxKey argKey )
	{
		CFBamBuffSchemaRole cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRole> matchSet = new LinkedList<CFBamBuffSchemaRole>();
		Iterator<CFBamBuffSchemaRole> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRole> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRole)(schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRole( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaRoleByUDefIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argDefSchemaId,
		String argName )
	{
		CFBamBuffRoleDefByUDefIdxKey key = (CFBamBuffRoleDefByUDefIdxKey)schema.getFactoryRoleDef().newByUDefIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		key.setRequiredName( argName );
		deleteSchemaRoleByUDefIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaRoleByUDefIdx( ICFSecAuthorization Authorization,
		ICFBamRoleDefByUDefIdxKey argKey )
	{
		CFBamBuffSchemaRole cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaRole> matchSet = new LinkedList<CFBamBuffSchemaRole>();
		Iterator<CFBamBuffSchemaRole> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaRole> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaRole)(schema.getTableSchemaRole().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaRole( Authorization, cur );
		}
	}
}
