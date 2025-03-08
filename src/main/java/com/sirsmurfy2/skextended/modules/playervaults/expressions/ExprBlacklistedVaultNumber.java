package com.sirsmurfy2.skextended.modules.playervaults.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import com.drtshock.playervaults.events.BlacklistedItemEvent;
import com.sirsmurfy2.skextended.lang.SimpleEventValueExpression;
import org.jetbrains.annotations.Nullable;

@Name("Blacklisted Vault Number")
@Description("The vault number of the vault owner that a blacklisted item is attempting to be added in to.")
@Examples({
	"on blacklisted item:",
		"\tif the blacklisted vault number is 7:",
			"\t\tcancel event"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX 4.4.0+")
public class ExprBlacklistedVaultNumber extends SimpleEventValueExpression<BlacklistedItemEvent, Integer> {

	static {
		if (Skript.classExists("com.drtshock.playervaults.events.BlacklistedItemEvent"))
			register(ExprBlacklistedVaultNumber.class, Integer.class, "[the] black[ ]listed vault (number|id)");
	}

	@Override
	public Integer @Nullable [] convert(BlacklistedItemEvent event) {
		return new Integer[] {event.getVaultNumber()};
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
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}

}
