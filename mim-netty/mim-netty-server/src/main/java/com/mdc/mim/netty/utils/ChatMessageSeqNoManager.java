package com.mdc.mim.netty.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.LRUMap;
import com.mdc.mim.common.dto.R;
import com.mdc.mim.netty.feign.MaxSeqFeignService;
import com.mdc.mim.rest.entity.MaxSeqEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/26 17:07
 */
@Slf4j
@Component
public class ChatMessageSeqNoManager implements InitializingBean {
    private static final int INIT_LRU_SIZE = 1 << 10;
    private static final int MAX_LRU_SIZE = 1 << 16;
    private static final int SEQ_STEP = 1 << 6;
    private LRUMap<Long, Long[]> uidSeqMap = new LRUMap<>(INIT_LRU_SIZE, MAX_LRU_SIZE);

    @Autowired
    private MaxSeqFeignService maxSeqFeignService;

    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {
        // TODO 刷新数据库中所有的maxSeq，这可能造成大量的磁盘IO，未来可使用Segment方式进行优化
        var r = (R) maxSeqFeignService.findAll();
        if ((Integer) r.get("code") != 0) {
            log.error("failed to get maxSeqs from db");
            throw new RuntimeException("failed to get maxSeqs from db");
        } else {
            var objectMapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            List<MaxSeqEntity> maxSeqs = ((List) r.get("maxSeqs")).stream().map(o -> {
                return objectMapper.convertValue(o, MaxSeqEntity.class);
            }).toList();
            // TODO 将maxSeqs中元素使用Jackson转换为MaxSeqEntity对象
            maxSeqs.forEach(maxSeq ->
            {
                uidSeqMap.put(maxSeq.getUid(), new Long[]{maxSeq.getMaxSeq(), maxSeq.getMaxSeq() + SEQ_STEP});// [0]当前seq，[1]最大seq
                maxSeq.setMaxSeq(maxSeq.getMaxSeq() + SEQ_STEP);
                maxSeqFeignService.updateMaxSeqs(List.of(maxSeq));
            });
        }
    }

    public synchronized Long getSeqNo(Long uid) {
        // TODO 此处可能出现并发问题，同一用户的多个客户端同时发起请求，可能导致出现相同的seqNo。目前先简单地使用synchronized进行同步，后续可优化
        var seq = uidSeqMap.get(uid);
        if (seq == null) {
            var r = (R) maxSeqFeignService.getMaxSeq(uid);
            if ((Integer) r.get("code") != 0) {
                log.error("failed to get maxSeq from db with uid: {}", uid);
                throw new RuntimeException("failed to get maxSeq from db");
            } else {
                var maxSeq = (Long) r.get("maxSeq");
                maxSeqFeignService.updateMaxSeq(uid, maxSeq + SEQ_STEP); // 首先更新数据库
                uidSeqMap.put(uid, new Long[]{maxSeq, maxSeq + SEQ_STEP}); // 更新缓存
            }
        }
        seq = uidSeqMap.get(uid);
        var result = seq[0];
        seq[0]++;
        if (seq[0] == seq[1]) {
            maxSeqFeignService.updateMaxSeq(uid, seq[0] + SEQ_STEP);
            seq[1] += SEQ_STEP;
        }
        return result;
    }
}
