package com.shareyi.molicode.service.gencode.impl

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.google.common.base.Function
import com.shareyi.molicode.common.chain.HandlerChainFactoryImpl
import com.shareyi.molicode.common.chain.handler.awares.TableModelHandlerAware
import com.shareyi.molicode.common.constants.CommonConstant
import com.shareyi.molicode.common.constants.ConfigKeyConstant
import com.shareyi.molicode.common.enums.DataTypeEnum
import com.shareyi.molicode.common.enums.DatabaseNameEnum
import com.shareyi.molicode.common.enums.TableSourceNameEnum
import com.shareyi.molicode.common.utils.*
import com.shareyi.molicode.common.valid.Validate
import com.shareyi.molicode.common.vo.code.SimpleTableInfoVo
import com.shareyi.molicode.common.vo.code.TableModelVo
import com.shareyi.molicode.common.vo.page.TableModelPageVo
import com.shareyi.molicode.common.web.CommonResult
import com.shareyi.molicode.context.TableModelContext
import com.shareyi.molicode.service.conf.AcConfigService
import com.shareyi.molicode.service.gencode.DatabaseTableService
import com.shareyi.molicode.service.gencode.SqlParseService
import groovy.sql.Sql
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.MapUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

import javax.annotation.Resource
import java.sql.DatabaseMetaData

@Service("databaseTableService")
class DatabaseTableServiceImpl implements DatabaseTableService {


    @Resource
    private AcConfigService acConfigService;

    @Resource
    private SqlParseService sqlParseService;

    CommonResult generateTableModel(TableModelPageVo tableModelPageVo) {
        CommonResult<String> result = CommonResult.create();
        try {
            def context = TableModelContext.create(tableModelPageVo)
            HandlerChainFactoryImpl.executeByHandlerAware(TableModelHandlerAware.class, context);
            result.succeed(context.outputPath);
        } catch (Exception e) {
            LogHelper.EXCEPTION.error("????????????????????????tableModelPageVo={}", tableModelPageVo, e);
            result.failed("?????????????????????, ???:" + tableModelPageVo.tableName + ", ?????????:" + e.getMessage());
        }
        return result;
    }

    @Override
    CommonResult<TableModelVo> getTableInfo(TableModelPageVo tableModelPageVo) {
        CommonResult<TableModelVo> result = CommonResult.create();
        try {
            ValidateUtils.notEmptyField(tableModelPageVo, "projectKey");
            ValidateUtils.notEmptyField(tableModelPageVo, "tableName");
            tableModelPageVo.setSmartFlag(CommonConstant.STD_YN_YES);
            tableModelPageVo.setTableModelDir(SystemFileUtils.getTableModelDir(tableModelPageVo.projectKey))
            tableModelPageVo.setModelType(CommonConstant.MODEL_TYPE_JSON);
            def context = TableModelContext.create(tableModelPageVo)
            if (Objects.equals(TableSourceNameEnum.SQL.code, tableModelPageVo.tableSourceName)) {
                context.setReadonly(true);
            }
            HandlerChainFactoryImpl.executeByHandlerAware(TableModelHandlerAware.class, context);
            result.addDefaultModel(context.getTableModelVo())
            result.addModel("customFieldGroupList", context.fetchCustomFieldGroupList());
            result.succeed();
        } catch (Exception e) {
            LogHelper.EXCEPTION.error("????????????????????????tableName={}", tableModelPageVo.tableName, e);
            result.failed("?????????????????????, ???:" + tableModelPageVo.tableName + ", ?????????:" + e.getMessage());
        }
        return result;
    }

