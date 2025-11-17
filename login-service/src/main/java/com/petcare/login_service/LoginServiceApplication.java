package com.petcare.login_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LoginServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginServiceApplication.class, args);
    }
}
//swagger http://localhost:8079/loggin
//loggin con Postman http://localhost:8079/login
// asi se hace un loggin {
///"email": "admin@petcare.com",
//"password": "admin123"
//}