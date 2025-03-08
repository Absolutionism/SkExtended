package com.sirsmurfy2.skextended.modules.playervaults;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.registrations.Classes;
import com.drtshock.playervaults.events.BlacklistedItemEvent.Reason;
import com.sirsmurfy2.skextended.ModuleLoader;

public class PlayerVaultsModule extends ModuleLoader {

	@Override
	public void loadModule() {
		if (Skript.classExists("com.drtshock.playervaults.events.BlacklistedItemEvent$Reason")) {
			Classes.registerClass(new EnumClassInfo<>(Reason.class, "blacklistedreasontype", "blacklisted reason types")
				.user("black ?listed ?reason ?types?")
				.name("Blacklisted Reason Types")
				.description("Represents the type of reasons why an item is blacklisted from player vaults.")
				.since("1.0.0")
				.requiredPlugins("PlayerVaultsX 4.4.0+")
			);
		}
	}

}
