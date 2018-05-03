package com.beini.order.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

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
import com.beini.core.exception.GlobalException;
import com.beini.core.utils.ResultVOUtil;
import com.beini.core.vo.ResultVO;
import com.beini.order.entity.Order;
import com.beini.order.entity.OrderDetail;
import com.beini.order.feignClient.product.ProductFeignClient;
import com.beini.order.service.OrderDetailService;
import com.beini.order.service.OrderService;
import com.beini.order.vo.PlaceOrderDetailVo;
import com.beini.order.vo.PlaceOrderVo;
import com.beini.product.entity.Product;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/order/order")
@SuppressWarnings("rawtypes")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private ProductFeignClient productFeignClient;
	/**
	 * 根据订单ID获取订单信息
	 * @param id 订单ID
	 * @return 订单信息
	 */
	@ApiOperation(value="根据订单ID获取订单信息")
	@GetMapping("{id}")
	public ResultVO findById(@PathVariable(value = "id") String id,
			@RequestParam(required=false,defaultValue="0",name="pageNo")int pageNo,
			@RequestParam(required=false,defaultValue="10",name="pageSize")int pageSize) {
		Order order = orderService.findById(id);
		Map<String ,Object> model = new HashMap<String,Object>();
		model.put("order", order);
		//ResultVO productVo = productFeignClient.findById(id);
		//System.out.println(product+"---------------订单模块的商品信息");
		//System.out.println("我是商品模块的findById： findById(11);  "+productVo);
		//model.put("product", productVo.getData());
		
		Page<OrderDetail> orderDetails = orderDetailService.findAll(id,new PageRequest(pageNo,pageSize));
		model.put("orderDetails", orderDetails);
		return ResultVOUtil.success(model);
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
	/*事务  引用javax.transaction.Transactional 的事务注解*/
	@Transactional
	@PostMapping
	public ResultVO save(PlaceOrderVo placeOrderVo) throws Exception {
		/*用户编号*/
		String openId = placeOrderVo.getOpenId();
		/**此处应该根据客户ID查询客户详情*/
		/*运费金额*/
		double logisticsFee = placeOrderVo.getLogisticsFee();
		/*店铺ID*/
		String storeId = placeOrderVo.getStoreId();
		String addressNo = placeOrderVo.getAddressNo();
		/**此处应该根据客户ID和地址编号查询地址详情*/
		/*下单详情*/
		List<PlaceOrderDetailVo> details = placeOrderVo.getDetails();
		/*生成订单ID*/
		String orderId = UUID.randomUUID().toString().replaceAll("-", "");
		List<OrderDetail> detailList = new ArrayList<>();
		/*金额合计*/
		double totalPrice=0;
		/*数量合计*/
		int totalCount = 0;
		
		for(PlaceOrderDetailVo detailVo :details) {
			double tempTotalPrice = 0;
			double tempTotalRatePrice = 0;
			/*查询商品--数量、价格*/
			ResultVO productVo = productFeignClient.findById(detailVo.getProUuid());
			Product product = new Product();
			if(productVo== null || productVo.getData()== null) {
				return ResultVOUtil.error(ResultEnum.PRODUCT_NOT_EXIST);
			}else {
				product = (Product) productVo.getData();
			}
			tempTotalPrice = product.getMaxPrice()*detailVo.getNumber();
			tempTotalPrice -= tempTotalRatePrice;
			/*得到对应总价*/
			totalCount +=detailVo.getNumber();
			totalPrice+=tempTotalPrice;
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderDetailUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			orderDetail.setNumber(detailVo.getNumber());
			orderDetail.setProUuid(product.getProUuid());
			orderDetail.setOrderUuid(orderId);
			/**商品规格和备注字段暂先省略*/
			detailList.add(orderDetail);
			/*计算总价*/
			orderDetail.setProductPrice(tempTotalPrice);
			//tempTotalRatePrice = tempTotalPrice*1;
			orderDetail.setDiscountAmount(tempTotalRatePrice);
			/*暂时保存订单详情数据到列表*/
			detailList.add(orderDetail);
		}
		totalPrice -= logisticsFee;
		/*写入订单数据库--订单总表和订单详情表*/
		Order order = new Order();
		order.setCreateTime(new Date());
		order.setOpenId(openId);
		order.setLogisticsFee(logisticsFee);
		order.setOrderAmountTotal(totalPrice);
		order.setOrderUuid(orderId);
		order.setShopUuid(storeId);
		/**默认将支付状态设置为未支付-0*/
		order.setOrderStatus(0);
		order.setProductAmountTotal(totalCount);
		order.setDeAdUuid(addressNo);
		/**--------------------------开始更新操作---------------------------------------**/
		/*订单总表写入数据库*/
		if (orderService.save(order) == null) {
			throw new GlobalException(ResultEnum.ORDER_DETAIL_INSERT_FAIL);
		} else {
			/*订单详情表写入数据库*/
			for(OrderDetail od :detailList) {
				OrderDetail od1 = orderDetailService.save(od);
				if(od1== null) {
					throw new GlobalException(ResultEnum.ORDER_DETAIL_INSERT_FAIL);
				}
			}
		}
		/*扣库存*/
		return deductingInventory(details);
	}
	/**
	 * 扣减库存
	 * @param details 下单明细信息
	 * @return
	 */
	private ResultVO deductingInventory (List<PlaceOrderDetailVo> details) {
		for(PlaceOrderDetailVo detailVo :details) {
			ResultVO rv = productFeignClient.updateStock(detailVo.getProUuid(),detailVo.getNumber());
			if(rv.getCode()!= ResultEnum.SUCCESS.getCode()) {
				throw new GlobalException(ResultEnum.PRODUCT_STOCK_ERROR);
			}
		}
		return ResultVOUtil.success();
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
