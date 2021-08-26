package com.shareyi.molicode.service.gencode.impl

import com.alibaba.druid.sql.SQLUtils
import com.alibaba.druid.sql.ast.SQLDataType
import com.alibaba.druid.sql.ast.SQLExpr
import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr
import com.alibaba.druid.sql.ast.statement.*
import com.google.common.collect.Lists
import com.shareyi.molicode.common.chain.HandlerChainFactoryImpl
import com.shareyi.molicode.common.chain.handler.awares.TableModelHandlerAware
import com.shareyi.molicode.common.constants.CommonConstant
import com.shareyi.molicode.common.constants.ConfigKeyConstant
import com.shareyi.molicode.common.enums.DataTypeEnum
import com.shareyi.molicode.common.enums.DatabaseNameEnum
import com.shareyi.molicode.common.enums.EnumCode
import com.shareyi.molicode.common.utils.*
import com.shareyi.molicode.common.vo.code.ColumnVo
import com.shareyi.molicode.common.vo.code.TableDefineVo
import com.shareyi.molicode.common.vo.code.TableModelVo
import com.shareyi.molicode.common.vo.page.TableModelPageVo
import com.shareyi.molicode.context.TableModelContext
import com.shareyi.molicode.service.conf.AcConfigService
import com.shareyi.molicode.service.gencode.SqlParseService
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.MapUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

import javax.annotation.Resource

/**
 * sql解析服务
 *
 * @author david* @date 2019/7/4
 */
@Service
class SqlParseServiceImpl implements SqlParseService {

    @Resource
    private AcConfigService acConfigService;

    @Override
    List<TableModelVo> parseCreateSql(String projectKey, String createSql, String databaseName) {
        DatabaseNameEnum databaseNameEnum = EnumCode.Parser.parseTo(DatabaseNameEnum.class, databaseName);
        List<SQLStatement> stmtList = SQLUtils.parseStatements(createSql, databaseNameEnum.getCode());
        LogHelper.DEFAULT.info("stmtList.size={}", stmtList.size());
        List<TableModelVo> list = Lists.newArrayListWithCapacity(20);
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            if (stmt instanceof SQLCreateTableStatement) {
                TableModelVo tableModelVo = parseToTableModelVo((SQLCreateTableStatement) stmt, projectKey);
                list.add(tableModelVo);
            }
        }
        TableModelPageVo tableModelPageVo = new TableModelPageVo();
        tableModelPageVo.setProjectKey(projectKey);
        tableModelPageVo.setSmartFlag(CommonConstant.STD_YN_YES);
        tableModelPageVo.setTableModelDir(SystemFileUtils.getTableModelDir(projectKey))
        tableModelPageVo.setModelType(CommonConstant.MODEL_TYPE_JSON);

