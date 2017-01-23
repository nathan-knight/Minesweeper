package mods;

import com.knight.minesweeper.BaseMod;
import com.knight.minesweeper.Tile;


public class TestMod extends BaseMod {
	
	
	public void init() {
		System.out.println("TestMod init() has been run");
	}
	
	@Override
	public void tileClicked(Tile t, int mouseButton) {
		System.out.println(t.getRow() + "," + t.getColumn() + " was clicked says TestMod");
	}
	
	@Override
	public void onWin() {
		System.out.println("Congrats on winning - TestMod");
	}
	
	@Override
	public void onLoss(Tile t) {
		System.out.println("YOU LOSE! - TestMod");
	}
	
}
