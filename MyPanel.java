package spaceinvaders;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener, KeyListener {
	
	final int PANEL_HEIGHT = 500;
	final int PANEL_WIDTH = 500;
	final int SHIPYPOSITION = PANEL_HEIGHT-50;
	int shipX = PANEL_WIDTH/2 - 40;
	int shipVelocity = 5;
	int bulletVelocity = -5;
	int lives = 3;
	int score = 0;
	int alienSway = 1;
	int aliensRemaining = 40;
	boolean play = true;
	boolean gameOver = false;
	Image ship;
	Alien[][] aliens = new Alien[10][4];
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	
	public MyPanel() {
		this.setPreferredSize(new Dimension(PANEL_HEIGHT, PANEL_WIDTH));
		this.setBackground(Color.black);
		addKeyListener(this);
		this.setFocusable(true);
		ship = new ImageIcon("ship.png").getImage();
		for (int i = 0; i < aliens.length; i++) {
			for (int j = 0; j < aliens[i].length; j++) {
				aliens[i][j] = new Alien(40*i + 40, 30*j + 50, "enemy.png");
			}
		}
		Timer timer = new Timer(30, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		//draw ship
		g2D.drawImage(ship, shipX, SHIPYPOSITION, null);
		//draw aliens
		for (int i = 0; i < aliens.length; i++) {
			for (int j = 0; j < aliens[i].length; j++) {
				if (aliens[i][j].isAlive) {
					g2D.drawImage(aliens[i][j].enemy, aliens[i][j].xPos, aliens[i][j].yPos, null);
				}
			}
		}
		
		g2D.setColor(Color.white);
		
		//draw bullet
		if (bullets.size() > 0) {
			for (Bullet b: bullets) {
				if (b.draw == true) {
					g2D.fillRect(b.xPos, b.yPos, 5, 20);
				}
			}
		}
		
		//draw lives and score
		g2D.setFont(new Font("serif", Font.BOLD, 15));
		g2D.drawString("Lives: " + lives, 410, 25);
		g2D.drawString("Score: " + score, 330, 25);
		
		//displaying messages and instructions to restart after losing a life, losing the game, or winning the game
		if (!play && lives > 0 && !gameOver) {
			g2D.drawString("Press up arrow key to continue", PANEL_WIDTH/2 - 100, PANEL_HEIGHT/2 + 30);
		}
		if (gameOver && lives == 0) {
			g2D.drawString("Game over. Press down arrow key to play again", PANEL_WIDTH/2 - 150, PANEL_HEIGHT/2 + 10);
		}
		if (gameOver && lives > 0) {
			g2D.drawString("You Win! Press down arrow key to play again", PANEL_WIDTH/2 - 150, PANEL_HEIGHT/2 + 10);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (play) {
			for (int i = 0; i < aliens.length; i++) {
				for (int j = 0; j < aliens[i].length; j++) {
					aliens[i][j].xPos += alienSway;
				}
			}
			if (aliens[aliens.length-1][aliens[0].length-1].xPos == PANEL_WIDTH - 70 || aliens[0][aliens[0].length-1].xPos == 30) {
				alienSway = alienSway*-1;
			}
			if (bullets.size() > 0) {
				for (Bullet b : bullets) {
					if (b.draw && b.isShip) {
						b.yPos += bulletVelocity;
					}
					if (b.draw && !b.isShip) {
						b.yPos -= bulletVelocity;
					}
					if (b.yPos < 0 || b.yPos > PANEL_HEIGHT) {
						b.draw = false;
					}
					//checking for collisions between bullets and aliens
					for (int i = 0; i < aliens.length; i++) {
						for (int j = 0; j < aliens[i].length; j++) {
							if (aliens[i][j].isAlive ) {
								if (b.isShip && new Rectangle(b.xPos, b.yPos, 5, 20).intersects(new Rectangle(aliens[i][j].xPos, aliens[i][j].yPos, aliens[i][j].enemy.getWidth(null), aliens[i][j].enemy.getHeight(null)))) {
									score+=10;
									aliensRemaining--;
									aliens[i][j].isAlive = false;
									b.draw = false;
									b.xPos = 600;
									b.yPos = -10;
								}
							}
						}
					}
					//if statement to check for collision with bullet and ship
					if (!b.isShip && new Rectangle(b.xPos, b.yPos, 5, 20).intersects(new Rectangle(shipX, SHIPYPOSITION, ship.getWidth(null), ship.getHeight(null)))) {
						lives--;
						shipX = PANEL_WIDTH/2 - 40;
						play = false;
						for (Bullet bul : bullets) {
							bul.xPos = 700;
							bul.yPos = -20;
						}
					}
				}
			}
			
			//changing up frequency of bullets depending on number of aliens remaining
			if (aliensRemaining >= 30) {
				if (Math.random() < 0.02) {
					int row = (int) Math.floor(10*Math.random());
					int col = (int) Math.floor(4*Math.random());
					if (aliens[row][col].isAlive) {
						bullets.add(new Bullet(aliens[row][col].xPos, aliens[row][col].yPos, false));
					}
				}
			} else if (aliensRemaining >= 18 && aliensRemaining <= 29) {
				if (Math.random() < 0.08) {
					int row = (int) Math.floor(10*Math.random());
					int col = (int) Math.floor(4*Math.random());
					if (aliens[row][col].isAlive) {
						bullets.add(new Bullet(aliens[row][col].xPos, aliens[row][col].yPos, false));
					}
				}
			} else if (aliensRemaining >= 8 && aliensRemaining <= 17) {
				if (Math.random() < 0.18) {
					int row = (int) Math.floor(10*Math.random());
					int col = (int) Math.floor(4*Math.random());
					if (aliens[row][col].isAlive) {
						bullets.add(new Bullet(aliens[row][col].xPos, aliens[row][col].yPos, false));
					}
				}
			} else if (aliensRemaining > 0 && aliensRemaining <= 7) {
				if (Math.random() < 0.36) {
					int row = (int) Math.floor(10*Math.random());
					int col = (int) Math.floor(4*Math.random());
					if (aliens[row][col].isAlive) {
						bullets.add(new Bullet(aliens[row][col].xPos, aliens[row][col].yPos, false));
					}
				}
			}
			//ending the game with either no aliens or lives remaining
			if (lives == 0 || aliensRemaining == 0) {
				play = false;
				gameOver = true;
				for (Bullet bullet : bullets) {
					bullet.xPos = 700;
					bullet.yPos = -20;
				}
			}
		}
		repaint();
	}
	
	public void restart() {
		lives = 3;
		score = 0;
		for (int i = 0; i < aliens.length; i++) {
			for (int j = 0; j < aliens[i].length; j++) {
				aliens[i][j] = new Alien(40*i + 40, 30*j + 50, "enemy.png");
			}
		}
		gameOver = false;
		play = true;
		aliensRemaining = 40;
		shipX = PANEL_WIDTH/2 - 40;
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (play) {
			switch (code) {
			case KeyEvent.VK_LEFT:
				if (shipX >= 0) {
					shipX -= shipVelocity;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (shipX <= PANEL_WIDTH-40) {
					shipX += shipVelocity;
				}
				break;
			}
			if (code == KeyEvent.VK_SPACE) {
				bullets.add(new Bullet(shipX+(ship.getWidth(null))/2, SHIPYPOSITION, true));
			}
		}
		if (!play && code == KeyEvent.VK_UP) {
			play = true;
		}
		if (gameOver && code == KeyEvent.VK_DOWN) {
			restart();
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}



	@Override
	public void keyReleased(KeyEvent e) {}

}
