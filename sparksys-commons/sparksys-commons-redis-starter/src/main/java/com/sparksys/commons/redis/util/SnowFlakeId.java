package com.sparksys.commons.redis.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * description: snowflake算法--用于分布式主键生成策略
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:34:24
 */
public class SnowFlakeId {

    /**
     * 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）
     */
    private final static long TWELFTH = 1288834974657L;
    /**
     * 机器标识位数
     */
    private final static long WORKER_ID_BITS = 5L;
    /**
     * 数据中心标识位数
     */
    private final static long DATA_CENTER_ID_BITS = 5L;
    /**
     * 机器ID最大值
     */
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    /**
     * 数据中心ID最大值
     */
    private final static long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    /**
     * 毫秒内自增位
     */
    private final static long SEQUENCE_BITS = 12L;
    /**
     * 机器ID偏左移12位
     */
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 数据中心ID左移17位
     */
    private final static long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间毫秒左移22位
     */
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 上次生产id时间戳
     */
    private static long lastTimestamp = -1L;
    /**
     * 0，并发控制
     */
    private long sequence = 0L;

    private final long workerId;
    /**
     * 数据标识id部分
     */
    private final long DATA_CENTER_ID;

    private static SnowFlakeId snowFlakeId;

    static {
        snowFlakeId = new SnowFlakeId();
    }

    private SnowFlakeId() {
        this.DATA_CENTER_ID = getDataCenterId(MAX_DATA_CENTER_ID);
        this.workerId = getMaxWorkerId(DATA_CENTER_ID, MAX_WORKER_ID);
    }

    public SnowFlakeId(long workerId, long dataCenterId) {
        if ((workerId > MAX_WORKER_ID) || (workerId < 0)) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
                    MAX_WORKER_ID));
        }
        if ((dataCenterId > MAX_DATA_CENTER_ID) || (dataCenterId < 0)) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",
                    MAX_DATA_CENTER_ID));
        }
        this.workerId = workerId;
        this.DATA_CENTER_ID = dataCenterId;
    }

    /**
     * @param lastTimestamp 上次时间戳
     * @return long
     * @Author zhouxinlei
     * @Date 2020-01-17 11:05:53
     */
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 系统时间获取
     *
     * @return long
     * @throws
     * @Author zhouxinlei
     * @Date 2019-12-25 15:43:38
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 数据标识id部分
     *
     * @param maxDataCenterId
     * @return long
     * @Author zhouxinlei
     * @Date 2019-12-25 15:43:23
     */
    protected static long getDataCenterId(long maxDataCenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();

                id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8)))
                        >> 6;
                id = id % (maxDataCenterId + 1);
            }
        } catch (Exception e) {
            System.out.println(" getDatacenterId: " + e.getMessage());

        }
        return id;
    }

    /**
     * 获取 maxWorkerId
     *
     * @param dataCenterId
     * @param maxWorkerId
     * @return long
     * @Author zhouxinlei
     * @Date 2019-12-25 15:41:10
     */
    protected static long getMaxWorkerId(long dataCenterId, long maxWorkerId) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(dataCenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            //GET jvmPid
            stringBuffer.append(name.split("@")[0]);
        }
        //MAC + PID 的 hashcode 获取16个低位
        return (stringBuffer.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * 获取下一个ID
     *
     * @return long
     * @Author zhouxinlei
     * @Date 2019-12-25 15:40:56
     */
    public synchronized long getNextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {

                // 当前毫秒内计数满了，则等待下一秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID

        return ((timestamp - TWELFTH) << TIMESTAMP_LEFT_SHIFT) | (DATA_CENTER_ID << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT) | sequence;
    }


    /**
     * 分布式生成id
     *
     * @return Long
     * @Author zhouxinlei
     * @Date 2020-01-17 11:06:10
     */
    public static synchronized Long getSnowFlakeId() {
        return snowFlakeId.getNextId();
    }

    public static void main(String[] args) {
        System.out.println(SnowFlakeId.getSnowFlakeId());
    }
}
