package com.mdc.mim.user.dao;

import org.apache.ibatis.annotations.*;

import com.mdc.mim.user.domain.UserStatus;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/20 17:04
 */
@Mapper
public interface UserStatusMapper {
    int insertUserStatus(UserStatus userStatusEntity);

    int insertUserStatuses(List<UserStatus> userStatusEntities);

    @Select("SELECT * FROM user_status WHERE uid = #{uid}")
    UserStatus findByUid(@Param("uid") Long uid);

    List<UserStatus> findByUids(List<Long> uids);

    @Select("SELECT * FROM user_status")
    List<UserStatus> findAll();

    @Delete("DELETE FROM user_status WHERE uid = #{uid}")
    int deleteByUid(@Param("uid") Long uid);

    int deleteByUids(List<Long> uids);

    @Update("UPDATE user_status SET status = #{status}, `do_notify` = #{doNotify} WHERE uid = #{uid}")
    int updateUserStatus(UserStatus userStatusEntity);

    int updateUserStatuses(List<UserStatus> userStatusEntities);
}
