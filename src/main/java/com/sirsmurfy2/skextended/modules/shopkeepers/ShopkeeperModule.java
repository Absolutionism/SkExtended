package com.sirsmurfy2.skextended.modules.shopkeepers;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.coll.CollectionUtils;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.block.BlockShopObject;
import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObject;
import com.sirsmurfy2.skextended.ModuleLoader;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converters;

public class ShopkeeperModule extends ModuleLoader {

	@Override
	public void loadModule() {
		Classes.registerClass(new ClassInfo<>(Shopkeeper.class, "shopkeeper")
			.user("shop ?keepers?")
			.name("Shopkeeper")
			.description("Represents a shopkeeper. A shopkeeper can be an entity or a block.")
			.since("1.0.0")
			.requiredPlugins("Shopkeepers")
			.changer(new Changer<Shopkeeper>() {
				@Override
				public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
					if (mode == ChangeMode.DELETE)
						return CollectionUtils.array();
					return null;
				}

				@Override
				public void change(Shopkeeper[] shopkeepers, Object @Nullable [] delta, ChangeMode mode) {
					for (Shopkeeper shopkeeper : shopkeepers)
						shopkeeper.delete();
				}
			})
			.defaultExpression(new EventValueExpression<>(Shopkeeper.class))
		);

		Converters.registerConverter(Entity.class, Shopkeeper.class, ShopkeeperUtils::getShopkeeper);
		Converters.registerConverter(Shopkeeper.class, Entity.class, shopkeeper -> {
			if (!(shopkeeper.getShopObject() instanceof EntityShopObject entityShopObject))
				return null;
			return entityShopObject.getEntity();
		});

		Converters.registerConverter(Block.class, Shopkeeper.class, ShopkeeperUtils::getShopkeeper);
		Converters.registerConverter(Shopkeeper.class, Block.class, shopkeeper -> {
			if (!(shopkeeper.getShopObject() instanceof BlockShopObject blockShopObject))
				return null;
			return blockShopObject.getBlock();
		});
	}

}
