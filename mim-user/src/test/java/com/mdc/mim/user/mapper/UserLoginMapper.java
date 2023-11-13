package com.mdc.mim.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.mdc.mim.user.entity.UserEntity;

@Mapper
@Repository
public interface UserLoginMapper {
    // 查询
    public List<UserEntity> queryAll();

    // 添加数据
    public int add(UserEntity userLogin);

    // 根据用户名查询数据
    public UserEntity queryByName(String userName);
}
