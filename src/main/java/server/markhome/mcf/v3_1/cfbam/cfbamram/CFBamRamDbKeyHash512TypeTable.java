
// Description: Java 25 in-memory RAM DbIO implementation for DbKeyHash512Type.

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
 *	CFBamRamDbKeyHash512TypeTable in-memory RAM DbIO implementation
 *	for DbKeyHash512Type.
 */
public class CFBamRamDbKeyHash512TypeTable
	implements ICFBamDbKeyHash512TypeTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDbKeyHash512Type > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDbKeyHash512Type >();
	private Map< CFBamBuffDbKeyHash512TypeBySchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDbKeyHash512Type >> dictBySchemaIdx
		= new HashMap< CFBamBuffDbKeyHash512TypeBySchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDbKeyHash512Type >>();

	public CFBamRamDbKeyHash512TypeTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffValue ensureRec(ICFBamValue rec) {
		if (rec == null) {
			return( null );
		}
		else {
			return ((CFBamRamValueTable)(schema.getTableValue())).ensureRec((ICFBamValue)rec);
		}
	}

	@Override
	public ICFBamDbKeyHash512Type createDbKeyHash512Type( ICFSecAuthorization Authorization,
		ICFBamDbKeyHash512Type iBuff )
	{
		final String S_ProcName = "createDbKeyHash512Type";
		
		CFBamBuffDbKeyHash512Type Buff = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Def().createDbKeyHash512Def( Authorization,
			iBuff ));
		ICFBamValue tail = null;
		if( Buff.getClassCode() == ICFBamDbKeyHash512Type.CLASS_CODE ) {
			ICFBamValue[] siblings = schema.getTableValue().readDerivedByScopeIdx( Authorization,
				Buff.getRequiredSchemaDefId() );
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
		CFBamBuffDbKeyHash512TypeBySchemaIdxKey keySchemaIdx = (CFBamBuffDbKeyHash512TypeBySchemaIdxKey)schema.getFactoryDbKeyHash512Type().newBySchemaIdxKey();
		keySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDbKeyHash512Def().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"DbKeyHash512Def",
						"DbKeyHash512Def",
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
						"SchemaDef",
						"SchemaDef",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDbKeyHash512Type > subdictSchemaIdx;
		if( dictBySchemaIdx.containsKey( keySchemaIdx ) ) {
			subdictSchemaIdx = dictBySchemaIdx.get( keySchemaIdx );
		}
		else {
			subdictSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDbKeyHash512Type >();
			dictBySchemaIdx.put( keySchemaIdx, subdictSchemaIdx );
		}
		subdictSchemaIdx.put( pkey, Buff );

		if( tail != null ) {
			int tailClassCode = tail.getClassCode();
			if( tailClassCode == ICFBamValue.CLASS_CODE ) {
				ICFBamValue tailEdit = schema.getFactoryValue().newRec();
				tailEdit.set( (ICFBamValue)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableValue().updateValue( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamAtom.CLASS_CODE ) {
				ICFBamAtom tailEdit = schema.getFactoryAtom().newRec();
				tailEdit.set( (ICFBamAtom)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableAtom().updateAtom( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBlobDef.CLASS_CODE ) {
				ICFBamBlobDef tailEdit = schema.getFactoryBlobDef().newRec();
				tailEdit.set( (ICFBamBlobDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBlobDef().updateBlobDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBlobType.CLASS_CODE ) {
				ICFBamBlobType tailEdit = schema.getFactoryBlobType().newRec();
				tailEdit.set( (ICFBamBlobType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBlobType().updateBlobType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBlobCol.CLASS_CODE ) {
				ICFBamBlobCol tailEdit = schema.getFactoryBlobCol().newRec();
				tailEdit.set( (ICFBamBlobCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBlobCol().updateBlobCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBoolDef.CLASS_CODE ) {
				ICFBamBoolDef tailEdit = schema.getFactoryBoolDef().newRec();
				tailEdit.set( (ICFBamBoolDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBoolDef().updateBoolDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBoolType.CLASS_CODE ) {
				ICFBamBoolType tailEdit = schema.getFactoryBoolType().newRec();
				tailEdit.set( (ICFBamBoolType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBoolType().updateBoolType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBoolCol.CLASS_CODE ) {
				ICFBamBoolCol tailEdit = schema.getFactoryBoolCol().newRec();
				tailEdit.set( (ICFBamBoolCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBoolCol().updateBoolCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDateDef.CLASS_CODE ) {
				ICFBamDateDef tailEdit = schema.getFactoryDateDef().newRec();
				tailEdit.set( (ICFBamDateDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDateDef().updateDateDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDateType.CLASS_CODE ) {
				ICFBamDateType tailEdit = schema.getFactoryDateType().newRec();
				tailEdit.set( (ICFBamDateType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDateType().updateDateType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDateCol.CLASS_CODE ) {
				ICFBamDateCol tailEdit = schema.getFactoryDateCol().newRec();
				tailEdit.set( (ICFBamDateCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDateCol().updateDateCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDoubleDef.CLASS_CODE ) {
				ICFBamDoubleDef tailEdit = schema.getFactoryDoubleDef().newRec();
				tailEdit.set( (ICFBamDoubleDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDoubleDef().updateDoubleDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDoubleType.CLASS_CODE ) {
				ICFBamDoubleType tailEdit = schema.getFactoryDoubleType().newRec();
				tailEdit.set( (ICFBamDoubleType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDoubleType().updateDoubleType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDoubleCol.CLASS_CODE ) {
				ICFBamDoubleCol tailEdit = schema.getFactoryDoubleCol().newRec();
				tailEdit.set( (ICFBamDoubleCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDoubleCol().updateDoubleCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamFloatDef.CLASS_CODE ) {
				ICFBamFloatDef tailEdit = schema.getFactoryFloatDef().newRec();
				tailEdit.set( (ICFBamFloatDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableFloatDef().updateFloatDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamFloatType.CLASS_CODE ) {
				ICFBamFloatType tailEdit = schema.getFactoryFloatType().newRec();
				tailEdit.set( (ICFBamFloatType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableFloatType().updateFloatType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamFloatCol.CLASS_CODE ) {
				ICFBamFloatCol tailEdit = schema.getFactoryFloatCol().newRec();
				tailEdit.set( (ICFBamFloatCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableFloatCol().updateFloatCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt16Def.CLASS_CODE ) {
				ICFBamInt16Def tailEdit = schema.getFactoryInt16Def().newRec();
				tailEdit.set( (ICFBamInt16Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt16Def().updateInt16Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt16Type.CLASS_CODE ) {
				ICFBamInt16Type tailEdit = schema.getFactoryInt16Type().newRec();
				tailEdit.set( (ICFBamInt16Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt16Type().updateInt16Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamId16Gen.CLASS_CODE ) {
				ICFBamId16Gen tailEdit = schema.getFactoryId16Gen().newRec();
				tailEdit.set( (ICFBamId16Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableId16Gen().updateId16Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamEnumDef.CLASS_CODE ) {
				ICFBamEnumDef tailEdit = schema.getFactoryEnumDef().newRec();
				tailEdit.set( (ICFBamEnumDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableEnumDef().updateEnumDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamEnumType.CLASS_CODE ) {
				ICFBamEnumType tailEdit = schema.getFactoryEnumType().newRec();
				tailEdit.set( (ICFBamEnumType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableEnumType().updateEnumType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt16Col.CLASS_CODE ) {
				ICFBamInt16Col tailEdit = schema.getFactoryInt16Col().newRec();
				tailEdit.set( (ICFBamInt16Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt16Col().updateInt16Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt32Def.CLASS_CODE ) {
				ICFBamInt32Def tailEdit = schema.getFactoryInt32Def().newRec();
				tailEdit.set( (ICFBamInt32Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt32Def().updateInt32Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt32Type.CLASS_CODE ) {
				ICFBamInt32Type tailEdit = schema.getFactoryInt32Type().newRec();
				tailEdit.set( (ICFBamInt32Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt32Type().updateInt32Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamId32Gen.CLASS_CODE ) {
				ICFBamId32Gen tailEdit = schema.getFactoryId32Gen().newRec();
				tailEdit.set( (ICFBamId32Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableId32Gen().updateId32Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt32Col.CLASS_CODE ) {
				ICFBamInt32Col tailEdit = schema.getFactoryInt32Col().newRec();
				tailEdit.set( (ICFBamInt32Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt32Col().updateInt32Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt64Def.CLASS_CODE ) {
				ICFBamInt64Def tailEdit = schema.getFactoryInt64Def().newRec();
				tailEdit.set( (ICFBamInt64Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt64Def().updateInt64Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt64Type.CLASS_CODE ) {
				ICFBamInt64Type tailEdit = schema.getFactoryInt64Type().newRec();
				tailEdit.set( (ICFBamInt64Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt64Type().updateInt64Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamId64Gen.CLASS_CODE ) {
				ICFBamId64Gen tailEdit = schema.getFactoryId64Gen().newRec();
				tailEdit.set( (ICFBamId64Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableId64Gen().updateId64Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt64Col.CLASS_CODE ) {
				ICFBamInt64Col tailEdit = schema.getFactoryInt64Col().newRec();
				tailEdit.set( (ICFBamInt64Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt64Col().updateInt64Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokenDef.CLASS_CODE ) {
				ICFBamNmTokenDef tailEdit = schema.getFactoryNmTokenDef().newRec();
				tailEdit.set( (ICFBamNmTokenDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokenType.CLASS_CODE ) {
				ICFBamNmTokenType tailEdit = schema.getFactoryNmTokenType().newRec();
				tailEdit.set( (ICFBamNmTokenType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokenType().updateNmTokenType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokenCol.CLASS_CODE ) {
				ICFBamNmTokenCol tailEdit = schema.getFactoryNmTokenCol().newRec();
				tailEdit.set( (ICFBamNmTokenCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokensDef.CLASS_CODE ) {
				ICFBamNmTokensDef tailEdit = schema.getFactoryNmTokensDef().newRec();
				tailEdit.set( (ICFBamNmTokensDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokensType.CLASS_CODE ) {
				ICFBamNmTokensType tailEdit = schema.getFactoryNmTokensType().newRec();
				tailEdit.set( (ICFBamNmTokensType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokensType().updateNmTokensType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokensCol.CLASS_CODE ) {
				ICFBamNmTokensCol tailEdit = schema.getFactoryNmTokensCol().newRec();
				tailEdit.set( (ICFBamNmTokensCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNumberDef.CLASS_CODE ) {
				ICFBamNumberDef tailEdit = schema.getFactoryNumberDef().newRec();
				tailEdit.set( (ICFBamNumberDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNumberDef().updateNumberDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNumberType.CLASS_CODE ) {
				ICFBamNumberType tailEdit = schema.getFactoryNumberType().newRec();
				tailEdit.set( (ICFBamNumberType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNumberType().updateNumberType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNumberCol.CLASS_CODE ) {
				ICFBamNumberCol tailEdit = schema.getFactoryNumberCol().newRec();
				tailEdit.set( (ICFBamNumberCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNumberCol().updateNumberCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				ICFBamDbKeyHash128Def tailEdit = schema.getFactoryDbKeyHash128Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash128Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				ICFBamDbKeyHash128Col tailEdit = schema.getFactoryDbKeyHash128Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash128Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				ICFBamDbKeyHash128Type tailEdit = schema.getFactoryDbKeyHash128Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash128Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				ICFBamDbKeyHash128Gen tailEdit = schema.getFactoryDbKeyHash128Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash128Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				ICFBamDbKeyHash160Def tailEdit = schema.getFactoryDbKeyHash160Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash160Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				ICFBamDbKeyHash160Col tailEdit = schema.getFactoryDbKeyHash160Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash160Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				ICFBamDbKeyHash160Type tailEdit = schema.getFactoryDbKeyHash160Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash160Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				ICFBamDbKeyHash160Gen tailEdit = schema.getFactoryDbKeyHash160Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash160Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				ICFBamDbKeyHash224Def tailEdit = schema.getFactoryDbKeyHash224Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash224Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				ICFBamDbKeyHash224Col tailEdit = schema.getFactoryDbKeyHash224Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash224Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				ICFBamDbKeyHash224Type tailEdit = schema.getFactoryDbKeyHash224Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash224Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				ICFBamDbKeyHash224Gen tailEdit = schema.getFactoryDbKeyHash224Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash224Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				ICFBamDbKeyHash256Def tailEdit = schema.getFactoryDbKeyHash256Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash256Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				ICFBamDbKeyHash256Col tailEdit = schema.getFactoryDbKeyHash256Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash256Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				ICFBamDbKeyHash256Type tailEdit = schema.getFactoryDbKeyHash256Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash256Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				ICFBamDbKeyHash256Gen tailEdit = schema.getFactoryDbKeyHash256Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash256Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				ICFBamDbKeyHash384Def tailEdit = schema.getFactoryDbKeyHash384Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash384Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				ICFBamDbKeyHash384Col tailEdit = schema.getFactoryDbKeyHash384Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash384Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				ICFBamDbKeyHash384Type tailEdit = schema.getFactoryDbKeyHash384Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash384Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				ICFBamDbKeyHash384Gen tailEdit = schema.getFactoryDbKeyHash384Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash384Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				ICFBamDbKeyHash512Def tailEdit = schema.getFactoryDbKeyHash512Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash512Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				ICFBamDbKeyHash512Col tailEdit = schema.getFactoryDbKeyHash512Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash512Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				ICFBamDbKeyHash512Type tailEdit = schema.getFactoryDbKeyHash512Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash512Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				ICFBamDbKeyHash512Gen tailEdit = schema.getFactoryDbKeyHash512Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash512Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamStringDef.CLASS_CODE ) {
				ICFBamStringDef tailEdit = schema.getFactoryStringDef().newRec();
				tailEdit.set( (ICFBamStringDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableStringDef().updateStringDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamStringType.CLASS_CODE ) {
				ICFBamStringType tailEdit = schema.getFactoryStringType().newRec();
				tailEdit.set( (ICFBamStringType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableStringType().updateStringType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamStringCol.CLASS_CODE ) {
				ICFBamStringCol tailEdit = schema.getFactoryStringCol().newRec();
				tailEdit.set( (ICFBamStringCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableStringCol().updateStringCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZDateDef.CLASS_CODE ) {
				ICFBamTZDateDef tailEdit = schema.getFactoryTZDateDef().newRec();
				tailEdit.set( (ICFBamTZDateDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZDateDef().updateTZDateDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZDateType.CLASS_CODE ) {
				ICFBamTZDateType tailEdit = schema.getFactoryTZDateType().newRec();
				tailEdit.set( (ICFBamTZDateType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZDateType().updateTZDateType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZDateCol.CLASS_CODE ) {
				ICFBamTZDateCol tailEdit = schema.getFactoryTZDateCol().newRec();
				tailEdit.set( (ICFBamTZDateCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZDateCol().updateTZDateCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimeDef.CLASS_CODE ) {
				ICFBamTZTimeDef tailEdit = schema.getFactoryTZTimeDef().newRec();
				tailEdit.set( (ICFBamTZTimeDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimeType.CLASS_CODE ) {
				ICFBamTZTimeType tailEdit = schema.getFactoryTZTimeType().newRec();
				tailEdit.set( (ICFBamTZTimeType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimeType().updateTZTimeType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimeCol.CLASS_CODE ) {
				ICFBamTZTimeCol tailEdit = schema.getFactoryTZTimeCol().newRec();
				tailEdit.set( (ICFBamTZTimeCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				ICFBamTZTimestampDef tailEdit = schema.getFactoryTZTimestampDef().newRec();
				tailEdit.set( (ICFBamTZTimestampDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimestampType.CLASS_CODE ) {
				ICFBamTZTimestampType tailEdit = schema.getFactoryTZTimestampType().newRec();
				tailEdit.set( (ICFBamTZTimestampType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				ICFBamTZTimestampCol tailEdit = schema.getFactoryTZTimestampCol().newRec();
				tailEdit.set( (ICFBamTZTimestampCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTextDef.CLASS_CODE ) {
				ICFBamTextDef tailEdit = schema.getFactoryTextDef().newRec();
				tailEdit.set( (ICFBamTextDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTextDef().updateTextDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTextType.CLASS_CODE ) {
				ICFBamTextType tailEdit = schema.getFactoryTextType().newRec();
				tailEdit.set( (ICFBamTextType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTextType().updateTextType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTextCol.CLASS_CODE ) {
				ICFBamTextCol tailEdit = schema.getFactoryTextCol().newRec();
				tailEdit.set( (ICFBamTextCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTextCol().updateTextCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimeDef.CLASS_CODE ) {
				ICFBamTimeDef tailEdit = schema.getFactoryTimeDef().newRec();
				tailEdit.set( (ICFBamTimeDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimeDef().updateTimeDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimeType.CLASS_CODE ) {
				ICFBamTimeType tailEdit = schema.getFactoryTimeType().newRec();
				tailEdit.set( (ICFBamTimeType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimeType().updateTimeType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimeCol.CLASS_CODE ) {
				ICFBamTimeCol tailEdit = schema.getFactoryTimeCol().newRec();
				tailEdit.set( (ICFBamTimeCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimeCol().updateTimeCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimestampDef.CLASS_CODE ) {
				ICFBamTimestampDef tailEdit = schema.getFactoryTimestampDef().newRec();
				tailEdit.set( (ICFBamTimestampDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimestampDef().updateTimestampDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimestampType.CLASS_CODE ) {
				ICFBamTimestampType tailEdit = schema.getFactoryTimestampType().newRec();
				tailEdit.set( (ICFBamTimestampType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimestampType().updateTimestampType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimestampCol.CLASS_CODE ) {
				ICFBamTimestampCol tailEdit = schema.getFactoryTimestampCol().newRec();
				tailEdit.set( (ICFBamTimestampCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimestampCol().updateTimestampCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTokenDef.CLASS_CODE ) {
				ICFBamTokenDef tailEdit = schema.getFactoryTokenDef().newRec();
				tailEdit.set( (ICFBamTokenDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTokenDef().updateTokenDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTokenType.CLASS_CODE ) {
				ICFBamTokenType tailEdit = schema.getFactoryTokenType().newRec();
				tailEdit.set( (ICFBamTokenType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTokenType().updateTokenType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTokenCol.CLASS_CODE ) {
				ICFBamTokenCol tailEdit = schema.getFactoryTokenCol().newRec();
				tailEdit.set( (ICFBamTokenCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTokenCol().updateTokenCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt16Def.CLASS_CODE ) {
				ICFBamUInt16Def tailEdit = schema.getFactoryUInt16Def().newRec();
				tailEdit.set( (ICFBamUInt16Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt16Def().updateUInt16Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt16Type.CLASS_CODE ) {
				ICFBamUInt16Type tailEdit = schema.getFactoryUInt16Type().newRec();
				tailEdit.set( (ICFBamUInt16Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt16Type().updateUInt16Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt16Col.CLASS_CODE ) {
				ICFBamUInt16Col tailEdit = schema.getFactoryUInt16Col().newRec();
				tailEdit.set( (ICFBamUInt16Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt16Col().updateUInt16Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt32Def.CLASS_CODE ) {
				ICFBamUInt32Def tailEdit = schema.getFactoryUInt32Def().newRec();
				tailEdit.set( (ICFBamUInt32Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt32Def().updateUInt32Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt32Type.CLASS_CODE ) {
				ICFBamUInt32Type tailEdit = schema.getFactoryUInt32Type().newRec();
				tailEdit.set( (ICFBamUInt32Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt32Type().updateUInt32Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt32Col.CLASS_CODE ) {
				ICFBamUInt32Col tailEdit = schema.getFactoryUInt32Col().newRec();
				tailEdit.set( (ICFBamUInt32Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt32Col().updateUInt32Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt64Def.CLASS_CODE ) {
				ICFBamUInt64Def tailEdit = schema.getFactoryUInt64Def().newRec();
				tailEdit.set( (ICFBamUInt64Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt64Def().updateUInt64Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt64Type.CLASS_CODE ) {
				ICFBamUInt64Type tailEdit = schema.getFactoryUInt64Type().newRec();
				tailEdit.set( (ICFBamUInt64Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt64Type().updateUInt64Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt64Col.CLASS_CODE ) {
				ICFBamUInt64Col tailEdit = schema.getFactoryUInt64Col().newRec();
				tailEdit.set( (ICFBamUInt64Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt64Col().updateUInt64Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuidDef.CLASS_CODE ) {
				ICFBamUuidDef tailEdit = schema.getFactoryUuidDef().newRec();
				tailEdit.set( (ICFBamUuidDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuidDef().updateUuidDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuidType.CLASS_CODE ) {
				ICFBamUuidType tailEdit = schema.getFactoryUuidType().newRec();
				tailEdit.set( (ICFBamUuidType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuidType().updateUuidType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuidGen.CLASS_CODE ) {
				ICFBamUuidGen tailEdit = schema.getFactoryUuidGen().newRec();
				tailEdit.set( (ICFBamUuidGen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuidGen().updateUuidGen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuidCol.CLASS_CODE ) {
				ICFBamUuidCol tailEdit = schema.getFactoryUuidCol().newRec();
				tailEdit.set( (ICFBamUuidCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuidCol().updateUuidCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuid6Def.CLASS_CODE ) {
				ICFBamUuid6Def tailEdit = schema.getFactoryUuid6Def().newRec();
				tailEdit.set( (ICFBamUuid6Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuid6Def().updateUuid6Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuid6Type.CLASS_CODE ) {
				ICFBamUuid6Type tailEdit = schema.getFactoryUuid6Type().newRec();
				tailEdit.set( (ICFBamUuid6Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuid6Type().updateUuid6Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuid6Gen.CLASS_CODE ) {
				ICFBamUuid6Gen tailEdit = schema.getFactoryUuid6Gen().newRec();
				tailEdit.set( (ICFBamUuid6Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuid6Col.CLASS_CODE ) {
				ICFBamUuid6Col tailEdit = schema.getFactoryUuid6Col().newRec();
				tailEdit.set( (ICFBamUuid6Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuid6Col().updateUuid6Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTableCol.CLASS_CODE ) {
				ICFBamTableCol tailEdit = schema.getFactoryTableCol().newRec();
				tailEdit.set( (ICFBamTableCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTableCol().updateTableCol( Authorization, tailEdit );
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
			if (classCode == ICFBamDbKeyHash512Type.CLASS_CODE) {
				CFBamBuffDbKeyHash512Type retbuff = ((CFBamBuffDbKeyHash512Type)(schema.getFactoryDbKeyHash512Type().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash512Gen retbuff = ((CFBamBuffDbKeyHash512Gen)(schema.getFactoryDbKeyHash512Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash512Gen)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamDbKeyHash512Type readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDbKeyHash512Type.readDerived";
		ICFBamDbKeyHash512Type buff;
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
	public ICFBamDbKeyHash512Type lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDbKeyHash512Type.lockDerived";
		ICFBamDbKeyHash512Type buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDbKeyHash512Type[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDbKeyHash512Type.readAllDerived";
		ICFBamDbKeyHash512Type[] retList = new ICFBamDbKeyHash512Type[ dictByPKey.values().size() ];
		Iterator< CFBamBuffDbKeyHash512Type > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamDbKeyHash512Type readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByUNameIdx";
		ICFBamValue buff = schema.getTableValue().readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamDbKeyHash512Type ) {
			return( (ICFBamDbKeyHash512Type)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByScopeIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			ScopeId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDbKeyHash512Type ) ) {
					filteredList.add( (ICFBamDbKeyHash512Type)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByDefSchemaIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDbKeyHash512Type ) ) {
					filteredList.add( (ICFBamDbKeyHash512Type)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByPrevIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByPrevIdx( Authorization,
			PrevId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDbKeyHash512Type ) ) {
					filteredList.add( (ICFBamDbKeyHash512Type)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByNextIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByNextIdx( Authorization,
			NextId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDbKeyHash512Type ) ) {
					filteredList.add( (ICFBamDbKeyHash512Type)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type[] readDerivedByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByContPrevIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDbKeyHash512Type ) ) {
					filteredList.add( (ICFBamDbKeyHash512Type)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type[] readDerivedByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByContNextIdx";
		ICFBamValue buffList[] = schema.getTableValue().readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamValue buff;
			ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDbKeyHash512Type ) ) {
					filteredList.add( (ICFBamDbKeyHash512Type)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type[] readDerivedBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamDbKeyHash512Type.readDerivedBySchemaIdx";
		CFBamBuffDbKeyHash512TypeBySchemaIdxKey key = (CFBamBuffDbKeyHash512TypeBySchemaIdxKey)schema.getFactoryDbKeyHash512Type().newBySchemaIdxKey();

		key.setRequiredSchemaDefId( SchemaDefId );
		ICFBamDbKeyHash512Type[] recArray;
		if( dictBySchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDbKeyHash512Type > subdictSchemaIdx
				= dictBySchemaIdx.get( key );
			recArray = new ICFBamDbKeyHash512Type[ subdictSchemaIdx.size() ];
			Iterator< CFBamBuffDbKeyHash512Type > iter = subdictSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDbKeyHash512Type > subdictSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDbKeyHash512Type >();
			dictBySchemaIdx.put( key, subdictSchemaIdx );
			recArray = new ICFBamDbKeyHash512Type[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamDbKeyHash512Type readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByIdIdx() ";
		ICFBamDbKeyHash512Type buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDbKeyHash512Type readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDbKeyHash512Type.readRec";
		ICFBamDbKeyHash512Type buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDbKeyHash512Type.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDbKeyHash512Type lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamDbKeyHash512Type buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDbKeyHash512Type.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamDbKeyHash512Type[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDbKeyHash512Type.readAllRec";
		ICFBamDbKeyHash512Type buff;
		ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
		ICFBamDbKeyHash512Type[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDbKeyHash512Type.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
	}

	@Override
	public ICFBamDbKeyHash512Type readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readRecByIdIdx() ";
		ICFBamDbKeyHash512Type buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
			return( (ICFBamDbKeyHash512Type)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readRecByUNameIdx() ";
		ICFBamDbKeyHash512Type buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
			return( (ICFBamDbKeyHash512Type)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamDbKeyHash512Type[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByScopeIdx() ";
		ICFBamDbKeyHash512Type buff;
		ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
		ICFBamDbKeyHash512Type[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDbKeyHash512Type)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
	}

	@Override
	public ICFBamDbKeyHash512Type[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByDefSchemaIdx() ";
		ICFBamDbKeyHash512Type buff;
		ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
		ICFBamDbKeyHash512Type[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDbKeyHash512Type)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
	}

	@Override
	public ICFBamDbKeyHash512Type[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByPrevIdx() ";
		ICFBamDbKeyHash512Type buff;
		ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
		ICFBamDbKeyHash512Type[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDbKeyHash512Type)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
	}

	@Override
	public ICFBamDbKeyHash512Type[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByNextIdx() ";
		ICFBamDbKeyHash512Type buff;
		ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
		ICFBamDbKeyHash512Type[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDbKeyHash512Type)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
	}

	@Override
	public ICFBamDbKeyHash512Type[] readRecByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByContPrevIdx() ";
		ICFBamDbKeyHash512Type buff;
		ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
		ICFBamDbKeyHash512Type[] buffList = readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDbKeyHash512Type)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
	}

	@Override
	public ICFBamDbKeyHash512Type[] readRecByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByContNextIdx() ";
		ICFBamDbKeyHash512Type buff;
		ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
		ICFBamDbKeyHash512Type[] buffList = readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDbKeyHash512Type)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
	}

	@Override
	public ICFBamDbKeyHash512Type[] readRecBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamDbKeyHash512Type.readRecBySchemaIdx() ";
		ICFBamDbKeyHash512Type buff;
		ArrayList<ICFBamDbKeyHash512Type> filteredList = new ArrayList<ICFBamDbKeyHash512Type>();
		ICFBamDbKeyHash512Type[] buffList = readDerivedBySchemaIdx( Authorization,
			SchemaDefId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDbKeyHash512Type.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDbKeyHash512Type)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDbKeyHash512Type[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamDbKeyHash512Type moveRecUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecUp";

		ICFBamValue grandprev = null;
		ICFBamValue prev = null;
		ICFBamValue cur = null;
		ICFBamValue next = null;

		cur = schema.getTableValue().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffDbKeyHash512Type)cur );
		}

		prev = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() ));
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
			}
		}

		int classCode = prev.getClassCode();
		ICFBamValue newInstance;
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffValue editPrev = (CFBamBuffValue)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffValue editCur = (CFBamBuffValue)newInstance;
		editCur.set( cur );

		CFBamBuffValue editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = (CFBamBuffValue)newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffValue editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext = (CFBamBuffValue)newInstance;
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
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editGrandprev );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editGrandprev );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editGrandprev );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editGrandprev );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editGrandprev );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editGrandprev );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editGrandprev );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editGrandprev );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editGrandprev );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editGrandprev );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editGrandprev );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editGrandprev );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editGrandprev );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editGrandprev );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editGrandprev );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editGrandprev );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editGrandprev );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editGrandprev );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editGrandprev );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editGrandprev );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editGrandprev );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editGrandprev );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editGrandprev );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editGrandprev );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editGrandprev );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editGrandprev );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editGrandprev );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editGrandprev );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editGrandprev );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editGrandprev );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editGrandprev );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editGrandprev );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editGrandprev );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editGrandprev );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editGrandprev );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editGrandprev );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editGrandprev );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editGrandprev );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editGrandprev );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editGrandprev );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editGrandprev );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editGrandprev );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editGrandprev );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editGrandprev );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editGrandprev );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editGrandprev );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editGrandprev );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editGrandprev );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editGrandprev );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editGrandprev );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editGrandprev );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editGrandprev );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editGrandprev );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editGrandprev );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editGrandprev );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editGrandprev );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editGrandprev );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editGrandprev );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editGrandprev );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editGrandprev );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editGrandprev );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editGrandprev );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editGrandprev );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editGrandprev );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editGrandprev );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editGrandprev );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editGrandprev );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editGrandprev );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editGrandprev );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editGrandprev );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editGrandprev );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editGrandprev );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editGrandprev );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editGrandprev );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editGrandprev );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editGrandprev );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editGrandprev );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editGrandprev );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editGrandprev );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editGrandprev );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editGrandprev );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editGrandprev );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editPrev );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editPrev );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editPrev );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editPrev );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editPrev );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editPrev );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editPrev );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editPrev );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editPrev );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editPrev );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editPrev );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editPrev );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editPrev );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editPrev );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editPrev );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editPrev );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editPrev );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editPrev );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editPrev );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editPrev );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editPrev );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editPrev );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editPrev );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editPrev );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editPrev );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editPrev );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editPrev );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editPrev );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editPrev );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editPrev );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editPrev );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editPrev );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editPrev );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editPrev );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editPrev );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editPrev );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editPrev );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editPrev );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editPrev );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editPrev );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editPrev );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editPrev );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editPrev );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editPrev );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editPrev );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editPrev );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editPrev );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editPrev );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editPrev );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editPrev );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editPrev );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editPrev );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editPrev );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editPrev );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editPrev );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editPrev );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editPrev );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editPrev );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editPrev );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editPrev );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editPrev );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editPrev );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editPrev );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editPrev );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editPrev );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editPrev );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editPrev );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editPrev );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editPrev );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editPrev );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editPrev );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editPrev );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editPrev );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editPrev );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editPrev );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editPrev );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editPrev );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editPrev );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editPrev );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editPrev );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editCur );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editCur );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editCur );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editCur );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editCur );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editCur );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editCur );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editCur );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editCur );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editCur );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editCur );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editCur );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editCur );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editCur );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editCur );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editCur );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editCur );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editCur );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editCur );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editCur );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editCur );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editCur );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editCur );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editCur );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editCur );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editCur );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editCur );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editCur );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editCur );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editCur );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editCur );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editCur );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editCur );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editCur );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editCur );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editCur );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editCur );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editCur );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editCur );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editCur );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editCur );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editCur );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editCur );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editCur );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editCur );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editCur );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editCur );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editCur );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editCur );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editCur );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editCur );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editCur );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editCur );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editCur );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editCur );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editCur );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editCur );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editCur );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editCur );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editCur );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editCur );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editCur );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editCur );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editCur );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editCur );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editCur );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editCur );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editCur );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editCur );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editCur );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editCur );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editCur );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editCur );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editCur );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editCur );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editCur );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editCur );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editCur );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editCur );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editCur );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editCur );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editNext );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editNext );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editNext );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editNext );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editNext );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editNext );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editNext );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editNext );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editNext );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editNext );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editNext );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editNext );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editNext );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editNext );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editNext );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editNext );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editNext );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editNext );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editNext );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editNext );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editNext );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editNext );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editNext );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editNext );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editNext );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editNext );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editNext );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editNext );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editNext );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editNext );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editNext );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editNext );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editNext );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editNext );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editNext );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editNext );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editNext );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editNext );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editNext );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editNext );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editNext );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editNext );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editNext );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editNext );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editNext );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editNext );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editNext );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editNext );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editNext );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editNext );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editNext );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editNext );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editNext );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editNext );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editNext );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editNext );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editNext );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editNext );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editNext );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editNext );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editNext );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editNext );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editNext );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editNext );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editNext );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editNext );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editNext );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editNext );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editNext );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editNext );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editNext );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editNext );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editNext );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editNext );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editNext );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editNext );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editNext );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editNext );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editNext );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editNext );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffDbKeyHash512Type)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamDbKeyHash512Type moveRecDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveRecDown";

		CFBamBuffValue prev = null;
		CFBamBuffValue cur = null;
		CFBamBuffValue next = null;
		CFBamBuffValue grandnext = null;

		cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx(Authorization, Id));
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffDbKeyHash512Type)cur );
		}

		next = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() ));
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx(Authorization, next.getOptionalNextId() ));
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() ));
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(), S_ProcName, "Could not locate object.prev" );
			}
		}

		int classCode = cur.getClassCode();
		ICFBamValue newInstance;
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffValue editCur = (CFBamBuffValue)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffValue editNext = (CFBamBuffValue)newInstance;
		editNext.set( next );

		CFBamBuffValue editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = (CFBamBuffValue)newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffValue editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev = (CFBamBuffValue)newInstance;
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
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editPrev );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editPrev );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editPrev );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editPrev );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editPrev );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editPrev );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editPrev );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editPrev );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editPrev );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editPrev );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editPrev );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editPrev );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editPrev );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editPrev );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editPrev );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editPrev );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editPrev );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editPrev );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editPrev );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editPrev );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editPrev );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editPrev );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editPrev );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editPrev );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editPrev );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editPrev );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editPrev );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editPrev );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editPrev );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editPrev );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editPrev );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editPrev );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editPrev );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editPrev );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editPrev );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editPrev );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editPrev );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editPrev );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editPrev );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editPrev );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editPrev );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editPrev );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editPrev );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editPrev );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editPrev );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editPrev );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editPrev );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editPrev );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editPrev );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editPrev );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editPrev );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editPrev );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editPrev );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editPrev );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editPrev );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editPrev );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editPrev );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editPrev );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editPrev );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editPrev );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editPrev );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editPrev );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editPrev );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editPrev );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editPrev );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editPrev );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editPrev );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editPrev );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editPrev );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editPrev );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editPrev );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editPrev );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editPrev );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editPrev );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editPrev );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editPrev );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editPrev );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editPrev );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editPrev );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editPrev );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editCur );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editCur );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editCur );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editCur );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editCur );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editCur );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editCur );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editCur );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editCur );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editCur );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editCur );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editCur );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editCur );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editCur );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editCur );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editCur );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editCur );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editCur );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editCur );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editCur );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editCur );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editCur );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editCur );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editCur );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editCur );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editCur );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editCur );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editCur );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editCur );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editCur );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editCur );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editCur );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editCur );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editCur );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editCur );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editCur );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editCur );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editCur );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editCur );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editCur );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editCur );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editCur );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editCur );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editCur );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editCur );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editCur );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editCur );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editCur );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editCur );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editCur );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editCur );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editCur );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editCur );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editCur );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editCur );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editCur );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editCur );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editCur );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editCur );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editCur );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editCur );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editCur );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editCur );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editCur );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editCur );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editCur );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editCur );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editCur );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editCur );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editCur );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editCur );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editCur );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editCur );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editCur );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editCur );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editCur );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editCur );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editCur );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editCur );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editCur );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editCur );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editCur );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editNext );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editNext );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editNext );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editNext );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editNext );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editNext );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editNext );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editNext );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editNext );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editNext );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editNext );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editNext );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editNext );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editNext );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editNext );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editNext );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editNext );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editNext );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editNext );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editNext );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editNext );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editNext );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editNext );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editNext );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editNext );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editNext );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editNext );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editNext );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editNext );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editNext );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editNext );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editNext );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editNext );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editNext );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editNext );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editNext );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editNext );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editNext );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editNext );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editNext );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editNext );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editNext );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editNext );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editNext );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editNext );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editNext );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editNext );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editNext );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editNext );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editNext );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editNext );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editNext );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editNext );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editNext );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editNext );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editNext );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editNext );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editNext );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editNext );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editNext );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editNext );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editNext );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editNext );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editNext );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editNext );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editNext );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editNext );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editNext );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editNext );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editNext );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editNext );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editNext );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editNext );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editNext );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editNext );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editNext );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editNext );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editNext );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editNext );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editNext );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editGrandnext );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editGrandnext );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editGrandnext );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editGrandnext );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editGrandnext );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editGrandnext );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editGrandnext );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editGrandnext );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editGrandnext );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editGrandnext );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editGrandnext );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editGrandnext );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editGrandnext );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editGrandnext );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editGrandnext );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editGrandnext );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editGrandnext );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editGrandnext );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editGrandnext );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editGrandnext );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editGrandnext );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editGrandnext );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editGrandnext );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editGrandnext );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editGrandnext );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editGrandnext );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editGrandnext );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editGrandnext );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editGrandnext );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editGrandnext );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editGrandnext );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editGrandnext );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editGrandnext );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editGrandnext );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editGrandnext );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editGrandnext );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editGrandnext );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editGrandnext );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editGrandnext );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editGrandnext );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editGrandnext );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editGrandnext );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editGrandnext );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editGrandnext );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editGrandnext );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editGrandnext );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editGrandnext );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editGrandnext );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editGrandnext );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editGrandnext );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editGrandnext );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editGrandnext );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editGrandnext );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editGrandnext );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editGrandnext );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editGrandnext );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editGrandnext );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editGrandnext );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editGrandnext );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editGrandnext );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editGrandnext );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editGrandnext );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editGrandnext );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editGrandnext );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editGrandnext );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editGrandnext );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editGrandnext );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editGrandnext );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editGrandnext );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editGrandnext );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editGrandnext );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editGrandnext );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editGrandnext );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editGrandnext );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editGrandnext );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editGrandnext );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editGrandnext );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editGrandnext );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editGrandnext );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editGrandnext );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editGrandnext );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editGrandnext );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffDbKeyHash512Type)editCur );
	}

	public ICFBamDbKeyHash512Type updateDbKeyHash512Type( ICFSecAuthorization Authorization,
		ICFBamDbKeyHash512Type iBuff )
	{
		CFBamBuffDbKeyHash512Type Buff = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffDbKeyHash512Type existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDbKeyHash512Type",
				"Existing record not found",
				"Existing record not found",
				"DbKeyHash512Type",
				"DbKeyHash512Type",
				pkey );
		}
		CFBamBuffDbKeyHash512TypeBySchemaIdxKey existingKeySchemaIdx = (CFBamBuffDbKeyHash512TypeBySchemaIdxKey)schema.getFactoryDbKeyHash512Type().newBySchemaIdxKey();
		existingKeySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamBuffDbKeyHash512TypeBySchemaIdxKey newKeySchemaIdx = (CFBamBuffDbKeyHash512TypeBySchemaIdxKey)schema.getFactoryDbKeyHash512Type().newBySchemaIdxKey();
		newKeySchemaIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDbKeyHash512Def().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDbKeyHash512Type",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"DbKeyHash512Def",
						"DbKeyHash512Def",
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
						"updateDbKeyHash512Type",
						"Container",
						"Container",
						"SchemaDef",
						"SchemaDef",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffDbKeyHash512Type > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDbKeyHash512Type >();
			dictBySchemaIdx.put( newKeySchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteDbKeyHash512Type( ICFSecAuthorization Authorization,
		ICFBamDbKeyHash512Type iBuff )
	{
		final String S_ProcName = "CFBamRamDbKeyHash512TypeTable.deleteDbKeyHash512Type() ";
		CFBamBuffDbKeyHash512Type Buff = (CFBamBuffDbKeyHash512Type)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffDbKeyHash512Type existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDbKeyHash512Type",
				pkey );
		}
		CFLibDbKeyHash256 varSchemaDefId = existing.getRequiredSchemaDefId();
		CFBamBuffSchemaDef container = (CFBamBuffSchemaDef)(schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
			varSchemaDefId ));
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamBuffValue prev = null;
		if( ( prevId != null ) )
		{
			prev = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				prevId ));
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamBuffValue editPrev;
			classCode = prev.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryValue().newRec());
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryAtom().newRec());
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryBlobDef().newRec());
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryBlobType().newRec());
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryBlobCol().newRec());
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryBoolDef().newRec());
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryBoolType().newRec());
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryBoolCol().newRec());
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDateDef().newRec());
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDateType().newRec());
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDateCol().newRec());
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDoubleDef().newRec());
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDoubleType().newRec());
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDoubleCol().newRec());
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryFloatDef().newRec());
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryFloatType().newRec());
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryFloatCol().newRec());
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt16Def().newRec());
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt16Type().newRec());
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryId16Gen().newRec());
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryEnumDef().newRec());
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryEnumType().newRec());
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt16Col().newRec());
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt32Def().newRec());
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt32Type().newRec());
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryId32Gen().newRec());
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt32Col().newRec());
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt64Def().newRec());
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt64Type().newRec());
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryId64Gen().newRec());
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryInt64Col().newRec());
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNmTokenDef().newRec());
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNmTokenType().newRec());
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNmTokenCol().newRec());
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNmTokensDef().newRec());
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNmTokensType().newRec());
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNmTokensCol().newRec());
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNumberDef().newRec());
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNumberType().newRec());
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryNumberCol().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash128Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash128Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash128Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash128Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash160Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash160Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash160Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash160Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash224Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash224Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash224Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash224Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash256Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash256Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash256Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash256Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash384Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash384Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash384Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash384Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash512Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash512Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash512Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryDbKeyHash512Gen().newRec());
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryStringDef().newRec());
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryStringType().newRec());
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryStringCol().newRec());
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZDateDef().newRec());
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZDateType().newRec());
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZDateCol().newRec());
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZTimeDef().newRec());
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZTimeType().newRec());
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZTimeCol().newRec());
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZTimestampDef().newRec());
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZTimestampType().newRec());
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTZTimestampCol().newRec());
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTextDef().newRec());
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTextType().newRec());
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTextCol().newRec());
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTimeDef().newRec());
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTimeType().newRec());
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTimeCol().newRec());
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTimestampDef().newRec());
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTimestampType().newRec());
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTimestampCol().newRec());
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTokenDef().newRec());
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTokenType().newRec());
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTokenCol().newRec());
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt16Def().newRec());
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt16Type().newRec());
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt16Col().newRec());
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt32Def().newRec());
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt32Type().newRec());
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt32Col().newRec());
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt64Def().newRec());
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt64Type().newRec());
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUInt64Col().newRec());
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUuidDef().newRec());
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUuidType().newRec());
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUuidGen().newRec());
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUuidCol().newRec());
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUuid6Def().newRec());
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUuid6Type().newRec());
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUuid6Gen().newRec());
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryUuid6Col().newRec());
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getFactoryTableCol().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalLookupNext(nextId);
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editPrev );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editPrev );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editPrev );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editPrev );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editPrev );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editPrev );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editPrev );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editPrev );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editPrev );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editPrev );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editPrev );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editPrev );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editPrev );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editPrev );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editPrev );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editPrev );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editPrev );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editPrev );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editPrev );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editPrev );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editPrev );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editPrev );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editPrev );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editPrev );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editPrev );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editPrev );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editPrev );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editPrev );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editPrev );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editPrev );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editPrev );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editPrev );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editPrev );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editPrev );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editPrev );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editPrev );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editPrev );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editPrev );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editPrev );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editPrev );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editPrev );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editPrev );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editPrev );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editPrev );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editPrev );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editPrev );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editPrev );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editPrev );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editPrev );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editPrev );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editPrev );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editPrev );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editPrev );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editPrev );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editPrev );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editPrev );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editPrev );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editPrev );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editPrev );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editPrev );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editPrev );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editPrev );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editPrev );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editPrev );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editPrev );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editPrev );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editPrev );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editPrev );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editPrev );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editPrev );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editPrev );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editPrev );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editPrev );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editPrev );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editPrev );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editPrev );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editPrev );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editPrev );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editPrev );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editPrev );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editPrev );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editPrev );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffValue next = null;
		if( ( nextId != null ) )
		{
			next = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				nextId ));
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamBuffValue editNext;
			classCode = next.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryValue().newRec());
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryAtom().newRec());
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryBlobDef().newRec());
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryBlobType().newRec());
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryBlobCol().newRec());
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryBoolDef().newRec());
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryBoolType().newRec());
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryBoolCol().newRec());
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDateDef().newRec());
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDateType().newRec());
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDateCol().newRec());
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDoubleDef().newRec());
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDoubleType().newRec());
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDoubleCol().newRec());
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryFloatDef().newRec());
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryFloatType().newRec());
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryFloatCol().newRec());
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt16Def().newRec());
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt16Type().newRec());
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryId16Gen().newRec());
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryEnumDef().newRec());
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryEnumType().newRec());
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt16Col().newRec());
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt32Def().newRec());
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt32Type().newRec());
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryId32Gen().newRec());
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt32Col().newRec());
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt64Def().newRec());
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt64Type().newRec());
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryId64Gen().newRec());
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryInt64Col().newRec());
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNmTokenDef().newRec());
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNmTokenType().newRec());
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNmTokenCol().newRec());
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNmTokensDef().newRec());
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNmTokensType().newRec());
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNmTokensCol().newRec());
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNumberDef().newRec());
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNumberType().newRec());
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryNumberCol().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash128Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash128Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash128Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash128Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash160Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash160Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash160Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash160Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash224Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash224Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash224Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash224Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash256Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash256Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash256Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash256Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash384Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash384Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash384Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash384Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash512Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash512Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash512Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryDbKeyHash512Gen().newRec());
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryStringDef().newRec());
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryStringType().newRec());
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryStringCol().newRec());
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZDateDef().newRec());
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZDateType().newRec());
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZDateCol().newRec());
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZTimeDef().newRec());
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZTimeType().newRec());
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZTimeCol().newRec());
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZTimestampDef().newRec());
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZTimestampType().newRec());
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTZTimestampCol().newRec());
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTextDef().newRec());
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTextType().newRec());
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTextCol().newRec());
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTimeDef().newRec());
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTimeType().newRec());
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTimeCol().newRec());
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTimestampDef().newRec());
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTimestampType().newRec());
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTimestampCol().newRec());
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTokenDef().newRec());
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTokenType().newRec());
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTokenCol().newRec());
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt16Def().newRec());
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt16Type().newRec());
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt16Col().newRec());
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt32Def().newRec());
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt32Type().newRec());
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt32Col().newRec());
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt64Def().newRec());
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt64Type().newRec());
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUInt64Col().newRec());
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUuidDef().newRec());
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUuidType().newRec());
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUuidGen().newRec());
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUuidCol().newRec());
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUuid6Def().newRec());
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUuid6Type().newRec());
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUuid6Gen().newRec());
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryUuid6Col().newRec());
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getFactoryTableCol().newRec());
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalLookupPrev(prevId);
			if( classCode == ICFBamValue.CLASS_CODE ) {
				schema.getTableValue().updateValue( Authorization, editNext );
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				schema.getTableAtom().updateAtom( Authorization, (CFBamBuffAtom)editNext );
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				schema.getTableBlobDef().updateBlobDef( Authorization, (CFBamBuffBlobDef)editNext );
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				schema.getTableBlobType().updateBlobType( Authorization, (CFBamBuffBlobType)editNext );
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				schema.getTableBlobCol().updateBlobCol( Authorization, (CFBamBuffBlobCol)editNext );
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				schema.getTableBoolDef().updateBoolDef( Authorization, (CFBamBuffBoolDef)editNext );
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				schema.getTableBoolType().updateBoolType( Authorization, (CFBamBuffBoolType)editNext );
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				schema.getTableBoolCol().updateBoolCol( Authorization, (CFBamBuffBoolCol)editNext );
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				schema.getTableDateDef().updateDateDef( Authorization, (CFBamBuffDateDef)editNext );
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				schema.getTableDateType().updateDateType( Authorization, (CFBamBuffDateType)editNext );
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				schema.getTableDateCol().updateDateCol( Authorization, (CFBamBuffDateCol)editNext );
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				schema.getTableDoubleDef().updateDoubleDef( Authorization, (CFBamBuffDoubleDef)editNext );
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				schema.getTableDoubleType().updateDoubleType( Authorization, (CFBamBuffDoubleType)editNext );
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				schema.getTableDoubleCol().updateDoubleCol( Authorization, (CFBamBuffDoubleCol)editNext );
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				schema.getTableFloatDef().updateFloatDef( Authorization, (CFBamBuffFloatDef)editNext );
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				schema.getTableFloatType().updateFloatType( Authorization, (CFBamBuffFloatType)editNext );
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				schema.getTableFloatCol().updateFloatCol( Authorization, (CFBamBuffFloatCol)editNext );
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				schema.getTableInt16Def().updateInt16Def( Authorization, (CFBamBuffInt16Def)editNext );
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				schema.getTableInt16Type().updateInt16Type( Authorization, (CFBamBuffInt16Type)editNext );
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				schema.getTableId16Gen().updateId16Gen( Authorization, (CFBamBuffId16Gen)editNext );
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				schema.getTableEnumDef().updateEnumDef( Authorization, (CFBamBuffEnumDef)editNext );
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				schema.getTableEnumType().updateEnumType( Authorization, (CFBamBuffEnumType)editNext );
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				schema.getTableInt16Col().updateInt16Col( Authorization, (CFBamBuffInt16Col)editNext );
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				schema.getTableInt32Def().updateInt32Def( Authorization, (CFBamBuffInt32Def)editNext );
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				schema.getTableInt32Type().updateInt32Type( Authorization, (CFBamBuffInt32Type)editNext );
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				schema.getTableId32Gen().updateId32Gen( Authorization, (CFBamBuffId32Gen)editNext );
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				schema.getTableInt32Col().updateInt32Col( Authorization, (CFBamBuffInt32Col)editNext );
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				schema.getTableInt64Def().updateInt64Def( Authorization, (CFBamBuffInt64Def)editNext );
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				schema.getTableInt64Type().updateInt64Type( Authorization, (CFBamBuffInt64Type)editNext );
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				schema.getTableId64Gen().updateId64Gen( Authorization, (CFBamBuffId64Gen)editNext );
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				schema.getTableInt64Col().updateInt64Col( Authorization, (CFBamBuffInt64Col)editNext );
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, (CFBamBuffNmTokenDef)editNext );
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				schema.getTableNmTokenType().updateNmTokenType( Authorization, (CFBamBuffNmTokenType)editNext );
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, (CFBamBuffNmTokenCol)editNext );
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, (CFBamBuffNmTokensDef)editNext );
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				schema.getTableNmTokensType().updateNmTokensType( Authorization, (CFBamBuffNmTokensType)editNext );
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, (CFBamBuffNmTokensCol)editNext );
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				schema.getTableNumberDef().updateNumberDef( Authorization, (CFBamBuffNumberDef)editNext );
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				schema.getTableNumberType().updateNumberType( Authorization, (CFBamBuffNumberType)editNext );
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				schema.getTableNumberCol().updateNumberCol( Authorization, (CFBamBuffNumberCol)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, (CFBamBuffDbKeyHash128Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, (CFBamBuffDbKeyHash128Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, (CFBamBuffDbKeyHash128Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, (CFBamBuffDbKeyHash128Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, (CFBamBuffDbKeyHash160Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, (CFBamBuffDbKeyHash160Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, (CFBamBuffDbKeyHash160Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, (CFBamBuffDbKeyHash160Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, (CFBamBuffDbKeyHash224Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, (CFBamBuffDbKeyHash224Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, (CFBamBuffDbKeyHash224Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, (CFBamBuffDbKeyHash224Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, (CFBamBuffDbKeyHash256Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, (CFBamBuffDbKeyHash256Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, (CFBamBuffDbKeyHash256Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, (CFBamBuffDbKeyHash256Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, (CFBamBuffDbKeyHash384Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, (CFBamBuffDbKeyHash384Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, (CFBamBuffDbKeyHash384Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, (CFBamBuffDbKeyHash384Gen)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, (CFBamBuffDbKeyHash512Def)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, (CFBamBuffDbKeyHash512Col)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, (CFBamBuffDbKeyHash512Type)editNext );
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, (CFBamBuffDbKeyHash512Gen)editNext );
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				schema.getTableStringDef().updateStringDef( Authorization, (CFBamBuffStringDef)editNext );
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				schema.getTableStringType().updateStringType( Authorization, (CFBamBuffStringType)editNext );
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				schema.getTableStringCol().updateStringCol( Authorization, (CFBamBuffStringCol)editNext );
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				schema.getTableTZDateDef().updateTZDateDef( Authorization, (CFBamBuffTZDateDef)editNext );
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				schema.getTableTZDateType().updateTZDateType( Authorization, (CFBamBuffTZDateType)editNext );
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				schema.getTableTZDateCol().updateTZDateCol( Authorization, (CFBamBuffTZDateCol)editNext );
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, (CFBamBuffTZTimeDef)editNext );
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				schema.getTableTZTimeType().updateTZTimeType( Authorization, (CFBamBuffTZTimeType)editNext );
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, (CFBamBuffTZTimeCol)editNext );
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, (CFBamBuffTZTimestampDef)editNext );
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, (CFBamBuffTZTimestampType)editNext );
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, (CFBamBuffTZTimestampCol)editNext );
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				schema.getTableTextDef().updateTextDef( Authorization, (CFBamBuffTextDef)editNext );
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				schema.getTableTextType().updateTextType( Authorization, (CFBamBuffTextType)editNext );
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				schema.getTableTextCol().updateTextCol( Authorization, (CFBamBuffTextCol)editNext );
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				schema.getTableTimeDef().updateTimeDef( Authorization, (CFBamBuffTimeDef)editNext );
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				schema.getTableTimeType().updateTimeType( Authorization, (CFBamBuffTimeType)editNext );
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				schema.getTableTimeCol().updateTimeCol( Authorization, (CFBamBuffTimeCol)editNext );
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				schema.getTableTimestampDef().updateTimestampDef( Authorization, (CFBamBuffTimestampDef)editNext );
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				schema.getTableTimestampType().updateTimestampType( Authorization, (CFBamBuffTimestampType)editNext );
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				schema.getTableTimestampCol().updateTimestampCol( Authorization, (CFBamBuffTimestampCol)editNext );
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				schema.getTableTokenDef().updateTokenDef( Authorization, (CFBamBuffTokenDef)editNext );
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				schema.getTableTokenType().updateTokenType( Authorization, (CFBamBuffTokenType)editNext );
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				schema.getTableTokenCol().updateTokenCol( Authorization, (CFBamBuffTokenCol)editNext );
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				schema.getTableUInt16Def().updateUInt16Def( Authorization, (CFBamBuffUInt16Def)editNext );
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				schema.getTableUInt16Type().updateUInt16Type( Authorization, (CFBamBuffUInt16Type)editNext );
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				schema.getTableUInt16Col().updateUInt16Col( Authorization, (CFBamBuffUInt16Col)editNext );
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				schema.getTableUInt32Def().updateUInt32Def( Authorization, (CFBamBuffUInt32Def)editNext );
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				schema.getTableUInt32Type().updateUInt32Type( Authorization, (CFBamBuffUInt32Type)editNext );
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				schema.getTableUInt32Col().updateUInt32Col( Authorization, (CFBamBuffUInt32Col)editNext );
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				schema.getTableUInt64Def().updateUInt64Def( Authorization, (CFBamBuffUInt64Def)editNext );
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				schema.getTableUInt64Type().updateUInt64Type( Authorization, (CFBamBuffUInt64Type)editNext );
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				schema.getTableUInt64Col().updateUInt64Col( Authorization, (CFBamBuffUInt64Col)editNext );
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				schema.getTableUuidDef().updateUuidDef( Authorization, (CFBamBuffUuidDef)editNext );
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				schema.getTableUuidType().updateUuidType( Authorization, (CFBamBuffUuidType)editNext );
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				schema.getTableUuidGen().updateUuidGen( Authorization, (CFBamBuffUuidGen)editNext );
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				schema.getTableUuidCol().updateUuidCol( Authorization, (CFBamBuffUuidCol)editNext );
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				schema.getTableUuid6Def().updateUuid6Def( Authorization, (CFBamBuffUuid6Def)editNext );
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				schema.getTableUuid6Type().updateUuid6Type( Authorization, (CFBamBuffUuid6Type)editNext );
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, (CFBamBuffUuid6Gen)editNext );
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				schema.getTableUuid6Col().updateUuid6Col( Authorization, (CFBamBuffUuid6Col)editNext );
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				schema.getTableTableCol().updateTableCol( Authorization, (CFBamBuffTableCol)editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckReferencingTableCols[] = schema.getTableTableCol().readDerivedByDataIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckReferencingTableCols.length > 0 ) {
			schema.getTableTableCol().deleteTableColByDataIdx( Authorization,
						existing.getRequiredId() );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckReferencingIndexCols[] = schema.getTableIndexCol().readDerivedByColIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckReferencingIndexCols.length > 0 ) {
			schema.getTableIndexCol().deleteIndexColByColIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffDbKeyHash512TypeBySchemaIdxKey keySchemaIdx = (CFBamBuffDbKeyHash512TypeBySchemaIdxKey)schema.getFactoryDbKeyHash512Type().newBySchemaIdxKey();
		keySchemaIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		// Validate reverse foreign keys

		if( schema.getTableDbKeyHash512Gen().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteDbKeyHash512Type",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"DbKeyHash512Gen",
				"DbKeyHash512Gen",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDbKeyHash512Type > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaIdx.get( keySchemaIdx );
		subdict.remove( pkey );

		schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization,
			Buff );
	}
	@Override
	public void deleteDbKeyHash512TypeBySchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId )
	{
		CFBamBuffDbKeyHash512TypeBySchemaIdxKey key = (CFBamBuffDbKeyHash512TypeBySchemaIdxKey)schema.getFactoryDbKeyHash512Type().newBySchemaIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		deleteDbKeyHash512TypeBySchemaIdx( Authorization, key );
	}

	@Override
	public void deleteDbKeyHash512TypeBySchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDbKeyHash512TypeBySchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeBySchemaIdx";
		CFBamBuffDbKeyHash512Type cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDbKeyHash512TypeByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffDbKeyHash512Type cur;
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDbKeyHash512TypeByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffValueByUNameIdxKey key = (CFBamBuffValueByUNameIdxKey)schema.getFactoryValue().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteDbKeyHash512TypeByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteDbKeyHash512TypeByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamValueByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeByUNameIdx";
		CFBamBuffDbKeyHash512Type cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDbKeyHash512TypeByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffValueByScopeIdxKey key = (CFBamBuffValueByScopeIdxKey)schema.getFactoryValue().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteDbKeyHash512TypeByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteDbKeyHash512TypeByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamValueByScopeIdxKey argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeByScopeIdx";
		CFBamBuffDbKeyHash512Type cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDbKeyHash512TypeByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffValueByDefSchemaIdxKey key = (CFBamBuffValueByDefSchemaIdxKey)schema.getFactoryValue().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDbKeyHash512TypeByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteDbKeyHash512TypeByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamValueByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeByDefSchemaIdx";
		CFBamBuffDbKeyHash512Type cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDbKeyHash512TypeByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByPrevIdxKey key = (CFBamBuffValueByPrevIdxKey)schema.getFactoryValue().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteDbKeyHash512TypeByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteDbKeyHash512TypeByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByPrevIdxKey argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeByPrevIdx";
		CFBamBuffDbKeyHash512Type cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDbKeyHash512TypeByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByNextIdxKey key = (CFBamBuffValueByNextIdxKey)schema.getFactoryValue().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteDbKeyHash512TypeByNextIdx( Authorization, key );
	}

	@Override
	public void deleteDbKeyHash512TypeByNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByNextIdxKey argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeByNextIdx";
		CFBamBuffDbKeyHash512Type cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDbKeyHash512TypeByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByContPrevIdxKey key = (CFBamBuffValueByContPrevIdxKey)schema.getFactoryValue().newByContPrevIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalPrevId( argPrevId );
		deleteDbKeyHash512TypeByContPrevIdx( Authorization, key );
	}

	@Override
	public void deleteDbKeyHash512TypeByContPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContPrevIdxKey argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeByContPrevIdx";
		CFBamBuffDbKeyHash512Type cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteDbKeyHash512TypeByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByContNextIdxKey key = (CFBamBuffValueByContNextIdxKey)schema.getFactoryValue().newByContNextIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalNextId( argNextId );
		deleteDbKeyHash512TypeByContNextIdx( Authorization, key );
	}

	@Override
	public void deleteDbKeyHash512TypeByContNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContNextIdxKey argKey )
	{
		final String S_ProcName = "deleteDbKeyHash512TypeByContNextIdx";
		CFBamBuffDbKeyHash512Type cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDbKeyHash512Type> matchSet = new LinkedList<CFBamBuffDbKeyHash512Type>();
		Iterator<CFBamBuffDbKeyHash512Type> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDbKeyHash512Type> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDbKeyHash512Type)(schema.getTableDbKeyHash512Type().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
