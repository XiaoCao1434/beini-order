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
import com.beini.order.entity.Order;
import com.beini.order.feignClient.product.ProductFeignClient;
import com.beini.order.service.OrderService;
import com.beini.product.entity.Product;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/order/order")
@SuppressWarnings("rawtypes")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductFeignClient productFeignClient;
	/**
	 * 根据订单ID获取订单信息
	 * @param id 订单ID
	 * @return 订单信息
	 */
	@ApiOperation(value="根据订单ID获取订单信息")
	@GetMapping("{id}")
	public ResultVO findById(@PathVariable(value = "id") String id) {
		Order order = orderService.findById(id);
		System.out.println("我是订单模块的findById： findById("+id+");");
		ResultVO rv = productFeignClient.findById("11");
		//System.out.println(product+"---------------订单模块的商品信息");
		System.out.println("我是商品模块的findById： findById(11);  "+rv);
		//Map<String ,Object> model = new HashMap<String,Object>();
		//model.put("order", order);
		//model.put("product", product);
		return ResultVOUtil.success(order);
	}
	
	/**
	 * 根据分页信息获取订单分页信息
	 * @param pageNo 第几页
	 * @param pageSize 每页条数
	 * @return 订单分页信息
	 */
	@ApiOperation(value="根据分页信息获取订单分页信息")
	@GetMapping("")
	public ResultVO findByPage(@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		PageRequest request = new PageRequest(pageNo - 1, pageSize);
		Page<Order> page = orderService.findAll(request);
		return ResultVOUtil.success(page);
	}
	@ApiOperation(value="根据订单信息进行数据更新(以主键为依据)")
	@PutMapping
	public ResultVO update(Order order) {
		if (order == null || order.getOrderUuid() == null || "".equals(order.getOrderUuid())) {
			return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_NOT_EXIST);
		}
		if (orderService.update(order) == null) {
			return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_UPDATE_FAIL);
		} else {
			return ResultVOUtil.success();
		}
	}
	@ApiOperation(value="增加订单信息")
	@PostMapping
	public ResultVO save(Order order) {
		
		/*查询商品--数量、价格*/
		/*计算总价*/
		/*写入订单数据库--订单总表和订单详情表*/
		/*扣库存*/
		if (orderService.save(order) == null) {
			return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_INSERT_FAIL);
		} else {
			return ResultVOUtil.success();
		}
	}
	@ApiOperation(value="根据订单ID删除订单信息")
	@DeleteMapping("{id}")
	public ResultVO deleteById(@PathVariable(value="id") String id) {
		try {
			orderService.delete(id);
			return ResultVOUtil.success();
		} catch (Exception e) {
			return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_DELETE_FAIL);
		}
	}
}
