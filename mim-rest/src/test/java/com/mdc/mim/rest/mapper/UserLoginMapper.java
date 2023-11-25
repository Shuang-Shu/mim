package com.mdc.mim.rest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.mdc.mim.rest.entity.UserEntity;

@Mapper
@Repository
public interface UserLoginMapper {
    // 查询
    @Select("select * from user")
    public List<UserEntity> queryAll();

    // 添加数据
    public int add(UserEntity userLogin);

    // 根据用户名查询数据
    @Select("select * from user\r\n" + //
            "        where userName = #{userName} ")
    public UserEntity queryByName(String userName);

    @Delete("delete from user where userName = #{userName}")
    public int deleteByName(String userName);

}
