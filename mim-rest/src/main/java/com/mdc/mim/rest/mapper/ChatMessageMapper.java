package com.mdc.mim.rest.mapper;

import com.mdc.mim.rest.entity.ChatMessageEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/25 12:16
 */
@Mapper
@Repository
public interface ChatMessageMapper {
    // 根据ID查询聊天记录
    @Select("SELECT * FROM chat_message WHERE id = #{id}")
    ChatMessageEntity findById(@Param("id") Long id);

    @Select("SELECT * FROM chat_message WHERE fromUid = #{fromUid} AND toUid = #{toUid}")
    List<ChatMessageEntity> findByFromUidAndToUid(@Param("fromUid") Long fromUid, @Param("toUid") Long toUid);

    // 查询所有聊天记录
    @Select("SELECT * FROM ChatMessage")
    List<ChatMessageEntity> findAll();

    // 插入聊天记录
    @Insert("INSERT INTO chat_message(id, fromUid, toUid, type, content, createTime) " +
            "VALUES (#{id}, #{fromUid}, #{toUid}, #{type}, #{content}, #{createTime})")
    int insertChatMessage(ChatMessageEntity chatMessage);

    // 更新聊天记录
    @Update("UPDATE chat_message SET fromUid = #{fromUid}, toUid = #{toUid}, type = #{type}, " +
            "content = #{content}, createTime = #{createTime} WHERE id = #{id}")
    int updateChatMessage(ChatMessageEntity chatMessage);

    // 删除聊天记录
    @Delete("DELETE FROM chat_message WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
