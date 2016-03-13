package pt.com.hugodias.sample.game;

import java.util.Collection;
import java.util.Timer;
import java.util.Map.Entry;

import pt.com.hugodias.sample.game.entities.Field;
import pt.com.hugodias.sample.game.entities.Player;

public class GameExtensionForTest extends Game{

	public GameExtensionForTest(Field field) {
		super(field);
	}

	@Override
	public void start() {
		Collection<Player> players = getField().getPlayers().values();
		for (final Player player : players) {
			Timer timer = new Timer();
				getTimers().put(player,timer);
		}
		setStatus(GameStatus.IN_PROGRESS);
	}

	@Override
	public void removePlayer(Player player){
		getTimers().remove(player);
		if (getTimers().size() == 1){
			Entry<Player, Timer> entry = getTimers().entrySet().iterator().next();
			endGame(entry.getKey());
		}
	}

	
}
