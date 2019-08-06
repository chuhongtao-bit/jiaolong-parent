package com.chuhongtao;

import lombok.Data;

@Data
public class ResponseResult {
    private int code;//返回信息编码  0失败 1成功
    private String error;//错误信息
    private Object resuit;//程序返回结果
    private String success;//成功信息
    public static ResponseResult getResponseResult(){//创建实例
        return new ResponseResult();
    }
    private String token;//登陆成功的标识(这里存储了一些用户的信息)
    private String tokenkey;//用来表示token的一个唯一的字符串
    private Long[] menuIds;
}

