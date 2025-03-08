package com.sirsmurfy2.skextended.modules.playervaults.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import com.drtshock.playervaults.events.BlacklistedItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EvtBlacklistedItem extends SkriptEvent {

	static {
		if (Skript.classExists("com.drtshock.playervaults.events.BlacklistedItemEvent")) {
			Skript.registerEvent("Blacklisted Item", EvtBlacklistedItem.class, BlacklistedItemEvent.class,
					"black[ ]listed item")
				.description("Called when a player attempts to put a blacklisted item into their vault.")
				.examples(
					"on black listed item:",
						"\tevent-player"
				)
				.since("1.0.0")
				.requiredPlugins("PlayerVaultsX 4.4.0+");

			EventValues.registerEventValue(BlacklistedItemEvent.class, Player.class, BlacklistedItemEvent::getActingPlayer);
			EventValues.registerEventValue(BlacklistedItemEvent.class, ItemStack.class, BlacklistedItemEvent::getItem);
		}
	}

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean check(Event event) {
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "blacklisted item";
	}

}