    @Override
    CommonResult<Object> saveTableModel(String projectKey, String tableModelJson) {
        CommonResult<String> result = CommonResult.create();
        try {
            TableModelVo tableModelVo = JSON.parseObject(tableModelJson, TableModelVo.class);
            tableModelVo.clearCache();
            String tableModelDir = FileIoUtil.getRuntimeFilePath("tableModel/project_" + projectKey);
            String fullPath = FileIoUtil.contactPath(tableModelDir, tableModelVo.getTableDefine().dbTableName + ".json");
            File file = new File(fullPath);
            FileIoUtil.makeSureFileExist(file);
            def newJson = JSON.toJSONString(tableModelVo, SerializerFeature.PrettyFormat, SerializerFeature.QuoteFieldNames, SerializerFeature.UseSingleQuotes, SerializerFeature.DisableCircularReferenceDetect);
            FileUtils.write(file, newJson, Profiles.instance.fileEncoding, false);
            result.addDefaultModel(file.getAbsolutePath());
            result.succeed();
        } catch (Exception e) {
            LogHelper.EXCEPTION.error("????????????????????????projectKey={}, json={}", projectKey, tableModelJson, e);
            result.failed("?????????????????????, ?????????:" + e.getMessage());
        }
        return result;
    }

    @Override
    CommonResult<List<SimpleTableInfoVo>> getTableListBySql(String projectKey, String createSql) {
        CommonResult result = CommonResult.create();
        try {
            Validate.notEmpty(projectKey, "projectKey????????????")
            Validate.notEmpty(createSql, "createSql????????????")
            Map<String, Map<String, String>> configMap = acConfigService.getConfigMapByProjectKey(projectKey, DataTypeEnum.JSON);
            Map<String, String> databaseConfigMap = configMap.get(ConfigKeyConstant.DatabaseConfig.CONFIG_KEY);
            String databaseName = MapUtils.getString(databaseConfigMap, ConfigKeyConstant.DatabaseConfig.DATABASE_NAME);
            if (StringUtils.isEmpty(databaseName)) {
                databaseName = DatabaseNameEnum.MYSQL.code;
            }
            Validate.notEmpty(databaseName, "???????????????????????????")
            List<TableModelVo> tableModelVoList = sqlParseService.parseCreateSql(projectKey, createSql, databaseName);
            List<SimpleTableInfoVo> list = MyLists.transform(tableModelVoList, new Function<TableModelVo, SimpleTableInfoVo>() {
                @Override
                SimpleTableInfoVo apply(TableModelVo input) {
                    SimpleTableInfoVo tableInfoVo = new SimpleTableInfoVo();
                    tableInfoVo.setId(input.tableDefine.id)
                    tableInfoVo.setCnname(input.tableDefine.cnname)
                    tableInfoVo.setTableName(input.tableDefine.dbTableName)
                    tableInfoVo.setSourceName(TableSourceNameEnum.SQL.code);
                    return tableInfoVo
                }
            });
            result.addDefaultModel(list)
            result.succeed()
        } catch (Exception e) {
            LogHelper.DEFAULT.error("??????????????????????????????", e)
            result.failed("????????????????????????????????? ????????????" + e.getMessage())
        }
        return result;
    }
/**
 * ????????????????????????tableModel xml??????
 * @param tName ??????
 */
    CommonResult<List<SimpleTableInfoVo>> getTableList(String projectKey) {
        CommonResult result = CommonResult.create();
        Sql sql = null;
        try {
            Validate.notEmpty(projectKey, "projectKey????????????")
            Map<String, Map<String, String>> configMap = acConfigService.getConfigMapByProjectKey(projectKey, DataTypeEnum.JSON);
            Map<String, String> databaseConfigMap = configMap.get(ConfigKeyConstant.DatabaseConfig.CONFIG_KEY);
            String driverName = MapUtils.getString(databaseConfigMap, ConfigKeyConstant.DatabaseConfig.DRIVER_CLASS);
            String url = MapUtils.getString(databaseConfigMap, ConfigKeyConstant.DatabaseConfig.URL);
            String username = MapUtils.getString(databaseConfigMap, ConfigKeyConstant.DatabaseConfig.USERNAME);
            String password = MapUtils.getString(databaseConfigMap, ConfigKeyConstant.DatabaseConfig.PASSWORD);

            Map<String, String> jsonConfigMap = ConfigUtil.getJsonConfigMap(configMap);
            boolean camelNameConvert = MapUtils.getBoolean(jsonConfigMap, ConfigKeyConstant.ExtConfig.CAMEL_NAME_KEY, true);
            Validate.notEmpty(driverName, "???????????????????????????")
            Validate.notEmpty(url, "?????????url????????????")
            Validate.notEmpty(username, "?????????userName????????????")
            // Validate.notEmpty(password, "???????????????????????????")

            sql = Sql.newInstance(url, username, password, driverName);
            TableNameUtil tableNameUtil = new TableNameUtil()
            def meta = sql.connection.metaData
            String[] types = new String[1];
            types[0] = "TABLE";

            def list = [];
            String database = getDataBaseName(meta)
            def tableInfo = meta.getTables(database, null, null, types);
            while (tableInfo.next()) {
                SimpleTableInfoVo tInfo = new SimpleTableInfoVo();
                String tableName = tableInfo.getString("TABLE_NAME");
                tInfo.tableName = tableName;
                tInfo.id = tableName;
                if (camelNameConvert) {
                    tInfo.id = tableNameUtil.upperFirst(tableNameUtil.convertToBeanNames(tableName));
                }
                tInfo.cnname = tInfo.id;
                tInfo.setSourceName(TableSourceNameEnum.DATABASE.code);
                list.add(tInfo)
            }

            LinkedHashMap commentMap = getTableCommentMap(sql, meta)

            list.each { tableInfoVo ->
                String comment = commentMap.get(tableInfoVo.tableName);
                if (StringUtils.isNotEmpty(comment)) {
                    tableInfoVo.cnname = comment;
                }
            }
            loadLocalConfig(projectKey, list);
            result.addDefaultModel(list)
            result.succeed()
        } catch (Exception e) {
            LogHelper.DEFAULT.error("??????????????????????????????", e)
            result.failed("????????????????????????????????? ????????????" + e.getMessage())
        } finally {
            if (sql != null) {
                sql.close();
            }
        }
        return result;
    }

