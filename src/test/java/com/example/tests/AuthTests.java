package com.example.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthTests {
    private static WebDriver driver; // Веб-драйвер для управления браузером
    private static WebDriverWait wait; // Объект для явного ожидания элементов

    // Метод выполняется один раз перед всеми тестами
    @BeforeAll
    public static void setUpClass() {
        System.out.println("Запуск браузера...");

        // Устанавливаем и настраиваем Chrome WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Устанавливаем время ожидания для поиска элементов
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Разворачиваем окно браузера на весь экран
        driver.manage().window().maximize();

        // Открываем страницу, где будем проводить тестирование
        driver.get("https://amzscout.net/app/#/database");
        System.out.println("Открыта страница входа.");
    }

    // Метод выполняется после каждого теста (очищаем куки для чистоты тестирования)
    @AfterEach
    public void clearCookies() {
        System.out.println("Очищаем cookies перед следующим тестом...");
        driver.manage().deleteAllCookies();
    }

    // Метод выполняется один раз после всех тестов (закрываем браузер)
    @AfterAll
    public static void tearDownClass() {
        if (driver != null) {
            System.out.println("Закрытие браузера...");
            driver.quit();
        }
    }

    // Тест на регистрацию нового пользователя
    @Test
    public void testSignUp() {
        try {
            System.out.println("Начинаем тест testSignUp...");

            // Ожидаем и кликаем по вкладке "Sign up"
            WebElement signUpTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Sign up')]")));
            signUpTab.click();
            System.out.println("Переключились на вкладку регистрации.");

            // Ожидаем появления поля email и вводим тестовый email
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='email']")));
            emailField.sendKeys("testuser@gmail.com");

            // Ожидаем появления кнопки "Continue" и кликаем по ней
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Continue')]")));
            continueButton.click();

            // Проверяем, появилось ли сообщение о необходимости подтверждения email
            WebElement confirmationMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Check your email')]")));
            assertTrue(confirmationMessage.isDisplayed(), "Сообщение о подтверждении почты не появилось!");
            System.out.println("Регистрация прошла успешно!");
        } catch (TimeoutException | NoSuchElementException e) {
            // В случае ошибки тест проваливается и выводит сообщение
            Assertions.fail("Ошибка testSignUp: " + e.getMessage());
        }
    }

    // Тест на вход в систему
    @Test
    public void testSignIn() {
        try {
            System.out.println("Начинаем тест testSignIn...");

            // Ожидаем и кликаем по вкладке "Sign in"
            WebElement signInTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Sign in')]")));
            signInTab.click();
            System.out.println("Переключились на вкладку входа.");

            // Ожидаем появления поля email и вводим тестовый email
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
            emailField.sendKeys("testuser@gmail.com");

            // Ожидаем появления поля пароля и вводим тестовый пароль
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
            passwordField.sendKeys("ValidPass123!");

            // Ожидаем появления кнопки "Continue" и кликаем по ней
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Continue')]")));
            continueButton.click();

            // Ждем, пока в URL появится "dashboard" (значит вход выполнен успешно)
            wait.until(ExpectedConditions.urlContains("dashboard"));
            assertTrue(driver.getCurrentUrl().contains("dashboard"), "Не произошел переход на главную страницу после входа!");
            System.out.println("Вход выполнен успешно!");
        } catch (TimeoutException | NoSuchElementException e) {
            // В случае ошибки тест проваливается и выводит сообщение
            Assertions.fail("Ошибка testSignIn: " + e.getMessage());
        }
    }
}
