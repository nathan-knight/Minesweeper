package com.knight.minesweeper;

import java.io.FileWriter;
import java.util.ArrayList;


public class Recorder {

	public boolean[][] board;
	int width = 9;
	int height = 9;
	int bombs = 10;
	public ArrayList<ThreeTuple<Integer, Integer, Integer>> moves 
	= new ArrayList<ThreeTuple<Integer, Integer, Integer>>();

	public Recorder(int width, int height, int bombs) {
		board = new boolean[width][height];
		this.bombs = bombs;
		this.width = width;
		this.height = height;
	}

	public void parseBoard(String s) {
		int pos = 0;
		int placedBombs = 0;
		for(char c : s.toCharArray()) {
			board[pos / width][pos % width] = "1".equals(""+c);
			if("1".equals(""+c)) placedBombs++;
			pos++;
		}
		//System.out.println(placedBombs + " bombs placed");
	}

	public void Save(String path) {
		try {
			FileWriter fw = new FileWriter(path, false);
			String lineSeparator = System.getProperty("line.separator");
			fw.write(width+lineSeparator);
			fw.write(height+lineSeparator);
			fw.write(bombs+lineSeparator);
			System.out.println("Writing recording of " + width + "x" + height);
			String boardStr = "";
			for(boolean[] bb : board) {
				for(boolean b : bb) if(b) boardStr+= "1"; else boardStr += "0";
			}
			fw.write(boardStr+lineSeparator);
			for(ThreeTuple<Integer,Integer,Integer> tuple : moves) {
				fw.write(tuple.x+","+tuple.y+","+tuple.z+lineSeparator);
			}

			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ThreeTuple<Integer, Integer, Integer>> getMoves() {
		return this.moves;
	}
}
class Tuple<X, Y> {
	public final X x;
	public final Y y;
	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}
}