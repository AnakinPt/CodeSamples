package pt.com.hugodias.sample.game.entities;

import javax.annotation.Resource;

import pt.com.hugodias.sample.game.exceptions.InvalidPositionException;

public class Position {

	private int row;
	private int column;

	@Resource(name = "max_row")
	private transient int MAX_ROW;
	@Resource(name = "max_column")
	private transient int MAX_COLUMN;

	public Position() {

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) throws InvalidPositionException {
		if (row < 0 || row >= MAX_ROW)
			throw new InvalidPositionException();
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) throws InvalidPositionException {
		if (column < 0 || column >= MAX_COLUMN)
			throw new InvalidPositionException();
		this.column = column;
	}

	public void move() {
		boolean possible = false;
		while (!possible) {
			int random = (int) Math.ceil(Math.random() * 4);
			switch (random) {
			case 0:
				possible = moveUp();
				break;
			case 1:
				possible = moveDown();
				break;
			case 2:
				possible = moveLeft();
				break;
			case 3:
				possible = moveRight();
				break;
			}
		}
	}

	public boolean moveLeft() {
		if (column > 1) {
			column--;
			return true;
		} else {
			return false;
		}
	}

	public boolean moveRight() {
		if (column < MAX_COLUMN) {
			column++;
			return true;
		} else {
			return false;
		}
	}

	public boolean moveDown() {
		if (row < MAX_ROW) {
			row++;
			return true;
		}
		return false;
	}

	public boolean moveUp() {
		if (row > 1) {
			row--;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Position [row=" + row + ", column=" + column + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public boolean isNear(Position position) {
		double result = Math.abs(position.getRow() - this.row) + Math.abs(position.getColumn() - this.column);
		return result <= 2;
	}
}
