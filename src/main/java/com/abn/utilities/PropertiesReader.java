package com.abn.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

/**
 * 
 * @author C60175
 *
 */
public class PropertiesReader {
	private String wrkDir;
	private String oprSys;

	public String getProperty(String property) throws IOException {
		File currentDir = new File("");
		wrkDir = currentDir.getAbsolutePath().toString();
		oprSys = SystemUtils.OS_NAME.toString();
		if (oprSys.contains("Windows")) {

			wrkDir = wrkDir + "\\resources\\config.properties";
		} else if (oprSys.contains("Linux")) {
			wrkDir = wrkDir + "/resources/config.properties";
		}

		FileReader reader = new FileReader(wrkDir);

		Properties props = new Properties();
		props.load(reader);

		return props.getProperty(property);

	}
}
