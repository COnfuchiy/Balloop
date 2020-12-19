package com.nwp.game;

import android.content.Intent;
import android.media.Image;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.levels.Level;
import com.nwp.game.levels.LevelData;
import com.nwp.game.levels.LevelsJson;
import com.nwp.game.source.Json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    public Array<Integer> Id_list = new Array<>(new Integer[]{
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
    });

    private LevelData levelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        levelData = new LevelData();
        if (!check_file(Environment.getExternalStorageDirectory().getPath() + "/levels_data")) {
            LevelsJson levelsJson = new LevelsJson();
            levelsJson.set_json(levelData.get_levels_completed_state());
            String json = Json.SerializeObject(levelsJson);
            write_file(Environment.getExternalStorageDirectory().getPath() + "/levels_data", json);
        }
        String file_data = read_file(Environment.getExternalStorageDirectory().getPath() + "/levels_data");
        LevelsJson levelsJson = Json.DeserializeObject(LevelsJson.class, file_data);
        levelData.set_levels_completed_state(levelsJson.is_completed);
        Array<Boolean> levels_completed = levelsJson.is_completed;
        boolean is_last_level = false;
        for (int i = 0; i < levels_completed.size; i++) {
            if (levels_completed.get(i)) {
                is_last_level = false;
            } else {
                if (!is_last_level) {
                    i++;
                    is_last_level = true;
                }
                set_locked_level_image(Id_list.get(i));
            }
        }
    }

    private void set_locked_level_image(int id) {
        ImageView imageView = findViewById(id);
        imageView.setImageResource(R.drawable.level_locked);
    }

    public int getLevelIndex(int id) {
        for (int i = 0; i < Id_list.size; i++) {
            if (id == Id_list.get(i))
                return i;
        }
        return -1;
    }

    public boolean check_file(String filename) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(filename));
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public void write_file(String filename, String data) {
        try {
            FileOutputStream outputStream = new FileOutputStream (new File(filename));
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(data);
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read_file(String filename) {
        try {
            // открываем поток для чтения
            FileInputStream inputStream = new FileInputStream(new File(filename));
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            StringBuilder output_str = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output_str.append(line + "\n");
            }

            inputStream.close();
            return output_str.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id != -1) {
            Level level = levelData.getLevel(getLevelIndex(v.getId()));
            if (level.is_completed()) {
                Intent intent = new Intent(this, AndroidLauncher.class);
                intent.putExtra("levelIndex", getLevelIndex(v.getId()));
                startActivity(intent);
            } else {
                if (getLevelIndex(v.getId()) != 0) {
                    Level prev_level = levelData.getLevel(getLevelIndex(v.getId()) - 1);
                    if (prev_level.is_completed()) {
                        Intent intent = new Intent(this, AndroidLauncher.class);
                        intent.putExtra("levelIndex", getLevelIndex(v.getId()));
                        startActivity(intent);
                        super.finish();
                    }
                }
                else {
                    Intent intent = new Intent(this, AndroidLauncher.class);
                    intent.putExtra("levelIndex", getLevelIndex(v.getId()));
                    startActivity(intent);
                    super.finish();
                }
            }
        }
    }
}