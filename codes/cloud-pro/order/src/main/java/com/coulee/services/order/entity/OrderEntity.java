package com.coulee.services.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 
 * @author zitong
 * @date 2020/09/03
 */
@Data
@TableName("t_order")
public class OrderEntity {

    /**
     * 订单号
     */
    private Long id;

    /***
     * 创建订单的用户
     */
    private Long createUserId;

    /**
     * 订单创建时间
     */
    private LocalDateTime createAt;

    /***
     * 取消订单的时间
     */
    private LocalDateTime cancelAt;

    /**
     * 订单商品信息：单价（￥）
     */
    private BigDecimal unitPrice;

    /**
     * 商品库存ID：
     */
    private Long storageId;

    /***
     * 商品数量
     */
    private Long amount;

    /***
     * 订单总价：totalPrice =unitPrice * amount
     */
    private BigDecimal totalPrice;
}
