package com.feiqu;

//import com.feiqu.web.listener.FeiquListener;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 * 
 * @author chenweidong
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@MapperScan("com.feiqu.*.mapper")
@EnableScheduling
public class FeiQuApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication springApplication = new SpringApplication(FeiQuApplication.class);
//        springApplication.addListeners(new FeiquListener());
//        SpringApplication.run(FeiQuApplication.class, args);
        springApplication.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  飞趣社区启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}