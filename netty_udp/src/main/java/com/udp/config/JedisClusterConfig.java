package com.udp.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class JedisClusterConfig {
	
	public static String clusterNodes="192.168.8.252:7000,192.168.8.252:7001,192.168.8.252:7002,192.168.8.248:7000,192.168.8.248:7001,192.168.8.248:7002";
	
    public static RedisClusterConfiguration getRedisCluster() {
        Set<RedisNode> jedisClusterNodes = new HashSet<RedisNode>();
        // Jedis Cluster will attempt to discover cluster nodes automatically
        String[] nodes = clusterNodes.split(",");
        for(String node : nodes){
        	//取到节点
        	String[] table = node.split(":");
        	//添加ip和端口
        	jedisClusterNodes.add(new RedisNode(table[0], Integer.valueOf(table[1])));
        }
        //jedisClusterNodes.add( xxx );
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setClusterNodes(jedisClusterNodes);
        return redisClusterConfiguration;
    }

    public static JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(getRedisCluster());
        return redisConnectionFactory;
    }

    public static RedisTemplate<String, String> redisTemplate(JedisConnectionFactory cf) {
    	RedisTemplate<String, String> redisTemplates = new RedisTemplate<String, String>();
        redisTemplates.setConnectionFactory(cf);
        return redisTemplates;
    }
}
