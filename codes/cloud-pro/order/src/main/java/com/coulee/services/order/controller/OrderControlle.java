package com.coulee.services.order.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.cloud.base.Result;
import com.coulee.cloud.common.SnowFlake;
import com.coulee.services.order.dao.OrderMapper;
import com.coulee.services.order.entity.OrderEntity;
import com.coulee.services.order.vo.OrderCreateVO;

import lombok.extern.slf4j.Slf4j;

/***
 * 
 * @author zitong
 * @date 2020/09/03
 */
@Slf4j
@RestController
public class OrderControlle {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SnowFlake snowFlakeId;

    @RequestMapping("/create/{storageId}/{count}")
    public Result<OrderCreateVO> create(@PathVariable("storageId") Long storageId,
        @PathVariable("count") Integer count) {
        if (storageId == null) {
            return Result.ofFail(-1, "订单不能为空");
        }
        if (count == null || count < 1) {
            return Result.ofFail(-1, "数量不能小于1");
        }

        OrderEntity order = new OrderEntity();
        order.setId(snowFlakeId.nextId());
        order.setStorageId(storageId);
        order.setAmount(count);
        order.setCreateAt(LocalDateTime.now());
        order.setUnitPrice(new BigDecimal(12.88));
        order.setTotalPrice(order.getUnitPrice().multiply(order.getUnitPrice()));
        orderMapper.insert(order);// 这里有db
        log.info("创建订单：{}, {}", getMsg(), order);
        OrderCreateVO vo = new OrderCreateVO();
        vo.setCreateAt(order.getCancelAt());
        vo.setId(order.getId());
        vo.setTotalPrice(order.getTotalPrice());// 测试就输出这么点儿东西给前端
        return Result.ofSuccess(vo);
    }

    private String getMsg() {
        String rst = context.getEnvironment().getProperty("spring.application.name") + ":"
            + context.getEnvironment().getProperty("local.server.port");
        return rst;
    }

}
