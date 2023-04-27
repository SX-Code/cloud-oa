import { http } from '@/utils/http/axios';
import { ContentTypeEnum } from '@/enums/httpEnum';

const prefix = '/admin/process/processTemplate';

/**
 * @description: 获取审批模板列表-分页
 */
export function pageProcessTemplate({ page, pageSize }) {
  return http.request({
    url: `${prefix}/${page}/${pageSize}`,
    method: 'GET',
  });
}

/**
 * @description: 获取审批模板
 */
export function getProcessTemplate(id) {
  return http.request({
    url: `${prefix}/${id}`,
    method: 'GET',
  });
}

/**
 * @description: 新增审批模板
 */
export function addProcessTemplate(data) {
  return http.request(
    {
      url: `${prefix}`,
      method: 'POST',
      data,
    },
    {
      successMessageText: '添加成功',
    }
  );
}

/**
 * @description: 更新审批模板
 */
export function updateProcessTemplate(data) {
  return http.request(
    {
      url: `${prefix}`,
      method: 'PUT',
      data,
    },
    {
      successMessageText: '更改成功',
    }
  );
}

/**
 * @description: 删除审批模板
 */
export function removeProcessTemplate(id) {
  return http.request(
    {
      url: `${prefix}/${id}`,
      method: 'DELETE',
    },
    {
      successMessageText: '删除成功',
    }
  );
}

/**
 * @description: 上传Activiti流程定义文件
 */
export function uplodProcessDefinition(formData, opt) {
  return http.request(
    {
      url: `${prefix}/upload/def`,
      method: 'POST',
      params: formData,
      headers: { 'Content-Type': ContentTypeEnum.FORM_DATA },
    },
    opt,
    {
      successMessageText: '上传成功',
    }
  );
}
/**
 * @description: 删除Activiti流程定义文件
 */
export function deleteProcessDefinition(filename) {
  return http.request(
    {
      url: `${prefix}/delete/def`,
      method: 'DELETE',
      data: {
        filename,
      },
    },
    {
      successMessageText: '定义文件已删除',
    }
  );
}
