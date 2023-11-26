package com.mdc.mim.rest.mapper;

import com.mdc.mim.rest.entity.MaxSeqEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 18:12
 */
@Mapper
@Repository
public interface MaxSeqMapper {
    // 插入一条新记录
    @Insert("INSERT INTO max_seq (uid, maxSeq) VALUES (#{uid}, #{maxSeq})")
    void insertMaxSeq(MaxSeqEntity maxSeqEntity);

    // 根据uid更新maxSeq
    @Update("UPDATE max_seq SET maxSeq = #{maxSeq} WHERE uid = #{uid}")
    int updateMaxSeq(@Param("uid") Long uid, @Param("maxSeq") Long maxSeq);

    int updateMaxSeqs(@Param("maxSeqs") List<MaxSeqEntity> maxSeqs);

    @Select("SELECT * FROM max_seq")
    List<MaxSeqEntity> findAll();

    // 根据uid查询maxSeq
    @Select("SELECT * FROM max_seq WHERE uid = #{uid}")
    MaxSeqEntity findByUid(@Param("uid") Long uid);

    // 查询所有的记录
    @Select("SELECT * FROM max_seq")
    List<MaxSeqEntity> selectAll();
}
