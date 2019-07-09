package com.harystolho.doundo;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import com.harystolho.doundo.command.Command;
import com.harystolho.doundo.command.DrawLineCommand;
import com.harystolho.doundo.command.DrawSquareCommand;

public class CanvasWrapper extends Container implements MouseListener, KeyListener {

	private static final long serialVersionUID = 12342135412435L;

	private Stack<Command> commands;

	private DoUndoCanvas canvas;

	public CanvasWrapper(DoUndoCanvas canvas) {
		commands = new Stack<>();

		this.canvas = canvas;
	}

	private void publishCommand(Command command) {
		commands.add(command);

		command.execute(canvas);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			publishCommand(new DrawLineCommand(e.getX(), e.getY(), 50));
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			publishCommand(new DrawSquareCommand(e.getX(), e.getY(), 50));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
			if (!commands.isEmpty())
				commands.pop().undo(canvas);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
