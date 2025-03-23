package com.sirsmurfy2.skextended.modules.playervaults;

import com.drtshock.playervaults.vaultmanagement.VaultHolder;
import com.drtshock.playervaults.vaultmanagement.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PlayerVaultUtils {

	private static final VaultManager vaultManager;

	static {
		vaultManager = VaultManager.getInstance();
	}

	public static boolean vaultExists(OfflinePlayer player, int vault) {
		return vaultManager.vaultExists(player.getUniqueId().toString(), vault);
	}

	public static Set<Integer> getVaultNumbers(OfflinePlayer player) {
		return vaultManager.getVaultNumbers(player.getUniqueId().toString());
	}

	public static void deleteVault(OfflinePlayer player, int vault) {
		if (!vaultExists(player, vault))
			return;
		vaultManager.deleteVault(Bukkit.getConsoleSender(), player.getUniqueId().toString(), vault);
	}

	public static void deleteVaults(OfflinePlayer player) {
		vaultManager.deleteAllVaults(player.getUniqueId().toString());
	}

	public static @Nullable Inventory getVault(OfflinePlayer player, int vault) {
		if (!vaultExists(player, vault))
			return null;
		return vaultManager.getVault(player.getUniqueId().toString(), vault);
	}

	public static void setVault(OfflinePlayer player, int vault, Inventory inventory) {
		vaultManager.saveVault(inventory, player.getUniqueId().toString(), vault);
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
