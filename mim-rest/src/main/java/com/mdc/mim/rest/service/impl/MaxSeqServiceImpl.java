package com.mdc.mim.rest.service.impl;

import com.mdc.mim.rest.entity.MaxSeqEntity;
import com.mdc.mim.rest.mapper.MaxSeqMapper;
import com.mdc.mim.rest.service.MaxSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 18:20
 */
@Service
public class MaxSeqServiceImpl implements MaxSeqService {
    @Autowired
    MaxSeqMapper maxSeqMapper;

    @Override
    public Long getMaxSeq(Long uid) {
        return maxSeqMapper.findByUid(uid).getMaxSeq();
    }

    @Override
    public int updateMaxSeq(Long uid, Long maxSeq) {
        return maxSeqMapper.updateMaxSeq(uid, maxSeq);
    }

    @Override
    public List<MaxSeqEntity> findAll() {
        return maxSeqMapper.findAll();
    }

    @Override
    public int updateMaxSeqs(List<MaxSeqEntity> maxSeqs) {
        return maxSeqMapper.updateMaxSeqs(maxSeqs);
    }
}
