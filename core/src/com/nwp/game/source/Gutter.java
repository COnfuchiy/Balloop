package com.nwp.game.source;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.Game;
import com.nwp.game.objects.Ball;
import com.nwp.game.objects.GutterBar;
import com.nwp.game.objects.TouchAdapter;

class GutterLevelBarControl{
    private GutterBar level_bar;
    private int num_level_balls;
    private float one_ball_value;

    public GutterLevelBarControl(float device_width,
                                 int height,
                                 int y_position,
                                 int num_level_balls,
                                 Stage stage){
        this.num_level_balls = num_level_balls;
        one_ball_value = 100.0f/num_level_balls;
        level_bar = new GutterBar((int)device_width,height,one_ball_value);
        level_bar.setPosition(0, y_position-20);
        stage.addActor(level_bar);
    }

    private float calc_balls_value(int num_balls){
        return one_ball_value*num_balls;
    }

    public void set_bar_value(int num_balls){
        if (level_bar.getValue()!=100.0f){
            float added_value = calc_balls_value(num_balls);
            level_bar.setValue(Math.min(level_bar.getValue() + added_value, 100.0f));
        }
    }
}

class GutterState {
    static final int MOVE = 1;
    static final int STOP = 0;
    static final int INSERT_DELAY = 2;
    static final int ANIMATE_DELAY = 3;
    private int general_state = 1;
    private int animate_counter = 0;
    private int delay_counter = 0;

    public void balls_move() {
        general_state = MOVE;
    }

    public void balls_stop() {
        general_state = STOP;
    }

    public void set_insert_delay(int num_frames) {
        delay_counter = num_frames;
        general_state = INSERT_DELAY;
    }

    public void set_animate_counter(int num_frames) {
        animate_counter = num_frames;
        general_state = ANIMATE_DELAY;
    }

    public boolean is_balls_move() {
        return general_state == MOVE;
    }

    public boolean is_balls_stop() {
        return general_state == STOP;
    }

    public boolean is_insert_delay() {
        return general_state == INSERT_DELAY;
    }

    public boolean is_animate_delay() {
        return general_state == ANIMATE_DELAY;
    }

    public boolean check_animate_counter() {
        if (animate_counter != 0) {
            animate_counter--;
            return animate_counter == 0;
        }
        return true;
    }

    public boolean check_delay_counter() {
        if (delay_counter != 0) {
            delay_counter--;
            return delay_counter == 0;
        }
        return true;
    }
}

public class Gutter {
    //main gutter vars
    private int y_level;
    private Vector2 velocity;
    private Array<Ball> balls;
    private int direction;
    private Array<Texture> textures;
    private float length_btw_balls = 10;
    private float device_width;
    private Array<Integer> destroyed_balls_indexes;
    private Vector2 collapse_position;
    private boolean shoot_ball_collision = false;
    private int collapsed_texture_index;
    private int ball_collision_pos;
    private int back_velocity_mul = 2;

    //level bar vars

    private Stage stage;
    private GutterLevelBarControl level_bar_control;
    private int num_level_balls = 60;
    private int bar_height = 20;

    //random generation balls vars
    private int last_double_index = -1;
    private int texture_index = 0;
    private Array<Texture> three_ball_ahead;

    //current state class elem
    private GutterState state;

    public Gutter(int y_level,
                  float x_velocity,
                  float devise_width,
                  Array<Texture> possible_level_textures,
                  Stage stage) {
        this.y_level = y_level;
        this.direction = x_velocity > 0 ? 1 : -1;
        this.velocity = new Vector2(x_velocity, 0);
        this.device_width = devise_width;
        this.textures = possible_level_textures;
        this.set_three_next_texture();
        this.balls = new Array<>();
        this.destroyed_balls_indexes = new Array<>();
        this.stage = stage;
        level_bar_control = new GutterLevelBarControl(device_width,bar_height,y_level,num_level_balls,stage);
        collapse_position = new Vector2();
        state = new GutterState();
        spawn_ball();
    }

    public int get_y_level(){
        return y_level;
    }

    public void spawn_ball() {
        Texture tmp_texture = three_ball_ahead.get(texture_index);
        texture_index++;
        if (velocity.x > 0)
            balls.add(new Ball(tmp_texture, velocity, y_level));
        else
            balls.add(new Ball(tmp_texture, velocity, y_level, device_width));
        if (texture_index == 3) {
            this.set_three_next_texture();
            texture_index = 0;
        }
    }

