package com.feiqu.generator.util;

import com.feiqu.common.config.Global;
import com.google.common.collect.Maps;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cwd
 * @create 2017-09-17:58
 * 生成 controller service serviceImpl
 **/
public class CSSGenerator {

    // Service模板路径
    private static String service_vm = "/template1/Service.vm";
    // ServiceImpl模板路径
    private static String serviceImpl_vm = "/template1/ServiceImpl.vm";
    private static String controller_vm = "/template1/Controller.vm";
    // generatorConfig模板路径
    private static String generatorConfig_vm = "/template1/generatorConfig.vm";
    private static String page_index_vm = "/template1/html_index.vm";
    private static String page_detail_vm = "/template1/html_detail.vm";

    /**
     *
     * @param package_name 包名称
     * @param includeService 是否包含service
     * @param lastInsertIdTables
     * @param includeHtml 是否包含页面生成
     */
    public static void generator(String package_name,
                                 Boolean includeService,
                                 Map<String, String> lastInsertIdTables,
                                 Boolean includeHtml,
                                 String onlyTable) {
        try {
            VelocityEngine ve = new VelocityEngine();
            service_vm = CSSGenerator.class.getResource(service_vm).getPath().replaceFirst("/", "");
            serviceImpl_vm = CSSGenerator.class.getResource(serviceImpl_vm).getPath().replaceFirst("/", "");
            controller_vm = CSSGenerator.class.getResource(controller_vm).getPath().replaceFirst("/", "");
            generatorConfig_vm = CSSGenerator.class.getResource(generatorConfig_vm).getPath().replaceFirst("/", "");
            page_index_vm = CSSGenerator.class.getResource(page_index_vm).getPath().replaceFirst("/", "");
            page_detail_vm = CSSGenerator.class.getResource(page_detail_vm).getPath().replaceFirst("/", "");

            String generatorConfigXml = CSSGenerator.class.getResource("/").getPath().replace("/target/classes/", "") + "/src/main/resources/mybatis-generator.xml";
            generatorConfigXml = generatorConfigXml.replaceFirst("/", "");
            ve.init();
            String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + "cwd_boring" + "' AND table_name LIKE '" + "" + "%';";
            JdbcUtil jdbcUtil = new JdbcUtil("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/cwd_boring?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true",
                    "root", "root");
            List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>();
            Map<String, Object> table;
            List<Map> result = jdbcUtil.selectByParams(sql, null);
            for (Map map : result) {
                if(StringUtils.isNotEmpty(onlyTable)){
                    if(!onlyTable.equalsIgnoreCase(String.valueOf(map.get("TABLE_NAME")))){
                        continue;
                    }
                }

                System.out.println(map.get("TABLE_NAME"));
                table = Maps.newHashMap();
                table.put("table_name", map.get("TABLE_NAME"));
                table.put("model_name", StringUtil.lineToHump(ObjectUtils.toString(map.get("TABLE_NAME"))));
                tables.add(table);
            }
            jdbcUtil.release();
            VelocityContext context = new VelocityContext();
            context.put("tables", tables);
            context.put("generator_javaModelGenerator_targetPackage", package_name + ".model");
            context.put("generator_sqlMapGenerator_targetPackage", "mapper");
            context.put("generator_javaClientGenerator_targetPackage", package_name + ".mapper");
            context.put("targetProject", "feiqu-system");
            context.put("master_driver", "com.mysql.jdbc.Driver");
            context.put("master_url", "jdbc:mysql://localhost:3306/cwd_boring?serverTimezone=UTC");
            context.put("master_username", "root");
            context.put("master_password", "root");
            context.put("last_insert_id_tables", lastInsertIdTables);
            VelocityUtil.generate(generatorConfig_vm, generatorConfigXml, context);

            System.out.println("========== 开始运行MybatisGenerator ==========");
            List<String> warnings = new ArrayList<>();
            File configFile = new File(generatorConfigXml);
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            for (String warning : warnings) {
                System.out.println(warning);
            }
            System.out.println("========== 结束运行MybatisGenerator ==========");

            if(!includeService){
                return;
            }

            String basePath = CSSGenerator.class.getResource("/").getPath().replace("/target/classes/", "").replaceFirst("/", "");
            basePath = basePath.replace("feiqu-generator","feiqu-system");
            String servicePath = basePath + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/service";
            String serviceImplPath = basePath + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/service/impl";
            String controllerBasePath = basePath.replace("feiqu-system","feiqu-front");
            String controllerPath = controllerBasePath + "/src/main/java/com/feiqu/web/controller";
            String pagePath = basePath + "/src/main/webapp/WEB-INF/page/";
            String ctime = new SimpleDateFormat("yyyy/M/d").format(new Date());
            for (int i = 0; i < tables.size(); i++) {
                String model = StringUtil.lineToHump(ObjectUtils.toString(tables.get(i).get("table_name")));
                String service = servicePath + "/" + model + "Service.java";
//                String serviceMock = servicePath + "/" + model + "ServiceMock.java";
                String serviceImpl = serviceImplPath + "/" + model + "ServiceImpl.java";
                String controller = controllerPath+"/"+model+"Controller.java";
                String modelLowerCase = StringUtil.toLowerCaseFirstOne(model);
                String pageDir = pagePath+modelLowerCase;
                // 生成service
                File serviceFile = new File(service);
                if (!serviceFile.exists()) {
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("ctime", ctime);
                    VelocityUtil.generate(service_vm, service, context);
                    System.out.println(service);
                }
                // 生成serviceImpl
                File serviceImplFile = new File(serviceImpl);
                if (!serviceImplFile.exists()) {
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
                    context.put("ctime", ctime);
                    VelocityUtil.generate(serviceImpl_vm, serviceImpl, context);
                    System.out.println(serviceImpl);
                }
                File controllerFile = new File(controller);
                if (!controllerFile.exists()) {
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
                    context.put("ctime", ctime);
                    VelocityUtil.generate(controller_vm, controller, context);
                    System.out.println(controller);
                }
                if(!includeHtml){
                    return;
                }
                File pageDirFile = new File(pageDir);
                if(!pageDirFile.exists()){
                    pageDirFile.mkdir();
                    String pageIndex = pageDir+"/index.html";
                    String pageDetail = pageDir+"/detail.html";
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("ctime", ctime);
                    context.put("mapper", modelLowerCase);
                    VelocityUtil.generate(page_index_vm, pageIndex, context);
                    VelocityUtil.generate(page_detail_vm, pageDetail, context);
                    System.out.println(service);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        System.out.println(CSSGenerator.class.getResource("/").getPath().replace("/target/classes/", "").replaceFirst("/", ""));
        try {
            // 需要insert后返回主键的表配置，key:表名,value:主键名
              Map<String, String> LAST_INSERT_ID_TABLES = new HashMap<>();
            generator(Global.getConfig("gen.packageName"),true,LAST_INSERT_ID_TABLES,false,"FQ_USER_PAY_WAY");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
