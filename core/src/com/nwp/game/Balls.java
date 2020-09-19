package com.nwp.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.nwp.game.objects.Ball;

public class Balls extends ApplicationAdapter {
	Array<Ball> balls;
	static Ball ShootBall;
	SpriteBatch batch;
	static Ball ball; // экземпляр мяча
	static Texture ballTexture; // текстура для мяча
	static Texture arrow_Texture;
	static Sprite arrow_sprite;
	OrthographicCamera camera;
	long last_ball;
	float height;
	float width;

	private void spawn_ball(){
		ball = new Ball();
		ballTexture = new Texture(Gdx.files.internal("ball.png"));
		ball.ballSprite = new Sprite(ballTexture);
		ball.ballSprite.setSize(ball.ballSprite.getWidth(), ball.ballSprite.getHeight());
		ball.position.set(0, height-ball.ballSprite.getHeight());
		last_ball = TimeUtils.nanoTime();
		balls.add(ball);
		ball.velocity.set(0.9f, -1.9f);
	}

	@Override
	public void create () {
		balls = new Array<Ball>();
		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();
		camera = new OrthographicCamera(width,height);// устанавливаем переменные высоты и ширины в качестве области просмотра нашей игры
		camera.setToOrtho(false);// этим методом мы центруем камеру на половину высоты и половину ширины
		batch = new SpriteBatch();
		spawn_ball();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for(Ball ball: balls) {
			ball.set_velocity();
			ball.update();
			ball.render(batch);
		}
		if (arrow_sprite!=null)
			arrow_sprite.draw(batch);
		if (ShootBall!=null){
			ShootBall.update();
			ShootBall.render(batch);
		}
		if(TimeUtils.nanoTime() - last_ball > 460000000)
			spawn_ball();
		batch.end();
		Gdx.input.setInputProcessor(new InputAdapter(){
			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				ShootBall = new Ball();
				ballTexture = new Texture(Gdx.files.internal("ball.png"));
				ShootBall.ballSprite = new Sprite(ballTexture);
				ShootBall.ballSprite.setSize(ball.ballSprite.getWidth(), ball.ballSprite.getHeight());
				ShootBall.position.set(touchPos.x, touchPos.y);
				ShootBall.velocity.set(0,0);
				arrow_Texture = new Texture(Gdx.files.internal("arrow.png"));
				arrow_sprite = new Sprite(arrow_Texture);
				arrow_sprite.setSize(arrow_sprite.getWidth(), arrow_sprite.getHeight());
				arrow_sprite.setPosition(touchPos.x-arrow_sprite.getWidth(), touchPos.y);
				arrow_sprite.setOrigin(arrow_sprite.getWidth()/2, arrow_sprite.getHeight()/2);
				return false;
			}
			@Override
			public boolean touchUp (int x, int y, int pointer, int button) {
				ShootBall = null;
				return false;
			}
			@Override
			public boolean touchDragged (int x, int y, int pointer) {
				Vector2 center = new Vector2(ShootBall.position.x+ball.ballSprite.getWidth()/2, ShootBall.position.y+ball.ballSprite.getHeight()/2);
				Vector2 point1 = new Vector2(x,y);
				Vector2 point2 = new Vector2(x, center.y);

				point1.sub(center).nor();
				point2.sub(center).nor();

				float angle = (MathUtils.atan2(point1.y, point1.x) - MathUtils.atan2(point2.y, point2.x));
				angle *= MathUtils.radiansToDegrees;
				if (angle<-180)
					arrow_sprite.setRotation(270+angle);
				else
					arrow_sprite.setRotation(angle);
				System.out.println(Float.toString(arrow_sprite.getRotation()) +" ^^ "+ Integer.toString(x) +" ^^ "+ Integer.toString(y));
				return false;
			}
		});

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		ballTexture.dispose();
		arrow_Texture.dispose();
	}
}
