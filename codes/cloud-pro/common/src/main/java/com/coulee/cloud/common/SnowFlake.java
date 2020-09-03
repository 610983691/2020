package com.coulee.cloud.common;


import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.net.InetAddresses;

import lombok.extern.slf4j.Slf4j;

/**
 * twitter的snowflake算法 改造适应自己项目
 * 
 * @author tongjie
 */
@Component("snowFlakeIDGenerator")
@Slf4j
public class SnowFlake {

	
	/**
	 * 开始时间，差不多这个点儿 2020-08-14 14:55:00
	 */
	private final static long START_STMP = 1597388036492L;

	/**
	 * 每毫秒可用的序号数量
	 */
	private final static long SEQUENCE_BIT = 6; // 序列号占用的位数,每毫秒产生的ID数=32，每秒总共64000个

	/***
	 * 机器ID数量，这里我们取IP的后16位做机器ID
	 */
	private final static long MACHINE_BIT = 16; // 机器标识占用的位数,可以使用IP的后16位

	/**
	 * 机器ID的值，因为同一个机器上不会部署两个相同的应用。所以同一机器产生相同的主键时，这两个主键也是入不同的业务表。
	 * 对日志的ID，这种情况日志的量级不应该是入数据库。一般入ES/mango 等非关系型数据库，利用es他们自身提供的docid来做主键，
	 * 而我们的logid只是一个普通字段。（目前应该也达不到这个量级）
	 */
	private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);

	/***
	 * 每毫秒可用的序号数量
	 */
	private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

	/**
	 * 机器序号左移位数
	 */
	private final static long MACHINE_LEFT = SEQUENCE_BIT;

	/**
	 * 时间戳左移位数
	 */
	private final static long TIMESTMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

	/**
	 * 机器ID
	 */
	private long machineId;

	/***
	 * 序号值
	 */
	private long sequence = 0L;

	/**
	 * 上一个毫秒数
	 */
	private long lastStmp = -1L;

	
	/**
	 * 构造方法,需要配置机器ID。 这个实例化可以采用
	 * 如果未配置机器ID：就默认以当前的IP的后两位字节的int值为机器ID
	 * 
	 * 
	 * @param machineId
	 */
	public SnowFlake(@Value("${app.snowflake.machine.id:#{null}}") Long machine) {
		if(Objects.isNull(machine)) {
			try {
				InetAddress ipv4 = Inet4Address.getLocalHost();
				log.info("snowflake machine ip is:{}",ipv4);
				BigInteger ipv4int = InetAddresses.toBigInteger(ipv4);
			    machineId = 65535&ipv4int.intValue();
			} catch (Exception e) {
				throw new IllegalArgumentException("init get machine ip err,machineId can't be generate");
			}
		}else {
			machineId = machine.longValue();//有值的情况	
		}
		if (machineId > MAX_MACHINE_NUM || machineId < 0) {
			throw new IllegalArgumentException("machineId can't be greater than 65535 or less than 0");
		}
		log.info("snowflake machine id is:{}",machineId);
	}

	/**
	 * 获取下一个ID
	 * 
	 * @return
	 */
	public synchronized long nextId() {
		long currStmp = System.currentTimeMillis();
		if (currStmp < lastStmp) {
			throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
		}

		if (currStmp == lastStmp) {
			// 相同毫秒内，序列号自增
			sequence = (sequence + 1) & MAX_SEQUENCE;
			// 同一毫秒的序列数已经达到最大
			if (sequence == 0L) {
				currStmp = getNextMill();
			}
		} else {
			// 不同毫秒内，序列号置为0
			sequence = 0L;
		}

		lastStmp = currStmp;

		return (currStmp - START_STMP) << TIMESTMP_LEFT // 时间戳部分
				| machineId << MACHINE_LEFT // 机器标识部分
				| sequence; // 序列号部分
	}

	/**
	 * 获取下一个毫秒数
	 * 
	 * @return
	 */
	private long getNextMill() {
		long mill = System.currentTimeMillis();
		while (mill <= lastStmp) {
			mill = System.currentTimeMillis();
		}
		return mill;
	}

	public static void main(String[] args) {
		SnowFlake sn =new SnowFlake(0L);
		System.out.println(sn.nextId());
	}

}
