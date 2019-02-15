package io.piotrjastrzebski.ftftest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FTFGame extends ApplicationAdapter {
	private static final String TAG = FTFGame.class.getSimpleName();

	public static final int WIDTH = 720;
	public static final int HEIGHT = 1280;

	private final static String FONT_BOLD = "fonts/Roboto-Bold.ttf";
	private final static String FONT_BOLD_ITALIC = "fonts/Roboto-BoldItalic.ttf";
	private final static String FONT_THIN = "fonts/Roboto-Thin.ttf";
	private final static String FONT_THIN_ITALIC = "fonts/Roboto-ThinItalic.ttf";
//	private final static String[] FONTS = { FONT_BOLD, FONT_BOLD_ITALIC, FONT_THIN, FONT_THIN_ITALIC };
	private final static String[] FONTS = { FONT_THIN_ITALIC };
	private final static String CHARS = FreeTypeFontGenerator.DEFAULT_CHARS + "żźłóćąęńśŻŹŁÓĆĄĘŃŚ";

	SpriteBatch batch;
	Texture img;
	ExtendViewport viewport;
	OrthographicCamera camera;
	AssetManager am;

	ObjectMap<String, BitmapFont> fonts = new ObjectMap<>();

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(WIDTH, HEIGHT, camera);

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");


		am = new AssetManager();

		FreeTypeFontGenerator.setMaxTextureSize(1024);
		am.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(am.getFileHandleResolver()));
		am.setLoader(BitmapFont.class, new FreetypeFontLoader(am.getFileHandleResolver()));

		rebuild();

//		font.getData().setScale(.5f);
	}

	private void rebuild () {
		fonts.clear();
		am.clear();
		final int size = 1024;
		PixmapPacker packer = new PixmapPacker(size, size, Pixmap.Format.RGBA8888, 4, false);
		for (String font : FONTS) {
			load(font, packer);
		}
	}

	private void load (final String font, final PixmapPacker packer) {
		// figure out the size we want
		// In this case we want the size to be 1/24 of the height
		// then we scale it so it is pixel perfect on current display
		// doesnt work ...
//		final float defaultFontSize = (float)HEIGHT/24f * (float)HEIGHT/(float)Gdx.graphics.getHeight();
		final float defaultFontSize = (float)HEIGHT/24f;// * (float)Gdx.graphics.getHeight()/HEIGHT;

		final String path = ".ftftest/fonts-"+MathUtils.round(defaultFontSize)+".png";

		final FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		parameter.fontFileName = font;
		parameter.fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.fontParameters.size = MathUtils.round(defaultFontSize);
		parameter.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		parameter.fontParameters.characters += "żźłóćąęńśŻŹŁÓĆĄĘŃŚ";
//		parameter.fontParameters.shadowOffsetX = Math.max(MathUtils.round(defaultFontSize/10), 1);
//		parameter.fontParameters.shadowOffsetY = Math.max(MathUtils.round(defaultFontSize/10), 1);
		// Nearest might be better
		parameter.fontParameters.minFilter = Texture.TextureFilter.Linear;
		parameter.fontParameters.magFilter = Texture.TextureFilter.Linear;
//		parameter.fontParameters.shadowColor = new Color(0, 0, 0, 0.6f);
//		parameter.fontParameters.spaceX = parameter.fontParameters.shadowOffsetX;
//		parameter.fontParameters.spaceY = parameter.fontParameters.shadowOffsetY;
		parameter.fontParameters.kerning = true;
		parameter.fontParameters.packer = packer;

		parameter.loadedCallback = new AssetLoaderParameters.LoadedCallback() {
			@Override public void finishedLoading (AssetManager assetManager, String fileName, Class type) {
				BitmapFont bitmapFont = assetManager.get(fileName, BitmapFont.class);
//				bitmapFont.setUseIntegerPositions(false);
				// scale the font so its pixel perfect
//				float scale = (HEIGHT)/(float)Gdx.graphics.getBackBufferHeight();
//				bitmapFont.getData().setScale(scale);
				Gdx.app.log(TAG, "'" + font + "' loaded, regions = " + bitmapFont.getRegions().size);
				fonts.put(font, bitmapFont);
				// bad!
				if (fonts.size == FONTS.length) {
					Texture texture = bitmapFont.getRegion().getTexture();
					Pixmap pixmap = texture.getTextureData().consumePixmap();
					PixmapIO.writePNG(Gdx.files.external(path), pixmap);
					packer.dispose();
				}
			}
		};
		am.load(font, BitmapFont.class, parameter);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			rebuild();
		}
		am.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.setColor(Color.WHITE);
//		float size = WIDTH * .5f;
//		batch.draw(img, WIDTH/2f - size/2, HEIGHT - size, size, size);

		float y = HEIGHT * .95f;
		float yStep = HEIGHT / 5f;

		for (String font : FONTS) {
			BitmapFont bitmapFont = fonts.get(font, null);
			if (bitmapFont != null) {
				bitmapFont.draw(batch, CHARS, WIDTH * .05f, y, WIDTH * .9f, Align.left, true);
				y -= yStep;
			}
		}

//		font.draw(batch, CHARS, 0, HEIGHT/4f, WIDTH, Align.left, true);
		batch.end();
	}

	@Override public void resize (int width, int height) {
		viewport.update(width, height, false);
		// expand in both direction
		camera.position.x = WIDTH / 2f;
		camera.position.y = HEIGHT / 2f;
		camera.update();
		if (true) rebuild();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
