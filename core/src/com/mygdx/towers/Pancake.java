package com.mygdx.towers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Pancake {
    public final float scale_;
    public final Color color_;

    private final float scaledWidth;
    private final float scaledHeight;

    private final float WIDTH = 100;
    private final float HEIGHT = 30;
    private final float holeFactor = 0.1f;

    // x and y it is center
    public float x;
    public float y;

    public Pancake(float x_, float y_, Color color, float scale) {
        color_ = color;
        scale_ = scale;
        scaledHeight = HEIGHT * scale * 0.95f;
        scaledWidth = WIDTH * scale;
        x = x_;
        y = y_;
    }

    public float getYUpGround() {
        return y + HEIGHT;
    }

    private void drawGround(ShapeRenderer renderer, float yBias) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(color_);
        renderer.ellipse(x - scaledWidth / 2, y + yBias, scaledWidth, scaledHeight);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(Color.BLACK);
        renderer.ellipse(x - scaledWidth / 2, y + yBias, scaledWidth, scaledHeight);
        renderer.end();
    } 

    public void draw(ShapeRenderer renderer) {
        drawGround(renderer, - scaledHeight / 2);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(color_);
        renderer.rect(x - scaledWidth / 2, y, scaledWidth, HEIGHT);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(Color.BLACK);
        renderer.line(x - scaledWidth / 2, y, x - scaledWidth / 2, y + HEIGHT);
        renderer.line(x + scaledWidth / 2, y, x + scaledWidth / 2, y + HEIGHT);
        renderer.end();

        drawGround(renderer, HEIGHT - scaledHeight / 2);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.BLACK);
        renderer.ellipse(x - scaledWidth * holeFactor / 2, y + HEIGHT - scaledHeight * holeFactor / 2, scaledWidth * holeFactor, scaledHeight * holeFactor);
        renderer.end();
    }

    public boolean contains(Vector2 pos) {
        Ellipse bottom = new Ellipse(x, y, scaledWidth, scaledHeight);
        Ellipse up = new Ellipse(x, y + scaledHeight, scaledWidth, scaledHeight);
        Rectangle center = new Rectangle(x - scaledWidth / 2, y, scaledWidth, HEIGHT);

        return center.contains(pos) || bottom.contains(pos) || up.contains(pos);
    }
}
