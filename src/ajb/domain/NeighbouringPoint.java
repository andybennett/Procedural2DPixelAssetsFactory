package ajb.domain;

import java.awt.Point;

import ajb.enums.NeighbouringPointDirection;

public class NeighbouringPoint extends Point {
	public NeighbouringPointDirection direction;

	public NeighbouringPoint(int x, int y, NeighbouringPointDirection direction) {
		super(x, y);
		this.direction = direction;
	}
}
