package pt.com.hugodias.sample.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;

import pt.com.hugodias.sample.game.entities.Field;
import pt.com.hugodias.sample.game.entities.Player;
import pt.com.hugodias.sample.game.entities.Position;
import pt.com.hugodias.sample.game.entities.Referee;
import pt.com.hugodias.sample.game.exceptions.InvalidPositionException;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AppConfig {

	/**
	 * Create a new player in the position
	 * @param name
	 * @param position
	 * @return
	 */
	@Bean()
	@Scope(value="prototype")
	public Player player(String name, Position position){
		return new Player(name, position);
	}

	/**
	 * Create a new player in the position
	 * @param name
	 * @param position
	 * @return
	 */
	@Bean
	@Scope(value="prototype")
	public Player player(Integer name, Position position){
		return new Player(name.toString(), position);
	}

	/**
	 * Create a new game
	 * @return
	 */
	@Bean
	@Scope("singleton")
	public Game game(){
		return new Game(field());
	}
	
	/**
	 * Create a referee for this game. This will be a singleton
	 * @return
	 */
	@Bean
	@Scope("singleton")
	public Referee referee(){
		return new Referee();
	}
	
	/**
	 * Create a new field with the size defined by MAX_ROW and MAX_COLUMN
	 * @return
	 */
	@Bean
	@Scope("singleton")
	public Field field(){
		return new Field(max_row(), max_column());
	}
	
	/**
	 * The number of the rows for this game
	 * @return
	 */
	@Bean(name="max_row")
	@Scope("singleton")
	public int max_row(){
		return 100;
	}
	
	/**
	 * The number of columns for this game
	 * @return
	 */
	@Bean(name="max_column")
	@Scope("singleton")
	public int max_column(){
		return 100;
	}
	
	/**
	 * Number of players allowed in the game
	 * @return
	 */
	@Bean(name="max_players")
	@Scope("singleton")
	public int max_players(){
		return 10;
	}
	
	@Bean(name="suspensionTime")
	@Scope("singleton")
	public int suspensionTime(){
		return 10000;
	}
	
	/**
	 * Create new position in the defined position
	 * @param row
	 * @param column
	 * @return
	 * @throws InvalidPositionException
	 */
	@Bean
	@Scope(value="prototype")
	public Position position(int row, int column) throws InvalidPositionException{
		Position position = new Position();
		position.setRow(row);
		position.setColumn(column);
		return position;
	}
	
	/**
	 * Create a new position in the field in a random position
	 * @return
	 * @throws InvalidPositionException
	 */
	@Bean
	@Scope(value="prototype")
	public  Position position() throws InvalidPositionException{
		Position position = new Position();
		return position;
	}
	
}
