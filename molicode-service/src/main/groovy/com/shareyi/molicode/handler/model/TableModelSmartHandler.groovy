package com.shareyi.molicode.handler.model

import com.shareyi.molicode.common.chain.handler.SimpleHandler
import com.shareyi.molicode.common.chain.handler.awares.TableModelHandlerAware
import com.shareyi.molicode.common.constants.MoliCodeConstant
import com.shareyi.molicode.common.filter.ColumnFilter
import com.shareyi.molicode.common.filter.impl.NameExpressionFilter
import com.shareyi.molicode.common.utils.PubUtils
import com.shareyi.molicode.common.vo.code.ColumnVo
import com.shareyi.molicode.common.vo.code.CustomFieldGroup
import com.shareyi.molicode.common.vo.code.TableDefineVo
import com.shareyi.molicode.common.vo.code.TableModelVo
import com.shareyi.molicode.common.vo.page.TableModelPageVo
import com.shareyi.molicode.context.TableModelContext
import com.shareyi.molicode.service.gencode.impl.DictColumnProcessor
import org.springframework.stereotype.Service

import javax.annotation.Resource

/**
 * tableModel 数据库处理器
 *
 * @author david* @since 2018/10/7
 */
@Service
class TableModelSmartHandler extends SimpleHandler<TableModelContext> implements TableModelHandlerAware {

    @Resource
    DictColumnProcessor dictColumnProcessor;

    @Override
    int getOrder() {
        return 5;
    }

    @Override
    boolean shouldHandle(TableModelContext tableModelContext) {
        return !tableModelContext.isReadonly();
    }

    @Override
    void doHandle(TableModelContext tableModelContext) {
        TableModelVo tableModelVo = tableModelContext.tableModelVo;
        TableDefineVo tableDefineVo = tableModelVo.tableDefine;
        TableModelPageVo tableModelPageVo = tableModelContext.tableModelPageVo;
        List<CustomFieldGroup> customFieldGroupList = tableModelContext.fetchCustomFieldGroupList();

        //对字典项字段进行预处理，分解出字典项数据
        dictColumnProcessor.process(tableModelVo, tableDefineVo.columns);

        //不启用
        if (tableModelPageVo.smartFlag == null || tableModelPageVo.smartFlag == 2) {
            def columnNames = PubUtils.joinColumnNames(tableDefineVo.columns)
            tableModelVo.putBizFields(MoliCodeConstant.BIZ_FIELDS_KEY_QUERYLIST, columnNames);
            tableModelVo.putBizFields(MoliCodeConstant.BIZ_FIELDS_KEY_ADDLIST, columnNames);
            tableModelVo.putBizFields(MoliCodeConstant.BIZ_FIELDS_KEY_UPDATELIST, columnNames);
            tableModelVo.putBizFields(MoliCodeConstant.BIZ_FIELDS_KEY_VIEWLIST, columnNames);
            tableModelVo.putBizFields(MoliCodeConstant.BIZ_FIELDS_KEY_SEARCHKEY, "");
            tableModelVo.putBizFields(MoliCodeConstant.BIZ_FIELDS_KEY_UPDATETIME, "");
            tableModelVo.putBizFields(MoliCodeConstant.BIZ_FIELDS_KEY_CREATETIME, "");
        } else {
            customFieldGroupList.each { customFieldGroup ->
                ColumnFilter filter = new NameExpressionFilter(customFieldGroup.groupExpression);
                List<ColumnVo> bizColumnList = filter.filterColumns(tableDefineVo.columns)
                tableModelVo.putBizFields(customFieldGroup.groupKey, PubUtils.joinColumnNames(bizColumnList));
            }
        }
        tableModelVo.putBizFields(MoliCodeConstant.BIZ_FIELDS_KEY_ALLCOLUMN, PubUtils.joinColumnNames(tableDefineVo.columns));
    }
}
