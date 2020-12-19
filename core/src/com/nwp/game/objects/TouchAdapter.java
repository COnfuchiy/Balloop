package com.nwp.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.Game;

public class TouchAdapter implements YMoveBalls{
    private Array<Texture> textures;
    private Array<Integer> y_levels;

    // pseudo random
    private Array<Integer> pseudo_random_indexes;
    private int pseudo_random_iterator = 0;
    private int pseudo_random_size_selection = 5;

    private CurrentBallViewer ballViewer;

    private int active_level_index;
    private int default_touch_area;
    private int forbidden_touch_area;
    private int default_touch_delay;
    private int touch_delay = 0;
    private float down_speed;
    private Ball shoot_ball;

    public TouchAdapter(Array<Texture> textures,
                        int default_touch_area,
                        int forbidden_touch_area,
                        int default_touch_delay,
                        Array<Integer> all_y_levels,
                        float down_speed) {
        this.textures = textures;
        y_levels = all_y_levels;
        this.default_touch_area = default_touch_area;
        this.forbidden_touch_area = forbidden_touch_area;
        this.default_touch_delay = default_touch_delay;
        this.down_speed = down_speed;
        this.pseudo_random_indexes = new Array<>();
        set_pseudo_random_textures();
    }

    public void setCurrentBallViewer(CurrentBallViewer ballViewer) {
        this.ballViewer = ballViewer;
    }

    // shoot ball methods
    public Ball get_shoot_ball() {
        return shoot_ball;
    }

    public boolean is_ball_shooting() {
        return shoot_ball != null;
    }

    public void spawn_shoot_ball(Vector2 touch_position) {
        Texture ball_texture = get_current_ball_texture();
        shoot_ball = new Ball(ball_texture,new Vector2(touch_position));
        shoot_ball.velocity = new Vector2(0,down_speed);
    }

    @Override
    public void delete_ball(){
        shoot_ball = null;
    }

    public int get_active_level(){
        return y_levels.get(active_level_index);
    }

    public int get_forbidden_touch_area(){
        return forbidden_touch_area;
    }

    @Override
    public boolean check_ball_dist(){
        return shoot_ball.position.y - shoot_ball.ballSprite.getWidth() <= y_levels.get(active_level_index)+forbidden_touch_area;
    }

    @Override
    public boolean not_last_index(int index) {
        return index!=y_levels.size-1;
    }

    // delay methods
    private void set_touch_delay(int delay) {
        touch_delay = delay;
    }

    private void set_touch_delay() {
        touch_delay = default_touch_delay;
    }

    private boolean check_delay() {
        if (touch_delay != 0) {
            touch_delay--;
            return touch_delay == 0;
        }
        return true;
    }

    @Override
    public void updates(SpriteBatch batch) {
        //maybe something there or not
        if (is_ball_shooting()){
            shoot_ball.update();
            shoot_ball.render(batch);
        }
        if (touch_delay!=0)
            touch_delay--;
    }

    public float get_down_speed(){
        return down_speed;
    }

    public Texture get_current_ball_texture(){
        return textures.get(pseudo_random_indexes.get(pseudo_random_iterator));
    }

    // random texture method
    private Texture get_random_ball_texture() {
        pseudo_random_iterator++;
        if (pseudo_random_iterator == pseudo_random_indexes.size - 1) {
            pseudo_random_iterator = 0;
            set_pseudo_random_textures();
        }
        return textures.get(pseudo_random_indexes.get(pseudo_random_iterator));
    }

    private void set_pseudo_random_textures() {
        pseudo_random_indexes = new Array<>();
        for (int i = 0; i < pseudo_random_size_selection; i++) {
            pseudo_random_indexes.add(Game.get_rand_in_range(0, textures.size - 1));
        }
        int mid_selection_index = pseudo_random_size_selection / 2;
        if (pseudo_random_indexes.get(mid_selection_index).equals(pseudo_random_indexes.get(mid_selection_index - 1))
                || pseudo_random_indexes.get(mid_selection_index).equals(pseudo_random_indexes.get(mid_selection_index + 1))) {
            int num_matches = 1;
            if (pseudo_random_indexes.get(mid_selection_index).equals(pseudo_random_indexes.get(mid_selection_index - 1))) {
                for (int i = mid_selection_index; i > 0; i--)
                    if (pseudo_random_indexes.get(i).equals(pseudo_random_indexes.get(i - 1)))
                        num_matches++;

            } else {
                for (int i = mid_selection_index; i < pseudo_random_indexes.size - 1; i++) {
                    if (pseudo_random_indexes.get(i).equals(pseudo_random_indexes.get(i + 1)))
                        num_matches++;
                }
            }
            if (num_matches == mid_selection_index)
                if (pseudo_random_indexes.get(mid_selection_index) == 0 || pseudo_random_indexes.get(mid_selection_index) != textures.size - 1)
                    pseudo_random_indexes.set(mid_selection_index, pseudo_random_indexes.get(mid_selection_index) + 1);
                else
                    pseudo_random_indexes.set(mid_selection_index, pseudo_random_indexes.get(mid_selection_index) - 1);
        }
    }

    public void handle_touch(Vector2 touch_position) {
        if (check_delay()) {
            boolean possibility_shoot = false;
            for (int level : y_levels)
                if (level - forbidden_touch_area <= touch_position.y && level + default_touch_area >= touch_position.y) {
                    possibility_shoot = true;
                    active_level_index = y_levels.indexOf(level,false);
                    break;
                }
            if (possibility_shoot){
                spawn_shoot_ball(touch_position);
                ballViewer.setBallSprite(get_random_ball_texture());
            }
            set_touch_delay();
        }
    }
}
