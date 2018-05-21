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
import com.beini.order.entity.DeliveryAddress;
import com.beini.order.service.DeliveryAddressService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="收货地址信息操作")
@RestController
@RequestMapping("/order/deliveryAddress")
@SuppressWarnings("rawtypes")
public class DeliveryAddressController {
	@Autowired
	private DeliveryAddressService deliveryAddressService;

	/**
	 * 根据收货地址信息ID获取收货地址信息
	 * 
	 * @param id
	 *            收货地址信息ID
	 * @return 收货地址信息
	 */
	@ApiOperation(value = "根据收货地址编码获取收货地址信息")
	@GetMapping("/{id}")
	public ResultVO findById(@PathVariable(value = "id") String id) {
		DeliveryAddress deliveryAddress = deliveryAddressService.findById(id);
		return ResultVOUtil.success(deliveryAddress);
	}

	@ApiOperation(value = "根据分页信息获取收货地址分页信息")
	@GetMapping("/")
	public ResultVO findByPage(@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		PageRequest request = new PageRequest(pageNo - 1, pageSize);
		Page<DeliveryAddress> page = deliveryAddressService.findAll(request);
		return ResultVOUtil.success(page);
	}
	@ApiOperation(value = "根据openId和分页信息获取收货地址分页信息")
	@GetMapping("/openId/{openId}")
	public ResultVO findPageByOpenId(
			@RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(name = "openId", required = true)String openId) {
		PageRequest request = new PageRequest(pageNo - 1, pageSize);
		Page<DeliveryAddress> page = deliveryAddressService.findAllByOpenId(openId,request);
		return ResultVOUtil.success(page);
	}
	
	@ApiOperation(value = "根据收货地址信息进行数据更新(以主键为依据)")
	@PutMapping("")
	public ResultVO update(DeliveryAddress deliveryAddress) {
		if (deliveryAddress == null || deliveryAddress.getDeAdUuid() == null || "".equals(deliveryAddress.getDeAdUuid())) {
			return ResultVOUtil.error(ResultEnum.DELIVERY_ADDRESS_NOT_EXIST);
		}
		if (deliveryAddressService.update(deliveryAddress) == null) {
			return ResultVOUtil.error(ResultEnum.DELIVERY_ADDRESS_UPDATE_FAIL);
		} else {
			return ResultVOUtil.success();
		}
	}

	@ApiOperation(value = "增加收货地址信息")
	@PostMapping("")
	public ResultVO save(DeliveryAddress deliveryAddress) {
		if (deliveryAddressService.save(deliveryAddress) == null) {
			return ResultVOUtil.error(ResultEnum.DELIVERY_ADDRESS_INSERT_FAIL);
		} else {
			return ResultVOUtil.success();
		}
	}

	@ApiOperation(value = "根据收货地址信息ID删除收货地址信息")
	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable(value = "id") String id) {
		try {
			deliveryAddressService.delete(id);
			return ResultVOUtil.success();
		} catch (Exception e) {
			return ResultVOUtil.error(ResultEnum.DELIVERY_ADDRESS_DELETE_FAIL);
		}
	}
}
