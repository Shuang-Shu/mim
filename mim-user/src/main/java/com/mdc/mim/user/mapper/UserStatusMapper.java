package com.mdc.mim.user.mapper;

import com.mdc.mim.user.entity.UserStatusEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/11/20 17:04
 */
@Mapper
@Repository
public interface UserStatusMapper {
    //    @Insert("INSERT INTO user_status (uid, status, lastOnlineTime, doNotify) VALUES (#{uid}, #{status}, #{lastOnlineTime}, #{doNotify})")
    int insertUserStatus(UserStatusEntity userStatusEntity);

    int insertUserStatuses(List<UserStatusEntity> userStatusEntities);

    @Select("SELECT * FROM user_status WHERE uid = #{uid}")
    UserStatusEntity findByUid(@Param("uid") Long uid);

    List<UserStatusEntity> findByUids(List<Long> uids);

    @Select("SELECT * FROM user_status")
    List<UserStatusEntity> findAll();

    @Delete("DELETE FROM user_status WHERE uid = #{uid}")
    int deleteByUid(@Param("uid") Long uid);

    int deleteByUids(List<Long> uids);

    @Update("UPDATE user_status SET status = #{status}, lastOnlineTime = #{lastOnlineTime}, doNotify = #{doNotify} WHERE uid = #{uid}")
    int updateUserStatus(UserStatusEntity userStatusEntity);

    int updateUserStatuses(List<UserStatusEntity> userStatusEntities);
}
