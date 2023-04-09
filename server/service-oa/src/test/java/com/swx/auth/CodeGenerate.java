package com.swx.auth;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerate {
    public static void OnWindows() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
//        gc.setOutputDir(projectPath + "/service-oa/src/main/java");
        gc.setAuthor("sw-code");
        gc.setOpen(false); // 是否打开文件资源管理器
        gc.setFileOverride(false); // 是否覆盖
        gc.setServiceName("%sService"); // 去Service的I前缀
        gc.setSwagger2(true); // 实体属性 Swagger2 注解
        gc.setIdType(IdType.ID_WORKER); // 主键策略
        gc.setDateType(DateType.ONLY_DATE); // 定义生成的实体类中日期类型
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql:///guigu-oa?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8&nullCatalogMeansCurrent=true");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("swx852345");
        mpg.setDataSource(dsc);

        // 包配置;

        /**
         * 包配置
         * 简单来讲 就是写绝对路径
         */
        PackageConfig pc = new PackageConfig();
        //  pc.setModuleName("code");
        pc.setParent("com.swx");
        //指定生成文件的绝对路径
        Map<String, String> pathInfo  = new HashMap<>();
        String parentPath = "\\src\\main\\java\\com\\swx";
        String otherPath ="\\service-oa\\src\\main\\java\\com\\swx\\auth";

        pc.setEntity("model.system");
        pc.setMapper("auth.mapper");
        pc.setService("auth.service");
        pc.setServiceImpl("auth.service.impl");
        pc.setController("auth.controller");

        String entityPath = projectPath.concat("\\model").concat(parentPath).concat("\\model\\system");
        String mapper_path = projectPath.concat(otherPath).concat("\\mapper");
        String xml_path = projectPath.concat("\\service-oa").concat("\\src\\main\\resources\\xml");
        String service_path = projectPath.concat(otherPath).concat("\\service");
        String service_impl_path = projectPath.concat(otherPath).concat("\\service\\impl");
        String controller_path = projectPath.concat(otherPath).concat("\\controller");

        pathInfo.put("entity_path",entityPath);
        pathInfo.put("mapper_path",mapper_path);
        pathInfo.put("xml_path",xml_path);
        pathInfo.put("service_path",service_path);
        pathInfo.put("service_impl_path",service_impl_path);
        pathInfo.put("controller_path",controller_path);
        pc.setPathInfo(pathInfo);
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("sys_user, sys_user_role");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setSuperEntityClass("com.swx.model.base.BaseEntity"); // 自定义父类
        strategy.setSuperEntityColumns("id", "create_time", "update_time", "version", "deleted");
        strategy.setLogicDeleteFieldName("deleted");

        // 自动填充
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        List<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategy.setTableFillList(tableFills);
        // 乐观锁
        strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
//        strategy.setTablePrefix("sys_");
        mpg.setStrategy(strategy);

        mpg.execute();
    }

    public static void OnMac() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
//        gc.setOutputDir(projectPath + "/service-oa/src/main/java");
        gc.setAuthor("sw-code");
        gc.setOpen(false); // 是否打开文件资源管理器
        gc.setFileOverride(false); // 是否覆盖
        gc.setServiceName("%sService"); // 去Service的I前缀
        gc.setSwagger2(true); // 实体属性 Swagger2 注解
        gc.setIdType(IdType.ID_WORKER); // 主键策略
        gc.setDateType(DateType.ONLY_DATE); // 定义生成的实体类中日期类型
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql:///guigu-oa?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8&nullCatalogMeansCurrent=true");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("swx852345");
        mpg.setDataSource(dsc);

        // 包配置;

        /**
         * 包配置
         * 简单来讲 就是写绝对路径
         */
        PackageConfig pc = new PackageConfig();
        //  pc.setModuleName("code");
        pc.setParent("com.swx");
        //指定生成文件的绝对路径
        Map<String, String> pathInfo  = new HashMap<>();
        String parentPath = "/src/main/java/com/swx";
        String otherPath ="/service-oa/src/main/java/com/swx/auth";

        pc.setEntity("model.system");
        pc.setMapper("auth.mapper");
        pc.setService("auth.service");
        pc.setServiceImpl("auth.service.impl");
        pc.setController("auth.controller");

        String entityPath = projectPath.concat("/model").concat(parentPath).concat("/model/system");
        String mapper_path = projectPath.concat(otherPath).concat("/mapper");
        String mapper_xml_path = projectPath.concat("/service-oa").concat("/src/main/resources/mapper");
        String service_path = projectPath.concat(otherPath).concat("/service");
        String service_impl_path = projectPath.concat(otherPath).concat("/service/impl");
        String controller_path = projectPath.concat(otherPath).concat("/controller");

        pathInfo.put("entity_path",entityPath);
        pathInfo.put("mapper_path",mapper_path);
        pathInfo.put("xml_path",mapper_xml_path);
        pathInfo.put("service_path",service_path);
        pathInfo.put("service_impl_path",service_impl_path);
        pathInfo.put("controller_path",controller_path);
        pc.setPathInfo(pathInfo);
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("sys_menu", "sys_role_menu");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setSuperEntityClass("com.swx.model.base.BaseEntity"); // 自定义父类
        strategy.setSuperEntityColumns("id", "create_time", "update_time", "version", "deleted");
        strategy.setLogicDeleteFieldName("deleted");

        // 自动填充
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        List<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategy.setTableFillList(tableFills);
        // 乐观锁
        strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
//        strategy.setTablePrefix("sys_");
        mpg.setStrategy(strategy);

        mpg.execute();
    }

    public static void main(String[] args) {
        OnMac();
//        String projectPath = System.getProperty("user.dir");
//        System.out.println(projectPath);
    }
}
