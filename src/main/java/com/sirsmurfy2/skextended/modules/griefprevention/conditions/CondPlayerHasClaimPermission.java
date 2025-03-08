package com.sirsmurfy2.skextended.modules.griefprevention.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Player Has Claim Permission")
@Description("Whether a player has a specific claim permission for a claim.")
@Examples({
	"if player has the edit claim permission for {_claim}:"
})
@Since("1.0.0")
@RequiredPlugins("GriefPrevention")
public class CondPlayerHasClaimPermission extends Condition {

	static {
		Skript.registerCondition(CondPlayerHasClaimPermission.class, ConditionType.PROPERTY,
			"%players% (has|have) [the] [claim permission[s]] %claimpermissions% (of|for) %claims%",
			"%players% (does not|doesn't) have [the] [claim permission[s]] %claimspermissions% (of|for) %claims%");
	}

	private Expression<Player> players;
	private Expression<ClaimPermission> permissions;
	private Expression<Claim> claims;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		//noinspection unchecked
		players = (Expression<Player>) exprs[0];
		//noinspection unchecked
		permissions = (Expression<ClaimPermission>) exprs[1];
		//noinspection unchecked
		claims = (Expression<Claim>) exprs[2];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		ClaimPermission[] permissions = this.permissions.getArray(event);
		Claim[] claims = this.claims.getArray(event);
		if (permissions.length == 0 || claims.length == 0)
			return false;
		return players.check(event, player ->
				SimpleExpression.check(claims, claim ->
						SimpleExpression.check(permissions, permission ->
							claim.checkPermission(player, permission, null) == null
						, false, this.permissions.getAnd())
				, false, this.claims.getAnd())
		, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
		builder.append(players);
		if (isNegated())
			builder.append("does not");
		builder.append("have the claim permissions", permissions, "for", claims);
		return builder.toString();
	}

}