        //将tableModel信息，存储到本地json文件之中
        for (TableModelVo tableModelVo : list) {
            tableModelPageVo.setTableName(tableModelVo.tableDefine.dbTableName);
            TableModelContext context = TableModelContext.createByTableModel(tableModelVo)
            context.tableModelPageVo = tableModelPageVo;
            HandlerChainFactoryImpl.executeByHandlerAware(TableModelHandlerAware.class, context);
        }
        return list;
    }

    /**
     * 转换为tableModel对象
     *
     * @param sqlCreateTableStatement
     * @param projectKey
     * @return
     */
    private TableModelVo parseToTableModelVo(SQLCreateTableStatement sqlCreateTableStatement, String projectKey) {
        Map<String, Map<String, String>> projectConfigMap = acConfigService.getConfigMapByProjectKey(projectKey, DataTypeEnum.JSON);
        Map<String, String> jsonConfigMap = ConfigUtil.getJsonConfigMap(projectConfigMap);
        boolean camelNameConvert = MapUtils.getBoolean(jsonConfigMap, ConfigKeyConstant.ExtConfig.CAMEL_NAME_KEY, true);

        TableModelVo tableModelVo = new TableModelVo();
        TableDefineVo tableDefineVo = new TableDefineVo();
        tableModelVo.tableDefine = tableDefineVo;
        TableNameUtil tableNameUtil = new TableNameUtil();


        LogHelper.DEFAULT.info("table comment:{}" + sqlCreateTableStatement.getComment());
        tableDefineVo.dbTableName = MoliCodeStringUtils.removeQuot(sqlCreateTableStatement.getName().getSimpleName());
        tableDefineVo.cnname = MoliCodeStringUtils.removeQuot(sqlCreateTableStatement.getComment().toString());
        if (StringUtils.isEmpty(tableDefineVo.cnname)) {
            tableDefineVo.cnname = tableDefineVo.dbTableName;
        }
        tableDefineVo.id = tableDefineVo.dbTableName;
        if (camelNameConvert) {
            tableDefineVo.id = tableNameUtil.upperFirst(tableNameUtil.convertToBeanNames(tableDefineVo.dbTableName));
        }
        List<ColumnVo> columnVoList = Lists.newArrayList();
        tableDefineVo.columns = columnVoList;
        List<SQLTableElement> columnList = sqlCreateTableStatement.getTableElementList();

        List<String> primaryKeyList = Lists.newArrayList();
        for (SQLTableElement sqlTableElement : columnList) {
            if (sqlTableElement instanceof SQLColumnDefinition) {
                SQLColumnDefinition sqlColumnDefinition = (SQLColumnDefinition) sqlTableElement;
                LogHelper.DEFAULT.info("columnInfo, name={}, comment={}, dataType={}, nullable={}", sqlColumnDefinition.getNameAsString(), sqlColumnDefinition.getComment(), sqlColumnDefinition.getDataType(), sqlColumnDefinition.containsNotNullConstaint());

                ColumnVo columnVo = new ColumnVo();
                columnVo.comment = MoliCodeStringUtils.removeQuot(sqlColumnDefinition.getComment().toString());
                columnVo.columnName = MoliCodeStringUtils.removeQuot(sqlColumnDefinition.getName().simpleName);
                columnVo.isPK = sqlColumnDefinition.isAutoIncrement();
                columnVo.canBeNull = !sqlColumnDefinition.containsNotNullConstaint();
                columnVo.dataName = columnVo.columnName;
                if (camelNameConvert) {
                    columnVo.dataName = tableNameUtil.convertToBeanNames(columnVo.columnName);
                }

                SQLDataType sqlDataType = sqlColumnDefinition.getDataType();
                columnVo.columnType = sqlDataType.getName();
                int length = getColumnLength(sqlDataType)
                columnVo.length = length;
                columnVo.jspTag = tableNameUtil.getJspTag(columnVo.columnType);
                if (StringUtils.isEmpty(columnVo.comment)) {
                    columnVo.comment = columnVo.columnName;
                }
                columnVo.cnname = columnVo.comment;
                columnVoList.add(columnVo);
            } else if (sqlTableElement instanceof SQLPrimaryKey) {
                SQLPrimaryKey sqlPrimaryKey = (SQLPrimaryKey) sqlTableElement;
                for (SQLSelectOrderByItem item : sqlPrimaryKey.getColumns()) {
                    primaryKeyList.add(MoliCodeStringUtils.removeQuot(item.getExpr().toString()));
                }
            } else {
                LogHelper.DEFAULT.info("other class:" + sqlTableElement.getClass());
            }
        }
        //如果pk字段不为空，设置pk字段信息
        if (CollectionUtils.isNotEmpty(primaryKeyList)) {
            for (ColumnVo columnVo : columnVoList) {
                if (primaryKeyList.contains(columnVo.getColumnName())) {
                    columnVo.isPK = true;
                }
            }
        }
        return tableModelVo;
    }

    /**
     * 获取字段长度
     *
     * @param sqlDataType
     * @return
     */
    private int getColumnLength(SQLDataType sqlDataType) {
        List<SQLExpr> exprList = sqlDataType.getArguments();
        int length = 0;
        for (SQLExpr sqlExpr : exprList) {
            if (sqlExpr instanceof SQLIntegerExpr) {
                Number number = ((SQLIntegerExpr) sqlExpr).getNumber();
                Integer integer = number.toInteger();
                length += integer;
            }
        }
        length
    }
}
