package com.harystolho.doundo;

import java.awt.Toolkit;

public class Utils {

	public static double getScreenWidth() {
		return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	public static double getScreenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

}
