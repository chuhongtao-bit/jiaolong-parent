package com.chuhongtao.web;

import com.alibaba.fastjson.JSON;
import com.chuhongtao.ResponseResult;
import com.chuhongtao.entity.UserInfo;
import com.chuhongtao.exception.LoginException;
import com.chuhongtao.jwt.JWTUtils;
import com.chuhongtao.randm.VerifyCodeUtils;
import com.chuhongtao.server.UserService;
import com.chuhongtao.utils.MD5;
import com.chuhongtao.utils.UID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller

public class LoginController {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserService userService;
    //登录验证
    @ResponseBody
    @RequestMapping("login")
    public ResponseResult tologin(@RequestBody Map<String,Object> map)throws LoginException {
        System.out.println("进来了");
        System.out.println(map.get("loginname").toString()+"88888888888888888888");
        System.out.println(map.get("password").toString()+"88888888888888888888");

           ResponseResult responseResult=ResponseResult.getResponseResult();
           String code=redisTemplate.opsForValue().get(map.get("codekey").toString());//获取redis中验证码是否生成
        if (code==null||code.equals(map.get("code").toString())){//判断
            responseResult.setCode(500);//存储返回错误信息
            responseResult.setError("验证码错误，请重新尝试");
            return responseResult;
        }
      if (map!=null&&map.get("loginname")!=null){
          UserInfo user=userService.getUserByLogin(map.get("loginname").toString());//根据用户名获取信息
          if (user!=null){
              String password = MD5.encryptPassword(map.get("password").toString(),"lcg");//将获取的密码进行MD5加密
              if (user.getPassword().equals(password)){
                  String userinfo = JSON.toJSONString(user);//将用户信息转存Json串
                  String token=JWTUtils.generateToken(userinfo);//将用户信息使用JWT进行加密
                  responseResult.setToken(token);//将加密信息存入自定义实体类中
                  redisTemplate.opsForValue().set("USERINFO"+user.getId().toString(),token);//将token存入redis
                 /* redisTemplate.opsForHash().putAll("USERDATAAUTH"+user.getId().toString(),user.getAuthmap());*///将该用户的数据访问权限信息存入缓存中
                  redisTemplate.expire("USERINFO"+user.getId().toString(),600,TimeUnit.SECONDS);//设置token的过期时间
                  responseResult.setResuit(user);
                  responseResult.setCode(200);
                  responseResult.setSuccess("登录成功！！！！");
                  System.out.println("成功");
                  return responseResult;
              }else{ throw new LoginException("用户名或密码错误");
              }
          }else{ throw new LoginException("用户名或密码错误");
          }
      }else{ throw new LoginException("用户名或密码错误");
      }

    }

    //滑动获取验证码
    @RequestMapping("getCode")
    @ResponseBody
    public ResponseResult getCode(HttpServletResponse response, HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        String code=VerifyCodeUtils.generateVerifyCode(5);//随机获取
        ResponseResult responseResult=ResponseResult.getResponseResult();
        responseResult.setResuit(code);//存储到自定义实体类上
        String uidCode="CODE"+UID.getUUID16();
        redisTemplate.opsForValue().set(uidCode,code);//将生成的字符串存入redis
        redisTemplate.expire(uidCode,1, TimeUnit.MINUTES);//设置超时时间
        //回写cookie
        Cookie cookie=new Cookie("authcode",uidCode);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        return responseResult;
    }

}
