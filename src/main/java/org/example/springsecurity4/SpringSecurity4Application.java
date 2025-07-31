package org.example.springsecurity4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurity4Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity4Application.class, args);
    }

}
/*todo
    POST /api/auth/login — вход (вернуть текущую инфу о пользователе в ответ, ну и куку)
    POST /api/auth/logout — выход
    POST /api/auth/register — регистрация (вернуть текущую инфу о пользователе в ответ)
    GET /api/users — получить всех пользователей (только для админа, сделать пагинацию)
    GET /api/users/{id} — получить инфу о конкретном пользователе (просто USER может получить инфу только о себе самом)
    POST /api/users — создать нового пользака (только для админов, вернуть нового пользака в ответ)
    PUT /api/users/{id} — отредактировать пользователя (толкьо админ)
    DELETE /api/users/{id} — уничтожить пользователя (только админ)
    GET /api/roles — получить список всех родей
    Также сформируй файл test.http в ресурсах приложения и сделай там как раз все запросы на это АПИ на языке IDEA
*/