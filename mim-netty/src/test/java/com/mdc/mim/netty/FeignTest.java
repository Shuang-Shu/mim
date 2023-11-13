package com.mdc.mim.netty;

import com.mdc.mim.common.utils.DigestUtils;
import com.mdc.mim.netty.feign.UserLoginService;
import org.bouncycastle.crypto.Digest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/13 22:48
 */
@SpringBootTest
public class FeignTest {
    @Autowired
    private UserLoginService userLoginService;

    @Test
    public void basicTest() {
        Assertions.assertNotNull(userLoginService);
    }

    @Test
    public void feignRpcTest() {
        var r = userLoginService.identify("shuangshu", DigestUtils.md5("12345"));
        Assertions.assertEquals(true, r.get("valid"));
        r = userLoginService.identify("shuangshu1", DigestUtils.md5("12345"));
        Assertions.assertEquals(false, r.get("valid"));
    }
}
