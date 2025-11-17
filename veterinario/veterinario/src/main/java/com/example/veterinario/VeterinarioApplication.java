package com.example.veterinario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VeterinarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeterinarioApplication.class, args);
	}

}
//http://localhost:8084/swagger-ui/index.html#/
// postman http://localhost:8084/api/v1/veterinario
//SE TIENE QUE AGREGAR AL HEADERS X-USER-ID y el numero el id del usuario
//{
/*  "rut": 87654321,
  "nombre": "Ana",
  "apellido": "Gomez",
  "especialidad": "Dermatología",
  "correo": "ana@mail.com",
  "usuarioId": 1
} */
