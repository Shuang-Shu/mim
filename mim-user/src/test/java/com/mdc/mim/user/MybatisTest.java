package com.mdc.mim.user;

import java.sql.Date;

import com.mdc.mim.common.constant.UserStatusEnum;
import com.mdc.mim.user.entity.UserStatusEntity;
import com.mdc.mim.user.mapper.FriendEntityMapper;
import com.mdc.mim.user.mapper.UserStatusMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.pagehelper.PageHelper;
import com.mdc.mim.user.entity.UserEntity;
import com.mdc.mim.user.mapper.UserEntityMapper;
import com.mdc.mim.user.mapper.UserLoginMapper;

@SpringBootTest
public class MybatisTest {

    /**
     * 数据库内容与`duumy_data/insert_user.sql`应相同
     */
    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private UserEntityMapper userEntityMapper;
    @Autowired
    private UserStatusMapper userStatusMapper;
    @Autowired
    private FriendEntityMapper friendEntityMapper;

    @Test
    public void basicTest() {
        Assertions.assertNotNull(userLoginMapper);
    }

    @Test
    public void testQuery() {
        var user = userLoginMapper.queryByName("shuangshu");
        Assertions.assertEquals("827ccb0eea8a706c4c34a16891f84e7b", user.getPasswdMd5());
        var users = userLoginMapper.queryAll();
        Assertions.assertEquals(4, users.size());
    }

    @Test
    public void testAdd() {
        var user = UserEntity.builder().userName("test").passwdMd5("827ccb0eea8a706c4c34a16891f84e7b")
                .nickName("test_nickname")
                .registerDate(new Date(System.currentTimeMillis())).build();
        var ret = userLoginMapper.add(user);
        Assertions.assertEquals(1, ret);
        ret = userLoginMapper.deleteByName("test");
        Assertions.assertEquals(1, ret);
    }

    @Test
    public void testPage() {
        PageHelper.startPage(2, 2);
        var users = userLoginMapper.queryAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void testUserEntityMapper() {
        var users = userEntityMapper.findAll();
        Assertions.assertEquals(4, users.size());
        userEntityMapper.insertUsers(users);
        Assertions.assertNotEquals(1, users.get(0).getUid());
        userEntityMapper.deleteByIds(users.stream().map(u -> u.getUid()).toList());
        users = userEntityMapper.findAll();
        Assertions.assertEquals(4, users.size());
    }

    @Test
    public void testBasicUserStatus() {
        var ret = userStatusMapper.findAll();
        Assertions.assertEquals(0, ret.size());
    }

    // userStatus相关测试
    @Test
    public void testUserStatusMapper() {
        var userStatus = UserStatusEntity.builder().status(UserStatusEnum.ONLINE).uid(1L).lastOnlineTime(new Date(System.currentTimeMillis())).build();
        var ret = userStatusMapper.insertUserStatus(userStatus);
        Assertions.assertEquals(1, ret);
        userStatus = userStatusMapper.findByUid(1L);
        Assertions.assertEquals(UserStatusEnum.ONLINE, userStatus.getStatus());
        userStatus.setStatus(UserStatusEnum.OFFLINE);
        ret = userStatusMapper.updateUserStatus(userStatus);
        Assertions.assertEquals(1, ret);
        userStatus = userStatusMapper.findByUid(1L);
        Assertions.assertEquals(UserStatusEnum.OFFLINE, userStatus.getStatus());
        ret = userStatusMapper.deleteByUid(1L);
        Assertions.assertEquals(1, ret);
    }

    // friend相关测试
    @Test
    public void testBasicFriends() {
        var friends = friendEntityMapper.findAll();
        Assertions.assertEquals(6, friends.size());
    }

    @Test
    public void testInsertFriends() {
        var friends = friendEntityMapper.findAll();
        var friend = friends.get(0);
        var ret = friendEntityMapper.deleteByUidAndFriendUid(friend.getUid(), friend.getFriendUid());
        Assertions.assertEquals(1, ret);
        ret = friendEntityMapper.insertFriend(friend);
        Assertions.assertEquals(1, ret);
    }

    @Test
    public void testUpdateFriends() {
        var friends = friendEntityMapper.findAll();
        var friend = friends.get(0);
        var oldCreateTime = friend.getCreateTime();
        var newCreateTime = new Date(System.currentTimeMillis());
        friend.setCreateTime(newCreateTime);
        var ret = friendEntityMapper.updateFriend(friend);
        Assertions.assertEquals(1, ret);
        friend = friendEntityMapper.findByUidAndFriendUid(friend.getUid(), friend.getFriendUid());
        Assertions.assertNotEquals(newCreateTime, friend.getCreateTime());
        friend.setCreateTime(oldCreateTime);
        ret = friendEntityMapper.updateFriend(friend);
        Assertions.assertEquals(1, ret);
    }
}
