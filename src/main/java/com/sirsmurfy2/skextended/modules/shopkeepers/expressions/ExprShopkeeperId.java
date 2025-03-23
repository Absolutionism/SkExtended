package com.sirsmurfy2.skextended.modules.shopkeepers.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.jetbrains.annotations.Nullable;

@Name("Shopkeeper ID")
@Description("Get the ID of a shopkeeper.")
@Examples({
	"if the shopkeeper id of {_shopkeeper} is 1:"
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class ExprShopkeeperId extends SimplePropertyExpression<Shopkeeper, Integer> {

	static {
		register(ExprShopkeeperId.class, Integer.class, "[shop[ ]keeper] id[entifier][s]", "shopkeepers");
	}

	@Override
	public @Nullable Integer convert(Shopkeeper shopkeeper) {
		return shopkeeper.getId();
	}

	@Override
	protected String getPropertyName() {
		return "shopkeeper id";
	}

	@Override
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}

}
