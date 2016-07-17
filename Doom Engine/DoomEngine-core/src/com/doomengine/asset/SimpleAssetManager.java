package com.doomengine.asset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.doomengine.asset.cache.AssetCache;
import com.doomengine.asset.cache.SimpleAssetCache;
import com.doomengine.system.DoomException;
import com.doomengine.system.Logger;

public class SimpleAssetManager implements AssetManager {

	private final ArrayList<AssetLocator> locatorsList = new ArrayList<AssetLocator>();

	private final HashMap<Class<?>, AssetLoader> loaderMap = new HashMap<Class<?>, AssetLoader>();
	private final HashMap<AssetLoader, String[]> extensionMap = new HashMap<AssetLoader, String[]>();

	private AssetCache cache;

	public SimpleAssetManager(URL configURL) {
		this(configURL, true);
	}

	public SimpleAssetManager(URL configURL, boolean useCache) {
		super();

		if (useCache)
			this.cache = new SimpleAssetCache();

		try {
			loadAssetConfig(this, configURL);
		} catch (IOException e) {
			throw new DoomException("Failed to load AssetConfig from URL: " + configURL, e);
		}
	}

	private static Class<?> acquireClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException ex) {
			return null;
		}
	}

	public static void loadAssetConfig(AssetManager assetManager, URL configURL) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(configURL.openStream()));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				String cmd = tokens[0];
				if (cmd.equals("loader")) {
					String loaderClass = tokens[1];
					String colon = tokens[2];
					if (!colon.equals(":")) {
						throw new IOException("Expected ':', got '" + colon + "'");
					}
					String extensionsList = line.substring(line.indexOf(colon) + 1, line.length());
					String[] extensions = extensionsList.split(",");
					for (int i = 0; i < extensions.length; i++) {
						extensions[i] = extensions[i].trim();
					}
					Class clazz = acquireClass(loaderClass);
					if (clazz != null) {
						assetManager.registerLoader(clazz, extensions);
					} else {
						Logger.log("Cannot find loader " + loaderClass);
					}
				} else if (cmd.equals("locator")) {
					String locatorClass = tokens[1].trim();
					Class clazz = acquireClass(locatorClass);
					if (clazz != null) {
						assetManager.registerLocator(clazz);
					} else {
						Logger.log("Cannot find locator " + locatorClass);
					}
				} else if (cmd.equals("include")) {
					String includedCfg = tokens[1].trim();
					URL includedCfgURL = Thread.currentThread().getContextClassLoader().getResource(includedCfg);
					if (includedCfgURL != null) {
						loadAssetConfig(assetManager, includedCfgURL);
					} else {
						Logger.log("Cannot find config include " + includedCfg);
					}
				} else if (cmd.trim().startsWith("#") || cmd.equals("")) {
					continue;
				} else {
					throw new IOException("Expected command, got '" + cmd + "'");
				}
			}
		} finally {
			reader.close();
		}
	}

	@Override public void registerLoader(Class<? extends AssetLoader> loaderClass, String... extensions) {
		AssetLoader loader = (AssetLoader) newInstance(loaderClass);

		loaderMap.put(loaderClass, loader);
		extensionMap.put(loader, extensions);

		Logger.log("A new AssetLoader was registered: " + loaderClass.getName());
	}

	@Override public void unregisterLoader(Class<? extends AssetLoader> loaderClass) {
		AssetLoader loader = loaderMap.get(loaderClass);

		loaderMap.remove(loaderClass);
		extensionMap.remove(loader);

		Logger.log("A AssetLoader was unregistered: " + loaderClass.getName());
	}

	@Override public void registerLocator(Class<? extends AssetLocator> locatorClass) {
		AssetLocator locator = (AssetLocator) newInstance(locatorClass);

		locatorsList.add(locator);

		Logger.log("A new AssetLocator was registered: " + locatorClass.getName());
	}

	@Override public void unregisterLocator(Class<? extends AssetLocator> locatorClass) {
		for (AssetLocator locator : locatorsList) {
			if (locator.getClass().equals(locatorClass)) {
				locatorsList.remove(locator);
			}
		}

		Logger.log("A AssetLocator was unregistered: " + locatorClass.getName());
	}

	@Override public AssetInfo locateAsset(AssetKey<?> key) {
		if (locatorsList.isEmpty()) {
			Logger.log("There are no locators currently registered.");
			return null;
		}

		for (AssetLocator locator : locatorsList) {
			AssetInfo info = locator.locate(this, key);
			if (info != null)
				return info;
		}

		return null;
	}

	@SuppressWarnings("unchecked") protected <T> T loadLocatedAsset(AssetKey<T> key, AssetInfo info) {
		AssetLoader loader = aquireLoader(key);
		if (loader == null) {
			throw new AssetLoadException("No loader registered for type " + key.getExtension());
		}

		T obj;
		try {
			obj = (T) loader.load(info);
		} catch (IOException ex) {
			throw new AssetLoadException("An exception has occured while loading asset: " + key, ex);
		}

		if (obj == null) {
			throw new AssetLoadException("Error occured while loading asset " + key + " using " + loader.getClass().getSimpleName());
		}

		Logger.log("Loaded " + key + " with " + loader.getClass().getSimpleName());

		if (hasCache())
			cache.addToCache(key, obj);

		return obj;

	}

	protected AssetLoader aquireLoader(AssetKey<?> key) {
		for (AssetLoader loader : extensionMap.keySet()) {
			String[] extensions = extensionMap.get(loader);
			for (String extension : extensions) {
				if (extension.equalsIgnoreCase(key.getExtension())) {
					return loader;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked") @Override public <T> T loadAsset(AssetKey<T> key) {
		T obj = null;

		if (hasCache()) {
			obj = cache.getFromCache(key);

			if (obj instanceof CloneableAsset) {
				CloneableAsset asset = (CloneableAsset) obj;
				obj = (T) asset.clone();
			}
		}

		if (obj == null) {
			AssetInfo info = locateAsset(key);
			if (info == null) {
				throw new AssetNotFoundException(key.toString());
			}

			obj = loadLocatedAsset(key, info);
		}

		return obj;
	}

	public Object newInstance(Class<?> type) {
		try {
			return type.newInstance();
		} catch (Exception ex) {
			Logger.log("Cannot create an instance of type " + type.getName() + ", does the class have an empty and publically accessible constructor?");
		}
		return null;
	}

	@Override public AssetCache getCache() {
		return cache;
	}

	@Override public boolean hasCache() {
		return cache != null;
	}

	@Override public void clearCache() {
		cache.clearCache();
	}

}
