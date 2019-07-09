package com.harystolho.doundo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.harystolho.doundo.command.Command;
import com.harystolho.doundo.model.DrawableObject;

public class DoUndoCanvasImpl extends Canvas implements DoUndoCanvas {

	private static final long serialVersionUID = 145417589875L;

	List<DrawableObject> objects;

	public DoUndoCanvasImpl() {
		objects = new ArrayList<>();
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);

		for (DrawableObject drawingObject : objects) {
			drawingObject.draw(g);
		}
	}

	@Override
	public void addObject(DrawableObject obj) {
		objects.add(obj);

		repaint();
	}

	@Override
	public void removeObject(DrawableObject obj) {
		objects.remove(obj);

		repaint();
	}

}
