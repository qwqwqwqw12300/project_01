package com.newlandnpt.varyar;

import com.newlandnpt.varyar.system.domain.ServeRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author newlandnpt
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class VaryarApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(VaryarApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  varyar启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
