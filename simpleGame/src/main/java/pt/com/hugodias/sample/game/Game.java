package pt.com.hugodias.sample.game;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pt.com.hugodias.sample.game.entities.Field;
import pt.com.hugodias.sample.game.entities.Player;
import pt.com.hugodias.sample.game.entities.Position;
import pt.com.hugodias.sample.game.entities.Referee;
import pt.com.hugodias.sample.game.exceptions.GameAlreadyStarted;
import pt.com.hugodias.sample.game.exceptions.InvalidPositionException;
import pt.com.hugodias.sample.game.exceptions.TooMuchPlayers;

public class Game implements ApplicationContextAware {

	private Field field;

	private GameStatus status;
	private Player winner=null;
	private Map<Player,Timer> timers = new TreeMap<Player,Timer>();

	@Autowired
	private ApplicationContext applicationContext;

	@Resource(name = "max_players")
	private int max_players;

	@Autowired
	private Referee referee;

	@Resource(name = "max_row")
	private transient int MAX_ROW;
	@Resource(name = "max_column")
	private transient int MAX_COLUMN;

	public Game(Field field) {
		this.field = field;
		this.status = GameStatus.NOT_STARTED;
	}

	/**
	 * Initializes the game. Creates players in random positions.
	 */
	public void init() {
		for (int i = 0; i < max_players; i++) {
			boolean added = false;
			while (!added) {
				try {
					Position position = field.getFreePosition();
					Player player = (Player) applicationContext.getBean(Player.class, i, position);
					getField().addPlayer(player);
					added = true;
				} catch (GameAlreadyStarted | TooMuchPlayers | InvalidPositionException e) {
					added = false;
				}
			}
		}
	}

	public void reset(){
		field.reset();
		referee.reset();
		status = GameStatus.NOT_STARTED;
		winner = null;
		timers.clear();
	}
	
	public static void main(String args[]) throws InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		final Game game = (Game) context.getBean("game");
		game.init();
		game.start();
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new TimerTask(){
//
//			@Override
//			public void run() {
//				System.out.println(game.getField().toString());
//			}
//			
//		}, 0, 1000);
		Thread.sleep(100000);
		if (game.getStatus() == GameStatus.ENDED){
			System.out.println("Game ended with player " + game.getWinner() + " as the great winner");
		}
		context.close();
	}

	/**
	 * Start the action... "There can be only one". Creates triggers to make
	 * every player to move every second until last men stand.
	 */
	public void start() {
		Collection<Player> players = field.getPlayers().values();
		for (final Player player : players) {
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){

				@Override
				public void run() {
					player.move();
				}
				
			}, 0, 1000);
			timers.put(player,timer);
		}
		status = GameStatus.IN_PROGRESS;
	}

	public Field getField() {
		return field;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public GameStatus getStatus() {
		return status;
	}

	public enum GameStatus {
		NOT_STARTED, IN_PROGRESS, ENDED
	}

	public void endGame(Player player) {
		status = GameStatus.ENDED;
		winner = player;
	}

	public Player getWinner() {
		return winner;
	}
	
	public void removePlayer(Player player){
		Timer timer = timers.get(player);
		timer.cancel();
		timers.remove(player);
		if (timers.size() == 1){
			Entry<Player, Timer> entry = timers.entrySet().iterator().next();
			entry.getValue().cancel();
			endGame(entry.getKey());
		}
	}
	
	protected void setStatus(GameStatus status){
		this.status = status;
	}

	protected Map<Player, Timer> getTimers() {
		return timers;
	}
	
	
}
