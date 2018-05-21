package com.beini.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beini.order.entity.DeliveryAddress;

public interface DeliveryAddressRepository extends JpaSpecificationExecutor<DeliveryAddress>, JpaRepository<DeliveryAddress, String> {
	@Query(value="select da from DeliveryAddress da where da.openId=:openId")
	Page<DeliveryAddress> findAllByOpenId(@Param("openId")String openId, Pageable request);

}
