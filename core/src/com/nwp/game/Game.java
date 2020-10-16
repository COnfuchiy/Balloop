package com.nwp.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.objects.TouchAdapter;
import com.nwp.game.source.BallsActions;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Array<Texture> textures;
	static Texture second_ball;
	private TouchAdapter tmp_adapter;
	private BallsActions main_actions;
	OrthographicCamera camera;
	private Array<Integer> y_levels;
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
		y_levels = new Array<>();
		y_levels.add(800);
		y_levels.add(1400);
		Array<Float> velocities = new Array<>();
		velocities.add(2.0f,3.0f);
		textures = new Array<>();
		textures.add(new Texture(Gdx.files.internal("ball.png")));
		textures.add(new Texture(Gdx.files.internal("ball_1.png")));
		textures.add(new Texture(Gdx.files.internal("ball_2.png")));
		tmp_adapter = new TouchAdapter(textures,500,30,100,y_levels,-10);
		main_actions = new BallsActions(y_levels, velocities,textures,tmp_adapter,width);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		tmp_adapter.updates(batch);
		main_actions.balls_update(batch);
		batch.end();

		Gdx.input.setInputProcessor(new InputAdapter(){
			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				tmp_adapter.handle_touch(new Vector2(touchPos.x, touchPos.y));
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
