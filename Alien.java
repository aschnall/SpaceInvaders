package spaceinvaders;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Alien {

	int xPos;
	int yPos;
	Image enemy;
	boolean isAlive = true;
	
	public Alien(int x, int y, String img) {
		this.xPos = x;
		this.yPos = y;
		this.enemy = new ImageIcon(img).getImage();
	}
}
