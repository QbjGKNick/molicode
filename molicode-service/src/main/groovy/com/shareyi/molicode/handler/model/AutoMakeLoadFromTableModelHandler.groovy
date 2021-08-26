package com.shareyi.molicode.handler.model

import com.google.common.collect.Lists
import com.shareyi.molicode.common.chain.HandlerChainExecutor
import com.shareyi.molicode.common.chain.handler.SimpleHandler
import com.shareyi.molicode.common.chain.handler.awares.TableModelHandlerAware
import com.shareyi.molicode.common.constants.CommonConstant
import com.shareyi.molicode.common.constants.MoliCodeConstant
import com.shareyi.molicode.common.context.MoliCodeContext
import com.shareyi.molicode.common.vo.code.AutoCodeParams
import com.shareyi.molicode.common.vo.code.AutoMakeVo
import com.shareyi.molicode.common.vo.page.TableModelPageVo
import com.shareyi.molicode.context.TableModelContext
import com.shareyi.molicode.hander.gencode.loader.AutoMakeLoadHandler
import com.shareyi.molicode.service.conf.AcConfigService
import org.springframework.stereotype.Service

import javax.annotation.Resource

/**
 * tableModel生成的前置参数验证器
 * 及配置文件读取
 * @author david* @since 2018/10/7
 */
@Service
class AutoMakeLoadFromTableModelHandler extends SimpleHandler<TableModelContext> implements TableModelHandlerAware {

    @Resource
    AcConfigService acConfigService;

    @Resource
    AutoMakeLoadHandler autoMakeLoadHandler;

    @Override
    int getOrder() {
        return 2;
    }

    @Override
    boolean shouldHandle(TableModelContext tableModelContext) {
        //如果外部传入了tableModel，就不需要查库了，一般是通过SQL解析得来的
        return true;
    }

    @Override
    void doHandle(TableModelContext tableModelContext) {
        TableModelPageVo tableModelPageVo = tableModelContext.getTableModelPageVo();
        AutoCodeParams autoCodeParams = new AutoCodeParams();
        autoCodeParams.projectKey = tableModelPageVo.projectKey;
        autoCodeParams.flushMaven = CommonConstant.STD_YN_NO_STR
        MoliCodeContext moliCodeContext = MoliCodeContext.create(autoCodeParams)
        HandlerChainExecutor.execute(moliCodeContext, Lists.newArrayList(autoMakeLoadHandler));
        AutoMakeVo autoMakeVo = (AutoMakeVo) moliCodeContext.get(MoliCodeConstant.CTX_KEY_AUTO_MAKE);
        tableModelContext.provideLoadAutoMakeConfig(autoMakeVo);
    }
}
