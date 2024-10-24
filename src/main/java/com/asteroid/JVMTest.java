package com.asteroid;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;

public class JVMTest {
    public static void main(String[] args) {
        // 获取当前 JVM 的总内存大小
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();

        System.out.println("Heap Memory Usage: " + heapMemoryUsage);
        System.out.println("Non-Heap Memory Usage: " + nonHeapMemoryUsage);

        // 打印出JVM参数
        System.out.println("Input Arguments: " + runtimeMxBean.getInputArguments());
    }
}
