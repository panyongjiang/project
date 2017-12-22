package com.uway.mobile.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mongodb.DB;
import com.mongodb.MongoClient;

@Component
public class MongoUtil {
	/**
	 * @ThreadSafe
	 * java并发包java。util。处理多线程访问，，，基于map接口
    private final ConcurrentMap<String, DB> dbCache = new ConcurrentHashMap<String, DB>();
	 */
	@Autowired
	private  MongoClient mongoClient;
	
	public  DB getDB(String dbName){
   	 if (dbName != null && !"".equals(dbName)) {
			@SuppressWarnings("deprecation")
			DB db = mongoClient.getDB(dbName);
            return db;
        }
        return null;
   }
}
