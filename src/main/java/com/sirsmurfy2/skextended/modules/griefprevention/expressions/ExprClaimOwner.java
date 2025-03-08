package com.sirsmurfy2.skextended.modules.griefprevention.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Name("Claim Owner")
@Description("The owner of a claim.")
@Examples({
	"set {_owner} to the claim owner of {_claim}"
})
@Since("1.0.0")
@RequiredPlugins("GriefPrevention")
public class ExprClaimOwner extends PropertyExpression<Claim, OfflinePlayer> {

	static {
		register(ExprClaimOwner.class, OfflinePlayer.class, "[grief prevention] claim owner", "claims");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		//noinspection unchecked
		setExpr((Expression<? extends Claim>) exprs[0]);
		return true;
	}

	@Override
	protected OfflinePlayer[] get(Event event, Claim[] claims) {
		return get(claims, claim -> {
			UUID uuid = claim.getOwnerID();
			return Bukkit.getOfflinePlayer(uuid);
		});
	}

	@Override
	public Class<? extends OfflinePlayer> getReturnType() {
		return OfflinePlayer.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "claim owner";
	}

}
