package com.swx.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.swx.model.process.ProcessTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.vo.system.page.CustomPage;

/**
 * <p>
 * 审批模板 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
public interface ProcessTemplateService extends IService<ProcessTemplate> {

    /**
     * 分页查询审批模板，并且查出审批类型
     * @param pageParam 分页信息
     */
    IPage<ProcessTemplate> pageWithTypeName(CustomPage<ProcessTemplate> pageParam);
}