    public void render_gutter(SpriteBatch batch, TouchAdapter adapter,AdditionalBall additionalBall, boolean is_ball_near) {
        Ball shoot_ball = adapter.is_ball_shooting() ? adapter.get_shoot_ball() : additionalBall.get_additional_ball();
        for (Ball ball : balls) {
            if (shoot_ball != null && is_ball_near) {
                if (shoot_ball.position.x != 0 && state.is_balls_move() && ball.ballSprite.getBoundingRectangle().overlaps(shoot_ball.ballSprite.getBoundingRectangle())) {
                    handle_collision(shoot_ball, ball);
                    shoot_ball_collision = true;
                    if (adapter.is_ball_shooting())
                        adapter.delete_ball();
                    else
                        additionalBall.delete_ball();
                }
            }

            if (state.is_balls_move()) {
                if (direction == 1 && ball.position.x >= device_width + ball.ballSprite.getWidth() ||
                        direction == -1 && ball.position.x + ball.ballSprite.getWidth() <= 0) {
                    balls.removeValue(ball, false);
                }
                if (direction == 1 && ball.position.x >= length_btw_balls && ball.position.x < length_btw_balls + Math.abs(velocity.x) ||
                        direction == -1 && ball.position.x + length_btw_balls >= device_width - Math.abs(velocity.x) &&
                                ball.position.x + length_btw_balls < device_width) {
                    spawn_ball();
                }
                ball.update();
            } else {
                if (state.is_insert_delay() && state.check_delay_counter()) {
                    if (check_collapse(ball_collision_pos)) {
                        destroy_balls();
                        level_bar_control.set_bar_value(destroyed_balls_indexes.get(2)- destroyed_balls_indexes.get(0));
                        state.set_animate_counter(120);
                    } else {
                        state.balls_move();
                    }
                }
                if (state.is_animate_delay() && state.check_animate_counter())
                    if (collapse_balls()){
                        if (shoot_ball_collision && additionalBall.not_last_index(y_level))
                            additionalBall.spawn_additional_ball(y_level,textures.get(collapsed_texture_index),collapse_position);
                        shoot_ball_collision = false;
                    }
            }

            ball.ballSprite.draw(batch);
        }
    }

    private void set_three_next_texture() {
        int index_double_texture = Game.get_rand_in_range(0, textures.size - 1);
        int index_another_texture = Game.get_rand_in_range(0, textures.size - 1);
        if (this.last_double_index != -1) {
            if (index_double_texture == last_double_index) {
                if (index_double_texture != 0) {
                    if (index_double_texture == textures.size - 1)
                        index_double_texture--;
                    else
                        index_double_texture++;
                } else
                    index_double_texture++;
            }
        }
        if (index_another_texture == index_double_texture) {
            if (index_another_texture != 0) {
                if (index_another_texture == textures.size - 1)
                    index_another_texture--;
                else
                    index_another_texture++;
            } else
                index_another_texture++;
        }
        three_ball_ahead = new Array<Texture>();
        if (Game.get_rand_in_range(0, 1) == 0)
            three_ball_ahead.add(textures.get(index_another_texture), textures.get(index_double_texture), textures.get(index_double_texture));
        else
            three_ball_ahead.add(textures.get(index_double_texture), textures.get(index_double_texture), textures.get(index_another_texture));
        this.last_double_index = index_double_texture;
    }

    private void handle_collision(Ball shoot_ball, Ball ball) {
        state.balls_stop();
        Vector2 pos_insert;
        if (direction == 1 && shoot_ball.position.x <= ball.position.x ||
                direction == -1 && shoot_ball.position.x >= ball.position.x) {
            ball_collision_pos = balls.indexOf(ball, false) + 1;
            pos_insert = new Vector2(balls.get(ball_collision_pos).position);
        } else {
            pos_insert = new Vector2(ball.position);
            ball_collision_pos = balls.indexOf(ball, false);
        }

        Ball new_ball = new Ball(shoot_ball.ballSprite.getTexture(), velocity, y_level);
        new_ball.position.set(pos_insert.x, pos_insert.y);
        balls.insert(ball_collision_pos, new_ball);
        for (int i = ball_collision_pos; i >= 0; i--) {
            Ball current_ball = balls.get(i);
            if (i != 0) {
                Ball next_ball = balls.get(i - 1);
                while (current_ball.position.x != next_ball.position.x)
                    current_ball.position.add(current_ball.velocity);
            } else {
                Vector2 tmp_position = new Vector2(current_ball.position);
                while (current_ball.position.x != tmp_position.x + direction*(current_ball.ballSprite.getWidth() + length_btw_balls))
                    current_ball.position.add(current_ball.velocity);
            }
            current_ball.ballSprite.setPosition(current_ball.position.x, current_ball.position.y);
        }
        state.set_insert_delay(120);
    }

    private void destroy_balls() {
        calculate_collapse_borders();
        set_collapse_position();
        collapsed_texture_index = textures.indexOf(balls.get(destroyed_balls_indexes.get(0)).ballSprite.getTexture(),false);
        balls.removeRange(destroyed_balls_indexes.get(0), destroyed_balls_indexes.get(2));
        //animate?
    }

