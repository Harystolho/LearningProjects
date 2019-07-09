package com.harystolho.doundo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Square extends DrawableObject {

	private int length;

	public Square(int x, int y, int length) {
		super(x, y);
		this.length = length;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		
		g.drawRect(x, y, length, length);
	}

}
