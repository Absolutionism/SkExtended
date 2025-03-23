package com.sirsmurfy2.skextended.modules.shopkeepers.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Shopkeeper Owner")
@Description("The owner of a shopkeeper. The shopkeeper must be a player shopkeeper type.")
@Examples({
	"if {_shopkeeper} is a player shopkeeper:",
		"\tset {_owner} to the shopkeeper owner of {_shopkeeper}",
		"\tset the shopkeeper owner of {_shopkeeper} to {_player}"
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class ExprShopkeeperOwner extends SimplePropertyExpression<Shopkeeper, OfflinePlayer> {

	static {
		register(ExprShopkeeperOwner.class, OfflinePlayer.class, "shop[ ]keeper owner", "shopkeepers");
	}

	@Override
	public @Nullable OfflinePlayer convert(Shopkeeper shopkeeper) {
		if (!(shopkeeper instanceof PlayerShopkeeper playerShopkeeper))
			return null;
		return Bukkit.getOfflinePlayer(playerShopkeeper.getOwnerUUID());
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET)
			return CollectionUtils.array(OfflinePlayer.class);
		return null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		assert delta != null;
		OfflinePlayer player = (OfflinePlayer) delta[0];
		assert player.getName() != null;
		for (Shopkeeper shopkeeper : getExpr().getArray(event)) {
			if (!(shopkeeper instanceof PlayerShopkeeper playerShopkeeper))
				continue;
			playerShopkeeper.setOwner(player.getUniqueId(), player.getName());
		}
	}

	@Override
	protected String getPropertyName() {
		return "shopkeeper owner";
	}

	@Override
	public Class<? extends OfflinePlayer> getReturnType() {
		return OfflinePlayer.class;
	}

}
