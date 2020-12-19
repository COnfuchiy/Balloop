package com.nwp.game.levels;

import com.badlogic.gdx.utils.Array;

public class LevelsJson {
    public Array<Boolean> is_completed;

    public LevelsJson(){
    }
    public void set_json(Array<Boolean> is_completed){
        this.is_completed = is_completed;
    }
}
