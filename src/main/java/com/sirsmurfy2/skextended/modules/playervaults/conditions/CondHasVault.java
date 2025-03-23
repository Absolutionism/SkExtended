package com.sirsmurfy2.skextended.modules.playervaults.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sirsmurfy2.skextended.modules.playervaults.PlayerVaultUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Player Has Vault")
@Description("Whether a player has a specific vault.")
@Examples({
	"if player have player vault 1:",
		"\tdelete player vault 1 of player"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX")
public class CondHasVault extends Condition {

	static {
		Skript.registerCondition(CondHasVault.class, ConditionType.PROPERTY,
			"%offlineplayers% (has|have) player vault[s] %integers%",
			"%offlineplayers% (does not|doesn't) have player vault[s] %integers%");
	}

	private Expression<OfflinePlayer> players;
	private Expression<Integer> vaultNumbers;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		//noinspection unchecked
		players = (Expression<OfflinePlayer>) exprs[0];
		//noinspection unchecked
		vaultNumbers = (Expression<Integer>) exprs[1];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		Integer[] integers = vaultNumbers.getArray(event);
		return players.check(event, player ->
				SimpleExpression.check(integers, integer ->
					PlayerVaultUtils.vaultExists(player, integer)
				, false, vaultNumbers.getAnd())
		, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
		builder.append(players);
		if (isNegated())
			builder.append("does not");
		builder.append("have player vaults", vaultNumbers);
		return builder.toString();
	}

}
