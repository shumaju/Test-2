package com.abn.tests;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.By;
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
public class TS003Test {
	/*
	 * private SeleniumInit initParams = new SeleniumInit(); private Framework repo
	 * = initParams.getRepo(); private WebDriver driver = initParams.getDriver();
	 */
	private WebDriver driver = null;
	private SeleniumPlatform sp = null;
	private static Logger logger = Logger.getLogger(TS002Test.class);

	@BeforeAll
	public void createDriver() throws IOException {
		sp = SeleniumPlatform.getInstance();
		System.out.println(sp);
		driver = sp.getDriver("firefox");
	}

	//@Test
	public void launchIntranet() throws UnirestException, InterruptedException {
		driver.get("http://intranet.nl.eu.abnamro.com/nl/home/tops/index.html");
		Thread.sleep(2000);
		logger.info("the current application opened is " + driver.getTitle());
		driver.findElement(By.xpath(sp.getObject("Username"))).sendKeys(sp.getData("Username").toString());
		driver.findElement(By.xpath(sp.getObject("Password"))).sendKeys(sp.getData("Password").toString());
		Thread.sleep(2000);
		driver.findElement(By.xpath(sp.getObject("LogIn"))).click();

	}

	@AfterAll
	@Tag("closedriver")
	public void closeDriver() throws UnirestException, IOException, InterruptedException {
		driver.close();
		driver.quit();
	}

}
