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
        create_levels();
    }
    public void create_levels(){
        levels.addAll(
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1000}),
                        new Array<Integer>(new Integer[]{10}),
                        new Array<Float>(new Float[]{2.0f}),
                        new Array<String>(new String[]{"ball_1.png", "ball.png", "ball_2.png"})
                ),
                new Level(
                        "2",
                        false,
                        2,
                        new Array<Integer>(new Integer[]{300,1000}),
                        new Array<Integer>(new Integer[]{15, 20}),
                        new Array<Float>(new Float[]{2.5f, 3.0f}),
                        new Array<String>(new String[]{"ball_1.png", "ball_2.png","ball_3.png","ball_4.png", "ball_5.png"})
                ),
                new Level(
                        "3",
                        false,
                        3,
                        new Array<Integer>(new Integer[]{300,1000, 1700}),
                        new Array<Integer>(new Integer[]{15, 30, 30}),
                        new Array<Float>(new Float[]{3.0f, 2.5f, 3.0f}),
                        new Array<String>(new String[]{"ball_1.png","ball_6.png","ball.png", "ball_2.png","ball_3.png","ball_4.png", "ball_5.png"})
                ),
                new Level(
                        "4",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1500}),
                        new Array<Integer>(new Integer[]{35}),
                        new Array<Float>(new Float[]{2.6f}),
                        new Array<String>(new String[]{"ball_1.png", "ball.png", "ball_2.png","ball_3.png","ball_4.png"})
                ),
                new Level(
                        "5",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1000}),
                        new Array<Integer>(new Integer[]{50}),
                        new Array<Float>(new Float[]{2.8f}),
                        new Array<String>(new String[]{"ball_6.png", "ball.png", "ball_5.png","ball_3.png","ball_4.png"})
                ),
                new Level(
                        "6",
                        false,
                        2,
                        new Array<Integer>(new Integer[]{1300, 300}),
                        new Array<Integer>(new Integer[]{20, 30}),
                        new Array<Float>(new Float[]{2.4f,2.7f}),
                        new Array<String>(new String[]{"ball_6.png", "ball.png", "ball_5.png","ball_3.png","ball_4.png"})
                ),
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                ),
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                ),
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                ),
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                ),
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                ),
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                ),
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                ),
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                )
        );
        levels.add(
                new Level(
                        "1",
                        false,
                        1,
                        new Array<Integer>(new Integer[]{1}),
                        new Array<Integer>(new Integer[]{45}),
                        new Array<Float>(new Float[]{1.0f}),
                        new Array<String>(new String[]{"ball_1", "ball_2", "ball_3", "ball_4"})
                )
        );
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

    public Level getLevel(int index) { return levels.get(index); }

    public int getIndex(Level level){
        return levels.indexOf(level,false);
    }

    public void set_level_completed(int num_level){
        levels.get(num_level).setIs_completed(true);
    }

    public Array<Boolean> get_levels_completed_state(){
        Array<Boolean> state_array = new Array<>();
        for (Level level: levels){
            state_array.add(level.is_completed());
        }
        return state_array;
    }
    public void set_levels_completed_state(Array<Boolean> state_array){
        for (int i = 0;i<state_array.size;i++){
            levels.get(i).setIs_completed(state_array.get(i));
        }
    }
}
