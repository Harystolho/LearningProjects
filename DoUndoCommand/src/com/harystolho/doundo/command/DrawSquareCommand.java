package com.harystolho.doundo.command;

import com.harystolho.doundo.DoUndoCanvas;
import com.harystolho.doundo.model.Square;

public class DrawSquareCommand implements Command {

	private int x;
	private int y;
	private int length;

	private Square square;

	public DrawSquareCommand(int x, int y, int length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}

	@Override
	public void execute(DoUndoCanvas canvas) {
		square = new Square(x, y, length);

		canvas.addObject(square);
	}

	@Override
	public void undo(DoUndoCanvas canvas) {
		canvas.removeObject(square);
	}

}
