package com.yuyenews.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.util.FileUtil;
import com.yuyenews.easy.server.constant.EasySpace;

/**
 * 加载配置数据
 * @author yuye
 *
 */
public class Config {
	
	private static Logger log = LoggerFactory.getLogger(Config.class);

	/**
	 * 加载配置文件
	 */
	public static void loadConfig(EasySpace constants) {
		try {
			String content = FileUtil.readFileString("/easy.json");
			
			JSONObject object = JSONObject.parseObject(content);
			
			constants.setAttr("config", object);
		} catch (Exception e) {
			log.error("加载配置文件报错",e);
		}
	}
}
