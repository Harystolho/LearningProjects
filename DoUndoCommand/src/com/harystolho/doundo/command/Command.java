package com.harystolho.doundo.command;

import com.harystolho.doundo.DoUndoCanvas;

public interface Command {

	public void execute(DoUndoCanvas canvas);

	public void undo(DoUndoCanvas canvas);

}
