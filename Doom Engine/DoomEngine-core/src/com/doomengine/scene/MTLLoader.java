package com.doomengine.scene;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.doomengine.asset.AssetInfo;
import com.doomengine.asset.AssetKey;
import com.doomengine.asset.AssetLoader;
import com.doomengine.asset.AssetManager;
import com.doomengine.asset.AssetNotFoundException;
import com.doomengine.material.Material;
import com.doomengine.material.MaterialList;
import com.doomengine.math.ColorRGBA;
import com.doomengine.system.Logger;
import com.doomengine.texture.Texture2D;
import com.doomengine.texture.image.Image;
import com.doomengine.util.StreamUtils;

public class MTLLoader implements AssetLoader {

	private MaterialList matList;
	private AssetManager assetManager;
	private String folderName;
	private AssetKey<?> key;

	private Texture2D diffuseMap, normalMap, specularMap, alphaMap;
	private ColorRGBA ambient = new ColorRGBA();
	private ColorRGBA diffuse = new ColorRGBA();
	private ColorRGBA specular = new ColorRGBA();
	private float shininess = 16f;
	private String matName;
	private float alpha = 1f;

	public void reset() {
		matList = null;

		resetMaterial();
	}

	private void resetMaterial() {
		ambient.set(ColorRGBA.BLACK);
		diffuse.set(ColorRGBA.WHITE);
		specular.set(ColorRGBA.BLACK);
		shininess = 16f;
		matName = null;
		diffuseMap = null;
		normalMap = null;
		specularMap = null;
		alphaMap = null;
		alpha = 1f;
	}

	private void createMaterial() {
		Material material;

		if (alpha < 1f) {
			diffuse.setA(alpha);
		}

		material = new Material(matName);
		material.setColor("Ambient", new ColorRGBA(ambient));
		material.setColor("Diffuse", new ColorRGBA(diffuse));
		material.setColor("Specular", new ColorRGBA(specular));
		material.setFloat("Shininess", shininess);

		if (diffuseMap != null)
			material.setTexture("DiffuseMap", diffuseMap);
		if (specularMap != null)
			material.setTexture("SpecularMap", specularMap);
		if (normalMap != null)
			material.setTexture("NormalMap", normalMap);
		if (alphaMap != null)
			material.setTexture("AlphaMap", alphaMap);

		matList.put(matName, material);
	}

	private void startMaterial(String name) {
		if (matName != null) {
			createMaterial();
		}

		resetMaterial();
		matName = name;
	}

	private Texture2D loadTexture(String path) {
		String[] split = path.trim().split(" ");

		// will crash if path is an empty string
		path = split[split.length - 1];

		String name = new File(path).getName();
		AssetKey<Image> texKey = new AssetKey<Image>(folderName + name);
		Texture2D texture = null;

		try {
			Image image = assetManager.loadAsset(texKey);
			texture = new Texture2D(image);
		} catch (AssetNotFoundException ex) {
			Logger.log("Cannot locate " + texKey + " for material " + key);
		}

		return texture;
	}

	private ColorRGBA readColor(String v1, String v2, String v3) {
		return new ColorRGBA(Float.parseFloat(v1), Float.parseFloat(v2), Float.parseFloat(v3), 1.0f);
	}

	private void readLine(String line) {
		String[] tokens = line.split(" ");

		String cmd = tokens[0].toLowerCase();
		if (cmd.startsWith("#") || line.equals("")) {
			return;
		} else if (cmd.equals("newmtl")) {
			String name = tokens[1];
			startMaterial(name);
		} else if (cmd.equals("ka")) {
			ambient.set(readColor(tokens[1], tokens[2], tokens[3]));
		} else if (cmd.equals("kd")) {
			diffuse.set(readColor(tokens[1], tokens[2], tokens[3]));
		} else if (cmd.equals("ks")) {
			specular.set(readColor(tokens[1], tokens[2], tokens[3]));
		} else if (cmd.equals("ns")) {
			float shiny = Float.parseFloat(tokens[1]);
			if (shiny >= 1) {
				shininess = shiny; /* (128f / 1000f) */
				if (specular.equals(ColorRGBA.BLACK)) {
					specular.set(ColorRGBA.WHITE);
				}
			} else {
				// For some reason blender likes to export Ns 0 statements
				// Ignore Ns 0 instead of setting it
			}

		} else if (cmd.equals("d") || cmd.equals("tr")) {
			float tempAlpha = Float.parseFloat(tokens[1]);
			if (tempAlpha > 0.0f && tempAlpha < 1.0f) {
				alpha = tempAlpha;
			}
		} else if (cmd.equals("map_ka")) {
			// ignore it for now
			return;
		} else if (cmd.equals("map_kd")) {
			String path = tokens[1];
			diffuseMap = loadTexture(path);
		} else if (cmd.equals("map_bump") || cmd.equals("bump")) {
			if (normalMap == null) {
				String path = tokens[1];
				normalMap = loadTexture(path);
			}
		} else if (cmd.equals("map_ks")) {
			String path = tokens[1];
			specularMap = loadTexture(path);
			if (specularMap != null) {
				// NOTE: since specular color is modulated with specmap
				// make sure we have it set
				if (specular.equals(ColorRGBA.BLACK)) {
					specular.set(ColorRGBA.WHITE);
				}
			}
		} else if (cmd.equals("map_d")) {
			String path = tokens[1];
			alphaMap = loadTexture(path);
		} else if (cmd.equals("illum")) {
			int mode = Integer.parseInt(tokens[1]);

			switch (mode) {
			case 0:
				// no lighting
				// shadeless = true;
				break;
			case 1:
				// disallowSpecular = true;
				break;
			case 2:
			case 3:
			case 5:
			case 8:
				break;
			case 4:
			case 6:
			case 7:
			case 9:
				// transparent = true;
				break;
			}
		} else if (cmd.equals("ke") || cmd.equals("ni")) {
			// Ni: index of refraction
			// Ke: emission color
			return;
		} else {
			Logger.log("Unknown statement in MTL! " + cmd);
			return;
		}
	}

	@Override public Object load(AssetInfo info) throws IOException {
		reset();

		this.key = info.getKey();
		this.assetManager = info.getManager();
		folderName = key.getFolder();
		matList = new MaterialList();

		InputStream in = null;
		try {
			in = info.openStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String line = null;
			while ((line = reader.readLine()) != null) {
				readLine(line);
			}
		} finally {
			StreamUtils.closeQuietly(in);
		}

		if (matName != null) {
			// still have a material in the vars
			createMaterial();
			resetMaterial();
		}

		MaterialList list = matList;

		return list;
	}

}
