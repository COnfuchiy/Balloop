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
import com.nwp.game.source.Gutter;

public class Game extends ApplicationAdapter {
	static Ball ShootBall;
	SpriteBatch batch;
	Array<Texture> textures;
	private Gutter test_gutter;
	static Texture second_ball;
	OrthographicCamera camera;
	int touch_x;
	int touch_y;
	float height;
	float width;

	public Game(){
	}

	public static int get_rand_in_range(int min, int max){
		return (int)(Math.random()*((max-min)+1))+min;
	}


	@Override
	public void create () {
		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();
		camera = new OrthographicCamera(width,height);// устанавливаем переменные высоты и ширины в качестве области просмотра нашей игры
		camera.setToOrtho(false);// этим методом мы центруем камеру на половину высоты и половину ширины
		batch = new SpriteBatch();
		textures = new Array<>();
		textures.add(new Texture(Gdx.files.internal("ball.png")));
		textures.add(new Texture(Gdx.files.internal("ball_1.png")));
		textures.add(new Texture(Gdx.files.internal("ball_2.png")));
		test_gutter = new Gutter(1000,2.5f,width,textures);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		test_gutter.render_gutter(batch,ShootBall);
		if (ShootBall!= null && ShootBall.position.x==0)
			ShootBall = null;
//		for(Ball ball: balls) {
//			if (is_move_balls){
//				ball.set_velocity();
//				ball.update();
//			}
//			else
//				if (pos_collapse!=-1 && balls.indexOf(ball,false)<pos_collapse){
//					if (ball.position.x == cord_collapse.x) {
//						System.out.println(String.valueOf(balls.indexOf(ball, false)) + " " + String.valueOf(pos_collapse));
////						Ball new_ball;
////						new_ball= balls.get(pos_collapse+1);
////						if (new_ball.ballSprite.getTexture().toString().equals(ball.ballSprite.getTexture().toString())){
////							int left = pos_collapse;
////							int right = pos_collapse;
////							do{
////								left--;
////								new_ball= balls.get(left);
////							}while (left > 0 && new_ball.ballSprite.getTexture().toString().equals(ball.ballSprite.getTexture().toString()));
////							do{
////								right++;
////								new_ball= balls.get(right);
////							}while  (new_ball.ballSprite.getTexture().toString().equals(ball.ballSprite.getTexture().toString()));
////							if (right-left>=3){
////								pos_collapse = left+1;
////								cord_collapse = new Vector2(balls.get(right-1).position);
////								balls.removeRange(left+1,right-1);
////							}
////							else{
////								is_move_balls = true;
////								cord_collapse = null;
////								pos_collapse = - 1;
////							}
////						}
////						else{
////
////							cord_collapse = null;
////							pos_collapse = - 1;
////						}
////					}
//						is_move_balls = true;
//						ball.update();
//					}
//				}
//			ball.render(batch);
//
////					new_ball = balls.get(ball_pos-1);
////					if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
////						new_ball = balls.get(ball_pos-2);
////						if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
////							pos_collapse = ball_pos-3;
////							cord_collapse = new Vector2(balls.get(ball_pos).position);
////							balls.removeRange(ball_pos-2,ball_pos);
////						}
////						else {
////							new_ball = balls.get(ball_pos+1);
////							if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
////								pos_collapse = ball_pos-2;
////								cord_collapse = new Vector2(balls.get(ball_pos+1).position);
////								balls.removeRange(ball_pos-1,ball_pos+1);
////							}
////						}
////					}
////					else {
////						new_ball = balls.get(ball_pos+1);
////						if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
////							new_ball = balls.get(ball_pos+2);
////							if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
////								pos_collapse = ball_pos-1;
////								cord_collapse = new Vector2(balls.get(ball_pos+2).position);
////								balls.removeRange(ball_pos,ball_pos+2);
////							}
////						}
////					}
////					if (pos_collapse!=-1){
////						pos_collapse++;
////						for(int i = pos_collapse; i>=0;i--){
////							Vector2 speed = new Vector2(balls.get(i).velocity);
////							balls.get(i).velocity.set(speed.rotate(180).scl(1.5f,1.5f));
////						}
////					}
////					else
////						is_move_balls = true;
//				}
//			}
//		}
		if (ShootBall!=null){
			ShootBall.update();
			ShootBall.render(batch);
		}
		batch.end();
		if (Gdx.input.isTouched()) {
			if (ShootBall!=null) {
				Vector2 center = new Vector2(ShootBall.position.x+ShootBall.ballSprite.getWidth()/2, ShootBall.position.y+ShootBall.ballSprite.getHeight()/2);
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
				ShootBall = new Ball(new Texture(Gdx.files.internal("ball_1.png")),new Vector2(touchPos.x, touchPos.y));
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
		second_ball.dispose();
	}
}
