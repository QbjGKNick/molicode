package com.shareyi.molicode.context;

import com.shareyi.molicode.common.constants.MoliCodeConstant;
import com.shareyi.molicode.common.vo.code.AutoMakeVo;
import com.shareyi.molicode.common.vo.code.CustomFieldGroup;
import com.shareyi.molicode.common.vo.code.TableModelVo;
import com.shareyi.molicode.common.vo.page.TableModelPageVo;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 表模型上下文
 *
 * @author david
 * @date 2018/10/7
 */
public class TableModelContext {

    /**
     * 页面入参
     */
    private TableModelPageVo tableModelPageVo;
    /**
     * project 关联的配置文件map
     */
    private Map<String, Map<String, String>> projectConfigMap;
    /**
     * 表模型VO
     */
    private TableModelVo tableModelVo;

    /**
     * 输出目录
     */
    private String outputPath;

    /**
     * 是否只读
     */
    private boolean readonly = false;

    /**
     * 通过tableModel构建
     *
     * @param tableModelVo
     * @return
     */
    public static TableModelContext createByTableModel(TableModelVo tableModelVo) {
        TableModelContext context = new TableModelContext();
        context.tableModelVo = tableModelVo;
        return context;
    }
    /**
     * autoMake对象
     */
    private AutoMakeVo loadAutoMakeConfig;

    public TableModelPageVo getTableModelPageVo() {
        return tableModelPageVo;
    }

    public void setTableModelPageVo(TableModelPageVo tableModelPageVo) {
        this.tableModelPageVo = tableModelPageVo;
    }

    public Map<String, Map<String, String>> getProjectConfigMap() {
        return projectConfigMap;
    }

    public void setProjectConfigMap(Map<String, Map<String, String>> projectConfigMap) {
        this.projectConfigMap = projectConfigMap;
    }

    public TableModelVo getTableModelVo() {
        return tableModelVo;
    }

    public void setTableModelVo(TableModelVo tableModelVo) {
        this.tableModelVo = tableModelVo;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public AutoMakeVo getLoadAutoMakeConfig() {
        return loadAutoMakeConfig;
    }

    public void provideLoadAutoMakeConfig(AutoMakeVo loadAutoMakeConfig) {
        this.loadAutoMakeConfig = loadAutoMakeConfig;
    }

    /**
     * 创建tableModel 上下文
     *
     * @param pageVo
     * @return
     */
    public static TableModelContext create(TableModelPageVo pageVo) {
        TableModelContext context = new TableModelContext();
        context.tableModelPageVo = pageVo;
        return context;
    }


    /**
     * 获取自定义字段分组列表
     *
     * @return
     */
    public List<CustomFieldGroup> fetchCustomFieldGroupList() {
        if (loadAutoMakeConfig == null || CollectionUtils.isEmpty(loadAutoMakeConfig.getCustomFieldGroupList())) {
            return MoliCodeConstant.DEFAULT_CUSTOM_FIELD_GROUP_LIST;
        }
        return loadAutoMakeConfig.getCustomFieldGroupList();
    }
}
