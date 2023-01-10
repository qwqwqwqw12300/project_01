package com.newlandnpt.varyar.cloudBase.service;

/**
 * 接收通知基本接口
 * @author bean
 * @param <T>
 */
public interface BaseNotifyService<T> {
	
	/**
	 * 接收通知接口
	 * @param t
	 */
    public void receve(T t);

}
