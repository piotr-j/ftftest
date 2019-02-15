package io.piotrjastrzebski.ftftest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import io.piotrjastrzebski.ftftest.FTFGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		double scale = 1.0;
		if (UIUtils.isMac) {
			scale = .5;
		} else {
			scale = .7;
		}
		config.width = (int)(FTFGame.WIDTH * scale);
		config.height = (int)(FTFGame.HEIGHT * scale);
		config.title = "FTF Test";
		config.useHDPI = true;
		new LwjglApplication(new FTFGame(), config);
	}
}
