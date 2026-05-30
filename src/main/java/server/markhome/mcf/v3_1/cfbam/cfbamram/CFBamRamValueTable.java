
// Description: Java 25 in-memory RAM DbIO implementation for Value.

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
 *	CFBamRamValueTable in-memory RAM DbIO implementation
 *	for Value.
 */
public class CFBamRamValueTable
	implements ICFBamValueTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffValue > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffValue >();
	private Map< CFBamBuffValueByUNameIdxKey,
			CFBamBuffValue > dictByUNameIdx
		= new HashMap< CFBamBuffValueByUNameIdxKey,
			CFBamBuffValue >();
	private Map< CFBamBuffValueByScopeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >> dictByScopeIdx
		= new HashMap< CFBamBuffValueByScopeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >>();
	private Map< CFBamBuffValueByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffValueByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >>();
	private Map< CFBamBuffValueByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >> dictByPrevIdx
		= new HashMap< CFBamBuffValueByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >>();
	private Map< CFBamBuffValueByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >> dictByNextIdx
		= new HashMap< CFBamBuffValueByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >>();
	private Map< CFBamBuffValueByContPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >> dictByContPrevIdx
		= new HashMap< CFBamBuffValueByContPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >>();
	private Map< CFBamBuffValueByContNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >> dictByContNextIdx
		= new HashMap< CFBamBuffValueByContNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffValue >>();

	public CFBamRamValueTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffValue ensureRec(ICFBamValue rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			if (classCode == ICFBamValue.CLASS_CODE) {
				return( ((CFBamBuffValueDefaultFactory)(schema.getFactoryValue())).ensureRec((ICFBamValue)rec) );
			}
			else if (classCode == ICFBamAtom.CLASS_CODE) {
				return( ((CFBamBuffAtomDefaultFactory)(schema.getFactoryAtom())).ensureRec((ICFBamAtom)rec) );
			}
			else if (classCode == ICFBamBlobDef.CLASS_CODE) {
				return( ((CFBamBuffBlobDefDefaultFactory)(schema.getFactoryBlobDef())).ensureRec((ICFBamBlobDef)rec) );
			}
			else if (classCode == ICFBamBlobType.CLASS_CODE) {
				return( ((CFBamBuffBlobTypeDefaultFactory)(schema.getFactoryBlobType())).ensureRec((ICFBamBlobType)rec) );
			}
			else if (classCode == ICFBamBlobCol.CLASS_CODE) {
				return( ((CFBamBuffBlobColDefaultFactory)(schema.getFactoryBlobCol())).ensureRec((ICFBamBlobCol)rec) );
			}
			else if (classCode == ICFBamBoolDef.CLASS_CODE) {
				return( ((CFBamBuffBoolDefDefaultFactory)(schema.getFactoryBoolDef())).ensureRec((ICFBamBoolDef)rec) );
			}
			else if (classCode == ICFBamBoolType.CLASS_CODE) {
				return( ((CFBamBuffBoolTypeDefaultFactory)(schema.getFactoryBoolType())).ensureRec((ICFBamBoolType)rec) );
			}
			else if (classCode == ICFBamBoolCol.CLASS_CODE) {
				return( ((CFBamBuffBoolColDefaultFactory)(schema.getFactoryBoolCol())).ensureRec((ICFBamBoolCol)rec) );
			}
			else if (classCode == ICFBamDateDef.CLASS_CODE) {
				return( ((CFBamBuffDateDefDefaultFactory)(schema.getFactoryDateDef())).ensureRec((ICFBamDateDef)rec) );
			}
			else if (classCode == ICFBamDateType.CLASS_CODE) {
				return( ((CFBamBuffDateTypeDefaultFactory)(schema.getFactoryDateType())).ensureRec((ICFBamDateType)rec) );
			}
			else if (classCode == ICFBamDateCol.CLASS_CODE) {
				return( ((CFBamBuffDateColDefaultFactory)(schema.getFactoryDateCol())).ensureRec((ICFBamDateCol)rec) );
			}
			else if (classCode == ICFBamDoubleDef.CLASS_CODE) {
				return( ((CFBamBuffDoubleDefDefaultFactory)(schema.getFactoryDoubleDef())).ensureRec((ICFBamDoubleDef)rec) );
			}
			else if (classCode == ICFBamDoubleType.CLASS_CODE) {
				return( ((CFBamBuffDoubleTypeDefaultFactory)(schema.getFactoryDoubleType())).ensureRec((ICFBamDoubleType)rec) );
			}
			else if (classCode == ICFBamDoubleCol.CLASS_CODE) {
				return( ((CFBamBuffDoubleColDefaultFactory)(schema.getFactoryDoubleCol())).ensureRec((ICFBamDoubleCol)rec) );
			}
			else if (classCode == ICFBamFloatDef.CLASS_CODE) {
				return( ((CFBamBuffFloatDefDefaultFactory)(schema.getFactoryFloatDef())).ensureRec((ICFBamFloatDef)rec) );
			}
			else if (classCode == ICFBamFloatType.CLASS_CODE) {
				return( ((CFBamBuffFloatTypeDefaultFactory)(schema.getFactoryFloatType())).ensureRec((ICFBamFloatType)rec) );
			}
			else if (classCode == ICFBamFloatCol.CLASS_CODE) {
				return( ((CFBamBuffFloatColDefaultFactory)(schema.getFactoryFloatCol())).ensureRec((ICFBamFloatCol)rec) );
			}
			else if (classCode == ICFBamInt16Def.CLASS_CODE) {
				return( ((CFBamBuffInt16DefDefaultFactory)(schema.getFactoryInt16Def())).ensureRec((ICFBamInt16Def)rec) );
			}
			else if (classCode == ICFBamInt16Type.CLASS_CODE) {
				return( ((CFBamBuffInt16TypeDefaultFactory)(schema.getFactoryInt16Type())).ensureRec((ICFBamInt16Type)rec) );
			}
			else if (classCode == ICFBamId16Gen.CLASS_CODE) {
				return( ((CFBamBuffId16GenDefaultFactory)(schema.getFactoryId16Gen())).ensureRec((ICFBamId16Gen)rec) );
			}
			else if (classCode == ICFBamEnumDef.CLASS_CODE) {
				return( ((CFBamBuffEnumDefDefaultFactory)(schema.getFactoryEnumDef())).ensureRec((ICFBamEnumDef)rec) );
			}
			else if (classCode == ICFBamEnumType.CLASS_CODE) {
				return( ((CFBamBuffEnumTypeDefaultFactory)(schema.getFactoryEnumType())).ensureRec((ICFBamEnumType)rec) );
			}
			else if (classCode == ICFBamInt16Col.CLASS_CODE) {
				return( ((CFBamBuffInt16ColDefaultFactory)(schema.getFactoryInt16Col())).ensureRec((ICFBamInt16Col)rec) );
			}
			else if (classCode == ICFBamInt32Def.CLASS_CODE) {
				return( ((CFBamBuffInt32DefDefaultFactory)(schema.getFactoryInt32Def())).ensureRec((ICFBamInt32Def)rec) );
			}
			else if (classCode == ICFBamInt32Type.CLASS_CODE) {
				return( ((CFBamBuffInt32TypeDefaultFactory)(schema.getFactoryInt32Type())).ensureRec((ICFBamInt32Type)rec) );
			}
			else if (classCode == ICFBamId32Gen.CLASS_CODE) {
				return( ((CFBamBuffId32GenDefaultFactory)(schema.getFactoryId32Gen())).ensureRec((ICFBamId32Gen)rec) );
			}
			else if (classCode == ICFBamInt32Col.CLASS_CODE) {
				return( ((CFBamBuffInt32ColDefaultFactory)(schema.getFactoryInt32Col())).ensureRec((ICFBamInt32Col)rec) );
			}
			else if (classCode == ICFBamInt64Def.CLASS_CODE) {
				return( ((CFBamBuffInt64DefDefaultFactory)(schema.getFactoryInt64Def())).ensureRec((ICFBamInt64Def)rec) );
			}
			else if (classCode == ICFBamInt64Type.CLASS_CODE) {
				return( ((CFBamBuffInt64TypeDefaultFactory)(schema.getFactoryInt64Type())).ensureRec((ICFBamInt64Type)rec) );
			}
			else if (classCode == ICFBamId64Gen.CLASS_CODE) {
				return( ((CFBamBuffId64GenDefaultFactory)(schema.getFactoryId64Gen())).ensureRec((ICFBamId64Gen)rec) );
			}
			else if (classCode == ICFBamInt64Col.CLASS_CODE) {
				return( ((CFBamBuffInt64ColDefaultFactory)(schema.getFactoryInt64Col())).ensureRec((ICFBamInt64Col)rec) );
			}
			else if (classCode == ICFBamNmTokenDef.CLASS_CODE) {
				return( ((CFBamBuffNmTokenDefDefaultFactory)(schema.getFactoryNmTokenDef())).ensureRec((ICFBamNmTokenDef)rec) );
			}
			else if (classCode == ICFBamNmTokenType.CLASS_CODE) {
				return( ((CFBamBuffNmTokenTypeDefaultFactory)(schema.getFactoryNmTokenType())).ensureRec((ICFBamNmTokenType)rec) );
			}
			else if (classCode == ICFBamNmTokenCol.CLASS_CODE) {
				return( ((CFBamBuffNmTokenColDefaultFactory)(schema.getFactoryNmTokenCol())).ensureRec((ICFBamNmTokenCol)rec) );
			}
			else if (classCode == ICFBamNmTokensDef.CLASS_CODE) {
				return( ((CFBamBuffNmTokensDefDefaultFactory)(schema.getFactoryNmTokensDef())).ensureRec((ICFBamNmTokensDef)rec) );
			}
			else if (classCode == ICFBamNmTokensType.CLASS_CODE) {
				return( ((CFBamBuffNmTokensTypeDefaultFactory)(schema.getFactoryNmTokensType())).ensureRec((ICFBamNmTokensType)rec) );
			}
			else if (classCode == ICFBamNmTokensCol.CLASS_CODE) {
				return( ((CFBamBuffNmTokensColDefaultFactory)(schema.getFactoryNmTokensCol())).ensureRec((ICFBamNmTokensCol)rec) );
			}
			else if (classCode == ICFBamNumberDef.CLASS_CODE) {
				return( ((CFBamBuffNumberDefDefaultFactory)(schema.getFactoryNumberDef())).ensureRec((ICFBamNumberDef)rec) );
			}
			else if (classCode == ICFBamNumberType.CLASS_CODE) {
				return( ((CFBamBuffNumberTypeDefaultFactory)(schema.getFactoryNumberType())).ensureRec((ICFBamNumberType)rec) );
			}
			else if (classCode == ICFBamNumberCol.CLASS_CODE) {
				return( ((CFBamBuffNumberColDefaultFactory)(schema.getFactoryNumberCol())).ensureRec((ICFBamNumberCol)rec) );
			}
			else if (classCode == ICFBamDbKeyHash128Def.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash128DefDefaultFactory)(schema.getFactoryDbKeyHash128Def())).ensureRec((ICFBamDbKeyHash128Def)rec) );
			}
			else if (classCode == ICFBamDbKeyHash128Col.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash128ColDefaultFactory)(schema.getFactoryDbKeyHash128Col())).ensureRec((ICFBamDbKeyHash128Col)rec) );
			}
			else if (classCode == ICFBamDbKeyHash128Type.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash128TypeDefaultFactory)(schema.getFactoryDbKeyHash128Type())).ensureRec((ICFBamDbKeyHash128Type)rec) );
			}
			else if (classCode == ICFBamDbKeyHash128Gen.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash128GenDefaultFactory)(schema.getFactoryDbKeyHash128Gen())).ensureRec((ICFBamDbKeyHash128Gen)rec) );
			}
			else if (classCode == ICFBamDbKeyHash160Def.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash160DefDefaultFactory)(schema.getFactoryDbKeyHash160Def())).ensureRec((ICFBamDbKeyHash160Def)rec) );
			}
			else if (classCode == ICFBamDbKeyHash160Col.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash160ColDefaultFactory)(schema.getFactoryDbKeyHash160Col())).ensureRec((ICFBamDbKeyHash160Col)rec) );
			}
			else if (classCode == ICFBamDbKeyHash160Type.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash160TypeDefaultFactory)(schema.getFactoryDbKeyHash160Type())).ensureRec((ICFBamDbKeyHash160Type)rec) );
			}
			else if (classCode == ICFBamDbKeyHash160Gen.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash160GenDefaultFactory)(schema.getFactoryDbKeyHash160Gen())).ensureRec((ICFBamDbKeyHash160Gen)rec) );
			}
			else if (classCode == ICFBamDbKeyHash224Def.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash224DefDefaultFactory)(schema.getFactoryDbKeyHash224Def())).ensureRec((ICFBamDbKeyHash224Def)rec) );
			}
			else if (classCode == ICFBamDbKeyHash224Col.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash224ColDefaultFactory)(schema.getFactoryDbKeyHash224Col())).ensureRec((ICFBamDbKeyHash224Col)rec) );
			}
			else if (classCode == ICFBamDbKeyHash224Type.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash224TypeDefaultFactory)(schema.getFactoryDbKeyHash224Type())).ensureRec((ICFBamDbKeyHash224Type)rec) );
			}
			else if (classCode == ICFBamDbKeyHash224Gen.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash224GenDefaultFactory)(schema.getFactoryDbKeyHash224Gen())).ensureRec((ICFBamDbKeyHash224Gen)rec) );
			}
			else if (classCode == ICFBamDbKeyHash256Def.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash256DefDefaultFactory)(schema.getFactoryDbKeyHash256Def())).ensureRec((ICFBamDbKeyHash256Def)rec) );
			}
			else if (classCode == ICFBamDbKeyHash256Col.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash256ColDefaultFactory)(schema.getFactoryDbKeyHash256Col())).ensureRec((ICFBamDbKeyHash256Col)rec) );
			}
			else if (classCode == ICFBamDbKeyHash256Type.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash256TypeDefaultFactory)(schema.getFactoryDbKeyHash256Type())).ensureRec((ICFBamDbKeyHash256Type)rec) );
			}
			else if (classCode == ICFBamDbKeyHash256Gen.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash256GenDefaultFactory)(schema.getFactoryDbKeyHash256Gen())).ensureRec((ICFBamDbKeyHash256Gen)rec) );
			}
			else if (classCode == ICFBamDbKeyHash384Def.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash384DefDefaultFactory)(schema.getFactoryDbKeyHash384Def())).ensureRec((ICFBamDbKeyHash384Def)rec) );
			}
			else if (classCode == ICFBamDbKeyHash384Col.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash384ColDefaultFactory)(schema.getFactoryDbKeyHash384Col())).ensureRec((ICFBamDbKeyHash384Col)rec) );
			}
			else if (classCode == ICFBamDbKeyHash384Type.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash384TypeDefaultFactory)(schema.getFactoryDbKeyHash384Type())).ensureRec((ICFBamDbKeyHash384Type)rec) );
			}
			else if (classCode == ICFBamDbKeyHash384Gen.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash384GenDefaultFactory)(schema.getFactoryDbKeyHash384Gen())).ensureRec((ICFBamDbKeyHash384Gen)rec) );
			}
			else if (classCode == ICFBamDbKeyHash512Def.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash512DefDefaultFactory)(schema.getFactoryDbKeyHash512Def())).ensureRec((ICFBamDbKeyHash512Def)rec) );
			}
			else if (classCode == ICFBamDbKeyHash512Col.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash512ColDefaultFactory)(schema.getFactoryDbKeyHash512Col())).ensureRec((ICFBamDbKeyHash512Col)rec) );
			}
			else if (classCode == ICFBamDbKeyHash512Type.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash512TypeDefaultFactory)(schema.getFactoryDbKeyHash512Type())).ensureRec((ICFBamDbKeyHash512Type)rec) );
			}
			else if (classCode == ICFBamDbKeyHash512Gen.CLASS_CODE) {
				return( ((CFBamBuffDbKeyHash512GenDefaultFactory)(schema.getFactoryDbKeyHash512Gen())).ensureRec((ICFBamDbKeyHash512Gen)rec) );
			}
			else if (classCode == ICFBamStringDef.CLASS_CODE) {
				return( ((CFBamBuffStringDefDefaultFactory)(schema.getFactoryStringDef())).ensureRec((ICFBamStringDef)rec) );
			}
			else if (classCode == ICFBamStringType.CLASS_CODE) {
				return( ((CFBamBuffStringTypeDefaultFactory)(schema.getFactoryStringType())).ensureRec((ICFBamStringType)rec) );
			}
			else if (classCode == ICFBamStringCol.CLASS_CODE) {
				return( ((CFBamBuffStringColDefaultFactory)(schema.getFactoryStringCol())).ensureRec((ICFBamStringCol)rec) );
			}
			else if (classCode == ICFBamTZDateDef.CLASS_CODE) {
				return( ((CFBamBuffTZDateDefDefaultFactory)(schema.getFactoryTZDateDef())).ensureRec((ICFBamTZDateDef)rec) );
			}
			else if (classCode == ICFBamTZDateType.CLASS_CODE) {
				return( ((CFBamBuffTZDateTypeDefaultFactory)(schema.getFactoryTZDateType())).ensureRec((ICFBamTZDateType)rec) );
			}
			else if (classCode == ICFBamTZDateCol.CLASS_CODE) {
				return( ((CFBamBuffTZDateColDefaultFactory)(schema.getFactoryTZDateCol())).ensureRec((ICFBamTZDateCol)rec) );
			}
			else if (classCode == ICFBamTZTimeDef.CLASS_CODE) {
				return( ((CFBamBuffTZTimeDefDefaultFactory)(schema.getFactoryTZTimeDef())).ensureRec((ICFBamTZTimeDef)rec) );
			}
			else if (classCode == ICFBamTZTimeType.CLASS_CODE) {
				return( ((CFBamBuffTZTimeTypeDefaultFactory)(schema.getFactoryTZTimeType())).ensureRec((ICFBamTZTimeType)rec) );
			}
			else if (classCode == ICFBamTZTimeCol.CLASS_CODE) {
				return( ((CFBamBuffTZTimeColDefaultFactory)(schema.getFactoryTZTimeCol())).ensureRec((ICFBamTZTimeCol)rec) );
			}
			else if (classCode == ICFBamTZTimestampDef.CLASS_CODE) {
				return( ((CFBamBuffTZTimestampDefDefaultFactory)(schema.getFactoryTZTimestampDef())).ensureRec((ICFBamTZTimestampDef)rec) );
			}
			else if (classCode == ICFBamTZTimestampType.CLASS_CODE) {
				return( ((CFBamBuffTZTimestampTypeDefaultFactory)(schema.getFactoryTZTimestampType())).ensureRec((ICFBamTZTimestampType)rec) );
			}
			else if (classCode == ICFBamTZTimestampCol.CLASS_CODE) {
				return( ((CFBamBuffTZTimestampColDefaultFactory)(schema.getFactoryTZTimestampCol())).ensureRec((ICFBamTZTimestampCol)rec) );
			}
			else if (classCode == ICFBamTextDef.CLASS_CODE) {
				return( ((CFBamBuffTextDefDefaultFactory)(schema.getFactoryTextDef())).ensureRec((ICFBamTextDef)rec) );
			}
			else if (classCode == ICFBamTextType.CLASS_CODE) {
				return( ((CFBamBuffTextTypeDefaultFactory)(schema.getFactoryTextType())).ensureRec((ICFBamTextType)rec) );
			}
			else if (classCode == ICFBamTextCol.CLASS_CODE) {
				return( ((CFBamBuffTextColDefaultFactory)(schema.getFactoryTextCol())).ensureRec((ICFBamTextCol)rec) );
			}
			else if (classCode == ICFBamTimeDef.CLASS_CODE) {
				return( ((CFBamBuffTimeDefDefaultFactory)(schema.getFactoryTimeDef())).ensureRec((ICFBamTimeDef)rec) );
			}
			else if (classCode == ICFBamTimeType.CLASS_CODE) {
				return( ((CFBamBuffTimeTypeDefaultFactory)(schema.getFactoryTimeType())).ensureRec((ICFBamTimeType)rec) );
			}
			else if (classCode == ICFBamTimeCol.CLASS_CODE) {
				return( ((CFBamBuffTimeColDefaultFactory)(schema.getFactoryTimeCol())).ensureRec((ICFBamTimeCol)rec) );
			}
			else if (classCode == ICFBamTimestampDef.CLASS_CODE) {
				return( ((CFBamBuffTimestampDefDefaultFactory)(schema.getFactoryTimestampDef())).ensureRec((ICFBamTimestampDef)rec) );
			}
			else if (classCode == ICFBamTimestampType.CLASS_CODE) {
				return( ((CFBamBuffTimestampTypeDefaultFactory)(schema.getFactoryTimestampType())).ensureRec((ICFBamTimestampType)rec) );
			}
			else if (classCode == ICFBamTimestampCol.CLASS_CODE) {
				return( ((CFBamBuffTimestampColDefaultFactory)(schema.getFactoryTimestampCol())).ensureRec((ICFBamTimestampCol)rec) );
			}
			else if (classCode == ICFBamTokenDef.CLASS_CODE) {
				return( ((CFBamBuffTokenDefDefaultFactory)(schema.getFactoryTokenDef())).ensureRec((ICFBamTokenDef)rec) );
			}
			else if (classCode == ICFBamTokenType.CLASS_CODE) {
				return( ((CFBamBuffTokenTypeDefaultFactory)(schema.getFactoryTokenType())).ensureRec((ICFBamTokenType)rec) );
			}
			else if (classCode == ICFBamTokenCol.CLASS_CODE) {
				return( ((CFBamBuffTokenColDefaultFactory)(schema.getFactoryTokenCol())).ensureRec((ICFBamTokenCol)rec) );
			}
			else if (classCode == ICFBamUInt16Def.CLASS_CODE) {
				return( ((CFBamBuffUInt16DefDefaultFactory)(schema.getFactoryUInt16Def())).ensureRec((ICFBamUInt16Def)rec) );
			}
			else if (classCode == ICFBamUInt16Type.CLASS_CODE) {
				return( ((CFBamBuffUInt16TypeDefaultFactory)(schema.getFactoryUInt16Type())).ensureRec((ICFBamUInt16Type)rec) );
			}
			else if (classCode == ICFBamUInt16Col.CLASS_CODE) {
				return( ((CFBamBuffUInt16ColDefaultFactory)(schema.getFactoryUInt16Col())).ensureRec((ICFBamUInt16Col)rec) );
			}
			else if (classCode == ICFBamUInt32Def.CLASS_CODE) {
				return( ((CFBamBuffUInt32DefDefaultFactory)(schema.getFactoryUInt32Def())).ensureRec((ICFBamUInt32Def)rec) );
			}
			else if (classCode == ICFBamUInt32Type.CLASS_CODE) {
				return( ((CFBamBuffUInt32TypeDefaultFactory)(schema.getFactoryUInt32Type())).ensureRec((ICFBamUInt32Type)rec) );
			}
			else if (classCode == ICFBamUInt32Col.CLASS_CODE) {
				return( ((CFBamBuffUInt32ColDefaultFactory)(schema.getFactoryUInt32Col())).ensureRec((ICFBamUInt32Col)rec) );
			}
			else if (classCode == ICFBamUInt64Def.CLASS_CODE) {
				return( ((CFBamBuffUInt64DefDefaultFactory)(schema.getFactoryUInt64Def())).ensureRec((ICFBamUInt64Def)rec) );
			}
			else if (classCode == ICFBamUInt64Type.CLASS_CODE) {
				return( ((CFBamBuffUInt64TypeDefaultFactory)(schema.getFactoryUInt64Type())).ensureRec((ICFBamUInt64Type)rec) );
			}
			else if (classCode == ICFBamUInt64Col.CLASS_CODE) {
				return( ((CFBamBuffUInt64ColDefaultFactory)(schema.getFactoryUInt64Col())).ensureRec((ICFBamUInt64Col)rec) );
			}
			else if (classCode == ICFBamUuidDef.CLASS_CODE) {
				return( ((CFBamBuffUuidDefDefaultFactory)(schema.getFactoryUuidDef())).ensureRec((ICFBamUuidDef)rec) );
			}
			else if (classCode == ICFBamUuidType.CLASS_CODE) {
				return( ((CFBamBuffUuidTypeDefaultFactory)(schema.getFactoryUuidType())).ensureRec((ICFBamUuidType)rec) );
			}
			else if (classCode == ICFBamUuidGen.CLASS_CODE) {
				return( ((CFBamBuffUuidGenDefaultFactory)(schema.getFactoryUuidGen())).ensureRec((ICFBamUuidGen)rec) );
			}
			else if (classCode == ICFBamUuidCol.CLASS_CODE) {
				return( ((CFBamBuffUuidColDefaultFactory)(schema.getFactoryUuidCol())).ensureRec((ICFBamUuidCol)rec) );
			}
			else if (classCode == ICFBamUuid6Def.CLASS_CODE) {
				return( ((CFBamBuffUuid6DefDefaultFactory)(schema.getFactoryUuid6Def())).ensureRec((ICFBamUuid6Def)rec) );
			}
			else if (classCode == ICFBamUuid6Type.CLASS_CODE) {
				return( ((CFBamBuffUuid6TypeDefaultFactory)(schema.getFactoryUuid6Type())).ensureRec((ICFBamUuid6Type)rec) );
			}
			else if (classCode == ICFBamUuid6Gen.CLASS_CODE) {
				return( ((CFBamBuffUuid6GenDefaultFactory)(schema.getFactoryUuid6Gen())).ensureRec((ICFBamUuid6Gen)rec) );
			}
			else if (classCode == ICFBamUuid6Col.CLASS_CODE) {
				return( ((CFBamBuffUuid6ColDefaultFactory)(schema.getFactoryUuid6Col())).ensureRec((ICFBamUuid6Col)rec) );
			}
			else if (classCode == ICFBamTableCol.CLASS_CODE) {
				return( ((CFBamBuffTableColDefaultFactory)(schema.getFactoryTableCol())).ensureRec((ICFBamTableCol)rec) );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), "ensureRec", "rec", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamValue createValue( ICFSecAuthorization Authorization,
		ICFBamValue iBuff )
	{
		final String S_ProcName = "createValue";
		
		CFBamBuffValue Buff = (CFBamBuffValue)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextValueIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffValueByUNameIdxKey keyUNameIdx = (CFBamBuffValueByUNameIdxKey)schema.getFactoryValue().newByUNameIdxKey();
		keyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffValueByScopeIdxKey keyScopeIdx = (CFBamBuffValueByScopeIdxKey)schema.getFactoryValue().newByScopeIdxKey();
		keyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamBuffValueByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffValueByDefSchemaIdxKey)schema.getFactoryValue().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffValueByPrevIdxKey keyPrevIdx = (CFBamBuffValueByPrevIdxKey)schema.getFactoryValue().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffValueByNextIdxKey keyNextIdx = (CFBamBuffValueByNextIdxKey)schema.getFactoryValue().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffValueByContPrevIdxKey keyContPrevIdx = (CFBamBuffValueByContPrevIdxKey)schema.getFactoryValue().newByContPrevIdxKey();
		keyContPrevIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffValueByContNextIdxKey keyContNextIdx = (CFBamBuffValueByContNextIdxKey)schema.getFactoryValue().newByContNextIdxKey();
		keyContNextIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		keyContNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ValueUNameIdx",
				"ValueUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredScopeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Container",
						"Scope",
						"Scope",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffValue > subdictScopeIdx;
		if( dictByScopeIdx.containsKey( keyScopeIdx ) ) {
			subdictScopeIdx = dictByScopeIdx.get( keyScopeIdx );
		}
		else {
			subdictScopeIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByScopeIdx.put( keyScopeIdx, subdictScopeIdx );
		}
		subdictScopeIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffValue > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffValue > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffValue > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffValue > subdictContPrevIdx;
		if( dictByContPrevIdx.containsKey( keyContPrevIdx ) ) {
			subdictContPrevIdx = dictByContPrevIdx.get( keyContPrevIdx );
		}
		else {
			subdictContPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByContPrevIdx.put( keyContPrevIdx, subdictContPrevIdx );
		}
		subdictContPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffValue > subdictContNextIdx;
		if( dictByContNextIdx.containsKey( keyContNextIdx ) ) {
			subdictContNextIdx = dictByContNextIdx.get( keyContNextIdx );
		}
		else {
			subdictContNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByContNextIdx.put( keyContNextIdx, subdictContNextIdx );
		}
		subdictContNextIdx.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamValue.CLASS_CODE) {
				CFBamBuffValue retbuff = ((CFBamBuffValue)(schema.getFactoryValue().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamAtom.CLASS_CODE) {
				CFBamBuffAtom retbuff = ((CFBamBuffAtom)(schema.getFactoryAtom().newRec()));
				retbuff.set((ICFBamAtom)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBlobDef.CLASS_CODE) {
				CFBamBuffBlobDef retbuff = ((CFBamBuffBlobDef)(schema.getFactoryBlobDef().newRec()));
				retbuff.set((ICFBamBlobDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBlobType.CLASS_CODE) {
				CFBamBuffBlobType retbuff = ((CFBamBuffBlobType)(schema.getFactoryBlobType().newRec()));
				retbuff.set((ICFBamBlobType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBlobCol.CLASS_CODE) {
				CFBamBuffBlobCol retbuff = ((CFBamBuffBlobCol)(schema.getFactoryBlobCol().newRec()));
				retbuff.set((ICFBamBlobCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBoolDef.CLASS_CODE) {
				CFBamBuffBoolDef retbuff = ((CFBamBuffBoolDef)(schema.getFactoryBoolDef().newRec()));
				retbuff.set((ICFBamBoolDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBoolType.CLASS_CODE) {
				CFBamBuffBoolType retbuff = ((CFBamBuffBoolType)(schema.getFactoryBoolType().newRec()));
				retbuff.set((ICFBamBoolType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamBoolCol.CLASS_CODE) {
				CFBamBuffBoolCol retbuff = ((CFBamBuffBoolCol)(schema.getFactoryBoolCol().newRec()));
				retbuff.set((ICFBamBoolCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDateDef.CLASS_CODE) {
				CFBamBuffDateDef retbuff = ((CFBamBuffDateDef)(schema.getFactoryDateDef().newRec()));
				retbuff.set((ICFBamDateDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDateType.CLASS_CODE) {
				CFBamBuffDateType retbuff = ((CFBamBuffDateType)(schema.getFactoryDateType().newRec()));
				retbuff.set((ICFBamDateType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDateCol.CLASS_CODE) {
				CFBamBuffDateCol retbuff = ((CFBamBuffDateCol)(schema.getFactoryDateCol().newRec()));
				retbuff.set((ICFBamDateCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDoubleDef.CLASS_CODE) {
				CFBamBuffDoubleDef retbuff = ((CFBamBuffDoubleDef)(schema.getFactoryDoubleDef().newRec()));
				retbuff.set((ICFBamDoubleDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDoubleType.CLASS_CODE) {
				CFBamBuffDoubleType retbuff = ((CFBamBuffDoubleType)(schema.getFactoryDoubleType().newRec()));
				retbuff.set((ICFBamDoubleType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDoubleCol.CLASS_CODE) {
				CFBamBuffDoubleCol retbuff = ((CFBamBuffDoubleCol)(schema.getFactoryDoubleCol().newRec()));
				retbuff.set((ICFBamDoubleCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamFloatDef.CLASS_CODE) {
				CFBamBuffFloatDef retbuff = ((CFBamBuffFloatDef)(schema.getFactoryFloatDef().newRec()));
				retbuff.set((ICFBamFloatDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamFloatType.CLASS_CODE) {
				CFBamBuffFloatType retbuff = ((CFBamBuffFloatType)(schema.getFactoryFloatType().newRec()));
				retbuff.set((ICFBamFloatType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamFloatCol.CLASS_CODE) {
				CFBamBuffFloatCol retbuff = ((CFBamBuffFloatCol)(schema.getFactoryFloatCol().newRec()));
				retbuff.set((ICFBamFloatCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt16Def.CLASS_CODE) {
				CFBamBuffInt16Def retbuff = ((CFBamBuffInt16Def)(schema.getFactoryInt16Def().newRec()));
				retbuff.set((ICFBamInt16Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt16Type.CLASS_CODE) {
				CFBamBuffInt16Type retbuff = ((CFBamBuffInt16Type)(schema.getFactoryInt16Type().newRec()));
				retbuff.set((ICFBamInt16Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamId16Gen.CLASS_CODE) {
				CFBamBuffId16Gen retbuff = ((CFBamBuffId16Gen)(schema.getFactoryId16Gen().newRec()));
				retbuff.set((ICFBamId16Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamEnumDef.CLASS_CODE) {
				CFBamBuffEnumDef retbuff = ((CFBamBuffEnumDef)(schema.getFactoryEnumDef().newRec()));
				retbuff.set((ICFBamEnumDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamEnumType.CLASS_CODE) {
				CFBamBuffEnumType retbuff = ((CFBamBuffEnumType)(schema.getFactoryEnumType().newRec()));
				retbuff.set((ICFBamEnumType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt16Col.CLASS_CODE) {
				CFBamBuffInt16Col retbuff = ((CFBamBuffInt16Col)(schema.getFactoryInt16Col().newRec()));
				retbuff.set((ICFBamInt16Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt32Def.CLASS_CODE) {
				CFBamBuffInt32Def retbuff = ((CFBamBuffInt32Def)(schema.getFactoryInt32Def().newRec()));
				retbuff.set((ICFBamInt32Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt32Type.CLASS_CODE) {
				CFBamBuffInt32Type retbuff = ((CFBamBuffInt32Type)(schema.getFactoryInt32Type().newRec()));
				retbuff.set((ICFBamInt32Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamId32Gen.CLASS_CODE) {
				CFBamBuffId32Gen retbuff = ((CFBamBuffId32Gen)(schema.getFactoryId32Gen().newRec()));
				retbuff.set((ICFBamId32Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt32Col.CLASS_CODE) {
				CFBamBuffInt32Col retbuff = ((CFBamBuffInt32Col)(schema.getFactoryInt32Col().newRec()));
				retbuff.set((ICFBamInt32Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt64Def.CLASS_CODE) {
				CFBamBuffInt64Def retbuff = ((CFBamBuffInt64Def)(schema.getFactoryInt64Def().newRec()));
				retbuff.set((ICFBamInt64Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt64Type.CLASS_CODE) {
				CFBamBuffInt64Type retbuff = ((CFBamBuffInt64Type)(schema.getFactoryInt64Type().newRec()));
				retbuff.set((ICFBamInt64Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamId64Gen.CLASS_CODE) {
				CFBamBuffId64Gen retbuff = ((CFBamBuffId64Gen)(schema.getFactoryId64Gen().newRec()));
				retbuff.set((ICFBamId64Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamInt64Col.CLASS_CODE) {
				CFBamBuffInt64Col retbuff = ((CFBamBuffInt64Col)(schema.getFactoryInt64Col().newRec()));
				retbuff.set((ICFBamInt64Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokenDef.CLASS_CODE) {
				CFBamBuffNmTokenDef retbuff = ((CFBamBuffNmTokenDef)(schema.getFactoryNmTokenDef().newRec()));
				retbuff.set((ICFBamNmTokenDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokenType.CLASS_CODE) {
				CFBamBuffNmTokenType retbuff = ((CFBamBuffNmTokenType)(schema.getFactoryNmTokenType().newRec()));
				retbuff.set((ICFBamNmTokenType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokenCol.CLASS_CODE) {
				CFBamBuffNmTokenCol retbuff = ((CFBamBuffNmTokenCol)(schema.getFactoryNmTokenCol().newRec()));
				retbuff.set((ICFBamNmTokenCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokensDef.CLASS_CODE) {
				CFBamBuffNmTokensDef retbuff = ((CFBamBuffNmTokensDef)(schema.getFactoryNmTokensDef().newRec()));
				retbuff.set((ICFBamNmTokensDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokensType.CLASS_CODE) {
				CFBamBuffNmTokensType retbuff = ((CFBamBuffNmTokensType)(schema.getFactoryNmTokensType().newRec()));
				retbuff.set((ICFBamNmTokensType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNmTokensCol.CLASS_CODE) {
				CFBamBuffNmTokensCol retbuff = ((CFBamBuffNmTokensCol)(schema.getFactoryNmTokensCol().newRec()));
				retbuff.set((ICFBamNmTokensCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNumberDef.CLASS_CODE) {
				CFBamBuffNumberDef retbuff = ((CFBamBuffNumberDef)(schema.getFactoryNumberDef().newRec()));
				retbuff.set((ICFBamNumberDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNumberType.CLASS_CODE) {
				CFBamBuffNumberType retbuff = ((CFBamBuffNumberType)(schema.getFactoryNumberType().newRec()));
				retbuff.set((ICFBamNumberType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamNumberCol.CLASS_CODE) {
				CFBamBuffNumberCol retbuff = ((CFBamBuffNumberCol)(schema.getFactoryNumberCol().newRec()));
				retbuff.set((ICFBamNumberCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash128Def.CLASS_CODE) {
				CFBamBuffDbKeyHash128Def retbuff = ((CFBamBuffDbKeyHash128Def)(schema.getFactoryDbKeyHash128Def().newRec()));
				retbuff.set((ICFBamDbKeyHash128Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash128Col.CLASS_CODE) {
				CFBamBuffDbKeyHash128Col retbuff = ((CFBamBuffDbKeyHash128Col)(schema.getFactoryDbKeyHash128Col().newRec()));
				retbuff.set((ICFBamDbKeyHash128Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash128Type.CLASS_CODE) {
				CFBamBuffDbKeyHash128Type retbuff = ((CFBamBuffDbKeyHash128Type)(schema.getFactoryDbKeyHash128Type().newRec()));
				retbuff.set((ICFBamDbKeyHash128Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash128Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash128Gen retbuff = ((CFBamBuffDbKeyHash128Gen)(schema.getFactoryDbKeyHash128Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash128Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash160Def.CLASS_CODE) {
				CFBamBuffDbKeyHash160Def retbuff = ((CFBamBuffDbKeyHash160Def)(schema.getFactoryDbKeyHash160Def().newRec()));
				retbuff.set((ICFBamDbKeyHash160Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash160Col.CLASS_CODE) {
				CFBamBuffDbKeyHash160Col retbuff = ((CFBamBuffDbKeyHash160Col)(schema.getFactoryDbKeyHash160Col().newRec()));
				retbuff.set((ICFBamDbKeyHash160Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash160Type.CLASS_CODE) {
				CFBamBuffDbKeyHash160Type retbuff = ((CFBamBuffDbKeyHash160Type)(schema.getFactoryDbKeyHash160Type().newRec()));
				retbuff.set((ICFBamDbKeyHash160Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash160Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash160Gen retbuff = ((CFBamBuffDbKeyHash160Gen)(schema.getFactoryDbKeyHash160Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash160Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash224Def.CLASS_CODE) {
				CFBamBuffDbKeyHash224Def retbuff = ((CFBamBuffDbKeyHash224Def)(schema.getFactoryDbKeyHash224Def().newRec()));
				retbuff.set((ICFBamDbKeyHash224Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash224Col.CLASS_CODE) {
				CFBamBuffDbKeyHash224Col retbuff = ((CFBamBuffDbKeyHash224Col)(schema.getFactoryDbKeyHash224Col().newRec()));
				retbuff.set((ICFBamDbKeyHash224Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash224Type.CLASS_CODE) {
				CFBamBuffDbKeyHash224Type retbuff = ((CFBamBuffDbKeyHash224Type)(schema.getFactoryDbKeyHash224Type().newRec()));
				retbuff.set((ICFBamDbKeyHash224Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash224Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash224Gen retbuff = ((CFBamBuffDbKeyHash224Gen)(schema.getFactoryDbKeyHash224Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash224Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash256Def.CLASS_CODE) {
				CFBamBuffDbKeyHash256Def retbuff = ((CFBamBuffDbKeyHash256Def)(schema.getFactoryDbKeyHash256Def().newRec()));
				retbuff.set((ICFBamDbKeyHash256Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash256Col.CLASS_CODE) {
				CFBamBuffDbKeyHash256Col retbuff = ((CFBamBuffDbKeyHash256Col)(schema.getFactoryDbKeyHash256Col().newRec()));
				retbuff.set((ICFBamDbKeyHash256Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash256Type.CLASS_CODE) {
				CFBamBuffDbKeyHash256Type retbuff = ((CFBamBuffDbKeyHash256Type)(schema.getFactoryDbKeyHash256Type().newRec()));
				retbuff.set((ICFBamDbKeyHash256Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash256Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash256Gen retbuff = ((CFBamBuffDbKeyHash256Gen)(schema.getFactoryDbKeyHash256Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash256Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash384Def.CLASS_CODE) {
				CFBamBuffDbKeyHash384Def retbuff = ((CFBamBuffDbKeyHash384Def)(schema.getFactoryDbKeyHash384Def().newRec()));
				retbuff.set((ICFBamDbKeyHash384Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash384Col.CLASS_CODE) {
				CFBamBuffDbKeyHash384Col retbuff = ((CFBamBuffDbKeyHash384Col)(schema.getFactoryDbKeyHash384Col().newRec()));
				retbuff.set((ICFBamDbKeyHash384Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash384Type.CLASS_CODE) {
				CFBamBuffDbKeyHash384Type retbuff = ((CFBamBuffDbKeyHash384Type)(schema.getFactoryDbKeyHash384Type().newRec()));
				retbuff.set((ICFBamDbKeyHash384Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash384Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash384Gen retbuff = ((CFBamBuffDbKeyHash384Gen)(schema.getFactoryDbKeyHash384Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash384Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Def.CLASS_CODE) {
				CFBamBuffDbKeyHash512Def retbuff = ((CFBamBuffDbKeyHash512Def)(schema.getFactoryDbKeyHash512Def().newRec()));
				retbuff.set((ICFBamDbKeyHash512Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Col.CLASS_CODE) {
				CFBamBuffDbKeyHash512Col retbuff = ((CFBamBuffDbKeyHash512Col)(schema.getFactoryDbKeyHash512Col().newRec()));
				retbuff.set((ICFBamDbKeyHash512Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Type.CLASS_CODE) {
				CFBamBuffDbKeyHash512Type retbuff = ((CFBamBuffDbKeyHash512Type)(schema.getFactoryDbKeyHash512Type().newRec()));
				retbuff.set((ICFBamDbKeyHash512Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamDbKeyHash512Gen.CLASS_CODE) {
				CFBamBuffDbKeyHash512Gen retbuff = ((CFBamBuffDbKeyHash512Gen)(schema.getFactoryDbKeyHash512Gen().newRec()));
				retbuff.set((ICFBamDbKeyHash512Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamStringDef.CLASS_CODE) {
				CFBamBuffStringDef retbuff = ((CFBamBuffStringDef)(schema.getFactoryStringDef().newRec()));
				retbuff.set((ICFBamStringDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamStringType.CLASS_CODE) {
				CFBamBuffStringType retbuff = ((CFBamBuffStringType)(schema.getFactoryStringType().newRec()));
				retbuff.set((ICFBamStringType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamStringCol.CLASS_CODE) {
				CFBamBuffStringCol retbuff = ((CFBamBuffStringCol)(schema.getFactoryStringCol().newRec()));
				retbuff.set((ICFBamStringCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZDateDef.CLASS_CODE) {
				CFBamBuffTZDateDef retbuff = ((CFBamBuffTZDateDef)(schema.getFactoryTZDateDef().newRec()));
				retbuff.set((ICFBamTZDateDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZDateType.CLASS_CODE) {
				CFBamBuffTZDateType retbuff = ((CFBamBuffTZDateType)(schema.getFactoryTZDateType().newRec()));
				retbuff.set((ICFBamTZDateType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZDateCol.CLASS_CODE) {
				CFBamBuffTZDateCol retbuff = ((CFBamBuffTZDateCol)(schema.getFactoryTZDateCol().newRec()));
				retbuff.set((ICFBamTZDateCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimeDef.CLASS_CODE) {
				CFBamBuffTZTimeDef retbuff = ((CFBamBuffTZTimeDef)(schema.getFactoryTZTimeDef().newRec()));
				retbuff.set((ICFBamTZTimeDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimeType.CLASS_CODE) {
				CFBamBuffTZTimeType retbuff = ((CFBamBuffTZTimeType)(schema.getFactoryTZTimeType().newRec()));
				retbuff.set((ICFBamTZTimeType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimeCol.CLASS_CODE) {
				CFBamBuffTZTimeCol retbuff = ((CFBamBuffTZTimeCol)(schema.getFactoryTZTimeCol().newRec()));
				retbuff.set((ICFBamTZTimeCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimestampDef.CLASS_CODE) {
				CFBamBuffTZTimestampDef retbuff = ((CFBamBuffTZTimestampDef)(schema.getFactoryTZTimestampDef().newRec()));
				retbuff.set((ICFBamTZTimestampDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimestampType.CLASS_CODE) {
				CFBamBuffTZTimestampType retbuff = ((CFBamBuffTZTimestampType)(schema.getFactoryTZTimestampType().newRec()));
				retbuff.set((ICFBamTZTimestampType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTZTimestampCol.CLASS_CODE) {
				CFBamBuffTZTimestampCol retbuff = ((CFBamBuffTZTimestampCol)(schema.getFactoryTZTimestampCol().newRec()));
				retbuff.set((ICFBamTZTimestampCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTextDef.CLASS_CODE) {
				CFBamBuffTextDef retbuff = ((CFBamBuffTextDef)(schema.getFactoryTextDef().newRec()));
				retbuff.set((ICFBamTextDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTextType.CLASS_CODE) {
				CFBamBuffTextType retbuff = ((CFBamBuffTextType)(schema.getFactoryTextType().newRec()));
				retbuff.set((ICFBamTextType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTextCol.CLASS_CODE) {
				CFBamBuffTextCol retbuff = ((CFBamBuffTextCol)(schema.getFactoryTextCol().newRec()));
				retbuff.set((ICFBamTextCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimeDef.CLASS_CODE) {
				CFBamBuffTimeDef retbuff = ((CFBamBuffTimeDef)(schema.getFactoryTimeDef().newRec()));
				retbuff.set((ICFBamTimeDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimeType.CLASS_CODE) {
				CFBamBuffTimeType retbuff = ((CFBamBuffTimeType)(schema.getFactoryTimeType().newRec()));
				retbuff.set((ICFBamTimeType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimeCol.CLASS_CODE) {
				CFBamBuffTimeCol retbuff = ((CFBamBuffTimeCol)(schema.getFactoryTimeCol().newRec()));
				retbuff.set((ICFBamTimeCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimestampDef.CLASS_CODE) {
				CFBamBuffTimestampDef retbuff = ((CFBamBuffTimestampDef)(schema.getFactoryTimestampDef().newRec()));
				retbuff.set((ICFBamTimestampDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimestampType.CLASS_CODE) {
				CFBamBuffTimestampType retbuff = ((CFBamBuffTimestampType)(schema.getFactoryTimestampType().newRec()));
				retbuff.set((ICFBamTimestampType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTimestampCol.CLASS_CODE) {
				CFBamBuffTimestampCol retbuff = ((CFBamBuffTimestampCol)(schema.getFactoryTimestampCol().newRec()));
				retbuff.set((ICFBamTimestampCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTokenDef.CLASS_CODE) {
				CFBamBuffTokenDef retbuff = ((CFBamBuffTokenDef)(schema.getFactoryTokenDef().newRec()));
				retbuff.set((ICFBamTokenDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTokenType.CLASS_CODE) {
				CFBamBuffTokenType retbuff = ((CFBamBuffTokenType)(schema.getFactoryTokenType().newRec()));
				retbuff.set((ICFBamTokenType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTokenCol.CLASS_CODE) {
				CFBamBuffTokenCol retbuff = ((CFBamBuffTokenCol)(schema.getFactoryTokenCol().newRec()));
				retbuff.set((ICFBamTokenCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt16Def.CLASS_CODE) {
				CFBamBuffUInt16Def retbuff = ((CFBamBuffUInt16Def)(schema.getFactoryUInt16Def().newRec()));
				retbuff.set((ICFBamUInt16Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt16Type.CLASS_CODE) {
				CFBamBuffUInt16Type retbuff = ((CFBamBuffUInt16Type)(schema.getFactoryUInt16Type().newRec()));
				retbuff.set((ICFBamUInt16Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt16Col.CLASS_CODE) {
				CFBamBuffUInt16Col retbuff = ((CFBamBuffUInt16Col)(schema.getFactoryUInt16Col().newRec()));
				retbuff.set((ICFBamUInt16Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt32Def.CLASS_CODE) {
				CFBamBuffUInt32Def retbuff = ((CFBamBuffUInt32Def)(schema.getFactoryUInt32Def().newRec()));
				retbuff.set((ICFBamUInt32Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt32Type.CLASS_CODE) {
				CFBamBuffUInt32Type retbuff = ((CFBamBuffUInt32Type)(schema.getFactoryUInt32Type().newRec()));
				retbuff.set((ICFBamUInt32Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt32Col.CLASS_CODE) {
				CFBamBuffUInt32Col retbuff = ((CFBamBuffUInt32Col)(schema.getFactoryUInt32Col().newRec()));
				retbuff.set((ICFBamUInt32Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt64Def.CLASS_CODE) {
				CFBamBuffUInt64Def retbuff = ((CFBamBuffUInt64Def)(schema.getFactoryUInt64Def().newRec()));
				retbuff.set((ICFBamUInt64Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt64Type.CLASS_CODE) {
				CFBamBuffUInt64Type retbuff = ((CFBamBuffUInt64Type)(schema.getFactoryUInt64Type().newRec()));
				retbuff.set((ICFBamUInt64Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUInt64Col.CLASS_CODE) {
				CFBamBuffUInt64Col retbuff = ((CFBamBuffUInt64Col)(schema.getFactoryUInt64Col().newRec()));
				retbuff.set((ICFBamUInt64Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuidDef.CLASS_CODE) {
				CFBamBuffUuidDef retbuff = ((CFBamBuffUuidDef)(schema.getFactoryUuidDef().newRec()));
				retbuff.set((ICFBamUuidDef)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuidType.CLASS_CODE) {
				CFBamBuffUuidType retbuff = ((CFBamBuffUuidType)(schema.getFactoryUuidType().newRec()));
				retbuff.set((ICFBamUuidType)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuidGen.CLASS_CODE) {
				CFBamBuffUuidGen retbuff = ((CFBamBuffUuidGen)(schema.getFactoryUuidGen().newRec()));
				retbuff.set((ICFBamUuidGen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuidCol.CLASS_CODE) {
				CFBamBuffUuidCol retbuff = ((CFBamBuffUuidCol)(schema.getFactoryUuidCol().newRec()));
				retbuff.set((ICFBamUuidCol)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuid6Def.CLASS_CODE) {
				CFBamBuffUuid6Def retbuff = ((CFBamBuffUuid6Def)(schema.getFactoryUuid6Def().newRec()));
				retbuff.set((ICFBamUuid6Def)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuid6Type.CLASS_CODE) {
				CFBamBuffUuid6Type retbuff = ((CFBamBuffUuid6Type)(schema.getFactoryUuid6Type().newRec()));
				retbuff.set((ICFBamUuid6Type)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuid6Gen.CLASS_CODE) {
				CFBamBuffUuid6Gen retbuff = ((CFBamBuffUuid6Gen)(schema.getFactoryUuid6Gen().newRec()));
				retbuff.set((ICFBamUuid6Gen)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamUuid6Col.CLASS_CODE) {
				CFBamBuffUuid6Col retbuff = ((CFBamBuffUuid6Col)(schema.getFactoryUuid6Col().newRec()));
				retbuff.set((ICFBamUuid6Col)Buff);
				return( retbuff );
			}
			else if (classCode == ICFBamTableCol.CLASS_CODE) {
				CFBamBuffTableCol retbuff = ((CFBamBuffTableCol)(schema.getFactoryTableCol().newRec()));
				retbuff.set((ICFBamTableCol)Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	@Override
	public ICFBamValue readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamValue.readDerived";
		ICFBamValue buff;
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
	public ICFBamValue lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamValue.lockDerived";
		ICFBamValue buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamValue[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamValue.readAllDerived";
		ICFBamValue[] retList = new ICFBamValue[ dictByPKey.values().size() ];
		Iterator< CFBamBuffValue > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	@Override
	public ICFBamValue readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByUNameIdx";
		CFBamBuffValueByUNameIdxKey key = (CFBamBuffValueByUNameIdxKey)schema.getFactoryValue().newByUNameIdxKey();

		key.setRequiredScopeId( ScopeId );
		key.setRequiredName( Name );
		ICFBamValue buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamValue[] readDerivedByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByScopeIdx";
		CFBamBuffValueByScopeIdxKey key = (CFBamBuffValueByScopeIdxKey)schema.getFactoryValue().newByScopeIdxKey();

		key.setRequiredScopeId( ScopeId );
		ICFBamValue[] recArray;
		if( dictByScopeIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictScopeIdx
				= dictByScopeIdx.get( key );
			recArray = new ICFBamValue[ subdictScopeIdx.size() ];
			Iterator< CFBamBuffValue > iter = subdictScopeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictScopeIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByScopeIdx.put( key, subdictScopeIdx );
			recArray = new ICFBamValue[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamValue[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByDefSchemaIdx";
		CFBamBuffValueByDefSchemaIdxKey key = (CFBamBuffValueByDefSchemaIdxKey)schema.getFactoryValue().newByDefSchemaIdxKey();

		key.setOptionalDefSchemaId( DefSchemaId );
		ICFBamValue[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamValue[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamBuffValue > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamValue[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamValue[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByPrevIdx";
		CFBamBuffValueByPrevIdxKey key = (CFBamBuffValueByPrevIdxKey)schema.getFactoryValue().newByPrevIdxKey();

		key.setOptionalPrevId( PrevId );
		ICFBamValue[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamValue[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffValue > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamValue[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamValue[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByNextIdx";
		CFBamBuffValueByNextIdxKey key = (CFBamBuffValueByNextIdxKey)schema.getFactoryValue().newByNextIdxKey();

		key.setOptionalNextId( NextId );
		ICFBamValue[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamValue[ subdictNextIdx.size() ];
			Iterator< CFBamBuffValue > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamValue[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamValue[] readDerivedByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByContPrevIdx";
		CFBamBuffValueByContPrevIdxKey key = (CFBamBuffValueByContPrevIdxKey)schema.getFactoryValue().newByContPrevIdxKey();

		key.setRequiredScopeId( ScopeId );
		key.setOptionalPrevId( PrevId );
		ICFBamValue[] recArray;
		if( dictByContPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictContPrevIdx
				= dictByContPrevIdx.get( key );
			recArray = new ICFBamValue[ subdictContPrevIdx.size() ];
			Iterator< CFBamBuffValue > iter = subdictContPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictContPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByContPrevIdx.put( key, subdictContPrevIdx );
			recArray = new ICFBamValue[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamValue[] readDerivedByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByContNextIdx";
		CFBamBuffValueByContNextIdxKey key = (CFBamBuffValueByContNextIdxKey)schema.getFactoryValue().newByContNextIdxKey();

		key.setRequiredScopeId( ScopeId );
		key.setOptionalNextId( NextId );
		ICFBamValue[] recArray;
		if( dictByContNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictContNextIdx
				= dictByContNextIdx.get( key );
			recArray = new ICFBamValue[ subdictContNextIdx.size() ];
			Iterator< CFBamBuffValue > iter = subdictContNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffValue > subdictContNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByContNextIdx.put( key, subdictContNextIdx );
			recArray = new ICFBamValue[0];
		}
		return( recArray );
	}

	@Override
	public ICFBamValue readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readDerivedByIdIdx() ";
		ICFBamValue buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamValue readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamValue.readRec";
		ICFBamValue buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamValue.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamValue lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamValue buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamValue.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	@Override
	public ICFBamValue[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamValue.readAllRec";
		ICFBamValue buff;
		ArrayList<ICFBamValue> filteredList = new ArrayList<ICFBamValue>();
		ICFBamValue[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamValue[0] ) );
	}

	@Override
	public ICFBamValue readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamValue.readRecByIdIdx() ";
		ICFBamValue buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
			return( (ICFBamValue)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamValue readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		String Name )
	{
		final String S_ProcName = "CFBamRamValue.readRecByUNameIdx() ";
		ICFBamValue buff = readDerivedByUNameIdx( Authorization,
			ScopeId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
			return( (ICFBamValue)buff );
		}
		else {
			return( null );
		}
	}

	@Override
	public ICFBamValue[] readRecByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByScopeIdx() ";
		ICFBamValue buff;
		ArrayList<ICFBamValue> filteredList = new ArrayList<ICFBamValue>();
		ICFBamValue[] buffList = readDerivedByScopeIdx( Authorization,
			ScopeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamValue)buff );
			}
		}
		return( filteredList.toArray( new ICFBamValue[0] ) );
	}

	@Override
	public ICFBamValue[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByDefSchemaIdx() ";
		ICFBamValue buff;
		ArrayList<ICFBamValue> filteredList = new ArrayList<ICFBamValue>();
		ICFBamValue[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamValue)buff );
			}
		}
		return( filteredList.toArray( new ICFBamValue[0] ) );
	}

	@Override
	public ICFBamValue[] readRecByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByPrevIdx() ";
		ICFBamValue buff;
		ArrayList<ICFBamValue> filteredList = new ArrayList<ICFBamValue>();
		ICFBamValue[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamValue)buff );
			}
		}
		return( filteredList.toArray( new ICFBamValue[0] ) );
	}

	@Override
	public ICFBamValue[] readRecByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByNextIdx() ";
		ICFBamValue buff;
		ArrayList<ICFBamValue> filteredList = new ArrayList<ICFBamValue>();
		ICFBamValue[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamValue)buff );
			}
		}
		return( filteredList.toArray( new ICFBamValue[0] ) );
	}

	@Override
	public ICFBamValue[] readRecByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByContPrevIdx() ";
		ICFBamValue buff;
		ArrayList<ICFBamValue> filteredList = new ArrayList<ICFBamValue>();
		ICFBamValue[] buffList = readDerivedByContPrevIdx( Authorization,
			ScopeId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamValue)buff );
			}
		}
		return( filteredList.toArray( new ICFBamValue[0] ) );
	}

	@Override
	public ICFBamValue[] readRecByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ScopeId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamValue.readRecByContNextIdx() ";
		ICFBamValue buff;
		ArrayList<ICFBamValue> filteredList = new ArrayList<ICFBamValue>();
		ICFBamValue[] buffList = readDerivedByContNextIdx( Authorization,
			ScopeId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamValue.CLASS_CODE ) ) {
				filteredList.add( (ICFBamValue)buff );
			}
		}
		return( filteredList.toArray( new ICFBamValue[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamValue moveRecUp( ICFSecAuthorization Authorization,
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
			return( (CFBamBuffValue)cur );
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

		return( (CFBamBuffValue)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	@Override
	public ICFBamValue moveRecDown( ICFSecAuthorization Authorization,
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
			return( (CFBamBuffValue)cur );
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

		return( (CFBamBuffValue)editCur );
	}

	public ICFBamValue updateValue( ICFSecAuthorization Authorization,
		ICFBamValue iBuff )
	{
		CFBamBuffValue Buff = (CFBamBuffValue)ensureRec(iBuff);
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)Buff.getPKey();
		CFBamBuffValue existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateValue",
				"Existing record not found",
				"Existing record not found",
				"Value",
				"Value",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateValue",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffValueByUNameIdxKey existingKeyUNameIdx = (CFBamBuffValueByUNameIdxKey)schema.getFactoryValue().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffValueByUNameIdxKey newKeyUNameIdx = (CFBamBuffValueByUNameIdxKey)schema.getFactoryValue().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffValueByScopeIdxKey existingKeyScopeIdx = (CFBamBuffValueByScopeIdxKey)schema.getFactoryValue().newByScopeIdxKey();
		existingKeyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamBuffValueByScopeIdxKey newKeyScopeIdx = (CFBamBuffValueByScopeIdxKey)schema.getFactoryValue().newByScopeIdxKey();
		newKeyScopeIdx.setRequiredScopeId( Buff.getRequiredScopeId() );

		CFBamBuffValueByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffValueByDefSchemaIdxKey)schema.getFactoryValue().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffValueByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffValueByDefSchemaIdxKey)schema.getFactoryValue().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffValueByPrevIdxKey existingKeyPrevIdx = (CFBamBuffValueByPrevIdxKey)schema.getFactoryValue().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffValueByPrevIdxKey newKeyPrevIdx = (CFBamBuffValueByPrevIdxKey)schema.getFactoryValue().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffValueByNextIdxKey existingKeyNextIdx = (CFBamBuffValueByNextIdxKey)schema.getFactoryValue().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffValueByNextIdxKey newKeyNextIdx = (CFBamBuffValueByNextIdxKey)schema.getFactoryValue().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffValueByContPrevIdxKey existingKeyContPrevIdx = (CFBamBuffValueByContPrevIdxKey)schema.getFactoryValue().newByContPrevIdxKey();
		existingKeyContPrevIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffValueByContPrevIdxKey newKeyContPrevIdx = (CFBamBuffValueByContPrevIdxKey)schema.getFactoryValue().newByContPrevIdxKey();
		newKeyContPrevIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffValueByContNextIdxKey existingKeyContNextIdx = (CFBamBuffValueByContNextIdxKey)schema.getFactoryValue().newByContNextIdxKey();
		existingKeyContNextIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		existingKeyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffValueByContNextIdxKey newKeyContNextIdx = (CFBamBuffValueByContNextIdxKey)schema.getFactoryValue().newByContNextIdxKey();
		newKeyContNextIdx.setRequiredScopeId( Buff.getRequiredScopeId() );
		newKeyContNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateValue",
					"ValueUNameIdx",
					"ValueUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredScopeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateValue",
						"Container",
						"Container",
						"Scope",
						"Scope",
						"Scope",
						"Scope",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffValue > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByScopeIdx.get( existingKeyScopeIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByScopeIdx.containsKey( newKeyScopeIdx ) ) {
			subdict = dictByScopeIdx.get( newKeyScopeIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByScopeIdx.put( newKeyScopeIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffValue >();
			dictByContNextIdx.put( newKeyContNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	@Override
	public void deleteValue( ICFSecAuthorization Authorization,
		ICFBamValue iBuff )
	{
		final String S_ProcName = "CFBamRamValueTable.deleteValue() ";
		CFBamBuffValue Buff = (CFBamBuffValue)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffValue existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteValue",
				pkey );
		}
		CFLibDbKeyHash256 varScopeId = existing.getRequiredScopeId();
		CFBamBuffScope container = (CFBamBuffScope)(schema.getTableScope().readDerivedByIdIdx( Authorization,
			varScopeId ));
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
		CFBamBuffValueByUNameIdxKey keyUNameIdx = (CFBamBuffValueByUNameIdxKey)schema.getFactoryValue().newByUNameIdxKey();
		keyUNameIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffValueByScopeIdxKey keyScopeIdx = (CFBamBuffValueByScopeIdxKey)schema.getFactoryValue().newByScopeIdxKey();
		keyScopeIdx.setRequiredScopeId( existing.getRequiredScopeId() );

		CFBamBuffValueByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffValueByDefSchemaIdxKey)schema.getFactoryValue().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffValueByPrevIdxKey keyPrevIdx = (CFBamBuffValueByPrevIdxKey)schema.getFactoryValue().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffValueByNextIdxKey keyNextIdx = (CFBamBuffValueByNextIdxKey)schema.getFactoryValue().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffValueByContPrevIdxKey keyContPrevIdx = (CFBamBuffValueByContPrevIdxKey)schema.getFactoryValue().newByContPrevIdxKey();
		keyContPrevIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffValueByContNextIdxKey keyContNextIdx = (CFBamBuffValueByContNextIdxKey)schema.getFactoryValue().newByContNextIdxKey();
		keyContNextIdx.setRequiredScopeId( existing.getRequiredScopeId() );
		keyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		if( schema.getTableAtom().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteValue",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"Atom",
				"Atom",
				pkey );
		}

		if( schema.getTableTableCol().readDerivedByIdIdx( Authorization,
					existing.getRequiredId() ) != null )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteValue",
				"Superclass",
				"Superclass",
				"SuperClass",
				"SuperClass",
				"TableCol",
				"TableCol",
				pkey );
		}

		if( schema.getTableIndexCol().readDerivedByColIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteValue",
				"Lookup",
				"Lookup",
				"Column",
				"Column",
				"IndexCol",
				"IndexCol",
				pkey );
		}

		if( schema.getTableParam().readDerivedByServerTypeIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteValue",
				"Lookup",
				"Lookup",
				"Type",
				"Type",
				"Param",
				"Param",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffValue > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByScopeIdx.get( keyScopeIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
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
	public void deleteValueByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		final String S_ProcName = "deleteValueByIdIdx";
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffValue cur;
		LinkedList<CFBamBuffValue> matchSet = new LinkedList<CFBamBuffValue>();
		Iterator<CFBamBuffValue> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffValue> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamValue.CLASS_CODE == subClassCode ) {
				schema.getTableValue().deleteValue( Authorization, cur );
			}
			else if( ICFBamAtom.CLASS_CODE == subClassCode ) {
				schema.getTableAtom().deleteAtom( Authorization, (ICFBamAtom)cur );
			}
			else if( ICFBamBlobDef.CLASS_CODE == subClassCode ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( ICFBamBlobType.CLASS_CODE == subClassCode ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( ICFBamBlobCol.CLASS_CODE == subClassCode ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( ICFBamBoolDef.CLASS_CODE == subClassCode ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( ICFBamBoolType.CLASS_CODE == subClassCode ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( ICFBamBoolCol.CLASS_CODE == subClassCode ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( ICFBamDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( ICFBamDateType.CLASS_CODE == subClassCode ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( ICFBamDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( ICFBamDoubleDef.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( ICFBamDoubleType.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( ICFBamDoubleCol.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( ICFBamFloatDef.CLASS_CODE == subClassCode ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( ICFBamFloatType.CLASS_CODE == subClassCode ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( ICFBamFloatCol.CLASS_CODE == subClassCode ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( ICFBamInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( ICFBamInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( ICFBamId16Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( ICFBamEnumDef.CLASS_CODE == subClassCode ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( ICFBamEnumType.CLASS_CODE == subClassCode ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( ICFBamInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( ICFBamInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( ICFBamInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( ICFBamId32Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( ICFBamInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( ICFBamInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( ICFBamInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( ICFBamId64Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( ICFBamInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( ICFBamNmTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( ICFBamNmTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( ICFBamNmTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( ICFBamNmTokensDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( ICFBamNmTokensType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( ICFBamNmTokensCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( ICFBamNumberDef.CLASS_CODE == subClassCode ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( ICFBamNumberType.CLASS_CODE == subClassCode ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( ICFBamNumberCol.CLASS_CODE == subClassCode ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( ICFBamDbKeyHash128Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( ICFBamDbKeyHash128Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( ICFBamDbKeyHash128Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( ICFBamDbKeyHash128Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( ICFBamDbKeyHash160Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( ICFBamDbKeyHash160Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( ICFBamDbKeyHash160Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( ICFBamDbKeyHash160Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( ICFBamDbKeyHash224Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( ICFBamDbKeyHash224Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( ICFBamDbKeyHash224Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( ICFBamDbKeyHash224Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( ICFBamDbKeyHash256Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( ICFBamDbKeyHash256Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( ICFBamDbKeyHash256Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( ICFBamDbKeyHash256Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( ICFBamDbKeyHash384Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( ICFBamDbKeyHash384Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( ICFBamDbKeyHash384Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( ICFBamDbKeyHash384Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( ICFBamDbKeyHash512Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( ICFBamDbKeyHash512Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( ICFBamStringDef.CLASS_CODE == subClassCode ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( ICFBamStringType.CLASS_CODE == subClassCode ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( ICFBamStringCol.CLASS_CODE == subClassCode ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( ICFBamTZDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( ICFBamTZDateType.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( ICFBamTZDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( ICFBamTZTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( ICFBamTZTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( ICFBamTZTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( ICFBamTZTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( ICFBamTZTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( ICFBamTZTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( ICFBamTextDef.CLASS_CODE == subClassCode ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( ICFBamTextType.CLASS_CODE == subClassCode ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( ICFBamTextCol.CLASS_CODE == subClassCode ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( ICFBamTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( ICFBamTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( ICFBamTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( ICFBamTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( ICFBamTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( ICFBamTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( ICFBamTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( ICFBamTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( ICFBamTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( ICFBamUInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( ICFBamUInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( ICFBamUInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( ICFBamUInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( ICFBamUInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( ICFBamUInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( ICFBamUInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( ICFBamUInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( ICFBamUInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( ICFBamUuidDef.CLASS_CODE == subClassCode ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( ICFBamUuidType.CLASS_CODE == subClassCode ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( ICFBamUuidGen.CLASS_CODE == subClassCode ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( ICFBamUuidCol.CLASS_CODE == subClassCode ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( ICFBamUuid6Def.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( ICFBamUuid6Type.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( ICFBamUuid6Gen.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( ICFBamUuid6Col.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else if( ICFBamTableCol.CLASS_CODE == subClassCode ) {
				schema.getTableTableCol().deleteTableCol( Authorization, (ICFBamTableCol)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteValueByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		String argName )
	{
		CFBamBuffValueByUNameIdxKey key = (CFBamBuffValueByUNameIdxKey)schema.getFactoryValue().newByUNameIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setRequiredName( argName );
		deleteValueByUNameIdx( Authorization, key );
	}

	@Override
	public void deleteValueByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamValueByUNameIdxKey argKey )
	{
		final String S_ProcName = "deleteValueByUNameIdx";
		CFBamBuffValue cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffValue> matchSet = new LinkedList<CFBamBuffValue>();
		Iterator<CFBamBuffValue> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffValue> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamValue.CLASS_CODE == subClassCode ) {
				schema.getTableValue().deleteValue( Authorization, cur );
			}
			else if( ICFBamAtom.CLASS_CODE == subClassCode ) {
				schema.getTableAtom().deleteAtom( Authorization, (ICFBamAtom)cur );
			}
			else if( ICFBamBlobDef.CLASS_CODE == subClassCode ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( ICFBamBlobType.CLASS_CODE == subClassCode ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( ICFBamBlobCol.CLASS_CODE == subClassCode ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( ICFBamBoolDef.CLASS_CODE == subClassCode ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( ICFBamBoolType.CLASS_CODE == subClassCode ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( ICFBamBoolCol.CLASS_CODE == subClassCode ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( ICFBamDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( ICFBamDateType.CLASS_CODE == subClassCode ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( ICFBamDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( ICFBamDoubleDef.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( ICFBamDoubleType.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( ICFBamDoubleCol.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( ICFBamFloatDef.CLASS_CODE == subClassCode ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( ICFBamFloatType.CLASS_CODE == subClassCode ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( ICFBamFloatCol.CLASS_CODE == subClassCode ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( ICFBamInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( ICFBamInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( ICFBamId16Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( ICFBamEnumDef.CLASS_CODE == subClassCode ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( ICFBamEnumType.CLASS_CODE == subClassCode ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( ICFBamInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( ICFBamInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( ICFBamInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( ICFBamId32Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( ICFBamInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( ICFBamInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( ICFBamInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( ICFBamId64Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( ICFBamInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( ICFBamNmTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( ICFBamNmTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( ICFBamNmTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( ICFBamNmTokensDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( ICFBamNmTokensType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( ICFBamNmTokensCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( ICFBamNumberDef.CLASS_CODE == subClassCode ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( ICFBamNumberType.CLASS_CODE == subClassCode ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( ICFBamNumberCol.CLASS_CODE == subClassCode ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( ICFBamDbKeyHash128Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( ICFBamDbKeyHash128Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( ICFBamDbKeyHash128Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( ICFBamDbKeyHash128Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( ICFBamDbKeyHash160Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( ICFBamDbKeyHash160Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( ICFBamDbKeyHash160Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( ICFBamDbKeyHash160Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( ICFBamDbKeyHash224Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( ICFBamDbKeyHash224Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( ICFBamDbKeyHash224Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( ICFBamDbKeyHash224Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( ICFBamDbKeyHash256Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( ICFBamDbKeyHash256Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( ICFBamDbKeyHash256Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( ICFBamDbKeyHash256Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( ICFBamDbKeyHash384Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( ICFBamDbKeyHash384Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( ICFBamDbKeyHash384Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( ICFBamDbKeyHash384Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( ICFBamDbKeyHash512Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( ICFBamDbKeyHash512Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( ICFBamStringDef.CLASS_CODE == subClassCode ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( ICFBamStringType.CLASS_CODE == subClassCode ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( ICFBamStringCol.CLASS_CODE == subClassCode ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( ICFBamTZDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( ICFBamTZDateType.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( ICFBamTZDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( ICFBamTZTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( ICFBamTZTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( ICFBamTZTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( ICFBamTZTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( ICFBamTZTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( ICFBamTZTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( ICFBamTextDef.CLASS_CODE == subClassCode ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( ICFBamTextType.CLASS_CODE == subClassCode ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( ICFBamTextCol.CLASS_CODE == subClassCode ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( ICFBamTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( ICFBamTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( ICFBamTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( ICFBamTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( ICFBamTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( ICFBamTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( ICFBamTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( ICFBamTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( ICFBamTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( ICFBamUInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( ICFBamUInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( ICFBamUInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( ICFBamUInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( ICFBamUInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( ICFBamUInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( ICFBamUInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( ICFBamUInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( ICFBamUInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( ICFBamUuidDef.CLASS_CODE == subClassCode ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( ICFBamUuidType.CLASS_CODE == subClassCode ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( ICFBamUuidGen.CLASS_CODE == subClassCode ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( ICFBamUuidCol.CLASS_CODE == subClassCode ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( ICFBamUuid6Def.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( ICFBamUuid6Type.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( ICFBamUuid6Gen.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( ICFBamUuid6Col.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else if( ICFBamTableCol.CLASS_CODE == subClassCode ) {
				schema.getTableTableCol().deleteTableCol( Authorization, (ICFBamTableCol)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteValueByScopeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId )
	{
		CFBamBuffValueByScopeIdxKey key = (CFBamBuffValueByScopeIdxKey)schema.getFactoryValue().newByScopeIdxKey();
		key.setRequiredScopeId( argScopeId );
		deleteValueByScopeIdx( Authorization, key );
	}

	@Override
	public void deleteValueByScopeIdx( ICFSecAuthorization Authorization,
		ICFBamValueByScopeIdxKey argKey )
	{
		final String S_ProcName = "deleteValueByScopeIdx";
		CFBamBuffValue cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffValue> matchSet = new LinkedList<CFBamBuffValue>();
		Iterator<CFBamBuffValue> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffValue> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamValue.CLASS_CODE == subClassCode ) {
				schema.getTableValue().deleteValue( Authorization, cur );
			}
			else if( ICFBamAtom.CLASS_CODE == subClassCode ) {
				schema.getTableAtom().deleteAtom( Authorization, (ICFBamAtom)cur );
			}
			else if( ICFBamBlobDef.CLASS_CODE == subClassCode ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( ICFBamBlobType.CLASS_CODE == subClassCode ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( ICFBamBlobCol.CLASS_CODE == subClassCode ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( ICFBamBoolDef.CLASS_CODE == subClassCode ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( ICFBamBoolType.CLASS_CODE == subClassCode ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( ICFBamBoolCol.CLASS_CODE == subClassCode ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( ICFBamDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( ICFBamDateType.CLASS_CODE == subClassCode ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( ICFBamDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( ICFBamDoubleDef.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( ICFBamDoubleType.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( ICFBamDoubleCol.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( ICFBamFloatDef.CLASS_CODE == subClassCode ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( ICFBamFloatType.CLASS_CODE == subClassCode ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( ICFBamFloatCol.CLASS_CODE == subClassCode ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( ICFBamInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( ICFBamInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( ICFBamId16Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( ICFBamEnumDef.CLASS_CODE == subClassCode ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( ICFBamEnumType.CLASS_CODE == subClassCode ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( ICFBamInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( ICFBamInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( ICFBamInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( ICFBamId32Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( ICFBamInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( ICFBamInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( ICFBamInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( ICFBamId64Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( ICFBamInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( ICFBamNmTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( ICFBamNmTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( ICFBamNmTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( ICFBamNmTokensDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( ICFBamNmTokensType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( ICFBamNmTokensCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( ICFBamNumberDef.CLASS_CODE == subClassCode ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( ICFBamNumberType.CLASS_CODE == subClassCode ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( ICFBamNumberCol.CLASS_CODE == subClassCode ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( ICFBamDbKeyHash128Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( ICFBamDbKeyHash128Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( ICFBamDbKeyHash128Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( ICFBamDbKeyHash128Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( ICFBamDbKeyHash160Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( ICFBamDbKeyHash160Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( ICFBamDbKeyHash160Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( ICFBamDbKeyHash160Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( ICFBamDbKeyHash224Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( ICFBamDbKeyHash224Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( ICFBamDbKeyHash224Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( ICFBamDbKeyHash224Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( ICFBamDbKeyHash256Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( ICFBamDbKeyHash256Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( ICFBamDbKeyHash256Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( ICFBamDbKeyHash256Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( ICFBamDbKeyHash384Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( ICFBamDbKeyHash384Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( ICFBamDbKeyHash384Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( ICFBamDbKeyHash384Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( ICFBamDbKeyHash512Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( ICFBamDbKeyHash512Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( ICFBamStringDef.CLASS_CODE == subClassCode ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( ICFBamStringType.CLASS_CODE == subClassCode ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( ICFBamStringCol.CLASS_CODE == subClassCode ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( ICFBamTZDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( ICFBamTZDateType.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( ICFBamTZDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( ICFBamTZTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( ICFBamTZTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( ICFBamTZTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( ICFBamTZTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( ICFBamTZTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( ICFBamTZTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( ICFBamTextDef.CLASS_CODE == subClassCode ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( ICFBamTextType.CLASS_CODE == subClassCode ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( ICFBamTextCol.CLASS_CODE == subClassCode ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( ICFBamTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( ICFBamTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( ICFBamTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( ICFBamTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( ICFBamTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( ICFBamTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( ICFBamTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( ICFBamTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( ICFBamTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( ICFBamUInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( ICFBamUInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( ICFBamUInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( ICFBamUInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( ICFBamUInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( ICFBamUInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( ICFBamUInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( ICFBamUInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( ICFBamUInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( ICFBamUuidDef.CLASS_CODE == subClassCode ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( ICFBamUuidType.CLASS_CODE == subClassCode ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( ICFBamUuidGen.CLASS_CODE == subClassCode ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( ICFBamUuidCol.CLASS_CODE == subClassCode ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( ICFBamUuid6Def.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( ICFBamUuid6Type.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( ICFBamUuid6Gen.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( ICFBamUuid6Col.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else if( ICFBamTableCol.CLASS_CODE == subClassCode ) {
				schema.getTableTableCol().deleteTableCol( Authorization, (ICFBamTableCol)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteValueByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffValueByDefSchemaIdxKey key = (CFBamBuffValueByDefSchemaIdxKey)schema.getFactoryValue().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteValueByDefSchemaIdx( Authorization, key );
	}

	@Override
	public void deleteValueByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamValueByDefSchemaIdxKey argKey )
	{
		final String S_ProcName = "deleteValueByDefSchemaIdx";
		CFBamBuffValue cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffValue> matchSet = new LinkedList<CFBamBuffValue>();
		Iterator<CFBamBuffValue> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffValue> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamValue.CLASS_CODE == subClassCode ) {
				schema.getTableValue().deleteValue( Authorization, cur );
			}
			else if( ICFBamAtom.CLASS_CODE == subClassCode ) {
				schema.getTableAtom().deleteAtom( Authorization, (ICFBamAtom)cur );
			}
			else if( ICFBamBlobDef.CLASS_CODE == subClassCode ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( ICFBamBlobType.CLASS_CODE == subClassCode ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( ICFBamBlobCol.CLASS_CODE == subClassCode ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( ICFBamBoolDef.CLASS_CODE == subClassCode ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( ICFBamBoolType.CLASS_CODE == subClassCode ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( ICFBamBoolCol.CLASS_CODE == subClassCode ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( ICFBamDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( ICFBamDateType.CLASS_CODE == subClassCode ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( ICFBamDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( ICFBamDoubleDef.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( ICFBamDoubleType.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( ICFBamDoubleCol.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( ICFBamFloatDef.CLASS_CODE == subClassCode ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( ICFBamFloatType.CLASS_CODE == subClassCode ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( ICFBamFloatCol.CLASS_CODE == subClassCode ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( ICFBamInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( ICFBamInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( ICFBamId16Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( ICFBamEnumDef.CLASS_CODE == subClassCode ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( ICFBamEnumType.CLASS_CODE == subClassCode ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( ICFBamInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( ICFBamInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( ICFBamInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( ICFBamId32Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( ICFBamInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( ICFBamInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( ICFBamInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( ICFBamId64Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( ICFBamInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( ICFBamNmTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( ICFBamNmTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( ICFBamNmTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( ICFBamNmTokensDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( ICFBamNmTokensType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( ICFBamNmTokensCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( ICFBamNumberDef.CLASS_CODE == subClassCode ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( ICFBamNumberType.CLASS_CODE == subClassCode ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( ICFBamNumberCol.CLASS_CODE == subClassCode ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( ICFBamDbKeyHash128Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( ICFBamDbKeyHash128Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( ICFBamDbKeyHash128Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( ICFBamDbKeyHash128Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( ICFBamDbKeyHash160Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( ICFBamDbKeyHash160Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( ICFBamDbKeyHash160Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( ICFBamDbKeyHash160Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( ICFBamDbKeyHash224Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( ICFBamDbKeyHash224Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( ICFBamDbKeyHash224Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( ICFBamDbKeyHash224Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( ICFBamDbKeyHash256Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( ICFBamDbKeyHash256Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( ICFBamDbKeyHash256Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( ICFBamDbKeyHash256Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( ICFBamDbKeyHash384Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( ICFBamDbKeyHash384Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( ICFBamDbKeyHash384Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( ICFBamDbKeyHash384Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( ICFBamDbKeyHash512Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( ICFBamDbKeyHash512Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( ICFBamStringDef.CLASS_CODE == subClassCode ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( ICFBamStringType.CLASS_CODE == subClassCode ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( ICFBamStringCol.CLASS_CODE == subClassCode ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( ICFBamTZDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( ICFBamTZDateType.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( ICFBamTZDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( ICFBamTZTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( ICFBamTZTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( ICFBamTZTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( ICFBamTZTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( ICFBamTZTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( ICFBamTZTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( ICFBamTextDef.CLASS_CODE == subClassCode ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( ICFBamTextType.CLASS_CODE == subClassCode ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( ICFBamTextCol.CLASS_CODE == subClassCode ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( ICFBamTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( ICFBamTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( ICFBamTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( ICFBamTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( ICFBamTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( ICFBamTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( ICFBamTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( ICFBamTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( ICFBamTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( ICFBamUInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( ICFBamUInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( ICFBamUInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( ICFBamUInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( ICFBamUInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( ICFBamUInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( ICFBamUInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( ICFBamUInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( ICFBamUInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( ICFBamUuidDef.CLASS_CODE == subClassCode ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( ICFBamUuidType.CLASS_CODE == subClassCode ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( ICFBamUuidGen.CLASS_CODE == subClassCode ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( ICFBamUuidCol.CLASS_CODE == subClassCode ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( ICFBamUuid6Def.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( ICFBamUuid6Type.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( ICFBamUuid6Gen.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( ICFBamUuid6Col.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else if( ICFBamTableCol.CLASS_CODE == subClassCode ) {
				schema.getTableTableCol().deleteTableCol( Authorization, (ICFBamTableCol)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteValueByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByPrevIdxKey key = (CFBamBuffValueByPrevIdxKey)schema.getFactoryValue().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteValueByPrevIdx( Authorization, key );
	}

	@Override
	public void deleteValueByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByPrevIdxKey argKey )
	{
		final String S_ProcName = "deleteValueByPrevIdx";
		CFBamBuffValue cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffValue> matchSet = new LinkedList<CFBamBuffValue>();
		Iterator<CFBamBuffValue> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffValue> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamValue.CLASS_CODE == subClassCode ) {
				schema.getTableValue().deleteValue( Authorization, cur );
			}
			else if( ICFBamAtom.CLASS_CODE == subClassCode ) {
				schema.getTableAtom().deleteAtom( Authorization, (ICFBamAtom)cur );
			}
			else if( ICFBamBlobDef.CLASS_CODE == subClassCode ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( ICFBamBlobType.CLASS_CODE == subClassCode ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( ICFBamBlobCol.CLASS_CODE == subClassCode ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( ICFBamBoolDef.CLASS_CODE == subClassCode ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( ICFBamBoolType.CLASS_CODE == subClassCode ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( ICFBamBoolCol.CLASS_CODE == subClassCode ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( ICFBamDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( ICFBamDateType.CLASS_CODE == subClassCode ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( ICFBamDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( ICFBamDoubleDef.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( ICFBamDoubleType.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( ICFBamDoubleCol.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( ICFBamFloatDef.CLASS_CODE == subClassCode ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( ICFBamFloatType.CLASS_CODE == subClassCode ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( ICFBamFloatCol.CLASS_CODE == subClassCode ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( ICFBamInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( ICFBamInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( ICFBamId16Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( ICFBamEnumDef.CLASS_CODE == subClassCode ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( ICFBamEnumType.CLASS_CODE == subClassCode ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( ICFBamInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( ICFBamInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( ICFBamInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( ICFBamId32Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( ICFBamInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( ICFBamInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( ICFBamInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( ICFBamId64Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( ICFBamInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( ICFBamNmTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( ICFBamNmTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( ICFBamNmTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( ICFBamNmTokensDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( ICFBamNmTokensType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( ICFBamNmTokensCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( ICFBamNumberDef.CLASS_CODE == subClassCode ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( ICFBamNumberType.CLASS_CODE == subClassCode ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( ICFBamNumberCol.CLASS_CODE == subClassCode ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( ICFBamDbKeyHash128Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( ICFBamDbKeyHash128Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( ICFBamDbKeyHash128Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( ICFBamDbKeyHash128Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( ICFBamDbKeyHash160Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( ICFBamDbKeyHash160Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( ICFBamDbKeyHash160Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( ICFBamDbKeyHash160Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( ICFBamDbKeyHash224Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( ICFBamDbKeyHash224Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( ICFBamDbKeyHash224Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( ICFBamDbKeyHash224Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( ICFBamDbKeyHash256Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( ICFBamDbKeyHash256Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( ICFBamDbKeyHash256Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( ICFBamDbKeyHash256Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( ICFBamDbKeyHash384Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( ICFBamDbKeyHash384Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( ICFBamDbKeyHash384Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( ICFBamDbKeyHash384Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( ICFBamDbKeyHash512Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( ICFBamDbKeyHash512Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( ICFBamStringDef.CLASS_CODE == subClassCode ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( ICFBamStringType.CLASS_CODE == subClassCode ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( ICFBamStringCol.CLASS_CODE == subClassCode ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( ICFBamTZDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( ICFBamTZDateType.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( ICFBamTZDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( ICFBamTZTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( ICFBamTZTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( ICFBamTZTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( ICFBamTZTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( ICFBamTZTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( ICFBamTZTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( ICFBamTextDef.CLASS_CODE == subClassCode ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( ICFBamTextType.CLASS_CODE == subClassCode ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( ICFBamTextCol.CLASS_CODE == subClassCode ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( ICFBamTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( ICFBamTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( ICFBamTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( ICFBamTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( ICFBamTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( ICFBamTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( ICFBamTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( ICFBamTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( ICFBamTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( ICFBamUInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( ICFBamUInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( ICFBamUInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( ICFBamUInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( ICFBamUInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( ICFBamUInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( ICFBamUInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( ICFBamUInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( ICFBamUInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( ICFBamUuidDef.CLASS_CODE == subClassCode ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( ICFBamUuidType.CLASS_CODE == subClassCode ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( ICFBamUuidGen.CLASS_CODE == subClassCode ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( ICFBamUuidCol.CLASS_CODE == subClassCode ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( ICFBamUuid6Def.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( ICFBamUuid6Type.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( ICFBamUuid6Gen.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( ICFBamUuid6Col.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else if( ICFBamTableCol.CLASS_CODE == subClassCode ) {
				schema.getTableTableCol().deleteTableCol( Authorization, (ICFBamTableCol)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteValueByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByNextIdxKey key = (CFBamBuffValueByNextIdxKey)schema.getFactoryValue().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteValueByNextIdx( Authorization, key );
	}

	@Override
	public void deleteValueByNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByNextIdxKey argKey )
	{
		final String S_ProcName = "deleteValueByNextIdx";
		CFBamBuffValue cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffValue> matchSet = new LinkedList<CFBamBuffValue>();
		Iterator<CFBamBuffValue> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffValue> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamValue.CLASS_CODE == subClassCode ) {
				schema.getTableValue().deleteValue( Authorization, cur );
			}
			else if( ICFBamAtom.CLASS_CODE == subClassCode ) {
				schema.getTableAtom().deleteAtom( Authorization, (ICFBamAtom)cur );
			}
			else if( ICFBamBlobDef.CLASS_CODE == subClassCode ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( ICFBamBlobType.CLASS_CODE == subClassCode ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( ICFBamBlobCol.CLASS_CODE == subClassCode ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( ICFBamBoolDef.CLASS_CODE == subClassCode ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( ICFBamBoolType.CLASS_CODE == subClassCode ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( ICFBamBoolCol.CLASS_CODE == subClassCode ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( ICFBamDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( ICFBamDateType.CLASS_CODE == subClassCode ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( ICFBamDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( ICFBamDoubleDef.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( ICFBamDoubleType.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( ICFBamDoubleCol.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( ICFBamFloatDef.CLASS_CODE == subClassCode ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( ICFBamFloatType.CLASS_CODE == subClassCode ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( ICFBamFloatCol.CLASS_CODE == subClassCode ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( ICFBamInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( ICFBamInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( ICFBamId16Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( ICFBamEnumDef.CLASS_CODE == subClassCode ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( ICFBamEnumType.CLASS_CODE == subClassCode ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( ICFBamInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( ICFBamInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( ICFBamInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( ICFBamId32Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( ICFBamInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( ICFBamInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( ICFBamInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( ICFBamId64Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( ICFBamInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( ICFBamNmTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( ICFBamNmTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( ICFBamNmTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( ICFBamNmTokensDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( ICFBamNmTokensType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( ICFBamNmTokensCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( ICFBamNumberDef.CLASS_CODE == subClassCode ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( ICFBamNumberType.CLASS_CODE == subClassCode ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( ICFBamNumberCol.CLASS_CODE == subClassCode ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( ICFBamDbKeyHash128Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( ICFBamDbKeyHash128Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( ICFBamDbKeyHash128Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( ICFBamDbKeyHash128Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( ICFBamDbKeyHash160Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( ICFBamDbKeyHash160Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( ICFBamDbKeyHash160Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( ICFBamDbKeyHash160Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( ICFBamDbKeyHash224Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( ICFBamDbKeyHash224Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( ICFBamDbKeyHash224Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( ICFBamDbKeyHash224Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( ICFBamDbKeyHash256Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( ICFBamDbKeyHash256Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( ICFBamDbKeyHash256Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( ICFBamDbKeyHash256Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( ICFBamDbKeyHash384Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( ICFBamDbKeyHash384Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( ICFBamDbKeyHash384Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( ICFBamDbKeyHash384Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( ICFBamDbKeyHash512Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( ICFBamDbKeyHash512Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( ICFBamStringDef.CLASS_CODE == subClassCode ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( ICFBamStringType.CLASS_CODE == subClassCode ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( ICFBamStringCol.CLASS_CODE == subClassCode ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( ICFBamTZDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( ICFBamTZDateType.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( ICFBamTZDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( ICFBamTZTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( ICFBamTZTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( ICFBamTZTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( ICFBamTZTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( ICFBamTZTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( ICFBamTZTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( ICFBamTextDef.CLASS_CODE == subClassCode ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( ICFBamTextType.CLASS_CODE == subClassCode ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( ICFBamTextCol.CLASS_CODE == subClassCode ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( ICFBamTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( ICFBamTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( ICFBamTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( ICFBamTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( ICFBamTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( ICFBamTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( ICFBamTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( ICFBamTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( ICFBamTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( ICFBamUInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( ICFBamUInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( ICFBamUInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( ICFBamUInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( ICFBamUInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( ICFBamUInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( ICFBamUInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( ICFBamUInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( ICFBamUInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( ICFBamUuidDef.CLASS_CODE == subClassCode ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( ICFBamUuidType.CLASS_CODE == subClassCode ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( ICFBamUuidGen.CLASS_CODE == subClassCode ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( ICFBamUuidCol.CLASS_CODE == subClassCode ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( ICFBamUuid6Def.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( ICFBamUuid6Type.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( ICFBamUuid6Gen.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( ICFBamUuid6Col.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else if( ICFBamTableCol.CLASS_CODE == subClassCode ) {
				schema.getTableTableCol().deleteTableCol( Authorization, (ICFBamTableCol)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteValueByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffValueByContPrevIdxKey key = (CFBamBuffValueByContPrevIdxKey)schema.getFactoryValue().newByContPrevIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalPrevId( argPrevId );
		deleteValueByContPrevIdx( Authorization, key );
	}

	@Override
	public void deleteValueByContPrevIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContPrevIdxKey argKey )
	{
		final String S_ProcName = "deleteValueByContPrevIdx";
		CFBamBuffValue cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffValue> matchSet = new LinkedList<CFBamBuffValue>();
		Iterator<CFBamBuffValue> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffValue> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamValue.CLASS_CODE == subClassCode ) {
				schema.getTableValue().deleteValue( Authorization, cur );
			}
			else if( ICFBamAtom.CLASS_CODE == subClassCode ) {
				schema.getTableAtom().deleteAtom( Authorization, (ICFBamAtom)cur );
			}
			else if( ICFBamBlobDef.CLASS_CODE == subClassCode ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( ICFBamBlobType.CLASS_CODE == subClassCode ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( ICFBamBlobCol.CLASS_CODE == subClassCode ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( ICFBamBoolDef.CLASS_CODE == subClassCode ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( ICFBamBoolType.CLASS_CODE == subClassCode ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( ICFBamBoolCol.CLASS_CODE == subClassCode ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( ICFBamDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( ICFBamDateType.CLASS_CODE == subClassCode ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( ICFBamDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( ICFBamDoubleDef.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( ICFBamDoubleType.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( ICFBamDoubleCol.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( ICFBamFloatDef.CLASS_CODE == subClassCode ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( ICFBamFloatType.CLASS_CODE == subClassCode ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( ICFBamFloatCol.CLASS_CODE == subClassCode ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( ICFBamInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( ICFBamInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( ICFBamId16Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( ICFBamEnumDef.CLASS_CODE == subClassCode ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( ICFBamEnumType.CLASS_CODE == subClassCode ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( ICFBamInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( ICFBamInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( ICFBamInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( ICFBamId32Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( ICFBamInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( ICFBamInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( ICFBamInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( ICFBamId64Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( ICFBamInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( ICFBamNmTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( ICFBamNmTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( ICFBamNmTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( ICFBamNmTokensDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( ICFBamNmTokensType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( ICFBamNmTokensCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( ICFBamNumberDef.CLASS_CODE == subClassCode ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( ICFBamNumberType.CLASS_CODE == subClassCode ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( ICFBamNumberCol.CLASS_CODE == subClassCode ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( ICFBamDbKeyHash128Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( ICFBamDbKeyHash128Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( ICFBamDbKeyHash128Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( ICFBamDbKeyHash128Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( ICFBamDbKeyHash160Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( ICFBamDbKeyHash160Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( ICFBamDbKeyHash160Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( ICFBamDbKeyHash160Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( ICFBamDbKeyHash224Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( ICFBamDbKeyHash224Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( ICFBamDbKeyHash224Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( ICFBamDbKeyHash224Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( ICFBamDbKeyHash256Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( ICFBamDbKeyHash256Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( ICFBamDbKeyHash256Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( ICFBamDbKeyHash256Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( ICFBamDbKeyHash384Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( ICFBamDbKeyHash384Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( ICFBamDbKeyHash384Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( ICFBamDbKeyHash384Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( ICFBamDbKeyHash512Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( ICFBamDbKeyHash512Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( ICFBamStringDef.CLASS_CODE == subClassCode ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( ICFBamStringType.CLASS_CODE == subClassCode ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( ICFBamStringCol.CLASS_CODE == subClassCode ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( ICFBamTZDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( ICFBamTZDateType.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( ICFBamTZDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( ICFBamTZTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( ICFBamTZTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( ICFBamTZTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( ICFBamTZTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( ICFBamTZTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( ICFBamTZTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( ICFBamTextDef.CLASS_CODE == subClassCode ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( ICFBamTextType.CLASS_CODE == subClassCode ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( ICFBamTextCol.CLASS_CODE == subClassCode ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( ICFBamTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( ICFBamTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( ICFBamTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( ICFBamTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( ICFBamTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( ICFBamTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( ICFBamTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( ICFBamTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( ICFBamTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( ICFBamUInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( ICFBamUInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( ICFBamUInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( ICFBamUInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( ICFBamUInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( ICFBamUInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( ICFBamUInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( ICFBamUInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( ICFBamUInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( ICFBamUuidDef.CLASS_CODE == subClassCode ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( ICFBamUuidType.CLASS_CODE == subClassCode ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( ICFBamUuidGen.CLASS_CODE == subClassCode ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( ICFBamUuidCol.CLASS_CODE == subClassCode ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( ICFBamUuid6Def.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( ICFBamUuid6Type.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( ICFBamUuid6Gen.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( ICFBamUuid6Col.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else if( ICFBamTableCol.CLASS_CODE == subClassCode ) {
				schema.getTableTableCol().deleteTableCol( Authorization, (ICFBamTableCol)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}

	@Override
	public void deleteValueByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argScopeId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffValueByContNextIdxKey key = (CFBamBuffValueByContNextIdxKey)schema.getFactoryValue().newByContNextIdxKey();
		key.setRequiredScopeId( argScopeId );
		key.setOptionalNextId( argNextId );
		deleteValueByContNextIdx( Authorization, key );
	}

	@Override
	public void deleteValueByContNextIdx( ICFSecAuthorization Authorization,
		ICFBamValueByContNextIdxKey argKey )
	{
		final String S_ProcName = "deleteValueByContNextIdx";
		CFBamBuffValue cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffValue> matchSet = new LinkedList<CFBamBuffValue>();
		Iterator<CFBamBuffValue> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffValue> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffValue)(schema.getTableValue().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			int subClassCode = cur.getClassCode();
			if( ICFBamValue.CLASS_CODE == subClassCode ) {
				schema.getTableValue().deleteValue( Authorization, cur );
			}
			else if( ICFBamAtom.CLASS_CODE == subClassCode ) {
				schema.getTableAtom().deleteAtom( Authorization, (ICFBamAtom)cur );
			}
			else if( ICFBamBlobDef.CLASS_CODE == subClassCode ) {
				schema.getTableBlobDef().deleteBlobDef( Authorization, (ICFBamBlobDef)cur );
			}
			else if( ICFBamBlobType.CLASS_CODE == subClassCode ) {
				schema.getTableBlobType().deleteBlobType( Authorization, (ICFBamBlobType)cur );
			}
			else if( ICFBamBlobCol.CLASS_CODE == subClassCode ) {
				schema.getTableBlobCol().deleteBlobCol( Authorization, (ICFBamBlobCol)cur );
			}
			else if( ICFBamBoolDef.CLASS_CODE == subClassCode ) {
				schema.getTableBoolDef().deleteBoolDef( Authorization, (ICFBamBoolDef)cur );
			}
			else if( ICFBamBoolType.CLASS_CODE == subClassCode ) {
				schema.getTableBoolType().deleteBoolType( Authorization, (ICFBamBoolType)cur );
			}
			else if( ICFBamBoolCol.CLASS_CODE == subClassCode ) {
				schema.getTableBoolCol().deleteBoolCol( Authorization, (ICFBamBoolCol)cur );
			}
			else if( ICFBamDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableDateDef().deleteDateDef( Authorization, (ICFBamDateDef)cur );
			}
			else if( ICFBamDateType.CLASS_CODE == subClassCode ) {
				schema.getTableDateType().deleteDateType( Authorization, (ICFBamDateType)cur );
			}
			else if( ICFBamDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableDateCol().deleteDateCol( Authorization, (ICFBamDateCol)cur );
			}
			else if( ICFBamDoubleDef.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleDef().deleteDoubleDef( Authorization, (ICFBamDoubleDef)cur );
			}
			else if( ICFBamDoubleType.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleType().deleteDoubleType( Authorization, (ICFBamDoubleType)cur );
			}
			else if( ICFBamDoubleCol.CLASS_CODE == subClassCode ) {
				schema.getTableDoubleCol().deleteDoubleCol( Authorization, (ICFBamDoubleCol)cur );
			}
			else if( ICFBamFloatDef.CLASS_CODE == subClassCode ) {
				schema.getTableFloatDef().deleteFloatDef( Authorization, (ICFBamFloatDef)cur );
			}
			else if( ICFBamFloatType.CLASS_CODE == subClassCode ) {
				schema.getTableFloatType().deleteFloatType( Authorization, (ICFBamFloatType)cur );
			}
			else if( ICFBamFloatCol.CLASS_CODE == subClassCode ) {
				schema.getTableFloatCol().deleteFloatCol( Authorization, (ICFBamFloatCol)cur );
			}
			else if( ICFBamInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Def().deleteInt16Def( Authorization, (ICFBamInt16Def)cur );
			}
			else if( ICFBamInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Type().deleteInt16Type( Authorization, (ICFBamInt16Type)cur );
			}
			else if( ICFBamId16Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId16Gen().deleteId16Gen( Authorization, (ICFBamId16Gen)cur );
			}
			else if( ICFBamEnumDef.CLASS_CODE == subClassCode ) {
				schema.getTableEnumDef().deleteEnumDef( Authorization, (ICFBamEnumDef)cur );
			}
			else if( ICFBamEnumType.CLASS_CODE == subClassCode ) {
				schema.getTableEnumType().deleteEnumType( Authorization, (ICFBamEnumType)cur );
			}
			else if( ICFBamInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt16Col().deleteInt16Col( Authorization, (ICFBamInt16Col)cur );
			}
			else if( ICFBamInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Def().deleteInt32Def( Authorization, (ICFBamInt32Def)cur );
			}
			else if( ICFBamInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Type().deleteInt32Type( Authorization, (ICFBamInt32Type)cur );
			}
			else if( ICFBamId32Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId32Gen().deleteId32Gen( Authorization, (ICFBamId32Gen)cur );
			}
			else if( ICFBamInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt32Col().deleteInt32Col( Authorization, (ICFBamInt32Col)cur );
			}
			else if( ICFBamInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Def().deleteInt64Def( Authorization, (ICFBamInt64Def)cur );
			}
			else if( ICFBamInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Type().deleteInt64Type( Authorization, (ICFBamInt64Type)cur );
			}
			else if( ICFBamId64Gen.CLASS_CODE == subClassCode ) {
				schema.getTableId64Gen().deleteId64Gen( Authorization, (ICFBamId64Gen)cur );
			}
			else if( ICFBamInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableInt64Col().deleteInt64Col( Authorization, (ICFBamInt64Col)cur );
			}
			else if( ICFBamNmTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenDef().deleteNmTokenDef( Authorization, (ICFBamNmTokenDef)cur );
			}
			else if( ICFBamNmTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenType().deleteNmTokenType( Authorization, (ICFBamNmTokenType)cur );
			}
			else if( ICFBamNmTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokenCol().deleteNmTokenCol( Authorization, (ICFBamNmTokenCol)cur );
			}
			else if( ICFBamNmTokensDef.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensDef().deleteNmTokensDef( Authorization, (ICFBamNmTokensDef)cur );
			}
			else if( ICFBamNmTokensType.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensType().deleteNmTokensType( Authorization, (ICFBamNmTokensType)cur );
			}
			else if( ICFBamNmTokensCol.CLASS_CODE == subClassCode ) {
				schema.getTableNmTokensCol().deleteNmTokensCol( Authorization, (ICFBamNmTokensCol)cur );
			}
			else if( ICFBamNumberDef.CLASS_CODE == subClassCode ) {
				schema.getTableNumberDef().deleteNumberDef( Authorization, (ICFBamNumberDef)cur );
			}
			else if( ICFBamNumberType.CLASS_CODE == subClassCode ) {
				schema.getTableNumberType().deleteNumberType( Authorization, (ICFBamNumberType)cur );
			}
			else if( ICFBamNumberCol.CLASS_CODE == subClassCode ) {
				schema.getTableNumberCol().deleteNumberCol( Authorization, (ICFBamNumberCol)cur );
			}
			else if( ICFBamDbKeyHash128Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Def().deleteDbKeyHash128Def( Authorization, (ICFBamDbKeyHash128Def)cur );
			}
			else if( ICFBamDbKeyHash128Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Col().deleteDbKeyHash128Col( Authorization, (ICFBamDbKeyHash128Col)cur );
			}
			else if( ICFBamDbKeyHash128Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Type().deleteDbKeyHash128Type( Authorization, (ICFBamDbKeyHash128Type)cur );
			}
			else if( ICFBamDbKeyHash128Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash128Gen().deleteDbKeyHash128Gen( Authorization, (ICFBamDbKeyHash128Gen)cur );
			}
			else if( ICFBamDbKeyHash160Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Def().deleteDbKeyHash160Def( Authorization, (ICFBamDbKeyHash160Def)cur );
			}
			else if( ICFBamDbKeyHash160Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Col().deleteDbKeyHash160Col( Authorization, (ICFBamDbKeyHash160Col)cur );
			}
			else if( ICFBamDbKeyHash160Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Type().deleteDbKeyHash160Type( Authorization, (ICFBamDbKeyHash160Type)cur );
			}
			else if( ICFBamDbKeyHash160Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash160Gen().deleteDbKeyHash160Gen( Authorization, (ICFBamDbKeyHash160Gen)cur );
			}
			else if( ICFBamDbKeyHash224Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Def().deleteDbKeyHash224Def( Authorization, (ICFBamDbKeyHash224Def)cur );
			}
			else if( ICFBamDbKeyHash224Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Col().deleteDbKeyHash224Col( Authorization, (ICFBamDbKeyHash224Col)cur );
			}
			else if( ICFBamDbKeyHash224Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Type().deleteDbKeyHash224Type( Authorization, (ICFBamDbKeyHash224Type)cur );
			}
			else if( ICFBamDbKeyHash224Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash224Gen().deleteDbKeyHash224Gen( Authorization, (ICFBamDbKeyHash224Gen)cur );
			}
			else if( ICFBamDbKeyHash256Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Def().deleteDbKeyHash256Def( Authorization, (ICFBamDbKeyHash256Def)cur );
			}
			else if( ICFBamDbKeyHash256Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Col().deleteDbKeyHash256Col( Authorization, (ICFBamDbKeyHash256Col)cur );
			}
			else if( ICFBamDbKeyHash256Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Type().deleteDbKeyHash256Type( Authorization, (ICFBamDbKeyHash256Type)cur );
			}
			else if( ICFBamDbKeyHash256Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash256Gen().deleteDbKeyHash256Gen( Authorization, (ICFBamDbKeyHash256Gen)cur );
			}
			else if( ICFBamDbKeyHash384Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Def().deleteDbKeyHash384Def( Authorization, (ICFBamDbKeyHash384Def)cur );
			}
			else if( ICFBamDbKeyHash384Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Col().deleteDbKeyHash384Col( Authorization, (ICFBamDbKeyHash384Col)cur );
			}
			else if( ICFBamDbKeyHash384Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Type().deleteDbKeyHash384Type( Authorization, (ICFBamDbKeyHash384Type)cur );
			}
			else if( ICFBamDbKeyHash384Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash384Gen().deleteDbKeyHash384Gen( Authorization, (ICFBamDbKeyHash384Gen)cur );
			}
			else if( ICFBamDbKeyHash512Def.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Def().deleteDbKeyHash512Def( Authorization, (ICFBamDbKeyHash512Def)cur );
			}
			else if( ICFBamDbKeyHash512Col.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Col().deleteDbKeyHash512Col( Authorization, (ICFBamDbKeyHash512Col)cur );
			}
			else if( ICFBamDbKeyHash512Type.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Type().deleteDbKeyHash512Type( Authorization, (ICFBamDbKeyHash512Type)cur );
			}
			else if( ICFBamDbKeyHash512Gen.CLASS_CODE == subClassCode ) {
				schema.getTableDbKeyHash512Gen().deleteDbKeyHash512Gen( Authorization, (ICFBamDbKeyHash512Gen)cur );
			}
			else if( ICFBamStringDef.CLASS_CODE == subClassCode ) {
				schema.getTableStringDef().deleteStringDef( Authorization, (ICFBamStringDef)cur );
			}
			else if( ICFBamStringType.CLASS_CODE == subClassCode ) {
				schema.getTableStringType().deleteStringType( Authorization, (ICFBamStringType)cur );
			}
			else if( ICFBamStringCol.CLASS_CODE == subClassCode ) {
				schema.getTableStringCol().deleteStringCol( Authorization, (ICFBamStringCol)cur );
			}
			else if( ICFBamTZDateDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateDef().deleteTZDateDef( Authorization, (ICFBamTZDateDef)cur );
			}
			else if( ICFBamTZDateType.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateType().deleteTZDateType( Authorization, (ICFBamTZDateType)cur );
			}
			else if( ICFBamTZDateCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZDateCol().deleteTZDateCol( Authorization, (ICFBamTZDateCol)cur );
			}
			else if( ICFBamTZTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeDef().deleteTZTimeDef( Authorization, (ICFBamTZTimeDef)cur );
			}
			else if( ICFBamTZTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeType().deleteTZTimeType( Authorization, (ICFBamTZTimeType)cur );
			}
			else if( ICFBamTZTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimeCol().deleteTZTimeCol( Authorization, (ICFBamTZTimeCol)cur );
			}
			else if( ICFBamTZTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampDef().deleteTZTimestampDef( Authorization, (ICFBamTZTimestampDef)cur );
			}
			else if( ICFBamTZTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampType().deleteTZTimestampType( Authorization, (ICFBamTZTimestampType)cur );
			}
			else if( ICFBamTZTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTZTimestampCol().deleteTZTimestampCol( Authorization, (ICFBamTZTimestampCol)cur );
			}
			else if( ICFBamTextDef.CLASS_CODE == subClassCode ) {
				schema.getTableTextDef().deleteTextDef( Authorization, (ICFBamTextDef)cur );
			}
			else if( ICFBamTextType.CLASS_CODE == subClassCode ) {
				schema.getTableTextType().deleteTextType( Authorization, (ICFBamTextType)cur );
			}
			else if( ICFBamTextCol.CLASS_CODE == subClassCode ) {
				schema.getTableTextCol().deleteTextCol( Authorization, (ICFBamTextCol)cur );
			}
			else if( ICFBamTimeDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimeDef().deleteTimeDef( Authorization, (ICFBamTimeDef)cur );
			}
			else if( ICFBamTimeType.CLASS_CODE == subClassCode ) {
				schema.getTableTimeType().deleteTimeType( Authorization, (ICFBamTimeType)cur );
			}
			else if( ICFBamTimeCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimeCol().deleteTimeCol( Authorization, (ICFBamTimeCol)cur );
			}
			else if( ICFBamTimestampDef.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampDef().deleteTimestampDef( Authorization, (ICFBamTimestampDef)cur );
			}
			else if( ICFBamTimestampType.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampType().deleteTimestampType( Authorization, (ICFBamTimestampType)cur );
			}
			else if( ICFBamTimestampCol.CLASS_CODE == subClassCode ) {
				schema.getTableTimestampCol().deleteTimestampCol( Authorization, (ICFBamTimestampCol)cur );
			}
			else if( ICFBamTokenDef.CLASS_CODE == subClassCode ) {
				schema.getTableTokenDef().deleteTokenDef( Authorization, (ICFBamTokenDef)cur );
			}
			else if( ICFBamTokenType.CLASS_CODE == subClassCode ) {
				schema.getTableTokenType().deleteTokenType( Authorization, (ICFBamTokenType)cur );
			}
			else if( ICFBamTokenCol.CLASS_CODE == subClassCode ) {
				schema.getTableTokenCol().deleteTokenCol( Authorization, (ICFBamTokenCol)cur );
			}
			else if( ICFBamUInt16Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Def().deleteUInt16Def( Authorization, (ICFBamUInt16Def)cur );
			}
			else if( ICFBamUInt16Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Type().deleteUInt16Type( Authorization, (ICFBamUInt16Type)cur );
			}
			else if( ICFBamUInt16Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt16Col().deleteUInt16Col( Authorization, (ICFBamUInt16Col)cur );
			}
			else if( ICFBamUInt32Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Def().deleteUInt32Def( Authorization, (ICFBamUInt32Def)cur );
			}
			else if( ICFBamUInt32Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Type().deleteUInt32Type( Authorization, (ICFBamUInt32Type)cur );
			}
			else if( ICFBamUInt32Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt32Col().deleteUInt32Col( Authorization, (ICFBamUInt32Col)cur );
			}
			else if( ICFBamUInt64Def.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Def().deleteUInt64Def( Authorization, (ICFBamUInt64Def)cur );
			}
			else if( ICFBamUInt64Type.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Type().deleteUInt64Type( Authorization, (ICFBamUInt64Type)cur );
			}
			else if( ICFBamUInt64Col.CLASS_CODE == subClassCode ) {
				schema.getTableUInt64Col().deleteUInt64Col( Authorization, (ICFBamUInt64Col)cur );
			}
			else if( ICFBamUuidDef.CLASS_CODE == subClassCode ) {
				schema.getTableUuidDef().deleteUuidDef( Authorization, (ICFBamUuidDef)cur );
			}
			else if( ICFBamUuidType.CLASS_CODE == subClassCode ) {
				schema.getTableUuidType().deleteUuidType( Authorization, (ICFBamUuidType)cur );
			}
			else if( ICFBamUuidGen.CLASS_CODE == subClassCode ) {
				schema.getTableUuidGen().deleteUuidGen( Authorization, (ICFBamUuidGen)cur );
			}
			else if( ICFBamUuidCol.CLASS_CODE == subClassCode ) {
				schema.getTableUuidCol().deleteUuidCol( Authorization, (ICFBamUuidCol)cur );
			}
			else if( ICFBamUuid6Def.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Def().deleteUuid6Def( Authorization, (ICFBamUuid6Def)cur );
			}
			else if( ICFBamUuid6Type.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Type().deleteUuid6Type( Authorization, (ICFBamUuid6Type)cur );
			}
			else if( ICFBamUuid6Gen.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Gen().deleteUuid6Gen( Authorization, (ICFBamUuid6Gen)cur );
			}
			else if( ICFBamUuid6Col.CLASS_CODE == subClassCode ) {
				schema.getTableUuid6Col().deleteUuid6Col( Authorization, (ICFBamUuid6Col)cur );
			}
			else if( ICFBamTableCol.CLASS_CODE == subClassCode ) {
				schema.getTableTableCol().deleteTableCol( Authorization, (ICFBamTableCol)cur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-by-suffix-class-walker-", (Integer)subClassCode, "Classcode not recognized: " + Integer.toString(subClassCode));
			}
		}
	}
}
