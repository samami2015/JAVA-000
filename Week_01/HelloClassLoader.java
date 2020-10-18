package week01;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

public class HelloClassLoader extends ClassLoader {
    private String basePath;

    HelloClassLoader(String basePath) {
        this.basePath = basePath;
    }

    public static void main(String[] args) {
        try {
            Object obj = new HelloClassLoader("C:\\Users\\wangsen\\Downloads\\Hello").findClass("Hello").newInstance();
            Method hello = obj.getClass().getDeclaredMethod("hello");
            hello.invoke(obj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = {};
        try {
            bytes = loadXlassData(name);
            bytes = decode(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    public byte[] decode(byte[] bytes) {
        byte[] classBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            classBytes[i] = (byte) (255 - bytes[i]);
        }
        return classBytes;
    }

    public byte[] loadXlassData(String xlassName) throws IOException {
        xlassName = xlassName.replaceAll("\\.", "/");
        String path = basePath + File.separator + xlassName + ".xlass";
        FileInputStream fis = null;
        byte[] classBytes = new byte[0];
        fis = new FileInputStream(path);
        int length = fis.available();
        classBytes = new byte[length];
        fis.read(classBytes);
        fis.close();
        return classBytes;
    }
}
