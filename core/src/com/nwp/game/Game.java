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
import com.nwp.game.objects.Ball;
import com.nwp.game.objects.TouchAdapter;
import com.nwp.game.source.Gutter;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Array<Texture> textures;
	private Gutter test_gutter;
	static Texture second_ball;
	static Sprite background;
	private TouchAdapter test_adapter;
	OrthographicCamera camera;
	private Array<Integer> y_levels;
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
		background = new Sprite(new Texture(Gdx.files.internal("gutter.png")));
		background.setSize(background.getWidth(), background.getHeight());
		background.setPosition(0,950);
		y_levels = new Array<>();
		y_levels.add(1000);
		textures = new Array<>();
		textures.add(new Texture(Gdx.files.internal("ball.png")));
		textures.add(new Texture(Gdx.files.internal("ball_1.png")));
		textures.add(new Texture(Gdx.files.internal("ball_2.png")));
		test_gutter = new Gutter(1000,2.5f,width,textures);
		test_adapter = new TouchAdapter(textures,500,30,100,-10);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		test_adapter.update_counters();
		test_gutter.render_gutter(batch,test_adapter.get_shoot_ball());
		if (test_adapter.is_ball_shooting() && test_adapter.get_shoot_ball().position.x==0)
			test_adapter.delete_ball();
		if (test_adapter.is_ball_shooting()){
			test_adapter.get_shoot_ball().update();
			test_adapter.get_shoot_ball().render(batch);
		}
		batch.end();

		Gdx.input.setInputProcessor(new InputAdapter(){
			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				test_adapter.handle_touch(new Vector2(touchPos.x, touchPos.y), y_levels);
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
