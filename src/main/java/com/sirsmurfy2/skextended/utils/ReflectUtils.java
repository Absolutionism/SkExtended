package com.sirsmurfy2.skextended.utils;

import com.sirsmurfy2.skextended.SkExtended;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtils {

	public static Field getField(Class<?> clazz, String fieldName) {
		return getField(clazz, fieldName, true);
	}

	public static Field getField(Class<?> clazz, String fieldName, boolean required) {
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException ignored) {}
		if (field != null) {
			field.setAccessible(true);
			return field;
		}
		if (required)
			throw new RuntimeException("Could not find a field named '" + fieldName + "' from the class '" + clazz + "'");
		return null;
    }

	public static <R> R getFieldData(Field field) {
		return getFieldData(field, null);
	}

	public static <R> R getFieldData(Field field, @Nullable Object object) {
        try {
			//noinspection unchecked
			return (R) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

	public static Class<?> getClass(String path) {
        try {
            return Class.forName(path);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

	public static Method getMethod(Class<?> clazz, String methodName) {
		return getMethod(clazz, methodName, (Class<?>[]) null);
	}

	public static Method getMethod(Class<?> clazz, String methodName, @Nullable Class<?> ... parameters) {
		Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, parameters);
        } catch (NoSuchMethodException ignored) {}
		if (method == null && parameters == null)
			method = findMethod(clazz, methodName);
		if (method != null)
			return method;
		throw new RuntimeException("Could not find a method named '" + methodName + "' with the parameter types ["
			+ Arrays.toString(parameters) + "] in the class '" + clazz + "'");
    }

	private static @Nullable Method findMethod(Class<?> clazz, String methodName) {
		Method[] methods = clazz.getDeclaredMethods();
		if (methods.length == 0)
			return null;
		List<Method> matched = new ArrayList<>();
		for (Method method : methods) {
			if (method.getName().equals(methodName))
				matched.add(method);
		}
		if (matched.isEmpty()) {
			return null;
		} else if (matched.size() == 1) {
			return matched.get(0);
		}
		throw new RuntimeException("There are multiple methods in the class '" + clazz + "' that match the name '"  + methodName + "'. "
			+ "Use #getMethod(Class<?>, String, Class<?>[]) to get the exact method.");
	}

	public static <E> @Nullable E invokeMethod(Method method, Object reference) {
		return invokeMethod(method, reference, (Object[]) null);
	}

	public static <E> @Nullable E invokeMethod(Method method, Object reference, @Nullable Object ... parameters) {
        E value = null;
		try {
			if (method.getReturnType().equals(void.class)) {
				method.invoke(reference, parameters);
			} else {
				//noinspection unchecked
				value = (E) method.invoke(reference, parameters);
			}
        } catch (Exception ignored) {}
		return value;
    }

	public static Class<?>[] loadClasses(String basePackage, String ... subPackages) {
		return loadClasses(SkExtended.getInstance(), basePackage, subPackages);
	}

	public static Class<?>[] loadClasses(Plugin plugin, String basePackage, String ... subPackages) {
		List<Class<?>> classes = new ArrayList<>();
		ClassLoader classLoader = ClassLoader.builder()
			.basePackage(basePackage)
			.addSubPackages(subPackages)
			.deep(true)
			.initialize(true)
			.forEachClass(classes::add)
			.build();
		File jarFile = getFile(plugin);
		if (jarFile != null) {
			classLoader.loadClasses(plugin.getClass(), jarFile);
		} else {
			classLoader.loadClasses(plugin.getClass());
		}
		return classes.toArray(Class[]::new);
	}

	public static @Nullable File getFile(Plugin plugin) {
		try {
			Method getFile = JavaPlugin.class.getDeclaredMethod("getFile");
			getFile.setAccessible(true);
			return (File) getFile.invoke(plugin);
		} catch (Exception ignored) {}
		return null;
	}


}
