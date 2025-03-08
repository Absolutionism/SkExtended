package com.sirsmurfy2.skextended.modules.griefprevention.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sirsmurfy2.skextended.modules.griefprevention.GriefPreventionUtils;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Claim From ID")
@Description("Get a claim from the id.")
@Examples({
	"set {_claim} to claim with the id 2",
	"set {_claim} to the claim from id \"5\""
})
@Since("1.0.0")
@RequiredPlugins("GriefPrevention")
public class ExprClaimWithId extends SimpleExpression<Claim> {

	static {
		Skript.registerExpression(ExprClaimWithId.class, Claim.class, ExpressionType.SIMPLE,
			"[the] claim[s] (with|from) [the] [id[s]] %strings/longs%");
	}

	private Expression<?> ids;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		ids = exprs[0];
		return true;
	}

	@Override
	protected Claim @Nullable [] get(Event event) {
		List<Claim> claims = new ArrayList<>();
		for (Object object : ids.getArray(event)) {
			Long id = null;
			if (object instanceof String string) {
				id = Long.valueOf(string);
			} else if (object instanceof Long value) {
				id = value;
			}
			if (id == null)
				continue;
			claims.add(GriefPreventionUtils.getClaim(id));
		}
		return claims.toArray(Claim[]::new);
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
	public String toString(@Nullable Event event, boolean debug) {
		return "claims with the ids " + ids.toString(event, debug);
	}

}
