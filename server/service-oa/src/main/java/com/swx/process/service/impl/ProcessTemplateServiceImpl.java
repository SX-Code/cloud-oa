package com.swx.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.swx.common.pojo.BizException;
import com.swx.model.process.ProcessTemplate;
import com.swx.model.process.ProcessType;
import com.swx.process.mapper.ProcessTemplateMapper;
import com.swx.process.service.ProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.process.service.ProcessTypeService;
import com.swx.vo.system.page.CustomPage;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate> implements ProcessTemplateService {

    final
    private ProcessTypeService processTypeService;

    private final RepositoryService repositoryService;

    public ProcessTemplateServiceImpl(ProcessTypeService processTypeService, RepositoryService repositoryService) {
        this.processTypeService = processTypeService;
        this.repositoryService = repositoryService;
    }

    /**
     * 分页查询审批模板，并且查出审批类型
     *
     * @param pageParam 分页信息
     */
    @Override
    public IPage<ProcessTemplate> pageWithTypeName(CustomPage<ProcessTemplate> pageParam) {
        CustomPage<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, null);
        List<ProcessTemplate> templates = processTemplatePage.getRecords();
        // 过滤出类型ID
        List<Long> ids = templates.stream().map(ProcessTemplate::getProcessTypeId).collect(Collectors.toList());
        // 查询类型，以id为key，name为value创建map，方便后续填充
        List<ProcessType> processTypes = processTypeService.listByIds(ids);
        Map<Long, String> idNameMap = processTypes.stream().collect(Collectors.toMap(ProcessType::getId, ProcessType::getName));
        // 填充类型名称
        templates.forEach(template -> {
            template.setProcessTypeName(idNameMap.get(template.getProcessTypeId()));
        });
        return processTemplatePage;
    }

    /**
     * 模板发布
     *
     * @param id 模板ID
     */
    @Override
    public void publish(Long id) {
        // 修改状态
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        // 流程部署
        String path = processTemplate.getProcessDefinitionPath();
        if (StringUtils.isEmpty(path)) {
            throw new BizException("文件路径为空");
        }
        deployByZip(path);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);
    }

    /**
     * 部署流程定义
     */
    private void deployByZip(String path) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
    }
}
