package pt.com.hugodias.sample.game.exceptions;

public class GameAlreadyStarted extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameAlreadyStarted() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GameAlreadyStarted(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public GameAlreadyStarted(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public GameAlreadyStarted(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public GameAlreadyStarted(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
