package mods;

import com.knight.minesweeper.BaseMod;
import com.knight.minesweeper.Tile;


public class ModMod extends BaseMod {
	
	@Override
	public void init() {
		System.out.println("asdf init() has been run");
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
