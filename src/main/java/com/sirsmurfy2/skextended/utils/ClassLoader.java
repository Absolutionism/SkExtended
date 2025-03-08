package com.sirsmurfy2.skextended.utils;

import ch.njol.util.StringUtils;
import com.google.common.base.MoreObjects;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClassLoader {

	public static Builder builder() {
		return new Builder();
	}

	public static void loadClasses(Class<?> source, File jarFile, String basePackage, String ... subPackages) {
		builder()
			.basePackage(basePackage)
			.addSubPackages(subPackages)
			.initialize(true)
			.deep(true)
			.build();
	}

	private final String basePackage;
	private final Collection<String> subPackages;
	private final @Nullable Predicate<String> filter;
	private final boolean initialize;
	private final boolean deep;
	private final @Nullable Consumer<Class<?>> forEachClass;

	private ClassLoader(
		String basePackage,
		Collection<String> subPackages,
		@Nullable Predicate<String> filter,
		boolean initialize,
		boolean deep,
		@Nullable Consumer<Class<?>> forEachClass
	) {
		if (!basePackage.isEmpty())
			basePackage = basePackage.replace('.', '/') + "/";
		this.basePackage = basePackage;
		this.subPackages =  subPackages.stream()
			.map(subPackage -> subPackage.replace('.', '/') + "/")
			.collect(Collectors.toList());
		this.filter = filter;
		this.initialize = initialize;
		this.deep = deep;
		this.forEachClass = forEachClass;
	}

	public void loadClasses(Class<?> source) {
		loadClasses(source, (JarFile) null);
	}

	public void loadClasses(Class<?> source, File jarFile) {
		try (JarFile jar = new JarFile(jarFile)) {
			loadClasses(source, jar);
		} catch (IOException e) {
			loadClasses(source);
		}
	}

	public void loadClasses(Class<?> source, @Nullable JarFile jarFile) {
		Collection<String> classPaths;
		try  {
			if (jarFile != null) {
				classPaths = jarFile.stream()
					.map(JarEntry::getName)
					.collect(Collectors.toSet());
			} else {
				classPaths = ClassPath.from(source.getClassLoader()).getResources().stream()
					.map(ResourceInfo::getResourceName)
					.collect(Collectors.toSet());
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load classes: " + e);
		}

		int expectedDepth = !this.deep ? StringUtils.count(this.basePackage, '/') : 0;
		int offset = this.basePackage.length();

		Collection<String> classNames = new TreeSet<>(String::compareToIgnoreCase);
		for (String name : classPaths) {
			if (!name.startsWith(this.basePackage) ||  !name.endsWith(".class") || name.endsWith("package-info.class"))
				continue;
			boolean load = false;
			if (this.subPackages.isEmpty()) {
				load = this.deep || StringUtils.count(name, '/') == expectedDepth;
			} else {
				for (String subPackage : this.subPackages) {
					if (
						name.startsWith(subPackage, offset)
						&& (this.deep || StringUtils.count(name, '/') == (expectedDepth + StringUtils.count(subPackage, '/')))
					) {
						load = true;
						break;
					}
				}
			}

			if (load) {
				name = name.replace('/', '.').substring(0, name.length() - 6);
				if (filter == null || filter.test(name))
					classNames.add(name);
			}
		}

		java.lang.ClassLoader loader = source.getClassLoader();
		for (String className : classNames) {
			try {
				Class<?> clazz = Class.forName(className, this.initialize, loader);
				if (this.forEachClass != null)
					this.forEachClass.accept(clazz);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Failed to load class: " + className, e);
			} catch (ExceptionInInitializerError err) {
				throw new RuntimeException(className + " generated an exception while loading", err.getCause());
			}
		}
	}

	@Contract("-> new")
	public Builder toBuilder() {
        return builder()
			.basePackage(this.basePackage)
			.addSubPackages(this.subPackages)
			.initialize(this.initialize)
			.deep(this.deep)
			.filter(this.filter)
			.forEachClass(this.forEachClass);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("basePackage", basePackage)
			.add("subPackages", subPackages)
			.add("filter", filter)
			.add("initialize", initialize)
			.add("deep", deep)
			.add("forEachClass", forEachClass)
			.toString();
	}

	public static class Builder {

		private String basePackage = "";
		private final Collection<String> subPackages = new HashSet<>();
		private @Nullable Predicate<String> filter = null;
		private boolean initialize;
		private boolean deep;
		private @Nullable Consumer<Class<?>> forEachClass;

		private Builder() {}

		@Contract("_ -> this")
		public Builder basePackage(String basePackage) {
			this.basePackage = basePackage;
			return this;
		}

		@Contract("_ -> this")
		public Builder addSubPackage(String subPackage) {
			this.subPackages.add(subPackage);
			return this;
		}

		@Contract("_ -> this")
		public Builder addSubPackages(String ... subPackages) {
			Collections.addAll(this.subPackages, subPackages);
			return this;
		}

		@Contract("_ -> this")
		public Builder addSubPackages(Collection<String> subPackages) {
			this.subPackages.addAll(subPackages);
			return this;
		}

		@Contract("_ -> this")
		public Builder filter(Predicate<String> filter) {
			this.filter = filter;
			return this;
		}

		@Contract("_ -> this")
		public Builder initialize(boolean initialize) {
			this.initialize = initialize;
			return this;
		}

		@Contract("_ -> this")
		public Builder deep(boolean deep) {
			this.deep = deep;
			return this;
		}

		@Contract("_ -> this")
		public Builder forEachClass(Consumer<Class<?>> forEachClass) {
			this.forEachClass = forEachClass;
			return this;
		}

		@Contract("-> new")
		public ClassLoader build()  {
			return new ClassLoader(basePackage, subPackages, filter, initialize, deep, forEachClass);
		}

	}

}
