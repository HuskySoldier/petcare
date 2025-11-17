package com.petcare.register_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
public class RegisterServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegisterServiceApplication.class, args);
    }

    

}
//SWAGGER
//http://localhost:8080/register/swagger-ui/index.html
//http://localhost:8080/register
//consulta en Postman
//{
//  "nombre": "Ana",
//  "apellido": "Pérez",
//  "email": "ana.perez@example.com",
//  "telefono": "987654321",
//  "password": "secreta123"
//}
