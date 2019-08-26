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
public class TS002Test {
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
		driver = sp.getDriver("chrome");
	}

	//@Test
	@DisplayName("desktopchrome")
	public void JIRA_Login() throws UnirestException, InterruptedException {
		driver.get("https://jira.aws.abnamro.org/login.jsp?os_destination=%2Fdefault.jsp");
		Thread.sleep(2000);
		sp.getScreenShot(driver);
		logger.info("the current application opened is " + driver.getTitle());
		driver.findElement(By.xpath(sp.getObject("Username"))).sendKeys(sp.getData("Username").toString());
		driver.findElement(By.xpath(sp.getObject("Password"))).sendKeys(sp.getData("Password").toString());
		Thread.sleep(2000);
		driver.findElement(By.xpath(sp.getObject("LogIn"))).click();
		sp.getScreenShot(driver);

	}

	@AfterAll
	@Tag("closedriver")
	public void closeDriver() throws UnirestException, IOException, InterruptedException {
		driver.close();
		driver.quit();
	}

}
