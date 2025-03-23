package com.sirsmurfy2.skextended.utils;

import ch.njol.skript.SkriptAddon;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptEventInfo;
import com.sirsmurfy2.skextended.SkExtended;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.Skript;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import org.skriptlang.skript.registration.SyntaxRegistry.Key;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class SkriptUtils {

	private static final Skript orgSkript;
	private static final SyntaxRegistry skriptSyntaxRegistry;
	private static final Map<Key<?>, ?> skriptRegisters;
	private static final Class<?> syntaxRegisterClass;
	private static final Method syntaxRegisterRemove;
	private static final Field syntaxRegisterInfosField;

	static {
		orgSkript = ReflectUtils.getFieldData(ReflectUtils.getField(ch.njol.skript.Skript.class, "skript"));
		skriptSyntaxRegistry = orgSkript.syntaxRegistry();
		Class<?> syntaxRegistryImplClass = ReflectUtils.getClass("org.skriptlang.skript.registration.SyntaxRegistryImpl");
		skriptRegisters = ReflectUtils.getFieldData(ReflectUtils.getField(syntaxRegistryImplClass, "registers"), skriptSyntaxRegistry);
		syntaxRegisterClass = ReflectUtils.getClass("org.skriptlang.skript.registration.SyntaxRegister");
		syntaxRegisterRemove = ReflectUtils.getMethod(syntaxRegisterClass, "remove", SyntaxInfo.class);
		syntaxRegisterInfosField = ReflectUtils.getField(syntaxRegisterClass, "syntaxes");
	}

	public static String getRegisteredEventName(Class<? extends Event> eventClass) {
		return getRegisteredEventName(eventClass, null, null);
	}

	public static String getRegisteredEventName(Class<? extends Event> eventClass, Class<? extends SkriptEvent> skriptClass) {
		return getRegisteredEventName(eventClass, skriptClass, null);
	}

	public static String getRegisteredEventName(Class<? extends Event> eventClass, String docName) {
		return getRegisteredEventName(eventClass, null, docName);
	}

	public static String getRegisteredEventName(
		Class<? extends Event> eventClass,
		@Nullable Class<? extends SkriptEvent> skriptClass,
		@Nullable String docName
	) {
		SkriptAddon addon = SkExtended.getAddonInstance();
		List<SkriptEventInfo<?>> eventInfos = new ArrayList<>();
		for (SyntaxInfo<?> info : addon.syntaxRegistry().elements()) {
			if (info instanceof SkriptEventInfo<?> skriptEventInfo) {
				for (Class<? extends Event> infoClass : skriptEventInfo.events) {
					if (infoClass.equals(eventClass))
						eventInfos.add(skriptEventInfo);
				}
			}
		}
		if (!eventInfos.isEmpty())
			return stripPossibleEventNames(eventInfos, eventClass, skriptClass, docName);

		// Check Skript's
		for (SyntaxInfo<?> info : skriptSyntaxRegistry.elements()) {
			if (info instanceof SkriptEventInfo<?> skriptEventInfo) {
				for (Class<? extends Event> infoClass : skriptEventInfo.events) {
					if (infoClass.equals(eventClass))
						eventInfos.add(skriptEventInfo);
				}
			}
		}
		if (!eventInfos.isEmpty())
			return stripPossibleEventNames(eventInfos, eventClass, skriptClass, docName);
		throw new IllegalArgumentException("Unable to find SkriptEventInfo for the class '" + eventClass + "'.");
	}

	private static String stripPossibleEventNames(
		List<SkriptEventInfo<?>> eventInfos,
		Class<? extends Event> eventClass,
		@Nullable Class<? extends SkriptEvent> skriptClass,
		@Nullable String docName
	) {
		if (eventInfos.size() == 1)
			return eventInfos.get(0).getName();

		List<SkriptEventInfo<?>> strippedClasses = new ArrayList<>();
		if (skriptClass != null) {
			for (SkriptEventInfo<?> eventInfo : eventInfos) {
				if (eventInfo.elementClass.equals(skriptClass))
					strippedClasses.add(eventInfo);
			}
			if (strippedClasses.isEmpty()) {
				strippedClasses = eventInfos;
			} else if (strippedClasses.size() == 1) {
				return strippedClasses.get(0).getName();
			}
		} else {
			strippedClasses = eventInfos;
		}

		List<SkriptEventInfo<?>> strippedDocs = new ArrayList<>();
		if (docName != null && !docName.isEmpty()) {
			for (SkriptEventInfo<?> eventInfo : strippedClasses) {
				if (eventInfo.getName().equalsIgnoreCase(docName) || eventInfo.getName().matches(docName))
					strippedDocs.add(eventInfo);
			}
			if (strippedDocs.isEmpty()) {
				strippedDocs = eventInfos;
			} else if (strippedDocs.size() == 1) {
				return strippedDocs.get(0).getName();
			}
		} else {
			strippedDocs = strippedClasses;
		}

		if (strippedDocs.size() == 1)
			return strippedDocs.get(0).getName();

		throw new IllegalArgumentException("There are multiple SkriptEventInfos that include '" + eventClass + "'. "
			+ "Please add additional information to grab the desired doc name.");
	}

}
