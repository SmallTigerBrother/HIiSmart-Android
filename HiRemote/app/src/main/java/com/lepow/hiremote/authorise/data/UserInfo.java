package com.lepow.hiremote.authorise.data;

import java.io.Serializable;

/**
 * Created by Dalang on 2015/8/19.
 */
public class UserInfo implements Serializable
{
    private int id;

    private String userName;

    private String token;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
