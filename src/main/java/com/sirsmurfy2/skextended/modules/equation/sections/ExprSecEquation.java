package com.sirsmurfy2.skextended.modules.equation.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.EffChange;
import ch.njol.skript.expressions.arithmetic.ExprArithmetic;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.log.runtime.SyntaxRuntimeErrorProducer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Name("Variable Equation")
@Description("Provide an equation and replace variables to get the result.")
@Examples({
	"set {_number} to equation \"x + y + z\":",
		"\treplace the equation variable \"x\" with {_x}",
		"\treplace the equation variable \"y\" with {_y}",
		"\treplace the equation variable \"z\" with {_z}",
	"",
	"set {_result} to equation \"([X1] + [X2]) * [X3]\":",
		"\treplace the equation variable \"[X1]\" with {_x1}",
		"\treplace the equation variable \"[X2]\" with {_x2}",
		"\treplace the equation variable \"[X3]\" with {_x3}",
})
@Since("1.0.0")
public class ExprSecEquation extends SectionExpression<Number> implements SyntaxRuntimeErrorProducer {

	public static class EquationEvent extends Event {

		private final String originalEquation;
		private String finalEquation;
		private boolean errorInSection = false;

		public EquationEvent(String originalEquation) {
			this.originalEquation = originalEquation;
			this.finalEquation = originalEquation;
		}

		public String getOriginalEquation() {
			return originalEquation;
		}

		public String getFinalEquation() {
			return finalEquation;
		}

		public void replaceVariables(String x, String replacing) {
			finalEquation = finalEquation.replace(x, replacing);
		}

		public void setErrorInSection() {
			this.errorInSection = true;
		}

		public boolean hasErrorInSection() {
			return errorInSection;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}

	}

	private static Field changerField;

	static {
		Skript.registerExpression(ExprSecEquation.class, Number.class, ExpressionType.SIMPLE,
			"[variable] equation %string%");
        try {
            changerField = EffChange.class.getDeclaredField("changer");
			changerField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

	private Expression<String> equationString;
	private Trigger trigger;
	private Node node;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> list) {
		if (sectionNode == null) {
			Skript.error("This expression requires a section.");
			return false;
		} else if (sectionNode.isEmpty()) {
			Skript.error("Cannot have an empty section.");
			return false;
		}
		AtomicBoolean delayed = new AtomicBoolean(false);
		Runnable afterLoading = () -> delayed.set(!getParser().getHasDelayBefore().isFalse());
		//noinspection unchecked
		trigger = loadCode(sectionNode, "equation", afterLoading, EquationEvent.class);
		if (delayed.get()) {
			Skript.error("Delays can't be used within an Equation Section");
			return false;
		}
		//noinspection unchecked
		equationString = (Expression<String>) exprs[0];
		this.node = sectionNode;
		return true;
	}

	@Override
	protected Number @Nullable [] get(Event event) {
		String equation = equationString.getSingle(event);
		if (equation == null || equation.isEmpty()) {
			error("An equation can not be null nor empty.");
			return null;
		}
		EquationEvent equationEvent = new EquationEvent(equation);
		Variables.withLocalVariables(event, equationEvent, () -> TriggerItem.walk(trigger, equationEvent));
		if (equationEvent.hasErrorInSection())
			return null;
		String finalEquation = equationEvent.getFinalEquation();
		if (finalEquation.matches("[a-zA-Z&$#@!<,>.?:;'\"{}_`~|\\\\\\[\\]]")) {
			error("The final equation contains illegal characters.");
			return null;
		}
		Effect effect = Effect.parse("set {_equation} to " + finalEquation, null);
		if (!(effect instanceof EffChange effChange))
			return null;
		Expression<?> changeExpr = null;
        try {
            changeExpr = (Expression<?>) changerField.get(effChange);
        } catch (IllegalAccessException ignored) {}
        if (!(changeExpr instanceof ExprArithmetic<?, ?, ?> exprArithmetic))
			return null;
		return (Number[]) exprArithmetic.getAll(equationEvent);
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "variable equation " + equationString.toString(event, debug);
	}

}
