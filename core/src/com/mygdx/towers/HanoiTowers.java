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
	private Music winMusic;
	private SpriteBatch batch;
	private BitmapFont font;
	public OrthographicCamera camera;
	public Array<Stick> sticks = new Array<Stick>();
	private ShapeRenderer shapeRenderer;
	public Bot_3 bot;
	private long width;
	private long height;
	private Array<Color> colors = new Array<>();
	private GlyphLayout layout = new GlyphLayout();
	private long initTime = System.currentTimeMillis();
	private float currentTime = 0;
	public int moves = 0;
	public int botMoves = 0;
	static public final int numDisks = 10;
	private final int numTowers = 3;
	public boolean win = false;
	private boolean quite = true;

	@Override
	public void create() {
		// load the drop sound effect and the rain background "music"
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("TheHouseOfRisingSun.wav"));
		winMusic = Gdx.audio.newMusic(Gdx.files.internal("tevvez-legend.wav"));

		// start the playback of the background music immediately
		backgroundMusic.setLooping(true);
		winMusic.setLooping(true);
		if(quite) {
			backgroundMusic.setVolume(0.f);
			winMusic.setVolume(0.f);
		}

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
		colors.addAll(Color.SKY, Color.CHARTREUSE, Color.CYAN, Color.GOLDENROD, Color.OLIVE, Color.TAN, Color.FIREBRICK, 
				Color.PURPLE, Color.PINK);

		reset();

		Gdx.input.setInputProcessor(new InputMy(this));
		bot = new Bot_3(this);
	}
	
	public void reset() {
		win = false;
		moves = 0;
		botMoves = 0;
		initTime = System.currentTimeMillis();
		if(!backgroundMusic.isPlaying()) {
			backgroundMusic.play();
		}
		if(winMusic.isPlaying()) {
			winMusic.stop();
		}
		fillSticks();
	}

	private void fillSticks() {
		sticks.clear();
		// create sticks
		for (int i = 0; i < numTowers; ++i) {
			Rectangle f = new Rectangle(i * width / numTowers, 0, width / numTowers, height);
			Stick s = new Stick(width / (2 * numTowers) + i * width / numTowers, 
								height / 6, width / (numTowers + 0.6f), height / 6,
								f, (i == 0));
			if(i == 0) {
				for (int j = 0; j < numDisks; ++j) {
					s.add(new Pancake(0, 0, colors.get(j % colors.size), numDisks - j));
				}
			}
			sticks.add(s);
		}
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

	private boolean checkWin() {
		for(Stick s: sticks) {
			if ((s.getSize() == numDisks) && !s.initial)
				return true;
		}
		return false;
	}

	@Override
	public void render() {
		bot.run();

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
		font.getData().setScale(2.5f);
		layout.setText(font, "Hanoi Towers");
		font.draw(batch, layout, (width - layout.width) / 2.f, (height) * 19.f / 20.f - layout.height / 2);

		if(win) {
			if(backgroundMusic.isPlaying()) {
				backgroundMusic.stop();
			}
			if(!winMusic.isPlaying()) {
				winMusic.play();
			}
			font.getData().setScale(2.3f);
			layout.setText(font, "TIME TO WIN: " + String.format("%.1f", currentTime) + "S");
			font.draw(batch, layout, (width - layout.width) / 2.f, 2.f / 3.f * (height + 3 * layout.height));
			
			font.getData().setScale(2.3f);
			layout.setText(font, "MOVES: " + moves);
			font.draw(batch, layout, (width - layout.width) / 2.f, 2.f / 3.f * height);

			font.getData().setScale(2.3f);
			layout.setText(font, "BOT MOVES: " + botMoves);
			font.draw(batch, layout, (width - layout.width) / 2.f, 2.f / 3.f * height - 2.f * layout.height);

			font.getData().setScale(1.9f);
			layout.setText(font, "Press \'R\' to restart");
			font.draw(batch, layout, (width - layout.width) / 2.f, (height - 3.f * layout.height) / 2.f);
		}
		else {
			win = checkWin();
			currentTime = (System.currentTimeMillis() - initTime) / 1000.f;

			font.getData().setScale(1.9f);
			float timePos = 16.5f / 20.f;
			layout.setText(font, "Time: ");
			font.draw(batch, layout, (width - layout.width) / 3.f, height * timePos);

			layout.setText(font, String.format("%.1f", currentTime) + " s");
			font.draw(batch, layout, 2.f * (width - layout.width) / 3.f, height * timePos);

			float yourMovesPos = 15.f / 20.f;
			layout.setText(font, "YOUR MOVES: ");
			font.draw(batch, layout, (width - layout.width) / 3.f, height * yourMovesPos);
			
			layout.setText(font, String.format("%d", moves));
			font.draw(batch, layout, 2.f * (width - layout.width) / 3.f, height * yourMovesPos);

			float botMovesPos = 13.5f / 20.f;
			layout.setText(font, "BOT MOVES: ");
			font.draw(batch, layout, (width - layout.width) / 3.f, height * botMovesPos);
			
			layout.setText(font, String.format("%d", botMoves));
			font.draw(batch, layout, 2.f * (width - layout.width) / 3.f, height * botMovesPos);
		}
		
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
