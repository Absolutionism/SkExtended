package com.sirsmurfy2.skextended.modules.playervaults.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import com.drtshock.playervaults.events.BlacklistedItemEvent;
import com.sirsmurfy2.skextended.lang.SimpleEventRestrictedExpression;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Name("Blacklisted Vault Owner")
@Description("The owner of the vault that is attempting to have a blacklisted item added to.")
@Examples({
	"on blacklisted item:",
		"\tif the blacklisted vault owner is op:",
			"\t\tcancel event"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX 4.4.0+")
public class ExprBlacklistedVaultOwner extends SimpleEventRestrictedExpression<BlacklistedItemEvent, OfflinePlayer> {

	static {
		if (Skript.classExists("com.drtshock.playervaults.events.BlacklistedItemEvent"))
			register(ExprBlacklistedVaultOwner.class, OfflinePlayer.class, "[the] black[ ]listed vault owner");
	}

	@Override
	public OfflinePlayer @Nullable [] convert(BlacklistedItemEvent event) {
		UUID uuid = UUID.fromString(event.getOwner());
		return new OfflinePlayer[] {Bukkit.getOfflinePlayer(uuid)};
	}

	@Override
	public Class<? extends BlacklistedItemEvent> getEventClass() {
		return BlacklistedItemEvent.class;
	}

    @Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends OfflinePlayer> getReturnType() {
		return OfflinePlayer.class;
	}

}
