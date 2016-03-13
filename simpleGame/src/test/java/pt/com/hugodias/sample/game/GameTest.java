package pt.com.hugodias.sample.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pt.com.hugodias.sample.game.Game.GameStatus;
import pt.com.hugodias.sample.game.entities.Field;
import pt.com.hugodias.sample.game.entities.Player;
import pt.com.hugodias.sample.game.entities.Position;
import pt.com.hugodias.sample.game.entities.Referee;
import pt.com.hugodias.sample.game.exceptions.GameAlreadyStarted;
import pt.com.hugodias.sample.game.exceptions.InvalidPositionException;
import pt.com.hugodias.sample.game.exceptions.TooMuchPlayers;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class GameTest {

	@Autowired
	private Game game;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Referee referee;
	
	@Autowired
	private Field field;
	
	@Before
	public void setup(){
		game.reset();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGame() {
		game.init();
		assertEquals(10, game.getField().getRows());
		assertEquals(10, game.getField().getColumns());
		assertEquals(Game.GameStatus.NOT_STARTED, game.getStatus());
		System.out.println(game.getField().toString());
	}
	
	@Test
	public void testMove() throws InvalidPositionException, GameAlreadyStarted, TooMuchPlayers{
		Field field = game.getField();
		Position position = context.getBean(Position.class);
		position.setRow(4);
		position.setColumn(4);
		Player player = context.getBean(Player.class, "A", position);
		field.addPlayer(player);
		player.move();
		
		assertEquals(1, Math.abs(position.getRow()-4)+Math.abs(position.getColumn()-4));
	}
	
	@Test
	public void testBooking() throws InvalidPositionException, GameAlreadyStarted, TooMuchPlayers{
		Field field = game.getField();
		Position position = context.getBean(Position.class);
		position.setRow(6);
		position.setColumn(5);
		Player playerStatic = context.getBean(Player.class, "B", position);
		field.addPlayer(playerStatic);

		position = context.getBean(Position.class);
		position.setRow(4);
		position.setColumn(4);
		Player player = context.getBean(Player.class, "A", position);
		field.addPlayer(player);
		boolean possible = player.moveRight();
		assertTrue(possible);
		assertFalse(player.isActive());
		assertTrue(player.isSuspended());
		assertTrue(player.isBooked());
	}
	
	@Test
	public void testSentoff() throws InvalidPositionException, GameAlreadyStarted, TooMuchPlayers{
		Field field = game.getField();
		Position position = context.getBean(Position.class);
		position.setRow(6);
		position.setColumn(5);
		Player playerStatic = context.getBean(Player.class, "B", position);
		field.addPlayer(playerStatic);

		position = context.getBean(Position.class);
		position.setRow(4);
		position.setColumn(4);
		Player player = context.getBean(Player.class, "A", position);
		field.addPlayer(player);
		boolean possible = player.moveRight();
		assertTrue(possible);
		assertFalse(player.isActive());
		assertTrue(player.isSuspended());
		assertTrue(player.isBooked());
	}

	@Test
	public void testEndgame() throws InvalidPositionException, GameAlreadyStarted, TooMuchPlayers{
		Position position = context.getBean(Position.class);
		position.setRow(6);
		position.setColumn(5);
		Player playerStatic = context.getBean(Player.class, "B", position);
		field.addPlayer(playerStatic);
		
		position = context.getBean(Position.class);
		position.setRow(4);
		position.setColumn(4);
		Player player = context.getBean(Player.class, "A", position);
		
		game.start();
		player.booked();
		player.booked();
		assertEquals(GameStatus.ENDED, game.getStatus());
	}

	@Test
	public void testMovingAllDirections() throws InvalidPositionException, GameAlreadyStarted, TooMuchPlayers{
		Position position = context.getBean(Position.class);
		position.setRow(0);
		position.setColumn(0);
		Player playerStatic = context.getBean(Player.class, "B", position);
		field.addPlayer(playerStatic);

		position = context.getBean(Position.class);
		position.setRow(4);
		position.setColumn(4);
		Player player = context.getBean(Player.class, "A", position);
		game.start();

		player.moveLeft();
		player.moveRight();
		player.moveUp();
		player.moveDown();
		
		assertEquals(4, player.getPosition().getRow());
		assertEquals(4, player.getPosition().getColumn());
		assertEquals("A", player.toString());
	}
	
	@Test
	public void testMovingRandomly() throws InvalidPositionException, GameAlreadyStarted, TooMuchPlayers{
		Position position = context.getBean(Position.class);
		position.setRow(0);
		position.setColumn(0);
		Player playerStatic = context.getBean(Player.class, "B", position);
		field.addPlayer(playerStatic);

		position = context.getBean(Position.class);
		position.setRow(9);
		position.setColumn(5);
		assertEquals("Position [row=9, column=5]", position.toString());
		Player player = context.getBean(Player.class, "A", position);
		game.start();

		for(int i = 0; i< 20 ; i++)
			player.move();
		
		assertEquals("A", player.toString());
	}

}
