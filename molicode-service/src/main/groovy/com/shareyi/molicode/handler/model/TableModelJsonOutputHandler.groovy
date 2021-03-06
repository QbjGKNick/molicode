package com.shareyi.molicode.handler.model

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.shareyi.molicode.common.chain.handler.SimpleHandler
import com.shareyi.molicode.common.chain.handler.awares.TableModelHandlerAware
import com.shareyi.molicode.common.constants.CommonConstant
import com.shareyi.molicode.common.utils.FileIoUtil
import com.shareyi.molicode.common.utils.Profiles
import com.shareyi.molicode.common.utils.PubUtils
import com.shareyi.molicode.common.vo.code.ColumnVo
import com.shareyi.molicode.common.vo.code.TableDefineVo
import com.shareyi.molicode.common.vo.code.TableModelVo
import com.shareyi.molicode.common.vo.page.TableModelPageVo
import com.shareyi.molicode.context.TableModelContext
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

/**
 * tableModel 输出为json文件处理器
 *
 * @author david* @since 2018/10/7
 */
@Service
class TableModelJsonOutputHandler extends SimpleHandler<TableModelContext> implements TableModelHandlerAware {
    @Override
    int getOrder() {
        return 7;
    }

    @Override
    boolean shouldHandle(TableModelContext tableModelContext) {
        return Objects.equals(tableModelContext.tableModelPageVo.modelType, CommonConstant.MODEL_TYPE_JSON) &&
                !tableModelContext.isReadonly();
    }

    @Override
    void doHandle(TableModelContext tableModelContext) {
        TableModelVo tableModelVo = tableModelContext.tableModelVo;
        TableDefineVo tableDefineVo = tableModelVo.tableDefine;
        TableModelPageVo tableModelPageVo = tableModelContext.tableModelPageVo;
        File f = new File(FileIoUtil.contactPath(tableModelPageVo.tableModelDir, tableDefineVo.dbTableName?.trim() + ".json"));

        //如果文件已经存在，需要读取，并将已配置的信息配置回来
        if (f.exists()) {
            loadFromPreConfigInfo(f, tableModelVo);
        }


        FileIoUtil.makeSureFileExist(f);
        tableModelVo.clearCache(); //必须要清空
        //换行符号替换为Windows的换行符号
        f.withWriter("utf-8")
                { writer ->
                    writer.write JSON.toJSONString(tableModelVo, SerializerFeature.PrettyFormat, SerializerFeature.QuoteFieldNames, SerializerFeature.UseSingleQuotes, SerializerFeature.DisableCircularReferenceDetect);
                }

        print "table " + tableModelPageVo.tableName + "的json文件成功生成，在：" + f.absolutePath;
        tableModelContext.setOutputPath(f.absolutePath)
    }

    /**
     * 从之前的配置信息中获取配置信息
     *
     * @param file
     * @param tableModelVo
     */
    void loadFromPreConfigInfo(File file, TableModelVo tableModelVo) {
        String preConfig = FileUtils.readFileToString(file, Profiles.instance.fileEncoding);
        if (StringUtils.isBlank(preConfig)) {
            return;
        }
        List<String> allColumnList = Lists.newArrayList();
        Set<String> columnSet = Sets.newHashSet()
        tableModelVo.tableDefine.columns.each {
            column ->
                columnSet.add(column.getColumnName())
                allColumnList.add(column.getColumnName())
        }
        TableModelVo preTableModelVo = JSON.parseObject(preConfig, TableModelVo.class);
        preTableModelVo.clearCache(); //必须要清空

        tableModelVo.orderColumns = preTableModelVo.orderColumns;
        /* if (MapUtils.isNotEmpty(preTableModelVo.dictMap)) {
             if (tableModelVo.dictMap == null) {
                 tableModelVo.dictMap = Maps.newHashMap();
             }
             tableModelVo.dictMap.putAll(preTableModelVo.dictMap);
         }*/
        for (Map.Entry<String, String> entry : preTableModelVo.bizFieldsMap) {
            String key = entry.getKey();
            if (Objects.equals(key, "allColumn")) {
                continue;
            } else {
                String columnNames = this.filterColumn(entry.getValue(), columnSet)
                if (StringUtils.isNotEmpty(columnNames)) {
                    tableModelVo.bizFieldsMap.put(entry.key, columnNames);
                }
            }
        }
        tableModelVo.tableDefine.id = preTableModelVo.tableDefine.id;
        tableModelVo.tableDefine.cnname = preTableModelVo.tableDefine.cnname;
        tableModelVo.tableDefine.pageSize = preTableModelVo.tableDefine.pageSize;
        tableModelVo.tableDefine.isPaged = preTableModelVo.tableDefine.isPaged;
        tableModelVo.customProps = preTableModelVo.customProps;

        preTableModelVo.tableDefine.columns.each { preColumn ->
            ColumnVo columnVo = tableModelVo.tableDefine.getColumnByColumnName(preColumn.columnName);
            if (columnVo != null) {
                columnVo.setCnname(preColumn.cnname)
                columnVo.setCanBeNull(preColumn.canBeNull)
                columnVo.setJspTag(preColumn.jspTag)
                columnVo.setReadonly(preColumn.readonly)
                columnVo.setCanBeNull(preColumn.canBeNull)
                columnVo.setCustomProps(preColumn.customProps)
                columnVo.setIsPK(preColumn.isPK)
            }
        }
    }

    /**
     * 过滤字段，必须在新的数据库也存在
     * @param columnNames
     * @param allColumnSet
     * @return
     */
    String filterColumn(String columnNames, HashSet<String> allColumnSet) {
        List<String> columnNameList = PubUtils.stringToList(columnNames);
        List<String> filteredList = Lists.newArrayList();
        columnNameList.each { name ->
            if (allColumnSet.contains(name)) {
                filteredList.add(name);
            }
        }
        return StringUtils.join(filteredList, ",");
    }
}
