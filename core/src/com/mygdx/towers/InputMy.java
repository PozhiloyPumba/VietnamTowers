package com.mygdx.towers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputMy extends InputAdapter {
    private float offsetX;
    private float offsetY;
    private int grabbed = -1;
    private HanoiTowers towers;

    public InputMy(HanoiTowers t) {
        towers = t;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (towers.win || towers.bot.enable) 
            return true;

        Vector3 pos = new Vector3(screenX, screenY, 0);
        towers.camera.unproject(pos);
        
        offsetX = pos.x;
        offsetY = pos.y;
        
        Vector2 touch = new Vector2(pos.x, pos.y);
        for(int i = 0; i < towers.sticks.size; ++i) {
            if(towers.sticks.get(i).grabLast(touch)) {
                grabbed = i;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (towers.win || towers.bot.enable) 
            return true;

        Vector3 pos = new Vector3(screenX, screenY, 0);
        towers.camera.unproject(pos);

        if(grabbed != -1) {
            towers.sticks.get(grabbed).moveLast(pos.x - offsetX, pos.y - offsetY);
        }

        offsetX = pos.x;
        offsetY = pos.y;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (towers.win || towers.bot.enable)
            return true;

        if (grabbed == -1)
            return true;

        Vector3 pos = new Vector3(screenX, screenY, 0);
        towers.camera.unproject(pos);

        int dst = 0;
        for(int i = 1; i < towers.sticks.size; ++i) {
            if (towers.sticks.get(i).contains(new Vector2(pos.x, pos.y))) {
                dst = i;
                break;
            }
        }
        towers.moves += Stick.move(towers.sticks.get(grabbed), towers.sticks.get(dst));
        grabbed = -1;
        return true;
    }

    @Override
	public boolean keyDown (int keycode) {
		if(towers.win) {
            if(keycode == Input.Keys.R) {
                towers.reset();
                towers.bot.enable = false;
            }
        }
        else {
            if(keycode == Input.Keys.A) {
                towers.bot.enable = true;
            }
            if(keycode == Input.Keys.S) {
                towers.bot.enable = false;
                towers.bot.clear();
            }
        }

        return true;
	}
}
