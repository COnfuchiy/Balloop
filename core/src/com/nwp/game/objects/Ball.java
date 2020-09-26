package com.nwp.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.nwp.game.source.Path;


public class Ball {
    public Vector2 position = new Vector2(); // вектор для обозначения позиции
    public Vector2 velocity = new Vector2(); // вектор для обозначения скорости
    public Sprite ballSprite;// спрайт для отображения мяча
    private Path path;
    private int current_iter = 0;
    private int velocity_pos = 0;

    public void render(SpriteBatch batch) {
        ballSprite.draw(batch);
    }

    public void set_velocity() {
        Vector2 temp_velocity = path.get_velocity(velocity_pos, current_iter);
        if (!temp_velocity.isOnLine(velocity)) {
            velocity_pos++;
            current_iter = 0;
        }
        velocity = temp_velocity;
        current_iter += 1;
        /*velocity.set(0.9f, -1.9f);
//        if (position.x>50 && velocity.x!=0.8f)
//            velocity.set(0.8f, -1.9f);
//        if (position.x>150 && velocity.x!=0.7f)
//            velocity.set(0.7f, -1.9f);
//        if (position.x>250 && velocity.x!=0.6f)
//            velocity.set(0.6f, -1.9f);
//        if (position.x>350 && velocity.x!=0.5f)
//            velocity.set(0.5f, -1.9f);

         */
    }

    public Ball(Path Path) {
        path = Path;
    }

    public void update() {
        position.add(velocity);
        ballSprite.setPosition(position.x, position.y);
    }
}