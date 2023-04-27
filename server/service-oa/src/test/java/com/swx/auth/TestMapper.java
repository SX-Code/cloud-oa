package com.swx.auth;

import com.swx.ServiceAuthApplication;
import com.swx.auth.mapper.SysRoleMapper;
import com.swx.model.system.SysRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = ServiceAuthApplication.class)
@RunWith(SpringRunner.class)
public class TestMapper {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Test
    public void getAll() {
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        System.out.println(sysRoles);
    }

}
