package com.mdc.mim.rest.controller;

import com.mdc.mim.common.dto.R;
import com.mdc.mim.rest.entity.MaxSeqEntity;
import com.mdc.mim.rest.service.MaxSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 18:22
 */
@RestController
public class MaxSeqController {
    @Autowired
    private MaxSeqService maxSeqService;

    @PostMapping("/max-seq/get")
    public R getMaxSeq(Long uid) {
        try {
            return R.ok().put("maxSeq", maxSeqService.getMaxSeq(uid));
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/max-seq/update")
    public R updateMaxSeq(@RequestParam("uid") Long uid, @RequestParam("maxSeq") Long maxSeq) {
        try {
            maxSeqService.updateMaxSeq(uid, maxSeq);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/max-seq/find-all")
    public R findAll() {
        try {
            return R.ok().put("maxSeqs", maxSeqService.findAll());
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/max-seq/update-batch")
    public R updateMaxSeqs(@RequestBody List<MaxSeqEntity> maxSeqs) {
        try {
            maxSeqService.updateMaxSeqs(maxSeqs);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
