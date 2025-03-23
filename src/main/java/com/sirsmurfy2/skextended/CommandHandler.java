package com.sirsmurfy2.skextended;

import com.sirsmurfy2.skextended.ModuleLoader.ModuleInfo;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.sirsmurfy2.skextended.SkExtended.sendMessage;

public class CommandHandler implements TabExecutor {

	@Override
	public boolean onCommand(
		@NotNull CommandSender sender,
		@NotNull Command command,
		@NotNull String label,
		@NotNull String @NotNull [] args
	) {
		if (args[0].equalsIgnoreCase("info")) {
			sendMessage(sender, "");
			for (ModuleInfo moduleInfo : ModuleLoader.getModules()) {
				if (moduleInfo.isLoaded()) {
					sendMessage(sender, "");
				} else {
					sendMessage(sender, "");
				}
			}
		} else if (args[0].equalsIgnoreCase("github")) {

		} else if (args[0].equalsIgnoreCase("help")) {

		} else {
			return false;
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(
		@NotNull CommandSender sender,
		@NotNull Command command,
		@NotNull String label,
		@NotNull String @NotNull [] args
	) {
		if (!command.getName().equalsIgnoreCase("skextended"))
			return null;

		List<String> options = new ArrayList<>();
		options.add("info");
		options.add("github");
		options.add("help");
		return options;
	}

}
