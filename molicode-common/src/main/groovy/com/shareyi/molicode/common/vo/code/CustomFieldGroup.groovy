package com.shareyi.molicode.common.vo.code
/**
 * 自定义列
 *
 * @author david* @since 2021-05-31
 */
class CustomFieldGroup implements Serializable {

    /**
     * 组key
     */
    String groupKey;
    /**
     * 描述
     */
    String desc;

    /**
     * 配置是否展示
     */
    boolean configShow = true;

    /**
     * 表达式
     */
    String groupExpression;
    /**
     * 默认构建
     */
    CustomFieldGroup() {

    }

    CustomFieldGroup(String groupKey, String desc, boolean configShow, String groupExpression) {
        this.groupKey = groupKey
        this.desc = desc
        this.configShow = configShow
        this.groupExpression = groupExpression
    }

    public static CustomFieldGroup create(String groupKey, String desc, boolean configShow, String groupExpression) {
        return new CustomFieldGroup(groupKey, desc, configShow, groupExpression);

    }
}
