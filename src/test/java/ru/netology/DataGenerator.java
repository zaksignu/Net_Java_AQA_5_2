package ru.netology;

import com.github.javafaker.Faker;

import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
        public static FellowOne updateStatus(FellowOne one,String status) {
            FellowOne user = new FellowOne(
                    one.login,
                    one.password,
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
    }


