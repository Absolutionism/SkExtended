package com.sirsmurfy2.skextended.modules.shopkeepers.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.sirsmurfy2.skextended.modules.shopkeepers.ShopkeeperUtils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("All Shopkeepers")
@Description("Get all shopkeepers of all worlds, a world, and of a player.")
@Examples({
	"set {_shopkeepers::*} to all shopkeepers",
	"set {_shopkeepers::*} to all shopkeepers in world \"world\"",
	"set {_shopkeepers::*} to all player shopkeepers of event-player",
	"set {_shopkeepers::*} to all player shopkeepers of event-player in world \"world\""
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class ExprShopkeepers extends SimpleExpression<Shopkeeper> {

	static {
		Skript.registerExpression(ExprShopkeepers.class, Shopkeeper.class, ExpressionType.SIMPLE,
			"(all [[of] the]|the) shopkeepers [(in|from) %-worlds%]",
			"(all [[of] the]|the) player shopkeepers (of|from) %offlineplayers% [(in|from) %-worlds%]");
	}

	private @Nullable Expression<OfflinePlayer> players;
	private @Nullable Expression<World> worlds;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (exprs[0] != null) {
			if (matchedPattern == 0) {
				//noinspection unchecked
				worlds = (Expression<World>) exprs[0];
			} else {
				//noinspection unchecked
				players = (Expression<OfflinePlayer>) exprs[0];
				if (exprs[1] != null)
					//noinspection unchecked
					worlds = (Expression<World>) exprs[1];
			}
		}
		return true;
	}

	@Override
	protected Shopkeeper @Nullable [] get(Event event) {
		List<Shopkeeper> shopkeepers = new ArrayList<>();
		if (worlds == null && players == null) {
			shopkeepers.addAll(Arrays.stream(ShopkeeperUtils.getShopkeepers()).toList());
		} else if (players != null) {
			List<Shopkeeper> playerShopkeepers = new ArrayList<>();
			for (OfflinePlayer player : players.getArray(event))
				playerShopkeepers.addAll(Arrays.stream(ShopkeeperUtils.getShopkeepers(player)).toList());
			if (worlds != null) {
				//noinspection unchecked
				List<World> worlds = (List<World>) this.worlds.stream(event).toList();
				playerShopkeepers = playerShopkeepers.stream().filter(shopkeeper -> {
					Location location = shopkeeper.getLocation();
					if (location == null)
						return false;
					return worlds.contains(location.getWorld());
				}).toList();
			}
			shopkeepers = playerShopkeepers;
		} else {
			for (World world : worlds.getArray(event))
				shopkeepers.addAll(Arrays.stream(ShopkeeperUtils.getShopkeepers(world)).toList());
		}
		return shopkeepers.toArray(Shopkeeper[]::new);
	}

	@Override
	public boolean isSingle() {
		if (worlds != null && !worlds.isSingle())
			return false;
		if (players != null && !players.isSingle())
			return false;
		return true;
	}

	@Override
	public Class<? extends Shopkeeper> getReturnType() {
		return Shopkeeper.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
		if (players != null) {
			builder.append("all player shopkeepers of", players);
		} else {
			builder.append("all shopkeepers");
		}
		if (worlds != null)
			builder.append("in", worlds);
		return builder.toString();
	}

}
