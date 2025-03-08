package com.sirsmurfy2.skextended.modules.griefprevention.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Claim ID")
@Description("The ID of a claim.")
@Examples({
	"set {_id} to the claim id of {_claim}"
})
@Since("1.0.0")
@RequiredPlugins("GriefPrevention")
public class ExprClaimId extends PropertyExpression<Claim, Long> {

	static {
		register(ExprClaimId.class, Long.class, "[grief prevention] claim id[s]", "claims");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		//noinspection unchecked
		setExpr((Expression<? extends Claim>) exprs[0]);
		return true;
	}

	@Override
	protected Long[] get(Event event, Claim[] claims) {
		return get(claims, Claim::getID);
	}

	@Override
	public Class<? extends Long> getReturnType() {
		return Long.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "claim ids of " + getExpr().toString(event, debug);
	}

}
