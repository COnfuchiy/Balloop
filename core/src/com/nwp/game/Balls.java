package com.nwp.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.nwp.game.objects.Ball;

public class Balls extends ApplicationAdapter {
	Array<Ball> balls;
	SpriteBatch batch;
	static Ball ball; // экземпляр мяча
	static Texture ballTexture; // текстура для мяча
	OrthographicCamera camera;
	long last_ball;

	private void create_ball(){

	}

	@Override
	public void create () {
		balls = new Array<Ball>();
		float height = Gdx.graphics.getHeight();
		float width = Gdx.graphics.getWidth();
		camera = new OrthographicCamera(width,height);// устанавливаем переменные высоты и ширины в качестве области просмотра нашей игры
		camera.setToOrtho(false);// этим методом мы центруем камеру на половину высоты и половину ширины
		ball = new Ball();
		ballTexture = new Texture(Gdx.files.internal("ball.png"));
		ball.ballSprite = new Sprite(ballTexture);
		ball.ballSprite.setSize(ball.ballSprite.getWidth(), ball.ballSprite.getHeight());
		ball.position.set(0, height-ball.ballSprite.getHeight());
		ball.velocity.set(1, -2);
		last_ball = TimeUtils.nanoTime();
		balls.add(ball);
		batch = new SpriteBatch();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for(Ball ball: balls) {
			ball.update();
			ball.render(batch);
		}
		float height = Gdx.graphics.getHeight();
		if(TimeUtils.nanoTime() - last_ball > 2000000000) {
			ball = new Ball();
			ball.ballSprite = new Sprite(ballTexture);
			ball.ballSprite.setSize(ball.ballSprite.getWidth(), ball.ballSprite.getHeight());
			ball.position.set(0, height-ball.ballSprite.getHeight());
			ball.velocity.set(1, -2);
			last_ball = TimeUtils.nanoTime();
			balls.add(ball);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		ballTexture.dispose();
	}
}
