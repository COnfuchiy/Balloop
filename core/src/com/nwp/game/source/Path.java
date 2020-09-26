package com.nwp.game.source;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

class Velocity {
    public Vector2 velocity;
    public int iterations_count;

    public Velocity(Vector2 Velocity, int Iterations_count) {
        velocity = Velocity;
        iterations_count = Iterations_count;
    }
}

public class Path {
    private Array<Velocity> points;

    public Path(Array<Vector2> velocity_array, Array<Integer> velocity_iter) {
        points = new Array<Velocity>();
        for (int i = 0; i < velocity_array.size; i++) {
            points.add(new Velocity(velocity_array.get(i), velocity_iter.get(i)));
        }
    }

    public Vector2 get_velocity(int velocity_pos, int current_iter) {
        Velocity current_velocity = points.get(velocity_pos);
        if (current_iter == current_velocity.iterations_count)
            return points.get(velocity_pos + 1).velocity;
        else return current_velocity.velocity;
    }
}