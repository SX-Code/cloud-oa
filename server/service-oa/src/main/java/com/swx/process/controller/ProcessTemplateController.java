package com.swx.process.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.swx.common.annotation.ResponseResult;
import com.swx.common.pojo.BizException;
import com.swx.model.process.ProcessTemplate;
import com.swx.process.service.ProcessTemplateService;
import com.swx.vo.system.page.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 审批模板 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Api(value = "审批模版", tags = "审批模版")
@RestController
@ResponseResult
@RequestMapping("/admin/process/processTemplate")
public class ProcessTemplateController {

    private final ProcessTemplateService processTemplateService;

    public ProcessTemplateController(ProcessTemplateService processTemplateService) {
        this.processTemplateService = processTemplateService;
    }

    // 分页查询审批模版
    @PreAuthorize("hasAuthority('process_template_list')")
    @ApiOperation("分页查询审批模版")
    @GetMapping("{page}/{limit}")
    public IPage<ProcessTemplate> page(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit
    ) {
        CustomPage<ProcessTemplate> pageParam = new CustomPage<>(page, limit);
        return processTemplateService.pageWithTypeName(pageParam);
    }

    // 查询审批模版
    @PreAuthorize("hasAuthority('process_template_list')")
    @ApiOperation("查询审批模版")
    @GetMapping("{id}")
    public ProcessTemplate get(@PathVariable Long id) {
        return processTemplateService.getById(id);
    }

    @PreAuthorize("hasAuthority('process_template_set')")
    @ApiOperation("新增审批模版")
    @PostMapping("")
    public void add(@RequestBody ProcessTemplate processTemplate) {
        boolean save = processTemplateService.save(processTemplate);
        if (!save) {
            throw new BizException("审批模版新增失败");
        }
    }

    @PreAuthorize("hasAuthority('process_template_set')")
    @ApiOperation("修改审批模版")
    @PutMapping("")
    public void update(@RequestBody ProcessTemplate processTemplate) {
        boolean update = processTemplateService.updateById(processTemplate);
        if (!update) {
            throw new BizException("审批模版修改失败");
        }
    }

    @PreAuthorize("hasAuthority('process_template_remove')")
    @ApiOperation("删除审批模版")
    @DeleteMapping("{id}")
    public void remove(@PathVariable Long id) {
        boolean remove = processTemplateService.removeById(id);
        if (!remove) {
            throw new BizException("审批模版删除失败");
        }
    }

    @ApiOperation("上传流程定义")
    @PostMapping("/upload/def")
    public Map<String, Object> uploadProcessDefinition(@RequestPart("file") MultipartFile file) throws FileNotFoundException {
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        String uuidFilename = "activiti_" + UUID.randomUUID().toString();
        // 上传目录
        File tempFile = new File(path + "/processes/");
        // 判断目录是否存在
        if (!tempFile.exists()) {
            tempFile.mkdirs(); // 创建目录
        }
        // 创建空文件用于写入文件
        File zipFile = new File(path + "/processes/" + uuidFilename + suffix);
        // 保存文件流到本地
        try {
            file.transferTo(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("文件保存失败");
        }

        Map<String, Object> map = new HashMap<>();
        // 根据上传地址后续部署流程定义，文件名为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + uuidFilename + suffix);
        map.put("processDefinitionKey", uuidFilename);
        return map;
    }

    @ApiOperation("删除流程定义")
    @DeleteMapping("/delete/def")
    public void deleteProcessDefinition(@RequestBody String filenameObj) throws FileNotFoundException {
        String filename = JSON.parseObject(filenameObj).getString("filename");
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        File zipFile = new File(path + "/" + filename);
        if (zipFile.isFile() && zipFile.exists()) {
            if (!zipFile.delete()) {
                throw new BizException("文件删除失败");
            }
        } else {
            throw new BizException("文件不存在");
        }
    }

}

