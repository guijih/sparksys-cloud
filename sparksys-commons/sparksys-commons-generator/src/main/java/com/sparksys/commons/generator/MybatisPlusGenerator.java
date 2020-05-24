package com.sparksys.commons.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Scanner;

/**
 * description: 代码生成类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:20:58
 */
public class MybatisPlusGenerator {

    /**
     * 读取控制台内容
     *
     * @param tip
     * @return String[]
     * @throws
     * @Author zhouxinlei
     * @Date 2020-04-12 14:06:32
     */
    public static String[] scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (!StringUtils.isEmpty(ipt)) {
                return ipt.split(",");
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        /*gc.setOutputDir(projectPath + "/sparksys-business/sparksys-problem/src/main/java");*/
        gc.setOutputDir(projectPath + "/sparksys-business/sparksys-authorization/src/main/java");
        gc.setBaseColumnList(true);
        gc.setBaseResultMap(true);
        gc.setFileOverride(true);
        gc.setSwagger2(true);
        gc.setDateType(DateType.TIME_PACK);
        gc.setIdType(IdType.ASSIGN_ID);
        gc.setAuthor("zhouxinlei");
        gc.setOpen(false);
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:p6spy:mysql://47.100.79.211:3340/sparksys-admin");
        dsc.setDriverName("com.p6spy.engine.spy.P6SpyDriver");
        dsc.setUsername("root");
        dsc.setPassword("123456");

        /*dsc.setUrl("jdbc:p6spy:mysql://47.116.52.58:3306/hongneng_problem");
        dsc.setDriverName("com.p6spy.engine.spy.P6SpyDriver");
        dsc.setUsername("root");
        dsc.setPassword("sparksys.2020");*/

        /*dsc.setUrl("jdbc:p6spy:sqlserver://47.96.148.189:1433;DatabaseName=sparksysLTCTest");
        dsc.setDriverName("com.p6spy.engine.spy.P6SpyDriver");
        dsc.setUsername("test");
        dsc.setPassword("sparksysltc");*/

        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.sparksys.authorization");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("mapper");
        pc.setEntity("entity");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        mpg.setTemplate(templateConfig);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntitySerialVersionUID(true);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        String[] tableNames = scanner("表名，多个英文逗号分割");
        System.out.println(Arrays.toString(tableNames));
        strategy.setInclude(tableNames);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}

