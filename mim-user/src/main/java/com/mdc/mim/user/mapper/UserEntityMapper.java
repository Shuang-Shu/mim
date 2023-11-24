package com.mdc.mim.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.mdc.mim.user.entity.UserEntity;

// 整体顺序遵循：增查删改，同一类别先单个后批量，按条件字段在表中的顺序排列
@Mapper
@Repository
public interface UserEntityMapper {
    // 插入使用insert+实体类名
    public int insertUser(UserEntity user);

    public int insertUsers(List<UserEntity> users);

    // 查找使用 findBy+属性名(s)
    public List<UserEntity> findByUids(List<Long> uids);

    @Select("select * from user where userName = #{userName}")
    public UserEntity findByName(String userName);

    public List<UserEntity> findByNames(List<String> userNames);

    // 无条件查找使用findAll
    @Select("select * from user")
    public List<UserEntity> findAll();

    // 删除使用delete+属性名(s)
    public int deleteByIds(List<Long> uids);

    @Delete("delete from user where userName = #{userName}")
    public int deleteByName(String userName);

    public int deleteByNames(List<String> userNames);

    // 更新使用update+实体类名(s)
    @Update(
            "update user set userName = #{userName}, passwdMd5 = #{passwdMd5}, nickName = #{nickName}, registerDate = #{registerDate} where uid = #{uid}"
    )
    public int updateUser(UserEntity user);

    public int updateUsers(List<UserEntity> users);
}
