package com.sirsmurfy2.skextended.lang;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SectionEvent<T> extends Event {

	public SectionEvent() {}

	private Map<Class<?>, Object> values = new HashMap<>();

	public SectionEvent<T> setValue(Class<? extends T> tClass, T object) {
		values.put(tClass, object);
		return this;
	}

	public void setValues(Map<Class<?>, Object> newValues) {
		values = newValues;
	}

	public void setValues(Class<?>[] classes, Object[] objects) {
		if (classes.length != objects.length)
			throw new IllegalArgumentException("The length of both arrays must be the same.");
		for (int i = 0; i < classes.length; i++) {
			Class<?> clazz = classes[i];
			Object object = objects[i];
			values.put(clazz, object);
		}
	}

	public @Nullable T getValue(Class<T> tClass) {
		Object object = values.get(tClass);
		try {
			//noinspection unchecked
			T t = (T) object;
			return t;
		} catch (Exception ignored) {}
		return null;
	}

	public void clearValue(Class<?> clazz) {
		values.remove(clazz);
	}

	public void clearValues() {
		values.clear();
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		throw new IllegalStateException();
	}

}
