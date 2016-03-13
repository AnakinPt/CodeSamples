package pt.com.hugodias.sample.game.entities;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pt.com.hugodias.sample.game.Game;

public class Player implements Comparable<Player> {

	private String name;
	private Position position;
	private boolean playing = true;
	private boolean suspended = false;
	private boolean booked = false;
	
	@Autowired
	private Referee referee;
	
	@Resource(name="suspensionTime")
	private int suspensionTime;

	@Autowired
	private Game game;
	
	@Autowired
	private Field field;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public Player(String name, Position position) {
		this.name = name;
		this.position = position;
		log.debug(String.format("Created player %s at position (%d,%d)", name, position.getRow(), position.getColumn()));
	}

	/**
	 * Do a random move in the field.
	 * 
	 * @return
	 */
	public boolean move() {
		validateMove();
		position.move();
		log.debug(String.format("Moved player %s at position (%d,%d)", name, position.getRow(), position.getColumn()));
		return true;
	}

	/**
	 * Validate if the player can do a move.
	 * 
	 * @return
	 */
	private boolean validateMove() {
		if (!playing)
			return false;
		if (suspended) {
			return false;
		}
		return true;
	}

	/**
	 * move 1m to the left
	 * 
	 * @return
	 */
	public boolean moveLeft() {
		if (!playing)
			return false;
		return position.moveLeft();
	}

	/**
	 * move 1m to the right
	 * 
	 * @return
	 */
	public boolean moveRight() {
		if (!playing)
			return false;

		return position.moveRight();
	}

	/**
	 * move 1m up
	 * 
	 * @return
	 */
	public boolean moveUp() {
		if (!playing)
			return false;
		return position.moveUp();
	}

	/**
	 * move 1m down
	 * 
	 * @return
	 */
	public boolean moveDown() {
		if (!playing)
			return false;
		return position.moveDown();
	}

	/**
	 * Book this player. This played will be suspended for 10 seconds. In the
	 * end of the suspension, the player will request the return to the referee.
	 * If the player was already booked once, the player will be sent off the
	 * game
	 */
	public void booked() {
		log.debug(String.format("Player %s booked", name));
		if (booked) {
			sentoff();
			return;
		}
		booked = true;
		suspended = true;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				while(!referee.requestEntry(Player.this)){
					synchronized (this) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
 
				Position freePosition = field.getFreePosition();
				Player.this.position = freePosition;
				suspended = false;
				field.returnPlayer(Player.this);
			}
		}, suspensionTime);

	}

	/**
	 * Sent off the player from the game. He cannot return.
	 */
	public void sentoff() {
		log.debug(String.format("Player %s sentoff", name));
		playing = false;
		field.removePlayer(this);
		game.removePlayer(this);
	}

	/**
	 * Check if the player is "in-game"
	 * @return
	 */
	public boolean isActive() {
		return playing && !suspended;
	}

	/**
	 * returns the string representation of the player
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * returns the position of the player in the field.
	 * @return
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * returns the name
	 * @return
	 */
	public String getName() {
		return name;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public boolean isBooked() {
		return booked;
	}

	@Override
	public int compareTo(Player player) {
		return this.getName().compareTo(player.getName());
	}
	
	
}
