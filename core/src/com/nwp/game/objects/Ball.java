package com.nwp.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.nwp.game.source.Path;


public class Ball implements Cloneable{
    public Vector2 position = new Vector2(); // вектор для обозначения позиции
    public Vector2 velocity = new Vector2(); // вектор для обозначения скорости
    public Sprite ballSprite;// спрайт для отображения мяча
    private Path path;
    private int current_iter = 0;
    private int velocity_pos = 0;
    private int direction = 1;

    public Ball(Path Path) {
        path = Path;
    }

    public void render(SpriteBatch batch) {
        ballSprite.draw(batch);
    }

    public void set_velocity() {
        if (direction == 1) {
            Vector2 temp_velocity = path.get_normal_velocity(velocity_pos, current_iter);
            if (!temp_velocity.isOnLine(velocity)) {
                velocity_pos = 0;
                if (path.check_index(current_iter,direction))
                    current_iter++;
                else
                    current_iter = 0;
            } else
                velocity_pos += 1;
            velocity = temp_velocity;
        } else {
            Vector2 temp_velocity = path.get_back_velocity(velocity_pos, current_iter);
            if (!temp_velocity.isOnLine(velocity)) {
                if (path.check_index(current_iter,direction))
                    current_iter--;
                else
                    current_iter = path.last_index();
                velocity_pos = path.get_current_total_iterations(current_iter);
            } else
                velocity_pos -= path.get_current_back_acceleration();
            velocity = temp_velocity.rotate(180).scl(path.get_current_back_acceleration());
        }
    }

    public void next_iteration(){
        current_iter++;
    }

    public void next_velocity_position(){
        velocity_pos++;
    }

    public int get_current_iter(){
        return current_iter;
    }

    public int get_velocity_position(){
        return velocity_pos;
    }

    public void set_velocity_pos(int velocity_pos) {
        this.velocity_pos = velocity_pos;
    }

    public void set_current_iter(int current_iter){
        this.current_iter = current_iter;
    }

    public void collapse_move() {
        direction = -1;
    }

    public void normal_move() {
        direction = 1;
    }

    public void update() {
        if (velocity_pos < 0) {
            int num_step_after = path.get_current_total_iterations(velocity_pos) - velocity_pos;
            while (velocity_pos != 0) {
                position.add(velocity);
                velocity_pos++;
            }
            set_velocity();
            while (num_step_after != 0) {
                position.add(velocity);
                velocity_pos--;
                num_step_after--;
            }
        }
        else
            position.add(velocity);
        ballSprite.setPosition(position.x, position.y);
    }
}