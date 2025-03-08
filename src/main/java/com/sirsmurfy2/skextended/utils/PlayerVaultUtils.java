package com.sirsmurfy2.skextended.utils;

import com.drtshock.playervaults.vaultmanagement.VaultHolder;
import com.drtshock.playervaults.vaultmanagement.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PlayerVaultUtils {

	public static boolean vaultExists(OfflinePlayer player, int vault) {
		return VaultManager.getInstance().vaultExists(player.getUniqueId().toString(), vault);
	}

	public static Set<Integer> getVaultNumbers(OfflinePlayer player) {
		return VaultManager.getInstance().getVaultNumbers(player.getUniqueId().toString());
	}

	public static void deleteVault(OfflinePlayer player, int vault) {
		if (!vaultExists(player, vault))
			return;
		VaultManager.getInstance().deleteVault(Bukkit.getConsoleSender(), player.getUniqueId().toString(), vault);
	}

	public static void deleteVaults(OfflinePlayer player) {
		VaultManager.getInstance().deleteAllVaults(player.getUniqueId().toString());
	}

	public static @Nullable Inventory getVault(OfflinePlayer player, int vault) {
		if (!vaultExists(player, vault))
			return null;
		return VaultManager.getInstance().getVault(player.getUniqueId().toString(), vault);
	}

	public static void setVault(OfflinePlayer player, int vault, Inventory inventory) {
		VaultManager.getInstance().saveVault(inventory, player.getUniqueId().toString(), vault);
	}

	public static ItemStack @Nullable [] getItems(OfflinePlayer player, int vault) {
		Inventory inventory = getVault(player, vault);
		if (inventory == null)
			return null;
		return inventory.getContents();
	}

	public static void setItems(OfflinePlayer player, int vault, ItemStack[] items) {
		Inventory inventory = Bukkit.createInventory(null, items.length);
		inventory.setContents(items);
		setVault(player, vault, inventory);
	}

	public static boolean inventoryIsVault(Inventory inventory) {
		return inventory.getHolder() instanceof VaultHolder;
	}

}
