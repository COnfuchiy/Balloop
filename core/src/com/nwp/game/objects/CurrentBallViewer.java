package com.nwp.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CurrentBallViewer {
    public Vector2 position = new Vector2(); // вектор для обозначения позиции
    public Sprite ballSprite;// спрайт для отображения мяча

    public CurrentBallViewer(Texture texture, float width, int y_level){
        ballSprite = new Sprite(texture);
        ballSprite.setSize(ballSprite.getWidth(), ballSprite.getHeight());
        this.position.set(new Vector2(width/2-(ballSprite.getWidth()/2),y_level+400));
        ballSprite.setPosition(position.x, position.y);
    }
    public void render(SpriteBatch batch) {
        ballSprite.draw(batch);
    }

    public void setBallSprite(Texture texture){
        ballSprite = new Sprite(texture);
        ballSprite.setSize(ballSprite.getWidth(), ballSprite.getHeight());
        ballSprite.setPosition(position.x, position.y);
    }
}
