package com.sirsmurfy2.skextended.modules.griefprevention;

import com.sirsmurfy2.skextended.utils.ReflectUtils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class GriefPreventionUtils {

	private static final Field claimsField;
	private static final DataStore dataStore;
	private static Method getClaimMethod;

	static {
		claimsField = ReflectUtils.getField(DataStore.class, "claims");
		dataStore = GriefPrevention.instance.dataStore;
		getClaimMethod =  ReflectUtils.getMethod(DataStore.class, "getClaim", long.class);
	}

	public static Claim[] getClaims() {
		List<Claim> claims = ReflectUtils.getFieldData(claimsField, dataStore);
		return claims.toArray(Claim[]::new);
	}

	public static @Nullable Claim getClaim(long id) {
        return ReflectUtils.invokeMethod(getClaimMethod, dataStore, id);
	}

}
