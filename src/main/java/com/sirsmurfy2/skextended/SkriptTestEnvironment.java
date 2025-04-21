package com.sirsmurfy2.skextended;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SkriptTestEnvironment {

	public static void main(String[] args) {
		Map<String, String> systemEnv = System.getenv();
		if (systemEnv.containsKey("INPUT_EXTRA_PLUGINS_DIRECTORY")) {
			String extraPluginsPath = systemEnv.get("INPUT_EXTRA_PLUGINS_DIRECTORY");
			File extraPluginsDir = new File(extraPluginsPath);
			// If the 'extra-plugins' directory exists, port over jar files into '/build'
			if (extraPluginsDir.exists() && extraPluginsDir.isDirectory()) {
				File pluginsStorage = new File("build/extra-plugins");
				if (!pluginsStorage.exists()) {
					pluginsStorage.mkdir();
				} else {
					for (File subFile : pluginsStorage.listFiles()) {
						subFile.delete();
					}
				}

				// If the 'plugins.json' file exists, download the jars from online into the '/build/extra-plugins'
				File pluginsJson = new File(extraPluginsPath,  "plugins.json");
				if (pluginsJson.exists()) {
					Type pluginDataType = new TypeToken<List<PluginData>>() {}.getType();
					Gson gson = new Gson();
					FileReader reader = null;
					try {
						reader = new FileReader(pluginsJson);
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
					List<PluginData> pluginDatas = gson.fromJson(reader, pluginDataType);
					for (PluginData pluginData : pluginDatas) {
						debug("DOWNLOADING: " + pluginData.name + "-" + pluginData.version);
                        URL url = null;
                        try {
                            url = new URL(pluginData.url);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                        try {
							Files.copy(
								url.openStream(),
								Path.of(pluginsStorage.toPath() + "/" + pluginData.name + "-" + pluginData.version + ".jar"),
								StandardCopyOption.REPLACE_EXISTING
							);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				}
				// Copy over the '.jar' files from 'extra-plugins' to '/build/extra-plugins'
				for (File pluginFile : extraPluginsDir.listFiles()) {
					if (!pluginFile.getName().endsWith(".jar"))
						continue;
					try {
						Files.copy(
							pluginFile.toPath(),
							Path.of(extraPluginsDir.getAbsolutePath(), pluginFile.getName()),
							StandardCopyOption.REPLACE_EXISTING
						);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		debug("STARTING ENVIRONMENT");
		startEnvironment();
	}

	private static void startEnvironment() {
		Map<String, String> systemEnv = System.getenv();
		ProcessBuilder processBuilder = new ProcessBuilder("python", "run-tests.py");
		processBuilder.inheritIO();

		for (Entry<String, String> entry : systemEnv.entrySet()) {
			processBuilder.environment().put(entry.getKey(), entry.getValue());
		}
        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	private static void debug(String message) {
		System.out.println(message);
	}

	public class PluginData {
		public String name;
		public String version;
		public String url;
	}

}
