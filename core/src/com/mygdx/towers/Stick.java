package com.mygdx.towers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Stick {
	private Array<Pancake> disks;

	private Rectangle f;

    private final float width;
    private final float height;
    private final float x_;
    private final float y_;
	public final boolean initial;

	public Stick(float x, float y, float w, float h, Rectangle field, boolean init) {
		disks = new Array<>();

		width = w;
		height = h;
		x_ = x;
		y_ = y;
		f = field;
		initial = init;
	}

	public boolean contains(Vector2 p) {
		return f.contains(p);
	}

	public boolean grabLast(Vector2 p) {
		if (disks.isEmpty()) return false;

		Pancake last = disks.peek();
		return last.contains(p);
	}

	public void moveLast(float deltaX, float deltaY) {
		if (disks.isEmpty()) return;

		Pancake last = disks.peek();
		last.x += deltaX;
		last.y += deltaY;
	}

	public int getBiggest() {
		if (disks.isEmpty()) return 0;
		
		return disks.first().number;
	}

	public boolean add(Pancake disk) {
		if (disks.isEmpty()) {
			disk.x = x_;
			disk.y = y_;
			disks.add(disk);
			return true;
		}

		Pancake last = disks.peek();

		if(disk.number > last.number) return false;

		disk.x = x_;
		disk.y = last.getYUpGround();

		disks.add(disk);
		return true;
	}

	private Pancake removeLast() {
		return disks.pop();
	}

	public void drawPlane(ShapeRenderer renderer) {
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(initial ? Color.DARK_GRAY : Color.GOLD);
		renderer.rect(x_ - width / 2, y_ - height / 2, width, height);
		renderer.end();

		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(Color.WHITE);
		renderer.rect(x_ - width / 2, y_ - height / 2, width, height);
		renderer.end();
	}

	public void draw(ShapeRenderer renderer) {
		for(Pancake p: disks) {
			p.draw(renderer);
		}
	}

	public int getSize() {
		return disks.size;
	}

	public Array<Integer> returnDisks() {
		Array<Integer> nums = new Array<>();

		for (Pancake p: disks) {
			nums.add(p.number);
		}
		return nums;
	}

	static public int move(Stick src, Stick dst) {
		Pancake tmp = src.removeLast();

		if(dst.add(tmp)) return Boolean.compare(src != dst, false);

		src.add(tmp);
		return 0;
	}
}
