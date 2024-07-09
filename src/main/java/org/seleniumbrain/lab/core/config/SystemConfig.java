package org.seleniumbrain.lab.core.config;

public class SystemConfig {

    public static String getOsName() {
        String defaultOsName = "windows";
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("windows")) return "windows";
        if(osName.contains("mac")) return "mac";
        if(osName.contains("linux")) return "linux";
        return defaultOsName;
    }

    public static String getOsVersion() {
        return System.getProperty("os.version");
    }

    public static String getOsArch() {
        return System.getProperty("os.arch");
    }

    public static String getProjectDir() {
        return System.getProperty("user.dir");
    }

    public static String getUserHomeDir() {
        return System.getProperty("user.home");
    }

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public static String getJavaHome() {
        return System.getProperty("java.home");
    }

    public static String getUserAccountName() {
        return System.getProperty("user.name");
    }

    public static void main(String[] args) {
        System.out.println(getUserAccountName());
    }
}
