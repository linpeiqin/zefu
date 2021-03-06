package com.zefu.mq.config;

import com.zefu.common.base.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
服务调用下发
 */
@Configuration
@EnableAsync
public class ServiceInvokeTaskConfig {
    // 声明一个线程池(并指定线程池的名字)
    @Bean(Constants.TASK.SERVICE_INVOKE_NAME)
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(20);
        //最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(100);
        // 线程池对拒绝任务的处理策略
        //丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        //允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        //线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix(Constants.TASK.SERVICE_INVOKE_NAME);
        executor.initialize();
        return executor;
    }
}