    /**
     * ?????????????????????
     * @param meta
     * @return
     */
    private String getDataBaseName(DatabaseMetaData meta) {
        String database = null;
        try {
            database = meta.database;
        } catch (Exception e) {
            LogHelper.EXCEPTION.error("??????????????????????????????", e)
        }
        return database
    }

    private LinkedHashMap getTableCommentMap(Sql sql, DatabaseMetaData meta) {
        def commentMap = [:]
        try {
            sql.eachRow("Select table_name,table_comment from INFORMATION_SCHEMA.TABLES Where table_schema = ? ", [meta['database']], { row ->
                commentMap.put(row[0], row[1])
            });
        } catch (Exception e) {
            LogHelper.EXCEPTION.error("?????????????????????", e);
        }
        commentMap
    }

    /**
     * ?????????????????????????????????
     * @param projectKey
     * @param list
     */
    void loadLocalConfig(String projectKey, List<SimpleTableInfoVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        String tableModelDir = SystemFileUtils.getTableModelDir(projectKey);
        list.each { simpleTableInfoVo ->
            try {
                String filePath = FileIoUtil.contactPath(tableModelDir, simpleTableInfoVo.tableName + ".json")
                File file = new File(filePath);
                if (file.exists()) {
                    String tableModelJson = FileUtils.readFileToString(file, Profiles.instance.fileEncoding);
                    TableModelVo tableModelVo = JSON.parseObject(tableModelJson, TableModelVo.class);
                    simpleTableInfoVo.setId(tableModelVo.tableDefine.id);
                    simpleTableInfoVo.setCnname(tableModelVo.tableDefine.cnname);
                }
            } catch (Exception e) {
                LogHelper.EXCEPTION.error("??????tableModel?????????????????????table={}", simpleTableInfoVo.tableName, e);
            }
        }
    }
}
