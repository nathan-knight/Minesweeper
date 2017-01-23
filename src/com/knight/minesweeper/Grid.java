package com.knight.minesweeper;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Grid extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Tile[][] tiles;
	GameFrame mineFrame;
	int numBombs;
	int unclickedTiles;
	public boolean beenClicked;
	public int width = 9;
	public int height = 9;
	private String secretString = "Secret Fogarty";

	/* Made by Nathan Knight
	 * Instantiates the tile array (Tile tiles[][])
	 * Populates array
	 * Adds actionlisteners to buttons
	 * Sets bounds itself (jpanel)
	 * Sets up the gridlayout of itself
	 */

	public Grid(GameFrame frame) {
		tiles = new Tile[width][height];
		unclickedTiles = width*height;
		GridLayout g = new GridLayout(width,height);
		this.setLayout(g);
		int size = 18;
		if(!GameFrame.outlines) size = 14;
		this.setPreferredSize(new Dimension(width * size, height * size));
		this.setMaximumSize(new Dimension(width * size, height * size));
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				tiles[x][y] = new Tile(x, y, this);
			}
		}
		numBombs = 10;
		this.mineFrame = frame;
	}
	public Grid(GameFrame frame, int width, int height, int bombs) {
		tiles = new Tile[width][height];
		unclickedTiles = width * height;
		GridLayout g = new GridLayout(width,height);
		this.setLayout(g);
		int size = 18;
		if(!GameFrame.outlines) size = 17;//14 Micro mode
		this.setPreferredSize(new Dimension(width * size, height * size));
		this.setMaximumSize(new Dimension(width * size, height * size));
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				tiles[x][y] = new Tile(x, y, this);
			}
		}
		numBombs = bombs;
		this.height = height;
		this.width = width;
		this.mineFrame = frame;
	}

	public void showLoss(int clickedRow, int clickedColumn){
		for(int row = 0; row < width; row++){
			for(int column = 0; column < height; column ++){
				int underValue = tiles[row][column].getUnderValue();
				int overValue = tiles[row][column].getOverValue();
				if(underValue == -1 && overValue != 1){
					tiles[row][column].setOver(-4);
				}
				else if(underValue != -1 && overValue == 1){
					tiles[row][column].setOver(-2);
				}
				tiles[row][column].removeMouseListener(tiles[row][column]);
			}
		}
		tiles[clickedRow][clickedColumn].setOver(-3);
		GameFrame.timer.stop();
		GameFrame.smileyButton.setIcon(GameFrame.imgSmileyDead);
		ModTools.onLoss(tiles[clickedRow][clickedColumn]);
		if(GameFrame.saveReplay) GameFrame.recorder.Save(GameFrame.saveLoc);
	}
	public void showWin(){
		for(int x=0;x < width;x++){
			for(int y=0;y < height;y++){
				if(tiles[x][y].getUnderValue()==Tile.BOMB) {
					tiles[x][y].setOver(Tile.FLAG);;
				}
				tiles[x][y].removeMouseListener(tiles[x][y]);
			}
		}
		GameFrame.timer.stop();
		GameFrame.lblBombs.setText("000");
		GameFrame.smileyButton.setIcon(GameFrame.imgSmileyWin);
		ModTools.onWin();
		highScore(Integer.valueOf(GameFrame.lblTimer.getText()));
		if(GameFrame.saveReplay) GameFrame.recorder.Save(GameFrame.saveLoc);
		if(GameFrame.cagemode) try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(Grid.class.getResource("/res/nick win.wav"));
			clip.open(ais);
			clip.start();
			while (!clip.isRunning()) Thread.sleep(10);
			while (clip.isRunning()) Thread.sleep(10);
			clip.close();
		}catch (Exception exc){
			exc.printStackTrace();
		}


	}
	public void highScore1(int time) {
		String curScore = "";//new String();
		boolean highscored = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader("highscores.txt"));
			curScore = br.readLine();
			br.close();
			String split[] = curScore.split(":");
			if(time < Integer.valueOf(split[1])) {
				//System.out.println("Congratulations, you have the new high score with " + time + " seconds");
				PrintWriter pr = new PrintWriter("highscores.txt", "UTF-8");
				pr.println(System.getProperty("user.name") + ":" + time);
				pr.close();
				//WallpaperChanger.ChangeWallpaper("\\\\fileserver1\\StudentShare\\Java\\Minesweeper 2014\\new isaac victory.bmp");
				JOptionPane.showMessageDialog(GameFrame.mineFrame,
						"You have the new high score! You beat " + split[0] + "'s time of " + split[1] + " with your " + time + " seconds!");
				highscored = true;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find high score file!");
			try {
				PrintWriter pr = new PrintWriter("highscores.txt", "UTF-8");
				pr.println(System.getProperty("user.name") + ":" + time);
				pr.close();
				JOptionPane.showMessageDialog(GameFrame.mineFrame,
						"You have the new high score! Your time: " + time + " seconds!");
				highscored = true;
				//System.out.println("New high score file: " + System.getProperty("user.name") + ":" + time);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void highScore(int time) {
		if(GameFrame.godmode || !GameFrame.writeScore) return;
		boolean highscored = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader("highscores.txt"));
			ArrayList<String> scores = new ArrayList<String>();
			String line;
			while((line = br.readLine()) != null) scores.add(line);
			br.close();
			ArrayList<String[]> splitScores = new ArrayList<String[]>();
			for(String s : scores) splitScores.add(s.split(":"));
			int highscore = 999;
			int entry = 0;
			for(String s[] : splitScores) {
				if(s[0].equals(""+width) && s[1].equals(""+height) && s[2].equals(""+numBombs)) {
					highscore = Integer.valueOf(s[4]);
					break;
				} else entry++;
			}
			String newLine = System.getProperty("line.separator");
			if(time<highscore) {
				highscored = true;
				String score = width+":"+height+":"+this.numBombs+":"+System.getProperty("user.name")+":"+time;
				if(scores.size() > entry) scores.remove(entry);
				scores.add(score);
				FileWriter fw = new FileWriter("highscores.txt", false);
				for(String s : scores) {
					fw.write(s + newLine);
				}
				fw.close();
				if(splitScores.size() > entry) JOptionPane.showMessageDialog(GameFrame.mineFrame,
						"You have the new high score in the " + width+"x" + height+" with " + numBombs + " bombs category" 
								+ "! Your time: " + time + " seconds! You beat " + splitScores.get(entry)[3] + "'s score of " + splitScores.get(entry)[4]);
				else JOptionPane.showMessageDialog(GameFrame.mineFrame,
						"You have the new high score in the " + width+"x" + height+" with " + numBombs + " bombs category" 
								+ "! Your time: " + time + " seconds!");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find high score file!");
			try {
				highscored = true;
				PrintWriter pr = new PrintWriter("highscores.txt", "UTF-8");
				String score = width+":"+height+":"+this.numBombs+":"+System.getProperty("user.name")+":"+time;
				pr.println(score);
				pr.close();
				JOptionPane.showMessageDialog(GameFrame.mineFrame,
						"You have the new high score in the " + width+"x" + height+" with " + numBombs + " bombs category" 
								+ "! Your time: " + time + " seconds!");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(highscored && JOptionPane.showConfirmDialog(mineFrame, "Would you like to save that replay", "Super Highscore System v9002", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			GameFrame.recorder.Save(JOptionPane.showInputDialog(mineFrame, "Enter the location you want to save to (like mysave.txt)"));
		}
		//GameFrame.recorder.Save("test.txt");
	}

	public boolean calcWin(){
		if (numBombs == unclickedTiles) {
			System.out.println("Game won!");
			return true;
		} else
			return false;
	}
	public void makeBombs(int firstRow, int firstColumn){
		int bombCount = 0;
		java.util.Random rand = new java.util.Random();
		while(bombCount < numBombs) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			if(!tiles[x][y].isBomb() && x != firstRow && y != firstColumn) {
				tiles[x][y].setNumber(Tile.BOMB);
				incrementBombsTouching(x,y);
				bombCount++;
				GameFrame.recorder.board[x][y] = true;
				ModTools.onBombPlace(tiles[x][y]);
			}
		}
	}

	public void incrementBombsTouching(int row, int column) {
		for(int x = -1; x < 2; x++) {
			for(int y = -1; y < 2; y++) {
				if(x + row > -1 && y + column > -1 && x + row < width && y + column < height && tiles[x + row][y + column].getUnderValue() != Tile.BOMB) 
					tiles[x + row][y + column].setNumber(tiles[x + row][y + column].getUnderValue() + 1);
			}
		}
	}

	private Tile getTile(int row, int column){
		return tiles[row][column];
	}
	private boolean tileIsBlank(Tile inputTile){
		return inputTile.getUnderValue() == 0;
	}
	private void reveal(Tile inputTile){
		if (inputTile.isRevealed()){
			System.out.println("problem");
		}
		inputTile.setOver(-1);
	}
	private boolean isRevealed(Tile inputTile){
		return inputTile.getOverValue() == -1;
	}
	private boolean isFlagged(Tile inputTile){
		return inputTile.getOverValue() == 1;
	}
	private void firstClick(int row, int column){
		if (!beenClicked){
			beenClicked = true;
			makeBombs(row, column);
			GameFrame.timer.start();
		}
	}
	private int calcNumNeighbors(int row, int column){
		int startNum = 8;
		if (row == 0 || row == tiles.length - 1){
			startNum -= 3;
			if (column == 0 || column == tiles[0].length - 1){
				startNum -= 2;
			}
			return startNum;
		}
		else if (column == 0 || column == tiles[0].length - 1){
			startNum -= 3;
		}
		return startNum;
	}
	private Tile[] getNeighbors(int clickedRow, int clickedColumn){
		int places = calcNumNeighbors(clickedRow, clickedColumn);
		int index = 0;
		Tile[] adjacentLocations = new Tile[places];
		for (int changeInRow = clickedRow > 0 ? -1 : 0; changeInRow + clickedRow < tiles.length && changeInRow <= 1; changeInRow++){ //iterates through all adjacent rows
			for (int changeInColumn = clickedColumn > 0 ? -1 : 0; changeInColumn + clickedColumn < tiles[0].length && changeInColumn <= 1; changeInColumn++){ //iterates through all adjacent columns (in the row)
				if (changeInColumn != 0 || changeInRow != 0){ //makes sure it doesn't call on the same tile.
					int newRow = changeInRow + clickedRow; //finds the new position (row)
					int newColumn = changeInColumn + clickedColumn; //finds the new position (column)
					Tile newTile = getTile(newRow, newColumn); //gets the new tile
					adjacentLocations[index] = newTile;
					index++;
				}
			}
		}
		return adjacentLocations;
	}
	public void expansion(int clickedRow, int clickedColumn){
		firstClick(clickedRow, clickedColumn); //places bombs if this is the first click.
		Tile currentTile = getTile(clickedRow, clickedColumn);
		if (!isRevealed(currentTile)){
			reveal(currentTile); //shows the tiles undervalue if it isn't already.
			if(!currentTile.isBomb()) this.unclickedTiles--;
		}
		if(currentTile.isBomb() && !GameFrame.godmode) {
			showLoss(clickedRow, clickedColumn);
		}
		if (tileIsBlank(currentTile)){ //tileIsBlank returns true if the undervalue of the tile is 0.
			for (Tile adjacentTile : getNeighbors(clickedRow, clickedColumn)){
				if (!isRevealed(adjacentTile) && !isFlagged(adjacentTile)){ //tile isn't already revealed, and isn't flagged
					expansion(adjacentTile.getRow(), adjacentTile.getColumn()); //recursive call to "expansion"
				}
			}
		}
		if(calcWin()) showWin();
		//System.out.println("Tiles Remaining: " + unclickedTiles);
	}
}