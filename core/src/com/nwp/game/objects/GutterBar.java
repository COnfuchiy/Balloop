package com.nwp.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class Utils {
    private Utils() {}

    /**
     * Creates an image of determined size filled with determined color.
     *
     * @param width of an image.
     * @param height of an image.
     * @param color of an image fill.
     * @return {@link Drawable} of determined size filled with determined color.
     */
    public static Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }
}

public class GutterBar extends ProgressBar {

    public GutterBar(int width, int height, float step) {
        super(0f, 100f, step, false, new ProgressBarStyle());
        getStyle().background = Utils.getColoredDrawable(width, height, Color.RED);
        getStyle().knob = Utils.getColoredDrawable(0, height, Color.GREEN);
        getStyle().knobBefore = Utils.getColoredDrawable(width, height, Color.GREEN);

        setWidth(width);
        setHeight(height);

        setAnimateDuration(0.0f);
        setValue(0.0f);

        setAnimateDuration(0.25f);
    }
}