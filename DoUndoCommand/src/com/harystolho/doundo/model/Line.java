package com.harystolho.doundo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Line extends DrawableObject {

	private int length;

	public Line(int x, int y, int length) {
		super(x, y);
		this.length = length;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.PINK);

		g.drawLine(x, y, x + length, y + length);
	}

}
