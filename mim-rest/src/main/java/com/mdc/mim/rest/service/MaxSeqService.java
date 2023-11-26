package com.mdc.mim.rest.service;

import com.mdc.mim.rest.entity.MaxSeqEntity;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 不使用缓存，因为缓存在NettyServer中直接实现
 * @date 2023/11/25 18:19
 */
public interface MaxSeqService {
    public Long getMaxSeq(Long uid);

    public int updateMaxSeq(Long uid, Long maxSeq);

    public List<MaxSeqEntity> findAll();

    public int updateMaxSeqs(List<MaxSeqEntity> maxSeqs);
}
