/**
 * Copyright(c) 2004-2018 david
 */


package com.shareyi.molicode.controller.conf;


import com.shareyi.molicode.controller.AbstractController;
import com.shareyi.molicode.domain.conf.CommonExtInfo;
import com.shareyi.molicode.service.conf.CommonExtInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/conf/commonExtInfo")
public class CommonExtInfoController extends AbstractController<CommonExtInfo> {

    @Resource(name = "commonExtInfoService")
    private CommonExtInfoService commonExtInfoService;

    @RequestMapping(value = "save", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map save(CommonExtInfo commonExtInfo) {
        return getService().save(commonExtInfo).getReturnMap();
    }

    @RequestMapping(value = "getByOwnerAndKey", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map getByOwnerAndKey(CommonExtInfo commonExtInfo) {
        return getService().getByOwnerAndKey(commonExtInfo).getReturnMap();
    }


    public CommonExtInfoService getService() {
        return commonExtInfoService;
    }
}
