package com.coulee.services.storage.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_storage")
public class StorageEntity {

	/**
     * 商品主键号
     */
    private Long id;


    /**
     * 商品名
     */
    private String name;
    
    /**
     * 数据创建时间
     */
    private LocalDateTime createAt;
    
    /**
     * 商品剩余数量
     */
    private Long amount;
    
    /**
     * 商品单价
     */
    private BigDecimal unitPrice;
    
    /**
     * 数据更新时间
     */
    private LocalDateTime updateAt;
}
