package com.nwp.game.source;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.objects.Ball;
import com.nwp.game.objects.TouchAdapter;
import com.nwp.game.objects.YMoveBalls;

class AdditionalBall implements YMoveBalls {
    private Ball add_ball;
    private Array<Integer> y_levels;
    private int hit_potential_level_index;

    public AdditionalBall(Array<Integer> all_y_levels){
        y_levels = all_y_levels;
    }

    public void spawn_additional_ball(int potential_level_index, Texture texture, Vector2 position, float down_speed){
        
    }

    public boolean is_additional_ball(){
        return add_ball!=null;
    }

    public Ball additional_ball(){
        return add_ball;
    }

    @Override
    public void updates(SpriteBatch batch) {
        if (is_additional_ball()){
            add_ball.update();
            add_ball.render(batch);
        }
    }

    @Override
    public void delete_ball() {
        add_ball = null;
    }

    @Override
    public boolean check_ball_dist() {
        return add_ball.position.y - add_ball.ballSprite.getWidth() <= y_levels.get(active_level_index)+forbidden_touch_area;
    }
}

public class BallsActions {
    private Array<Gutter> gutters;
    private float device_width;
    private TouchAdapter adapter;

    public BallsActions(Array<Integer> y_levels,
                        Array<Integer> levels_velocities,
                        Array<Texture> united_textures,
                        TouchAdapter adapter,
                        float width){
        this.adapter = adapter;
        device_width = width;
        gutters = new Array<>();
        for (int i=0;i<y_levels.size;i++)
            gutters.add(
                    new Gutter(y_levels.get(i),levels_velocities.get(i),device_width,united_textures)
            );
    }

    public void balls_update(SpriteBatch batch){
        for (Gutter level : gutters){
            if (adapter.is_ball_shooting() && adapter.get_active_level()==level.get_y_level() && adapter.check_ball_dist())
                level.render_gutter(batch, adapter.get_shoot_ball(),true);
            else
                level.render_gutter(batch, adapter.get_shoot_ball(),false);
        }
    }



}
