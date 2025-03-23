package com.sirsmurfy2.skextended.modules.playervaults.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.util.coll.CollectionUtils;
import com.drtshock.playervaults.events.BlacklistedItemEvent;
import com.drtshock.playervaults.events.BlacklistedItemEvent.Reason;
import com.sirsmurfy2.skextended.lang.SimpleEventRestrictedExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Blacklisted Reasons")
@Description({
	"The reasons why an item is blacklisted from player vaults.",
	"Supports removing reasons, only the first reason will be displayed to the player.",
	"Removing/Deleting all reasons has the same effect as cancelling the event, allowing the player to put the item "
		+ "in their player vault."
})
@Examples({
	"on blacklisted item:",
		"\tif event-item is {SpecialItem}:",
			"\t\tclear the blacklisted reasons"
})
@Since("1.0.0")
@RequiredPlugins("PlayerVaultsX 4.4.0+")
public class ExprBlacklistedReasons extends SimpleEventRestrictedExpression<BlacklistedItemEvent, Reason> {

	static {
		if (Skript.classExists("com.drtshock.playervaults.events.BlacklistedItemEvent"))
			register(ExprBlacklistedReasons.class, Reason.class, "[the] black[ ]listed reasons");
	}

	@Override
	public Reason @Nullable [] convert(BlacklistedItemEvent event) {
		return event.getReasons().toArray(Reason[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.REMOVE || mode == ChangeMode.DELETE)
			return CollectionUtils.array(Reason[].class);
		return null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (!(event instanceof BlacklistedItemEvent blacklistedItemEvent))
			return;
		Reason[] reasons = delta == null ? null : (Reason[]) delta;
		if (mode == ChangeMode.REMOVE && reasons != null) {
			for (Reason reason : reasons)
				blacklistedItemEvent.removeReason(reason);
		} else if (mode == ChangeMode.DELETE) {
			for (Reason reason : blacklistedItemEvent.getReasons())
				blacklistedItemEvent.removeReason(reason);
		}
	}

	@Override
	public Class<? extends BlacklistedItemEvent> getEventClass() {
		return BlacklistedItemEvent.class;
	}

    @Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Reason> getReturnType() {
		return Reason.class;
	}

}
