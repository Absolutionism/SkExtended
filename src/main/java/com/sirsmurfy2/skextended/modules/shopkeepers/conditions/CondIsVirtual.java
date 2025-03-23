package com.sirsmurfy2.skextended.modules.shopkeepers.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

@Name("Shopkeeper Is Virtual")
@Description("Whether a shopkeeper is virtual.")
@Examples({
	"if {_shopkeeper} is a virtual shopkeeper:"
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class CondIsVirtual extends PropertyCondition<Shopkeeper> {

	static {
		register(CondIsVirtual.class, "[a] virtual [shop[ ]keeper[s]]", "shopkeepers");
	}

	@Override
	public boolean check(Shopkeeper shopkeeper) {
		return shopkeeper.isVirtual();
	}

	@Override
	protected String getPropertyName() {
		return null;
	}
}
