package com.a50tt.main;

import javax.swing.SwingUtilities;

import com.a50tt.view.WindowView;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				WindowView windowView = new WindowView();
			}
		});
	}

}
