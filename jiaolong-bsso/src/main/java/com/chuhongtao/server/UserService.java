package com.chuhongtao.server;

import com.chuhongtao.dao.UserDao;
import com.chuhongtao.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public UserInfo getUserByLogin(String loginName){
        //获取用户信息
        UserInfo byLoginName = userDao.findByName(loginName);
        return byLoginName;
    }
}
