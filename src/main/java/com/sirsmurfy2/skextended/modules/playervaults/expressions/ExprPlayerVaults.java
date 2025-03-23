package com.sirsmurfy2.skextended.modules.playervaults.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.sirsmurfy2.skextended.modules.playervaults.PlayerVaultUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Player Vaults")
@Description("All player vaults/inventories of a player.")
@Examples({
	"set {_vaults::*} to all the player vaults of event-player",
	"",
	"delete all the player vaults of event-player"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX")
public class ExprPlayerVaults extends PropertyExpression<OfflinePlayer, Inventory> {

	static {
		Skript.registerExpression(ExprPlayerVaults.class, Inventory.class, ExpressionType.SIMPLE,
			"all [[of] the] player vaults (of|from) %offlineplayers%");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		//noinspection unchecked
		setExpr((Expression<? extends OfflinePlayer>) exprs[0]);
		return true;
	}

	@Override
	protected Inventory[] get(Event event, OfflinePlayer[] offlinePlayers) {
		List<Inventory> inventories = new ArrayList<>();
		for (OfflinePlayer player : offlinePlayers) {
			for (int integer : PlayerVaultUtils.getVaultNumbers(player)) {
				inventories.add(PlayerVaultUtils.getVault(player, integer));
			}
		}
		return inventories.toArray(Inventory[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.DELETE)
			return CollectionUtils.array();
		return null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		for (OfflinePlayer player : getExpr().getArray(event)) {
			PlayerVaultUtils.deleteVaults(player);
		}
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Inventory> getReturnType() {
		return Inventory.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return (new SyntaxStringBuilder(event, debug).append("all player vaults of", getExpr())).toString();
	}

}
