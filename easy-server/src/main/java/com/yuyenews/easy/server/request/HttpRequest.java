package com.yuyenews.easy.server.request;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuyenews.easy.server.request.model.FileUpLoad;
import com.yuyenews.easy.server.sessionm.SessionManager;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;

/**
 * 请求对象，对原生netty的request的补充
 * @author yuye
 *
 */
public class HttpRequest {
	
	private Logger logger = LoggerFactory.getLogger(HttpRequest.class);
	
	/**
	 * netty原生request
	 */
	private FullHttpRequest httpRequest;
	
	/**
	 * 请求体
	 */
	private String body;

	/**
	 * 参数
	 */
	private Map<String, Object> paremeters;
	
	/**
	 * 请求的文件
	 */
	private Map<String,FileUpLoad> files;
	
	/**
	 * 构造函数，框架自己用的，程序员用不到，用了也没意义
	 * @param httpRequest
	 */
	public HttpRequest(FullHttpRequest httpRequest) {
		this.body = getBody(httpRequest);
		this.setParemeters(getPams(httpRequest));
		this.httpRequest = httpRequest;
	}
	
	/**
	 * 获取请求方法
	 * @return
	 */
	public HttpMethod getMethod() {
		return httpRequest.method();
	}

	/**
	 * 获取要请求的uri
	 * @return
	 */
	public String getUri() {
		return httpRequest.uri();
	}
	
	/**
	 * 获取请求头数据
	 * @param key
	 * @return
	 */
	public Object getHeader(String key) {
		return httpRequest.headers().get(key);
	}
	
	/**
	 * 获取请求头
	 * @return
	 */
	public HttpHeaders getHeaders() {
		return httpRequest.headers();
	}

	/**
	 * 获取请求的参数集
	 * @return
	 */
	public Map<String, Object> getParemeters() {
		return paremeters;
	}

	/**
	 * 组装请求的参数
	 * @param paremeters
	 */
	private void setParemeters(Map<String, Object> paremeters) {
		Object obj = paremeters.get("files");
		if (obj != null) {
			@SuppressWarnings("unchecked")
			Map<String,FileUpLoad> files = (Map<String,FileUpLoad>) obj;
			this.files = files;
			paremeters.remove("files");
		}

		this.paremeters = paremeters;

	}
	
	/**
	 * 获取单个请求的参数
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getParemeter(String key) {
		Object objs = paremeters.get(key);
		if(objs != null) {
			List<Object> lis = (List<Object>)objs;
			return lis.get(0);
		}
		return null;
	}
	
	/**
	 * 获取单个请求的参数
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getParemeterValues(String key) {
		Object objs = paremeters.get(key);
		if(objs != null) {
			List<Object> lis = (List<Object>)objs;
			return lis;
		}
		return null;
	}

	/**
	 * 获取请求的文件
	 * @return
	 */
	public Map<String,FileUpLoad> getFiles() {
		return files;
	}

	/**
	 * 获取单个请求的文件
	 * @return
	 */
	public FileUpLoad getFile(String name) {
		if (files != null && files.size() > 0) {
			return files.get(name);
		} else {
			return null;
		}
	}

	/**
	 * 获取请求的url
	 * @return
	 */
	public String getUrl() {
		return httpRequest.uri();
	}

	/**
	 * 获取请求的body
	 * @return
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * 获取netty原生request
	 * @return
	 */
	public FullHttpRequest getHttpRequest() {
		return httpRequest;
	}

	/**
	 * 获取body参数
	 * 
	 * @param request
	 * @return
	 */
	private String getBody(FullHttpRequest request) {
		ByteBuf buf = request.content();
		return buf.toString(CharsetUtil.UTF_8);
	}

	/**
	 * 将GET, POST所有请求参数转换成Map对象
	 * @param request
	 */
	private Map<String, Object> getPams(FullHttpRequest request) {
		try {
			return new RequestParser(request).parse();
		} catch (IOException e) {
			logger.error("从请求中获取参数，报错",e);
		} 
		return new Hashtable<>();
	}
	
	/**
	 * 获取httpSession
	 * @param sessionId
	 * @return
	 */
	public HttpSession getHttpSession() {
		return SessionManager.getHttpSession(this);
	}
}