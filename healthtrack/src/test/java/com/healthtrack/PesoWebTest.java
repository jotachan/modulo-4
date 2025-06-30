package com.healthtrack;

import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.net.URL;

/**
 * Prueba funcional con Selenium para actualizar peso desde HTML.
 */
public class PesoWebTest extends TestCase {

  private WebDriver driver;

  protected void setUp() throws Exception {
    super.setUp();

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--user-data-dir=/tmp/chrome-profile");

    String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
    if (remoteUrl != null && !remoteUrl.isEmpty()) {
      driver = new RemoteWebDriver(new URL(remoteUrl), options);
    } else {
      driver = new ChromeDriver(options);
    }
  }

  public void testActualizarPesoDesdeFormulario() {
    String rutaHtml = System.getProperty("user.dir") + "/web/actualizar_peso.html";
    driver.get("file://" + rutaHtml);

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("peso")));

    WebElement inputPeso = driver.findElement(By.id("peso"));
    WebElement boton = driver.findElement(By.tagName("button"));

    inputPeso.sendKeys("67.8");
    boton.click();

    wait.until(ExpectedConditions.textToBePresentInElementLocated(
        By.id("resultado"), "Peso actualizado a 67.8 kg"));

    WebElement resultado = driver.findElement(By.id("resultado"));

    assertEquals("Peso actualizado a 67.8 kg", resultado.getText());
  }

  protected void tearDown() throws Exception {
    if (driver != null) {
      driver.quit();
    }
    super.tearDown();
  }
}
