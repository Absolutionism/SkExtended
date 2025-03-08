package com.sirsmurfy2.skextended.modules.griefprevention.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.EventRestrictedSyntax;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.ryanhamshire.GriefPrevention.events.TrustChangedEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Trust Is Given")
@Description("Whether trust is being given or removed in a \"trust changed\" event.")
@Examples({
	"on trust changed:",
		"\tif the trust is being removed:",
			"\t\tcancel event"
})
@Since("1.0.0")
@RequiredPlugins("GriefPrevention")
public class CondTrustIsGiven extends Condition implements EventRestrictedSyntax {

	static {
		Skript.registerCondition(CondTrustIsGiven.class, "[the] trust is [being] (given|:removed)");
	}

	private boolean given;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		given = !parseResult.hasTag("removed");
		return true;
	}

	@Override
	public Class<? extends Event>[] supportedEvents() {
		return CollectionUtils.array(TrustChangedEvent.class);
	}

	@Override
	public boolean check(Event event) {
		if (!(event instanceof TrustChangedEvent trustChangedEvent))
			return false;
		return trustChangedEvent.isGiven() == given;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "the trust is being ";
	}

}
