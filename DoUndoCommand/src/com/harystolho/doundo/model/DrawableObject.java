package com.harystolho.doundo.model;

import java.awt.Graphics;

public abstract class DrawableObject {

	protected int x;
	protected int y;

	public DrawableObject(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public abstract void draw(Graphics g);

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DrawableObject) {
			DrawableObject other = (DrawableObject) obj;

			return other.x == this.x && other.y == this.y;
		}

		return false;
	}

}
