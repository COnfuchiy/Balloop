package com.nwp.game.levels;

import com.badlogic.gdx.utils.Array;


public class LevelData {
    private Array<Level> levels;
    public enum LevelField{
        name,
        is_completed,
        num_gutters,
        gutters_positions,
        gutters_num_balls,
        gutters_velocities,
    };
    public LevelData() {
        levels = new Array<>();
    }
    public void create_levels(){
        levels.add(new Level(
                "1",
                false,
                1,
                new Array<Integer>(new Integer[]{1}),
                new Array<Integer>(new Integer[]{45}),
                new Array<Float>(new Float[]{1.0f}),
                new Array<String>(new String[]{"ball_1","ball_2","ball_3","ball_4"})));
    }

    public Object get_level_field(int num_level, LevelField field){
        switch (field){
            case name:{
                return levels.get(num_level).getName();
            }
            case is_completed:{
                return levels.get(num_level).is_completed();
            }
            case num_gutters:{
                return levels.get(num_level).getNum_gutters();
            }
            case gutters_positions:{
                return levels.get(num_level).getGutters_positions();
            }
            case gutters_num_balls:{
                return levels.get(num_level).getGutters_num_balls();
            }
            case gutters_velocities:{
                return levels.get(num_level).getGutters_velocities();
            }
        }
        return false;
    }

    public void set_level_completed(int num_level){
        levels.get(num_level).setIs_completed();
    }

    public Array<Boolean> get_levels_completed_state(){
        Array<Boolean> state_array = new Array<>();
        for (Level level: levels){
            state_array.add(level.is_completed());
        }
        return state_array;
    }
}
