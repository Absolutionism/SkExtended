package com.sirsmurfy2.skextended.modules.shopkeepers.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopkeeper;

@Name("Shopkeeper Is Admin Shopkeeper")
@Description("Whether a shopkeeper is an admin shopkeeper.")
@Examples({
	"if {_shopkeeper} is an admin shopkeeper:"
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class CondIsAdminShopkeeper extends PropertyCondition<Shopkeeper> {

	static {
		register(CondIsAdminShopkeeper.class, "[an] admin shop[ ]keeper[s]", "shopkeepers");
	}

	@Override
	public boolean check(Shopkeeper shopkeeper) {
		return shopkeeper instanceof AdminShopkeeper;
	}

	@Override
	protected String getPropertyName() {
		return "admin shopkeeper";
	}
}
