package com.playwright.tests;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class LandingPageTest {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            String url = "https://dev.egacademy.org.in/index";

            // Chromium (Chrome)
            Browser chromium = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page chromiumPage = chromium.newPage();
            chromiumPage.navigate(url);
            checkPageTitleAndScreenshot(chromiumPage, "Chromium", "chromium-screenshot.png");

            // Firefox
            Browser firefox = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page firefoxPage = firefox.newPage();
            firefoxPage.navigate(url);
            checkPageTitleAndScreenshot(firefoxPage, "Firefox", "firefox-screenshot.png");

            // WebKit
            Browser webkit = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page webkitPage = webkit.newPage();
            webkitPage.navigate(url);
            checkPageTitleAndScreenshot(webkitPage, "WebKit", "webkit-screenshot.png");

            // Microsoft Edge
            Browser edge = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setExecutablePath(Paths.get("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe"))
                .setHeadless(false));
            Page edgePage = edge.newPage();
            edgePage.navigate(url);
            checkPageTitleAndScreenshot(edgePage, "Microsoft Edge", "edge-screenshot.png");

            // Wait so all pages stay open for a bit
            edgePage.waitForTimeout(9000);

            chromium.close();
            firefox.close();
            webkit.close();
            edge.close();
        }
    }

    private static void checkPageTitleAndScreenshot(Page page, String browserName, String screenshotFileName) {
        String title = page.title();
        System.out.println("[" + browserName + "] Page title: " + title);

        if (title.contains("EG International Academy")) {
            System.out.println(" [" + browserName + "] Landing page loaded successfully.");
        } else {
            System.out.println(" [" + browserName + "] Landing page did NOT load as expected.");
        }

        // Take and save screenshot
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotFileName)));
    }
}
