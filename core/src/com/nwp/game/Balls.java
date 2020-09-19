package com.nwp.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.nwp.game.objects.Ball;

import jdk.nashorn.internal.objects.Global;

public class Balls extends ApplicationAdapter {
	Array<Ball> balls;
	static Ball ShootBall;
	SpriteBatch batch;
	static Ball ball; // экземпляр мяча
	static Texture ballTexture; // текстура для мяча
	static Texture arrow_Texture;
	static Sprite arrow_sprite;
	int touch_x;
	int touch_y;
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
			if (ShootBall!=null)
				if (ball.ballSprite.getBoundingRectangle().overlaps(ShootBall.ballSprite.getBoundingRectangle())){
					balls.removeValue(ball,false);
					ShootBall = null;
				}

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
		if (Gdx.input.isTouched()) {
			if (ShootBall!=null){
				Vector2 center = new Vector2(ShootBall.position.x+ball.ballSprite.getWidth()/2, ShootBall.position.y+ball.ballSprite.getHeight()/2);
				Vector2 point1 = new Vector2(touch_x + 2 * (center.x - touch_x),touch_y + 2 * (center.y - touch_y));
				//arrow_sprite.setRotation(angle-90);
				ShapeRenderer sr = new ShapeRenderer();
				sr.setColor(Color.BLACK);
				sr.begin(ShapeRenderer.ShapeType.Filled);
				sr.rectLine(center.x, center.y, point1.x, point1.y, 10);
				//System.out.println(Float.toString(point1.x) + " " + Float.toString(point1.y));
				sr.end();
			}

		}
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
//				arrow_Texture = new Texture(Gdx.files.internal("arrow.png"));
//				arrow_sprite = new Sprite(arrow_Texture);
//				arrow_sprite.setSize(arrow_sprite.getWidth(), arrow_sprite.getHeight());
//				arrow_sprite.setPosition(touchPos.x, touchPos.y-arrow_sprite.getHeight());
//				arrow_sprite.setOrigin(arrow_sprite.getWidth()/2, arrow_sprite.getHeight()/2);
				return false;
			}
			@Override
			public boolean touchUp (int x, int y, int pointer, int button) {
				Vector2 center = new Vector2(ShootBall.position.x+ShootBall.ballSprite.getWidth()/2, ShootBall.position.y+ShootBall.ballSprite.getHeight()/2);
				Vector2 dir = new Vector2();
				Vector2 touch = new Vector2(touch_x,touch_y);
				dir.set(touch).sub(center).nor();
				ShootBall.velocity.set(dir.rotate(180).scl(8,8));
				return false;
			}
			@Override
			public boolean touchDragged (int x, int y, int pointer) {
				touch_x = x;
				touch_y = (int)height - y;
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
