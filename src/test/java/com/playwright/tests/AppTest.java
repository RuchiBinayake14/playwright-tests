package com.playwright.tests;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;

public class AppTest {
    static Playwright playwright;
    static Browser browser;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void testWebsiteIsReachable() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        // ✅ Set timeout to 60 seconds and wait for DOM only
        page.navigate("https://dev.egacademy.org.in/index", 
            new NavigateOptions()
                .setTimeout(60000)
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        assertEquals("EG International Academy", page.title());
    }
}
