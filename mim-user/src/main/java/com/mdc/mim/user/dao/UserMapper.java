package com.mdc.mim.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mdc.mim.user.domain.User;

// 整体顺序遵循：增查删改，同一类别先单个后批量，按条件字段在表中的顺序排列
@Mapper
public interface UserMapper {
    // 插入使用insert+实体类名
    public int insertUser(User user);

    public int insertUsers(List<User> users);

    // 查找使用 findBy+属性名(s)
    public List<User> findByUids(List<Long> uids);

    @Select("select * from user where `username` = #{username}")
    public User findByName(String username);

    public List<User> findByNames(List<String> userNames);

    // 无条件查找使用findAll
    @Select("select * from `user`")
    public List<User> findAll();

    // 删除使用delete+属性名(s)
    public int deleteByIds(List<Long> uids);

    @Delete("delete from `user` where `username` = #{userName}")
    public int deleteByName(String userName);

    public int deleteByNames(List<String> userNames);

    // 更新使用update+实体类名(s)
    @Update("update user set `username` = #{userName}, `passwd_md5` = #{passwdMd5}, `register_date` = #{registerDate} where `id` = #{id}")
    public int updateUser(User user);

    public int updateUsers(List<User> users);
}
