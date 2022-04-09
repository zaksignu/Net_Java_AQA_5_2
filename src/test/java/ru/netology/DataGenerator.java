package ru.netology;

import com.github.javafaker.Faker;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    static Faker ghostOne = new Faker(new Locale("RU"));

    private DataGenerator() {
    }

    public static String generatelogin() {
        return ghostOne.name().username();
    }

    public static String generatePassword() {
        return ghostOne.aquaTeenHungerForce().character();
    }


    public static class Registration {
        private Registration() {
        }

        public static FellowOne generateUser(String status) {
            FellowOne user = new FellowOne(
                    generatelogin(),
                    generatePassword(),
                    status);
            return user;
        }
    }

    @Value
    public static class FellowOne {
        private String login;
        private String password;
        private String status;
    }

    @Value
    public static class ServerComm {

        private static RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        static void RegisterUser(FellowOne newUser) {
            Gson gson = new Gson();
            // сам запрос
            given() // "дано"
                    .spec(requestSpec) // указываем, какую спецификацию используем
                    .body(gson.toJson(newUser)) // передаём в теле объект, который будет преобразован в JSON
                    .when() // "когда"
                    .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                    .then() // "тогда ожидаем"
                    .statusCode(200); // код 200 OK
        }
        static FellowOne FullInitOfRandomActiveUser(){
            FellowOne randomOne = DataGenerator.Registration.generateUser("active");
            RegisterUser(randomOne);
            return randomOne;

        }
        static FellowOne FullInitOfRandomBlockedUser(){
            FellowOne randomOne = DataGenerator.Registration.generateUser("blocked");
            RegisterUser(randomOne);
            return randomOne;
        }
    }
}

