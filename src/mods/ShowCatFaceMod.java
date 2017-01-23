package mods;

import com.knight.minesweeper.BaseMod;
import com.knight.minesweeper.Tile;


public class ShowCatFaceMod extends BaseMod {
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void tileClicked(Tile t, int mouseButton) {
		//JOptionPane.showMessageDialog(GameFrame.mineFrame, "Cat");
		//t.cycleOver();
	}
	
	@Override
	public void onWin() {
		
	}
	
	@Override
	public void onLoss(Tile t) {
		
	}
	
}
