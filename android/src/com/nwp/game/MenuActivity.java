package com.nwp.game;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.levels.Level;
import com.nwp.game.levels.LevelData;
import com.nwp.game.source.File;
import com.nwp.game.source.Json;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        check_levels_file();
        LevelData levelData = Json.ReadFromFile(LevelData.class,File.filename);
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
                switch (i){
                    case 0:{
                        set_locked_level_image(R.id.level_1);
                    }
                    case 1:{
                        set_locked_level_image(R.id.level_2);
                    }
                    case 2:{
                        set_locked_level_image(R.id.level_3);
                    }
                    case 3:{
                        set_locked_level_image(R.id.level_4);
                    }
                    case 4:{
                        set_locked_level_image(R.id.level_5);
                    }
                    case 5:{
                        set_locked_level_image(R.id.level_6);
                    }
                    case 6:{
                        set_locked_level_image(R.id.level_7);
                    }
                    case 7:{
                        set_locked_level_image(R.id.level_8);
                    }
                    case 8:{
                        set_locked_level_image(R.id.level_9);
                    }
                    case 9:{
                        set_locked_level_image(R.id.level_10);
                    }
                    case 10:{
                        set_locked_level_image(R.id.level_11);
                    }
                    case 11:{
                        set_locked_level_image(R.id.level_12);
                    }
                    case 12:{
                        set_locked_level_image(R.id.level_13);
                    }
                    case 13:{
                        set_locked_level_image(R.id.level_14);
                    }
                    case 14:{
                        set_locked_level_image(R.id.level_15);
                    }
                    case 15:{
                        set_locked_level_image(R.id.level_16);
                    }
                    case 16:{
                        set_locked_level_image(R.id.level_17);
                    }
                    case 17:{
                        set_locked_level_image(R.id.level_18);
                    }
                    case 18:{
                        set_locked_level_image(R.id.level_19);
                    }
                    case 19:{
                        set_locked_level_image(R.id.level_20);
                    }

                }
            }
        }
    }

    private void set_locked_level_image(int id){
        ImageView imageView = findViewById(id);
        imageView.setImageResource(R.drawable.level_locked);
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        ImageView imageView = findViewById(id);
        //if (imageView.getDrawable().getConstantState().equals(getDrawable(R.drawable.level_locked).getConstantState()))
    }
    private void check_levels_file(){
        if(!Gdx.files.internal(File.filename).exists()){
            LevelData levelData = new LevelData();
            Json.WriteToFile(File.filename,levelData);
        }
    }
}