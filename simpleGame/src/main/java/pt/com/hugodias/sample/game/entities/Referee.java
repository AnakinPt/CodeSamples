package pt.com.hugodias.sample.game.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pt.com.hugodias.sample.game.Game;

@Aspect
public class Referee {

	@Autowired
	private Game game;

	private Map<Player, List<Timestamp>> suspensions = new TreeMap<Player, List<Timestamp>>();

	@Resource(name = "suspensionTime")
	private int suspensionTime;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * checks if a player can return into to the game. It verifies if suspension
	 * time has passed. If so, he is allowed to return. A player not suspended
	 * yet is always allowed to return. A player with more than 1 suspension is
	 * not allowed to return.
	 * 
	 * @param player
	 * @return
	 */
	public boolean requestEntry(Player player) {
		List<Timestamp> list = suspensions.get(player);
		if (list == null)
			return true;
		if (list.size() > 1)
			return false;
		Timestamp now = new Timestamp(new Date().getTime());
		now.setNanos(now.getNanos() - suspensionTime);
		if (now.before(list.get(0)))
			return false;
		return true;
	}

	/**
	 * Verifies if a player is too near to another player. If so, book him and
	 * add a suspension to the player.
	 * 
	 * @param joinpoint
	 * @param result
	 */
	@AfterReturning(pointcut = "execution(* pt.com.hugodias.sample.game.entities.Player.move*(..))", returning = "result")
	public void checkMove(JoinPoint joinpoint, Object result) {
		log.debug("Checking move");
		if ((Boolean) result) {
			Collection<Player> players = game.getField().getPlayers().values();
			Player playerMoved = (Player) joinpoint.getThis();
			for (Player player : players) {
				if (!player.equals(playerMoved)) {
					if (playerMoved.getPosition().isNear(player.getPosition())) {
						log.debug("Priuuuu");
						playerMoved.booked();
						List<Timestamp> list = suspensions.get(playerMoved);
						if (list == null) {
							list = new ArrayList<Timestamp>();
						}
						list.add(new Timestamp(new Date().getTime()));
					}
				}
			}
		}
	}

	/**
	 * Removes a player from the field and checks if this is the end of game.
	 * This happens when only 1 player is in game (only have 0 or 1 suspension)
	 * 
	 * @param joinpoint
	 */
	@After("execution(* pt.com.hugodias.sample.game.entities.Player.sentoff(..))")
	public void removePlayer(JoinPoint joinpoint) {
		Player playerMoved = (Player) joinpoint.getThis();
		Field field = game.getField();
		Map<Position, Player> players = field.getPlayers();
		players.remove(playerMoved.getPosition());
		if (players.size() == 1) {
			// check if no more players are available
			int i = 0;
			for (List<Timestamp> timestamp : suspensions.values()) {
				if (timestamp.size() == 1)
					i++;
			}
			if (i <= 1) {
				Iterator<Player> iterator = players.values().iterator();
				game.endGame(iterator.next());
			}
		}
		game.removePlayer(playerMoved);
	}

	public void reset() {
		suspensions.clear();
	}

	public boolean allowReturn(Player player) {
		return requestEntry(player);
	}

}
