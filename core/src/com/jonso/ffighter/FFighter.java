package com.jonso.ffighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Handles initializing the Title Screen.
 * Contains the SpriteBatch as well as the default font to draw with.
 * @author Jonathan So
 */
public class FFighter extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	

	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new GameScreen(this));
	}

	public void render () {
		super.render();
	}

	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
