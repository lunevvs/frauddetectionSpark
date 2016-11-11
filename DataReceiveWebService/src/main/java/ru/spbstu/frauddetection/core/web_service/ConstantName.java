package ru.spbstu.frauddetection.core.web_service;

public class ConstantName {
	public final String workDirName = "WorkDir";
	public final String reportsName = "Reports";
	public final String configFileName = "ConfigFile.xml";
	public final String reportFileName = "report";
	public final String pathToJsonReport = DirectoryHelper.getPathTo(reportsName) + "/" + reportFileName + ".json";
	public final String pathToCsvReport = DirectoryHelper.getPathTo(reportsName) + "/" + reportFileName + ".csv";
	public final String pathToConfigXML = DirectoryHelper.getPathTo(workDirName) + "/" + configFileName;
}
