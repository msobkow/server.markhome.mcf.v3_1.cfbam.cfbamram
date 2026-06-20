
// Description: Java 25 in-memory RAM DbIO implementation for Uuid6Col.

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
 *	CFBamRamUuid6ColTable in-memory RAM DbIO implementation
 *	for Uuid6Col.
 */
public class CFBamRamUuid6ColTable
	implements ICFBamUuid6ColTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffUuid6Col > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffUuid6Col >();
	private Map< CFBamBuffUuid6ColByTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffUuid6Col >> dictByTableIdx
		= new HashMap< CFBamBuffUuid6ColByTableIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffUuid6Col >>();

	public CFBamRamUuid6ColTable( ICFBamSchema argSchema ) {
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
	public ICFBamUuid6Col createUuid6Col( ICFSecAuthorization Authorization,
		ICFBamUuid6Col iBuff )
	{
		final String S_ProcName = "createUuid6Col";
		
		CFBamBuffUuid6Col Buff = (CFBamBuffUuid6Col)(schema.getTableUuid6Def().createUuid6Def( Authorization,
			iBuff ));
		ICFBamValue tail = null;
		if( Buff.getClassCode() == ICFBamUuid6Col.CLASS_CODE ) {
			ICFBamValue[] siblings = schema.getTableValue().readDerivedByScopeIdx( Authorization,
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
		CFBamBuffUuid6ColByTableIdxKey keyTableIdx = (CFBamBuffUuid6ColByTableIdxKey)schema.getCFBamFactory().getFactoryUuid6Col().newByTableIdxKey();
		keyTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableUuid6Def().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"Uuid6Def",
						"Uuid6Def",
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

		Map< CFLibDbKeyHash256, CFBamBuffUuid6Col > subdictTableIdx;
		if( dictByTableIdx.containsKey( keyTableIdx ) ) {
			subdictTableIdx = dictByTableIdx.get( keyTableIdx );
		}
		else {
			subdictTableIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffUuid6Col >();
			dictByTableIdx.put( keyTableIdx, subdictTableIdx );
		}
		subdictTableIdx.put( pkey, Buff );

		if( tail != null ) {
			int tailClassCode = tail.getClassCode();
			if( tailClassCode == ICFBamValue.CLASS_CODE ) {
				ICFBamValue tailEdit = schema.getCFBamFactory().getFactoryValue().newRec();
				tailEdit.set( (ICFBamValue)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableValue().updateValue( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamAtom.CLASS_CODE ) {
				ICFBamAtom tailEdit = schema.getCFBamFactory().getFactoryAtom().newRec();
				tailEdit.set( (ICFBamAtom)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableAtom().updateAtom( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBlobDef.CLASS_CODE ) {
				ICFBamBlobDef tailEdit = schema.getCFBamFactory().getFactoryBlobDef().newRec();
				tailEdit.set( (ICFBamBlobDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBlobDef().updateBlobDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBlobType.CLASS_CODE ) {
				ICFBamBlobType tailEdit = schema.getCFBamFactory().getFactoryBlobType().newRec();
				tailEdit.set( (ICFBamBlobType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBlobType().updateBlobType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBlobCol.CLASS_CODE ) {
				ICFBamBlobCol tailEdit = schema.getCFBamFactory().getFactoryBlobCol().newRec();
				tailEdit.set( (ICFBamBlobCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBlobCol().updateBlobCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBoolDef.CLASS_CODE ) {
				ICFBamBoolDef tailEdit = schema.getCFBamFactory().getFactoryBoolDef().newRec();
				tailEdit.set( (ICFBamBoolDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBoolDef().updateBoolDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBoolType.CLASS_CODE ) {
				ICFBamBoolType tailEdit = schema.getCFBamFactory().getFactoryBoolType().newRec();
				tailEdit.set( (ICFBamBoolType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBoolType().updateBoolType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamBoolCol.CLASS_CODE ) {
				ICFBamBoolCol tailEdit = schema.getCFBamFactory().getFactoryBoolCol().newRec();
				tailEdit.set( (ICFBamBoolCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableBoolCol().updateBoolCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDateDef.CLASS_CODE ) {
				ICFBamDateDef tailEdit = schema.getCFBamFactory().getFactoryDateDef().newRec();
				tailEdit.set( (ICFBamDateDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDateDef().updateDateDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDateType.CLASS_CODE ) {
				ICFBamDateType tailEdit = schema.getCFBamFactory().getFactoryDateType().newRec();
				tailEdit.set( (ICFBamDateType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDateType().updateDateType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDateCol.CLASS_CODE ) {
				ICFBamDateCol tailEdit = schema.getCFBamFactory().getFactoryDateCol().newRec();
				tailEdit.set( (ICFBamDateCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDateCol().updateDateCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDoubleDef.CLASS_CODE ) {
				ICFBamDoubleDef tailEdit = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
				tailEdit.set( (ICFBamDoubleDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDoubleDef().updateDoubleDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDoubleType.CLASS_CODE ) {
				ICFBamDoubleType tailEdit = schema.getCFBamFactory().getFactoryDoubleType().newRec();
				tailEdit.set( (ICFBamDoubleType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDoubleType().updateDoubleType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDoubleCol.CLASS_CODE ) {
				ICFBamDoubleCol tailEdit = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
				tailEdit.set( (ICFBamDoubleCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDoubleCol().updateDoubleCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamFloatDef.CLASS_CODE ) {
				ICFBamFloatDef tailEdit = schema.getCFBamFactory().getFactoryFloatDef().newRec();
				tailEdit.set( (ICFBamFloatDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableFloatDef().updateFloatDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamFloatType.CLASS_CODE ) {
				ICFBamFloatType tailEdit = schema.getCFBamFactory().getFactoryFloatType().newRec();
				tailEdit.set( (ICFBamFloatType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableFloatType().updateFloatType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamFloatCol.CLASS_CODE ) {
				ICFBamFloatCol tailEdit = schema.getCFBamFactory().getFactoryFloatCol().newRec();
				tailEdit.set( (ICFBamFloatCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableFloatCol().updateFloatCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt16Def.CLASS_CODE ) {
				ICFBamInt16Def tailEdit = schema.getCFBamFactory().getFactoryInt16Def().newRec();
				tailEdit.set( (ICFBamInt16Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt16Def().updateInt16Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt16Type.CLASS_CODE ) {
				ICFBamInt16Type tailEdit = schema.getCFBamFactory().getFactoryInt16Type().newRec();
				tailEdit.set( (ICFBamInt16Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt16Type().updateInt16Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamId16Gen.CLASS_CODE ) {
				ICFBamId16Gen tailEdit = schema.getCFBamFactory().getFactoryId16Gen().newRec();
				tailEdit.set( (ICFBamId16Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableId16Gen().updateId16Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamEnumDef.CLASS_CODE ) {
				ICFBamEnumDef tailEdit = schema.getCFBamFactory().getFactoryEnumDef().newRec();
				tailEdit.set( (ICFBamEnumDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableEnumDef().updateEnumDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamEnumType.CLASS_CODE ) {
				ICFBamEnumType tailEdit = schema.getCFBamFactory().getFactoryEnumType().newRec();
				tailEdit.set( (ICFBamEnumType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableEnumType().updateEnumType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt16Col.CLASS_CODE ) {
				ICFBamInt16Col tailEdit = schema.getCFBamFactory().getFactoryInt16Col().newRec();
				tailEdit.set( (ICFBamInt16Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt16Col().updateInt16Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt32Def.CLASS_CODE ) {
				ICFBamInt32Def tailEdit = schema.getCFBamFactory().getFactoryInt32Def().newRec();
				tailEdit.set( (ICFBamInt32Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt32Def().updateInt32Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt32Type.CLASS_CODE ) {
				ICFBamInt32Type tailEdit = schema.getCFBamFactory().getFactoryInt32Type().newRec();
				tailEdit.set( (ICFBamInt32Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt32Type().updateInt32Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamId32Gen.CLASS_CODE ) {
				ICFBamId32Gen tailEdit = schema.getCFBamFactory().getFactoryId32Gen().newRec();
				tailEdit.set( (ICFBamId32Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableId32Gen().updateId32Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt32Col.CLASS_CODE ) {
				ICFBamInt32Col tailEdit = schema.getCFBamFactory().getFactoryInt32Col().newRec();
				tailEdit.set( (ICFBamInt32Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt32Col().updateInt32Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt64Def.CLASS_CODE ) {
				ICFBamInt64Def tailEdit = schema.getCFBamFactory().getFactoryInt64Def().newRec();
				tailEdit.set( (ICFBamInt64Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt64Def().updateInt64Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt64Type.CLASS_CODE ) {
				ICFBamInt64Type tailEdit = schema.getCFBamFactory().getFactoryInt64Type().newRec();
				tailEdit.set( (ICFBamInt64Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt64Type().updateInt64Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamId64Gen.CLASS_CODE ) {
				ICFBamId64Gen tailEdit = schema.getCFBamFactory().getFactoryId64Gen().newRec();
				tailEdit.set( (ICFBamId64Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableId64Gen().updateId64Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamInt64Col.CLASS_CODE ) {
				ICFBamInt64Col tailEdit = schema.getCFBamFactory().getFactoryInt64Col().newRec();
				tailEdit.set( (ICFBamInt64Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableInt64Col().updateInt64Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokenDef.CLASS_CODE ) {
				ICFBamNmTokenDef tailEdit = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
				tailEdit.set( (ICFBamNmTokenDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokenDef().updateNmTokenDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokenType.CLASS_CODE ) {
				ICFBamNmTokenType tailEdit = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
				tailEdit.set( (ICFBamNmTokenType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokenType().updateNmTokenType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokenCol.CLASS_CODE ) {
				ICFBamNmTokenCol tailEdit = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
				tailEdit.set( (ICFBamNmTokenCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokenCol().updateNmTokenCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokensDef.CLASS_CODE ) {
				ICFBamNmTokensDef tailEdit = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
				tailEdit.set( (ICFBamNmTokensDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokensDef().updateNmTokensDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokensType.CLASS_CODE ) {
				ICFBamNmTokensType tailEdit = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
				tailEdit.set( (ICFBamNmTokensType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokensType().updateNmTokensType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNmTokensCol.CLASS_CODE ) {
				ICFBamNmTokensCol tailEdit = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
				tailEdit.set( (ICFBamNmTokensCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNmTokensCol().updateNmTokensCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNumberDef.CLASS_CODE ) {
				ICFBamNumberDef tailEdit = schema.getCFBamFactory().getFactoryNumberDef().newRec();
				tailEdit.set( (ICFBamNumberDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNumberDef().updateNumberDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNumberType.CLASS_CODE ) {
				ICFBamNumberType tailEdit = schema.getCFBamFactory().getFactoryNumberType().newRec();
				tailEdit.set( (ICFBamNumberType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNumberType().updateNumberType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamNumberCol.CLASS_CODE ) {
				ICFBamNumberCol tailEdit = schema.getCFBamFactory().getFactoryNumberCol().newRec();
				tailEdit.set( (ICFBamNumberCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableNumberCol().updateNumberCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				ICFBamDbKeyHash128Def tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash128Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash128Def().updateDbKeyHash128Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				ICFBamDbKeyHash128Col tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash128Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash128Col().updateDbKeyHash128Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				ICFBamDbKeyHash128Type tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash128Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash128Type().updateDbKeyHash128Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				ICFBamDbKeyHash128Gen tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash128Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash128Gen().updateDbKeyHash128Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				ICFBamDbKeyHash160Def tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash160Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash160Def().updateDbKeyHash160Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				ICFBamDbKeyHash160Col tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash160Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash160Col().updateDbKeyHash160Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				ICFBamDbKeyHash160Type tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash160Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash160Type().updateDbKeyHash160Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				ICFBamDbKeyHash160Gen tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash160Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash160Gen().updateDbKeyHash160Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				ICFBamDbKeyHash224Def tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash224Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash224Def().updateDbKeyHash224Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				ICFBamDbKeyHash224Col tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash224Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash224Col().updateDbKeyHash224Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				ICFBamDbKeyHash224Type tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash224Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash224Type().updateDbKeyHash224Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				ICFBamDbKeyHash224Gen tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash224Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash224Gen().updateDbKeyHash224Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				ICFBamDbKeyHash256Def tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash256Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash256Def().updateDbKeyHash256Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				ICFBamDbKeyHash256Col tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash256Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash256Col().updateDbKeyHash256Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				ICFBamDbKeyHash256Type tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash256Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash256Type().updateDbKeyHash256Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				ICFBamDbKeyHash256Gen tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash256Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash256Gen().updateDbKeyHash256Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				ICFBamDbKeyHash384Def tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash384Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash384Def().updateDbKeyHash384Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				ICFBamDbKeyHash384Col tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash384Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash384Col().updateDbKeyHash384Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				ICFBamDbKeyHash384Type tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash384Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash384Type().updateDbKeyHash384Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				ICFBamDbKeyHash384Gen tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash384Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash384Gen().updateDbKeyHash384Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				ICFBamDbKeyHash512Def tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
				tailEdit.set( (ICFBamDbKeyHash512Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash512Def().updateDbKeyHash512Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				ICFBamDbKeyHash512Col tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
				tailEdit.set( (ICFBamDbKeyHash512Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash512Col().updateDbKeyHash512Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				ICFBamDbKeyHash512Type tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
				tailEdit.set( (ICFBamDbKeyHash512Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash512Type().updateDbKeyHash512Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				ICFBamDbKeyHash512Gen tailEdit = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
				tailEdit.set( (ICFBamDbKeyHash512Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableDbKeyHash512Gen().updateDbKeyHash512Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamStringDef.CLASS_CODE ) {
				ICFBamStringDef tailEdit = schema.getCFBamFactory().getFactoryStringDef().newRec();
				tailEdit.set( (ICFBamStringDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableStringDef().updateStringDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamStringType.CLASS_CODE ) {
				ICFBamStringType tailEdit = schema.getCFBamFactory().getFactoryStringType().newRec();
				tailEdit.set( (ICFBamStringType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableStringType().updateStringType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamStringCol.CLASS_CODE ) {
				ICFBamStringCol tailEdit = schema.getCFBamFactory().getFactoryStringCol().newRec();
				tailEdit.set( (ICFBamStringCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableStringCol().updateStringCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZDateDef.CLASS_CODE ) {
				ICFBamTZDateDef tailEdit = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
				tailEdit.set( (ICFBamTZDateDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZDateDef().updateTZDateDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZDateType.CLASS_CODE ) {
				ICFBamTZDateType tailEdit = schema.getCFBamFactory().getFactoryTZDateType().newRec();
				tailEdit.set( (ICFBamTZDateType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZDateType().updateTZDateType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZDateCol.CLASS_CODE ) {
				ICFBamTZDateCol tailEdit = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
				tailEdit.set( (ICFBamTZDateCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZDateCol().updateTZDateCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimeDef.CLASS_CODE ) {
				ICFBamTZTimeDef tailEdit = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
				tailEdit.set( (ICFBamTZTimeDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimeDef().updateTZTimeDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimeType.CLASS_CODE ) {
				ICFBamTZTimeType tailEdit = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
				tailEdit.set( (ICFBamTZTimeType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimeType().updateTZTimeType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimeCol.CLASS_CODE ) {
				ICFBamTZTimeCol tailEdit = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
				tailEdit.set( (ICFBamTZTimeCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimeCol().updateTZTimeCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				ICFBamTZTimestampDef tailEdit = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
				tailEdit.set( (ICFBamTZTimestampDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimestampDef().updateTZTimestampDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimestampType.CLASS_CODE ) {
				ICFBamTZTimestampType tailEdit = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
				tailEdit.set( (ICFBamTZTimestampType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimestampType().updateTZTimestampType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				ICFBamTZTimestampCol tailEdit = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
				tailEdit.set( (ICFBamTZTimestampCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTZTimestampCol().updateTZTimestampCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTextDef.CLASS_CODE ) {
				ICFBamTextDef tailEdit = schema.getCFBamFactory().getFactoryTextDef().newRec();
				tailEdit.set( (ICFBamTextDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTextDef().updateTextDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTextType.CLASS_CODE ) {
				ICFBamTextType tailEdit = schema.getCFBamFactory().getFactoryTextType().newRec();
				tailEdit.set( (ICFBamTextType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTextType().updateTextType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTextCol.CLASS_CODE ) {
				ICFBamTextCol tailEdit = schema.getCFBamFactory().getFactoryTextCol().newRec();
				tailEdit.set( (ICFBamTextCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTextCol().updateTextCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimeDef.CLASS_CODE ) {
				ICFBamTimeDef tailEdit = schema.getCFBamFactory().getFactoryTimeDef().newRec();
				tailEdit.set( (ICFBamTimeDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimeDef().updateTimeDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimeType.CLASS_CODE ) {
				ICFBamTimeType tailEdit = schema.getCFBamFactory().getFactoryTimeType().newRec();
				tailEdit.set( (ICFBamTimeType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimeType().updateTimeType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimeCol.CLASS_CODE ) {
				ICFBamTimeCol tailEdit = schema.getCFBamFactory().getFactoryTimeCol().newRec();
				tailEdit.set( (ICFBamTimeCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimeCol().updateTimeCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimestampDef.CLASS_CODE ) {
				ICFBamTimestampDef tailEdit = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
				tailEdit.set( (ICFBamTimestampDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimestampDef().updateTimestampDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimestampType.CLASS_CODE ) {
				ICFBamTimestampType tailEdit = schema.getCFBamFactory().getFactoryTimestampType().newRec();
				tailEdit.set( (ICFBamTimestampType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimestampType().updateTimestampType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTimestampCol.CLASS_CODE ) {
				ICFBamTimestampCol tailEdit = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
				tailEdit.set( (ICFBamTimestampCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTimestampCol().updateTimestampCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTokenDef.CLASS_CODE ) {
				ICFBamTokenDef tailEdit = schema.getCFBamFactory().getFactoryTokenDef().newRec();
				tailEdit.set( (ICFBamTokenDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTokenDef().updateTokenDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTokenType.CLASS_CODE ) {
				ICFBamTokenType tailEdit = schema.getCFBamFactory().getFactoryTokenType().newRec();
				tailEdit.set( (ICFBamTokenType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTokenType().updateTokenType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTokenCol.CLASS_CODE ) {
				ICFBamTokenCol tailEdit = schema.getCFBamFactory().getFactoryTokenCol().newRec();
				tailEdit.set( (ICFBamTokenCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableTokenCol().updateTokenCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt16Def.CLASS_CODE ) {
				ICFBamUInt16Def tailEdit = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
				tailEdit.set( (ICFBamUInt16Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt16Def().updateUInt16Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt16Type.CLASS_CODE ) {
				ICFBamUInt16Type tailEdit = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
				tailEdit.set( (ICFBamUInt16Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt16Type().updateUInt16Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt16Col.CLASS_CODE ) {
				ICFBamUInt16Col tailEdit = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
				tailEdit.set( (ICFBamUInt16Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt16Col().updateUInt16Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt32Def.CLASS_CODE ) {
				ICFBamUInt32Def tailEdit = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
				tailEdit.set( (ICFBamUInt32Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt32Def().updateUInt32Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt32Type.CLASS_CODE ) {
				ICFBamUInt32Type tailEdit = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
				tailEdit.set( (ICFBamUInt32Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt32Type().updateUInt32Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt32Col.CLASS_CODE ) {
				ICFBamUInt32Col tailEdit = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
				tailEdit.set( (ICFBamUInt32Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt32Col().updateUInt32Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt64Def.CLASS_CODE ) {
				ICFBamUInt64Def tailEdit = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
				tailEdit.set( (ICFBamUInt64Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt64Def().updateUInt64Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt64Type.CLASS_CODE ) {
				ICFBamUInt64Type tailEdit = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
				tailEdit.set( (ICFBamUInt64Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt64Type().updateUInt64Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUInt64Col.CLASS_CODE ) {
				ICFBamUInt64Col tailEdit = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
				tailEdit.set( (ICFBamUInt64Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUInt64Col().updateUInt64Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuidDef.CLASS_CODE ) {
				ICFBamUuidDef tailEdit = schema.getCFBamFactory().getFactoryUuidDef().newRec();
				tailEdit.set( (ICFBamUuidDef)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuidDef().updateUuidDef( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuidType.CLASS_CODE ) {
				ICFBamUuidType tailEdit = schema.getCFBamFactory().getFactoryUuidType().newRec();
				tailEdit.set( (ICFBamUuidType)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuidType().updateUuidType( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuidGen.CLASS_CODE ) {
				ICFBamUuidGen tailEdit = schema.getCFBamFactory().getFactoryUuidGen().newRec();
				tailEdit.set( (ICFBamUuidGen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuidGen().updateUuidGen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuidCol.CLASS_CODE ) {
				ICFBamUuidCol tailEdit = schema.getCFBamFactory().getFactoryUuidCol().newRec();
				tailEdit.set( (ICFBamUuidCol)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuidCol().updateUuidCol( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuid6Def.CLASS_CODE ) {
				ICFBamUuid6Def tailEdit = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
				tailEdit.set( (ICFBamUuid6Def)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuid6Def().updateUuid6Def( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuid6Type.CLASS_CODE ) {
				ICFBamUuid6Type tailEdit = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
				tailEdit.set( (ICFBamUuid6Type)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuid6Type().updateUuid6Type( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuid6Gen.CLASS_CODE ) {
				ICFBamUuid6Gen tailEdit = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
				tailEdit.set( (ICFBamUuid6Gen)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuid6Gen().updateUuid6Gen( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamUuid6Col.CLASS_CODE ) {
				ICFBamUuid6Col tailEdit = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
				tailEdit.set( (ICFBamUuid6Col)tail );
				tailEdit.setOptionalLookupNext(Buff.getRequiredId());
				schema.getTableUuid6Col().updateUuid6Col( Authorization, tailEdit );
			}
			else if( tailClassCode == ICFBamTableCol.CLASS_CODE ) {
				ICFBamTableCol tailEdit = schema.getCFBamFactory().getFactoryTableCol().newRec();
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
			if (classCode == ICFBamUuid6Col.CLASS_CODE) {
				CFBamBuffUuid6Col retbuff = ((CFBamBuffUuid6Col)(schema.getCFBamFactory().getFactoryUuid6Col().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamUuid6Col readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamUuid6Col.readDerived";
		ICFBamUuid6Col buff;
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
	public ICFBamUuid6Col lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamUuid6Col.lockDerived";
		ICFBamUuid6Col buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamUuid6Col[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamUuid6Col.readAllDerived";
		ICFBamUuid6Col[] retList = new ICFBamUuid6Col[ dictByPKey.values().size() ];
		Iterator< CFBamBuffUuid6Col > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamUuid6Col readDerivedByUNameIdx( ICFSecAuthorization Authorization,
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
		else if( buff instanceof ICFBamUuid6Col ) {
			return( (ICFBamUuid6Col)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamUuid6Col[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamUuid6Col ) ) {
					filteredList.add( (ICFBamUuid6Col)buff );
				}
			}
			return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
		}
	}

	@Override
	public ICFBamUuid6Col[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamUuid6Col ) ) {
					filteredList.add( (ICFBamUuid6Col)buff );
				}
			}
			return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
		}
	}

	@Override
	public ICFBamUuid6Col[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamUuid6Col ) ) {
					filteredList.add( (ICFBamUuid6Col)buff );
				}
			}
			return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
		}
	}

	@Override
	public ICFBamUuid6Col[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamUuid6Col ) ) {
					filteredList.add( (ICFBamUuid6Col)buff );
				}
			}
			return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
		}
	}

	@Override
	public ICFBamUuid6Col[] readDerivedByContPrevIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamUuid6Col ) ) {
					filteredList.add( (ICFBamUuid6Col)buff );
				}
			}
			return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
		}
	}

	@Override
	public ICFBamUuid6Col[] readDerivedByContNextIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamUuid6Col ) ) {
					filteredList.add( (ICFBamUuid6Col)buff );
				}
			}
			return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
		}
	}

	@Override
	public ICFBamUuid6Col[] readDerivedByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamUuid6Col.readDerivedByTableIdx";
		CFBamBuffUuid6ColByTableIdxKey key = (CFBamBuffUuid6ColByTableIdxKey)schema.getCFBamFactory().getFactoryUuid6Col().newByTableIdxKey();

		key.setRequiredTableId( TableId );
		ICFBamUuid6Col[] recArray;
		if( dictByTableIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffUuid6Col > subdictTableIdx
				= dictByTableIdx.get( key );
			recArray = new ICFBamUuid6Col[ subdictTableIdx.size() ];
			Iterator< CFBamBuffUuid6Col > iter = subdictTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffUuid6Col > subdictTableIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffUuid6Col >();
			dictByTableIdx.put( key, subdictTableIdx );
			recArray = new ICFBamUuid6Col[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamUuid6Col readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByIdIdx() ";
		ICFBamUuid6Col buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamUuid6Col readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamUuid6Col.readRec";
		ICFBamUuid6Col buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamUuid6Col.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamUuid6Col lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamUuid6Col buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamUuid6Col.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamUuid6Col[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamUuid6Col.readAllRec";
		ICFBamUuid6Col buff;
		ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
		ICFBamUuid6Col[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamUuid6Col.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
	}

	@Override
	public ICFBamUuid6Col readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readRecByIdIdx() ";
		ICFBamUuid6Col buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
			return( (ICFBamUuid6Col)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamUuid6Col readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readRecByUNameIdx() ";
		ICFBamUuid6Col buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
			return( (ICFBamUuid6Col)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamUuid6Col[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByScopeIdx() ";
		ICFBamUuid6Col buff;
		ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
		ICFBamUuid6Col[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamUuid6Col)buff );
			}
		}
		return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
	}

	@Override
	public ICFBamUuid6Col[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByDefSchemaIdx() ";
		ICFBamUuid6Col buff;
		ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
		ICFBamUuid6Col[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamUuid6Col)buff );
			}
		}
		return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
	}

	@Override
	public ICFBamUuid6Col[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByPrevIdx() ";
		ICFBamUuid6Col buff;
		ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
		ICFBamUuid6Col[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamUuid6Col)buff );
			}
		}
		return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
	}

	@Override
	public ICFBamUuid6Col[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByNextIdx() ";
		ICFBamUuid6Col buff;
		ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
		ICFBamUuid6Col[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamUuid6Col)buff );
			}
		}
		return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
	}

	@Override
	public ICFBamUuid6Col[] readRecByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByContPrevIdx() ";
		ICFBamUuid6Col buff;
		ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
		ICFBamUuid6Col[] buffList = readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamUuid6Col)buff );
			}
		}
		return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
	}

	@Override
	public ICFBamUuid6Col[] readRecByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByContNextIdx() ";
		ICFBamUuid6Col buff;
		ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
		ICFBamUuid6Col[] buffList = readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamUuid6Col)buff );
			}
		}
		return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
	}

	@Override
	public ICFBamUuid6Col[] readRecByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamUuid6Col.readRecByTableIdx() ";
		ICFBamUuid6Col buff;
		ArrayList<ICFBamUuid6Col> filteredList = new ArrayList<ICFBamUuid6Col>();
		ICFBamUuid6Col[] buffList = readDerivedByTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamUuid6Col.CLASS_CODE ) ) {
				filteredList.add( (ICFBamUuid6Col)buff );
			}
		}
		return( filteredList.toArray( new ICFBamUuid6Col[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamUuid6Col moveRecUp( ICFSecAuthorization Authorization,
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
			return( (CFBamBuffUuid6Col)cur );
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
				newInstance = schema.getCFBamFactory().getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffValue editPrev = (CFBamBuffValue)newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTableCol().newRec();
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
				newInstance = schema.getCFBamFactory().getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTableCol().newRec();
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
				newInstance = schema.getCFBamFactory().getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTableCol().newRec();
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

		return( (CFBamBuffUuid6Col)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamUuid6Col moveRecDown( ICFSecAuthorization Authorization,
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
			return( (CFBamBuffUuid6Col)cur );
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
				newInstance = schema.getCFBamFactory().getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTableCol().newRec();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffValue editCur = (CFBamBuffValue)newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamValue.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTableCol().newRec();
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
				newInstance = schema.getCFBamFactory().getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTableCol().newRec();
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
				newInstance = schema.getCFBamFactory().getFactoryValue().newRec();
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryAtom().newRec();
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobDef().newRec();
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobType().newRec();
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBlobCol().newRec();
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolDef().newRec();
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolType().newRec();
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryBoolCol().newRec();
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateDef().newRec();
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateType().newRec();
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDateCol().newRec();
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleDef().newRec();
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleType().newRec();
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDoubleCol().newRec();
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatDef().newRec();
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatType().newRec();
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryFloatCol().newRec();
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Def().newRec();
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Type().newRec();
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId16Gen().newRec();
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumDef().newRec();
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryEnumType().newRec();
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt16Col().newRec();
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Def().newRec();
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Type().newRec();
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId32Gen().newRec();
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt32Col().newRec();
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Def().newRec();
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Type().newRec();
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryId64Gen().newRec();
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryInt64Col().newRec();
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenDef().newRec();
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenType().newRec();
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokenCol().newRec();
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensDef().newRec();
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensType().newRec();
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNmTokensCol().newRec();
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberDef().newRec();
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberType().newRec();
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryNumberCol().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec();
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec();
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringDef().newRec();
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringType().newRec();
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryStringCol().newRec();
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateDef().newRec();
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateType().newRec();
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZDateCol().newRec();
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeDef().newRec();
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeType().newRec();
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimeCol().newRec();
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampDef().newRec();
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampType().newRec();
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTZTimestampCol().newRec();
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextDef().newRec();
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextType().newRec();
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTextCol().newRec();
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeDef().newRec();
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeType().newRec();
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimeCol().newRec();
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampDef().newRec();
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampType().newRec();
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTimestampCol().newRec();
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenDef().newRec();
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenType().newRec();
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTokenCol().newRec();
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Def().newRec();
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Type().newRec();
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt16Col().newRec();
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Def().newRec();
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Type().newRec();
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt32Col().newRec();
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Def().newRec();
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Type().newRec();
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUInt64Col().newRec();
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidDef().newRec();
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidType().newRec();
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidGen().newRec();
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuidCol().newRec();
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Def().newRec();
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Type().newRec();
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Gen().newRec();
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryUuid6Col().newRec();
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				newInstance = schema.getCFBamFactory().getFactoryTableCol().newRec();
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

		return( (CFBamBuffUuid6Col)editCur );
	}

	public ICFBamUuid6Col updateUuid6Col( ICFSecAuthorization Authorization,
		ICFBamUuid6Col iBuff )
	{
		CFBamBuffUuid6Col Buff = (CFBamBuffUuid6Col)(schema.getTableUuid6Def().updateUuid6Def( Authorization,	iBuff ));
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffUuid6Col existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateUuid6Col",
				"Existing record not found",
				"Existing record not found",
				"Uuid6Col",
				"Uuid6Col",
				pkey );
		}
		CFBamBuffUuid6ColByTableIdxKey existingKeyTableIdx = (CFBamBuffUuid6ColByTableIdxKey)schema.getCFBamFactory().getFactoryUuid6Col().newByTableIdxKey();
		existingKeyTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffUuid6ColByTableIdxKey newKeyTableIdx = (CFBamBuffUuid6ColByTableIdxKey)schema.getCFBamFactory().getFactoryUuid6Col().newByTableIdxKey();
		newKeyTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableUuid6Def().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateUuid6Col",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"Uuid6Def",
						"Uuid6Def",
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
						"updateUuid6Col",
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

		Map< CFLibDbKeyHash256, CFBamBuffUuid6Col > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByTableIdx.get( existingKeyTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByTableIdx.containsKey( newKeyTableIdx ) ) {
			subdict = dictByTableIdx.get( newKeyTableIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffUuid6Col >();
			dictByTableIdx.put( newKeyTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteUuid6Col( ICFSecAuthorization Authorization,
		ICFBamUuid6Col iBuff )
	{
		final String S_ProcName = "CFBamRamUuid6ColTable.deleteUuid6Col() ";
		CFBamBuffUuid6Col Buff = (CFBamBuffUuid6Col)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffUuid6Col existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteUuid6Col",
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
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryValue().newRec());
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryAtom().newRec());
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBlobDef().newRec());
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBlobType().newRec());
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBlobCol().newRec());
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBoolDef().newRec());
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBoolType().newRec());
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBoolCol().newRec());
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDateDef().newRec());
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDateType().newRec());
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDateCol().newRec());
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDoubleDef().newRec());
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDoubleType().newRec());
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDoubleCol().newRec());
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryFloatDef().newRec());
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryFloatType().newRec());
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryFloatCol().newRec());
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt16Def().newRec());
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt16Type().newRec());
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryId16Gen().newRec());
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryEnumDef().newRec());
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryEnumType().newRec());
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt16Col().newRec());
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt32Def().newRec());
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt32Type().newRec());
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryId32Gen().newRec());
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt32Col().newRec());
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt64Def().newRec());
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt64Type().newRec());
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryId64Gen().newRec());
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt64Col().newRec());
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokenDef().newRec());
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokenType().newRec());
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokenCol().newRec());
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokensDef().newRec());
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokensType().newRec());
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokensCol().newRec());
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNumberDef().newRec());
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNumberType().newRec());
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNumberCol().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec());
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryStringDef().newRec());
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryStringType().newRec());
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryStringCol().newRec());
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZDateDef().newRec());
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZDateType().newRec());
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZDateCol().newRec());
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimeDef().newRec());
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimeType().newRec());
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimeCol().newRec());
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimestampDef().newRec());
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimestampType().newRec());
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimestampCol().newRec());
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTextDef().newRec());
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTextType().newRec());
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTextCol().newRec());
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimeDef().newRec());
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimeType().newRec());
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimeCol().newRec());
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimestampDef().newRec());
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimestampType().newRec());
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimestampCol().newRec());
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTokenDef().newRec());
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTokenType().newRec());
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTokenCol().newRec());
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt16Def().newRec());
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt16Type().newRec());
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt16Col().newRec());
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt32Def().newRec());
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt32Type().newRec());
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt32Col().newRec());
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt64Def().newRec());
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt64Type().newRec());
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt64Col().newRec());
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuidDef().newRec());
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuidType().newRec());
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuidGen().newRec());
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuidCol().newRec());
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuid6Def().newRec());
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuid6Type().newRec());
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuid6Gen().newRec());
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuid6Col().newRec());
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				editPrev = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTableCol().newRec());
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
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryValue().newRec());
			}
			else if( classCode == ICFBamAtom.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryAtom().newRec());
			}
			else if( classCode == ICFBamBlobDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBlobDef().newRec());
			}
			else if( classCode == ICFBamBlobType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBlobType().newRec());
			}
			else if( classCode == ICFBamBlobCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBlobCol().newRec());
			}
			else if( classCode == ICFBamBoolDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBoolDef().newRec());
			}
			else if( classCode == ICFBamBoolType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBoolType().newRec());
			}
			else if( classCode == ICFBamBoolCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryBoolCol().newRec());
			}
			else if( classCode == ICFBamDateDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDateDef().newRec());
			}
			else if( classCode == ICFBamDateType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDateType().newRec());
			}
			else if( classCode == ICFBamDateCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDateCol().newRec());
			}
			else if( classCode == ICFBamDoubleDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDoubleDef().newRec());
			}
			else if( classCode == ICFBamDoubleType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDoubleType().newRec());
			}
			else if( classCode == ICFBamDoubleCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDoubleCol().newRec());
			}
			else if( classCode == ICFBamFloatDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryFloatDef().newRec());
			}
			else if( classCode == ICFBamFloatType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryFloatType().newRec());
			}
			else if( classCode == ICFBamFloatCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryFloatCol().newRec());
			}
			else if( classCode == ICFBamInt16Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt16Def().newRec());
			}
			else if( classCode == ICFBamInt16Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt16Type().newRec());
			}
			else if( classCode == ICFBamId16Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryId16Gen().newRec());
			}
			else if( classCode == ICFBamEnumDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryEnumDef().newRec());
			}
			else if( classCode == ICFBamEnumType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryEnumType().newRec());
			}
			else if( classCode == ICFBamInt16Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt16Col().newRec());
			}
			else if( classCode == ICFBamInt32Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt32Def().newRec());
			}
			else if( classCode == ICFBamInt32Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt32Type().newRec());
			}
			else if( classCode == ICFBamId32Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryId32Gen().newRec());
			}
			else if( classCode == ICFBamInt32Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt32Col().newRec());
			}
			else if( classCode == ICFBamInt64Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt64Def().newRec());
			}
			else if( classCode == ICFBamInt64Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt64Type().newRec());
			}
			else if( classCode == ICFBamId64Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryId64Gen().newRec());
			}
			else if( classCode == ICFBamInt64Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryInt64Col().newRec());
			}
			else if( classCode == ICFBamNmTokenDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokenDef().newRec());
			}
			else if( classCode == ICFBamNmTokenType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokenType().newRec());
			}
			else if( classCode == ICFBamNmTokenCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokenCol().newRec());
			}
			else if( classCode == ICFBamNmTokensDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokensDef().newRec());
			}
			else if( classCode == ICFBamNmTokensType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokensType().newRec());
			}
			else if( classCode == ICFBamNmTokensCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNmTokensCol().newRec());
			}
			else if( classCode == ICFBamNumberDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNumberDef().newRec());
			}
			else if( classCode == ICFBamNumberType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNumberType().newRec());
			}
			else if( classCode == ICFBamNumberCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryNumberCol().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash128Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash128Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash128Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash128Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash128Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash160Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash160Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash160Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash160Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash160Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash224Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash224Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash224Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash224Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash224Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash256Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash256Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash256Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash256Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash256Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash384Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash384Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash384Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash384Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash384Gen().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash512Def().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash512Col().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash512Type().newRec());
			}
			else if( classCode == ICFBamDbKeyHash512Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryDbKeyHash512Gen().newRec());
			}
			else if( classCode == ICFBamStringDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryStringDef().newRec());
			}
			else if( classCode == ICFBamStringType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryStringType().newRec());
			}
			else if( classCode == ICFBamStringCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryStringCol().newRec());
			}
			else if( classCode == ICFBamTZDateDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZDateDef().newRec());
			}
			else if( classCode == ICFBamTZDateType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZDateType().newRec());
			}
			else if( classCode == ICFBamTZDateCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZDateCol().newRec());
			}
			else if( classCode == ICFBamTZTimeDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimeDef().newRec());
			}
			else if( classCode == ICFBamTZTimeType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimeType().newRec());
			}
			else if( classCode == ICFBamTZTimeCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimeCol().newRec());
			}
			else if( classCode == ICFBamTZTimestampDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimestampDef().newRec());
			}
			else if( classCode == ICFBamTZTimestampType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimestampType().newRec());
			}
			else if( classCode == ICFBamTZTimestampCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTZTimestampCol().newRec());
			}
			else if( classCode == ICFBamTextDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTextDef().newRec());
			}
			else if( classCode == ICFBamTextType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTextType().newRec());
			}
			else if( classCode == ICFBamTextCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTextCol().newRec());
			}
			else if( classCode == ICFBamTimeDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimeDef().newRec());
			}
			else if( classCode == ICFBamTimeType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimeType().newRec());
			}
			else if( classCode == ICFBamTimeCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimeCol().newRec());
			}
			else if( classCode == ICFBamTimestampDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimestampDef().newRec());
			}
			else if( classCode == ICFBamTimestampType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimestampType().newRec());
			}
			else if( classCode == ICFBamTimestampCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTimestampCol().newRec());
			}
			else if( classCode == ICFBamTokenDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTokenDef().newRec());
			}
			else if( classCode == ICFBamTokenType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTokenType().newRec());
			}
			else if( classCode == ICFBamTokenCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTokenCol().newRec());
			}
			else if( classCode == ICFBamUInt16Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt16Def().newRec());
			}
			else if( classCode == ICFBamUInt16Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt16Type().newRec());
			}
			else if( classCode == ICFBamUInt16Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt16Col().newRec());
			}
			else if( classCode == ICFBamUInt32Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt32Def().newRec());
			}
			else if( classCode == ICFBamUInt32Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt32Type().newRec());
			}
			else if( classCode == ICFBamUInt32Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt32Col().newRec());
			}
			else if( classCode == ICFBamUInt64Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt64Def().newRec());
			}
			else if( classCode == ICFBamUInt64Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt64Type().newRec());
			}
			else if( classCode == ICFBamUInt64Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUInt64Col().newRec());
			}
			else if( classCode == ICFBamUuidDef.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuidDef().newRec());
			}
			else if( classCode == ICFBamUuidType.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuidType().newRec());
			}
			else if( classCode == ICFBamUuidGen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuidGen().newRec());
			}
			else if( classCode == ICFBamUuidCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuidCol().newRec());
			}
			else if( classCode == ICFBamUuid6Def.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuid6Def().newRec());
			}
			else if( classCode == ICFBamUuid6Type.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuid6Type().newRec());
			}
			else if( classCode == ICFBamUuid6Gen.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuid6Gen().newRec());
			}
			else if( classCode == ICFBamUuid6Col.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryUuid6Col().newRec());
			}
			else if( classCode == ICFBamTableCol.CLASS_CODE ) {
				editNext = (CFBamBuffValue)(schema.getCFBamFactory().getFactoryTableCol().newRec());
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
		CFBamBuffUuid6ColByTableIdxKey keyTableIdx = (CFBamBuffUuid6ColByTableIdxKey)schema.getCFBamFactory().getFactoryUuid6Col().newByTableIdxKey();
		keyTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffUuid6Col > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByTableIdx.get( keyTableIdx );
		subdict.remove( pkey );

		schema.getTableUuid6Def().deleteUuid6Def( Authorization,
			Buff );
	}
	@Override
	public void deleteUuid6ColByTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffUuid6ColByTableIdxKey key = (CFBamBuffUuid6ColByTableIdxKey)schema.getCFBamFactory().getFactoryUuid6Col().newByTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteUuid6ColByTableIdx( Authorization, key );
	}

	@Override
	public void deleteUuid6ColByTableIdx( ICFSecAuthorization Authorization,
		ICFBamUuid6ColByTableIdxKey argKey )
	{
		CFBamBuffUuid6Col cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}

	@Override
	public void deleteUuid6ColByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffUuid6Col cur;
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}

	@Override
	public void deleteUuid6ColByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffValueByUNameIdxKey key = (CFBamBuffValueByUNameIdxKey)schema.getCFBamFactory().getFactoryValue().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteUuid6ColByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteUuid6ColByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamValueByUNameIdxKey argKey )
	{
		CFBamBuffUuid6Col cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}

	@Override
	public void deleteUuid6ColByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffValueByScopeIdxKey key = (CFBamBuffValueByScopeIdxKey)schema.getCFBamFactory().getFactoryValue().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteUuid6ColByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteUuid6ColByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamValueByScopeIdxKey argKey )
	{
		CFBamBuffUuid6Col cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}

	@Override
	public void deleteUuid6ColByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffValueByDefSchemaIdxKey key = (CFBamBuffValueByDefSchemaIdxKey)schema.getCFBamFactory().getFactoryValue().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteUuid6ColByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteUuid6ColByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamValueByDefSchemaIdxKey argKey )
	{
		CFBamBuffUuid6Col cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}

	@Override
	public void deleteUuid6ColByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByPrevIdxKey key = (CFBamBuffValueByPrevIdxKey)schema.getCFBamFactory().getFactoryValue().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteUuid6ColByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteUuid6ColByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByPrevIdxKey argKey )
	{
		CFBamBuffUuid6Col cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}

	@Override
	public void deleteUuid6ColByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByNextIdxKey key = (CFBamBuffValueByNextIdxKey)schema.getCFBamFactory().getFactoryValue().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteUuid6ColByNextIdx( Authorization, key );
	}

	@Override
	public void deleteUuid6ColByNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByNextIdxKey argKey )
	{
		CFBamBuffUuid6Col cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}

	@Override
	public void deleteUuid6ColByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByContPrevIdxKey key = (CFBamBuffValueByContPrevIdxKey)schema.getCFBamFactory().getFactoryValue().newByContPrevIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalPrevId( argPrevId );
		deleteUuid6ColByContPrevIdx( Authorization, key );
	}

	@Override
	public void deleteUuid6ColByContPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContPrevIdxKey argKey )
	{
		CFBamBuffUuid6Col cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}

	@Override
	public void deleteUuid6ColByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByContNextIdxKey key = (CFBamBuffValueByContNextIdxKey)schema.getCFBamFactory().getFactoryValue().newByContNextIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalNextId( argNextId );
		deleteUuid6ColByContNextIdx( Authorization, key );
	}

	@Override
	public void deleteUuid6ColByContNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContNextIdxKey argKey )
	{
		CFBamBuffUuid6Col cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffUuid6Col> matchSet = new LinkedList<CFBamBuffUuid6Col>();
		Iterator<CFBamBuffUuid6Col> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffUuid6Col> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffUuid6Col)(schema.getTableUuid6Col().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteUuid6Col( Authorization, cur );
		}
	}
}
