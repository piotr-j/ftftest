package io.piotrjastrzebski.ftftest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import io.piotrjastrzebski.ftftest.FTFGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		if (UIUtils.isMac) {
			config.width = FTFGame.WIDTH / 2;
			config.height = FTFGame.HEIGHT / 2;
		} else {
			config.width = FTFGame.WIDTH;
			config.height = FTFGame.HEIGHT;
		}
		config.title = "FTF Test";
		config.useHDPI = true;
		new LwjglApplication(new FTFGame(), config);
	}
}
