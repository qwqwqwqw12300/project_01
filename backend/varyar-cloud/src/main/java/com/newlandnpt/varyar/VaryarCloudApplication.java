package com.newlandnpt.varyar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author newlandnpt
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class VaryarCloudApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(VaryarCloudApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  varyarCloud启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
