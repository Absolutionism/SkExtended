package com.sirsmurfy2.skextended.modules.shopkeepers.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.sirsmurfy2.skextended.modules.shopkeepers.ShopkeeperUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Shopkeeper From ID")
@Description("Get a shopkeeper from an id.")
@Examples({
	"set {_shopkeeper} to the shopkeeper from the id 1"
})
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class ExprShopkeeperFromId extends SimpleExpression<Shopkeeper> {

	static {
		Skript.registerExpression(ExprShopkeeperFromId.class, Shopkeeper.class, ExpressionType.SIMPLE,
			"[the] shop[ ]keeper[s] (from|with) [the] id[entifier][s] %integers%");
	}

	private Expression<Integer> ids;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		//noinspection unchecked
		ids = (Expression<Integer>) exprs[0];
		return true;
	}

	@Override
	protected Shopkeeper @Nullable [] get(Event event) {
		List<Shopkeeper> shopkeepers = new ArrayList<>();
		for (int id : ids.getArray(event)) {
			shopkeepers.add(ShopkeeperUtils.getShopkeeper(id));
		}
		return shopkeepers.toArray(Shopkeeper[]::new);
	}

	@Override
	public boolean isSingle() {
		return ids.isSingle();
	}

	@Override
	public Class<? extends Shopkeeper> getReturnType() {
		return Shopkeeper.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "the shopkeepers with the ids " + ids.toString(event, debug);
	}

}
