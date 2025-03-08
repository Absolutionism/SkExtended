package com.sirsmurfy2.skextended.modules.playervaults.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sirsmurfy2.skextended.utils.PlayerVaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Delete All Vaults")
@Description("Delete all vaults of all online and offline players.")
@Examples({
	"delete all player vaults"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX")
public class EffDeleteAllVaults extends Effect {

	static {
		Skript.registerEffect(EffDeleteAllVaults.class,
			"(delete|clear) all player vaults");
	}

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult) {
		return true;
	}

	@Override
	protected void execute(Event event) {
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			PlayerVaultUtils.deleteVaults(player);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "delete all player vaults";
	}

}
