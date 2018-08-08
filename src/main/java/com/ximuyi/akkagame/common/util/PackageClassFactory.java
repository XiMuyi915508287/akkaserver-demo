package com.ximuyi.akkagame.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class PackageClassFactory<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PackageClassFactory.class);

    private final String clsPackage;
    private final String clsNamePrefix;
    private final String clsPathPrefix;

    public PackageClassFactory(String clsPackage, String clsNamePrefix) {
        this.clsPackage = clsPackage;
        this.clsNamePrefix = clsNamePrefix;
        this.clsPathPrefix = String.format("%s.%s", this.clsPackage, this.clsNamePrefix);
    }

    public PackageClassFactory(Class<?> packageCls, String clsNamePrefix) {
        this.clsPackage = packageCls.getPackage().getName();
        this.clsNamePrefix = clsNamePrefix;
        this.clsPathPrefix = String.format("%s.%s", this.clsPackage, this.clsNamePrefix);
    }

    public T createInstance(Class<T> cls) {
        try {
            Constructor<T> ctor = cls.getDeclaredConstructor();
            if (ctor != null) {
                ctor.setAccessible(true);
                return ctor.newInstance();
            }

        } catch (Exception e) {
            LOGGER.error("<<PackageClassFactory.create>> error", e);
        }

        throw new RuntimeException("<PackageClassFactory> Create Failed, classPath: " + cls.getName());
    }

    @SuppressWarnings("unchecked")
    public T create(String clsNamePostfix) {
        String clsPath = clsPathPrefix + clsNamePostfix;

        try {
            Class<T> handler = (Class<T>) Class.forName(clsPath);
            Constructor<T> ctor = handler.getDeclaredConstructor();
            if (ctor != null) {
                ctor.setAccessible(true);
                return ctor.newInstance();
            }

        } catch (Exception e) {
            LOGGER.error("<<PackageClassFactory.create>> error", e);
        }

        throw new RuntimeException("<PackageClassFactory> Create Failed, classPath: " + clsPath);
    }

    @SuppressWarnings("unchecked")
    public T create(String clsNamePostfix, String param) {
        String clsPath = clsPathPrefix + clsNamePostfix;

        try {
            Class<T> handler = (Class<T>) Class.forName(clsPath);
            Constructor<T> ctor = handler.getDeclaredConstructor(String.class);
            if (ctor != null) {
                ctor.setAccessible(true);
                return ctor.newInstance(param);
            }

        } catch (Exception e) {
            LOGGER.error("<<PackageClassFactory.create>> error", e);
        }

        throw new RuntimeException("<PackageClassFactory> Create Failed, classPath: " + clsPath);
    }
}
