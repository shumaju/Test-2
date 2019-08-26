package com.abn.tests;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.WebDriver;

import com.abn.framework.SeleniumPlatform;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * 
 * @author C60175
 *
 */
@SuppressWarnings("unused")
@TestInstance(Lifecycle.PER_CLASS)
public class SaucelabEdgeTest {

	private WebDriver driver = null;
	private SeleniumPlatform sp = null;
	private static Logger logger = Logger.getLogger(SaucelabChromeTest.class);

	@BeforeAll
	public void createDriver() throws IOException {
		/*
		 * Before any test executes , it is required get driver using the below two
		 * steps. Refer connections page for more details.
		 */
		sp = SeleniumPlatform.getInstance();
		driver = sp.getSaucelabsDriver("Edge");
	}

	//@Test
	@DisplayName("sauceedgetest")
	public void launchIntranet() throws UnirestException, InterruptedException {
		/*
		 * Write your test steps and use SeleniumPlatform's getObject , getData , getScreenshot
		 * methods. Refer connections page for more details.
		 */
		driver.get("http://st-portal-c04.nl.eu.abnamro.com:9997/nl/grootzakelijk/index.html");
		Thread.sleep(2000);
		//sp.getScreenShot(driver);
		logger.info("the above step was executed in saucelabs " + driver.getTitle());

	}

	@AfterAll
	@Tag("closedriver")
	public void closeDriver() throws UnirestException, IOException, InterruptedException {
		driver.close();
		driver.quit();
	}

}
