package com.uway.mobile.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class JedisClusterConfig {
	@Value("${redis.cache.clusterNodes}")
	private String clusterNodes;

	@Bean
    public RedisClusterConfiguration getRedisCluster() {
        Set<RedisNode> jedisClusterNodes = new HashSet<RedisNode>();
        // Jedis Cluster will attempt to discover cluster nodes automatically
        String[] nodes = clusterNodes.split(",");
        for(String node : nodes){
        	String[] table = node.split(":");
        	jedisClusterNodes.add(new RedisNode(table[0], Integer.valueOf(table[1])));
        }
        //jedisClusterNodes.add( xxx );
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setClusterNodes(jedisClusterNodes);
        return redisClusterConfiguration;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(getRedisCluster());
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(JedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }
}
