package com.zefu.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.zefu.common.security.annotation.EnableCustomConfig;
import com.zefu.common.security.annotation.EnableRyFeignClients;
import com.zefu.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 系统模块
 * 
 * @author linking
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.zefu.common.storage.es")
public class ZeFuBusinessApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ZeFuBusinessApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  业务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
