package com.mdc.mim.netty.feign;

import com.mdc.mim.common.dto.R;
import com.mdc.mim.rest.entity.MaxSeqEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 18:11
 */
@FeignClient(value = "mim-rest", contextId = "max-seq-service")
public interface MaxSeqFeignService {
    @PostMapping("/max-seq/get")
    public R getMaxSeq(Long uid);

    @PostMapping("/max-seq/update")
    public R updateMaxSeq(@RequestParam("uid") Long uid, @RequestParam("maxSeq") Long maxSeq);

    @PostMapping("/max-seq/find-all")
    public R findAll();

    @PostMapping("/max-seq/update-batch")
    public R updateMaxSeqs(@RequestBody List<MaxSeqEntity> maxSeqs);
}
