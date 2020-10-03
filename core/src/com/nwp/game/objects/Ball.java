package com.nwp.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Ball {
    public Vector2 position = new Vector2(); // вектор для обозначения позиции
    public Vector2 velocity = new Vector2(); // вектор для обозначения скорости
    public Sprite ballSprite;// спрайт для отображения мяча
    private int direction = 1;

    public Ball(Texture texture, Vector2 velocity, int y_level) {
        ballSprite = new Sprite(texture);
        ballSprite.setSize(ballSprite.getWidth(), ballSprite.getHeight());
        position.set(-ballSprite.getWidth(),(float)y_level);
        this.velocity.set(velocity);
    }

    public void render(SpriteBatch batch) {
        ballSprite.draw(batch);
    }

    public void collapse_move() {
        direction = -1;
    }

    public void normal_move() {
        direction = 1;
    }

    public void update() {
        position.add(velocity);
        ballSprite.setPosition(position.x, position.y);
    }
}