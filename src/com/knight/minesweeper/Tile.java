package com.knight.minesweeper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Tile extends JButton implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int BLANK = 0;
	public static final int BOMB = -1;
	public static final int FLAG = 1;
	public static final int ISSHOWN = -1;
	public static final int NOTABOMB = -2;
	public static final int BOMBCLICKED = -3;
	public static final int SHOWNBOMB = -4;
	public static final int QUESTIONMARK = 2;

	public static ImageIcon imgBlank = null;//new ImageIcon("T:\\Java\\Minesweeper 2014\\Image Files\\Blank.png");
	public static ImageIcon imgFlag = null;//new ImageIcon("T:\\Java\\Minesweeper 2014\\Image Files\\Flagged.png");
	public static ImageIcon imgQuestionMark = null;//new ImageIcon("T:\\Java\\Minesweeper 2014\\Image Files\\Question Mark.png");
	public static ImageIcon imgUnclicked = null;//new ImageIcon("T:\\Java\\Minesweeper 2014\\Image Files\\Unclicked.png");
	public static ImageIcon imgNotABomb = null;//new ImageIcon("T:\\Java\\Minesweeper 2014\\Image Files\\Not A Bomb.png");
	public static ImageIcon imgBombClicked = null;//new ImageIcon("T:\\Java\\Minesweeper 2014\\Image Files\\Mine Clicked.png");
	public static ImageIcon imgShownBomb = null;//new ImageIcon("T:\\Java\\Minesweeper 2014\\Image Files\\Shown Mine.png");
	public final static ImageIcon imgNumbers[] = new ImageIcon[9];

	private int underValue = BLANK;
	private int overValue = BLANK; //TODO: CHANGE THIS NUMBER FOR TESTING PORPOISES

	private int row;
	private int column;

	private Grid mineGrid;

	public Tile(int wat, int asdf, Grid grid) {
		try {
			if(imgNumbers[0] == null) {
				imgBlank = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Blank.png")));
				imgFlag = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Flagged.png")));
				imgQuestionMark = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Question Mark.png")));
				imgUnclicked = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Unclicked.png")));
				imgNotABomb = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Not A Bomb.png")));
				imgBombClicked = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Mine Clicked.png")));
				imgShownBomb = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Shown Mine.png")));
				imgNumbers[0] = imgBlank;
				imgNumbers[1] = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/1.png")));
				imgNumbers[2] = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/2.png")));
				imgNumbers[3] = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/3.png")));
				imgNumbers[4] = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/4.png")));
				imgNumbers[5] = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/5.png")));
				imgNumbers[6] = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/6.png")));
				imgNumbers[7] = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/7.png")));
				imgNumbers[8] = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/8.png")));
			}
		} catch(Exception e) {

		}
		grid.add(this);
		mineGrid = grid;
		this.row = wat;
		this.column= asdf;
		this.addMouseListener(this);
		this.setIcon(imgUnclicked);
		//underValue = (int)(Math.random() * 8) + 1;//TODO: TEMP TESTING YOLO
		if(!GameFrame.outlines) this.setBorderPainted(false);
	}

	public void setNumber(int newUnderValue){
		if (underValue != -1 && !(newUnderValue < -1 || newUnderValue > 8)) {
			this.underValue = newUnderValue;
		}
	}

	public boolean isRevealed() {
		return this.overValue == Tile.ISSHOWN;
	}

	public int cycleOver(){
		////////////////////////////
		// Done by Chris    REV 1 //
		// Initialize a variable  //
		// called over at the top //
		////////////////////////////

		if (overValue <= 1)
			overValue++;
		else if (overValue == 2)
			overValue = 0;
		return overValue;
	}
	public void updateCaption() {
		if(getOverValue() == BLANK) {
			this.setIcon(imgUnclicked);//imgUnclicked
		}
		if(this.getOverValue() == ISSHOWN) {
			if(this.getUnderValue() > -1) {
				this.setIcon(imgNumbers[underValue]);
			} else {
				this.setIcon(imgBombClicked);
			}
			this.setBorderPainted(false);
		}
		if(this.getOverValue() == FLAG) {
			this.setIcon(imgFlag);
		}
		if(this.getOverValue() == QUESTIONMARK) {
			this.setIcon(imgQuestionMark);
		}
		if(this.getOverValue() == NOTABOMB) { //For Matt to show bombs after game over
			this.setIcon(imgNotABomb);
		}
		if(this.getOverValue() == Tile.SHOWNBOMB) {
			this.setIcon(imgShownBomb);
		}
		if(this.getOverValue() == BOMBCLICKED) {
			this.setIcon(imgBombClicked);
		}
	}
	public int getRow(){
		return row;
	}
	public int getColumn(){
		return column;
	}
	public boolean isBomb(){
		if (underValue == -1){
			return true;
		}
		else{
			return false;
		}
	}
	@Override
	public void mousePressed(MouseEvent arg0){
		GameFrame.smileyButton.setIcon(GameFrame.imgSmileyWorried);
	}

	@Override
	public void mouseExited(MouseEvent arg0){}

	@Override
	public void mouseEntered(MouseEvent arg0){}

	@Override
	public void mouseClicked(MouseEvent arg0){}

	public void setBomb(){
		underValue = BOMB;
	}

	public void setOver(int over){
		overValue = over;
		updateCaption();
	}

	public int getUnderValue(){
		return this.underValue;
	}

	//MADE BY NATHAN
	public int getOverValue() {
		return this.overValue;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(GameFrame.recorder == null) GameFrame.recorder = new Recorder(mineGrid.width, mineGrid.height, mineGrid.numBombs);
		GameFrame.recorder.moves.add(new ThreeTuple<Integer, Integer, Integer>(row, column, arg0.getButton()));
		action(arg0.getButton());
		//System.out.println("I've been clicked " + row +", " + column + " with MouseButton " + arg0.getButton());
		
	}
	
	public void action(int mouseButton) {
		ModTools.tileClicked(this, mouseButton);
		if(mouseButton == 3 && this.overValue != Tile.ISSHOWN) {
			int value = 0;
			if((value = this.cycleOver()) == Tile.FLAG) {
				int total = Integer.valueOf(GameFrame.lblBombs.getText());
				GameFrame.lblBombs.setText(((total-1) + "")/*.format("%", arg1)*/);
				//WallpaperChanger.ChangeWallpaper("\\\\fileserver1\\StudentShare\\Java\\Minesweeper 2014\\new isaac victory.bmp");
				//mineGrid.showWin();
			} else if(value == Tile.FLAG + 1) {
				int total = Integer.valueOf(GameFrame.lblBombs.getText());
				GameFrame.lblBombs.setText((total+1) + "");
			}
		} else {
			if(this.overValue != FLAG) this.mineGrid.expansion(row, column);
		}
		updateCaption();
		if(GameFrame.timer.isRunning()) GameFrame.smileyButton.setIcon(GameFrame.imgSmiley);
	}

}