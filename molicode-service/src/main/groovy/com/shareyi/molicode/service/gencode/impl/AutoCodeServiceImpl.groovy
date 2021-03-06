package com.shareyi.molicode.service.gencode.impl

import com.google.common.collect.Lists
import com.shareyi.molicode.common.bean.LoginContext
import com.shareyi.molicode.common.chain.HandlerChainExecutor
import com.shareyi.molicode.common.chain.HandlerChainFactoryImpl
import com.shareyi.molicode.common.chain.handler.awares.CodeGenMainHandlerAware
import com.shareyi.molicode.common.constants.MoliCodeConstant
import com.shareyi.molicode.common.context.MoliCodeContext
import com.shareyi.molicode.common.enums.EnumCode
import com.shareyi.molicode.common.enums.OutputTypeEnum
import com.shareyi.molicode.common.enums.ResourceTypeEnum
import com.shareyi.molicode.common.utils.*
import com.shareyi.molicode.common.valid.Validate
import com.shareyi.molicode.common.vo.code.AutoCodeParams
import com.shareyi.molicode.common.vo.code.AutoMakeVo
import com.shareyi.molicode.common.web.CommonResult
import com.shareyi.molicode.hander.gencode.loader.AutoMakeLoadHandler
import com.shareyi.molicode.helper.LoginHelper
import com.shareyi.molicode.service.conf.AcConfigService
import com.shareyi.molicode.service.conf.CommonExtInfoService
import com.shareyi.molicode.service.gencode.AutoCodeService
import com.shareyi.molicode.service.gencode.AutoMakeService
import com.shareyi.molicode.service.maven.MavenService
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service("autoCodeService")
class AutoCodeServiceImpl implements AutoCodeService {

    @Resource
    AcConfigService acConfigService

    @Resource
    MavenService mavenService;

    @Resource
    CommonExtInfoService commonExtInfoService;

    @Resource
    AutoMakeService autoMakeService;

    @Resource
    AutoMakeLoadHandler autoMakeLoadHandler;


    @Override
    CommonResult<String> generateCode(AutoCodeParams autoMakeParams) {
        CommonResult result = CommonResult.create();
        long startTime = System.currentTimeMillis();
        LoginContext loginContext = LoginHelper.getLoginContext();
        try {
            LogHelper.DEFAULT.info("{}?????????????????????param={}", loginContext.getUserName(), autoMakeParams)
            autoMakeParams.setLoadTemplateContent(true)
            ResourceTypeEnum resourceTypeEnum = EnumCode.Parser.parseToNullSafe(ResourceTypeEnum.class, autoMakeParams.resourceType, ResourceTypeEnum.FILE);
            if (resourceTypeEnum == ResourceTypeEnum.FILE || resourceTypeEnum == ResourceTypeEnum.DATABASE) {
                Validate.notEmpty(autoMakeParams.getTableModelPath(), "tableModelPath????????????")
            } else {
                Validate.notEmpty(autoMakeParams.getFrontContent(), "frontContent????????????")
            }

            //headless????????????token???????????????????????????????????
            if (Profiles.getInstance().isHeadLess() && !loginContext.isByToken()) {
                autoMakeParams.setFrontTemplate(null);
            }

            OutputTypeEnum outputTypeEnum = EnumCode.Parser.parseTo(OutputTypeEnum.class, autoMakeParams.getOutputType());

            //headless?????????????????????zip??????
            if (Profiles.getInstance().isHeadLess() && outputTypeEnum == OutputTypeEnum.LOCAL_DIR) {
                outputTypeEnum = OutputTypeEnum.ZIP_FILE;
                autoMakeParams.setOutputType(OutputTypeEnum.ZIP_FILE.getCode());
            }

            //?????????headless???????????????????????????????????????????????????????????????
            if (outputTypeEnum == OutputTypeEnum.ZIP_FILE ||
                    outputTypeEnum == OutputTypeEnum.RESPONSE) {
                String projectOutputDir = SystemFileUtils.buildDefaultProjectOutputDir(autoMakeParams.getProjectKey());
                def outputDir = MoliCodeStringUtils.getTimeBasedStr()
                projectOutputDir = FileIoUtil.contactPath(projectOutputDir, outputDir);
                autoMakeParams.setProjectOutputDir(projectOutputDir);
                autoMakeParams.outputDir = outputDir;
            }
            if (outputTypeEnum.needOutputDir) {
                Validate.notEmpty(autoMakeParams.getProjectOutputDir(), "??????????????????????????????")
            }
            MoliCodeContext moliCodeContext = MoliCodeContext.create(autoMakeParams)
            ThreadLocalHolder.setMoliCodeContext(moliCodeContext)
            HandlerChainFactoryImpl.executeByHandlerAware(CodeGenMainHandlerAware.class, moliCodeContext)

            result.addModel(MoliCodeConstant.CTX_KEY_ZIP_FILE_NAME, moliCodeContext.get(MoliCodeConstant.CTX_KEY_ZIP_FILE_NAME));
            result.addModel(MoliCodeConstant.TEMPLATE_RESULT_LIST, moliCodeContext.get(MoliCodeConstant.TEMPLATE_RESULT_LIST));
            result.addModel(MoliCodeConstant.OUTPUT_DIR, autoMakeParams.outputDir);
            result.setResultInfo(true, moliCodeContext.getMessage().toString());
        } catch (Exception e) {
            LogHelper.FRONT_CONSOLE.error("?????????????????????data={}", autoMakeParams, e);
            result.failed("?????????????????????????????????" + e.getMessage());
        } finally {
            ThreadLocalHolder.removeMoliCodeContext();
        }
        long endTime = System.currentTimeMillis();
        def costTime = endTime - startTime;
        result.addModel(MoliCodeConstant.ResultInfo.START_TIME_KEY, startTime);
        result.addModel(MoliCodeConstant.ResultInfo.COST_TIME_KEY, costTime);
        return result
    }
/**
 * ??????autoMake??????
 * @param autoXmlPath
 * @param templateBaseDir
 * @return
 */
    CommonResult<AutoMakeVo> getAutoMake(AutoCodeParams autoCodeParams) {
        CommonResult result = CommonResult.create();
        try {
            MoliCodeContext moliCodeContext = MoliCodeContext.create(autoCodeParams)
            HandlerChainExecutor.execute(moliCodeContext, Lists.newArrayList(autoMakeLoadHandler));
            AutoMakeVo autoMakeVo = (AutoMakeVo) moliCodeContext.get(MoliCodeConstant.CTX_KEY_AUTO_MAKE);
            result.addDefaultModel("autoMake", autoMakeVo);
            result.succeed();
        } catch (Exception e) {
            LogHelper.EXCEPTION.error("?????????????????????????????????autoMakeParams={}", autoCodeParams, e);
            result.failed("??????????????????????????????,????????????" + e.getMessage());
        }
        return result;
    }

}
