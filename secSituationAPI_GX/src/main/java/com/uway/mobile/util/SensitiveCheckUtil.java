package com.uway.mobile.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.uway.mobile.common.Constance;
import com.uway.mobile.common.Result;

public class SensitiveCheckUtil {

	/**
	 * 敏感词库
	 */
	@SuppressWarnings("rawtypes")
	public static Map sensitiveWordMap = null;

	/**
	 * 只过滤最小敏感词
	 */
	public static int minMatchTYpe = 1;

	/**
	 * 过滤所有敏感词
	 */
	public static int maxMatchType = 2;

	/**
	 * 根据敏感词集合,文章内容，返回结果
	 * 
	 * @param sensitiveWordList
	 * @param text
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static Result getResultByText(List<String> sensitiveWordList, String text, Result result) throws Exception {
		// 初始化敏感库
		initKeyWord(sensitiveWordList);
		// 校验文章是否合法，并过滤所有敏感词
		boolean bool = SensitiveCheckUtil.isContaintSensitiveWord(text, 1);
		if (bool) {
			result.setCode(Constance.RESPONSE_SENSITIVE_EXIST);
			result.setMsg("含有敏感词！");
		}
		return result;
	}

	/**
	 * 敏感词库敏感词数量
	 * 
	 * @return
	 */
	public static int getWordSize() {
		if (SensitiveCheckUtil.sensitiveWordMap == null) {
			return 0;
		}
		return SensitiveCheckUtil.sensitiveWordMap.size();
	}

	/**
	 * 是否包含敏感词
	 * 
	 * @param txt
	 * @param matchType
	 * @return
	 */
	public static boolean isContaintSensitiveWord(String txt, int matchType) {
		boolean flag = false;
		for (int i = 0; i < txt.length(); i++) {
			int matchFlag = checkSensitiveWord(txt, i, matchType);
			if (matchFlag > 0) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 检查敏感词数量
	 * 
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
		boolean flag = false;
		// 记录敏感词数量
		int matchFlag = 0;
		char word = 0;
		Map nowMap = SensitiveCheckUtil.sensitiveWordMap;
		for (int i = beginIndex; i < txt.length(); i++) {
			word = txt.charAt(i);
			// 判断该字是否存在于敏感词库中
			nowMap = (Map) nowMap.get(word);
			if (nowMap != null) {
				matchFlag++;
				// 判断是否是敏感词的结尾字，如果是结尾字则判断是否继续检测
				if ("1".equals(nowMap.get("isEnd"))) {
					flag = true;
					// 判断过滤类型，如果是小过滤则跳出循环，否则继续循环
					if (SensitiveCheckUtil.minMatchTYpe == matchType) {
						break;
					}
				}
			} else {
				break;
			}
		}
		if (!flag) {
			matchFlag = 0;
		}
		return matchFlag;
	}

	/**
	 * 初始化敏感词
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map initKeyWord(List<String> sensitiveWords) throws Exception {
		// 从敏感词集合对象中取出敏感词并封装到Set集合中
		Set<String> keyWordSet = new HashSet<String>();
		for (String s : sensitiveWords) {
			keyWordSet.add(s.trim());
		}
		return addSensitiveWordToHashMap(keyWordSet);
	}

	/**
	 * 封装敏感词库
	 * 
	 * @param keyWordSet
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map addSensitiveWordToHashMap(Set<String> keyWordSet) {
		// 初始化HashMap对象并控制容器的大小
		sensitiveWordMap = new HashMap(keyWordSet.size());
		// 敏感词
		String key = null;
		// 用来按照相应的格式保存敏感词库数据
		Map nowMap = null;
		// 用来辅助构建敏感词库
		Map<String, String> newWorMap = null;
		// 使用一个迭代器来循环敏感词集合
		Iterator<String> iterator = keyWordSet.iterator();
		while (iterator.hasNext()) {
			key = iterator.next();
			// 等于敏感词库，HashMap对象在内存中占用的是同一个地址，所以此nowMap对象的变化，sensitiveWordMap对象也会跟着改变
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				// 截取敏感词当中的字，在敏感词库中字为HashMap对象的Key键值
				char keyChar = key.charAt(i);

				// 判断这个字是否存在于敏感词库中
				Object wordMap = nowMap.get(keyChar);
				if (wordMap != null) {
					nowMap = (Map) wordMap;
				} else {
					newWorMap = new HashMap<String, String>();
					newWorMap.put("isEnd", "0");
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}

				// 如果该字是当前敏感词的最后一个字，则标识为结尾字
				if (i == key.length() - 1) {
					nowMap.put("isEnd", "1");
				}
				// System.out.println("封装敏感词库过程：" + sensitiveWordMap);
			}
			// System.out.println("查看敏感词库数据:" + sensitiveWordMap);
		}
		return newWorMap;
	}

	/*
	 * public static void main(String[] args) throws Exception { List<String>
	 * list = new ArrayList<String>(); list.add("擦多负少"); list.add("擦皮皮虾"); //
	 * list.add("方式"); Map<String, String> map = initKeyWord(list);
	 * //initKeyWord(list); String txt = "cacacacaca皮皮虾胜多负少所发生的方式"; Boolean bool
	 * = isContaintSensitiveWord(txt, 2); for(String test :map.keySet()){
	 * System.err.println(map.get(test)); }
	 * //System.err.println(map.toString()); //System.err.println(bool); }
	 */
}
