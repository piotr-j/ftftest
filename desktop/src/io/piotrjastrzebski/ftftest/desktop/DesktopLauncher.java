package io.piotrjastrzebski.ftftest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import io.piotrjastrzebski.ftftest.FTFGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		if (UIUtils.isMac) {
			config.width = (int)(FTFGame.WIDTH * .33);
			config.height = (int)(FTFGame.HEIGHT * .33);
		} else {
			config.width = (int)(FTFGame.WIDTH  * .7);
			config.height = (int)(FTFGame.HEIGHT * .7);
		}
		config.title = "FTF Test";
		config.useHDPI = true;
		new LwjglApplication(new FTFGame(), config);
	}
}
