package pt.com.hugodias.sample.game.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import pt.com.hugodias.sample.game.exceptions.GameAlreadyStarted;
import pt.com.hugodias.sample.game.exceptions.InvalidPositionException;
import pt.com.hugodias.sample.game.exceptions.TooMuchPlayers;

public class Field {

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Referee referee;

	private Map<Position, Player> players = new HashMap<Position, Player>();
	private int rows;
	private int columns;
	private boolean status;

	public Field(){
		rows = 10;
		columns = 10;
		status = false;
	}
	
	public Field(int row, int column) {
		this.rows = row;
		this.columns = column;
		status = false;
	}

	public void addPlayer(Player newPlayer) throws GameAlreadyStarted, TooMuchPlayers, InvalidPositionException {
		if (status)
			throw new GameAlreadyStarted();
		if (players.size() >= 10)
			throw new TooMuchPlayers();

		if (players.containsKey(newPlayer.getPosition()))
			throw new InvalidPositionException();
		players.put(newPlayer.getPosition(), newPlayer);
	}

	public void removePlayer(Player player) {
		players.remove(player.getPosition());
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		Position position = context.getBean(Position.class);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				try {
					position.setColumn(j);
					position.setRow(i);
					if (players.containsKey(position)) {
						Player player = players.get(position);
						sb.append(player.getName());
					} else
						sb.append(" ");
				} catch (InvalidPositionException e) {
					// Void
				}

			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public Map<Position, Player> getPlayers() {
		return players;
	}

	public void reset() {
		players.clear();
		status = false;

	}

	public Position getFreePosition() {
		try {
			Position position = (Position) context.getBean("position");
			Set<Position> inGamePlayers = players.keySet();
			boolean valid = false;
			while (!valid) {
				int row = (int) Math.floor(Math.random() * rows);
				int column = (int) Math.floor(Math.random() * columns);
				position.setRow(row);
				position.setColumn(column);
				boolean near = false;
				for (Position position2 : inGamePlayers) {
					if(position.isNear(position2))
						near = true;
				}
				valid = !near;
			}
			return position;
		} catch (BeansException | InvalidPositionException e) {
			return null;
		}

	}

	public void returnPlayer(Player player) {
		if (referee.allowReturn(player)){
			players.put(player.getPosition(), player);
		}
	}

}
