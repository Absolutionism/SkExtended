package com.sirsmurfy2.skextended.modules.griefprevention.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import com.griefprevention.events.BoundaryVisualizationEvent;
import com.griefprevention.visualization.Boundary;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.events.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class GriefPreventionSimpleEvents extends SkriptEvent {

	static {
		Skript.registerEvent("Accrue Claim Blocks", GriefPreventionSimpleEvents.class, AccrueClaimBlocksEvent.class,
				"[grief prevention] [player] (accrue|gain)[ed] claim blocks")
			.description("Called when a player gains claim blocks.")
			.examples(
				"on player gained claim blocks:",
				"on grief prevention accrue claim blocks:"
			)
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(AccrueClaimBlocksEvent.class, Player.class, AccrueClaimBlocksEvent::getPlayer);

		Skript.registerEvent("Claim Change", GriefPreventionSimpleEvents.class, ClaimChangeEvent.class,
				"[grief prevention] claim change[d]")
			.description("Called when a claim is changed.")
			.examples("on claim changed:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(ClaimChangeEvent.class, Claim.class, ClaimChangeEvent::getFrom, EventValues.TIME_PAST);
		EventValues.registerEventValue(ClaimChangeEvent.class, Claim.class, ClaimChangeEvent::getTo);

		Skript.registerEvent("Claim Created", GriefPreventionSimpleEvents.class, ClaimCreatedEvent.class,
				"[grief prevention] claim create[d]")
			.description("Called when a claim is created by a player or console.")
			.examples("on claim created:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(ClaimCreatedEvent.class, CommandSender.class, ClaimCreatedEvent::getCreator);
		EventValues.registerEventValue(ClaimCreatedEvent.class, Player.class, event -> {
			if (event.getCreator() instanceof Player player)
				return player;
			return null;
		});

		Skript.registerEvent("Claim Deleted", GriefPreventionSimpleEvents.class, ClaimDeletedEvent.class,
				"[grief prevention] claim delete[d]")
			.description("Called when a claim is deleted.")
			.examples("on claim deleted:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");

		Skript.registerEvent("Claim Expired", GriefPreventionSimpleEvents.class, ClaimExpirationEvent.class,
			"[grief prevention] claim expire[d]")
			.description("Called when a claim is expired due to the inactivity of the claim's owner.")
			.examples("on claim expired:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");

		Skript.registerEvent("Claim Extended", GriefPreventionSimpleEvents.class, ClaimExtendEvent.class,
			"[grief prevention] claim extend[ed] [downward[s]]")
			.description("Called when a claim's lower Y boundary is extended downwards.")
			.examples("on claim extended:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");

		Skript.registerEvent("Claim Inspected", GriefPreventionSimpleEvents.class, ClaimInspectionEvent.class,
			"[grief prevention] claim inspect[ed|ion]")
			.description("Called when a player inspects a claim.")
			.examples("on claim inspection:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(ClaimInspectionEvent.class, Claim[].class, event ->
			event.getClaims().toArray(Claim[]::new)
		);
		EventValues.registerEventValue(ClaimInspectionEvent.class, Block.class, ClaimInspectionEvent::getInspectedBlock);

		Skript.registerEvent("Claim Permission Check", GriefPreventionSimpleEvents.class, ClaimPermissionCheckEvent.class,
			"[grief prevention] claim permission check")
			.description("Called when a claim checks the permissions of a player.")
			.examples("on claim permission check:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(ClaimPermissionCheckEvent.class, Player.class, ClaimPermissionCheckEvent::getCheckedPlayer);
		EventValues.registerEventValue(ClaimPermissionCheckEvent.class, ClaimPermission.class, ClaimPermissionCheckEvent::getRequiredPermission);

		Skript.registerEvent("Claim Resized", GriefPreventionSimpleEvents.class, ClaimResizeEvent.class,
			"[grief prevention] claim resize[d]")
			.description("Called when a claim is resized.")
			.examples("on claim resized:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(ClaimResizeEvent.class, CommandSender.class, ClaimResizeEvent::getModifier);
		EventValues.registerEventValue(ClaimResizeEvent.class, Player.class, event -> {
			if (event.getModifier() instanceof Player player)
				return player;
			return null;
		});

		Skript.registerEvent("Claim Transferred", GriefPreventionSimpleEvents.class, ClaimTransferEvent.class,
			"[grief prevention] claim transfer[red]")
			.description("Called when a claim is transferred ownership.")
			.examples("on claim transferred:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(ClaimTransferEvent.class, OfflinePlayer.class, event -> {
			UUID uuid = event.getClaim().getOwnerID();
			return Bukkit.getOfflinePlayer(uuid);
		}, EventValues.TIME_PAST);
		EventValues.registerEventValue(ClaimTransferEvent.class, OfflinePlayer.class, event -> {
			UUID uuid = event.getNewOwner();
			if (uuid == null)
				return null;
			return Bukkit.getOfflinePlayer(uuid);
		});

		Skript.registerEvent("Multi Claim", GriefPreventionSimpleEvents.class, MultiClaimEvent.class,
			"[grief prevention] multi claim[s|ed]")
			.description("Called when an event is called involving multiple claims.")
			.examples("on multi claim:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(MultiClaimEvent.class, Claim[].class, event ->
			event.getClaims().toArray(Claim[]::new)
		);

		Skript.registerEvent("Claim Visualization", GriefPreventionSimpleEvents.class, BoundaryVisualizationEvent.class,
			"[grief prevention] (boundary|claim) visualization")
			.description("Called when a player is receiving visuals of a boundary.")
			.examples("on boundary visualization:")
			.since("1.0.0")
			.requiredPlugins("GriefPrevention");
		EventValues.registerEventValue(BoundaryVisualizationEvent.class, Claim[].class, event -> {
			Collection<Boundary> boundaries = event.getBoundaries();
			List<Claim> claims = new ArrayList<>();
			boundaries.forEach(boundary -> claims.add(boundary.claim()));
			return claims.toArray(Claim[]::new);
		});
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
		if (event instanceof AccrueClaimBlocksEvent) {
			return "player accrued claim blocks";
		} else if (event instanceof ClaimExtendEvent) {
			return "claim extended";
		} else if (event instanceof ClaimChangeEvent) {
			return "claim changed";
		} else if (event instanceof ClaimCreatedEvent) {
			return "claim created";
		} else if (event instanceof ClaimDeletedEvent) {
			return "claim deleted";
		} else if (event instanceof ClaimExpirationEvent) {
			return "claim expired";
		} else if (event instanceof ClaimInspectionEvent) {
			return "claim inspected";
		}
		return null;
	}

}
