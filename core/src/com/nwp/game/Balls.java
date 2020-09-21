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

public class Balls extends ApplicationAdapter {
	Array<Ball> balls;
	static Ball ShootBall;
	SpriteBatch batch;
	static Ball ball; // экземпляр мяча
	static Texture ballTexture; // текстура для мяча
	static Texture second_ball;
	static Vector2 cord_collapse;
	static int pos_collapse = -1;
	Vector2 pos_insert;
	int touch_x;
	int touch_y;
	OrthographicCamera camera;
	boolean is_move_balls = true;
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
			if (is_move_balls){
				ball.set_velocity();
				ball.update();
			}
			else
				if (pos_collapse!=-1 && ball.position.x < cord_collapse.x){
					ball.update();
					if (ball.position.y > cord_collapse.y){
						System.out.println(cord_collapse.toString() + " " + ball.position.toString());
						is_move_balls = true;
						for(int i = pos_collapse; i<balls.size;i++){
							balls.get(i).set_velocity();
						}
					}

				}
			ball.render(batch);
			if (ShootBall!=null) {
				if (is_move_balls &&
						ball.ballSprite.getBoundingRectangle().overlaps(ShootBall.ballSprite.getBoundingRectangle()) &&
						ShootBall.position.y<ball.position.y) {
					is_move_balls = false;
					pos_insert = new Vector2(ball.position);
					int ball_pos = balls.indexOf(ball,false);
					ShootBall.position.set(pos_insert.x,pos_insert.y);
					ShootBall.ballSprite.setPosition(pos_insert.x,pos_insert.y);
					ShootBall.velocity.set(0,0);
					Ball new_ball = new Ball();
					new_ball.ballSprite = new Sprite(second_ball);
					new_ball.ballSprite.setSize(ball.ballSprite.getWidth(), ball.ballSprite.getHeight());
					new_ball.position.set(ShootBall.position.x, ShootBall.position.y);
					new_ball.set_velocity();
					balls.insert(ball_pos,new_ball);
					ShootBall = null;
					for(int i = ball_pos; i>=0;i--){
						Vector2 speed = new Vector2(balls.get(i).velocity);
						balls.get(i).position.add(speed.scl(30,30));
						balls.get(i).ballSprite.setPosition(balls.get(i).position.x,balls.get(i).position.y);
					}
					new_ball = balls.get(ball_pos-1);
					if (new_ball.ballSprite.getTexture().toString().equals("ball_1.png")){
						new_ball = balls.get(ball_pos-2);
						if (new_ball.ballSprite.getTexture().toString().equals("ball_1.png")){
							pos_collapse = ball_pos-2;
							cord_collapse = new Vector2(balls.get(ball_pos-2).position);
							balls.removeRange(ball_pos-2,ball_pos);
						}
						else {
							new_ball = balls.get(ball_pos+1);
							if (new_ball.ballSprite.getTexture().toString().equals("ball_1.png")){
								pos_collapse = ball_pos-1;
								cord_collapse = new Vector2(balls.get(ball_pos-2).position);
								balls.removeRange(ball_pos-1,ball_pos+1);
							}
						}
					}
					else {
						new_ball = balls.get(ball_pos+1);
						if (new_ball.ballSprite.getTexture().toString().equals("ball_1.png")){
							new_ball = balls.get(ball_pos+2);
							if (new_ball.ballSprite.getTexture().toString().equals("ball_1.png")){
								pos_collapse = ball_pos;
								cord_collapse = new Vector2(balls.get(ball_pos-2).position);
								balls.removeRange(ball_pos,ball_pos+2);
							}
						}
					}
					if (pos_collapse!=-1){
						for(int i = pos_collapse+1; i>=0;i--){
							Vector2 speed = new Vector2(balls.get(i).velocity);
							balls.get(i).velocity.set(speed.rotate(180).scl(3,3));
						}
					}
					else
						is_move_balls = true;
				}
			}
		}
		if (ShootBall!=null){
			ShootBall.update();
			ShootBall.render(batch);
		}
		if(is_move_balls && TimeUtils.nanoTime() - last_ball > 460000000)
			spawn_ball();
		batch.end();
		if (Gdx.input.isTouched()) {
			if (ShootBall!=null) {
				Vector2 center = new Vector2(ShootBall.position.x+ball.ballSprite.getWidth()/2, ShootBall.position.y+ball.ballSprite.getHeight()/2);
				Vector2 point1 = new Vector2(touch_x,touch_y);
				Vector2 point2 = new Vector2(center.x + 1, center.y);
				point1.sub(center).nor();
				point2.sub(center).nor();
				float angle = (MathUtils.atan2(point1.y, point1.x) - MathUtils.atan2(point2.y, point2.x));
				Vector2 point3 = new Vector2(center.x + 300 * MathUtils.cos(MathUtils.PI - angle), center.y - 300 * MathUtils.sin(MathUtils.PI - angle));
				ShapeRenderer sr = new ShapeRenderer();
				sr.setColor(Color.BLACK);
				sr.begin(ShapeRenderer.ShapeType.Filled);
				sr.rectLine(center.x, center.y, point3.x, point3.y, 10);
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
				second_ball = new Texture(Gdx.files.internal("ball_1.png"));
				ShootBall.ballSprite = new Sprite(second_ball);
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
				ShootBall.velocity.set(dir.rotate(180).scl(12,12));
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
		second_ball.dispose();
	}
}
