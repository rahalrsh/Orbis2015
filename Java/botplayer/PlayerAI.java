import java.util.*;
import java.awt.Point;

import com.orbischallenge.engine.gameboard.GameObject;

public class PlayerAI extends ClientAI {
	int safeDistanceratio = 3;
	boolean enemyIsClose = false;
	
	int dx, dy, dx_wrapped, dy_wrapped;
    
	ArrayList<Bullet> BulletArrayList;
	ArrayList<PowerUp> PowerUpArrayList;
	ArrayList<Wall> WallsArrayList;
	int GameWidth, GameHeight;

	
	public PlayerAI() {
		//Write your initialization here
		 BulletArrayList  = new ArrayList<Bullet>();
		 PowerUpArrayList = new ArrayList<PowerUp>();
		 WallsArrayList   = new ArrayList<Wall>();
	}

	@Override
	public Move getMove(Gameboard gameboard, Opponent opponent, Player player) throws NoItemException, MapOutOfBoundsException {
		
		dx = Math.abs(player.x - opponent.x);
		dy = Math.abs(player.y - opponent.y);
		if (dx > gameboard.getWidth()/safeDistanceratio || dy > gameboard.getHeight()/safeDistanceratio){
			  dx_wrapped = gameboard.getWidth() - dx;
			  dy_wrapped = gameboard.getHeight() - dy;
			  if (dx_wrapped > gameboard.getWidth()/safeDistanceratio && dx > gameboard.getWidth()/safeDistanceratio){
				  enemyIsClose = false;
			  }
			  else if(dy_wrapped > gameboard.getHeight()/safeDistanceratio && dy > gameboard.getHeight()/safeDistanceratio){
				  enemyIsClose = false;
			  }
			  else{
				  enemyIsClose = true;
			  }
		}
		else{
			enemyIsClose = true;
		}
		
		if(enemyIsClose)
			System.out.println("ENEMY IS CLOSE!!!!\n");

		//Write your AI here
		GameWidth  = gameboard.getWidth();
		GameHeight = gameboard.getHeight();
		
		BulletArrayList  = gameboard.getBullets();
		PowerUpArrayList = gameboard.getPowerUps();
		WallsArrayList = gameboard.getWalls();
		
		for(int i = 0; i < BulletArrayList.size(); i++) {   
		    System.out.println("B x :" + BulletArrayList.get(i).x+"B y: " +BulletArrayList.get(i).y);
		} 
		
		for(int i = 0; i < PowerUpArrayList.size(); i++) {   
		    System.out.println("P x :" + PowerUpArrayList.get(i).x+"P y: " +PowerUpArrayList.get(i).y);
		} 
		
		return Move.NONE;
	}
	
	private void wrapAroundPoint(Point temp) {
		if (temp.x < 0)
			temp.x += GameWidth;
		if (temp.y < 0)
			temp.y += GameHeight;
		if (temp.x > GameWidth)
			temp.x -= GameWidth;
		if (temp.y > GameHeight)
			temp.y -= GameHeight;
	}
	
	
}
