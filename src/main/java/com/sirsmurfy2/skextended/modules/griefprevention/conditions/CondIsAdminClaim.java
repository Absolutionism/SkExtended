package com.sirsmurfy2.skextended.modules.griefprevention.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import me.ryanhamshire.GriefPrevention.Claim;

@Name("Claim Is Admin Claim")
@Description("Whether a claim is an admin claim.")
@Examples({
	"if {_claim} is an admin claim:"
})
@Since("1.0.0")
@RequiredPlugins("GriefPrevention")
public class CondIsAdminClaim extends PropertyCondition<Claim> {

	static {
		register(CondIsAdminClaim.class, "[an] admin claim[s]", "claims");
	}

	@Override
	public boolean check(Claim claim) {
		return claim.isAdminClaim();
	}

	@Override
	protected String getPropertyName() {
		return "admin claim";
	}

}
