package com.sirsmurfy2.skextended.lang;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sirsmurfy2.skextended.utils.SkriptUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.log.runtime.SyntaxRuntimeErrorProducer;

public abstract class SimpleEventRestrictedExpression<E extends Event, R> extends SimpleExpression<R> implements SyntaxRuntimeErrorProducer {

	public static <R> void register(
		Class<? extends Expression<R>> expressionClass,
		Class<R> returnType,
		String ... patterns
	) {
		Skript.registerExpression(expressionClass, returnType, ExpressionType.SIMPLE, patterns);
	}

	private String expr;
	private Node node;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		Class<? extends E> eventClass = getEventClass();
		expr = parseResult.expr;
		node = getParser().getNode();
		if (!getParser().isCurrentEvent(eventClass)) {
			String eventName = SkriptUtils.getRegisteredEventName(eventClass);
			Skript.error("This expression can only be used in a(n) '" + eventName + "' event");
			return false;
		}
		return true;
	}

	public abstract R @Nullable [] convert(E event);
	public abstract Class<? extends E> getEventClass();

	@Override
	@SuppressWarnings("unchecked")
	protected R @Nullable [] get(Event event) {
        return convert((E) event);
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return expr;
	}

}
