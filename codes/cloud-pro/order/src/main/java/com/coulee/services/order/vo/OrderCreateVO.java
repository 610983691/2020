package com.coulee.services.order.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderCreateVO {
    /**
     * 订单号
     */
    private Long id;

    /**
     * 订单创建时间
     */
    private LocalDateTime createAt;

    /***
     * 订单总价
     */
    private BigDecimal totalPrice;
}
