/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public final class NacosConfigUtil {

    public static final String GROUP_ID = "SENTINEL_GROUP";
    
    /***
     * 流控规则
     */
    public static final String FLOW_RULE_DATA_ID_POSTFIX = "-flow-rules.json";
    /***
     * 参数限流规则
     */
    public static final String PARAM_FLOW_RULE_DATA_ID_POSTFIX = "-param-flow-rules.json";
    
    /***
     *系统规则
     */
    public static final String SYSTEM_RULE_DATA_ID_POSTFIX = "-system-rules.json";
    
    
    /***
     *降级规则
     */
    public static final String DEGRADE_RULE_DATA_ID_POSTFIX = "-degrade-rules.json";
    
    public static final String CLUSTER_MAP_DATA_ID_POSTFIX = "-cluster-map";

    /**
     * cc for `cluster-client`
     */
    public static final String CLIENT_CONFIG_DATA_ID_POSTFIX = "-cc-config";
    /**
     * cs for `cluster-server`
     */
    public static final String SERVER_TRANSPORT_CONFIG_DATA_ID_POSTFIX = "-cs-transport-config";
    public static final String SERVER_FLOW_CONFIG_DATA_ID_POSTFIX = "-cs-flow-config";
    public static final String SERVER_NAMESPACE_SET_DATA_ID_POSTFIX = "-cs-namespace-set";

    private NacosConfigUtil() {}
    
    public static String listToJson(List<? extends RuleEntity> list) {
        return JSON.toJSONString(list);
    }
    
    public static <T> List<T> jsonToList(String rules,Class<T> clazz) {
    	 if (StringUtil.isEmpty(rules)) {
             return new ArrayList<>();
         }
        return JSON.parseArray(rules, clazz);
    }
}
