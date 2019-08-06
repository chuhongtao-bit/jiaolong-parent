package com.chuhongtao;


import com.chuhongtao.config.MyReslover;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *GateWayServer的启动类
 */
@SpringBootApplication
@RestController
public class TestGateWayServer {

    public static void main(String[] args) {
        SpringApplication.run(TestGateWayServer.class,args);
    }

    @RequestMapping("serverhealth")
    public String serverhealth(){
        System.out.println("=========gateway server check health is ok! ^_^ ========");
        return "ok";
    }

    @Bean(name="myAddrReslover")
    public MyReslover getMyReslover(){
        return new MyReslover();
    }

}
