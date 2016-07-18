package com.doomengine.scene;

import java.util.ArrayList;

import com.doomengine.asset.CloneableAsset;
import com.doomengine.math.Transform;
import com.doomengine.renderer.RenderQueue;

public class GameObject implements CloneableAsset {

	private ArrayList<GameObject> children;
	private ArrayList<GameComponent> components;

	private Transform transform;

	private GameObject parent;

	private Scene scene;

	public GameObject() {
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
		transform = new Transform();
		scene = null;
	}

	@Override public GameObject clone() {
		try {
			GameObject clone = (GameObject) super.clone();

			clone.parent = this.parent;
			clone.scene = this.scene;

			clone.transform = this.transform.clone();

			clone.children = new ArrayList<GameObject>();
			for (int i = 0; i < children.size(); i++) {
				GameObject child = children.get(i);
				clone.addChild(child.clone());
			}

			clone.components = new ArrayList<GameComponent>();
			for (int i = 0; i < components.size(); i++) {
				GameComponent component = components.get(i);
				clone.addComponent(component.clone());
			}

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	public GameObject addChild(GameObject child) {
		if (child != null) {
			children.add(child);
			child.setScene(scene);
			child.setParent(this);
			child.getTransform().setParent(transform);
		}

		return this;
	}

	public GameObject removeChild(GameObject child) {
		if (child != null && children.contains(child)) {
			children.remove(child);

			for (int i = 0; i < children.size(); i++) {
				GameObject c = children.get(i);
				removeChild(c);
			}

			for (int i = 0; i < components.size(); i++) {
				GameComponent component = components.get(i);
				removeComponent(component);
			}
		}

		return this;
	}

	public GameObject addComponent(GameComponent component) {
		if (component != null) {
			components.add(component);
			component.setParent(this);
		}

		return this;
	}

	public GameObject removeComponent(GameComponent component) {
		if (component != null) {
			components.remove(component);
			component.removeFromScene(scene);
		}

		return this;
	}

	public void inputAll() {
		input();

		for (int i = 0; i < children.size(); i++) {
			GameObject child = children.get(i);
			child.inputAll();
		}
	}

	public void updateAll(float deltaTime) {
		update(deltaTime);

		for (int i = 0; i < children.size(); i++) {
			GameObject child = children.get(i);
			child.updateAll(deltaTime);
		}
	}

	public void renderAll(RenderQueue queue) {
		render(queue);

		for (int i = 0; i < children.size(); i++) {
			GameObject child = children.get(i);
			child.renderAll(queue);
		}
	}

	public void input() {
		transform.update(); // NOTE: crappy but fine

		for (int i = 0; i < components.size(); i++) {
			GameComponent component = components.get(i);
			component.input();
		}
	}

	public void update(float deltaTime) {
		for (int i = 0; i < components.size(); i++) {
			GameComponent component = components.get(i);
			component.update(deltaTime);
		}
	}

	public void render(RenderQueue queue) {
		for (int i = 0; i < components.size(); i++) {
			GameComponent component = components.get(i);
			component.render(queue);
		}
	}

	public void destroy() {
		if (parent != null) {
			parent.removeChild(this);
		}
	}

	public ArrayList<GameObject> getAllAttached() {
		ArrayList<GameObject> result = new ArrayList<GameObject>();

		for (int i = 0; i < children.size(); i++) {
			GameObject child = children.get(i);
			result.addAll(child.getAllAttached());
		}

		result.add(this);
		return result;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setScene(Scene scene) {
		if (this.scene != scene) {
			this.scene = scene;

			for (int i = 0; i < components.size(); i++) {
				GameComponent component = components.get(i);
				component.addToScene(scene);
			}

			for (int i = 0; i < children.size(); i++) {
				GameObject child = children.get(i);
				child.setScene(scene);
			}
		}
	}

	public ArrayList<GameObject> getChildren() {
		return children;
	}

	public ArrayList<GameComponent> getComponents() {
		return components;
	}

	/**
	 * Gets a component of a specific type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param type
	 *            the type
	 * @return the component or null if there is none
	 */
	public <T extends GameComponent> T getComponent(Class<T> type) {
		ArrayList<T> list = getComponents(type);
		if (list.size() > 0) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Gets a list of components of a specific type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param type
	 *            the type
	 * @return the components (might be empty if there is none)
	 */
	@SuppressWarnings("unchecked") public <T extends GameComponent> ArrayList<T> getComponents(Class<T> type) {
		ArrayList<T> result = new ArrayList<T>();
		for (int i = 0; i < components.size(); i++) {
			GameComponent component = components.get(i);
			if (type.isInstance(component)) {
				result.add((T) component);
			}
		}
		return result;
	}

	public GameObject getParent() {
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	public Scene getScene() {
		return scene;
	}
}
