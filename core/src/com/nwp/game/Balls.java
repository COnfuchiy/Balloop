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
import com.nwp.game.source.Path;

public class Balls extends ApplicationAdapter {
	Array<Ball> balls;
	static int color_counter =0;
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
	Path path;

	private void spawn_ball(){
		ball = new Ball(path);
		if (color_counter<2)
			ballTexture = new Texture(Gdx.files.internal("ball_2.png"));
		else if (color_counter<4)
			ballTexture = new Texture(Gdx.files.internal("ball_1.png"));
		else {
			ballTexture = new Texture(Gdx.files.internal("ball.png"));
		}
		if (color_counter==5)
			color_counter=0;
		else
			color_counter++;
		ball.ballSprite = new Sprite(ballTexture);
		ball.ballSprite.setSize(ball.ballSprite.getWidth(), ball.ballSprite.getHeight());
		ball.position.set(0, height-ball.ballSprite.getHeight());
		last_ball = TimeUtils.nanoTime();
		balls.add(ball);
		ball.velocity.set(2, -2);
	}

	@Override
	public void create () {
		balls = new Array<Ball>();
		Array<Vector2> velocity = new Array<Vector2>();
		Array<Integer> velocity_iter = new Array<Integer>();
		velocity.add(new Vector2(2, -2));
		velocity.add(new Vector2(-3, -3));
		velocity_iter.add(100);
		velocity_iter.add(10000);
		path = new Path(velocity, velocity_iter);
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
				if (pos_collapse!=-1 && balls.indexOf(ball,false)<pos_collapse){
					if (ball.position.x - cord_collapse.x<1){
						System.out.println(String.valueOf(balls.indexOf(ball,false))+" "+ String.valueOf(pos_collapse));
						Ball new_ball;
						new_ball= balls.get(pos_collapse+1);
						if (new_ball.ballSprite.getTexture().toString().equals(ball.ballSprite.getTexture().toString())){
							int left = pos_collapse;
							int right = pos_collapse;
							do{
								left--;
								new_ball= balls.get(left);
							}while (left > 0 && new_ball.ballSprite.getTexture().toString().equals(ball.ballSprite.getTexture().toString()));
							do{
								right++;
								new_ball= balls.get(right);
							}while  (new_ball.ballSprite.getTexture().toString().equals(ball.ballSprite.getTexture().toString()));
							if (right-left>=3){
								pos_collapse = left+1;
								cord_collapse = new Vector2(balls.get(right-1).position);
								balls.removeRange(left+1,right-1);
							}
							else{
								is_move_balls = true;
								cord_collapse = null;
								pos_collapse = - 1;
							}
						}
						else{
							is_move_balls = true;
							cord_collapse = null;
							pos_collapse = - 1;
						}
					}
					ball.update();

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
					Ball new_ball = new Ball(path);
					new_ball.ballSprite = new Sprite(second_ball);
					new_ball.ballSprite.setSize(ball.ballSprite.getWidth(), ball.ballSprite.getHeight());
					new_ball.position.set(ShootBall.position.x, ShootBall.position.y);
					new_ball.set_velocity();
					balls.insert(ball_pos,new_ball);
					String shoot_tex = ShootBall.ballSprite.getTexture().toString();
					ShootBall = null;
					for(int i = ball_pos; i>=0;i--){
						Vector2 speed = new Vector2(balls.get(i).velocity);
						balls.get(i).position.add(speed.scl(30,30));
						balls.get(i).ballSprite.setPosition(balls.get(i).position.x,balls.get(i).position.y);
					}
					new_ball = balls.get(ball_pos-1);
					if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
						new_ball = balls.get(ball_pos-2);
						if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
							pos_collapse = ball_pos-3;
							cord_collapse = new Vector2(balls.get(ball_pos).position);
							balls.removeRange(ball_pos-2,ball_pos);
						}
						else {
							new_ball = balls.get(ball_pos+1);
							if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
								pos_collapse = ball_pos-2;
								cord_collapse = new Vector2(balls.get(ball_pos+1).position);
								balls.removeRange(ball_pos-1,ball_pos+1);
							}
						}
					}
					else {
						new_ball = balls.get(ball_pos+1);
						if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
							new_ball = balls.get(ball_pos+2);
							if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
								pos_collapse = ball_pos-1;
								cord_collapse = new Vector2(balls.get(ball_pos+2).position);
								balls.removeRange(ball_pos,ball_pos+2);
							}
						}
					}
					if (pos_collapse!=-1){
						pos_collapse++;
						for(int i = pos_collapse; i>=0;i--){
							Vector2 speed = new Vector2(balls.get(i).velocity);
							balls.get(i).velocity.set(speed.rotate(180).scl(1.5f,1.5f));
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
				ShootBall = new Ball(path);
				second_ball = new Texture(Gdx.files.internal("ball_"+String.valueOf((int)(Math.random()*((2-1)+1))+1)+".png"));
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
