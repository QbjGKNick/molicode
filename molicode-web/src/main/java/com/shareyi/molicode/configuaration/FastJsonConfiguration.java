package com.shareyi.molicode.configuaration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * fastjson 配置器
 *
 * @author david
 * @date 2018/3/27
 */
@Configuration
public class FastJsonConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
         /*
         先把JackSon的消息转换器删除.
         备注: (1)源码分析可知，返回json的过程为:
         Controller调用结束后返回一个数据对象，for循环遍历conventers，找到支持application/json的HttpMessageConverter，然后将返回的数据序列化成json。
         具体参考org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor的writeWithMessageConverters方法
          (2)由于是list结构，我们添加的fastjson在最后。因此必须要将jackson的转换器删除，不然会先匹配上jackson，导致没使用fastjson
        */
        for (int i = converters.size() - 1; i >= 0; i--) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                converters.remove(i);
            }
        }

        FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);
/*        SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
        ObjectSerializer dateTimeSerializer = (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            Class<?> clazz = object.getClass();
            if (clazz == java.sql.Date.class) {
                long millis = ((java.sql.Date) object).getTime();
                out.writeString(String.valueOf(millis));
                return;
            }
            if (clazz == Date.class) {
                long millis = ((Date) object).getTime();
                out.writeString(String.valueOf(millis));
                return;
            }
            if (clazz == Timestamp.class) {
                long millis = ((Timestamp) object).getTime();
                out.writeString(String.valueOf(millis));
                return;
            }
        };
        serializeConfig.put(Date.class, dateTimeSerializer);
        serializeConfig.put(java.sql.Date.class, dateTimeSerializer);
        serializeConfig.put(java.sql.Timestamp.class, dateTimeSerializer);
        fastJsonConfig.setSerializeConfig(serializeConfig);*/

        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        messageConverter.setSupportedMediaTypes(fastMediaTypes);
        messageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(messageConverter);
    }
}
