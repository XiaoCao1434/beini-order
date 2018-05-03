package com.beini.order.feignClient.product.impl;

import org.springframework.stereotype.Component;

import com.beini.core.enums.ResultEnum;
import com.beini.core.utils.ResultVOUtil;
import com.beini.core.vo.ResultVO;
import com.beini.order.feignClient.product.ProductFeignClient;
import com.beini.product.entity.Product;

/**
 * 从产品服务中获取资源报错的信息处理
 * 
 * @author lb_chen
 */
@SuppressWarnings("unchecked")
@Component
public class ProductFeignClientFallback implements ProductFeignClient {

	@Override
	public ResultVO<Product> findById(String id) {
		return ResultVOUtil.error(ResultEnum.PRODUCT_NOT_EXIST);
	}

	@Override
	public ResultVO<Product> findByPage(Integer pageNo, Integer pageSize) {
		return ResultVOUtil.error(ResultEnum.PRODUCT_NOT_EXIST);
	}

	@Override
	public ResultVO<Product> updateStock(String proUuid, double number) {
		return ResultVOUtil.error(ResultEnum.PRODUCT_STOCK_ERROR);
	}

}
