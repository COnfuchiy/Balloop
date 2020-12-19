package com.nwp.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.levels.Level;
import com.nwp.game.levels.LevelData;
import com.nwp.game.levels.LevelsJson;
import com.nwp.game.objects.CurrentBallViewer;
import com.nwp.game.objects.GutterBar;
import com.nwp.game.objects.TouchAdapter;
import com.nwp.game.source.BallsActions;
import com.nwp.game.source.Json;

import java.util.Currency;
import java.util.concurrent.TimeUnit;

public class Game extends ApplicationAdapter {
    //render vars
    private OrthographicCamera camera;
    private SpriteBatch batch;
    //input adapter
    private TouchAdapter tmp_adapter;
    //balls adapter
    private BallsActions main_actions;
    //current level
    private Level level;
    private int level_index;
    private Stage stage;

    private BitmapFont FontRed1;
    private boolean is_level_complete = false;

    private CurrentBallViewer ballViewer;

    //dispose image
	private Array<Texture> textures;
    //settings

    private int default_touch_area = 500;
    private int forbidden_touch_area = 30;
    private int default_touch_delay = 50;
    private float down_speed = -15;

    //display data
    public float height;
    public float width;

    public Game(int level_index) {
        this.level_index = level_index;
        this.level =new LevelData().getLevel(level_index);
    }

    public Stage getStage(){
        return stage;
    }

    public static int get_rand_in_range(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    private Array<Texture> get_textures_by_name(Array<String> textures_names) {
        Array<Texture> textures = new Array<>();
        for (String name : textures_names) {
            textures.add(new Texture(Gdx.files.internal(name)));
        }
        return textures;
    }

    @Override
    public void create() {
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        Array<Integer> y_levels = level.getGutters_positions();
        Array<Float> velocities = level.getGutters_velocities();
        Array<Integer> num_level_balls = level.getGutters_num_balls();
        Array<Texture> textures = get_textures_by_name(level.getBall_spites());
        stage = new Stage();
        tmp_adapter = new TouchAdapter(textures,
				default_touch_area, forbidden_touch_area, default_touch_delay, y_levels, down_speed);
        ballViewer = new CurrentBallViewer(tmp_adapter.get_current_ball_texture(),width,y_levels.get(y_levels.size-1));
        tmp_adapter.setCurrentBallViewer(ballViewer);
        main_actions = new BallsActions(y_levels, velocities, textures, tmp_adapter, num_level_balls,width, stage);
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                tmp_adapter.handle_touch(new Vector2(touchPos.x, touchPos.y));
                return false;
            }
        });
        FontRed1 = new BitmapFont();
        FontRed1.setColor(Color.RED); //Красный
        FontRed1.getData().setScale(3,3);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!is_level_complete){
            batch.begin();
            tmp_adapter.updates(batch);
            is_level_complete = main_actions.balls_update(batch);
            ballViewer.render(batch);
            batch.end();
            if (is_level_complete){
                stage.clear();
                Gdx.input.setInputProcessor(new InputAdapter() {
                    @Override
                    public boolean touchDown(int x, int y, int pointer, int button) {
                        LevelData levelData = new LevelData();
                        String file_data = Game.read_file();
                        LevelsJson levelsJson = Json.DeserializeObject(LevelsJson.class, file_data);
                        levelData.set_levels_completed_state(levelsJson.is_completed);
                        levelData.set_level_completed(level_index);
                        levelsJson = new LevelsJson();
                        levelsJson.set_json(levelData.get_levels_completed_state());
                        String json = Json.SerializeObject(levelsJson);
                        Game.write_file(json);
                        Gdx.app.exit();
                        return false;
                    }
                });
            }
        }
        else {
            batch.begin();
            FontRed1.draw(batch,"Level "+level.getName() + " complete!", width/2-200, height/2);
            batch.end();

        }
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
    	ballViewer.ballSprite.getTexture().dispose();
        stage.dispose();
        batch.dispose();
    }

    private static void write_file(String data){
        FileHandle file = Gdx.files.external("levels_data");
        file.writeString(data,false);
    }

    private static String read_file(){
        FileHandle file = Gdx.files.external("levels_data");
        return file.readString();
    }
}
