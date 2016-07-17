package com.doomengine.scene;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.doomengine.asset.AssetInfo;
import com.doomengine.asset.AssetKey;
import com.doomengine.asset.AssetLoadException;
import com.doomengine.asset.AssetLoader;
import com.doomengine.asset.AssetManager;
import com.doomengine.asset.AssetNotFoundException;
import com.doomengine.components.GeometryComponent;
import com.doomengine.material.Material;
import com.doomengine.material.MaterialList;
import com.doomengine.math.Vector2f;
import com.doomengine.math.Vector3f;
import com.doomengine.renderer.Geometry;
import com.doomengine.renderer.Mesh;
import com.doomengine.renderer.VertexAttributes;
import com.doomengine.system.Logger;
import com.doomengine.util.StreamUtils;

public class OBJLoader implements AssetLoader {

	private class Face {
		Vertex[] vertices;
	}

	private class Vertex {
		Vector3f v;
		Vector2f vt;
		Vector3f vn;
		int index;

		@Override public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Vertex other = (Vertex) obj;
			if (this.v != other.v && (this.v == null || !this.v.equals(other.v))) {
				return false;
			}
			if (this.vt != other.vt && (this.vt == null || !this.vt.equals(other.vt))) {
				return false;
			}
			if (this.vn != other.vn && (this.vn == null || !this.vn.equals(other.vn))) {
				return false;
			}
			return true;
		}
	}

	private final ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
	private final ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
	private final ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	private final ArrayList<Face> faces = new ArrayList<Face>();
	private final HashMap<String, ArrayList<Face>> matFaces = new HashMap<String, ArrayList<Face>>();

	private int curIndex = 0;
	private final HashMap<Vertex, Integer> vertIndexMap = new HashMap<Vertex, Integer>(100);
	private final HashMap<Integer, Vertex> indexVertMap = new HashMap<Integer, Vertex>(100);

	private MaterialList matList;
	private String currentMatName;

	private AssetManager assetManager;
	private ModelKey key;

	public void reset() {
		vertices.clear();
		texCoords.clear();
		normals.clear();
		faces.clear();
		matFaces.clear();

		vertIndexMap.clear();
		indexVertMap.clear();

		curIndex = 0;

		matList = null;
		currentMatName = null;
	}

	private void readLine(String line) {
		String[] tokens = line.split(" ");

		String cmd = tokens[0];
		if (cmd.startsWith("#") || line.equals("")) {
			return;
		} else if (cmd.equals("v")) {
			// vertex position
			vertices.add(readVector3(tokens[1], tokens[2], tokens[3]));
		} else if (cmd.equals("vn")) {
			// vertex normal
			normals.add(readVector3(tokens[1], tokens[2], tokens[3]));
		} else if (cmd.equals("vt")) {
			// texture coordinate
			texCoords.add(readVector2(tokens[1], tokens[2]));
		} else if (cmd.equals("f")) {
			// face
			readFace(line.substring(cmd.length() + 1, line.length()));
		} else if (cmd.equals("usemtl")) {
			currentMatName = tokens[1];
		} else if (cmd.equals("mtllib")) {
			String mtllib = tokens[1];
			loadMtlLib(mtllib);
		} else if (cmd.equals("s") || cmd.equals("g")) {
			return;
		} else if (cmd.equals("o")) {
			return;
		} else {
			Logger.log("Unknown statement in OBJ! " + cmd);
			return;
		}
	}

	private Vector2f readVector2(String v1, String v2) {
		return new Vector2f(Float.parseFloat(v1), Float.parseFloat(v2));
	}

	private Vector3f readVector3(String v1, String v2, String v3) {
		return new Vector3f(Float.parseFloat(v1), Float.parseFloat(v2), Float.parseFloat(v3));
	}

	private void readFace(String line) {
		Face f = new Face();
		ArrayList<Vertex> vertList = new ArrayList<Vertex>();

		String[] verticesTokens = line.split(" ");
		for (String vertexToken : verticesTokens) {
			int vIndex = 0;
			int vtIndex = 0;
			int vnIndex = 0;

			String[] split = vertexToken.split("/");
			if (split.length == 1) {
				vIndex = Integer.parseInt(split[0]);
			} else if (split.length == 2) {
				vIndex = Integer.parseInt(split[0]);
				vtIndex = Integer.parseInt(split[1]);
			} else if (split.length == 3 && !split[1].equals("")) {
				vIndex = Integer.parseInt(split[0]);
				vtIndex = Integer.parseInt(split[1]);
				vnIndex = Integer.parseInt(split[2]);
			} else if (split.length == 3) {
				vIndex = Integer.parseInt(split[0]);
				vnIndex = Integer.parseInt(split[2]);
			}

			if (vIndex < 0) {
				vIndex = vertices.size() + vIndex + 1;
			}
			if (vtIndex < 0) {
				vtIndex = texCoords.size() + vtIndex + 1;
			}
			if (vnIndex < 0) {
				vnIndex = normals.size() + vnIndex + 1;
			}

			Vertex vertex = new Vertex();
			vertex.v = vertices.get(vIndex - 1);

			if (vtIndex > 0)
				vertex.vt = texCoords.get(vtIndex - 1);

			if (vnIndex > 0)
				vertex.vn = normals.get(vnIndex - 1);

			vertList.add(vertex);
		}

		if (vertList.size() != 3) {
			Logger.log("Something else than triangle detected in OBJ. Ignored.");
			return;
		}

		f.vertices = new Vertex[vertList.size()];
		for (int i = 0; i < vertList.size(); i++) {
			f.vertices[i] = vertList.get(i);
		}

		if (matList != null && matFaces.containsKey(currentMatName)) {
			matFaces.get(currentMatName).add(f);
		} else {
			faces.add(f); // face that belong to the default material
		}
	}

	private void loadMtlLib(String name) {
		if (!name.toLowerCase().endsWith(".mtl"))
			throw new AssetLoadException("Expected .mtl file! Got: " + name);

		name = new File(name).getName();
		AssetKey<MaterialList> mtlKey = new AssetKey<MaterialList>(key.getFolder() + name);
		try {
			matList = assetManager.loadAsset(mtlKey);
		} catch (AssetNotFoundException ex) {
			Logger.log("Cannot locate " + name + " for model " + key);
		}

		if (matList != null) {
			for (String matName : matList.keySet()) {
				matFaces.put(matName, new ArrayList<Face>());
			}
		}
	}

	private void findVertexIndex(Vertex vert) {
		Integer index = vertIndexMap.get(vert);
		if (index != null) {
			vert.index = index.intValue();
		} else {
			vert.index = curIndex++;
			vertIndexMap.put(vert, vert.index);
			indexVertMap.put(vert.index, vert);
		}
	}

	private Mesh constructMesh(ArrayList<Face> faceList) {
		boolean hasTexCoords = false;
		boolean hasNormals = false;

		ArrayList<Face> newFaces = new ArrayList<Face>(faceList.size());
		for (int i = 0; i < faceList.size(); i++) {
			Face f = faceList.get(i);

			for (Vertex v : f.vertices) {
				findVertexIndex(v);

				if (!hasTexCoords && v.vt != null)
					hasTexCoords = true;
				if (!hasNormals && v.vn != null)
					hasNormals = true;
			}

			newFaces.add(f);
		}

		float[] positionArray = new float[vertIndexMap.size() * 3];
		float[] normalArray = null;
		float[] texCoordArray = null;
		short[] indexArray = new short[newFaces.size() * 3];

		if (hasNormals) {
			normalArray = new float[vertIndexMap.size() * 3];
		}
		if (hasTexCoords) {
			texCoordArray = new float[vertIndexMap.size() * 2];
		}

		int numFaces = newFaces.size();
		for (int i = 0; i < numFaces; i++) {
			Face f = newFaces.get(i);
			if (f.vertices.length != 3)
				continue;

			int pos;

			Vertex v0 = f.vertices[0];
			Vertex v1 = f.vertices[1];
			Vertex v2 = f.vertices[2];

			pos = v0.index * 3;
			positionArray[pos + 0] = v0.v.getX();
			positionArray[pos + 1] = v0.v.getY();
			positionArray[pos + 2] = v0.v.getZ();
			pos = v1.index * 3;
			positionArray[pos + 0] = v1.v.getX();
			positionArray[pos + 1] = v1.v.getY();
			positionArray[pos + 2] = v1.v.getZ();
			pos = v2.index * 3;
			positionArray[pos + 0] = v2.v.getX();
			positionArray[pos + 1] = v2.v.getY();
			positionArray[pos + 2] = v2.v.getZ();

			if (normalArray != null) {
				if (v0.vn != null) {
					pos = v0.index * 3;
					normalArray[pos + 0] = v0.vn.getX();
					normalArray[pos + 1] = v0.vn.getY();
					normalArray[pos + 2] = v0.vn.getZ();
					pos = v1.index * 3;
					normalArray[pos + 0] = v1.vn.getX();
					normalArray[pos + 1] = v1.vn.getY();
					normalArray[pos + 2] = v1.vn.getZ();
					pos = v2.index * 3;
					normalArray[pos + 0] = v2.vn.getX();
					normalArray[pos + 1] = v2.vn.getY();
					normalArray[pos + 2] = v2.vn.getZ();
				}
			}

			if (texCoordArray != null) {
				if (v0.vt != null) {
					pos = v0.index * 2;
					texCoordArray[pos + 0] = v0.vt.getX();
					texCoordArray[pos + 1] = v0.vt.getY();
					pos = v1.index * 2;
					texCoordArray[pos + 0] = v1.vt.getX();
					texCoordArray[pos + 1] = v1.vt.getY();
					pos = v2.index * 2;
					texCoordArray[pos + 0] = v2.vt.getX();
					texCoordArray[pos + 1] = v2.vt.getY();
				}
			}

			int index = i * 3;
			indexArray[index + 0] = (short) v0.index;
			indexArray[index + 1] = (short) v1.index;
			indexArray[index + 2] = (short) v2.index;
		}

		if (vertIndexMap.size() >= 65536) {
			throw new AssetLoadException("to many vertices!");
		}

		VertexAttributes attributes = new VertexAttributes(VertexAttributes.POSITION_ATTRIB, VertexAttributes.NORMAL_ATTRIB, VertexAttributes.TEXCOORD_ATTRIB);

		int vertexSize = attributes.vertexSize;
		float[] resultArray = new float[vertexSize * vertIndexMap.size()];
		for (int i = 0; i < vertIndexMap.size(); i++) {
			int i0 = (i * vertexSize);
			resultArray[i0 + 0] = positionArray[(i * 3) + 0];
			resultArray[i0 + 1] = positionArray[(i * 3) + 1];
			resultArray[i0 + 2] = positionArray[(i * 3) + 2];

			if (hasNormals) {
				resultArray[i0 + 3] = normalArray[(i * 3) + 0];
				resultArray[i0 + 4] = normalArray[(i * 3) + 1];
				resultArray[i0 + 5] = normalArray[(i * 3) + 2];
			}

			if (hasTexCoords) {
				resultArray[i0 + 6] = texCoordArray[(i * 2) + 0];
				resultArray[i0 + 7] = texCoordArray[(i * 2) + 1];
			}
		}

		// prepare for next mesh
		vertIndexMap.clear();
		indexVertMap.clear();
		curIndex = 0;

		return new Mesh(resultArray, indexArray, attributes);
	}

	private Geometry createGeometry(ArrayList<Face> faceList, String matName) throws IOException {
		if (faceList.isEmpty())
			throw new IOException("No geometry data to generate mesh");

		Mesh mesh = constructMesh(faceList);

		Material material = null;
		if (matName != null && matList != null) {
			// Get material from material list
			material = matList.get(matName);
		}
		if (material == null) {
			// create default material
			material = new Material("default");
		}

		Geometry geom = new Geometry(mesh, material);

		return geom;
	}

	@Override public Object load(AssetInfo info) throws IOException {
		reset();

		if (!(info.getKey() instanceof ModelKey)) {
			throw new AssetLoadException("Models must be loaded using a ModelKey!");
		}

		this.key = (ModelKey) info.getKey();
		this.assetManager = info.getManager();

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

		GameObject object = new GameObject();

		if (matFaces.size() > 0) {
			for (Map.Entry<String, ArrayList<Face>> entry : matFaces.entrySet()) {
				ArrayList<Face> materialFaces = entry.getValue();
				if (materialFaces.size() > 0) {
					Geometry geometry = createGeometry(materialFaces, entry.getKey());
					object.addComponent(new GeometryComponent(geometry));
				}
			}
		} else if (faces.size() > 0) {
			Geometry geometry = createGeometry(faces, null);
			object.addComponent(new GeometryComponent(geometry));
		}

		return object;
	}

}
