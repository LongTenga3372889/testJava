package com.test.test.netty.dto;

import org.msgpack.annotation.Message;

import java.io.Serializable;

@Message
public class UserInfo {

    private int age;

    private String userName;

    public static UserInfo[] userInfos(int count){
        UserInfo[] userInfos = new UserInfo[count];
        UserInfo userInfo = null;
        for (int i = 0;i<count;i++) {
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setUserName("U"+i);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }

    public int getAge() {
        return age;
    }

    public UserInfo setAge(int age) {
        this.age = age;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
