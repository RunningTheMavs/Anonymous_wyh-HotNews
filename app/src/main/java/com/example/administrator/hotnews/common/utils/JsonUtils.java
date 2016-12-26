package com.example.administrator.hotnews.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class JsonUtils {

	/**
	 * 创建Json字符串
	 * @param value
	 * @return
	 */
	public static String createJsonString(Object value){
		return JSON.toJSONString(value);
	}

	/**
	 * 将Json字符串解析为List<T>集合
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> List<T> getListObject(String json, Class<T> cls){
		return JSON.parseArray(json, cls);
	}
	/**
	 * 将Json字符串，解析为Map<String, String>集合
	 * @param json
	 * @return
	 */
	public static Map<String, String> getMapStr(String json){
		return JSON.parseObject(json, new TypeReference<Map<String, String>>(){});
	}

	/**
	 * 将Json字符串，解析为Map<String, Object>集合
	 * @param json
	 * @return
	 */
	public static Map<String, Object> getMapObj(String json){
		return JSON.parseObject(json, new TypeReference<Map<String, Object>>(){});
	}

	/**
	 * 将Json字符串解析为List<Map<String, Object>>集合
	 * @param json
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(String json){
		return JSON.parseObject(json, new TypeReference<List<Map<String,Object>>>(){});
	}


}
