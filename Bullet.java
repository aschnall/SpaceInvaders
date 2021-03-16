package spaceinvaders;

public class Bullet {
	
	int xPos;
	int yPos;
	boolean draw;
	boolean isShip;
	
	public Bullet(int x, int y, boolean isShip) {
		this.xPos = x;
		this.yPos = y;
		this.draw = true;
		this.isShip = isShip;
	}

}
