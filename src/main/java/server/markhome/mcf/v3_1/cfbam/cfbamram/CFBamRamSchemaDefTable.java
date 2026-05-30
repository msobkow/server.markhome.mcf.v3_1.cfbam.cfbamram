
// Description: Java 25 in-memory RAM DbIO implementation for SchemaDef.

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
 *	CFBamRamSchemaDefTable in-memory RAM DbIO implementation
 *	for SchemaDef.
 */
public class CFBamRamSchemaDefTable
	implements ICFBamSchemaDefTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffSchemaDef > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffSchemaDef >();
	private Map< CFBamBuffSchemaDefByCTenantIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaDef >> dictByCTenantIdx
		= new HashMap< CFBamBuffSchemaDefByCTenantIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaDef >>();
	private Map< CFBamBuffSchemaDefByMinorVersionIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaDef >> dictByMinorVersionIdx
		= new HashMap< CFBamBuffSchemaDefByMinorVersionIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaDef >>();
	private Map< CFBamBuffSchemaDefByUNameIdxKey,
			CFBamBuffSchemaDef > dictByUNameIdx
		= new HashMap< CFBamBuffSchemaDefByUNameIdxKey,
			CFBamBuffSchemaDef >();
	private Map< CFBamBuffSchemaDefByAuthEMailIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaDef >> dictByAuthEMailIdx
		= new HashMap< CFBamBuffSchemaDefByAuthEMailIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaDef >>();
	private Map< CFBamBuffSchemaDefByProjectURLIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaDef >> dictByProjectURLIdx
		= new HashMap< CFBamBuffSchemaDefByProjectURLIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffSchemaDef >>();
	private Map< CFBamBuffSchemaDefByPubURIIdxKey,
			CFBamBuffSchemaDef > dictByPubURIIdx
		= new HashMap< CFBamBuffSchemaDefByPubURIIdxKey,
			CFBamBuffSchemaDef >();

	public CFBamRamSchemaDefTable( ICFBamSchema argSchema ) {
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
	public ICFBamSchemaDef createSchemaDef( ICFSecAuthorization Authorization,
		ICFBamSchemaDef iBuff )
	{
		final String S_ProcName = "createSchemaDef";
		
		CFBamBuffSchemaDef Buff = (CFBamBuffSchemaDef)(schema.getTableScope().createScope( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffSchemaDefByCTenantIdxKey keyCTenantIdx = (CFBamBuffSchemaDefByCTenantIdxKey)schema.getFactorySchemaDef().newByCTenantIdxKey();
		keyCTenantIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );

		CFBamBuffSchemaDefByMinorVersionIdxKey keyMinorVersionIdx = (CFBamBuffSchemaDefByMinorVersionIdxKey)schema.getFactorySchemaDef().newByMinorVersionIdxKey();
		keyMinorVersionIdx.setRequiredMinorVersionId( Buff.getRequiredMinorVersionId() );

		CFBamBuffSchemaDefByUNameIdxKey keyUNameIdx = (CFBamBuffSchemaDefByUNameIdxKey)schema.getFactorySchemaDef().newByUNameIdxKey();
		keyUNameIdx.setRequiredMinorVersionId( Buff.getRequiredMinorVersionId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffSchemaDefByAuthEMailIdxKey keyAuthEMailIdx = (CFBamBuffSchemaDefByAuthEMailIdxKey)schema.getFactorySchemaDef().newByAuthEMailIdxKey();
		keyAuthEMailIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		keyAuthEMailIdx.setRequiredAuthorEMail( Buff.getRequiredAuthorEMail() );

		CFBamBuffSchemaDefByProjectURLIdxKey keyProjectURLIdx = (CFBamBuffSchemaDefByProjectURLIdxKey)schema.getFactorySchemaDef().newByProjectURLIdxKey();
		keyProjectURLIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		keyProjectURLIdx.setRequiredProjectURL( Buff.getRequiredProjectURL() );

		CFBamBuffSchemaDefByPubURIIdxKey keyPubURIIdx = (CFBamBuffSchemaDefByPubURIIdxKey)schema.getFactorySchemaDef().newByPubURIIdxKey();
		keyPubURIIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		keyPubURIIdx.setRequiredPublishURI( Buff.getRequiredPublishURI() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"SchemaDefUNameIdx",
				"SchemaDefUNameIdx",
				keyUNameIdx );
		}

		if( dictByPubURIIdx.containsKey( keyPubURIIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"SchemaPublishURIIdx",
				"SchemaPublishURIIdx",
				keyPubURIIdx );
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
				if( null == schema.getTableMinorVersion().readDerivedByIdIdx( Authorization,
						Buff.getRequiredMinorVersionId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"MinorVersion",
						"MinorVersion",
						"MinorVersion",
						"MinorVersion",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredCTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Owner",
						"Owner",
						"CTenant",
						"CTenant",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictCTenantIdx;
		if( dictByCTenantIdx.containsKey( keyCTenantIdx ) ) {
			subdictCTenantIdx = dictByCTenantIdx.get( keyCTenantIdx );
		}
		else {
			subdictCTenantIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByCTenantIdx.put( keyCTenantIdx, subdictCTenantIdx );
		}
		subdictCTenantIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictMinorVersionIdx;
		if( dictByMinorVersionIdx.containsKey( keyMinorVersionIdx ) ) {
			subdictMinorVersionIdx = dictByMinorVersionIdx.get( keyMinorVersionIdx );
		}
		else {
			subdictMinorVersionIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByMinorVersionIdx.put( keyMinorVersionIdx, subdictMinorVersionIdx );
		}
		subdictMinorVersionIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictAuthEMailIdx;
		if( dictByAuthEMailIdx.containsKey( keyAuthEMailIdx ) ) {
			subdictAuthEMailIdx = dictByAuthEMailIdx.get( keyAuthEMailIdx );
		}
		else {
			subdictAuthEMailIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByAuthEMailIdx.put( keyAuthEMailIdx, subdictAuthEMailIdx );
		}
		subdictAuthEMailIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictProjectURLIdx;
		if( dictByProjectURLIdx.containsKey( keyProjectURLIdx ) ) {
			subdictProjectURLIdx = dictByProjectURLIdx.get( keyProjectURLIdx );
		}
		else {
			subdictProjectURLIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByProjectURLIdx.put( keyProjectURLIdx, subdictProjectURLIdx );
		}
		subdictProjectURLIdx.put( pkey, Buff );

		dictByPubURIIdx.put( keyPubURIIdx, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamSchemaDef.CLASS_CODE) {
				CFBamBuffSchemaDef retbuff = ((CFBamBuffSchemaDef)(schema.getFactorySchemaDef().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamSchemaDef readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerived";
		ICFBamSchemaDef buff;
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
	public ICFBamSchemaDef lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaDef.lockDerived";
		ICFBamSchemaDef buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaDef[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaDef.readAllDerived";
		ICFBamSchemaDef[] retList = new ICFBamSchemaDef[ dictByPKey.values().size() ];
		Iterator< CFBamBuffSchemaDef > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamSchemaDef[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamSchemaDef> filteredList = new ArrayList<ICFBamSchemaDef>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamSchemaDef ) ) {
					filteredList.add( (ICFBamSchemaDef)buff );
				}
			}
			return( filteredList.toArray( new ICFBamSchemaDef[0] ) );
		}
	}

	@Override
	public ICFBamSchemaDef[] readDerivedByCTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByCTenantIdx";
		CFBamBuffSchemaDefByCTenantIdxKey key = (CFBamBuffSchemaDefByCTenantIdxKey)schema.getFactorySchemaDef().newByCTenantIdxKey();

		key.setRequiredCTenantId( CTenantId );
		ICFBamSchemaDef[] recArray;
		if( dictByCTenantIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictCTenantIdx
				= dictByCTenantIdx.get( key );
			recArray = new ICFBamSchemaDef[ subdictCTenantIdx.size() ];
			Iterator< CFBamBuffSchemaDef > iter = subdictCTenantIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictCTenantIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByCTenantIdx.put( key, subdictCTenantIdx );
			recArray = new ICFBamSchemaDef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaDef[] readDerivedByMinorVersionIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByMinorVersionIdx";
		CFBamBuffSchemaDefByMinorVersionIdxKey key = (CFBamBuffSchemaDefByMinorVersionIdxKey)schema.getFactorySchemaDef().newByMinorVersionIdxKey();

		key.setRequiredMinorVersionId( MinorVersionId );
		ICFBamSchemaDef[] recArray;
		if( dictByMinorVersionIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictMinorVersionIdx
				= dictByMinorVersionIdx.get( key );
			recArray = new ICFBamSchemaDef[ subdictMinorVersionIdx.size() ];
			Iterator< CFBamBuffSchemaDef > iter = subdictMinorVersionIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictMinorVersionIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByMinorVersionIdx.put( key, subdictMinorVersionIdx );
			recArray = new ICFBamSchemaDef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaDef readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByUNameIdx";
		CFBamBuffSchemaDefByUNameIdxKey key = (CFBamBuffSchemaDefByUNameIdxKey)schema.getFactorySchemaDef().newByUNameIdxKey();

		key.setRequiredMinorVersionId( MinorVersionId );
		key.setRequiredName( Name );
		ICFBamSchemaDef buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaDef[] readDerivedByAuthEMailIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String AuthorEMail )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByAuthEMailIdx";
		CFBamBuffSchemaDefByAuthEMailIdxKey key = (CFBamBuffSchemaDefByAuthEMailIdxKey)schema.getFactorySchemaDef().newByAuthEMailIdxKey();

		key.setRequiredCTenantId( CTenantId );
		key.setRequiredAuthorEMail( AuthorEMail );
		ICFBamSchemaDef[] recArray;
		if( dictByAuthEMailIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictAuthEMailIdx
				= dictByAuthEMailIdx.get( key );
			recArray = new ICFBamSchemaDef[ subdictAuthEMailIdx.size() ];
			Iterator< CFBamBuffSchemaDef > iter = subdictAuthEMailIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictAuthEMailIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByAuthEMailIdx.put( key, subdictAuthEMailIdx );
			recArray = new ICFBamSchemaDef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaDef[] readDerivedByProjectURLIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String ProjectURL )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByProjectURLIdx";
		CFBamBuffSchemaDefByProjectURLIdxKey key = (CFBamBuffSchemaDefByProjectURLIdxKey)schema.getFactorySchemaDef().newByProjectURLIdxKey();

		key.setRequiredCTenantId( CTenantId );
		key.setRequiredProjectURL( ProjectURL );
		ICFBamSchemaDef[] recArray;
		if( dictByProjectURLIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictProjectURLIdx
				= dictByProjectURLIdx.get( key );
			recArray = new ICFBamSchemaDef[ subdictProjectURLIdx.size() ];
			Iterator< CFBamBuffSchemaDef > iter = subdictProjectURLIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdictProjectURLIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByProjectURLIdx.put( key, subdictProjectURLIdx );
			recArray = new ICFBamSchemaDef[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamSchemaDef readDerivedByPubURIIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String PublishURI )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readDerivedByPubURIIdx";
		CFBamBuffSchemaDefByPubURIIdxKey key = (CFBamBuffSchemaDefByPubURIIdxKey)schema.getFactorySchemaDef().newByPubURIIdxKey();

		key.setRequiredCTenantId( CTenantId );
		key.setRequiredPublishURI( PublishURI );
		ICFBamSchemaDef buff;
		if( dictByPubURIIdx.containsKey( key ) ) {
			buff = dictByPubURIIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaDef readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamSchemaDef buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaDef readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readRec";
		ICFBamSchemaDef buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamSchemaDef.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaDef lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamSchemaDef buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamSchemaDef.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamSchemaDef[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readAllRec";
		ICFBamSchemaDef buff;
		ArrayList<ICFBamSchemaDef> filteredList = new ArrayList<ICFBamSchemaDef>();
		ICFBamSchemaDef[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaDef.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaDef[0] ) );
	}

	@Override
	public ICFBamSchemaDef readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamSchemaDef buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamSchemaDef)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaDef[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamSchemaDef buff;
		ArrayList<ICFBamSchemaDef> filteredList = new ArrayList<ICFBamSchemaDef>();
		ICFBamSchemaDef[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaDef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaDef[0] ) );
	}

	@Override
	public ICFBamSchemaDef[] readRecByCTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readRecByCTenantIdx() ";
		ICFBamSchemaDef buff;
		ArrayList<ICFBamSchemaDef> filteredList = new ArrayList<ICFBamSchemaDef>();
		ICFBamSchemaDef[] buffList = readDerivedByCTenantIdx( Authorization,
			CTenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaDef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaDef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaDef[0] ) );
	}

	@Override
	public ICFBamSchemaDef[] readRecByMinorVersionIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readRecByMinorVersionIdx() ";
		ICFBamSchemaDef buff;
		ArrayList<ICFBamSchemaDef> filteredList = new ArrayList<ICFBamSchemaDef>();
		ICFBamSchemaDef[] buffList = readDerivedByMinorVersionIdx( Authorization,
			MinorVersionId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaDef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaDef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaDef[0] ) );
	}

	@Override
	public ICFBamSchemaDef readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 MinorVersionId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readRecByUNameIdx() ";
		ICFBamSchemaDef buff = readDerivedByUNameIdx( Authorization,
			MinorVersionId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaDef.CLASS_CODE ) ) {
			return( (ICFBamSchemaDef)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamSchemaDef[] readRecByAuthEMailIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String AuthorEMail )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readRecByAuthEMailIdx() ";
		ICFBamSchemaDef buff;
		ArrayList<ICFBamSchemaDef> filteredList = new ArrayList<ICFBamSchemaDef>();
		ICFBamSchemaDef[] buffList = readDerivedByAuthEMailIdx( Authorization,
			CTenantId,
			AuthorEMail );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaDef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaDef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaDef[0] ) );
	}

	@Override
	public ICFBamSchemaDef[] readRecByProjectURLIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String ProjectURL )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readRecByProjectURLIdx() ";
		ICFBamSchemaDef buff;
		ArrayList<ICFBamSchemaDef> filteredList = new ArrayList<ICFBamSchemaDef>();
		ICFBamSchemaDef[] buffList = readDerivedByProjectURLIdx( Authorization,
			CTenantId,
			ProjectURL );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaDef.CLASS_CODE ) ) {
				filteredList.add( (ICFBamSchemaDef)buff );
			}
		}
		return( filteredList.toArray( new ICFBamSchemaDef[0] ) );
	}

	@Override
	public ICFBamSchemaDef readRecByPubURIIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 CTenantId,
		String PublishURI )
	{
		final String S_ProcName = "CFBamRamSchemaDef.readRecByPubURIIdx() ";
		ICFBamSchemaDef buff = readDerivedByPubURIIdx( Authorization,
			CTenantId,
			PublishURI );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamSchemaDef.CLASS_CODE ) ) {
			return( (ICFBamSchemaDef)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamSchemaDef updateSchemaDef( ICFSecAuthorization Authorization,
		ICFBamSchemaDef iBuff )
	{
		CFBamBuffSchemaDef Buff = (CFBamBuffSchemaDef)(schema.getTableScope().updateScope( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffSchemaDef existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaDef",
				"Existing record not found",
				"Existing record not found",
				"SchemaDef",
				"SchemaDef",
				pkey );
		}
		CFBamBuffSchemaDefByCTenantIdxKey existingKeyCTenantIdx = (CFBamBuffSchemaDefByCTenantIdxKey)schema.getFactorySchemaDef().newByCTenantIdxKey();
		existingKeyCTenantIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );

		CFBamBuffSchemaDefByCTenantIdxKey newKeyCTenantIdx = (CFBamBuffSchemaDefByCTenantIdxKey)schema.getFactorySchemaDef().newByCTenantIdxKey();
		newKeyCTenantIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );

		CFBamBuffSchemaDefByMinorVersionIdxKey existingKeyMinorVersionIdx = (CFBamBuffSchemaDefByMinorVersionIdxKey)schema.getFactorySchemaDef().newByMinorVersionIdxKey();
		existingKeyMinorVersionIdx.setRequiredMinorVersionId( existing.getRequiredMinorVersionId() );

		CFBamBuffSchemaDefByMinorVersionIdxKey newKeyMinorVersionIdx = (CFBamBuffSchemaDefByMinorVersionIdxKey)schema.getFactorySchemaDef().newByMinorVersionIdxKey();
		newKeyMinorVersionIdx.setRequiredMinorVersionId( Buff.getRequiredMinorVersionId() );

		CFBamBuffSchemaDefByUNameIdxKey existingKeyUNameIdx = (CFBamBuffSchemaDefByUNameIdxKey)schema.getFactorySchemaDef().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredMinorVersionId( existing.getRequiredMinorVersionId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffSchemaDefByUNameIdxKey newKeyUNameIdx = (CFBamBuffSchemaDefByUNameIdxKey)schema.getFactorySchemaDef().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredMinorVersionId( Buff.getRequiredMinorVersionId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffSchemaDefByAuthEMailIdxKey existingKeyAuthEMailIdx = (CFBamBuffSchemaDefByAuthEMailIdxKey)schema.getFactorySchemaDef().newByAuthEMailIdxKey();
		existingKeyAuthEMailIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		existingKeyAuthEMailIdx.setRequiredAuthorEMail( existing.getRequiredAuthorEMail() );

		CFBamBuffSchemaDefByAuthEMailIdxKey newKeyAuthEMailIdx = (CFBamBuffSchemaDefByAuthEMailIdxKey)schema.getFactorySchemaDef().newByAuthEMailIdxKey();
		newKeyAuthEMailIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		newKeyAuthEMailIdx.setRequiredAuthorEMail( Buff.getRequiredAuthorEMail() );

		CFBamBuffSchemaDefByProjectURLIdxKey existingKeyProjectURLIdx = (CFBamBuffSchemaDefByProjectURLIdxKey)schema.getFactorySchemaDef().newByProjectURLIdxKey();
		existingKeyProjectURLIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		existingKeyProjectURLIdx.setRequiredProjectURL( existing.getRequiredProjectURL() );

		CFBamBuffSchemaDefByProjectURLIdxKey newKeyProjectURLIdx = (CFBamBuffSchemaDefByProjectURLIdxKey)schema.getFactorySchemaDef().newByProjectURLIdxKey();
		newKeyProjectURLIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		newKeyProjectURLIdx.setRequiredProjectURL( Buff.getRequiredProjectURL() );

		CFBamBuffSchemaDefByPubURIIdxKey existingKeyPubURIIdx = (CFBamBuffSchemaDefByPubURIIdxKey)schema.getFactorySchemaDef().newByPubURIIdxKey();
		existingKeyPubURIIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		existingKeyPubURIIdx.setRequiredPublishURI( existing.getRequiredPublishURI() );

		CFBamBuffSchemaDefByPubURIIdxKey newKeyPubURIIdx = (CFBamBuffSchemaDefByPubURIIdxKey)schema.getFactorySchemaDef().newByPubURIIdxKey();
		newKeyPubURIIdx.setRequiredCTenantId( Buff.getRequiredCTenantId() );
		newKeyPubURIIdx.setRequiredPublishURI( Buff.getRequiredPublishURI() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateSchemaDef",
					"SchemaDefUNameIdx",
					"SchemaDefUNameIdx",
					newKeyUNameIdx );
			}
		}

		if( ! existingKeyPubURIIdx.equals( newKeyPubURIIdx ) ) {
			if( dictByPubURIIdx.containsKey( newKeyPubURIIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateSchemaDef",
					"SchemaPublishURIIdx",
					"SchemaPublishURIIdx",
					newKeyPubURIIdx );
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
						"updateSchemaDef",
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
				if( null == schema.getTableMinorVersion().readDerivedByIdIdx( Authorization,
						Buff.getRequiredMinorVersionId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaDef",
						"Container",
						"Container",
						"MinorVersion",
						"MinorVersion",
						"MinorVersion",
						"MinorVersion",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredCTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaDef",
						"Owner",
						"Owner",
						"CTenant",
						"CTenant",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByCTenantIdx.get( existingKeyCTenantIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByCTenantIdx.containsKey( newKeyCTenantIdx ) ) {
			subdict = dictByCTenantIdx.get( newKeyCTenantIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByCTenantIdx.put( newKeyCTenantIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByMinorVersionIdx.get( existingKeyMinorVersionIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByMinorVersionIdx.containsKey( newKeyMinorVersionIdx ) ) {
			subdict = dictByMinorVersionIdx.get( newKeyMinorVersionIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByMinorVersionIdx.put( newKeyMinorVersionIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByAuthEMailIdx.get( existingKeyAuthEMailIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByAuthEMailIdx.containsKey( newKeyAuthEMailIdx ) ) {
			subdict = dictByAuthEMailIdx.get( newKeyAuthEMailIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByAuthEMailIdx.put( newKeyAuthEMailIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByProjectURLIdx.get( existingKeyProjectURLIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByProjectURLIdx.containsKey( newKeyProjectURLIdx ) ) {
			subdict = dictByProjectURLIdx.get( newKeyProjectURLIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffSchemaDef >();
			dictByProjectURLIdx.put( newKeyProjectURLIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByPubURIIdx.remove( existingKeyPubURIIdx );
		dictByPubURIIdx.put( newKeyPubURIIdx, Buff );

		return(Buff);
	}

	@Override
	public void deleteSchemaDef( ICFSecAuthorization Authorization,
		ICFBamSchemaDef iBuff )
	{
		final String S_ProcName = "CFBamRamSchemaDefTable.deleteSchemaDef() ";
		CFBamBuffSchemaDef Buff = (CFBamBuffSchemaDef)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffSchemaDef existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaDef",
				pkey );
		}
			CFBamBuffValue buffClearTypeReferences;
			ICFBamValue arrClearTypeReferences[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			existing.getRequiredId() );
			for( int idxClearTypeReferences = 0; idxClearTypeReferences < arrClearTypeReferences.length; idxClearTypeReferences++ ) {
				buffClearTypeReferences = (CFBamBuffValue)(arrClearTypeReferences[idxClearTypeReferences]);
				CFBamBuffTableCol buffReferencingTableCols;
				ICFBamTableCol arrReferencingTableCols[] = schema.getTableTableCol().readDerivedByDataIdx( Authorization,
				buffClearTypeReferences.getRequiredId() );
				for( int idxReferencingTableCols = 0; idxReferencingTableCols < arrReferencingTableCols.length; idxReferencingTableCols++ ) {
					buffReferencingTableCols = (CFBamBuffTableCol)(arrReferencingTableCols[idxReferencingTableCols]);
					{
						CFBamBuffTableCol editBuff = (CFBamBuffTableCol)(schema.getTableTableCol().readDerivedByIdIdx( Authorization,
							buffReferencingTableCols.getRequiredId() ));
						editBuff.setRequiredParentDataType((CFLibDbKeyHash256)null);
						classCode = editBuff.getClassCode();
						if( classCode == ICFBamTableCol.CLASS_CODE ) {
							schema.getTableTableCol().updateTableCol( Authorization, editBuff );
						}
						else {
							throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-clear-sub-dep-2-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
						}
					}
				}
			}
			CFBamBuffTable buffClearTableRelationNarrowed;
			ICFBamTable arrClearTableRelationNarrowed[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
			for( int idxClearTableRelationNarrowed = 0; idxClearTableRelationNarrowed < arrClearTableRelationNarrowed.length; idxClearTableRelationNarrowed++ ) {
				buffClearTableRelationNarrowed = (CFBamBuffTable)(arrClearTableRelationNarrowed[idxClearTableRelationNarrowed]);
				CFBamBuffRelation buffTableRelation;
				ICFBamRelation arrTableRelation[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
				buffClearTableRelationNarrowed.getRequiredId() );
				for( int idxTableRelation = 0; idxTableRelation < arrTableRelation.length; idxTableRelation++ ) {
					buffTableRelation = (CFBamBuffRelation)(arrTableRelation[idxTableRelation]);
					{
						CFBamBuffRelation editBuff = (CFBamBuffRelation)(schema.getTableRelation().readDerivedByIdIdx( Authorization,
							buffTableRelation.getRequiredId() ));
						editBuff.setOptionalLookupNarrowed((CFLibDbKeyHash256)null);
						classCode = editBuff.getClassCode();
						if( classCode == ICFBamRelation.CLASS_CODE ) {
							schema.getTableRelation().updateRelation( Authorization, editBuff );
						}
						else {
							throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-clear-sub-dep-2-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
						}
					}
				}
			}
		CFBamBuffTable buffDelTableMethods;
		ICFBamTable arrDelTableMethods[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableMethods = 0; idxDelTableMethods < arrDelTableMethods.length; idxDelTableMethods++ ) {
			buffDelTableMethods = (CFBamBuffTable)(arrDelTableMethods[idxDelTableMethods]);
					schema.getTableServerMethod().deleteServerMethodByMethTableIdx( Authorization,
						buffDelTableMethods.getRequiredId() );
		}
		CFBamBuffTable buffDelTableDelDep;
		ICFBamTable arrDelTableDelDep[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableDelDep = 0; idxDelTableDelDep < arrDelTableDelDep.length; idxDelTableDelDep++ ) {
			buffDelTableDelDep = (CFBamBuffTable)(arrDelTableDelDep[idxDelTableDelDep]);
					schema.getTableDelTopDep().deleteDelTopDepByDelTopDepTblIdx( Authorization,
						buffDelTableDelDep.getRequiredId() );
		}
		CFBamBuffTable buffDelTableClearDep;
		ICFBamTable arrDelTableClearDep[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableClearDep = 0; idxDelTableClearDep < arrDelTableClearDep.length; idxDelTableClearDep++ ) {
			buffDelTableClearDep = (CFBamBuffTable)(arrDelTableClearDep[idxDelTableClearDep]);
					schema.getTableClearTopDep().deleteClearTopDepByClrTopDepTblIdx( Authorization,
						buffDelTableClearDep.getRequiredId() );
		}
		CFBamBuffTable buffDelTableChain;
		ICFBamTable arrDelTableChain[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableChain = 0; idxDelTableChain < arrDelTableChain.length; idxDelTableChain++ ) {
			buffDelTableChain = (CFBamBuffTable)(arrDelTableChain[idxDelTableChain]);
					schema.getTableChain().deleteChainByChainTableIdx( Authorization,
						buffDelTableChain.getRequiredId() );
		}
		CFBamBuffTable buffDelTableRelationPopDep;
		ICFBamTable arrDelTableRelationPopDep[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationPopDep = 0; idxDelTableRelationPopDep < arrDelTableRelationPopDep.length; idxDelTableRelationPopDep++ ) {
			buffDelTableRelationPopDep = (CFBamBuffTable)(arrDelTableRelationPopDep[idxDelTableRelationPopDep]);
			CFBamBuffRelation buffTableRelation;
			ICFBamRelation arrTableRelation[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
				buffDelTableRelationPopDep.getRequiredId() );
			for( int idxTableRelation = 0; idxTableRelation < arrTableRelation.length; idxTableRelation++ ) {
				buffTableRelation = (CFBamBuffRelation)(arrTableRelation[idxTableRelation]);
					schema.getTablePopTopDep().deletePopTopDepByContRelIdx( Authorization,
						buffTableRelation.getRequiredId() );
			}
		}
		CFBamBuffTable buffDelTableRelationCol;
		ICFBamTable arrDelTableRelationCol[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationCol = 0; idxDelTableRelationCol < arrDelTableRelationCol.length; idxDelTableRelationCol++ ) {
			buffDelTableRelationCol = (CFBamBuffTable)(arrDelTableRelationCol[idxDelTableRelationCol]);
			CFBamBuffRelation buffTableRelation;
			ICFBamRelation arrTableRelation[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
				buffDelTableRelationCol.getRequiredId() );
			for( int idxTableRelation = 0; idxTableRelation < arrTableRelation.length; idxTableRelation++ ) {
				buffTableRelation = (CFBamBuffRelation)(arrTableRelation[idxTableRelation]);
					schema.getTableRelationCol().deleteRelationColByRelationIdx( Authorization,
						buffTableRelation.getRequiredId() );
			}
		}
		CFBamBuffTable buffDelTableRelation;
		ICFBamTable arrDelTableRelation[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelation = 0; idxDelTableRelation < arrDelTableRelation.length; idxDelTableRelation++ ) {
			buffDelTableRelation = (CFBamBuffTable)(arrDelTableRelation[idxDelTableRelation]);
					schema.getTableRelation().deleteRelationByRelTableIdx( Authorization,
						buffDelTableRelation.getRequiredId() );
		}
		CFBamBuffTable buffDelTableIndexRefRelFmCol;
		ICFBamTable arrDelTableIndexRefRelFmCol[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexRefRelFmCol = 0; idxDelTableIndexRefRelFmCol < arrDelTableIndexRefRelFmCol.length; idxDelTableIndexRefRelFmCol++ ) {
			buffDelTableIndexRefRelFmCol = (CFBamBuffTable)(arrDelTableIndexRefRelFmCol[idxDelTableIndexRefRelFmCol]);
			CFBamBuffIndex buffTableIndex;
			ICFBamIndex arrTableIndex[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
				buffDelTableIndexRefRelFmCol.getRequiredId() );
			for( int idxTableIndex = 0; idxTableIndex < arrTableIndex.length; idxTableIndex++ ) {
				buffTableIndex = (CFBamBuffIndex)(arrTableIndex[idxTableIndex]);
			CFBamBuffIndexCol buffColumns;
			ICFBamIndexCol arrColumns[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
					buffTableIndex.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = (CFBamBuffIndexCol)(arrColumns[idxColumns]);
					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
			}
		}
		CFBamBuffTable buffDelTableIndexRefRelToCol;
		ICFBamTable arrDelTableIndexRefRelToCol[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexRefRelToCol = 0; idxDelTableIndexRefRelToCol < arrDelTableIndexRefRelToCol.length; idxDelTableIndexRefRelToCol++ ) {
			buffDelTableIndexRefRelToCol = (CFBamBuffTable)(arrDelTableIndexRefRelToCol[idxDelTableIndexRefRelToCol]);
			CFBamBuffIndex buffTableIndex;
			ICFBamIndex arrTableIndex[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
				buffDelTableIndexRefRelToCol.getRequiredId() );
			for( int idxTableIndex = 0; idxTableIndex < arrTableIndex.length; idxTableIndex++ ) {
				buffTableIndex = (CFBamBuffIndex)(arrTableIndex[idxTableIndex]);
			CFBamBuffIndexCol buffColumns;
			ICFBamIndexCol arrColumns[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
					buffTableIndex.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = (CFBamBuffIndexCol)(arrColumns[idxColumns]);
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
			}
		}
		CFBamBuffTable buffDelTableIndexCols;
		ICFBamTable arrDelTableIndexCols[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexCols = 0; idxDelTableIndexCols < arrDelTableIndexCols.length; idxDelTableIndexCols++ ) {
			buffDelTableIndexCols = (CFBamBuffTable)(arrDelTableIndexCols[idxDelTableIndexCols]);
			CFBamBuffIndex buffTableIndex;
			ICFBamIndex arrTableIndex[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
				buffDelTableIndexCols.getRequiredId() );
			for( int idxTableIndex = 0; idxTableIndex < arrTableIndex.length; idxTableIndex++ ) {
				buffTableIndex = (CFBamBuffIndex)(arrTableIndex[idxTableIndex]);
					schema.getTableIndexCol().deleteIndexColByIndexIdx( Authorization,
						buffTableIndex.getRequiredId() );
			}
		}
		CFBamBuffTable buffDelTableIndexes;
		ICFBamTable arrDelTableIndexes[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexes = 0; idxDelTableIndexes < arrDelTableIndexes.length; idxDelTableIndexes++ ) {
			buffDelTableIndexes = (CFBamBuffTable)(arrDelTableIndexes[idxDelTableIndexes]);
					schema.getTableIndex().deleteIndexByIdxTableIdx( Authorization,
						buffDelTableIndexes.getRequiredId() );
		}
		CFBamBuffTable buffDelTableRefIndexColumns;
		ICFBamTable arrDelTableRefIndexColumns[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRefIndexColumns = 0; idxDelTableRefIndexColumns < arrDelTableRefIndexColumns.length; idxDelTableRefIndexColumns++ ) {
			buffDelTableRefIndexColumns = (CFBamBuffTable)(arrDelTableRefIndexColumns[idxDelTableRefIndexColumns]);
			CFBamBuffValue buffColumns;
			ICFBamValue arrColumns[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
				buffDelTableRefIndexColumns.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = (CFBamBuffValue)(arrColumns[idxColumns]);
					schema.getTableIndexCol().deleteIndexColByColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
		}
		CFBamBuffTable buffDelTableColumns;
		ICFBamTable arrDelTableColumns[] = schema.getTableTable().readDerivedBySchemaDefIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableColumns = 0; idxDelTableColumns < arrDelTableColumns.length; idxDelTableColumns++ ) {
			buffDelTableColumns = (CFBamBuffTable)(arrDelTableColumns[idxDelTableColumns]);
					schema.getTableValue().deleteValueByScopeIdx( Authorization,
						buffDelTableColumns.getRequiredId() );
		}
					schema.getTableTable().deleteTableBySchemaDefIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffValue buffDelTypeRefs;
		ICFBamValue arrDelTypeRefs[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTypeRefs = 0; idxDelTypeRefs < arrDelTypeRefs.length; idxDelTypeRefs++ ) {
			buffDelTypeRefs = (CFBamBuffValue)(arrDelTypeRefs[idxDelTypeRefs]);
					schema.getTableTableCol().deleteTableColByDataIdx( Authorization,
						buffDelTypeRefs.getRequiredId() );
		}
					schema.getTableValue().deleteValueByScopeIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffSchemaDefByCTenantIdxKey keyCTenantIdx = (CFBamBuffSchemaDefByCTenantIdxKey)schema.getFactorySchemaDef().newByCTenantIdxKey();
		keyCTenantIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );

		CFBamBuffSchemaDefByMinorVersionIdxKey keyMinorVersionIdx = (CFBamBuffSchemaDefByMinorVersionIdxKey)schema.getFactorySchemaDef().newByMinorVersionIdxKey();
		keyMinorVersionIdx.setRequiredMinorVersionId( existing.getRequiredMinorVersionId() );

		CFBamBuffSchemaDefByUNameIdxKey keyUNameIdx = (CFBamBuffSchemaDefByUNameIdxKey)schema.getFactorySchemaDef().newByUNameIdxKey();
		keyUNameIdx.setRequiredMinorVersionId( existing.getRequiredMinorVersionId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffSchemaDefByAuthEMailIdxKey keyAuthEMailIdx = (CFBamBuffSchemaDefByAuthEMailIdxKey)schema.getFactorySchemaDef().newByAuthEMailIdxKey();
		keyAuthEMailIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		keyAuthEMailIdx.setRequiredAuthorEMail( existing.getRequiredAuthorEMail() );

		CFBamBuffSchemaDefByProjectURLIdxKey keyProjectURLIdx = (CFBamBuffSchemaDefByProjectURLIdxKey)schema.getFactorySchemaDef().newByProjectURLIdxKey();
		keyProjectURLIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		keyProjectURLIdx.setRequiredProjectURL( existing.getRequiredProjectURL() );

		CFBamBuffSchemaDefByPubURIIdxKey keyPubURIIdx = (CFBamBuffSchemaDefByPubURIIdxKey)schema.getFactorySchemaDef().newByPubURIIdxKey();
		keyPubURIIdx.setRequiredCTenantId( existing.getRequiredCTenantId() );
		keyPubURIIdx.setRequiredPublishURI( existing.getRequiredPublishURI() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffSchemaDef > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByCTenantIdx.get( keyCTenantIdx );
		subdict.remove( pkey );

		subdict = dictByMinorVersionIdx.get( keyMinorVersionIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByAuthEMailIdx.get( keyAuthEMailIdx );
		subdict.remove( pkey );

		subdict = dictByProjectURLIdx.get( keyProjectURLIdx );
		subdict.remove( pkey );

		dictByPubURIIdx.remove( keyPubURIIdx );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	@Override
	public void deleteSchemaDefByCTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argCTenantId )
	{
		CFBamBuffSchemaDefByCTenantIdxKey key = (CFBamBuffSchemaDefByCTenantIdxKey)schema.getFactorySchemaDef().newByCTenantIdxKey();
		key.setRequiredCTenantId( argCTenantId );
		deleteSchemaDefByCTenantIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaDefByCTenantIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaDefByCTenantIdxKey argKey )
	{
		CFBamBuffSchemaDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaDef> matchSet = new LinkedList<CFBamBuffSchemaDef>();
		Iterator<CFBamBuffSchemaDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaDef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaDefByMinorVersionIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argMinorVersionId )
	{
		CFBamBuffSchemaDefByMinorVersionIdxKey key = (CFBamBuffSchemaDefByMinorVersionIdxKey)schema.getFactorySchemaDef().newByMinorVersionIdxKey();
		key.setRequiredMinorVersionId( argMinorVersionId );
		deleteSchemaDefByMinorVersionIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaDefByMinorVersionIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaDefByMinorVersionIdxKey argKey )
	{
		CFBamBuffSchemaDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaDef> matchSet = new LinkedList<CFBamBuffSchemaDef>();
		Iterator<CFBamBuffSchemaDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaDef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaDefByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argMinorVersionId,
		String argName )
	{
		CFBamBuffSchemaDefByUNameIdxKey key = (CFBamBuffSchemaDefByUNameIdxKey)schema.getFactorySchemaDef().newByUNameIdxKey();
		key.setRequiredMinorVersionId( argMinorVersionId );
		key.setRequiredName( argName );
		deleteSchemaDefByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaDefByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaDefByUNameIdxKey argKey )
	{
		CFBamBuffSchemaDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaDef> matchSet = new LinkedList<CFBamBuffSchemaDef>();
		Iterator<CFBamBuffSchemaDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaDef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaDefByAuthEMailIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argCTenantId,
		String argAuthorEMail )
	{
		CFBamBuffSchemaDefByAuthEMailIdxKey key = (CFBamBuffSchemaDefByAuthEMailIdxKey)schema.getFactorySchemaDef().newByAuthEMailIdxKey();
		key.setRequiredCTenantId( argCTenantId );
		key.setRequiredAuthorEMail( argAuthorEMail );
		deleteSchemaDefByAuthEMailIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaDefByAuthEMailIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaDefByAuthEMailIdxKey argKey )
	{
		CFBamBuffSchemaDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaDef> matchSet = new LinkedList<CFBamBuffSchemaDef>();
		Iterator<CFBamBuffSchemaDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaDef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaDefByProjectURLIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argCTenantId,
		String argProjectURL )
	{
		CFBamBuffSchemaDefByProjectURLIdxKey key = (CFBamBuffSchemaDefByProjectURLIdxKey)schema.getFactorySchemaDef().newByProjectURLIdxKey();
		key.setRequiredCTenantId( argCTenantId );
		key.setRequiredProjectURL( argProjectURL );
		deleteSchemaDefByProjectURLIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaDefByProjectURLIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaDefByProjectURLIdxKey argKey )
	{
		CFBamBuffSchemaDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaDef> matchSet = new LinkedList<CFBamBuffSchemaDef>();
		Iterator<CFBamBuffSchemaDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaDef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaDefByPubURIIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argCTenantId,
		String argPublishURI )
	{
		CFBamBuffSchemaDefByPubURIIdxKey key = (CFBamBuffSchemaDefByPubURIIdxKey)schema.getFactorySchemaDef().newByPubURIIdxKey();
		key.setRequiredCTenantId( argCTenantId );
		key.setRequiredPublishURI( argPublishURI );
		deleteSchemaDefByPubURIIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaDefByPubURIIdx( ICFSecAuthorization Authorization,
		ICFBamSchemaDefByPubURIIdxKey argKey )
	{
		CFBamBuffSchemaDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaDef> matchSet = new LinkedList<CFBamBuffSchemaDef>();
		Iterator<CFBamBuffSchemaDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaDef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaDefByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffSchemaDef cur;
		LinkedList<CFBamBuffSchemaDef> matchSet = new LinkedList<CFBamBuffSchemaDef>();
		Iterator<CFBamBuffSchemaDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaDef( Authorization, cur );
		}
	}

	@Override
	public void deleteSchemaDefByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteSchemaDefByTenantIdx( Authorization, key );
	}

	@Override
	public void deleteSchemaDefByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffSchemaDef cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffSchemaDef> matchSet = new LinkedList<CFBamBuffSchemaDef>();
		Iterator<CFBamBuffSchemaDef> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffSchemaDef> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteSchemaDef( Authorization, cur );
		}
	}
}
