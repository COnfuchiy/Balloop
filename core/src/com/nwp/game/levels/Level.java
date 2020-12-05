package com.nwp.game.levels;

import com.badlogic.gdx.utils.Array;

public class Level{
    private String name;
    private boolean is_completed;
    private int num_gutters;
    private Array<Integer> gutters_positions;
    private Array<Integer> gutters_num_balls;
    private Array<Float> gutters_velocities;
    private Array<String> ball_spites;

    public Level(String name,
                 boolean is_completed,
                 int num_gutters,
                 Array<Integer> gutters_positions,
                 Array<Integer> gutters_num_balls,
                 Array<Float> gutters_velocities,
                 Array<String> ball_spites) {
        this.name = name;
        this.is_completed = is_completed;
        this.num_gutters = num_gutters;
        this.gutters_positions = gutters_positions;
        this.gutters_num_balls = gutters_num_balls;
        this.gutters_velocities =gutters_velocities;
        this.ball_spites = ball_spites;
    }

    public String getName() {
        return name;
    }

    public boolean is_completed() {
        return is_completed;
    }

    public int getNum_gutters() {
        return num_gutters;
    }

    public Array<String> getBall_spites() {
        return ball_spites;
    }

    public Array<Integer> getGutters_positions() {
        return gutters_positions;
    }

    public Array<Integer> getGutters_num_balls() {
        return gutters_num_balls;
    }

    public Array<Float> getGutters_velocities() {
        return gutters_velocities;
    }

    public void setIs_completed(){
        is_completed = true;
    }
}
