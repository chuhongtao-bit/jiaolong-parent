package com.chuhongtao.server;

import com.chuhongtao.dao.MenuDao;
import com.chuhongtao.dao.RoleDao;
import com.chuhongtao.dao.UserDao;
import com.chuhongtao.entity.MenuInfo;
import com.chuhongtao.entity.RoleInfo;
import com.chuhongtao.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleDao roleDao;

    public UserInfo getUserByLogin(String loginName){
        //获取用户信息
        UserInfo byLoginName = userDao.findByName(loginName);
        if (byLoginName!=null){
            RoleInfo releInfoByUserId=roleDao.getInfoUserId(byLoginName.getId());//获取用户角色信息
            byLoginName.setRoleInfo(releInfoByUserId);//设置角色信息
            if (releInfoByUserId!=null){
                List<MenuInfo> firstMenInfo=menuDao.getFirstMenuInfo(releInfoByUserId.getId(),1);
                Map<String,String> authMap=new HashMap<>();
                this.getfirstMenInfo(firstMenInfo,releInfoByUserId.getId(),authMap);
                byLoginName.setAuthmap(authMap);
                byLoginName.setListMenuInfo(firstMenInfo);
            }


        }


        return byLoginName;
    }
    //递归
    public  void getfirstMenInfo(List<MenuInfo> firstMenInfo,Long roleId,Map<String,String> authMap){
for (MenuInfo menuInfo:firstMenInfo){
    int leval = menuInfo.getLeval() + 1;
    List<MenuInfo> firstMenuInfol=menuDao.getFirstMenuInfo(roleId,leval);
    if (firstMenuInfol!=null){

        if (leval==4){
            for (MenuInfo menu:firstMenuInfol){
                authMap.put(menu.getUrl(),"");
            }
        }
        menuInfo.setMenuInfoList(firstMenuInfol);
        getfirstMenInfo(firstMenuInfol,roleId,authMap);
    }else {
        break;
    }


}


    }




}
