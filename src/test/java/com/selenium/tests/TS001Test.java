package com.selenium.tests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
/**
 * 
 * @author 
 *
 */

@RunWith(DataProviderRunner.class)
public class TS001Test {

	private WebDriver driver = null;

	//private static Logger logger = Logger.getLogger(TS001Test.class);

	@Before
	public void createDriver() throws IOException {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\shubh\\Desktop\\Selenium Software\\chromedriver.exe");

		driver=new ChromeDriver();
	}

	@DataProvider
	public static Object[][] testData() {
		return new Object[][] { {"ashwini@gmail.com","#@MyShop@2019","COMPLETE YOUR PROFILE" },{"jessica@gmail.com","#@MyShop@2019","COMPLETE YOUR PROFILE" },{"tim@gmail.com","#@MyShop@2019","COMPLETE YOUR PROFILE" } };
	}

	@Test
	@UseDataProvider("testData")
	public void launchApplicationAndRegister(String email,String newPassword,String textToCompare) throws Exception {
		driver.get(" https://www.loblaws.ca");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//span[text()='My Shop']")).click();
		driver.findElement(By.xpath("(//a/span[text()='Sign In'])[2]")).click();
		driver.findElement(By.xpath("//a[@href='/create-account']")).click();
		Thread.sleep(5000);
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("newPassword")).sendKeys(newPassword);
		Thread.sleep(5000);
		driver.findElement(By.id("termsAndConditions")).click();
		driver.findElement(By.xpath("//button/span[text()='Create a ']")).click();
		String rc=driver.findElement(By.xpath("(//h2)[3]")).getText();
		System.out.println(rc);
		if(rc.equals(textToCompare))
		{
			System.out.println("Pass");
		}
		else{
			System.out.println("Fail");			
		}

	}
		
	@After
	public void closeDriver() {
		driver.close();
		driver.quit();
	}

}
