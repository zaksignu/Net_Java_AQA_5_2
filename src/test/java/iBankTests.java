import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;

public class iBankTests
{
    static UserDefine dummy;
    static Faker ghostOne = new Faker(new Locale("EN"));

     private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpAll() {

        Gson gson = new Gson();
        dummy = new UserDefine(
                ghostOne.name().username(),
                ghostOne.aquaTeenHungerForce().character(),
                "active");
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(gson.toJson(dummy)) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }
    @BeforeEach
    public void startUp(){
        open("http://localhost:9999");
    }

    @Test
    public void shouldWorkWithHappyPath(){
        $("[data-test-id=\"login\"] .input__control").setValue(dummy.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(dummy.getPassword());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("#root .heading").shouldHave(text("Личный кабинет"),Duration.ofSeconds(10));
}
    @Test
    public void shouldNotWorkWithInvalidUser(){
        $("[data-test-id=\"login\"] .input__control").setValue(ghostOne.name().name());
        $("[data-test-id=\"password\"] .input__control").setValue(dummy.getPassword());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"),Duration.ofSeconds(10));
    }

    @Test
    public void shouldNotWorkInvalidUserStatus(){
        Gson gson = new Gson();
        dummy.setStatus("blocked");
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(gson.toJson(dummy)) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
        $("[data-test-id=\"login\"] .input__control").setValue(dummy.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(dummy.getPassword());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(text("Ошибка! Пользователь заблокирован"),Duration.ofSeconds(10));

    }

    @Test
    public void shouldNotWorkInvalidUserPass(){
        $("[data-test-id=\"login\"] .input__control").setValue(dummy.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(ghostOne.name().name());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"),Duration.ofSeconds(10));

    }

    @Test
    public void shouldNotWorkNotRegistredUser(){
        $("[data-test-id=\"login\"] .input__control").setValue(ghostOne.name().name());
        $("[data-test-id=\"password\"] .input__control").setValue(ghostOne.name().name());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"),Duration.ofSeconds(10));

    }
//    @AfterEach
//    public void cleaning(){
//        closeWindow();
//        cleaning();
//    }
}
