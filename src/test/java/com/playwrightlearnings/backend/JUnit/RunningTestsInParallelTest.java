package com.playwrightlearnings.backend.JUnit;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * annotation to make JUnit create one instance of a class for all test methods within that class
 * (by default each JUnit will create a new instance of the class for each test method)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@UsePlaywright
class RunningTestsInParallelTest {

  @Test
  void showLoadedPropertiesFile() throws Exception {
    try (InputStream in = getClass().getClassLoader()
      .getResourceAsStream("junit-platform.properties")) {
      assertNotNull(in, "junit-platform.properties is not found");
      Properties props = new Properties();
      props.load(in);

      System.out.println("Loaded junit-platform.properties from src/test/resources:");
      props.forEach((k, v) -> System.out.println(k + " = " + v));
    }
  }

}

@UsePlaywright
class Test1 extends RunningTestsInParallelTest {
  @Test
  void testPlaywright(Page page) {
    page.navigate("https://playwright.dev");
    System.out.println("Title: " + page.title());
    assertThat(page).hasTitle("Fast and reliable end-to-end testing for modern web apps | Playwright");
    Locator getStarted = page.locator("text=Get Started");
    assertThat(getStarted).hasAttribute("href", "/docs/intro");
    getStarted.click();
    assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation"))).isVisible();
  }
}

@UsePlaywright
class Test2 extends RunningTestsInParallelTest {
  @Test
  void tracingPlaywright(Page page, BrowserContext context) {
    context.tracing().start(new Tracing.StartOptions()
      .setScreenshots(true)
      .setSnapshots(true)
      .setSources(true));
    page.navigate("https://playwright.dev");
    assertThat(page).hasTitle("Fast and reliable end-to-end testing for modern web apps | Playwright");
    context.tracing().stop(new Tracing.StopOptions()
      .setPath(Paths.get("trace.zip")));
  }
}
