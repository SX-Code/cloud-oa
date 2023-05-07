import { http } from '@/utils/http/axios';

const prefix = '/web/process';

/**
 * @description: 获取审批
 */
export function getProcess() {
  return http.request({
    url: `${prefix}/list`,
    method: 'get',
  });
}

/**
 * @description: 获取审批模版
 */
export function getProcessTemplate(id) {
  return http.request({
    url: `${prefix}/template/${id}`,
    method: 'get',
  });
}

/**
 * @description: 启动流程实例
 */
export function startUp(data) {
  return http.request({
    url: `${prefix}/startUp`,
    method: 'post',
    data,
  });
}

/**
 * @description: 查询待处理任务
 */
export function fetchPending({ page, pageSize }) {
  return http.request({
    url: `${prefix}/pending/${page}/${pageSize}`,
    method: 'get',
  });
}

/**
 * @description: 查询已处理任务
 */
export function fetchProcessed({ page, pageSize }) {
  return http.request({
    url: `${prefix}/processed/${page}/${pageSize}`,
    method: 'get',
  });
}

/**
 * @description: 查询已发起任务
 */
export function fetchStarted({ page, pageSize }) {
  return http.request({
    url: `${prefix}/started/${page}/${pageSize}`,
    method: 'get',
  });
}

/**
 * @description: 查询审批详情
 */
export function showProcess(id) {
  return http.request({
    url: `${prefix}/show/${id}`,
    method: 'get',
  });
}

/**
 * @description: 审批
 */
export function approveProcess(data) {
  return http.request({
    url: `${prefix}/approve`,
    method: 'post',
    data,
  });
}
