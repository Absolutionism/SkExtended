package com.sirsmurfy2.skextended.modules.griefprevention.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.util.Math2;
import ch.njol.util.coll.CollectionUtils;
import com.sirsmurfy2.skextended.lang.SimpleEventValueExpression;
import me.ryanhamshire.GriefPrevention.events.AccrueClaimBlocksEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Accrued Claim Blocks")
@Description("The accrued claim blocks in an \"player accrued claim blocks\" event.")
@Examples({
	"on accrued claim blocks:",
	"\tif event-player is op:",
	"\t\tset the accrued claim blocks to 100000"
})
@Since("1.0.0")
@RequiredPlugins("GriefPrevention")
public class ExprAccruedClaimBlocks extends SimpleEventValueExpression<AccrueClaimBlocksEvent, Integer> {

	static {
		register(ExprAccruedClaimBlocks.class, Integer.class, "[the] [grief prevention] (accrued|gained) claim blocks");
	}

	@Override
	public Integer @Nullable [] convert(AccrueClaimBlocksEvent event) {
		return new Integer[] {event.getBlocksToAccrue()};
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, ADD, REMOVE, DELETE -> CollectionUtils.array(Integer.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (!(event instanceof AccrueClaimBlocksEvent accrueClaimBlocksEvent))
			return;
		int claimBlocks = delta == null ? 0 : (int) delta[0];
		switch (mode) {
			case SET, DELETE -> accrueClaimBlocksEvent.setBlocksToAccrue(claimBlocks);
			case ADD -> {
				int current = accrueClaimBlocksEvent.getBlocksToAccrue();
				int value = Math2.fit(0, current + claimBlocks, Integer.MAX_VALUE);
				accrueClaimBlocksEvent.setBlocksToAccrue(value);
			}
			case REMOVE -> {
				int current = accrueClaimBlocksEvent.getBlocksToAccrue();
				int value = Math2.fit(0, current - claimBlocks, Integer.MAX_VALUE);
				accrueClaimBlocksEvent.setBlocksToAccrue(value);
			}
		}
	}

	@Override
	public Class<? extends AccrueClaimBlocksEvent> getEventClass() {
		return AccrueClaimBlocksEvent.class;
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
