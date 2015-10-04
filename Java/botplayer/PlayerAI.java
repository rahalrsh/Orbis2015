import java.util.*;
import java.awt.Point;

import com.orbischallenge.engine.gameboard.GameObject;

public class PlayerAI extends ClientAI {

	int safeDistanceratio = 3;
	boolean enemyIsClose = false;

	int dx, dy, dx_wrapped, dy_wrapped;
	int dirx[] = { -1, 0, 1, 0 };
	int diry[] = { 0, 1, 0, -1 };

	ArrayList<Bullet> BulletArrayList;
	ArrayList<PowerUp> PowerUpArrayList;
	ArrayList<Wall> WallsArrayList;

	int GameWidth, GameHeight; // both will be set to 15 in  Map1.json

	Point PlayerCurPos = null;
	Point PlayerNxtPos = null;

	Direction curDir;
	Random rnd;

	ArrayList<Move> playerMoves;

	boolean gaveForward = true;

	public PlayerAI() {
		// Write your initialization here
		BulletArrayList = new ArrayList<Bullet>();
		PowerUpArrayList = new ArrayList<PowerUp>();
		WallsArrayList = new ArrayList<Wall>();
		playerMoves = new ArrayList<Move>();
		PlayerCurPos = new Point();
		PlayerNxtPos = new Point();
		rnd = new Random(4);
	}

	@Override
	public Move getMove(Gameboard gameboard, Opponent opponent, Player player)
			throws NoItemException, MapOutOfBoundsException {

		// Write your AI here
		GameWidth = gameboard.getWidth();
		GameHeight = gameboard.getHeight();
		
		System.out.println("game width and height " + GameWidth + GameHeight);

		BulletArrayList = gameboard.getBullets();
		PowerUpArrayList = gameboard.getPowerUps();
		WallsArrayList = gameboard.getWalls();

		curDir = player.getDirection();
		System.out.println("\n\nplayer direction " + player.getDirection());

		// greedy walking logic
		PlayerCurPos.x = player.x;
		PlayerCurPos.y = player.y;

		// iff last move was a turn, try to go forward in that direction
		if (!gaveForward) {
			if (isPlayerSafe(PlayerNxtPos, gameboard)) {
				 //gaveForward = true;
				 //return Move.FORWARD;
				System.out.println("went forward in 2");
				return MoveToNewPosition();
			}
		}

		// randomly pick a direction until we get a safe position to move
		// we are currently in the previously calculated next position
		// so calculate a new next position to move
		int index;
		System.out.println("cur pos: " + PlayerCurPos);
		do {
			index = rnd.nextInt(4);
			System.out.println("rand: " + index);
			int x = PlayerCurPos.x + dirx[index];
			int y = PlayerCurPos.y + diry[index];
			PlayerNxtPos.setLocation(x, y);
			System.out.println("rand next pos: " + PlayerNxtPos);
			wrapAroundPoint(PlayerNxtPos);
			System.out.println("wrap next pos: " + PlayerNxtPos);
		} while (!isPlayerSafe(PlayerNxtPos, gameboard));

		System.out.println("safe next pos: " + PlayerNxtPos);

		// make the move to new position from current position
		return MoveToNewPosition();

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

	private Move MoveToNewPosition() {
		if ((PlayerCurPos.x + 1 == PlayerNxtPos.x)
				&& (curDir != Direction.RIGHT)) {
			gaveForward = false;
			return Move.FACE_RIGHT;

		} else if ((PlayerCurPos.y + 1 == PlayerNxtPos.y)
				&& (curDir != Direction.DOWN)) {
			gaveForward = false;
			return Move.FACE_DOWN;

		} else if ((PlayerCurPos.x - 1 == PlayerNxtPos.x)
				&& (curDir != Direction.LEFT)) {
			gaveForward = false;
			return Move.FACE_LEFT;
			
		} else if ((PlayerCurPos.y - 1 == PlayerNxtPos.y)
				&& (curDir != Direction.UP)) {
			gaveForward = false;
			return Move.FACE_UP;
		} else {
			gaveForward = true;
			System.out.println("went forward in 2");
			return Move.FORWARD;
		}
	}

	// Check if player is safe in a given point

	boolean isPlayerSafe(Point currPoint, Gameboard gameboard) {
		// to be checked for danger
		Point check = new Point();

		// Check what's in the current point
		try {
			if (gameboard.isWallAtTile(currPoint.x, currPoint.y)
					|| gameboard.isTurretAtTile(currPoint.x, currPoint.y)) {
				System.out.println(" ** wall here ! **");
				return false;
			}
			else{
				return true;
			}
				
		} catch (MapOutOfBoundsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true; 
	}

		/*
		// Check if there are turrets within the current point
		for (int i = 0; i < 4; i++) {
			for (int r = 0; r < 5; r++) {
				check.x = currPoint.x + r * dirx[i];
				check.y = currPoint.y + r * diry[i];
				wrapAroundPoint(check);

				try {
					// if we find a wall we don't have to check anymore in that
					// direction. player is safe
					if (gameboard.isWallAtTile(check.x, check.y)) {
						// System.out.println("wall");
						return false;
					}

					// if we find a Turret, we have to check for some conditions
					// Ex. Turret might be in its cool down time. That means the
					// tile is safe
					if (gameboard.isTurretAtTile(check.x, check.y)) {
						if (gameboard.getTurretAtTile(check.x, check.y)
								.isFiringNextTurn()) {
							return false;
						}
						
						if (gameboard.getTurretAtTile(check.x, check.y)
								.getFireTime() >= 1) {
							return false;
						}
						return true;
					}

					// check for bullets with are in range of r with currPoint
					if (r <= 2) {
						if (gameboard.areBulletsAtTile(check.x, check.y)) {
							ArrayList<Bullet> bulletDir = gameboard
									.getBulletsAtTile(check.x, check.y);
							// System.out.println("Bullet found\n");

							// Go thru all the bullets in that position and see
							// if they are facing players direction
							int b;
							for (b = 0; b < bulletDir.size(); b++) {
								Bullet bullet = bulletDir.get(b);
								if (i == 0
										&& bullet.getDirection() == Direction.RIGHT) {
									System.out.println("will hit\n");
									return false;
								} else if (i == 1
										&& bullet.getDirection() == Direction.UP) {
									System.out.println("will hit\n");
									return false;
								} else if (i == 2
										&& bullet.getDirection() == Direction.LEFT) {
									System.out.println("will hit\n");
									return false;
								} else if (i == 3
										&& bullet.getDirection() == Direction.DOWN) {
									System.out.println("will hit\n");
									return false;
								}

								else {
									System.out.println("wont hit\n");
									return true;
								}
							}
						}
					}
				} catch (MapOutOfBoundsException | NoItemException e) {
					e.printStackTrace();
				}
			}

		}
		return true;
	}*/

	// Check if Enemy is close to the player
	boolean isEnemyClose(Gameboard gameboard, Opponent opponent, Player player) {
		dx = Math.abs(player.x - opponent.x);
		dy = Math.abs(player.y - opponent.y);
		if (dx > gameboard.getWidth() / safeDistanceratio
				|| dy > gameboard.getHeight() / safeDistanceratio) {
			dx_wrapped = gameboard.getWidth() - dx;
			dy_wrapped = gameboard.getHeight() - dy;
			if (dx_wrapped > gameboard.getWidth() / safeDistanceratio
					&& dx > gameboard.getWidth() / safeDistanceratio) {
				return false;
			} else if (dy_wrapped > gameboard.getHeight() / safeDistanceratio
					&& dy > gameboard.getHeight() / safeDistanceratio) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

}
