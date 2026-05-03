// Description: Java 25 implementation of an in-memory RAM CFBam schema.

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

import java.lang.reflect.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;

import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfbam.cfbam.*;
import server.markhome.mcf.v3_1.cfsec.cfsecobj.*;
import server.markhome.mcf.v3_1.cfint.cfintobj.*;
import server.markhome.mcf.v3_1.cfbam.cfbamobj.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.buff.*;
import server.markhome.mcf.v3_1.cfint.cfint.buff.*;
import server.markhome.mcf.v3_1.cfbam.cfbam.buff.*;
import server.markhome.mcf.v3_1.cfbam.cfbamsaxloader.*;

public class CFBamRamSchema
	extends CFBamBuffSchema
	implements ICFBamSchema
{


	public CFBamRamSchema() {
		super();
		tableAtom = new CFBamRamAtomTable( this );
		tableBlobCol = new CFBamRamBlobColTable( this );
		tableBlobDef = new CFBamRamBlobDefTable( this );
		tableBlobType = new CFBamRamBlobTypeTable( this );
		tableBoolCol = new CFBamRamBoolColTable( this );
		tableBoolDef = new CFBamRamBoolDefTable( this );
		tableBoolType = new CFBamRamBoolTypeTable( this );
		tableChain = new CFBamRamChainTable( this );
		tableClearDep = new CFBamRamClearDepTable( this );
		tableClearSubDep1 = new CFBamRamClearSubDep1Table( this );
		tableClearSubDep2 = new CFBamRamClearSubDep2Table( this );
		tableClearSubDep3 = new CFBamRamClearSubDep3Table( this );
		tableClearTopDep = new CFBamRamClearTopDepTable( this );
		tableDateCol = new CFBamRamDateColTable( this );
		tableDateDef = new CFBamRamDateDefTable( this );
		tableDateType = new CFBamRamDateTypeTable( this );
		tableDbKeyHash128Col = new CFBamRamDbKeyHash128ColTable( this );
		tableDbKeyHash128Def = new CFBamRamDbKeyHash128DefTable( this );
		tableDbKeyHash128Gen = new CFBamRamDbKeyHash128GenTable( this );
		tableDbKeyHash128Type = new CFBamRamDbKeyHash128TypeTable( this );
		tableDbKeyHash160Col = new CFBamRamDbKeyHash160ColTable( this );
		tableDbKeyHash160Def = new CFBamRamDbKeyHash160DefTable( this );
		tableDbKeyHash160Gen = new CFBamRamDbKeyHash160GenTable( this );
		tableDbKeyHash160Type = new CFBamRamDbKeyHash160TypeTable( this );
		tableDbKeyHash224Col = new CFBamRamDbKeyHash224ColTable( this );
		tableDbKeyHash224Def = new CFBamRamDbKeyHash224DefTable( this );
		tableDbKeyHash224Gen = new CFBamRamDbKeyHash224GenTable( this );
		tableDbKeyHash224Type = new CFBamRamDbKeyHash224TypeTable( this );
		tableDbKeyHash256Col = new CFBamRamDbKeyHash256ColTable( this );
		tableDbKeyHash256Def = new CFBamRamDbKeyHash256DefTable( this );
		tableDbKeyHash256Gen = new CFBamRamDbKeyHash256GenTable( this );
		tableDbKeyHash256Type = new CFBamRamDbKeyHash256TypeTable( this );
		tableDbKeyHash384Col = new CFBamRamDbKeyHash384ColTable( this );
		tableDbKeyHash384Def = new CFBamRamDbKeyHash384DefTable( this );
		tableDbKeyHash384Gen = new CFBamRamDbKeyHash384GenTable( this );
		tableDbKeyHash384Type = new CFBamRamDbKeyHash384TypeTable( this );
		tableDbKeyHash512Col = new CFBamRamDbKeyHash512ColTable( this );
		tableDbKeyHash512Def = new CFBamRamDbKeyHash512DefTable( this );
		tableDbKeyHash512Gen = new CFBamRamDbKeyHash512GenTable( this );
		tableDbKeyHash512Type = new CFBamRamDbKeyHash512TypeTable( this );
		tableDelDep = new CFBamRamDelDepTable( this );
		tableDelSubDep1 = new CFBamRamDelSubDep1Table( this );
		tableDelSubDep2 = new CFBamRamDelSubDep2Table( this );
		tableDelSubDep3 = new CFBamRamDelSubDep3Table( this );
		tableDelTopDep = new CFBamRamDelTopDepTable( this );
		tableDoubleCol = new CFBamRamDoubleColTable( this );
		tableDoubleDef = new CFBamRamDoubleDefTable( this );
		tableDoubleType = new CFBamRamDoubleTypeTable( this );
		tableEnumDef = new CFBamRamEnumDefTable( this );
		tableEnumTag = new CFBamRamEnumTagTable( this );
		tableEnumType = new CFBamRamEnumTypeTable( this );
		tableFloatCol = new CFBamRamFloatColTable( this );
		tableFloatDef = new CFBamRamFloatDefTable( this );
		tableFloatType = new CFBamRamFloatTypeTable( this );
		tableId16Gen = new CFBamRamId16GenTable( this );
		tableId32Gen = new CFBamRamId32GenTable( this );
		tableId64Gen = new CFBamRamId64GenTable( this );
		tableIndex = new CFBamRamIndexTable( this );
		tableIndexCol = new CFBamRamIndexColTable( this );
		tableIndexTweak = new CFBamRamIndexTweakTable( this );
		tableInt16Col = new CFBamRamInt16ColTable( this );
		tableInt16Def = new CFBamRamInt16DefTable( this );
		tableInt16Type = new CFBamRamInt16TypeTable( this );
		tableInt32Col = new CFBamRamInt32ColTable( this );
		tableInt32Def = new CFBamRamInt32DefTable( this );
		tableInt32Type = new CFBamRamInt32TypeTable( this );
		tableInt64Col = new CFBamRamInt64ColTable( this );
		tableInt64Def = new CFBamRamInt64DefTable( this );
		tableInt64Type = new CFBamRamInt64TypeTable( this );
		tableNmTokenCol = new CFBamRamNmTokenColTable( this );
		tableNmTokenDef = new CFBamRamNmTokenDefTable( this );
		tableNmTokenType = new CFBamRamNmTokenTypeTable( this );
		tableNmTokensCol = new CFBamRamNmTokensColTable( this );
		tableNmTokensDef = new CFBamRamNmTokensDefTable( this );
		tableNmTokensType = new CFBamRamNmTokensTypeTable( this );
		tableNumberCol = new CFBamRamNumberColTable( this );
		tableNumberDef = new CFBamRamNumberDefTable( this );
		tableNumberType = new CFBamRamNumberTypeTable( this );
		tableParam = new CFBamRamParamTable( this );
		tablePopDep = new CFBamRamPopDepTable( this );
		tablePopSubDep1 = new CFBamRamPopSubDep1Table( this );
		tablePopSubDep2 = new CFBamRamPopSubDep2Table( this );
		tablePopSubDep3 = new CFBamRamPopSubDep3Table( this );
		tablePopTopDep = new CFBamRamPopTopDepTable( this );
		tableRelation = new CFBamRamRelationTable( this );
		tableRelationCol = new CFBamRamRelationColTable( this );
		tableRoleDef = new CFBamRamRoleDefTable( this );
		tableSchemaDef = new CFBamRamSchemaDefTable( this );
		tableSchemaRef = new CFBamRamSchemaRefTable( this );
		tableSchemaRole = new CFBamRamSchemaRoleTable( this );
		tableSchemaTweak = new CFBamRamSchemaTweakTable( this );
		tableScope = new CFBamRamScopeTable( this );
		tableServerListFunc = new CFBamRamServerListFuncTable( this );
		tableServerMethod = new CFBamRamServerMethodTable( this );
		tableServerObjFunc = new CFBamRamServerObjFuncTable( this );
		tableServerProc = new CFBamRamServerProcTable( this );
		tableStringCol = new CFBamRamStringColTable( this );
		tableStringDef = new CFBamRamStringDefTable( this );
		tableStringType = new CFBamRamStringTypeTable( this );
		tableTZDateCol = new CFBamRamTZDateColTable( this );
		tableTZDateDef = new CFBamRamTZDateDefTable( this );
		tableTZDateType = new CFBamRamTZDateTypeTable( this );
		tableTZTimeCol = new CFBamRamTZTimeColTable( this );
		tableTZTimeDef = new CFBamRamTZTimeDefTable( this );
		tableTZTimeType = new CFBamRamTZTimeTypeTable( this );
		tableTZTimestampCol = new CFBamRamTZTimestampColTable( this );
		tableTZTimestampDef = new CFBamRamTZTimestampDefTable( this );
		tableTZTimestampType = new CFBamRamTZTimestampTypeTable( this );
		tableTable = new CFBamRamTableTable( this );
		tableTableCol = new CFBamRamTableColTable( this );
		tableTableTweak = new CFBamRamTableTweakTable( this );
		tableTextCol = new CFBamRamTextColTable( this );
		tableTextDef = new CFBamRamTextDefTable( this );
		tableTextType = new CFBamRamTextTypeTable( this );
		tableTimeCol = new CFBamRamTimeColTable( this );
		tableTimeDef = new CFBamRamTimeDefTable( this );
		tableTimeType = new CFBamRamTimeTypeTable( this );
		tableTimestampCol = new CFBamRamTimestampColTable( this );
		tableTimestampDef = new CFBamRamTimestampDefTable( this );
		tableTimestampType = new CFBamRamTimestampTypeTable( this );
		tableTokenCol = new CFBamRamTokenColTable( this );
		tableTokenDef = new CFBamRamTokenDefTable( this );
		tableTokenType = new CFBamRamTokenTypeTable( this );
		tableTweak = new CFBamRamTweakTable( this );
		tableUInt16Col = new CFBamRamUInt16ColTable( this );
		tableUInt16Def = new CFBamRamUInt16DefTable( this );
		tableUInt16Type = new CFBamRamUInt16TypeTable( this );
		tableUInt32Col = new CFBamRamUInt32ColTable( this );
		tableUInt32Def = new CFBamRamUInt32DefTable( this );
		tableUInt32Type = new CFBamRamUInt32TypeTable( this );
		tableUInt64Col = new CFBamRamUInt64ColTable( this );
		tableUInt64Def = new CFBamRamUInt64DefTable( this );
		tableUInt64Type = new CFBamRamUInt64TypeTable( this );
		tableUuid6Col = new CFBamRamUuid6ColTable( this );
		tableUuid6Def = new CFBamRamUuid6DefTable( this );
		tableUuid6Gen = new CFBamRamUuid6GenTable( this );
		tableUuid6Type = new CFBamRamUuid6TypeTable( this );
		tableUuidCol = new CFBamRamUuidColTable( this );
		tableUuidDef = new CFBamRamUuidDefTable( this );
		tableUuidGen = new CFBamRamUuidGenTable( this );
		tableUuidType = new CFBamRamUuidTypeTable( this );
		tableValue = new CFBamRamValueTable( this );
	}

	@Override
	public ICFBamSchema newSchema() {
		throw new CFLibMustOverrideException( getClass(), "newSchema" );
	}

	@Override
	public CFLibDbKeyHash256 nextChainIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextEnumTagIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextIndexColIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextParamIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextRelationColIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextTweakIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextScopeIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextValueIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	@Override
	public CFLibDbKeyHash256 nextRoleIdGen() {
		CFLibDbKeyHash256 retval = new CFLibDbKeyHash256(0);
		return( retval );
	}

	public String fileImport( CFSecAuthorization Authorization,
		String fileName,
		String fileContent )
	{
		final String S_ProcName = "fileImport";
		if( ( fileName == null ) || ( fileName.length() <= 0 ) ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				1,
				"fileName" );
		}
		if( ( fileContent == null ) || ( fileContent.length() <= 0 ) ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				2,
				"fileContent" );
		}

		CFBamSaxLoader saxLoader = new CFBamSaxLoader();
		ICFBamSchemaObj schemaObj = new CFBamSchemaObj();
		schemaObj.setCFBamBackingStore( this );
		saxLoader.setSchemaObj( schemaObj );
		ICFSecClusterObj useCluster = schemaObj.getClusterTableObj().readClusterByIdIdx( Authorization.getSecClusterId() );
		ICFSecTenantObj useTenant = schemaObj.getTenantTableObj().readTenantByIdIdx( Authorization.getSecTenantId() );
		CFLibCachedMessageLog runlog = new CFLibCachedMessageLog();
		saxLoader.setLog( runlog );
		saxLoader.setUseCluster( useCluster );
		saxLoader.setUseTenant( useTenant );
		saxLoader.parseStringContents( fileContent );
		String logFileContent = runlog.getCacheContents();
		if( logFileContent == null ) {
			logFileContent = "";
		}

		return( logFileContent );
	}

		
	@Override
	public void wireTableTableInstances() {
		if (tableScope == null || !(tableScope instanceof CFBamRamScopeTable)) {
			tableScope = new CFBamRamScopeTable(this);
		}
		if (tableSchemaDef == null || !(tableSchemaDef instanceof CFBamRamSchemaDefTable)) {
			tableSchemaDef = new CFBamRamSchemaDefTable(this);
		}
		if (tableSchemaRef == null || !(tableSchemaRef instanceof CFBamRamSchemaRefTable)) {
			tableSchemaRef = new CFBamRamSchemaRefTable(this);
		}
		if (tableServerMethod == null || !(tableServerMethod instanceof CFBamRamServerMethodTable)) {
			tableServerMethod = new CFBamRamServerMethodTable(this);
		}
		if (tableServerObjFunc == null || !(tableServerObjFunc instanceof CFBamRamServerObjFuncTable)) {
			tableServerObjFunc = new CFBamRamServerObjFuncTable(this);
		}
		if (tableServerProc == null || !(tableServerProc instanceof CFBamRamServerProcTable)) {
			tableServerProc = new CFBamRamServerProcTable(this);
		}
		if (tableTable == null || !(tableTable instanceof CFBamRamTableTable)) {
			tableTable = new CFBamRamTableTable(this);
		}
		if (tableTweak == null || !(tableTweak instanceof CFBamRamTweakTable)) {
			tableTweak = new CFBamRamTweakTable(this);
		}
		if (tableTableTweak == null || !(tableTableTweak instanceof CFBamRamTableTweakTable)) {
			tableTableTweak = new CFBamRamTableTweakTable(this);
		}
		if (tableSchemaTweak == null || !(tableSchemaTweak instanceof CFBamRamSchemaTweakTable)) {
			tableSchemaTweak = new CFBamRamSchemaTweakTable(this);
		}
		if (tableIndexTweak == null || !(tableIndexTweak instanceof CFBamRamIndexTweakTable)) {
			tableIndexTweak = new CFBamRamIndexTweakTable(this);
		}
		if (tableValue == null || !(tableValue instanceof CFBamRamValueTable)) {
			tableValue = new CFBamRamValueTable(this);
		}
		if (tableAtom == null || !(tableAtom instanceof CFBamRamAtomTable)) {
			tableAtom = new CFBamRamAtomTable(this);
		}
		if (tableBlobDef == null || !(tableBlobDef instanceof CFBamRamBlobDefTable)) {
			tableBlobDef = new CFBamRamBlobDefTable(this);
		}
		if (tableBlobType == null || !(tableBlobType instanceof CFBamRamBlobTypeTable)) {
			tableBlobType = new CFBamRamBlobTypeTable(this);
		}
		if (tableBoolDef == null || !(tableBoolDef instanceof CFBamRamBoolDefTable)) {
			tableBoolDef = new CFBamRamBoolDefTable(this);
		}
		if (tableBoolType == null || !(tableBoolType instanceof CFBamRamBoolTypeTable)) {
			tableBoolType = new CFBamRamBoolTypeTable(this);
		}
		if (tableChain == null || !(tableChain instanceof CFBamRamChainTable)) {
			tableChain = new CFBamRamChainTable(this);
		}
		if (tableClearDep == null || !(tableClearDep instanceof CFBamRamClearDepTable)) {
			tableClearDep = new CFBamRamClearDepTable(this);
		}
		if (tableClearSubDep1 == null || !(tableClearSubDep1 instanceof CFBamRamClearSubDep1Table)) {
			tableClearSubDep1 = new CFBamRamClearSubDep1Table(this);
		}
		if (tableClearSubDep2 == null || !(tableClearSubDep2 instanceof CFBamRamClearSubDep2Table)) {
			tableClearSubDep2 = new CFBamRamClearSubDep2Table(this);
		}
		if (tableClearSubDep3 == null || !(tableClearSubDep3 instanceof CFBamRamClearSubDep3Table)) {
			tableClearSubDep3 = new CFBamRamClearSubDep3Table(this);
		}
		if (tableClearTopDep == null || !(tableClearTopDep instanceof CFBamRamClearTopDepTable)) {
			tableClearTopDep = new CFBamRamClearTopDepTable(this);
		}
		if (tableDateDef == null || !(tableDateDef instanceof CFBamRamDateDefTable)) {
			tableDateDef = new CFBamRamDateDefTable(this);
		}
		if (tableDateType == null || !(tableDateType instanceof CFBamRamDateTypeTable)) {
			tableDateType = new CFBamRamDateTypeTable(this);
		}
		if (tableDelDep == null || !(tableDelDep instanceof CFBamRamDelDepTable)) {
			tableDelDep = new CFBamRamDelDepTable(this);
		}
		if (tableDelSubDep1 == null || !(tableDelSubDep1 instanceof CFBamRamDelSubDep1Table)) {
			tableDelSubDep1 = new CFBamRamDelSubDep1Table(this);
		}
		if (tableDelSubDep2 == null || !(tableDelSubDep2 instanceof CFBamRamDelSubDep2Table)) {
			tableDelSubDep2 = new CFBamRamDelSubDep2Table(this);
		}
		if (tableDelSubDep3 == null || !(tableDelSubDep3 instanceof CFBamRamDelSubDep3Table)) {
			tableDelSubDep3 = new CFBamRamDelSubDep3Table(this);
		}
		if (tableDelTopDep == null || !(tableDelTopDep instanceof CFBamRamDelTopDepTable)) {
			tableDelTopDep = new CFBamRamDelTopDepTable(this);
		}
		if (tableDoubleDef == null || !(tableDoubleDef instanceof CFBamRamDoubleDefTable)) {
			tableDoubleDef = new CFBamRamDoubleDefTable(this);
		}
		if (tableDoubleType == null || !(tableDoubleType instanceof CFBamRamDoubleTypeTable)) {
			tableDoubleType = new CFBamRamDoubleTypeTable(this);
		}
		if (tableEnumTag == null || !(tableEnumTag instanceof CFBamRamEnumTagTable)) {
			tableEnumTag = new CFBamRamEnumTagTable(this);
		}
		if (tableFloatDef == null || !(tableFloatDef instanceof CFBamRamFloatDefTable)) {
			tableFloatDef = new CFBamRamFloatDefTable(this);
		}
		if (tableFloatType == null || !(tableFloatType instanceof CFBamRamFloatTypeTable)) {
			tableFloatType = new CFBamRamFloatTypeTable(this);
		}
		if (tableIndex == null || !(tableIndex instanceof CFBamRamIndexTable)) {
			tableIndex = new CFBamRamIndexTable(this);
		}
		if (tableIndexCol == null || !(tableIndexCol instanceof CFBamRamIndexColTable)) {
			tableIndexCol = new CFBamRamIndexColTable(this);
		}
		if (tableInt16Def == null || !(tableInt16Def instanceof CFBamRamInt16DefTable)) {
			tableInt16Def = new CFBamRamInt16DefTable(this);
		}
		if (tableInt16Type == null || !(tableInt16Type instanceof CFBamRamInt16TypeTable)) {
			tableInt16Type = new CFBamRamInt16TypeTable(this);
		}
		if (tableInt32Def == null || !(tableInt32Def instanceof CFBamRamInt32DefTable)) {
			tableInt32Def = new CFBamRamInt32DefTable(this);
		}
		if (tableInt32Type == null || !(tableInt32Type instanceof CFBamRamInt32TypeTable)) {
			tableInt32Type = new CFBamRamInt32TypeTable(this);
		}
		if (tableInt64Def == null || !(tableInt64Def instanceof CFBamRamInt64DefTable)) {
			tableInt64Def = new CFBamRamInt64DefTable(this);
		}
		if (tableInt64Type == null || !(tableInt64Type instanceof CFBamRamInt64TypeTable)) {
			tableInt64Type = new CFBamRamInt64TypeTable(this);
		}
		if (tableNmTokenDef == null || !(tableNmTokenDef instanceof CFBamRamNmTokenDefTable)) {
			tableNmTokenDef = new CFBamRamNmTokenDefTable(this);
		}
		if (tableNmTokenType == null || !(tableNmTokenType instanceof CFBamRamNmTokenTypeTable)) {
			tableNmTokenType = new CFBamRamNmTokenTypeTable(this);
		}
		if (tableNmTokensDef == null || !(tableNmTokensDef instanceof CFBamRamNmTokensDefTable)) {
			tableNmTokensDef = new CFBamRamNmTokensDefTable(this);
		}
		if (tableNmTokensType == null || !(tableNmTokensType instanceof CFBamRamNmTokensTypeTable)) {
			tableNmTokensType = new CFBamRamNmTokensTypeTable(this);
		}
		if (tableNumberDef == null || !(tableNumberDef instanceof CFBamRamNumberDefTable)) {
			tableNumberDef = new CFBamRamNumberDefTable(this);
		}
		if (tableNumberType == null || !(tableNumberType instanceof CFBamRamNumberTypeTable)) {
			tableNumberType = new CFBamRamNumberTypeTable(this);
		}
		if (tableParam == null || !(tableParam instanceof CFBamRamParamTable)) {
			tableParam = new CFBamRamParamTable(this);
		}
		if (tablePopDep == null || !(tablePopDep instanceof CFBamRamPopDepTable)) {
			tablePopDep = new CFBamRamPopDepTable(this);
		}
		if (tablePopSubDep1 == null || !(tablePopSubDep1 instanceof CFBamRamPopSubDep1Table)) {
			tablePopSubDep1 = new CFBamRamPopSubDep1Table(this);
		}
		if (tablePopSubDep2 == null || !(tablePopSubDep2 instanceof CFBamRamPopSubDep2Table)) {
			tablePopSubDep2 = new CFBamRamPopSubDep2Table(this);
		}
		if (tablePopSubDep3 == null || !(tablePopSubDep3 instanceof CFBamRamPopSubDep3Table)) {
			tablePopSubDep3 = new CFBamRamPopSubDep3Table(this);
		}
		if (tablePopTopDep == null || !(tablePopTopDep instanceof CFBamRamPopTopDepTable)) {
			tablePopTopDep = new CFBamRamPopTopDepTable(this);
		}
		if (tableRelation == null || !(tableRelation instanceof CFBamRamRelationTable)) {
			tableRelation = new CFBamRamRelationTable(this);
		}
		if (tableRelationCol == null || !(tableRelationCol instanceof CFBamRamRelationColTable)) {
			tableRelationCol = new CFBamRamRelationColTable(this);
		}
		if (tableServerListFunc == null || !(tableServerListFunc instanceof CFBamRamServerListFuncTable)) {
			tableServerListFunc = new CFBamRamServerListFuncTable(this);
		}
		if (tableDbKeyHash128Def == null || !(tableDbKeyHash128Def instanceof CFBamRamDbKeyHash128DefTable)) {
			tableDbKeyHash128Def = new CFBamRamDbKeyHash128DefTable(this);
		}
		if (tableDbKeyHash128Col == null || !(tableDbKeyHash128Col instanceof CFBamRamDbKeyHash128ColTable)) {
			tableDbKeyHash128Col = new CFBamRamDbKeyHash128ColTable(this);
		}
		if (tableDbKeyHash128Type == null || !(tableDbKeyHash128Type instanceof CFBamRamDbKeyHash128TypeTable)) {
			tableDbKeyHash128Type = new CFBamRamDbKeyHash128TypeTable(this);
		}
		if (tableDbKeyHash128Gen == null || !(tableDbKeyHash128Gen instanceof CFBamRamDbKeyHash128GenTable)) {
			tableDbKeyHash128Gen = new CFBamRamDbKeyHash128GenTable(this);
		}
		if (tableDbKeyHash160Def == null || !(tableDbKeyHash160Def instanceof CFBamRamDbKeyHash160DefTable)) {
			tableDbKeyHash160Def = new CFBamRamDbKeyHash160DefTable(this);
		}
		if (tableDbKeyHash160Col == null || !(tableDbKeyHash160Col instanceof CFBamRamDbKeyHash160ColTable)) {
			tableDbKeyHash160Col = new CFBamRamDbKeyHash160ColTable(this);
		}
		if (tableDbKeyHash160Type == null || !(tableDbKeyHash160Type instanceof CFBamRamDbKeyHash160TypeTable)) {
			tableDbKeyHash160Type = new CFBamRamDbKeyHash160TypeTable(this);
		}
		if (tableDbKeyHash160Gen == null || !(tableDbKeyHash160Gen instanceof CFBamRamDbKeyHash160GenTable)) {
			tableDbKeyHash160Gen = new CFBamRamDbKeyHash160GenTable(this);
		}
		if (tableDbKeyHash224Def == null || !(tableDbKeyHash224Def instanceof CFBamRamDbKeyHash224DefTable)) {
			tableDbKeyHash224Def = new CFBamRamDbKeyHash224DefTable(this);
		}
		if (tableDbKeyHash224Col == null || !(tableDbKeyHash224Col instanceof CFBamRamDbKeyHash224ColTable)) {
			tableDbKeyHash224Col = new CFBamRamDbKeyHash224ColTable(this);
		}
		if (tableDbKeyHash224Type == null || !(tableDbKeyHash224Type instanceof CFBamRamDbKeyHash224TypeTable)) {
			tableDbKeyHash224Type = new CFBamRamDbKeyHash224TypeTable(this);
		}
		if (tableDbKeyHash224Gen == null || !(tableDbKeyHash224Gen instanceof CFBamRamDbKeyHash224GenTable)) {
			tableDbKeyHash224Gen = new CFBamRamDbKeyHash224GenTable(this);
		}
		if (tableDbKeyHash256Def == null || !(tableDbKeyHash256Def instanceof CFBamRamDbKeyHash256DefTable)) {
			tableDbKeyHash256Def = new CFBamRamDbKeyHash256DefTable(this);
		}
		if (tableDbKeyHash256Col == null || !(tableDbKeyHash256Col instanceof CFBamRamDbKeyHash256ColTable)) {
			tableDbKeyHash256Col = new CFBamRamDbKeyHash256ColTable(this);
		}
		if (tableDbKeyHash256Type == null || !(tableDbKeyHash256Type instanceof CFBamRamDbKeyHash256TypeTable)) {
			tableDbKeyHash256Type = new CFBamRamDbKeyHash256TypeTable(this);
		}
		if (tableDbKeyHash256Gen == null || !(tableDbKeyHash256Gen instanceof CFBamRamDbKeyHash256GenTable)) {
			tableDbKeyHash256Gen = new CFBamRamDbKeyHash256GenTable(this);
		}
		if (tableDbKeyHash384Def == null || !(tableDbKeyHash384Def instanceof CFBamRamDbKeyHash384DefTable)) {
			tableDbKeyHash384Def = new CFBamRamDbKeyHash384DefTable(this);
		}
		if (tableDbKeyHash384Col == null || !(tableDbKeyHash384Col instanceof CFBamRamDbKeyHash384ColTable)) {
			tableDbKeyHash384Col = new CFBamRamDbKeyHash384ColTable(this);
		}
		if (tableDbKeyHash384Type == null || !(tableDbKeyHash384Type instanceof CFBamRamDbKeyHash384TypeTable)) {
			tableDbKeyHash384Type = new CFBamRamDbKeyHash384TypeTable(this);
		}
		if (tableDbKeyHash384Gen == null || !(tableDbKeyHash384Gen instanceof CFBamRamDbKeyHash384GenTable)) {
			tableDbKeyHash384Gen = new CFBamRamDbKeyHash384GenTable(this);
		}
		if (tableDbKeyHash512Def == null || !(tableDbKeyHash512Def instanceof CFBamRamDbKeyHash512DefTable)) {
			tableDbKeyHash512Def = new CFBamRamDbKeyHash512DefTable(this);
		}
		if (tableDbKeyHash512Col == null || !(tableDbKeyHash512Col instanceof CFBamRamDbKeyHash512ColTable)) {
			tableDbKeyHash512Col = new CFBamRamDbKeyHash512ColTable(this);
		}
		if (tableDbKeyHash512Type == null || !(tableDbKeyHash512Type instanceof CFBamRamDbKeyHash512TypeTable)) {
			tableDbKeyHash512Type = new CFBamRamDbKeyHash512TypeTable(this);
		}
		if (tableDbKeyHash512Gen == null || !(tableDbKeyHash512Gen instanceof CFBamRamDbKeyHash512GenTable)) {
			tableDbKeyHash512Gen = new CFBamRamDbKeyHash512GenTable(this);
		}
		if (tableStringDef == null || !(tableStringDef instanceof CFBamRamStringDefTable)) {
			tableStringDef = new CFBamRamStringDefTable(this);
		}
		if (tableStringType == null || !(tableStringType instanceof CFBamRamStringTypeTable)) {
			tableStringType = new CFBamRamStringTypeTable(this);
		}
		if (tableTZDateDef == null || !(tableTZDateDef instanceof CFBamRamTZDateDefTable)) {
			tableTZDateDef = new CFBamRamTZDateDefTable(this);
		}
		if (tableTZDateType == null || !(tableTZDateType instanceof CFBamRamTZDateTypeTable)) {
			tableTZDateType = new CFBamRamTZDateTypeTable(this);
		}
		if (tableTZTimeDef == null || !(tableTZTimeDef instanceof CFBamRamTZTimeDefTable)) {
			tableTZTimeDef = new CFBamRamTZTimeDefTable(this);
		}
		if (tableTZTimeType == null || !(tableTZTimeType instanceof CFBamRamTZTimeTypeTable)) {
			tableTZTimeType = new CFBamRamTZTimeTypeTable(this);
		}
		if (tableTZTimestampDef == null || !(tableTZTimestampDef instanceof CFBamRamTZTimestampDefTable)) {
			tableTZTimestampDef = new CFBamRamTZTimestampDefTable(this);
		}
		if (tableTZTimestampType == null || !(tableTZTimestampType instanceof CFBamRamTZTimestampTypeTable)) {
			tableTZTimestampType = new CFBamRamTZTimestampTypeTable(this);
		}
		if (tableTableCol == null || !(tableTableCol instanceof CFBamRamTableColTable)) {
			tableTableCol = new CFBamRamTableColTable(this);
		}
		if (tableTextDef == null || !(tableTextDef instanceof CFBamRamTextDefTable)) {
			tableTextDef = new CFBamRamTextDefTable(this);
		}
		if (tableTextType == null || !(tableTextType instanceof CFBamRamTextTypeTable)) {
			tableTextType = new CFBamRamTextTypeTable(this);
		}
		if (tableTimeDef == null || !(tableTimeDef instanceof CFBamRamTimeDefTable)) {
			tableTimeDef = new CFBamRamTimeDefTable(this);
		}
		if (tableTimeType == null || !(tableTimeType instanceof CFBamRamTimeTypeTable)) {
			tableTimeType = new CFBamRamTimeTypeTable(this);
		}
		if (tableTimestampDef == null || !(tableTimestampDef instanceof CFBamRamTimestampDefTable)) {
			tableTimestampDef = new CFBamRamTimestampDefTable(this);
		}
		if (tableTimestampType == null || !(tableTimestampType instanceof CFBamRamTimestampTypeTable)) {
			tableTimestampType = new CFBamRamTimestampTypeTable(this);
		}
		if (tableTokenDef == null || !(tableTokenDef instanceof CFBamRamTokenDefTable)) {
			tableTokenDef = new CFBamRamTokenDefTable(this);
		}
		if (tableTokenType == null || !(tableTokenType instanceof CFBamRamTokenTypeTable)) {
			tableTokenType = new CFBamRamTokenTypeTable(this);
		}
		if (tableUInt16Def == null || !(tableUInt16Def instanceof CFBamRamUInt16DefTable)) {
			tableUInt16Def = new CFBamRamUInt16DefTable(this);
		}
		if (tableUInt16Type == null || !(tableUInt16Type instanceof CFBamRamUInt16TypeTable)) {
			tableUInt16Type = new CFBamRamUInt16TypeTable(this);
		}
		if (tableUInt32Def == null || !(tableUInt32Def instanceof CFBamRamUInt32DefTable)) {
			tableUInt32Def = new CFBamRamUInt32DefTable(this);
		}
		if (tableUInt32Type == null || !(tableUInt32Type instanceof CFBamRamUInt32TypeTable)) {
			tableUInt32Type = new CFBamRamUInt32TypeTable(this);
		}
		if (tableUInt64Def == null || !(tableUInt64Def instanceof CFBamRamUInt64DefTable)) {
			tableUInt64Def = new CFBamRamUInt64DefTable(this);
		}
		if (tableUInt64Type == null || !(tableUInt64Type instanceof CFBamRamUInt64TypeTable)) {
			tableUInt64Type = new CFBamRamUInt64TypeTable(this);
		}
		if (tableUuidDef == null || !(tableUuidDef instanceof CFBamRamUuidDefTable)) {
			tableUuidDef = new CFBamRamUuidDefTable(this);
		}
		if (tableUuid6Def == null || !(tableUuid6Def instanceof CFBamRamUuid6DefTable)) {
			tableUuid6Def = new CFBamRamUuid6DefTable(this);
		}
		if (tableUuidType == null || !(tableUuidType instanceof CFBamRamUuidTypeTable)) {
			tableUuidType = new CFBamRamUuidTypeTable(this);
		}
		if (tableUuid6Type == null || !(tableUuid6Type instanceof CFBamRamUuid6TypeTable)) {
			tableUuid6Type = new CFBamRamUuid6TypeTable(this);
		}
		if (tableBlobCol == null || !(tableBlobCol instanceof CFBamRamBlobColTable)) {
			tableBlobCol = new CFBamRamBlobColTable(this);
		}
		if (tableBoolCol == null || !(tableBoolCol instanceof CFBamRamBoolColTable)) {
			tableBoolCol = new CFBamRamBoolColTable(this);
		}
		if (tableDateCol == null || !(tableDateCol instanceof CFBamRamDateColTable)) {
			tableDateCol = new CFBamRamDateColTable(this);
		}
		if (tableDoubleCol == null || !(tableDoubleCol instanceof CFBamRamDoubleColTable)) {
			tableDoubleCol = new CFBamRamDoubleColTable(this);
		}
		if (tableEnumDef == null || !(tableEnumDef instanceof CFBamRamEnumDefTable)) {
			tableEnumDef = new CFBamRamEnumDefTable(this);
		}
		if (tableEnumType == null || !(tableEnumType instanceof CFBamRamEnumTypeTable)) {
			tableEnumType = new CFBamRamEnumTypeTable(this);
		}
		if (tableFloatCol == null || !(tableFloatCol instanceof CFBamRamFloatColTable)) {
			tableFloatCol = new CFBamRamFloatColTable(this);
		}
		if (tableId16Gen == null || !(tableId16Gen instanceof CFBamRamId16GenTable)) {
			tableId16Gen = new CFBamRamId16GenTable(this);
		}
		if (tableId32Gen == null || !(tableId32Gen instanceof CFBamRamId32GenTable)) {
			tableId32Gen = new CFBamRamId32GenTable(this);
		}
		if (tableId64Gen == null || !(tableId64Gen instanceof CFBamRamId64GenTable)) {
			tableId64Gen = new CFBamRamId64GenTable(this);
		}
		if (tableInt16Col == null || !(tableInt16Col instanceof CFBamRamInt16ColTable)) {
			tableInt16Col = new CFBamRamInt16ColTable(this);
		}
		if (tableInt32Col == null || !(tableInt32Col instanceof CFBamRamInt32ColTable)) {
			tableInt32Col = new CFBamRamInt32ColTable(this);
		}
		if (tableInt64Col == null || !(tableInt64Col instanceof CFBamRamInt64ColTable)) {
			tableInt64Col = new CFBamRamInt64ColTable(this);
		}
		if (tableNmTokenCol == null || !(tableNmTokenCol instanceof CFBamRamNmTokenColTable)) {
			tableNmTokenCol = new CFBamRamNmTokenColTable(this);
		}
		if (tableNmTokensCol == null || !(tableNmTokensCol instanceof CFBamRamNmTokensColTable)) {
			tableNmTokensCol = new CFBamRamNmTokensColTable(this);
		}
		if (tableNumberCol == null || !(tableNumberCol instanceof CFBamRamNumberColTable)) {
			tableNumberCol = new CFBamRamNumberColTable(this);
		}
		if (tableStringCol == null || !(tableStringCol instanceof CFBamRamStringColTable)) {
			tableStringCol = new CFBamRamStringColTable(this);
		}
		if (tableTZDateCol == null || !(tableTZDateCol instanceof CFBamRamTZDateColTable)) {
			tableTZDateCol = new CFBamRamTZDateColTable(this);
		}
		if (tableTZTimeCol == null || !(tableTZTimeCol instanceof CFBamRamTZTimeColTable)) {
			tableTZTimeCol = new CFBamRamTZTimeColTable(this);
		}
		if (tableTZTimestampCol == null || !(tableTZTimestampCol instanceof CFBamRamTZTimestampColTable)) {
			tableTZTimestampCol = new CFBamRamTZTimestampColTable(this);
		}
		if (tableTextCol == null || !(tableTextCol instanceof CFBamRamTextColTable)) {
			tableTextCol = new CFBamRamTextColTable(this);
		}
		if (tableTimeCol == null || !(tableTimeCol instanceof CFBamRamTimeColTable)) {
			tableTimeCol = new CFBamRamTimeColTable(this);
		}
		if (tableTimestampCol == null || !(tableTimestampCol instanceof CFBamRamTimestampColTable)) {
			tableTimestampCol = new CFBamRamTimestampColTable(this);
		}
		if (tableTokenCol == null || !(tableTokenCol instanceof CFBamRamTokenColTable)) {
			tableTokenCol = new CFBamRamTokenColTable(this);
		}
		if (tableUInt16Col == null || !(tableUInt16Col instanceof CFBamRamUInt16ColTable)) {
			tableUInt16Col = new CFBamRamUInt16ColTable(this);
		}
		if (tableUInt32Col == null || !(tableUInt32Col instanceof CFBamRamUInt32ColTable)) {
			tableUInt32Col = new CFBamRamUInt32ColTable(this);
		}
		if (tableUInt64Col == null || !(tableUInt64Col instanceof CFBamRamUInt64ColTable)) {
			tableUInt64Col = new CFBamRamUInt64ColTable(this);
		}
		if (tableUuidCol == null || !(tableUuidCol instanceof CFBamRamUuidColTable)) {
			tableUuidCol = new CFBamRamUuidColTable(this);
		}
		if (tableUuid6Col == null || !(tableUuid6Col instanceof CFBamRamUuid6ColTable)) {
			tableUuid6Col = new CFBamRamUuid6ColTable(this);
		}
		if (tableUuidGen == null || !(tableUuidGen instanceof CFBamRamUuidGenTable)) {
			tableUuidGen = new CFBamRamUuidGenTable(this);
		}
		if (tableUuid6Gen == null || !(tableUuid6Gen instanceof CFBamRamUuid6GenTable)) {
			tableUuid6Gen = new CFBamRamUuid6GenTable(this);
		}
		if (tableRoleDef == null || !(tableRoleDef instanceof CFBamRamRoleDefTable)) {
			tableRoleDef = new CFBamRamRoleDefTable(this);
		}
		if (tableSchemaRole == null || !(tableSchemaRole instanceof CFBamRamSchemaRoleTable)) {
			tableSchemaRole = new CFBamRamSchemaRoleTable(this);
		}
	}
}
