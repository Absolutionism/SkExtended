package com.sirsmurfy2.skextended.modules.griefprevention.expressions;

import ch.njol.skript.doc.*;
import com.sirsmurfy2.skextended.lang.SimpleEventValueExpression;
import me.ryanhamshire.GriefPrevention.events.TrustChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Name("Trust Affected Player")
@Description("The affected player in a 'trust change' event.")
@Examples({
	"on trust change:",
		"\tif the trust affected player is op:"
})
@Since("1.0.0")
@RequiredPlugins("GriefPrevention")
public class ExprTrustAffectedPlayer extends SimpleEventValueExpression<TrustChangedEvent, OfflinePlayer> {

	static {
		register(ExprTrustAffectedPlayer.class, OfflinePlayer.class, "[the] [grief prevention] trust affected player");
	}

	@Override
	public OfflinePlayer @Nullable [] convert(TrustChangedEvent event) {
		UUID uuid = UUID.fromString(event.getIdentifier());
		return new OfflinePlayer[] {Bukkit.getOfflinePlayer(uuid)};
	}

	@Override
	public Class<TrustChangedEvent> getEventClass() {
		return TrustChangedEvent.class;
	}

    @Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends OfflinePlayer> getReturnType() {
		return OfflinePlayer.class;
	}

}
