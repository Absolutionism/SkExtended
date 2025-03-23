package com.sirsmurfy2.skextended.modules.shopkeepers.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Shopkeeper Name")
@Description("The name of a shopkeeper.")
@Examples({
	"set {_name} to the shopkeeper name of {_shopkeeper}",
	"set the shopkeeper name of {_shopkeeper} to \"Forge\""
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class ExprShopkeeperName extends SimplePropertyExpression<Shopkeeper, String> {

	static {
		register(ExprShopkeeperName.class, String.class, "[shop[ ]keeper] name", "shopkeepers");
	}

	@Override
	public @Nullable String convert(Shopkeeper shopkeeper) {
		return shopkeeper.getName();
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET)
			return CollectionUtils.array(String.class);
		return null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
        assert delta != null;
        String name = (String) delta[0];
		for (Shopkeeper shopkeeper : getExpr().getArray(event))
			shopkeeper.setName(name);
	}

	@Override
	protected String getPropertyName() {
		return "shopkeeper name";
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

}
