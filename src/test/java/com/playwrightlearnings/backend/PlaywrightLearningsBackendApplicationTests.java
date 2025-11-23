package com.playwrightlearnings.backend;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // allows non-static @BeforeAll/@AfterAll
class PlaywrightLearningsBackendApplicationTests {

  private Playwright playwright;
  private Browser browser;

  private BrowserContext context;
  private Page page;

  @BeforeAll
  void setupPlaywright() {
    System.out.println("Setup Playwright and Browser");
    playwright = Playwright.create();
    browser = playwright.chromium().launch(
      new BrowserType.LaunchOptions().setHeadless(true) // CI purpose: enforce headless because Linux has no Display
    );
  }

  @AfterAll
  void closePlaywright() {
    System.out.println("Close Playwright");
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
    System.out.println("Close Context");
    if (page != null) page.close();
    if (context != null) context.close();
  }

  @Test
  void contextLoads() {
    // Spring Boot context test
  }

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
