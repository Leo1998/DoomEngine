package com.doomengine.scene;

import java.util.ArrayList;

import com.doomengine.renderer.RenderQueue;

public class SceneList {

	private ArrayList<Scene> scenes;

	public SceneList() {
		scenes = new ArrayList<Scene>();
	}

	public Scene addScene(Scene scene) {
		scenes.add(scene);

		return scene;
	}

	public Scene getScene(int i) {
		return scenes.get(i);
	}

	public Scene removeScene(int i) {
		return scenes.remove(i);
	}

	public void removeScene(Scene scene) {
		scenes.remove(scene);
	}

	public void inputAll() {
		for (int i = 0; i < scenes.size(); i++) {
			Scene scene = scenes.get(i);
			scene.input();
		}
	}

	public void tickAll() {
		for (int i = 0; i < scenes.size(); i++) {
			Scene scene = scenes.get(i);
			scene.tick();
		}
	}

	public void renderAll(RenderQueue queue) {
		for (int i = 0; i < scenes.size(); i++) {
			Scene scene = scenes.get(i);
			scene.render(queue);
		}
	}

}
