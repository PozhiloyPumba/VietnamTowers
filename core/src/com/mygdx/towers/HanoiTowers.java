package com.mygdx.towers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class HanoiTowers extends ApplicationAdapter {
	private Music backgroundMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Array<Stick> sticks = new Array<Stick>();
	private ShapeRenderer shapeRenderer;
	private long width;
	private long height;
	private Array<Color> colors = new Array<>();
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();
	private final long initTime = System.currentTimeMillis();

	@Override
	public void create() {
		// load the drop sound effect and the rain background "music"
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("TheHouseOfRisingSun.wav"));

		// start the playback of the background music immediately
		backgroundMusic.setLooping(true);
		backgroundMusic.play();

		// font
		font = new BitmapFont(Gdx.files.internal("fonts/Danfo.fnt"), Gdx.files.internal("fonts/Danfo.png"), false);

		// another
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		// TODO: maybe pallete
		colors.addAll(Color.SKY, Color.CHARTREUSE, Color.CYAN, Color.GOLD, Color.OLIVE, Color.TAN, Color.FIREBRICK, 
				Color.PURPLE, Color.PINK);

		// create sticks

		for (int i = 0; i < 3; ++i) {
			Rectangle f = new Rectangle(i * width / 3, 0, width / 3, height);
			Stick s = new Stick(width / 6 + i * width / 3, height / 6, width / 3.6f, height / 6, f);
			if(i == 0) {
				for (int j = 0; j < 10; ++j) {
					s.add(new Pancake(0, 0, colors.get(j % colors.size), 3.5f - 0.3f * j));
				}
			}
			sticks.add(s);
		}

		Gdx.input.setInputProcessor(new InputMy(sticks, camera));
	}

	private void renderMy() {
        Gdx.gl.glLineWidth(3);
		for(Stick s: sticks) {
			s.drawPlane(shapeRenderer);
		}
		for(Stick s: sticks) {
			s.draw(shapeRenderer);
		}
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		//-----------------------------------------------------

		// render scene
		batch.begin();
		renderMy();
		batch.end();

		// hack because of text drawing
		batch.begin();
		font.getData().setScale(1.5f);
		layout.setText(font, "Hanoi Towers");
		font.draw(batch, layout, (width - layout.width) / 2.f, height * 19.f / 20.f);

		font.getData().setScale(1.3f);
		layout.setText(font, "Time: ");
		font.draw(batch, layout, (width - layout.width) / 3.f, height * 17.f / 20.f);
		layout.setText(font, String.format("%.1f", (System.currentTimeMillis() - initTime) / 1000.f) + " s");
		font.draw(batch, layout, 2.f * (width - layout.width) / 3.f, height * 17.f / 20.f);
		batch.end();

		//-----------------------------------------------------
	}

	@Override
	public void dispose() {
		backgroundMusic.dispose();
		shapeRenderer.dispose();
		font.dispose();
		batch.dispose();
	}
}
