package com.harystolho.doundo;

import com.harystolho.doundo.command.Command;
import com.harystolho.doundo.model.DrawableObject;

public interface DoUndoCanvas {

	public void addObject(DrawableObject obj);

	public void removeObject(DrawableObject obj);

}
