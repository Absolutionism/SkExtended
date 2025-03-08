package com.sirsmurfy2.skextended.modules.griefprevention.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.events.TrustChangedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EvtTrustChanged extends SkriptEvent {

	private enum TrustChange {
		CHANGED, REMOVED, ADDED, PERMISSION
	}

	static {
		Skript.registerEvent("Trust Changed", EvtTrustChanged.class, TrustChangedEvent.class,
				"[grief prevention] trust changed",
				"[grief prevention] trust removed",
				"[grief prevention] trust added",
				"[grief prevention] permission changed")
			.description(
				"Called when a player changes the trust or permission of another player for a claim.",
				"The 'event-player' is the player that is changing the trust/permissions of another player.",
				"The 'trust affected player' is the player that is having their trust/permissions changed."
			)
			.examples("on trust changed:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");

		EventValues.registerEventValue(TrustChangedEvent.class, ClaimPermission.class, TrustChangedEvent::getClaimPermission);
		EventValues.registerEventValue(TrustChangedEvent.class, Player.class, TrustChangedEvent::getChanger);
	}

	private TrustChange trustChange;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		trustChange = TrustChange.values()[matchedPattern];
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (!(event instanceof TrustChangedEvent trustChangedEvent))
			return false;
		return switch (trustChange) {
			case REMOVED -> !trustChangedEvent.isGiven();
			case ADDED -> trustChangedEvent.isGiven();
			case PERMISSION -> trustChangedEvent.getClaimPermission() != null;
			default -> true;
		};
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return null;
	}
}
