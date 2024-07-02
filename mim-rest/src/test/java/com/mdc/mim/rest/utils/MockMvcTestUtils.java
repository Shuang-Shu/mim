package com.mdc.mim.rest.utils;

import com.mdc.mim.common.dto.R;
import com.mdc.mim.common.utils.JsonUtils;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

public class MockMvcTestUtils {
    public static R mockPerformPost(MockMvc mvc, MockHttpSession session, String url,
            Map<String, String> params) throws Exception {
        var req = MockMvcRequestBuilders.post(url).session(session);
        for (var key : params.keySet()) {
            req.param(key, String.valueOf(params.get(key)));
        }
        return JsonUtils.parse(((MvcResult) mvc.perform(req).andReturn()).getResponse().getContentAsString(), R.class);
    }
}
