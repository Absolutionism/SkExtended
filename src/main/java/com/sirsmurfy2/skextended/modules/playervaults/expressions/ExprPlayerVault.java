package com.sirsmurfy2.skextended.modules.playervaults.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.sirsmurfy2.skextended.modules.playervaults.PlayerVaultUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Player Vault")
@Description("The player vault/inventory of a player.")
@Examples({
	"set {_inventory} to the player vault 1 of event-player",
	"",
	"set the player vault 1 of event-player to new chest inventory with 3 rows",
	"",
	"clear event-player's player vault 1"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX")
public class ExprPlayerVault extends SimpleExpression<Inventory> {

	static {
		Skript.registerExpression(ExprPlayerVault.class, Inventory.class, ExpressionType.COMBINED,
			"[the] player vault[s] %integers% (of|from) %offlineplayers%",
			"%offlineplayers%'[s] player vault[s] %integers%");
	}

	private Expression<Integer> vaultNumbers;
	private Expression<OfflinePlayer> players;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		if (matchedPattern == 0) {
			//noinspection unchecked
			vaultNumbers = (Expression<Integer>) exprs[0];
			//noinspection unchecked
			players = (Expression<OfflinePlayer>) exprs[1];
		} else {
			//noinspection unchecked
			players = (Expression<OfflinePlayer>) exprs[0];
			//noinspection unchecked
			vaultNumbers = (Expression<Integer>) exprs[1];
		}
		return true;
	}

	@Override
	protected Inventory @Nullable [] get(Event event) {
		Integer[] integers = vaultNumbers.getArray(event);
		List<Inventory> inventories = new ArrayList<>();
		for (OfflinePlayer player : players.getArray(event)) {
			for (int integer : integers) {
				Inventory inventory = PlayerVaultUtils.getVault(player, integer);
				if (inventory == null)
					continue;
				inventories.add(inventory);
			}
		}
		return inventories.toArray(new Inventory[0]);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET || mode == ChangeMode.DELETE)
			return CollectionUtils.array(Inventory.class);
		return null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		Inventory inventory = delta == null ? null : (Inventory) delta[0];
		Integer[] integers = vaultNumbers.getArray(event);
		for (OfflinePlayer player : players.getArray(event)) {
			for (int integer : integers) {
				if (mode == ChangeMode.DELETE) {
					PlayerVaultUtils.deleteVault(player, integer);
				} else {
					PlayerVaultUtils.setVault(player, integer, inventory);
				}
			}
		}
	}

	@Override
	public boolean isSingle() {
		return vaultNumbers.isSingle() && players.isSingle();
	}

	@Override
	public Class<? extends Inventory> getReturnType() {
		return Inventory.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
		builder.append("the player vault", vaultNumbers, "of", players);
		return builder.toString();
	}

}
