package com.shareyi.molicode.hander.gencode.main;

import com.shareyi.molicode.common.chain.HandlerChainFactoryImpl;
import com.shareyi.molicode.common.chain.handler.SimpleHandler;
import com.shareyi.molicode.common.chain.handler.awares.CodeGenMainHandlerAware;
import com.shareyi.molicode.common.chain.handler.awares.DataLoadHandlerAware;
import com.shareyi.molicode.common.context.MoliCodeContext;
import org.springframework.stereotype.Service;

/**
 * 数据加载处理器
 *
 * @author david
 * @date 2018/10/3
 */
@Service
public class DataLoadHandler extends SimpleHandler<MoliCodeContext> implements CodeGenMainHandlerAware {

    @Override
    public boolean shouldHandle(MoliCodeContext context) {
        return true;
    }

    @Override
    public void doHandle(MoliCodeContext context) {
        //加载数据, 子责任链
        HandlerChainFactoryImpl.executeByHandlerAware(DataLoadHandlerAware.class, context);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
