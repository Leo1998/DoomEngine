package test;

import java.util.Random;

import com.doomengine.asset.AssetManager;
import com.doomengine.components.Camera3D;
import com.doomengine.math.Vector3f;
import com.doomengine.scene.GameObject;
import com.doomengine.scene.ModelKey;
import com.doomengine.scene.Scene;
import com.doomengine.system.DoomSystem;
import com.doomengine.system.Input;
import com.doomengine.system.SystemListener;

public class Test implements SystemListener {

	private GameObject player;
	private GameObject bike;

	private DoomSystem system;
	private AssetManager assetManager;

	public void create(DoomSystem system) {
		this.system = system;
		this.assetManager = system.getAssetManager();

		Scene scene = system.addScene(new Scene());

		player = new GameObject();
		player.addComponent(new FreeMove());
		player.addComponent(new FreeLook());
		player.addComponent(new LightDropper());
		player.addComponent(new Camera3D(800, 600, 70, 0.01f, 80.0f));

		this.bike = assetManager.loadAsset(new ModelKey("test/bike.obj"));
		this.bike.addComponent(new AutoTurn());
		this.bike.getTransform().setPosition(new Vector3f(-6.0f, 0.6f, 10.0f));

		scene.addObject(player);
		scene.addObject(bike);

		Random random = new Random();
		for (int i = 0; i < 150; i++) {
			GameObject cube = assetManager.loadAsset(new ModelKey("test/cube.obj"));

			cube.getTransform().setPosition(new Vector3f(random.nextInt(50), random.nextInt(20) + 10, random.nextInt(50)));

			scene.addObject(cube);
		}
	}

	@Override public void resize(int width, int height) {
	}

	@Override public void gainFocus() {
	}

	@Override public void loseFocus() {
	}

	@Override public void update() {
		if (Input.getKey(Input.Keys.KEY_R)) {
			system.getContext().restart();
		}
	}

	@Override public void destroy() {
	}

}