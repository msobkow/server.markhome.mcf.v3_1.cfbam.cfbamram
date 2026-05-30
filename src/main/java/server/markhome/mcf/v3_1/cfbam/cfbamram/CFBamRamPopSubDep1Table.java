
// Description: Java 25 in-memory RAM DbIO implementation for PopSubDep1.

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
 *	CFBamRamPopSubDep1Table in-memory RAM DbIO implementation
 *	for PopSubDep1.
 */
public class CFBamRamPopSubDep1Table
	implements ICFBamPopSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffPopSubDep1 > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffPopSubDep1 >();
	private Map< CFBamBuffPopSubDep1ByPopTopDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopSubDep1 >> dictByPopTopDepIdx
		= new HashMap< CFBamBuffPopSubDep1ByPopTopDepIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffPopSubDep1 >>();
	private Map< CFBamBuffPopSubDep1ByUNameIdxKey,
			CFBamBuffPopSubDep1 > dictByUNameIdx
		= new HashMap< CFBamBuffPopSubDep1ByUNameIdxKey,
			CFBamBuffPopSubDep1 >();

	public CFBamRamPopSubDep1Table( ICFBamSchema argSchema ) {
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
	public ICFBamPopSubDep1 createPopSubDep1( ICFSecAuthorization Authorization,
		ICFBamPopSubDep1 iBuff )
	{
		final String S_ProcName = "createPopSubDep1";
		
		CFBamBuffPopSubDep1 Buff = (CFBamBuffPopSubDep1)(schema.getTablePopDep().createPopDep( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopSubDep1ByPopTopDepIdxKey keyPopTopDepIdx = (CFBamBuffPopSubDep1ByPopTopDepIdxKey)schema.getFactoryPopSubDep1().newByPopTopDepIdxKey();
		keyPopTopDepIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );

		CFBamBuffPopSubDep1ByUNameIdxKey keyUNameIdx = (CFBamBuffPopSubDep1ByUNameIdxKey)schema.getFactoryPopSubDep1().newByUNameIdxKey();
		keyUNameIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"PopSubDep1UNameIdx",
				"PopSubDep1UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"PopDep",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"PopTopDep",
						"PopTopDep",
						"PopTopDep",
						"PopTopDep",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep1 > subdictPopTopDepIdx;
		if( dictByPopTopDepIdx.containsKey( keyPopTopDepIdx ) ) {
			subdictPopTopDepIdx = dictByPopTopDepIdx.get( keyPopTopDepIdx );
		}
		else {
			subdictPopTopDepIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep1 >();
			dictByPopTopDepIdx.put( keyPopTopDepIdx, subdictPopTopDepIdx );
		}
		subdictPopTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamPopSubDep1.CLASS_CODE) {
				CFBamBuffPopSubDep1 retbuff = ((CFBamBuffPopSubDep1)(schema.getFactoryPopSubDep1().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamPopSubDep1 readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerived";
		ICFBamPopSubDep1 buff;
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
	public ICFBamPopSubDep1 lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.lockDerived";
		ICFBamPopSubDep1 buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep1[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamPopSubDep1.readAllDerived";
		ICFBamPopSubDep1[] retList = new ICFBamPopSubDep1[ dictByPKey.values().size() ];
		Iterator< CFBamBuffPopSubDep1 > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamPopSubDep1[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamPopSubDep1> filteredList = new ArrayList<ICFBamPopSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep1 ) ) {
					filteredList.add( (ICFBamPopSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep1[] readDerivedByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByRelationIdx";
		ICFBamPopDep buffList[] = schema.getTablePopDep().readDerivedByRelationIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamPopDep buff;
			ArrayList<ICFBamPopSubDep1> filteredList = new ArrayList<ICFBamPopSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep1 ) ) {
					filteredList.add( (ICFBamPopSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep1[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readDerivedByDefSchemaIdx";
		ICFBamPopDep buffList[] = schema.getTablePopDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamPopDep buff;
			ArrayList<ICFBamPopSubDep1> filteredList = new ArrayList<ICFBamPopSubDep1>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamPopSubDep1 ) ) {
					filteredList.add( (ICFBamPopSubDep1)buff );
				}
			}
			return( filteredList.toArray( new ICFBamPopSubDep1[0] ) );
		}
	}

	@Override
	public ICFBamPopSubDep1[] readDerivedByPopTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerivedByPopTopDepIdx";
		CFBamBuffPopSubDep1ByPopTopDepIdxKey key = (CFBamBuffPopSubDep1ByPopTopDepIdxKey)schema.getFactoryPopSubDep1().newByPopTopDepIdxKey();

		key.setRequiredPopTopDepId( PopTopDepId );
		ICFBamPopSubDep1[] recArray;
		if( dictByPopTopDepIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffPopSubDep1 > subdictPopTopDepIdx
				= dictByPopTopDepIdx.get( key );
			recArray = new ICFBamPopSubDep1[ subdictPopTopDepIdx.size() ];
			Iterator< CFBamBuffPopSubDep1 > iter = subdictPopTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffPopSubDep1 > subdictPopTopDepIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep1 >();
			dictByPopTopDepIdx.put( key, subdictPopTopDepIdx );
			recArray = new ICFBamPopSubDep1[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamPopSubDep1 readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readDerivedByUNameIdx";
		CFBamBuffPopSubDep1ByUNameIdxKey key = (CFBamBuffPopSubDep1ByUNameIdxKey)schema.getFactoryPopSubDep1().newByUNameIdxKey();

		key.setRequiredPopTopDepId( PopTopDepId );
		key.setRequiredName( Name );
		ICFBamPopSubDep1 buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep1 readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamPopSubDep1 buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep1 readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readRec";
		ICFBamPopSubDep1 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopSubDep1.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep1 lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamPopSubDep1 buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamPopSubDep1.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamPopSubDep1[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readAllRec";
		ICFBamPopSubDep1 buff;
		ArrayList<ICFBamPopSubDep1> filteredList = new ArrayList<ICFBamPopSubDep1>();
		ICFBamPopSubDep1[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep1.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep1[0] ) );
	}

	@Override
	public ICFBamPopSubDep1 readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamPopSubDep1 buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamPopSubDep1)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamPopSubDep1[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamPopSubDep1 buff;
		ArrayList<ICFBamPopSubDep1> filteredList = new ArrayList<ICFBamPopSubDep1>();
		ICFBamPopSubDep1[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep1[0] ) );
	}

	@Override
	public ICFBamPopSubDep1[] readRecByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByRelationIdx() ";
		ICFBamPopSubDep1 buff;
		ArrayList<ICFBamPopSubDep1> filteredList = new ArrayList<ICFBamPopSubDep1>();
		ICFBamPopSubDep1[] buffList = readDerivedByRelationIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep1[0] ) );
	}

	@Override
	public ICFBamPopSubDep1[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamPopDep.readRecByDefSchemaIdx() ";
		ICFBamPopSubDep1 buff;
		ArrayList<ICFBamPopSubDep1> filteredList = new ArrayList<ICFBamPopSubDep1>();
		ICFBamPopSubDep1[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep1[0] ) );
	}

	@Override
	public ICFBamPopSubDep1[] readRecByPopTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readRecByPopTopDepIdx() ";
		ICFBamPopSubDep1 buff;
		ArrayList<ICFBamPopSubDep1> filteredList = new ArrayList<ICFBamPopSubDep1>();
		ICFBamPopSubDep1[] buffList = readDerivedByPopTopDepIdx( Authorization,
			PopTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep1.CLASS_CODE ) ) {
				filteredList.add( (ICFBamPopSubDep1)buff );
			}
		}
		return( filteredList.toArray( new ICFBamPopSubDep1[0] ) );
	}

	@Override
	public ICFBamPopSubDep1 readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PopTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamPopSubDep1.readRecByUNameIdx() ";
		ICFBamPopSubDep1 buff = readDerivedByUNameIdx( Authorization,
			PopTopDepId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamPopSubDep1.CLASS_CODE ) ) {
			return( (ICFBamPopSubDep1)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamPopSubDep1 updatePopSubDep1( ICFSecAuthorization Authorization,
		ICFBamPopSubDep1 iBuff )
	{
		CFBamBuffPopSubDep1 Buff = (CFBamBuffPopSubDep1)(schema.getTablePopDep().updatePopDep( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffPopSubDep1 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updatePopSubDep1",
				"Existing record not found",
				"Existing record not found",
				"PopSubDep1",
				"PopSubDep1",
				pkey );
		}
		CFBamBuffPopSubDep1ByPopTopDepIdxKey existingKeyPopTopDepIdx = (CFBamBuffPopSubDep1ByPopTopDepIdxKey)schema.getFactoryPopSubDep1().newByPopTopDepIdxKey();
		existingKeyPopTopDepIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );

		CFBamBuffPopSubDep1ByPopTopDepIdxKey newKeyPopTopDepIdx = (CFBamBuffPopSubDep1ByPopTopDepIdxKey)schema.getFactoryPopSubDep1().newByPopTopDepIdxKey();
		newKeyPopTopDepIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );

		CFBamBuffPopSubDep1ByUNameIdxKey existingKeyUNameIdx = (CFBamBuffPopSubDep1ByUNameIdxKey)schema.getFactoryPopSubDep1().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffPopSubDep1ByUNameIdxKey newKeyUNameIdx = (CFBamBuffPopSubDep1ByUNameIdxKey)schema.getFactoryPopSubDep1().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredPopTopDepId( Buff.getRequiredPopTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updatePopSubDep1",
					"PopSubDep1UNameIdx",
					"PopSubDep1UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep1",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"PopDep",
						"PopDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTablePopTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredPopTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updatePopSubDep1",
						"Container",
						"Container",
						"PopTopDep",
						"PopTopDep",
						"PopTopDep",
						"PopTopDep",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep1 > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByPopTopDepIdx.get( existingKeyPopTopDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPopTopDepIdx.containsKey( newKeyPopTopDepIdx ) ) {
			subdict = dictByPopTopDepIdx.get( newKeyPopTopDepIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffPopSubDep1 >();
			dictByPopTopDepIdx.put( newKeyPopTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		return(Buff);
	}

	@Override
	public void deletePopSubDep1( ICFSecAuthorization Authorization,
		ICFBamPopSubDep1 iBuff )
	{
		final String S_ProcName = "CFBamRamPopSubDep1Table.deletePopSubDep1() ";
		CFBamBuffPopSubDep1 Buff = (CFBamBuffPopSubDep1)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffPopSubDep1 existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deletePopSubDep1",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckPopDep[] = schema.getTablePopSubDep2().readDerivedByPopSubDep1Idx( Authorization,
						existing.getRequiredId() );
		if( arrCheckPopDep.length > 0 ) {
			schema.getTablePopSubDep2().deletePopSubDep2ByPopSubDep1Idx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffPopSubDep1ByPopTopDepIdxKey keyPopTopDepIdx = (CFBamBuffPopSubDep1ByPopTopDepIdxKey)schema.getFactoryPopSubDep1().newByPopTopDepIdxKey();
		keyPopTopDepIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );

		CFBamBuffPopSubDep1ByUNameIdxKey keyUNameIdx = (CFBamBuffPopSubDep1ByUNameIdxKey)schema.getFactoryPopSubDep1().newByUNameIdxKey();
		keyUNameIdx.setRequiredPopTopDepId( existing.getRequiredPopTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffPopSubDep1 > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByPopTopDepIdx.get( keyPopTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTablePopDep().deletePopDep( Authorization,
			Buff );
	}
	@Override
	public void deletePopSubDep1ByPopTopDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopTopDepId )
	{
		CFBamBuffPopSubDep1ByPopTopDepIdxKey key = (CFBamBuffPopSubDep1ByPopTopDepIdxKey)schema.getFactoryPopSubDep1().newByPopTopDepIdxKey();
		key.setRequiredPopTopDepId( argPopTopDepId );
		deletePopSubDep1ByPopTopDepIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep1ByPopTopDepIdx( ICFSecAuthorization Authorization,
		ICFBamPopSubDep1ByPopTopDepIdxKey argKey )
	{
		CFBamBuffPopSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep1> matchSet = new LinkedList<CFBamBuffPopSubDep1>();
		Iterator<CFBamBuffPopSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep1)(schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep1ByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPopTopDepId,
		String argName )
	{
		CFBamBuffPopSubDep1ByUNameIdxKey key = (CFBamBuffPopSubDep1ByUNameIdxKey)schema.getFactoryPopSubDep1().newByUNameIdxKey();
		key.setRequiredPopTopDepId( argPopTopDepId );
		key.setRequiredName( argName );
		deletePopSubDep1ByUNameIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep1ByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamPopSubDep1ByUNameIdxKey argKey )
	{
		CFBamBuffPopSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep1> matchSet = new LinkedList<CFBamBuffPopSubDep1>();
		Iterator<CFBamBuffPopSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep1)(schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep1ByRelationIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffPopDepByRelationIdxKey key = (CFBamBuffPopDepByRelationIdxKey)schema.getFactoryPopDep().newByRelationIdxKey();
		key.setRequiredRelationId( argRelationId );
		deletePopSubDep1ByRelationIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep1ByRelationIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByRelationIdxKey argKey )
	{
		CFBamBuffPopSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep1> matchSet = new LinkedList<CFBamBuffPopSubDep1>();
		Iterator<CFBamBuffPopSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep1)(schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep1ByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffPopDepByDefSchemaIdxKey key = (CFBamBuffPopDepByDefSchemaIdxKey)schema.getFactoryPopDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deletePopSubDep1ByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep1ByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamPopDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffPopSubDep1 cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep1> matchSet = new LinkedList<CFBamBuffPopSubDep1>();
		Iterator<CFBamBuffPopSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep1)(schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep1ByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffPopSubDep1 cur;
		LinkedList<CFBamBuffPopSubDep1> matchSet = new LinkedList<CFBamBuffPopSubDep1>();
		Iterator<CFBamBuffPopSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep1)(schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep1( Authorization, cur );
		}
	}

	@Override
	public void deletePopSubDep1ByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deletePopSubDep1ByTenantIdx( Authorization, key );
	}

	@Override
	public void deletePopSubDep1ByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffPopSubDep1 cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffPopSubDep1> matchSet = new LinkedList<CFBamBuffPopSubDep1>();
		Iterator<CFBamBuffPopSubDep1> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffPopSubDep1> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffPopSubDep1)(schema.getTablePopSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deletePopSubDep1( Authorization, cur );
		}
	}
}
