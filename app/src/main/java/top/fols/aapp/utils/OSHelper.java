package top.fols.aapp.utils;


import android.os.Build;  
import android.os.Environment;  

import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.lang.reflect.Method;  
import java.util.Collection;  
import java.util.Enumeration;  
import java.util.Map;  
import java.util.Properties;  
import java.util.Set;  

/** 
 * 设备系统辅助类 
 * 
 * @author liyunlong 
 * @date 2017/5/27 11:36 
 */  
public final class OSHelper {  
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";  
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";  
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";  
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";  

    public static boolean isEMUI() {  
        return isPropertiesExist(KEY_EMUI_VERSION_CODE);  
    }  

    public static boolean isMIUI() {  
        return isPropertiesExist(KEY_MIUI_VERSION_CODE, KEY_MIUI_VERSION_NAME, KEY_MIUI_INTERNAL_STORAGE);  
    }  

    private static boolean isPropertiesExist(String... keys) {  
        if (keys == null || keys.length == 0) {  
            return false;  
        }  
        try {  
            BuildProperties properties = BuildProperties.newInstance();  
            for (String key : keys) {  
                String value = properties.getProperty(key);  
                if (value != null)  
                    return true;  
            }  
            return false;  
        } catch (IOException e) {  
            return false;  
        }  
    }  

    private static final class BuildProperties {
        private final Properties properties;  
        private BuildProperties() throws IOException {  
			FileInputStream in = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            properties = new Properties();  
            // 读取系统配置信息build.prop类
            properties.load(in);
			in.close();
        }
        public boolean containsKey(final Object key) {  
            return properties.containsKey(key);  
        }
        public boolean containsValue(final Object value) {  
            return properties.containsValue(value);  
        }
        public Set<Map.Entry<Object, Object>> entrySet() {  
            return properties.entrySet();  
        }
        public String getProperty(final String name) {  
            return properties.getProperty(name);  
        }
        public String getProperty(final String name, final String defaultValue) {  
            return properties.getProperty(name, defaultValue);  
        }
        public boolean isEmpty() {  
            return properties.isEmpty();  
        }
        public Enumeration<Object> keys() {  
            return properties.keys();  
        }
        public Set<Object> keySet() {  
            return properties.keySet();  
        }
        public int size() {  
            return properties.size();  
        }
        public Collection<Object> values() {  
            return properties.values();  
        }
        public static BuildProperties newInstance() throws IOException {  
            return new BuildProperties();  
        }  
    }  
}  
