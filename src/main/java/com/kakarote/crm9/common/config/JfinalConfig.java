package com.kakarote.crm9.common.config;

import BP.Difference.JFinalUtils;
import BP.Difference.RequestInterceptor;
import BP.Tools.DateUtils;
import BP.workFlowWeb.config.JflowRouter;
import BP.workFlowWeb.jflowHandler.StaticHandler;
import cn.hutool.core.util.ClassLoaderUtil;
import com.jfinal.aop.Aop;
import com.jfinal.config.*;
import com.jfinal.core.paragetter.ParaProcessorBuilder;
import com.jfinal.ext.proxy.CglibProxyFactory;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.RenderManager;
import com.jfinal.template.Engine;
import com.kakarote.crm9.common.config.cache.CaffeineCache;
import com.kakarote.crm9.common.config.json.ErpJsonFactory;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.config.paragetter.MapParaGetter;
import com.kakarote.crm9.common.config.paragetter.PageParaGetter;
import com.kakarote.crm9.common.config.redis.RedisPlugin;
import com.kakarote.crm9.common.config.render.ErpRenderFactory;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.common.service.JflowRefreshService;
import com.kakarote.crm9.erp._MappingKit;
import com.kakarote.crm9.erp.admin.common.AdminRouter;
import com.kakarote.crm9.erp.bi.common.BiRouter;
import com.kakarote.crm9.erp.crm.common.CrmRouter;
import com.kakarote.crm9.erp.oa.common.OaRouter;
import com.kakarote.crm9.erp.sms.common.SmsRouter;
import com.kakarote.crm9.erp.work.common.WorkRouter;
import com.kakarote.crm9.erp.wx.common.WxRouter;
import com.kakarote.crm9.erp.wx.config.WxCpConfiguration;
import com.kakarote.crm9.erp.wx.config.WxMaConfiguration;
import com.kakarote.crm9.erp.wxcms.common.WxCmsRouter;
import com.kakarote.crm9.erp.yeyx.common.YeyxRouter;
import com.kakarote.crm9.erp.yzj.common.YzjRouter;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

/**
 * API 引导式配置
 */
public class JfinalConfig extends JFinalConfig {

    public static Prop prop = JFinalUtils.loadConfig();
    /**
     * 配置常量
     */
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(prop.getBoolean("jfinal.devMode", true));
        me.setInjectDependency(true);
        //设置上传文件到哪个目录,默认在webapps下建一个upload文件夹，这里可以修改具体参考jfinal文档，在使用getFile()\getFiles()时会默认上传到该文件夹
        if(ClassLoaderUtil.isPresent("com.jfinal.server.undertow.UndertowServer")){
            me.setBaseUploadPath(BaseConstant.UPLOAD_PATH);
            me.setBaseDownloadPath(BaseConstant.UPLOAD_PATH);
        }
        me.setJsonFactory(new ErpJsonFactory());
        //限制上传100M
        me.setMaxPostSize(104857600);
        //TODO 因为很多人配置jdk环境问题，默认使用cgilb代理
        me.setProxyFactory(new CglibProxyFactory());

