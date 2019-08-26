package com.abn.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author C60175
 *
 */
public class JsonReader {
	private static JsonReader instance;

	private static Logger logger = Logger.getLogger(JsonReader.class);

	public static synchronized JsonReader getInstance() {
		if (instance == null) {
			instance = new JsonReader();
		}

		/**
		 * @return
		 */
		return instance;
	}

	public synchronized Object readJson() {
		JSONParser jsonParser = new JSONParser();
		Object obj = null;
		PropertiesReader props = new PropertiesReader();
		try {
			obj = jsonParser.parse(new FileReader(props.getProperty("masterjson")));
		} catch (FileNotFoundException e) {
			logger.info(ExceptionUtils.getFullStackTrace(e));
		} catch (IOException e) {
			logger.fatal(ExceptionUtils.getFullStackTrace(e));
		} catch (ParseException e) {
			logger.fatal(ExceptionUtils.getFullStackTrace(e));
		}
		/**
		 * @return
		 */
		return obj;
	}
	
	public synchronized Object readJson(String capabilitiesFile) {
		JSONParser jsonParser = new JSONParser();
		Object obj = null;
		PropertiesReader props = new PropertiesReader();
		try {
			obj = jsonParser.parse(new FileReader(props.getProperty(capabilitiesFile.toString())));
		} catch (FileNotFoundException e) {
			logger.info(ExceptionUtils.getFullStackTrace(e));
		} catch (IOException e) {
			logger.fatal(ExceptionUtils.getFullStackTrace(e));
		} catch (ParseException e) {
			logger.fatal(ExceptionUtils.getFullStackTrace(e));
		}
		/**
		 * @return
		 */
		return obj;
	}
}
