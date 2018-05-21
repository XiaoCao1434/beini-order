package com.beini.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.beini.order.entity.DeliveryAddress;


public interface DeliveryAddressService {
	/*查询*/
	Page<DeliveryAddress> findAll(Pageable pageable);
	DeliveryAddress findById(String id);
	/*更新*/
	DeliveryAddress save(DeliveryAddress bean);
	DeliveryAddress update(DeliveryAddress bean);
	void delete(String... id);
	
	/**
	 * 根据openId和分页信息获取送货地址分页信息内容
	 * @param openId openId
	 * @param request 分页信息
	 * @return 送货地址分页信息内容
	 */
	Page<DeliveryAddress> findAllByOpenId(String openId, Pageable request);
}
