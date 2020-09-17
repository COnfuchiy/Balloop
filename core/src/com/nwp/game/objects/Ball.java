package com.nwp.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Ball {
    public Vector2 position = new Vector2(); // вектор для обозначения позиции
    public Vector2 velocity = new Vector2(); // вектор для обозначения скорости
    public Sprite ballSprite;// спрайт для отображения мяча
    public long path = 0;

    public void render(SpriteBatch batch){
        ballSprite.draw(batch);
    }
    public void update() {
        position.add(velocity);
        if (position.y==120)
            velocity.set(-1,-2);
        if (position.y==320)
            velocity.set(2,-2);
        if (position.y==520)
            velocity.set(-1,-2);
        if (position.y==820)
            velocity.set(2,-2);
        if (position.y==1120)
            velocity.set(-1,-2);
        ballSprite.setPosition(position.x, position.y);
    }
}