package com.nwp.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Ball {
    public Vector2 position = new Vector2(); // вектор для обозначения позиции
    public Vector2 velocity = new Vector2(); // вектор для обозначения скорости
    public Sprite ballSprite;// спрайт для отображения мяча

    public void render(SpriteBatch batch){
        ballSprite.draw(batch);
    }
    public void set_velocity(){
        velocity.set(0.9f, -1.9f);
//        if (position.x>50 && velocity.x!=0.8f)
//            velocity.set(0.8f, -1.9f);
//        if (position.x>150 && velocity.x!=0.7f)
//            velocity.set(0.7f, -1.9f);
//        if (position.x>250 && velocity.x!=0.6f)
//            velocity.set(0.6f, -1.9f);
//        if (position.x>350 && velocity.x!=0.5f)
//            velocity.set(0.5f, -1.9f);
    }
    public void update() {
        position.add(velocity);
        ballSprite.setPosition(position.x, position.y);
    }
}