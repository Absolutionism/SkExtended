package com.sirsmurfy2.skextended.lang;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converter;

import java.util.HashMap;
import java.util.Map;

public class NamedEventValue<E extends Event, T> extends SimpleExpression<Object> {

	private static final Map<String, NamedEventValue<?, ?>> registeredNameds = new HashMap<>();

	private Class<E> eventClass;
	private Class<T> valueClass;
	private String pattern;
	private Converter<E, T> converter;

	public NamedEventValue(Class<E> eventClass, Class<T> valueClass, String pattern, Converter<E, T> converter) {
		this.eventClass = eventClass;
		this.valueClass = valueClass;
		this.pattern = pattern;
		this.converter = converter;
		registeredNameds.put(pattern, this);
		//noinspection unchecked
		Skript.registerExpression(NamedEventValue.class, valueClass, ExpressionType.SIMPLE, pattern);
	}

	private void copyData(NamedEventValue<E, T> other) {
		other.eventClass = this.eventClass;
		other.valueClass = this.valueClass;
		other.pattern = this.pattern;
		other.converter = this.converter;
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		//noinspection unchecked
		NamedEventValue<E, T> namedEventValue = (NamedEventValue<E, T>) registeredNameds.get(parseResult.expr);
		if (namedEventValue == null)
			return false;
		namedEventValue.copyData(this);
		if (!getParser().isCurrentEvent(eventClass)) {
			Skript.error("");
			return false;
		}
		return true;
	}

	@Override
	protected Object @Nullable [] get(Event event) {
		//noinspection unchecked
		return new Object[] {converter.convert((E) event)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<?> getReturnType() {
		return valueClass;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return pattern;
	}

}
