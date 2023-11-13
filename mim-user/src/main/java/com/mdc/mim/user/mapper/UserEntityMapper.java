package com.mdc.mim.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.mdc.mim.user.entity.UserEntity;

@Mapper
@Repository
public interface UserEntityMapper {
    public int add(UserEntity user);

    public int addAll(List<UserEntity> users);

    @Select("select * from user where userName = #{userName}")
    public UserEntity queryByName(String userName);

    @Select("select * from user where userName in (#{userNames})")
    public List<UserEntity> queryByNames(List<String> userNames);

    @Select("select * from user where uid = #{uid}")
    public List<UserEntity> queryByIds(List<Long> uids);

    @Select("select * from user")
    public List<UserEntity> queryAll();

    @Delete("delete from user where userName = #{userName}")
    public int deleteByName(String userName);

    @Delete("delete from user where userName in (#{userNames})")
    public int deleteByNames(List<String> userNames);

    public int deleteByIds(List<Long> uids);
}
