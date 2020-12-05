package com.nwp.game;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.levels.Level;
import com.nwp.game.levels.LevelData;
import com.nwp.game.source.File;
import com.nwp.game.source.Json;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    public Array<Integer> IdList = new Array<>(new Integer[]{
            R.id.level_1,
            R.id.level_2,
            R.id.level_3,
            R.id.level_4,
            R.id.level_5,
            R.id.level_6,
            R.id.level_7,
            R.id.level_8,
            R.id.level_9,
            R.id.level_10,
            R.id.level_11,
            R.id.level_12,
            R.id.level_13,
            R.id.level_14,
            R.id.level_15,
            R.id.level_16,
            R.id.level_17,
            R.id.level_18,
            R.id.level_19,
            R.id.level_20,
    });

    private LevelData levelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        check_levels_file();
        levelData = Json.ReadFromFile(LevelData.class,File.filename);
        Array<Boolean> levels_completed = levelData.get_levels_completed_state();
        boolean is_last_level = false;
        for (int i = 0;i<levels_completed.size;i++){
            if (levels_completed.get(i)){
                is_last_level = false;
            }
            else {
                if(!is_last_level){
                    i++;
                    is_last_level = true;
                }
                set_locked_level_image(IdList.get(i));
            }
        }
    }

    private void set_locked_level_image(int id){
        ImageView imageView = findViewById(id);
        imageView.setImageResource(R.drawable.level_locked);
    }

    public int getLevelIndex(int id) {
        for (int i = 0; i < IdList.size; i++) {
            if (id == IdList.get(i)) return i;
        }
        return -1;
    }

    @Override
    public void onClick(View v) {
        //Level level = levelData.getLevel(getLevelIndex(v.getId()));
        //ImageView imageView = findViewById(id);
        System.out.println(getLevelIndex(v.getId()));
//        if (!level.is_locked) {
//
//        }
    }
    private void check_levels_file(){
        System.out.println("FILENAME: " + File.filename);
        FileHandle file = Gdx.files.internal("/assets/levels_data");
        if(!Gdx.files.local("/levels_data").exists()){
            LevelData levelData = new LevelData();
            Json.WriteToFile(File.filename, levelData);
        }
    }
}