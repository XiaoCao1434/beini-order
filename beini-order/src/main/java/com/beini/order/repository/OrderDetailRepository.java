package com.beini.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beini.order.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String>, JpaSpecificationExecutor<OrderDetail> {
	
	@Query(value="from OrderDetail detail where detail.orderUuid=:orderUuid")
	Page<OrderDetail> findAllByOrderUuid(@Param("orderUuid")String orderUuid, Pageable pageable);

}
