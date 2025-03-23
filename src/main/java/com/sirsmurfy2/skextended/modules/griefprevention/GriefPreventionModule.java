package com.sirsmurfy2.skextended.modules.griefprevention;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import com.sirsmurfy2.skextended.ModuleLoader;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.events.ClaimEvent;
import org.bukkit.World;
import org.skriptlang.skript.lang.converter.Converters;

public class GriefPreventionModule extends ModuleLoader {

	@Override
	public void loadModule() {
		Classes.registerClass(new ClassInfo<>(Claim.class, "claim")
			.user("claims?")
			.name("Claim")
			.description("Represents a claim in GriefPreventions")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention")
			.supplier(GriefPreventionUtils.getClaims())
		);
		Classes.registerClass(new EnumClassInfo<>(ClaimPermission.class, "claimpermission", "claim permissions")
			.user("claim ?permissions?")
			.name("Claim Permission")
			.description("Represents a permission of a claim.")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention")
		);

		EventValues.registerEventValue(ClaimEvent.class, Claim.class, ClaimEvent::getClaim);

		Converters.registerConverter(Claim.class, World.class, claim -> claim.getLesserBoundaryCorner().getWorld());
	}

}
