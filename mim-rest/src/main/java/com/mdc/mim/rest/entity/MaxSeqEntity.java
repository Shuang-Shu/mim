package com.mdc.mim.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 18:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaxSeqEntity {
    private Long uid;
    private Long maxSeq;
}
