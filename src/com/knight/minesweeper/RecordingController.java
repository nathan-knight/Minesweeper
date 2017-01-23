package com.knight.minesweeper;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class RecordingController extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Recorder recorder;
	JButton next;
	
	public RecordingController(Recorder r) {
		recorder = r;
		if(r == null) System.out.println("There is problem. R == null in RC Constructor");
		GameFrame.resetBoard(r.width, r.height, r.bombs);
		Grid grid = GameFrame.mineGrid;
		grid.beenClicked = true;
		for(int x = 0; x < r.width; x++) {
			for(int y = 0; y < r.height; y++) {
				if(recorder.board[x][y]) {
					grid.tiles[x][y].setBomb();
					grid.incrementBombsTouching(x, y);
				}
			}
		}
		GameFrame.writeScore = false;
		this.setSize(200, 50);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridLayout(1,3));
		next = new JButton(">");
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(recorder.moves.size() == 0) return;
				System.out.println("Emulating click at " + recorder.getMoves().get(0).x + ", " + recorder.getMoves().get(0).y);
				Tile t = GameFrame.mineGrid.tiles[recorder.getMoves().get(0).x][recorder.getMoves().get(0).y];
				t.action(recorder.getMoves().get(0).z);
				recorder.moves.remove(0);
			}
		});
		this.add(next);
		this.setVisible(true);
	}
	
}
