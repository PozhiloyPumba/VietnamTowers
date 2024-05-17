package com.mygdx.towers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
// import com.badlogic.gdx.math.Rectangle;
// import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.graphics.Color;

public class Pancake {
    public final float scale_;
    public final Color color_;

    private final float scaledWidth;
    private final float scaledHeight;

    private final float width = 100;
    private final float height = 20;
    private final float holeFactor = 0.1f;

    // x and y it is center
    public long x;
    public long y;

    public Pancake(long x_, long y_, Color color, float scale) {
        color_ = color;
        scale_ = scale;
        scaledHeight = height * scale;
        scaledWidth = width * scale;
        x = x_;
        y = y_;
    }

    void draw(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(color_);
        renderer.ellipse(x - scaledWidth / 2, y - scaledHeight, scaledWidth, scaledHeight);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(Color.BLACK);
        renderer.ellipse(x - scaledWidth / 2, y - scaledHeight, scaledWidth, scaledHeight);
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(color_);
        renderer.rect(x - scaledWidth / 2, y - scaledHeight / 2, scaledWidth, scaledHeight);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(Color.BLACK);
        renderer.line(x - scaledWidth / 2, y - scaledHeight / 2, x - scaledWidth / 2, y + scaledHeight / 2);
        renderer.line(x + scaledWidth / 2, y - scaledHeight / 2, x + scaledWidth / 2, y + scaledHeight / 2);
        renderer.end();
        
        renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(color_);
        renderer.ellipse(x - scaledWidth / 2, y, scaledWidth, scaledHeight);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(Color.BLACK);
        renderer.ellipse(x - scaledWidth / 2, y, scaledWidth, scaledHeight);
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.BLACK);
        renderer.ellipse(x - scaledWidth * holeFactor / 2, y + scaledHeight / 2, scaledWidth * holeFactor, scaledHeight * holeFactor);
        renderer.end();
    }
}
