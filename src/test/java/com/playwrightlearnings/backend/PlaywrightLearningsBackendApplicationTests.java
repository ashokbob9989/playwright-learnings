package com.playwrightlearnings.backend;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest
class PlaywrightLearningsBackendApplicationTests {

//  Shared between all tests that belongs to this class
  static Playwright playwright;
  static Browser browser;

//  New instance will be created for each new test
  BrowserContext context;
  Page page;

  @BeforeAll
  static void setupPlaywright(){
    System.out.println("Setup Playwright and Browser");
    playwright = Playwright.create();
    browser = playwright.chromium().launch();
  }

  @AfterAll
  static void closePlaywright(){
    System.out.println("Close Browser and Playwright");
    browser.close();
    playwright.close();
  }

  @BeforeEach
  void setupContext(){
    System.out.println("Setup Context and Page");
    context = browser.newContext();
    page = context.newPage();
  }

  @AfterEach
  void closeContext(){
    System.out.println("Close Page and Context");
    page.close();
    context.close();
  }

	@Test
	void contextLoads() {
	}

  @Test
  void testPlaywright(){
    page.navigate("https://playwright.dev");
    System.out.println("Title: "+page.title());
    assertThat(page).hasTitle("Fast and reliable end-to-end testing for modern web apps | Playwright");
    Locator getStarted = page.locator("text=Get Started");
    assertThat(getStarted).hasAttribute("href", "/docs/intro");
    getStarted.click();
    assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation"))).isVisible();
  }

  @Test
  void testCodegenPlaywright(){
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
        .setHeadless(false));
      BrowserContext context = browser.newContext();
      Page page = context.newPage();
      page.navigate("https://playwright.dev/");
      page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get started")).click();
      page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation")).click();
    }
  }

  @Test
  void tracingPlaywright(){
    context.tracing().start(new Tracing.StartOptions()
      .setScreenshots(true)
      .setSnapshots(true)
      .setSources(true));
    page.navigate("https://playwright.dev");
    context.tracing().stop(new Tracing.StopOptions()
      .setPath(Paths.get("trace.zip")));
  }

}
