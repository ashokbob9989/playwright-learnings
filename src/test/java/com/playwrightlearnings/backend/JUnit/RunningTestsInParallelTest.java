package com.playwrightlearnings.backend.JUnit;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * annotation to make JUnit create one instance of a class for all test methods within that class
 * (by default each JUnit will create a new instance of the class for each test method)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RunningTestsInParallelTest {

  Playwright playwright;
  Browser browser;

  BrowserContext context;
  Page page;

  @BeforeAll
  void setupPlaywright() {
    System.out.println("Setup Playwright and Browser");
    playwright = Playwright.create();
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
  }

  @AfterAll
  void closePlaywright() {
    System.out.println("Close Browser and Playwright");
    if (browser != null) browser.close();
    if (playwright != null) playwright.close();
  }

  @BeforeEach
  void setupContext() {
    System.out.println("Setup Context and Page");
    context = browser.newContext();
    page = context.newPage();
  }

  @AfterEach
  void closeContext() {
    System.out.println("Close Page and Context");
    if (page != null) page.close();
    if (context != null) context.close();
  }

}

class Test1 extends RunningTestsInParallelTest {
  @Test
  void contextLoads() {
    // Spring Boot context test
  }
}

class Test2 extends RunningTestsInParallelTest {
  @Test
  void testPlaywright() {
    page.navigate("https://playwright.dev");
    System.out.println("Title: " + page.title());
    assertThat(page).hasTitle("Fast and reliable end-to-end testing for modern web apps | Playwright");
    Locator getStarted = page.locator("text=Get Started");
    assertThat(getStarted).hasAttribute("href", "/docs/intro");
    getStarted.click();
    assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation"))).isVisible();
  }
}

class Test3 extends RunningTestsInParallelTest {
  @Test
  void tracingPlaywright() {
    context.tracing().start(new Tracing.StartOptions()
      .setScreenshots(true)
      .setSnapshots(true)
      .setSources(true));
    page.navigate("https://playwright.dev");
    context.tracing().stop(new Tracing.StopOptions()
      .setPath(Paths.get("trace.zip")));
  }
}
