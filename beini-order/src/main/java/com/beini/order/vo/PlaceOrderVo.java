package com.beini.order.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 客户下单值对象
 * @author Administrator
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderVo {
	/**客户编号*/
	private String openId;
	
	/** 收货地址编号*/
	private String addressNo;
	/**店铺编号*/
	private String storeId;
	
	private List<PlaceOrderDetailVo> details = new ArrayList<>();
	
}
