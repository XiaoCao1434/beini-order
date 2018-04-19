package com.beini.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beini.core.enums.ResultEnum;
import com.beini.core.utils.ResultVOUtil;
import com.beini.core.vo.ResultVO;
import com.beini.order.entity.OrderDetail;
import com.beini.order.service.OrderDetailService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/order/detail")
@SuppressWarnings("rawtypes")
public class OrderDetailController {
	@Autowired
	private OrderDetailService orderDetailService;

	/**
	 * 根据订单详情ID获取订单详情信息
	 * 
	 * @param id
	 *            订单详情ID
	 * @return 订单详情信息
	 */
	@ApiOperation(value = "根据订单详情ID获取订单详情信息")
	@GetMapping("/{id}")
	public ResultVO findById(@PathVariable(value = "id") String id) {
		OrderDetail orderDetail = orderDetailService.findById(id);
		return ResultVOUtil.success(orderDetail);
	}

	@ApiOperation(value = "根据订单ID和分页信息获取订单详情分页信息")
	@GetMapping("/")
	public ResultVO findByPage(@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		PageRequest request = new PageRequest(pageNo - 1, pageSize);
		Page<OrderDetail> page = orderDetailService.findAll(request);
		return ResultVOUtil.success(page);
	}
	/**
	 * 根据订单ID获取订单详情信息
	 * @param id 订单ID
	 * @return 订单详情信息列表
	 */
	@ApiOperation(value="根据订单ID和分页信息获取订单详情分页信息")
	@GetMapping("/order/{orderId}")
	public ResultVO findPageByOrderId(@PathVariable(value = "orderId") String orderId,
			@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
		PageRequest request = new PageRequest(pageNo - 1, pageSize);
		Page<OrderDetail> page = orderDetailService.findAll(orderId,request);
		return ResultVOUtil.success(page);
	}
	
	@ApiOperation(value = "根据订单详情信息进行数据更新(以主键为依据)")
	@PutMapping("")
	public ResultVO update(OrderDetail orderDetail) {
		if (orderDetail == null || orderDetail.getOrderUuid() == null || "".equals(orderDetail.getOrderUuid())) {
			return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_NOT_EXIST);
		}
		if (orderDetailService.update(orderDetail) == null) {
			return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_UPDATE_FAIL);
		} else {
			return ResultVOUtil.success();
		}
	}

	@ApiOperation(value = "增加订单详情信息")
	@PostMapping("")
	public ResultVO save(OrderDetail orderDetail) {
		if (orderDetailService.save(orderDetail) == null) {
			return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_INSERT_FAIL);
		} else {
			return ResultVOUtil.success();
		}
	}

	@ApiOperation(value = "根据订单详情ID删除订单详情信息")
	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable(value = "id") String id) {
		try {
			orderDetailService.delete(id);
			return ResultVOUtil.success();
		} catch (Exception e) {
			return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_DELETE_FAIL);
		}
	}
}