        /**
         * add by tangamnrong 20200408 流程
         */
        me.setJsonDatePattern(DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE);
    }

    /**
     * 配置路由
     */
    @Override
    public void configRoute(Routes me) {
        me.add(new AdminRouter());
        me.add(new BiRouter());
        me.add(new CrmRouter());
        me.add(new OaRouter());
        me.add(new WorkRouter());
        me.add(new JflowRouter());//流程
        me.add(new WxRouter()); //微信集成
        me.add(new WxCmsRouter()); //微信粉丝业务集成
        me.add(new YzjRouter()); //云之家集成
        me.add(new SmsRouter());//短信集成
        me.add(new YeyxRouter()); //言而有信集成
    }

    @Override
    public void configEngine(Engine me) {

    }

    /**
     * 配置插件
     */
    @Override
    public void configPlugin(Plugins me) {
        //crm的数据连接池
        ParaProcessorBuilder.me.regist(BasePageRequest.class, PageParaGetter.class, null);
        ParaProcessorBuilder.me.regist(Map.class, MapParaGetter.class, null);
        // 配置 druid 数据库连接池插件
        DruidPlugin druidPlugin = JFinalUtils.getDruidPlugin();//modify by tangmanrong 20200409
        druidPlugin.setInitialSize(1);
        druidPlugin.setMinIdle(1);
        druidPlugin.setMaxActive(2000);
        druidPlugin.setTimeBetweenEvictionRunsMillis(5000);
        druidPlugin.setValidationQuery("select 1");
        druidPlugin.setTimeBetweenEvictionRunsMillis(60000);
        druidPlugin.setMinEvictableIdleTimeMillis(30000);
        druidPlugin.setFilters("stat");
        //druidPlugin.setFilters("stat,wall");//modify by tangmanrong 20200409，stat是收集各种信息的,wall是sql防注入的，防sql注入导致jflow很多sql报错
        me.add(druidPlugin);
        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin("druidPlugin1",druidPlugin);
        arp.setCache(CaffeineCache.ME);
        arp.setDialect(new MysqlDialect());
        arp.setShowSql(true);
        arp.getEngine().addDirective("fori", CrmDirective.class,true);
        arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED); //modify by tangmanrong 20200409
        me.add(arp);
        //扫描sql模板
        getSqlTemplate(PathKit.getRootClassPath() + "/template", arp);
        //Redis以及缓存插件
        RedisPlugin redisPlugin=new RedisPlugin();
        me.add(redisPlugin);
        //cron定时器
        me.add(new Cron4jPlugin(PropKit.use("config/cron4j.txt")));

        //model映射
        _MappingKit.mapping(arp);

        /**
         * 流程的数据连接池
         */
        /*DruidPlugin druidPlugin2 = JFinalUtils.getDruidPlugin();
        me.add(druidPlugin2);

        ActiveRecordPlugin wfPlugin = new ActiveRecordPlugin("druidPlugin2",druidPlugin2);
        //配置事务级别为读已提交
        wfPlugin.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
        //配置数据库方言
        wfPlugin.setDialect(new MysqlDialect());

        me.add(wfPlugin);*/
    }

    /**
     * modify by tangmanrong 20200409
     */
    /*public static DruidPlugin createDruidPlugin() {
        return new DruidPlugin(prop.get("mysql.jdbcUrl"), prop.get("mysql.user"), prop.get("mysql.password").trim()).setInitialSize(1).setMinIdle(1).setMaxActive(2000).setTimeBetweenEvictionRunsMillis(5000).setValidationQuery("select 1").setTimeBetweenEvictionRunsMillis(60000).setMinEvictableIdleTimeMillis(30000).setFilters("stat,wall");
    }*/

    /**
     * 配置全局拦截器
     */
    @Override
    public void configInterceptor(Interceptors me) {
        //添加全局拦截器
        //me.addGlobalActionInterceptor(new ErpInterceptor()); //基于crm的拦截器
        //me.add(new AuthInterceptor());

        //流程的拦截器 add by tangmanrong 20200408
        me.addGlobalActionInterceptor(new RequestInterceptor()); //将request、response放入工具类
    }

    /**
     * 配置处理器
     */
    @Override
    public void configHandler(Handlers me) {
        //配置数据库监控
        //me.add(new DruidStatViewHandler("/druid", new DruidConfig()));
        //自定义渲染工厂
        RenderManager.me().setRenderFactory(new ErpRenderFactory());

        //流程的handler add by tangmanrong 20200408
        me.add(new StaticHandler());
        //me.add(new ProxyHandler());
        //me.add(new FakeStaticHandler(".do"));

        //druid监控页面 add by tangmanrong 20200415
        //配置druid 的状态页面访问URI,//设置访问路径
        /*DruidStatViewHandler dsvh = new DruidStatViewHandler("/druid",
        new IDruidStatViewAuth(){
            public boolean isPermitted(HttpServletRequest request) {
                //通过权限判断工具类判断此用户是否有PermissionKey.DRUID_MONITOR这个权限
                return true;
            }
        });
        me.add(dsvh);*/
    }

    @Override
    public void onStart() {
        //WorkService workService= Aop.get(WorkService.class);
        //workService.initialization();

        //初始化小程序的配置
        WxMaConfiguration wxMaConfiguration= new WxMaConfiguration();
        wxMaConfiguration.init();

        //初始化企业微信的配置
        WxCpConfiguration wxCpConfiguration= new WxCpConfiguration();
        wxCpConfiguration.init();

        //刷新流程事件实体 add by tangamnrong
        JflowRefreshService jflowRefreshService = Aop.get(JflowRefreshService.class);
        jflowRefreshService.initFlow();
    }

    @Override
    public void onStop() {

    }

    private void getSqlTemplate(String path, ActiveRecordPlugin arp) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File childFile : files) {
                    if (childFile.isDirectory()) {
                        getSqlTemplate(childFile.getAbsolutePath(), arp);
                    } else {
                        if (childFile.getName().toLowerCase().endsWith(".sql")) {
                            arp.addSqlTemplate(childFile.getAbsolutePath().replace(PathKit.getRootClassPath(), "").replace("\\", "/"));
                        }
                    }
                }
            }
        }
    }
}
