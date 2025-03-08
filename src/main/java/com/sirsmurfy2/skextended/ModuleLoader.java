package com.sirsmurfy2.skextended;

import com.google.common.collect.ImmutableList;
import com.sirsmurfy2.skextended.modules.alignment.AlignmentModule;
import com.sirsmurfy2.skextended.modules.equation.EquationModule;
import com.sirsmurfy2.skextended.modules.griefprevention.GriefPreventionModule;
import com.sirsmurfy2.skextended.modules.playervaults.PlayerVaultsModule;
import com.sirsmurfy2.skextended.utils.ReflectUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ModuleLoader {

	private static final List<ModuleInfo> modules = new ArrayList<>();

	static {
		modules.add(new ModuleInfo(AlignmentModule.class, null, "alignment"));
		modules.add(new ModuleInfo(EquationModule.class, null, "equation"));
		modules.add(new ModuleInfo(PlayerVaultsModule.class, "PlayerVaults", "playervaults"));
		modules.add(new ModuleInfo(GriefPreventionModule.class, "GriefPrevention", "griefprevention"));
	}

	public static void loadModules() throws Exception {
		for (ModuleInfo moduleInfo : modules) {
			//noinspection DataFlowIssue
			if (moduleInfo.requiresPlugin() && !Bukkit.getPluginManager().isPluginEnabled(moduleInfo.getPlugin()))
				continue;
			moduleInfo.module.getConstructor().newInstance().loadModule();
			ReflectUtils.loadClasses("com.sirsmurfy2.skextended.modules", moduleInfo.path);
			moduleInfo.setLoaded(true);
        }
	}

	@SuppressWarnings("ClassEscapesDefinedScope")
	public static ImmutableList<ModuleInfo> getModules() {
		return ImmutableList.copyOf(modules);
	}

	public abstract void loadModule();

	private static class ModuleInfo {
		private final Class<? extends ModuleLoader> module;
		private final @Nullable String plugin;
		private final String path;
		private boolean loaded = false;

		public ModuleInfo(Class<? extends ModuleLoader> module, @Nullable String plugin, String path) {
			this.module = module;
			this.plugin = plugin;
			this.path = path;
		}

		public Class<? extends ModuleLoader> getModule() {
			return module;
		}

		public @Nullable String getPlugin() {
			return plugin;
		}

		public String getPath() {
			return path;
		}

		public boolean isLoaded() {
			return loaded;
		}

		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}

		public boolean requiresPlugin() {
			return plugin != null && !plugin.isEmpty();
		}

	}

}
