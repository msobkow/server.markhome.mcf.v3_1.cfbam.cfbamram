
// Description: Java 25 in-memory RAM DbIO implementation for Scope.

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
 *	CFBamRamScopeTable in-memory RAM DbIO implementation
 *	for Scope.
 */
public class CFBamRamScopeTable
	implements ICFBamScopeTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffScope > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffScope >();
	private Map< CFBamBuffScopeByTenantIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffScope >> dictByTenantIdx
		= new HashMap< CFBamBuffScopeByTenantIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffScope >>();

	public CFBamRamScopeTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffScope ensureRec(ICFBamScope rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			switch (classCode) {
				case ICFBamScope.CLASS_CODE:
					return(((CFBamBuffScopeFactoryService)(schema.getCFBamFactory().getFactoryScope())).ensureRec((ICFBamScope)rec) );
				case ICFBamSchemaDef.CLASS_CODE:
					return(((CFBamBuffSchemaDefFactoryService)(schema.getCFBamFactory().getFactorySchemaDef())).ensureRec((ICFBamSchemaDef)rec) );
				case ICFBamSchemaRef.CLASS_CODE:
					return(((CFBamBuffSchemaRefFactoryService)(schema.getCFBamFactory().getFactorySchemaRef())).ensureRec((ICFBamSchemaRef)rec) );
				case ICFBamServerMethod.CLASS_CODE:
					return(((CFBamBuffServerMethodFactoryService)(schema.getCFBamFactory().getFactoryServerMethod())).ensureRec((ICFBamServerMethod)rec) );
				case ICFBamServerObjFunc.CLASS_CODE:
					return(((CFBamBuffServerObjFuncFactoryService)(schema.getCFBamFactory().getFactoryServerObjFunc())).ensureRec((ICFBamServerObjFunc)rec) );
				case ICFBamServerProc.CLASS_CODE:
					return(((CFBamBuffServerProcFactoryService)(schema.getCFBamFactory().getFactoryServerProc())).ensureRec((ICFBamServerProc)rec) );
				case ICFBamServerListFunc.CLASS_CODE:
					return(((CFBamBuffServerListFuncFactoryService)(schema.getCFBamFactory().getFactoryServerListFunc())).ensureRec((ICFBamServerListFunc)rec) );
				case ICFBamTable.CLASS_CODE:
					return(((CFBamBuffTableFactoryService)(schema.getCFBamFactory().getFactoryTable())).ensureRec((ICFBamTable)rec) );
				case ICFBamClearDep.CLASS_CODE:
					return(((CFBamBuffClearDepFactoryService)(schema.getCFBamFactory().getFactoryClearDep())).ensureRec((ICFBamClearDep)rec) );
				case ICFBamClearSubDep1.CLASS_CODE:
					return(((CFBamBuffClearSubDep1FactoryService)(schema.getCFBamFactory().getFactoryClearSubDep1())).ensureRec((ICFBamClearSubDep1)rec) );
				case ICFBamClearSubDep2.CLASS_CODE:
					return(((CFBamBuffClearSubDep2FactoryService)(schema.getCFBamFactory().getFactoryClearSubDep2())).ensureRec((ICFBamClearSubDep2)rec) );
				case ICFBamClearSubDep3.CLASS_CODE:
					return(((CFBamBuffClearSubDep3FactoryService)(schema.getCFBamFactory().getFactoryClearSubDep3())).ensureRec((ICFBamClearSubDep3)rec) );
				case ICFBamClearTopDep.CLASS_CODE:
					return(((CFBamBuffClearTopDepFactoryService)(schema.getCFBamFactory().getFactoryClearTopDep())).ensureRec((ICFBamClearTopDep)rec) );
				case ICFBamDelDep.CLASS_CODE:
					return(((CFBamBuffDelDepFactoryService)(schema.getCFBamFactory().getFactoryDelDep())).ensureRec((ICFBamDelDep)rec) );
				case ICFBamDelSubDep1.CLASS_CODE:
					return(((CFBamBuffDelSubDep1FactoryService)(schema.getCFBamFactory().getFactoryDelSubDep1())).ensureRec((ICFBamDelSubDep1)rec) );
				case ICFBamDelSubDep2.CLASS_CODE:
					return(((CFBamBuffDelSubDep2FactoryService)(schema.getCFBamFactory().getFactoryDelSubDep2())).ensureRec((ICFBamDelSubDep2)rec) );
				case ICFBamDelSubDep3.CLASS_CODE:
					return(((CFBamBuffDelSubDep3FactoryService)(schema.getCFBamFactory().getFactoryDelSubDep3())).ensureRec((ICFBamDelSubDep3)rec) );
				case ICFBamDelTopDep.CLASS_CODE:
					return(((CFBamBuffDelTopDepFactoryService)(schema.getCFBamFactory().getFactoryDelTopDep())).ensureRec((ICFBamDelTopDep)rec) );
				case ICFBamIndex.CLASS_CODE:
					return(((CFBamBuffIndexFactoryService)(schema.getCFBamFactory().getFactoryIndex())).ensureRec((ICFBamIndex)rec) );
				case ICFBamPopDep.CLASS_CODE:
					return(((CFBamBuffPopDepFactoryService)(schema.getCFBamFactory().getFactoryPopDep())).ensureRec((ICFBamPopDep)rec) );
				case ICFBamPopSubDep1.CLASS_CODE:
					return(((CFBamBuffPopSubDep1FactoryService)(schema.getCFBamFactory().getFactoryPopSubDep1())).ensureRec((ICFBamPopSubDep1)rec) );
				case ICFBamPopSubDep2.CLASS_CODE:
					return(((CFBamBuffPopSubDep2FactoryService)(schema.getCFBamFactory().getFactoryPopSubDep2())).ensureRec((ICFBamPopSubDep2)rec) );
				case ICFBamPopSubDep3.CLASS_CODE:
					return(((CFBamBuffPopSubDep3FactoryService)(schema.getCFBamFactory().getFactoryPopSubDep3())).ensureRec((ICFBamPopSubDep3)rec) );
				case ICFBamPopTopDep.CLASS_CODE:
					return(((CFBamBuffPopTopDepFactoryService)(schema.getCFBamFactory().getFactoryPopTopDep())).ensureRec((ICFBamPopTopDep)rec) );
				case ICFBamRelation.CLASS_CODE:
					return(((CFBamBuffRelationFactoryService)(schema.getCFBamFactory().getFactoryRelation())).ensureRec((ICFBamRelation)rec) );
				default:
					throw new CFLibUnsupportedClassException(getClass(), "ensureRec", "rec", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamScope createScope( ICFSecAuthorization Authorization,
		ICFBamScope iBuff )
	{
		final String S_ProcName = "createScope";
		
		CFBamBuffScope Buff = (CFBamBuffScope)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextScopeIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffScopeByTenantIdxKey keyTenantIdx = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		keyTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Owner",
						"Owner",
						"Tenant",
						"Tenant",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffScope > subdictTenantIdx;
		if( dictByTenantIdx.containsKey( keyTenantIdx ) ) {
			subdictTenantIdx = dictByTenantIdx.get( keyTenantIdx );
		}
		else {
			subdictTenantIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffScope >();
			dictByTenantIdx.put( keyTenantIdx, subdictTenantIdx );
		}
		subdictTenantIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamScope.CLASS_CODE) {
				CFBamBuffScope retbuff = ((CFBamBuffScope)(schema.getCFBamFactory().getFactoryScope().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamSchemaDef.CLASS_CODE) {
				CFBamBuffSchemaDef retbuff = ((CFBamBuffSchemaDef)(schema.getCFBamFactory().getFactorySchemaDef().newRec()));
				retbuff.set((ICFBamSchemaDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamSchemaRef.CLASS_CODE) {
				CFBamBuffSchemaRef retbuff = ((CFBamBuffSchemaRef)(schema.getCFBamFactory().getFactorySchemaRef().newRec()));
				retbuff.set((ICFBamSchemaRef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerMethod.CLASS_CODE) {
				CFBamBuffServerMethod retbuff = ((CFBamBuffServerMethod)(schema.getCFBamFactory().getFactoryServerMethod().newRec()));
				retbuff.set((ICFBamServerMethod)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerObjFunc.CLASS_CODE) {
				CFBamBuffServerObjFunc retbuff = ((CFBamBuffServerObjFunc)(schema.getCFBamFactory().getFactoryServerObjFunc().newRec()));
				retbuff.set((ICFBamServerObjFunc)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerProc.CLASS_CODE) {
				CFBamBuffServerProc retbuff = ((CFBamBuffServerProc)(schema.getCFBamFactory().getFactoryServerProc().newRec()));
				retbuff.set((ICFBamServerProc)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamServerListFunc.CLASS_CODE) {
				CFBamBuffServerListFunc retbuff = ((CFBamBuffServerListFunc)(schema.getCFBamFactory().getFactoryServerListFunc().newRec()));
				retbuff.set((ICFBamServerListFunc)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTable.CLASS_CODE) {
				CFBamBuffTable retbuff = ((CFBamBuffTable)(schema.getCFBamFactory().getFactoryTable().newRec()));
				retbuff.set((ICFBamTable)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearDep.CLASS_CODE) {
				CFBamBuffClearDep retbuff = ((CFBamBuffClearDep)(schema.getCFBamFactory().getFactoryClearDep().newRec()));
				retbuff.set((ICFBamClearDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep1.CLASS_CODE) {
				CFBamBuffClearSubDep1 retbuff = ((CFBamBuffClearSubDep1)(schema.getCFBamFactory().getFactoryClearSubDep1().newRec()));
				retbuff.set((ICFBamClearSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep2.CLASS_CODE) {
				CFBamBuffClearSubDep2 retbuff = ((CFBamBuffClearSubDep2)(schema.getCFBamFactory().getFactoryClearSubDep2().newRec()));
				retbuff.set((ICFBamClearSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearSubDep3.CLASS_CODE) {
				CFBamBuffClearSubDep3 retbuff = ((CFBamBuffClearSubDep3)(schema.getCFBamFactory().getFactoryClearSubDep3().newRec()));
				retbuff.set((ICFBamClearSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamClearTopDep.CLASS_CODE) {
				CFBamBuffClearTopDep retbuff = ((CFBamBuffClearTopDep)(schema.getCFBamFactory().getFactoryClearTopDep().newRec()));
				retbuff.set((ICFBamClearTopDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelDep.CLASS_CODE) {
				CFBamBuffDelDep retbuff = ((CFBamBuffDelDep)(schema.getCFBamFactory().getFactoryDelDep().newRec()));
				retbuff.set((ICFBamDelDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep1.CLASS_CODE) {
				CFBamBuffDelSubDep1 retbuff = ((CFBamBuffDelSubDep1)(schema.getCFBamFactory().getFactoryDelSubDep1().newRec()));
				retbuff.set((ICFBamDelSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep2.CLASS_CODE) {
				CFBamBuffDelSubDep2 retbuff = ((CFBamBuffDelSubDep2)(schema.getCFBamFactory().getFactoryDelSubDep2().newRec()));
				retbuff.set((ICFBamDelSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelSubDep3.CLASS_CODE) {
				CFBamBuffDelSubDep3 retbuff = ((CFBamBuffDelSubDep3)(schema.getCFBamFactory().getFactoryDelSubDep3().newRec()));
				retbuff.set((ICFBamDelSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDelTopDep.CLASS_CODE) {
				CFBamBuffDelTopDep retbuff = ((CFBamBuffDelTopDep)(schema.getCFBamFactory().getFactoryDelTopDep().newRec()));
				retbuff.set((ICFBamDelTopDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamIndex.CLASS_CODE) {
				CFBamBuffIndex retbuff = ((CFBamBuffIndex)(schema.getCFBamFactory().getFactoryIndex().newRec()));
				retbuff.set((ICFBamIndex)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopDep.CLASS_CODE) {
				CFBamBuffPopDep retbuff = ((CFBamBuffPopDep)(schema.getCFBamFactory().getFactoryPopDep().newRec()));
				retbuff.set((ICFBamPopDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep1.CLASS_CODE) {
				CFBamBuffPopSubDep1 retbuff = ((CFBamBuffPopSubDep1)(schema.getCFBamFactory().getFactoryPopSubDep1().newRec()));
				retbuff.set((ICFBamPopSubDep1)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep2.CLASS_CODE) {
				CFBamBuffPopSubDep2 retbuff = ((CFBamBuffPopSubDep2)(schema.getCFBamFactory().getFactoryPopSubDep2().newRec()));
				retbuff.set((ICFBamPopSubDep2)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopSubDep3.CLASS_CODE) {
				CFBamBuffPopSubDep3 retbuff = ((CFBamBuffPopSubDep3)(schema.getCFBamFactory().getFactoryPopSubDep3().newRec()));
				retbuff.set((ICFBamPopSubDep3)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamPopTopDep.CLASS_CODE) {
				CFBamBuffPopTopDep retbuff = ((CFBamBuffPopTopDep)(schema.getCFBamFactory().getFactoryPopTopDep().newRec()));
				retbuff.set((ICFBamPopTopDep)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamRelation.CLASS_CODE) {
				CFBamBuffRelation retbuff = ((CFBamBuffRelation)(schema.getCFBamFactory().getFactoryRelation().newRec()));
				retbuff.set((ICFBamRelation)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamScope readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamScope.readDerived";
		ICFBamScope buff;
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
	public ICFBamScope lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamScope.lockDerived";
		ICFBamScope buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamScope[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamScope.readAllDerived";
		ICFBamScope[] retList = new ICFBamScope[ dictByPKey.values().size() ];
		Iterator< CFBamBuffScope > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamScope[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();

		key.setRequiredTenantId( TenantId );
		ICFBamScope[] recArray;
		if( dictByTenantIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffScope > subdictTenantIdx
				= dictByTenantIdx.get( key );
			recArray = new ICFBamScope[ subdictTenantIdx.size() ];
			Iterator< CFBamBuffScope > iter = subdictTenantIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffScope > subdictTenantIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffScope >();
			dictByTenantIdx.put( key, subdictTenantIdx );
			recArray = new ICFBamScope[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamScope readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamScope buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamScope readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamScope.readRec";
		ICFBamScope buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamScope.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamScope lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamScope buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamScope.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamScope[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamScope.readAllRec";
		ICFBamScope buff;
		ArrayList<ICFBamScope> filteredList = new ArrayList<ICFBamScope>();
		ICFBamScope[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamScope[0] ) );
	}

	@Override
	public ICFBamScope readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamScope buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamScope)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamScope[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamScope buff;
		ArrayList<ICFBamScope> filteredList = new ArrayList<ICFBamScope>();
		ICFBamScope[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamScope)buff );
			}
		}
		return( filteredList.toArray( new ICFBamScope[0] ) );
	}

	public ICFBamScope updateScope( ICFSecAuthorization Authorization,
		ICFBamScope iBuff )
	{
		CFBamBuffScope Buff = (CFBamBuffScope)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffScope existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateScope",
				"Existing record not found",
				"Existing record not found",
				"Scope",
				"Scope",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateScope",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffScopeByTenantIdxKey existingKeyTenantIdx = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		existingKeyTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamBuffScopeByTenantIdxKey newKeyTenantIdx = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		newKeyTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateScope",
						"Owner",
						"Owner",
						"Tenant",
						"Tenant",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffScope > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByTenantIdx.get( existingKeyTenantIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByTenantIdx.containsKey( newKeyTenantIdx ) ) {
			subdict = dictByTenantIdx.get( newKeyTenantIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffScope >();
			dictByTenantIdx.put( newKeyTenantIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteScope( ICFSecAuthorization Authorization,
		ICFBamScope iBuff )
	{
		final String S_ProcName = "CFBamRamScopeTable.deleteScope() ";
		CFBamBuffScope Buff = (CFBamBuffScope)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffScope existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteScope",
				pkey );
		}
		CFBamBuffScopeByTenantIdxKey keyTenantIdx = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		keyTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		// Validate reverse foreign keys

		if( schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"SchemaDef",
				"SchemaDef",
				pkey );
		}

		if( schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"SchemaRef",
				"SchemaRef",
				pkey );
		}

		if( schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ServerMethod",
				"ServerMethod",
				pkey );
		}

		if( schema.getTableTable().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"Table",
				"Table",
				pkey );
		}

		if( schema.getTableClearDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"ClearDep",
				"ClearDep",
				pkey );
		}

		if( schema.getTableDelDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"DelDep",
				"DelDep",
				pkey );
		}

		if( schema.getTableIndex().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"Index",
				"Index",
				pkey );
		}

		if( schema.getTablePopDep().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"PopDep",
				"PopDep",
				pkey );
		}

		if( schema.getTableRelation().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteScope",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"Relation",
				"Relation",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffScope > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByTenantIdx.get( keyTenantIdx );
		subdict.remove( pkey );

	}
	@Override
	public void deleteScopeByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteScopeByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffScope cur;
		LinkedList<CFBamBuffScope> matchSet = new LinkedList<CFBamBuffScope>();
		Iterator<CFBamBuffScope> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffScope> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffScope)(schema.getTableScope().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamScope.CLASS_CODE == subClassCode ) {
				schema.getTableScope().deleteScope( Authorization, cur );
			}
			else if( ICFBamSchemaDef.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaDef().deleteSchemaDef( Authorization, (ICFBamSchemaDef)cur );
			}
			else if( ICFBamSchemaRef.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRef().deleteSchemaRef( Authorization, (ICFBamSchemaRef)cur );
			}
			else if( ICFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, (ICFBamServerMethod)cur );
			}
			else if( ICFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( ICFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( ICFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else if( ICFBamTable.CLASS_CODE == subClassCode ) {
				schema.getTableTable().deleteTable( Authorization, (ICFBamTable)cur );
			}
			else if( ICFBamClearDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearDep().deleteClearDep( Authorization, (ICFBamClearDep)cur );
			}
			else if( ICFBamClearSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( ICFBamClearSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( ICFBamClearSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( ICFBamClearTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else if( ICFBamDelDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelDep().deleteDelDep( Authorization, (ICFBamDelDep)cur );
			}
			else if( ICFBamDelSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( ICFBamDelSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( ICFBamDelSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( ICFBamDelTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else if( ICFBamIndex.CLASS_CODE == subClassCode ) {
				schema.getTableIndex().deleteIndex( Authorization, (ICFBamIndex)cur );
			}
			else if( ICFBamPopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopDep().deletePopDep( Authorization, (ICFBamPopDep)cur );
			}
			else if( ICFBamPopSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( ICFBamPopSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( ICFBamPopSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( ICFBamPopTopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else if( ICFBamRelation.CLASS_CODE == subClassCode ) {
				schema.getTableRelation().deleteRelation( Authorization, (ICFBamRelation)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteScopeByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getCFBamFactory().getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteScopeByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteScopeByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		final String S_ProcName = "deleteScopeByTenantIdx";
		CFBamBuffScope cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffScope> matchSet = new LinkedList<CFBamBuffScope>();
		Iterator<CFBamBuffScope> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffScope> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffScope)(schema.getTableScope().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamScope.CLASS_CODE == subClassCode ) {
				schema.getTableScope().deleteScope( Authorization, cur );
			}
			else if( ICFBamSchemaDef.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaDef().deleteSchemaDef( Authorization, (ICFBamSchemaDef)cur );
			}
			else if( ICFBamSchemaRef.CLASS_CODE == subClassCode ) {
				schema.getTableSchemaRef().deleteSchemaRef( Authorization, (ICFBamSchemaRef)cur );
			}
			else if( ICFBamServerMethod.CLASS_CODE == subClassCode ) {
				schema.getTableServerMethod().deleteServerMethod( Authorization, (ICFBamServerMethod)cur );
			}
			else if( ICFBamServerObjFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerObjFunc().deleteServerObjFunc( Authorization, (ICFBamServerObjFunc)cur );
			}
			else if( ICFBamServerProc.CLASS_CODE == subClassCode ) {
				schema.getTableServerProc().deleteServerProc( Authorization, (ICFBamServerProc)cur );
			}
			else if( ICFBamServerListFunc.CLASS_CODE == subClassCode ) {
				schema.getTableServerListFunc().deleteServerListFunc( Authorization, (ICFBamServerListFunc)cur );
			}
			else if( ICFBamTable.CLASS_CODE == subClassCode ) {
				schema.getTableTable().deleteTable( Authorization, (ICFBamTable)cur );
			}
			else if( ICFBamClearDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearDep().deleteClearDep( Authorization, (ICFBamClearDep)cur );
			}
			else if( ICFBamClearSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep1().deleteClearSubDep1( Authorization, (ICFBamClearSubDep1)cur );
			}
			else if( ICFBamClearSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep2().deleteClearSubDep2( Authorization, (ICFBamClearSubDep2)cur );
			}
			else if( ICFBamClearSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableClearSubDep3().deleteClearSubDep3( Authorization, (ICFBamClearSubDep3)cur );
			}
			else if( ICFBamClearTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableClearTopDep().deleteClearTopDep( Authorization, (ICFBamClearTopDep)cur );
			}
			else if( ICFBamDelDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelDep().deleteDelDep( Authorization, (ICFBamDelDep)cur );
			}
			else if( ICFBamDelSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep1().deleteDelSubDep1( Authorization, (ICFBamDelSubDep1)cur );
			}
			else if( ICFBamDelSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep2().deleteDelSubDep2( Authorization, (ICFBamDelSubDep2)cur );
			}
			else if( ICFBamDelSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTableDelSubDep3().deleteDelSubDep3( Authorization, (ICFBamDelSubDep3)cur );
			}
			else if( ICFBamDelTopDep.CLASS_CODE == subClassCode ) {
				schema.getTableDelTopDep().deleteDelTopDep( Authorization, (ICFBamDelTopDep)cur );
			}
			else if( ICFBamIndex.CLASS_CODE == subClassCode ) {
				schema.getTableIndex().deleteIndex( Authorization, (ICFBamIndex)cur );
			}
			else if( ICFBamPopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopDep().deletePopDep( Authorization, (ICFBamPopDep)cur );
			}
			else if( ICFBamPopSubDep1.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep1().deletePopSubDep1( Authorization, (ICFBamPopSubDep1)cur );
			}
			else if( ICFBamPopSubDep2.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep2().deletePopSubDep2( Authorization, (ICFBamPopSubDep2)cur );
			}
			else if( ICFBamPopSubDep3.CLASS_CODE == subClassCode ) {
				schema.getTablePopSubDep3().deletePopSubDep3( Authorization, (ICFBamPopSubDep3)cur );
			}
			else if( ICFBamPopTopDep.CLASS_CODE == subClassCode ) {
				schema.getTablePopTopDep().deletePopTopDep( Authorization, (ICFBamPopTopDep)cur );
			}
			else if( ICFBamRelation.CLASS_CODE == subClassCode ) {
				schema.getTableRelation().deleteRelation( Authorization, (ICFBamRelation)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
