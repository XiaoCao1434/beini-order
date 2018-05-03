package com.beini.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderDetailVo {
	/**商品编码*/
	private String proUuid;
	/**购买数量*/
	private double number;
}
