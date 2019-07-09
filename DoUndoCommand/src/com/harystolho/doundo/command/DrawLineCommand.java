package com.harystolho.doundo.command;

import com.harystolho.doundo.DoUndoCanvas;
import com.harystolho.doundo.model.Line;

public class DrawLineCommand implements Command {

	private int x;
	private int y;
	private int length;

	private Line line;

	public DrawLineCommand(int x, int y, int length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}

	@Override
	public void execute(DoUndoCanvas canvas) {
		line = new Line(x, y, length);

		canvas.addObject(line);
	}

	@Override
	public void undo(DoUndoCanvas canvas) {
		if (line != null)
			canvas.removeObject(line);
	}

}
