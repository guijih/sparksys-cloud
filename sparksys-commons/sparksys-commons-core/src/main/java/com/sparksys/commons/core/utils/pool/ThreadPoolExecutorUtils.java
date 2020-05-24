package com.sparksys.commons.core.utils.pool;

import java.util.concurrent.*;

/**
 * description: 线程池-静态内部类方式
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:14:26
 */
public class ThreadPoolExecutorUtils {

    private ThreadPoolExecutorUtils() {
        System.out.println("初始化线程池--");
    }

    public static void execute(Runnable runnable) {
        SingleThreadPoolExecutorInstance.THREAD_POOL_EXECUTOR.execute(runnable);
    }


    private static class SingleThreadPoolExecutorInstance {
        private static int corePoolSize = Runtime.getRuntime().availableProcessors();
        private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                corePoolSize, corePoolSize + 1, 30,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000),
                Executors.defaultThreadFactory(),
                new CustomRejectedExecutionHandler()
        );
    }

    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
            try {
                //由LinkedBlockingQueue的offer改成put阻塞方法
                executor.getQueue().put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutorUtils.execute(() -> System.out.println("哈哈"));
    }
}
