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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Player Vault Items")
@Description("The items stored in a player's vault/inventory.")
@Examples({
	"set {_items::*} to the player vault 1 items of event-player",
	"",
	"set the items in player vault 1 of event-player to diamond sword and netherite ingot",
	"",
	"clear the items in player vault 1 of event-player"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX")
public class ExprPlayerVaultItems extends SimpleExpression<ItemStack> {

	static {
		Skript.registerExpression(ExprPlayerVaultItems.class, ItemStack.class, ExpressionType.COMBINED,
			"[the] player vault[s] %integers% items (of|from) %offlineplayers%",
			"[the] items in [the] player vault[s] %integers% (of|from) %offlineplayers%",
			"%offlineplayers%'[s] player vault[s] %integers% items",
			"[the] items in %offlineplayers%'[s] player vault[s] %integers%");
	}

	private Expression<Integer> vaultNumbers;
	private Expression<OfflinePlayer> players;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		if (matchedPattern <= 1) {
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
	protected ItemStack @Nullable [] get(Event event) {
		Integer[] integers = vaultNumbers.getArray(event);
		List<ItemStack> itemStacks = new ArrayList<>();
		for (OfflinePlayer player : players.getArray(event)) {
			for (int integer : integers) {
				ItemStack[] theseItems = PlayerVaultUtils.getItems(player, integer);
				if (theseItems == null || theseItems.length == 0)
					continue;
				itemStacks.addAll(Arrays.stream(theseItems).toList());
			}
		}
		return itemStacks.toArray(ItemStack[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET || mode == ChangeMode.DELETE)
			return CollectionUtils.array(ItemStack[].class);
		return null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		ItemStack[] items = delta == null ? null : (ItemStack[]) delta;
		Integer[] integers = vaultNumbers.getArray(event);
		for (OfflinePlayer player : players.getArray(event)) {
			for (int integer : integers) {
				if (mode == ChangeMode.DELETE) {
					PlayerVaultUtils.deleteVault(player, integer);
				} else if (items != null) {
					PlayerVaultUtils.setItems(player, integer, items);
				}
			}
		}
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return (new SyntaxStringBuilder(event, debug).append("the player vault", vaultNumbers, "items of", players)).toString();
	}

}
