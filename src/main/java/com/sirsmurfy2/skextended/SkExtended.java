package com.sirsmurfy2.skextended;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.util.Version;
import com.sirsmurfy2.skextended.Metrics.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkExtended extends JavaPlugin {

	private static SkExtended instance;
	private static SkriptAddon addon;

	/**
	 * @hidden
	 */
	public SkExtended() {
		if (instance != null)
			throw new IllegalStateException("Only one instance of SkExtended can be created.");
		instance = this;
	}

	/**
	 * @hidden
	 */
	@Override
	public void onEnable() {
		PluginManager manager = Bukkit.getPluginManager();
		Plugin skriptPlugin = manager.getPlugin("Skript");
		if (skriptPlugin == null || !skriptPlugin.isEnabled()) {
			getLogger().severe("Could not find Skript! Disabling...");
			manager.disablePlugin(this);
			return;
		} else if (Skript.getVersion().compareTo(new Version(2, 10, 0)) < 0) {
			getLogger().severe("You are running an unsupported version of Skript. Disabling...");
			manager.disablePlugin(this);
			return;
		}
		if (!Skript.isAcceptRegistrations()) {
			getLogger().severe("The plugin can't load when it's already loaded! Disabling...");
			manager.disablePlugin(this);
			return;
		}

		Metrics metrics = new Metrics(this, 24529);
		metrics.addCustomChart(new SimplePie("skript_version", () -> Skript.getVersion().toString()));
		metrics.addCustomChart(new SimplePie("server_version", Bukkit::getMinecraftVersion));

		addon = Skript.registerAddon(this);
		addon.setLanguageFileDirectory("lang");

        try {
            ModuleLoader.loadModules();
        } catch (Exception error) {
			error.printStackTrace();
			manager.disablePlugin(this);
			return;
        }
		getLogger().info("Successfully loaded SkExtended.");
	}

	/**
	 * @hidden
	 */
	@Override
	public void onDisable() {

	}

	public static SkExtended getInstance() {
		return instance;
	}

	public static SkriptAddon getAddonInstance() {
		return addon;
	}

	public static void debug(String message) {
		Bukkit.getConsoleSender().sendMessage("[SkExtended] " + message);
	}

	public static void sendMessage(CommandSender commandSender, String message) {
		commandSender.sendMessage(message);
	}

}
