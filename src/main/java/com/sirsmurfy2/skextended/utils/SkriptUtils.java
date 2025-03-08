package com.sirsmurfy2.skextended.utils;

import ch.njol.skript.SkriptAddon;
import ch.njol.skript.lang.SkriptEventInfo;
import com.sirsmurfy2.skextended.SkExtended;
import org.bukkit.event.Event;
import org.skriptlang.skript.Skript;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import org.skriptlang.skript.registration.SyntaxRegistry.Key;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class SkriptUtils {

	private static Skript orgSkript;
	private static SyntaxRegistry skriptSyntaxRegistry;
	private static Map<Key<?>, ?> skriptRegisters;
	private static Class<?> syntaxRegisterClass;
	private static Method syntaxRegisterRemove;
	private static Field syntaxRegisterInfosField;

	static {
		orgSkript = ReflectUtils.getFieldData(ReflectUtils.getField(ch.njol.skript.Skript.class, "skript"));
		skriptSyntaxRegistry = orgSkript.syntaxRegistry();
		Class<?> syntaxRegistryImplClass = ReflectUtils.getClass("org.skriptlang.skript.registration.SyntaxRegistryImpl");
		skriptRegisters = ReflectUtils.getFieldData(ReflectUtils.getField(syntaxRegistryImplClass, "registers"), skriptSyntaxRegistry);
		syntaxRegisterClass = ReflectUtils.getClass("org.skriptlang.skript.registration.SyntaxRegister");
		syntaxRegisterRemove = ReflectUtils.getMethod(syntaxRegisterClass, "remove", SyntaxInfo.class);
		syntaxRegisterInfosField = ReflectUtils.getField(syntaxRegisterClass, "syntaxes");
	}

	@SuppressWarnings("UnstableApiUsage")
	public static String getRegisteredEventName(Class<? extends Event> eventClass) {
		SkriptAddon addon = SkExtended.getAddonInstance();
		for (SyntaxInfo<?> info : addon.syntaxRegistry().elements()) {
			if (info instanceof SkriptEventInfo<?> skriptEventInfo) {
				for (Class<? extends Event> infoClass : skriptEventInfo.events) {
					if (infoClass.equals(eventClass))
						return skriptEventInfo.getName();
				}
			}
		}

		// Check Skript's
		for (SyntaxInfo<?> info : skriptSyntaxRegistry.elements()) {
			if (info instanceof SkriptEventInfo<?> skriptEventInfo) {
				for (Class<? extends Event> infoClass : skriptEventInfo.events) {
					if (infoClass.equals(eventClass))
						return skriptEventInfo.getName();
				}
			}
		}
		throw new IllegalArgumentException("Unable to find SkriptEventInfo for the class '" + eventClass + "'.");
	}

}
