package com.sirsmurfy2.skextended.modules.griefprevention.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprClaimFromId extends SimpleExpression<Claim> {

	static {
		Skript.registerExpression(ExprClaimFromId.class, Claim.class, ExpressionType.SIMPLE,
			"[the] claim[s] (from|with) [the] id[s] %integers%");
	}

	private Expression<Integer> ids;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		//noinspection unchecked
		ids = (Expression<Integer>) exprs[0];
		return true;
	}

	@Override
	protected Claim @Nullable [] get(Event event) {
		return new Claim[0];
	}

	@Override
	public boolean isSingle() {
		return ids.isSingle();
	}

	@Override
	public Class<? extends Claim> getReturnType() {
		return Claim.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return null;
	}
}
