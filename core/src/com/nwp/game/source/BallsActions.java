package com.nwp.game.source;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.Game;
import com.nwp.game.objects.Ball;
import com.nwp.game.objects.CurrentBallViewer;
import com.nwp.game.objects.TouchAdapter;
import com.nwp.game.objects.YMoveBalls;

class AdditionalBall implements YMoveBalls {
    private Ball add_ball;
    private Array<Integer> y_levels;
    private int potential_level_index;
    private float down_speed;
    private int forbidden_touch_area;

    public AdditionalBall(Array<Integer> all_y_levels,
                          float down_speed,
                          int forbidden_touch_area){
        y_levels = all_y_levels;
        this.forbidden_touch_area = forbidden_touch_area;
        this.down_speed = down_speed;
    }

    public void spawn_additional_ball(int potential_level,
                                      Texture texture,
                                      Vector2 position){
        potential_level_index = y_levels.indexOf(potential_level,false)-1;
        add_ball = new Ball(texture,new Vector2(position));
        add_ball.velocity.set(new Vector2(0,down_speed));
    }

    public boolean is_additional_ball(){
        return add_ball!=null;
    }

    public Ball get_additional_ball(){
        return add_ball;
    }

    public int get_potential_level_index(){
        return y_levels.get(potential_level_index);
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
        return add_ball.position.y - add_ball.ballSprite.getWidth() <= y_levels.get(potential_level_index)+forbidden_touch_area;
    }

    @Override
    public boolean not_last_index(int level) {
        return y_levels.indexOf(level,false)!=0;
    }
}

public class BallsActions {
    private Array<Gutter> gutters;
    private TouchAdapter adapter;
    private Array<Boolean> all_gutters_complete;
    private AdditionalBall additional_ball;
    private CurrentBallViewer ball_viewer;

    public BallsActions(Array<Integer> y_levels,
                        Array<Float> levels_velocities,
                        Array<Texture> united_textures,
                        TouchAdapter adapter,
                        Array<Integer> num_level_balls,
                        float width,
                        Stage stage){
        this.adapter = adapter;
        additional_ball = new AdditionalBall(y_levels,adapter.get_down_speed(),adapter.get_forbidden_touch_area());
        gutters = new Array<>();
        all_gutters_complete = new Array<>();
        for (int i=0;i<y_levels.size;i++)
            gutters.add(
                    new Gutter(y_levels.get(i),levels_velocities.get(i), width,num_level_balls.get(i),united_textures,stage)
            );
        ball_viewer = new CurrentBallViewer(adapter.get_current_ball_texture(),width,y_levels.get(y_levels.size-1));
    }

    public boolean balls_update(SpriteBatch batch){
        all_gutters_complete = new Array<>();
        additional_ball.updates(batch);
        for (Gutter level : gutters){
            if (adapter.is_ball_shooting() && adapter.get_active_level()==level.get_y_level() && adapter.check_ball_dist())
                level.render_gutter(batch, adapter,additional_ball,true);
            else{
                if (additional_ball.is_additional_ball() && additional_ball.get_potential_level_index()==level.get_y_level() && additional_ball.check_ball_dist())
                    level.render_gutter(batch, adapter, additional_ball,true);
                else
                    level.render_gutter(batch, adapter, additional_ball,false);
            }
            all_gutters_complete.add(level.check_gutter_complete());
        }
        ball_viewer.render(batch);
        boolean is_level_complete = true;
        for(int i  = 0; i<all_gutters_complete.size;i++){
            if(!all_gutters_complete.get(i)){
                is_level_complete = false;
            }
        }
      if (is_level_complete) {
          return true;
      }
      else {
          return false;
      }
    }
}
