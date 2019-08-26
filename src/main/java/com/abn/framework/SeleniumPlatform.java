package com.abn.framework;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.abn.utilities.JsonReader;
import com.abn.utilities.PropertiesReader;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

/**
 * 
 * @author
 *
 */
public final class SeleniumPlatform {

	private Object obj;
	private static final SeleniumPlatform INSTANCE = new SeleniumPlatform();
	private String sauceAccessKey = System.getenv("SAUCELABS_ACCESSKEY");
	private String javahome = System.getenv("JAVA_HOME");

	public static SeleniumPlatform getInstance() {
		return INSTANCE;
	}

	public SeleniumPlatform() {
		JsonReader masterJson = new JsonReader();
		System.out.println(System.getenv("SAUCELABS_ACCESSKEY"));
		System.out.println(System.getenv("JAVA_HOME"));
		setObj(masterJson.readJson());
		System.getProperties().put("http.proxyHost", "nl-userproxy-access.net.abnamro.com");
		System.getProperties().put("http.proxyPort", "8080");
		System.getProperties().put("https.proxyHost", "nl-userproxy-access.net.abnamro.com");
		System.getProperties().put("https.proxyPort", "8080");
		PropertiesReader pr = new PropertiesReader();
		try {
			PropertyConfigurator.configure(pr.getProperty("logPdesktop"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object getmasterJson() {

		/**
		 * @return
		 */
		return getObj();
	}

	public String getObject(String objKey) {
		String jPath = "$.masterInput.objects..[?(@.name==\"" + objKey + "\")].Value";
		net.minidev.json.JSONArray object = JsonPath.parse(obj.toString()).read(jPath);
		String intermediate = object.toString().replace("\\", "");
		StringBuilder builder = new StringBuilder(intermediate);
		String refined = builder.deleteCharAt(0).deleteCharAt(builder.length() - 1).toString();
		StringBuilder builder2 = new StringBuilder(refined);
		String objPath = builder2.deleteCharAt(0).deleteCharAt(builder2.length() - 1).toString();
		/**
		 * @return
		 */
		return objPath;
	}

	public String getData(String dataKey) {
		String dataVal;
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[2];
		String function = e.getMethodName();
		String scenario = e.getFileName().substring(0, (e.getFileName().length() - 5));

		String jPath = "$.masterInput.scenarios..[?(@.ID==\"" + scenario + "\")]." + function + "..[?(@.name==\""
				+ dataKey + "\")].Value";
		net.minidev.json.JSONArray data = JsonPath.parse(obj.toString()).read(jPath);
		dataVal = data.toString().replace("[", "").replace("]", "").replace("\"", "");
		/**
		 * @return
		 */
		return dataVal;
	}

	private WebDriver setDriverTo(String driverProperty, String browserPath, String browser) throws IOException {
		WebDriver driver = null;

		PropertiesReader props = new PropertiesReader();
		if (props.getProperty("platform").toString().equals("desktop")) {
			if (browser.equals("chrome")) {
				ChromeOptions options = new ChromeOptions();
				JsonReader capsJson = new JsonReader();
				java.lang.Object capabilities = capsJson.readJson("cmscapabilitiesfile");
				java.util.LinkedHashMap<String, Object> booleanCaps = JsonPath.parse(capabilities.toString())
						.read("$." + browser + ".boolean");

				java.util.LinkedHashMap<String, String> nonbooleanCaps = JsonPath.parse(capabilities.toString())
						.read("$." + browser + ".nonboolean");

				JSONArray values = JsonPath.parse(capabilities.toString()).read("$." + browser + ".arguments");

				booleanCaps.forEach((key, value) -> {
					System.out.println(key + " -> " + value);
					options.setCapability(key, value);
				});

				nonbooleanCaps.forEach((key, value) -> {
					System.out.println(key + " -> " + value);
					options.setCapability(key, value);

				});

				for (int i = 0; i < values.size(); i++) {

					System.out.println(values.get(i).toString());
					options.addArguments(values.get(i).toString());
				}

				driver = new ChromeDriver(options);
			} else if (browser.equals("firefox")) {
				System.setProperty(driverProperty, props.getProperty(browserPath));
				driver = new FirefoxDriver();
			} else if (browser.equals("ie")) {
				System.setProperty(driverProperty, props.getProperty(browserPath));
				driver = new InternetExplorerDriver();
			}
		} else if (props.getProperty("platform").toString().equals("saucelabs")) {

			driver = new SeleniumPlatform().setRemoteDriverTo();
		}
		/**
		 * @return
		 */
		return driver;

	}

	public WebDriver getDriver(String browser) throws IOException {
		WebDriver driver = null;
		if (browser == "chrome") {
			driver = new SeleniumPlatform().setDriverTo("webdriver.chrome.driver", "chromepath", browser);
		}
		if (browser == "firefox") {
			driver = new SeleniumPlatform().setDriverTo("webdriver.gecko.driver", "firefoxpath", browser);
		}
		if (browser == "ie") {
			driver = new SeleniumPlatform().setDriverTo("webdriver.ie.driver", "iepath", browser);
		}
		if (browser == "saucelabschrome") {
			driver = new SeleniumPlatform().setRemoteDriverTo("chrome");
		}

		return driver;
	}

	public WebDriver getDriver() throws IOException {
		WebDriver driver = null;
		PropertiesReader props = new PropertiesReader();
		String browser = props.getProperty("browser").toString();
		System.out.println(browser);
		if (browser.equals("chrome")) {
			driver = new SeleniumPlatform().setDriverTo("webdriver.chrome.driver", "chromepath", browser);
		}
		if (browser.equals("firefox")) {
			driver = new SeleniumPlatform().setDriverTo("webdriver.gecko.driver", "firefoxpath", browser);
		}
		if (browser == "saucelabschrome") {
			driver = new SeleniumPlatform().setRemoteDriverTo();
		}
		if (browser.equals("ie")) {
			driver = new SeleniumPlatform().setDriverTo("webdriver.ie.driver", "iepath", browser);
		}

		return driver;
	}

	private WebDriver setRemoteDriverTo() throws IOException {

		PropertiesReader props = new PropertiesReader();
		DesiredCapabilities caps = new DesiredCapabilities();
		String browser = props.getProperty("browser").toString();
		caps.setCapability(CapabilityType.BROWSER_NAME, browser);
		JsonReader capsJson = new JsonReader();
		java.lang.Object capabilities = capsJson.readJson("saucelabcapabilitiesfile");
		java.util.LinkedHashMap<String, Object> booleanCaps = JsonPath.parse(capabilities.toString())
				.read("$." + browser + ".boolean");

		java.util.LinkedHashMap<String, String> nonbooleanCaps = JsonPath.parse(capabilities.toString())
				.read("$." + browser + ".nonboolean");

		booleanCaps.forEach((key, value) -> {
			System.out.println(key + " -> " + value);
			caps.setCapability(key, value);
		});

		nonbooleanCaps.forEach((key, value) -> {
			System.out.println(key + " -> " + value);
			caps.setCapability(key, value);

		});

		caps.setCapability("accessKey", props.getProperty("accesskey").toString());

		WebDriver driver = new RemoteWebDriver(new URL(props.getProperty("sauceurl").toString()), caps);
		/**
		 * @return
		 */
		return driver;
	}

	private WebDriver setRemoteDriverTo(String browser) throws IOException {

		PropertiesReader props = new PropertiesReader();
		DesiredCapabilities caps = new DesiredCapabilities();

		caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		caps.setCapability("platform", "Windows 10");
		caps.setCapability("version", "latest");
		caps.setCapability(CapabilityType.BROWSER_NAME, props.getProperty("browser").toString());
		caps.setCapability("username", props.getProperty("sauceuser").toString());
		caps.setCapability("accessKey", props.getProperty("accesskey").toString());
		caps.setCapability("parentTunnel", "ABNAMRO_Admin");
		caps.setCapability("tunnelIdentifier", "ipsec-abnamro");
		// System.out.println(props.getProperty("sauceuser") + "--" +
		// props.getProperty("accesskey")+ "--" + props.getProperty("browser") + "--" +
		// props.getProperty("sauceurl") + "--" +
		// props.getProperty("sauceurl").toString() );
		WebDriver driver = new RemoteWebDriver(new URL(props.getProperty("sauceurl").toString()), caps);
		/**
		 * @return
		 */
		return driver;
	}

	public WebDriver getSauceDriver(String browser) throws IOException {
		WebDriver driver = null;
		PropertiesReader props = new PropertiesReader();

		DesiredCapabilities caps = new DesiredCapabilities();
		System.getProperties().put("http.proxyHost", "nl-userproxy-access.net.abnamro.com");
		System.getProperties().put("http.proxyPort", "8080");
		System.getProperties().put("https.proxyHost", "nl-userproxy-access.net.abnamro.com");
		System.getProperties().put("https.proxyPort", "8080");
		caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		caps.setCapability("platform", "Windows 10");
		caps.setCapability("version", "latest");
		caps.setCapability(CapabilityType.BROWSER_NAME, browser);
		caps.setCapability("username", "TAABNAMRO");
		// caps.setCapability("accessKey", props.getProperty("accesskey").toString());

		//caps.setCapability("accesskey", sauceAccessKey.toString());
		caps.setCapability("accessKey", props.getProperty("accesskey").toString());
		caps.setCapability("parentTunnel", "ABNAMRO_Admin");
		caps.setCapability("tunnelIdentifier", "ipsec-abnamro");
		// System.out.println(props.getProperty("sauceuser") + "--" +
		// props.getProperty("accesskey")+ "--" + props.getProperty("browser") + "--" +
		// props.getProperty("sauceurl") + "--" +
		// props.getProperty("sauceurl").toString() );
		driver = new RemoteWebDriver(new URL(props.getProperty("sauceurl").toString()), caps);
		/**
		 * @return
		 */
		return driver;
	}

	/**
	 * @return the obj
	 */
	public Object getObj() {
		return obj;
	}

	/**
	 * @param obj the obj to set
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}

	public void getScreenShot(WebDriver driver) {

		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement te = stacktrace[2];
		String function = te.getMethodName();
		String scenario = te.getFileName().substring(0, (te.getFileName().length() - 5));
		File currentDir = new File("");
		String wrkDir = currentDir.getAbsolutePath().toString();
		String oprSys = SystemUtils.OS_NAME.toString();

		try {

			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

			if (oprSys.contains("Windows")) {
				org.apache.commons.io.FileUtils.copyFile(SrcFile, new File(wrkDir + "\\test-output\\screenshots\\"
						+ scenario + "_" + function + "_" + System.currentTimeMillis() + ".png"));
			} else if (oprSys.contains("Linux")) {
				org.apache.commons.io.FileUtils.copyFile(SrcFile, new File(wrkDir + "/test-output/screenshots/"
						+ scenario + "_" + function + "_" + System.currentTimeMillis() + ".png"));
			}

		}

		catch (IOException e) {
			System.out.println(e.getMessage());

		}
	}

	@SuppressWarnings("unused")
	public WebDriver getSaucelabsDriver() throws IOException {

		PropertiesReader props = new PropertiesReader();
		System.getProperties().put("http.proxyHost", "nl-userproxy-access.net.abnamro.com");
		System.getProperties().put("http.proxyPort", "8080");
		System.getProperties().put("https.proxyHost", "nl-userproxy-access.net.abnamro.com");
		System.getProperties().put("https.proxyPort", "8080");
		DesiredCapabilities caps = new DesiredCapabilities();
		String browser = props.getProperty("browser").toString();
		caps.setCapability(CapabilityType.BROWSER_NAME, browser);
		JsonReader capsJson = new JsonReader();
		java.lang.Object capabilities = capsJson.readJson("saucelabcapabilitiesfile");
		java.util.LinkedHashMap<String, Object> booleanCaps = JsonPath.parse(capabilities.toString())
				.read("$." + browser + ".boolean");

		java.util.LinkedHashMap<String, String> nonbooleanCaps = JsonPath.parse(capabilities.toString())
				.read("$." + browser + ".nonboolean");

		booleanCaps.forEach((key, value) -> {
			System.out.println(key + " -> " + value);
			caps.setCapability(key, value);
		});

		nonbooleanCaps.forEach((key, value) -> {
			System.out.println(key + " -> " + value);
			caps.setCapability(key, value);

		});

		caps.setCapability("accessKey", props.getProperty("accesskey").toString());
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[2];
		String function = e.getMethodName();
		String scenario = e.getFileName().substring(0, (e.getFileName().length() - 5));
		caps.setCapability("name", scenario);

		WebDriver driver = new RemoteWebDriver(new URL(props.getProperty("sauceurl").toString()), caps);
		/**
		 * @return
		 */
		return driver;
	}

	@SuppressWarnings("unused")
	public WebDriver getSaucelabsDriver(String browser) throws IOException {

		PropertiesReader props = new PropertiesReader();
		DesiredCapabilities caps = new DesiredCapabilities();
		System.getProperties().put("http.proxyHost", "nl-userproxy-access.net.abnamro.com");
		System.getProperties().put("http.proxyPort", "8080");
		System.getProperties().put("https.proxyHost", "nl-userproxy-access.net.abnamro.com");
		System.getProperties().put("https.proxyPort", "8080");

		caps.setCapability(CapabilityType.BROWSER_NAME, browser);
		JsonReader capsJson = new JsonReader();
		java.lang.Object capabilities = capsJson.readJson("saucelabcapabilitiesfile");
		java.util.LinkedHashMap<String, Object> booleanCaps = JsonPath.parse(capabilities.toString())
				.read("$." + browser + ".boolean");

		java.util.LinkedHashMap<String, String> nonbooleanCaps = JsonPath.parse(capabilities.toString())
				.read("$." + browser + ".nonboolean");

		booleanCaps.forEach((key, value) -> {
			System.out.println(key + " -> " + value);
			caps.setCapability(key, value);
		});

		nonbooleanCaps.forEach((key, value) -> {
			System.out.println(key + " -> " + value);
			caps.setCapability(key, value);

		});

		caps.setCapability("accessKey", props.getProperty("accesskey").toString());
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[2];
		String function = e.getMethodName();
		String scenario = e.getFileName().substring(0, (e.getFileName().length() - 5));
		caps.setCapability("name", scenario);

		WebDriver driver = new RemoteWebDriver(new URL(props.getProperty("sauceurl").toString()), caps);
		/**
		 * @return
		 */
		return driver;
	}

}
