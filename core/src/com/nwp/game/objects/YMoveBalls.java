package com.nwp.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface YMoveBalls {
    void updates(SpriteBatch batch);
    void delete_ball();
    boolean check_ball_dist();
    boolean not_last_index(int index);
}
