package com.sirsmurfy2.skextended.modules.shopkeepers.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;

@Name("Shopkeeper Is Player Shopkeeper")
@Description("Whether a shopkeeper is a player shopkeeper.")
@Examples({
	"if {_shopkeper} is a player shopkeeper:"
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class CondIsPlayerShopkeeper extends PropertyCondition<Shopkeeper> {

	static {
		register(CondIsPlayerShopkeeper.class, "[a] player shop[ ]keeper[s]", "shopkeepers");
	}

	@Override
	public boolean check(Shopkeeper shopkeeper) {
		return shopkeeper instanceof PlayerShopkeeper;
	}

	@Override
	protected String getPropertyName() {
		return "player shopkeeper";
	}

}