    private boolean collapse_balls() {
        int last_collapsed_index = destroyed_balls_indexes.get(0) - 1;
        if (last_collapsed_index >= 0) {
            Ball last_collapsed_ball = balls.get(last_collapsed_index);
            if (direction == 1 && last_collapsed_ball.position.x + (float) back_velocity_mul * velocity.x < collapse_position.x ||
                    direction == -1 && last_collapsed_ball.position.x - (float) back_velocity_mul * velocity.x > collapse_position.x
            ) {
                float last_try_velocity_x;
                if (direction == 1){
                    last_try_velocity_x= last_collapsed_ball.position.x + (float) back_velocity_mul * velocity.x;
                    last_try_velocity_x += collapse_position.x;
                }
                else
                {
                    last_try_velocity_x = last_collapsed_ball.position.x - (float) back_velocity_mul * velocity.x;
                    last_try_velocity_x -= collapse_position.x;
                }
                for (int i = last_collapsed_index; i >= 0; i--) {
                    balls.get(i).position.add(last_try_velocity_x, 0);
                    //balls.get(i).ballSprite.setPosition(balls.get(i).position.x, balls.get(i).position.y);
                }
            } else {
                for (int i = last_collapsed_index; i >= 0; i--) {
                    balls.get(i).position.add(new Vector2(velocity).rotate(180).scl(2));
                    //balls.get(i).ballSprite.setPosition(balls.get(i).position.x, balls.get(i).position.y);
                }

            }
            if (balls.get(last_collapsed_index).position.x == collapse_position.x) {
                destroyed_balls_indexes.removeRange(0, destroyed_balls_indexes.size - 1);
                for (int i = last_collapsed_index; i >= 0; i--) {
                    balls.get(i).ballSprite.setPosition(balls.get(i).position.x, balls.get(i).position.y);
                }
                ball_collision_pos = last_collapsed_index;
                state.set_insert_delay(120);
                return true;
            }

        }
        else{
            destroyed_balls_indexes.removeRange(0, destroyed_balls_indexes.size - 1);
            state.balls_move();
            return true;
        }
        return false;
    }
    private void set_collapse_position() {
        Ball right_ball = balls.get(destroyed_balls_indexes.get(2));
        collapse_position.set(right_ball.position);
    }

    private void calculate_collapse_borders() {
        destroyed_balls_indexes.sort();
        if (destroyed_balls_indexes.get(0) > 0 &&
                check_balls_equals(destroyed_balls_indexes.get(0), destroyed_balls_indexes.get(0) - 1)) {
            int left_edge_index = destroyed_balls_indexes.get(0);
            while (left_edge_index > 0 && check_balls_equals(destroyed_balls_indexes.get(0), left_edge_index - 1))
                left_edge_index--;
            destroyed_balls_indexes.set(0, left_edge_index);
        }
        if (destroyed_balls_indexes.get(2) < balls.size-1 &&
                check_balls_equals(destroyed_balls_indexes.get(2), destroyed_balls_indexes.get(2) + 1)) {
            int right_edge_index = destroyed_balls_indexes.get(2);
            while (right_edge_index < balls.size-1 && check_balls_equals(destroyed_balls_indexes.get(0), right_edge_index + 1))
                right_edge_index++;
            destroyed_balls_indexes.set(2, right_edge_index);
        }
    }

    private boolean check_balls_equals(int index_first, int index_second) {
        Ball first_ball = balls.get(index_first);
        Ball second_ball = balls.get(index_second);
        return first_ball.ballSprite.getTexture().toString().equals(second_ball.ballSprite.getTexture().toString());
    }

    private boolean check_collapse(int start_ball_index) {
        if (balls.size >= 4) {
            if (start_ball_index<=balls.size-2 && check_balls_equals(start_ball_index, start_ball_index + 1)) {
                if (start_ball_index<=balls.size-3 && check_balls_equals(start_ball_index, start_ball_index + 2)) {
                    destroyed_balls_indexes.add(start_ball_index, start_ball_index + 1, start_ball_index + 2);
                    return true;
                } else if (start_ball_index > 0 && check_balls_equals(start_ball_index, start_ball_index - 1)) {
                    destroyed_balls_indexes.add(start_ball_index - 1, start_ball_index, start_ball_index + 1);
                    return true;
                }

            } else if (start_ball_index<=balls.size-1 && start_ball_index > 1 && check_balls_equals(start_ball_index, start_ball_index - 1) &&
                    check_balls_equals(start_ball_index, start_ball_index - 2)) {
                destroyed_balls_indexes.add(start_ball_index - 2, start_ball_index - 1, start_ball_index);
                return true;
            }
        }
        return false;
    }
};
