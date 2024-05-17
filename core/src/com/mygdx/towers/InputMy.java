package com.mygdx.towers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class InputMy extends InputAdapter {
    float offsetX;
    float offsetY;
    int grabbed = -1;
    Array<Stick> sticks;
    OrthographicCamera camera;

    public InputMy(Array<Stick> s, OrthographicCamera cam) {
        sticks = s;
        camera = cam;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);

        offsetX = pos.x;
        offsetY = pos.y;

        Vector2 touch = new Vector2(pos.x, pos.y);
        for(int i = 0; i < sticks.size; ++i) {
            if(sticks.get(i).grabLast(touch)) {
                grabbed = i;
            }
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);

        if(grabbed != -1) {
            sticks.get(grabbed).moveLast(pos.x - offsetX, pos.y - offsetY);
        }

        offsetX = pos.x;
        offsetY = pos.y;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (grabbed == -1)
            return true;

        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);

        int dst = 0;
        for(int i = 1; i < sticks.size; ++i) {
            if (sticks.get(i).contains(new Vector2(pos.x, pos.y))) {
                dst = i;
                break;
            }
        }
        Stick.move(sticks.get(grabbed), sticks.get(dst));
        grabbed = -1;
        return true;
    }
}
