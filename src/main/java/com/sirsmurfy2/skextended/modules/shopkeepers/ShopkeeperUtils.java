package com.sirsmurfy2.skextended.modules.shopkeepers;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ShopkeeperUtils {

	public static ShopkeeperRegistry shopkeeperRegistry;

	static {
		shopkeeperRegistry = ShopkeepersAPI.getShopkeeperRegistry();
	}

	public static @Nullable Shopkeeper getShopkeeper(int id) {
		return shopkeeperRegistry.getShopkeeperById(id);
	}

	public static @Nullable Shopkeeper getShopkeeper(Entity entity) {
		return shopkeeperRegistry.getShopkeeperByEntity(entity);
	}

	public static @Nullable Shopkeeper getShopkeeper(Block block) {
		return shopkeeperRegistry.getShopkeeperByBlock(block);
	}

	public static Shopkeeper[] getShopkeepers() {
		return shopkeeperRegistry.getAllShopkeepers().toArray(Shopkeeper[]::new);
	}

	public static Shopkeeper[] getShopkeepers(World world) {
		return shopkeeperRegistry.getShopkeepersInWorld(world.getName()).toArray(Shopkeeper[]::new);
	}

	public static Shopkeeper[] getShopkeepers(OfflinePlayer player) {
		return shopkeeperRegistry.getPlayerShopkeepersByOwner(player.getUniqueId()).toArray(Shopkeeper[]::new);
	}

}
