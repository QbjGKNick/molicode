package com.shareyi.molicode.common.utils;

import com.alibaba.fastjson.JSON;
import com.shareyi.molicode.common.constants.ConfigKeyConstant;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 配置信息工具类
 *
 * @author david
 * @date 2019-12-18
 */
public class ConfigUtil {

    /**
     * 从配置map中，获取jsonConfig
     *
     * @param configMap
     * @return
     */
    public static Map<String, Object> getJsonConfigMap(Map<String, Map<String, String>> configMap) {
        Map<String, Object> jsonConfigMap = null;
        Map<String, String> extConfigMap = configMap.get(ConfigKeyConstant.ExtConfig.CONFIG_KEY);
        String jsonConfigStr = MapUtils.getString(extConfigMap, ConfigKeyConstant.ExtConfig.JSON_KEY);
        if (StringUtils.isNotEmpty(jsonConfigStr)) {
            jsonConfigMap = JSON.parseObject(jsonConfigStr);
        }
        return jsonConfigMap;
    }

}
