package com.mdc.mim.user.mapper;

import com.mdc.mim.user.entity.FriendEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/21 22:20
 */
@Mapper
@Repository
public interface FriendEntityMapper {
    // 生成相关接口
    @Insert("INSERT INTO friend (uid, friendUid, createTime) VALUES (#{uid}, #{friendUid}, #{createTime})")
    int insertFriend(FriendEntity friendEntity);

    int insertFriends(List<FriendEntity> friendEntities);

    @Select("SELECT * FROM friend")
    List<FriendEntity> findAll();

    @Select("SELECT * FROM friend WHERE uid = #{uid} AND friendUid = #{friendUid}")
    FriendEntity findByUidAndFriendUid(@Param("uid") Long uid, @Param("friendUid") Long friendUid);

    @Delete("DELETE FROM friend WHERE uid = #{uid} AND friendUid = #{friendUid}")
    int deleteByUidAndFriendUid(@Param("uid") Long uid, @Param("friendUid") Long friendUid);

    int deleteByUids(List<Long> uids);

    @Update("UPDATE friend SET friendUid = #{friendUid}, createTime = #{createTime} WHERE uid = #{uid} AND friendUid = #{friendUid}")
    int updateFriend(FriendEntity friendEntity);

    int updateFriends(List<FriendEntity> friendEntities);
}
