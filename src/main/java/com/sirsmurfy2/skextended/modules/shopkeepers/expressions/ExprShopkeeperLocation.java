package com.sirsmurfy2.skextended.modules.shopkeepers.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

@Name("Shopkeeper Location")
@Description("Get the location of a shopkeeper.")
@Examples({
	"set {_loc} to the shopkeeper location of {_shopkeeper}"
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class ExprShopkeeperLocation extends SimplePropertyExpression<Shopkeeper, Location> {

	static {
		register(ExprShopkeeperLocation.class, Location.class, "[shop[ ]keeper] location", "shopkeepers");
	}

	@Override
	public @Nullable Location convert(Shopkeeper shopkeeper) {
		return shopkeeper.getLocation();
	}

	@Override
	protected String getPropertyName() {
		return "shopkeeper location";
	}

	@Override
	public Class<? extends Location> getReturnType() {
		return Location.class;
	}

}
