package com.swx.auth.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProcessTestGateway {

    @Resource
    RepositoryService repositoryService;

    @Resource
    RuntimeService runtimeService;

    @Resource
    TaskService taskService;

    // 部署流程定义
    @Test
    public void deployProcess() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/complete_leave.bpmn20.xml")
                .name("完整请假流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    // 启动流程实例
    @Test
    public void startProcessInstance() {
        // 设置请假天数
        HashMap<String, Object> variable = new HashMap<>();
        variable.put("day", 2);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("complete_leave", variable);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    // 查询任务
    @Test
    public void queryTaskList() {
        String assign = "Jesse";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("流程实例ID：" + task.getProcessInstanceId());
            System.out.println("任务ID：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    // 完成任务
    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("Jesse")
                .singleResult();
        taskService.complete(task.getId());
    }
}
