package com.sirsmurfy2.skextended.modules.shopkeepers.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import com.nisovin.shopkeepers.api.events.*;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ShopkeeperSimpleEvents extends SkriptEvent {

	static {
		EventValues.registerEventValue(ShopkeeperEvent.class, Shopkeeper.class, ShopkeeperEvent::getShopkeeper);

		Skript.registerEvent("Player Shopkeeper Created", ShopkeeperSimpleEvents.class, PlayerCreatePlayerShopkeeperEvent.class,
			"[player] create player shop[ ]keeper",
				"player shop[ ]keeper creat(ed|ion)")
			.description("Called when a player creates a player shopkeeper.")
			.examples("on player create player shopkeeper")
			.since("1.0.0")
			.requiredPlugins("Shopkeepers");

		Skript.registerEvent("Shopkeeper Created", ShopkeeperSimpleEvents.class, PlayerCreateShopkeeperEvent.class,
			"[player] create shop[ ]keeper",
				"shop[ ]keeper creat(ed|ion)")
			.description("Called when a player creates a shopkeeper.")
			.examples("om player create shopkeeper:")
			.since("1.0.0")
			.requiredPlugins("Shopkeepers");
		EventValues.registerEventValue(PlayerCreateShopkeeperEvent.class, Player.class, event -> event.getShopCreationData().getCreator());

		Skript.registerEvent("Shopkeeper Deleted", ShopkeeperSimpleEvents.class, PlayerDeleteShopkeeperEvent.class,
			"[player] delete shop[ ]keeper",
			"shop[ ]keeper delet(ed|ion)")
			.description("Called when a player deletes a shopkeeper.")
			.examples("on player delete shopkeeper:")
			.since("1.0.0")
			.requiredPlugins("Shopkeepers");
		EventValues.registerEventValue(PlayerDeleteShopkeeperEvent.class, Player.class, PlayerDeleteShopkeeperEvent::getPlayer);

		Skript.registerEvent("Shopkeeper Hired", ShopkeeperSimpleEvents.class, PlayerShopkeeperHireEvent.class,
			"[player] hire[d] shop[ ]keeper",
				"shop[ ]keeper hired")
			.description("Called when a player hires a shopkeeper.")
			.examples("on player hire shopkeeper:")
			.since("1.0.0")
			.requiredPlugins("Shopkeepers");
		EventValues.registerEventValue(PlayerShopkeeperHireEvent.class, Player.class, PlayerShopkeeperHireEvent::getPlayer);

		Skript.registerEvent("Shopkeeper Added", ShopkeeperSimpleEvents.class, ShopkeeperAddedEvent.class,
			"shop[ ]keeper added")
			.description("Called when a shopkeeper is created or loaded.")
			.examples("on shopkeeper added:")
			.since("1.0.0")
			.requiredPlugins("Shopkeepers");

		Skript.registerEvent("Shopkeeper Edited", ShopkeeperSimpleEvents.class, ShopkeeperEditedEvent.class,
			"[player] edit shop[ ]keeper",
			"shop[ ]keeper edited")
			.description("Called when a player has finished editing a shopkeeper.")
			.examples("on player edit shopkeeper:")
			.since("1.0.0")
			.requiredPlugins("Shopkeepers");
		EventValues.registerEventValue(ShopkeeperEditedEvent.class, Player.class, ShopkeeperEditedEvent::getPlayer);

		Skript.registerEvent("Shopkeeper Removed", ShopkeeperSimpleEvents.class, ShopkeeperRemoveEvent.class,
			"shop[ ]keeper remov(ed|al)")
			.description("Called when a shopkeeper is deleted or unloaded.")
			.examples("on shopkeeper removed:")
			.since("1.0.0")
			.requiredPlugins("Shopkeepers");

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
		return null;
	}
}
