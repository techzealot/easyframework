package com.yuyenews.aop.base;

/**
 * AOP定义模板
 * @author yuye
 *
 */
public interface BaseAop {

	/**
	 * 方法开始前调用
	 */
	void startMethod();
	
	/**
	 * 方法结束后调用
	 */
	void endMethod();
}
