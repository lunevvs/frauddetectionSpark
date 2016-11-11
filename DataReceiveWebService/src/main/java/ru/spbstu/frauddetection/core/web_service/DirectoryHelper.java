package ru.spbstu.frauddetection.core.web_service;

import java.io.File;
import org.apache.log4j.Logger;

public class DirectoryHelper {
	private static Logger log = Logger.getLogger(ConfigServiceController.class.getName());
	private static ConstantName constN = new ConstantName();
	private static String separator = "/";
	
	public static String getPathTo(String directoryName) {
		// Creates the directory named by this abstract pathname,
		// including any necessary but nonexistent parent directories.

		// "getPathTo(String directoryName)" return absolute path to directory
		// with name "directoryName"

		String path = "tmp/" +  directoryName;

		final File dir = new File(path);
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				log.info("Directiry " + dir.getAbsolutePath() + " is created.");
			} else {
				log.info("Directory " + dir.getAbsolutePath() + " didn`t created.");
			}
		} else {
			log.info("Directory " + dir.getAbsolutePath() + " exist.");
		}

		return dir.getAbsolutePath();
	}
	
	public static String getPathToConfig(){
		String path = getPathTo(constN.workDirName) + separator + constN.configFileName;
		return path;
	}
}
