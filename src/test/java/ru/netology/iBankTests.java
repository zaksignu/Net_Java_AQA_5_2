package ru.netology;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;


public class iBankTests {

    @BeforeEach
    public void startUp() {
        open("http://localhost:9999");
    }


    @Test
    public void shouldNotWorkWithInvalidUser() {
        var notValidUser = DataGenerator.Registration.generateUser("active");
        $("[data-test-id=\"login\"] .input__control").setValue(notValidUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(notValidUser.getPassword());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldNotWorkInvalidUserStatus() {

        DataGenerator.FellowOne validUserBlocked = DataGenerator.ServerComm.FullInitOfRandomBlockedUser();
        $("[data-test-id=\"login\"] .input__control").setValue(validUserBlocked.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(validUserBlocked.getPassword());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(text("Ошибка! Пользователь заблокирован"));

    }

    @Test
    public void shouldNotWorkInvalidUserPass() {
        DataGenerator.FellowOne spark  = DataGenerator.ServerComm.FullInitOfRandomActiveUser();
        $("[data-test-id=\"login\"] .input__control").setValue(spark.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(spark.getLogin());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldNotWorkNotRegistredUser() {
        DataGenerator.FellowOne spark  = DataGenerator.ServerComm.FullInitOfRandomActiveUser();
        $("[data-test-id=\"login\"] .input__control").setValue(spark.getPassword());
        $("[data-test-id=\"password\"] .input__control").setValue(spark.getLogin());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("[data-test-id=\"error-notification\"] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldWorkWithHappyPath() {
        DataGenerator.FellowOne spark  = DataGenerator.ServerComm.FullInitOfRandomActiveUser();
        $("[data-test-id=\"login\"] .input__control").setValue(spark.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(spark.getPassword());
        $(".form-field [data-test-id=\"action-login\"]").click();
        $("#root .heading").shouldHave(text("Личный кабинет"));
    }

}
