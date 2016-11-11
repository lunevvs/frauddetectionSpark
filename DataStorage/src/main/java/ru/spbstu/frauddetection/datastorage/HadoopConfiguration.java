package ru.spbstu.frauddetection.datastorage;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Properties;

public class HadoopConfiguration implements Serializable {

    private HadoopConfiguration instance;
    private Configuration configuration = new Configuration();

    private static String propertiesFile = "./etc/DataStorage.properties";
    public final String HADOOP_CONFIG_DIR = "hadoop_config_dir";
    public final Properties properties = new Properties();

    public HadoopConfiguration() {
        try {
            properties.load(new FileInputStream(propertiesFile));
        } catch (Exception e){
            new Exception(e);
        }
        String HCD = properties.getProperty(HADOOP_CONFIG_DIR);
        configuration.addResource(new Path(HCD + "core-site.xml"));
        configuration.addResource(new Path(HCD + "hdfs-site.xml"));
        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
    }

    public HadoopConfiguration getInstance() {
        if(instance == null)
            instance = new HadoopConfiguration();
        return instance;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}