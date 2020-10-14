package com.nwp.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface YMoveBalls {
    public void updates(SpriteBatch batch);
    public void delete_ball();
    public boolean check_ball_dist();
}
