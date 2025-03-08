package com.sirsmurfy2.skextended.modules.equation.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.EventRestrictedSyntax;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.sirsmurfy2.skextended.modules.equation.sections.ExprSecEquation.EquationEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.log.runtime.SyntaxRuntimeErrorProducer;

@Name("Replace Equation Variable")
@Description("Replace a variable of an equation string inside an equation section.")
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
public class EffEquationReplace extends Effect implements EventRestrictedSyntax, SyntaxRuntimeErrorProducer {

	static {
		Skript.registerEffect(EffEquationReplace.class,
			"replace [the] equation variable %string% with %string/number%");
	}

	private Expression<String> variable;
	private Expression<?> replacing;
	private Node node;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		node = getParser().getNode();
		//noinspection unchecked
		variable = (Expression<String>) exprs[0];
		replacing = exprs[1];
		return true;
	}

	@Override
	public Class<? extends Event>[] supportedEvents() {
		return CollectionUtils.array(EquationEvent.class);
	}

	@Override
	protected void execute(Event event) {
		if (!(event instanceof EquationEvent equationEvent))
			return;
		String variable = this.variable.getSingle(event);
		if (variable == null || variable.isEmpty()) {
			eventError("The variable to be replaced cannot be empty nor null.", equationEvent);
			return;
		}
		Object object = replacing.getSingle(event);
		String replacing;
		if (object == null) {
			eventError("The object to replace cannot be null.", equationEvent);
			return;
		} else if (object instanceof String stringReplace) {
			if (stringReplace.isEmpty()) {
				eventError("The object to replace cannot be empty.", equationEvent);
				return;
			}
			replacing = stringReplace;
		} else if (object instanceof Number number) {
			replacing = number.toString();
		} else {
			eventError("Invalid type", equationEvent);
			return;
		}
		if (!equationEvent.getFinalEquation().contains(variable)) {
			eventError("The equation does not contain a variable \"" + variable + "\".", equationEvent);
			return;
		}
		equationEvent.replaceVariables(variable, replacing);
	}

	private void eventError(String errorMessage, EquationEvent equationEvent) {
		equationEvent.setErrorInSection();
		error(errorMessage);
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return (new SyntaxStringBuilder(event, debug).append("replace the equation variable", variable, "with", replacing))
			.toString();
	}

}
