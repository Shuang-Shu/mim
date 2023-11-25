package com.mdc.mim.rest;

import com.github.pagehelper.PageHelper;
import com.mdc.mim.common.constant.ChatMessageTypeConst;
import com.mdc.mim.common.constant.UserStatusConst;
import com.mdc.mim.rest.entity.ChatMessageEntity;
import com.mdc.mim.rest.entity.UserEntity;
import com.mdc.mim.rest.entity.UserStatusEntity;
import com.mdc.mim.rest.mapper.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest
public class MybatisTest {

    /**
     * 数据库内容与`duumy_data/insert_user.sql`应相同
     */
    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserStatusMapper userStatusMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    ChatMessageEntity testChatMessageEntity = ChatMessageEntity.builder().id(1L).fromUid(1L).toUid(2L).type(ChatMessageTypeConst.TEXT).content("hello").createTime(new Date(System.currentTimeMillis())).build();

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
        var users = userMapper.findAll();
        Assertions.assertEquals(4, users.size());
        userMapper.insertUsers(users);
        Assertions.assertNotEquals(1, users.get(0).getUid());
        userMapper.deleteByIds(users.stream().map(u -> u.getUid()).toList());
        users = userMapper.findAll();
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
        var userStatus = UserStatusEntity.builder().status(UserStatusConst.ONLINE).uid(1L).lastOnlineTime(new Date(System.currentTimeMillis())).build();
        var ret = userStatusMapper.insertUserStatus(userStatus);
        Assertions.assertEquals(1, ret);
        userStatus = userStatusMapper.findByUid(1L);
        Assertions.assertEquals(UserStatusConst.ONLINE, userStatus.getStatus());
        userStatus.setStatus(UserStatusConst.OFFLINE);
        ret = userStatusMapper.updateUserStatus(userStatus);
        Assertions.assertEquals(1, ret);
        userStatus = userStatusMapper.findByUid(1L);
        Assertions.assertEquals(UserStatusConst.OFFLINE, userStatus.getStatus());
        ret = userStatusMapper.deleteByUid(1L);
        Assertions.assertEquals(1, ret);
    }

    // friend相关测试
    @Test
    public void testBasicFriends() {
        var friends = friendMapper.findAll();
        Assertions.assertEquals(6, friends.size());
    }

    @Test
    public void testInsertFriends() {
        var friends = friendMapper.findAll();
        var friend = friends.get(0);
        var ret = friendMapper.deleteByUidAndFriendUid(friend.getUid(), friend.getFriendUid());
        Assertions.assertEquals(1, ret);
        ret = friendMapper.insertFriend(friend);
        Assertions.assertEquals(1, ret);
    }

    @Test
    public void testUpdateFriends() {
        var friends = friendMapper.findAll();
        var friend = friends.get(0);
        var oldCreateTime = friend.getCreateTime();
        var newCreateTime = new Date(System.currentTimeMillis());
        friend.setCreateTime(newCreateTime);
        var ret = friendMapper.updateFriend(friend);
        Assertions.assertEquals(1, ret);
        friend = friendMapper.findByUidAndFriendUid(friend.getUid(), friend.getFriendUid());
        Assertions.assertNotEquals(newCreateTime, friend.getCreateTime());
        friend.setCreateTime(oldCreateTime);
        ret = friendMapper.updateFriend(friend);
        Assertions.assertEquals(1, ret);
    }

    @Test
    public void testUserStatusEntityMapper() {
        var userStatuses = userStatusMapper.findAll();
        System.out.println(userStatuses);
    }

    @Test
    public void testChatMessageMapper() {
        var ret = chatMessageMapper.insertChatMessage(testChatMessageEntity);
        Assertions.assertEquals(1, ret);
        var chatMessage = chatMessageMapper.findById(1L);
        Assertions.assertEquals("hello", chatMessage.getContent());
        ret = chatMessageMapper.deleteById(1L);
        Assertions.assertEquals(1, ret);
    }
}
