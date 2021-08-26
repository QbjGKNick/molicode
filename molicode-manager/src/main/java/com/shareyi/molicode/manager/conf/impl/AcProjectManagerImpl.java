/**
 * Copyright(c) 2004-2018 david
 */

package com.shareyi.molicode.manager.conf.impl;

import com.shareyi.molicode.builder.impl.AcProjectBuilder;
import com.shareyi.molicode.common.enums.columns.AcProjectColumn;
import com.shareyi.molicode.dao.conf.AcProjectDao;
import com.shareyi.molicode.domain.conf.AcProject;
import com.shareyi.molicode.manager.AbstractManager;
import com.shareyi.molicode.manager.conf.AcProjectManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目Manager实现类
 *
 * @author david
 * @date 2018-08-22
 */
@Component("acProjectManager")
public class AcProjectManagerImpl extends AbstractManager<AcProject> implements AcProjectManager {

    @Resource(name = "acProjectBuilder")
    AcProjectBuilder acProjectBuilder;
    @Resource
    private AcProjectDao acProjectDao;

    @Override
    public AcProject getByProjectKey(String projectKey) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(AcProjectColumn.projectKey.name(), projectKey);
        List<AcProject> list = acProjectDao.getListByExample(queryParam);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /******* getter and setter ***/
    public AcProjectBuilder getBuilder() {
        return acProjectBuilder;
    }

    public AcProjectDao getDao() {
        return acProjectDao;
    }

}
