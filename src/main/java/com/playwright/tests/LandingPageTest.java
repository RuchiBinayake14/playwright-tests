package com.playwright.tests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

public class LandingPageTest {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            String url = "https://dev.egacademy.org.in/index";

            // Create test-results directory if not exists
            Path resultDir = Paths.get("test-results");
            if (!Files.exists(resultDir)) {
                Files.createDirectories(resultDir);
            }

            // Chromium (Chrome)
            Browser chromium = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page chromiumPage = chromium.newPage();
            handlePageNavigation(chromiumPage, url, "Chromium", "test-results/chromium-screenshot.png");
            chromium.close();

            // Firefox
            Browser firefox = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page firefoxPage = firefox.newPage();
            handlePageNavigation(firefoxPage, url, "Firefox", "test-results/firefox-screenshot.png");
            firefox.close();

            // WebKit
            Browser webkit = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page webkitPage = webkit.newPage();
            handlePageNavigation(webkitPage, url, "WebKit", "test-results/webkit-screenshot.png");
            webkit.close();

            // Microsoft Edge
            Browser edge = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setExecutablePath(Paths.get("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe"))
                .setHeadless(false));
            Page edgePage = edge.newPage();
            handlePageNavigation(edgePage, url, "Microsoft Edge", "test-results/edge-screenshot.png");

            // Keep Edge open a bit longer
            edgePage.waitForTimeout(9000);
            edge.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handlePageNavigation(Page page, String url, String browserName, String screenshotFileName) {
        try {
            System.out.println("[" + browserName + "] Navigating to: " + url);
            page.navigate(url, new Page.NavigateOptions().setTimeout(15000));
            checkPageTitleAndScreenshot(page, browserName, screenshotFileName);
        } catch (Exception e) {
            System.out.println("❌ [" + browserName + "] Failed to load page. Reason: " + e.getMessage());
            try {
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotFileName.replace(".png", "-failure.png"))));
                System.out.println("📸 [" + browserName + "] Failure screenshot saved.");
            } catch (Exception ssEx) {
                System.out.println("⚠️ [" + browserName + "] Could not take failure screenshot.");
            }
        }
    }

    private static void checkPageTitleAndScreenshot(Page page, String browserName, String screenshotFileName) {
        try {
            // Wait for full page load
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForTimeout(2000); // buffer wait

            String title = page.title();
            System.out.println("[" + browserName + "] Page title: " + title);

            if (title.contains("EG International Academy")) {
                System.out.println("✅ [" + browserName + "] Landing page loaded successfully.");
            } else {
                System.out.println("⚠️ [" + browserName + "] Landing page did NOT load as expected.");
            }

            // Take screenshot
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotFileName)));

        } catch (Exception e) {
            System.out.println("❌ [" + browserName + "] Error while checking title or taking screenshot:");
            e.printStackTrace();
        }
    }
}
