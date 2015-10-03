import java.util.*;
import java.awt.Point;

public class PlayerAI extends ClientAI {
	
	int safeDistanceratio = 3;
	boolean enemyIsClose = false;
	
	int dx, dy, dx_wrapped, dy_wrapped;
	
	public PlayerAI() {
		//Write your initialization here
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
		return Move.NONE;
	}
}
