package com.sirsmurfy2.skextended.modules.playervaults.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import com.sirsmurfy2.skextended.utils.PlayerVaultUtils;
import org.bukkit.inventory.Inventory;

@Name("Inventory Is Player Vault")
@Description("Whether an inventory is a player vault.")
@Examples({
	"on inventory click:",
		"\tif event-inventory is not a player vault:",
			"\t\tcancel event"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX")
public class CondIsPlayerVault extends PropertyCondition<Inventory> {

	static {
		register(CondIsPlayerVault.class, "[a] player vault[s] [inventor(y|ies)]", "inventories");
	}

	@Override
	public boolean check(Inventory inventory) {
		return PlayerVaultUtils.inventoryIsVault(inventory);
	}

	@Override
	protected String getPropertyName() {
		return "player vaults";
	}

}
