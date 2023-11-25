package com.mdc.mim.rest.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.stereotype.Controller;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/13 21:27
 */
@Controller
public class TestController {
    @NacosInjected
    NamingService namingService;

}
