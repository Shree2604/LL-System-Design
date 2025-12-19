// AppConfigDemo.java

class AppConfig {

    private static AppConfig instance;
    private java.util.HashMap<String, String> settings = new java.util.HashMap<>();

    private AppConfig() {} // private constructor

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public void set(String key, String value) {
        settings.put(key, value);
    }

    public String get(String key) {
        return settings.getOrDefault(key, null);
    }
}

public class AppConfigDemo {
    public static void main(String[] args) {
        AppConfig c1 = AppConfig.getInstance();
        AppConfig c2 = AppConfig.getInstance();

        c1.set("mode", "production");

        System.out.println("c1 mode: " + c1.get("mode"));
        System.out.println("c2 mode: " + c2.get("mode"));

        System.out.println("Same Instance? " + (c1 == c2));
    }
}
