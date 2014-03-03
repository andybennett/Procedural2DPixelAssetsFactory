package ajb.factory;

import java.awt.Point;

import ajb.domain.NeighbouringPoint;
import ajb.domain.Pixel;
import ajb.enums.NeighbouringPointDirection;
import ajb.random.RandomInt;
import ajb.utils.PixelGridUtils;

public class VesselGeneratorFactory {

	// @TODO should really be private and have getter and setters
	public int ROWS = 1000;
	public int COLS = 20;
	public int STEPS = 10;
	public int SUB_STEPS = 100;

	public Pixel[][] create() {

		Pixel[][] grid = new Pixel[ROWS][COLS];
		PixelGridUtils.initEmptyGrid(grid, ROWS, COLS);

		Point point = new Point(0, COLS - 1);

		for (int i = 0; i < STEPS; i++) {

			if (point == null) {
				// we are passed the first step lets find the lowest most pixel
				// that is closest to the middle, and go again from there...

				// top down
				for (int x = 0; x < ROWS; x++) {
					// left to right
					for (int y = 0; y < COLS; y++) {
						if (grid[x][y].value == Pixel.FILLED) {
							point = new Point(x, y);
						}
					}
				}
			}

			for (int y = 0; y < SUB_STEPS; y++) {
				// now process points randomly starting with one determined from
				// above
				point = processPoint(point, grid);
			}

			point = null;
		}
		
		for (int i = 0; i < STEPS; i++) {
			point = getRandomFilledPoint(grid);
			
			for (int y = 0; y < SUB_STEPS; y++) {
				// now process points randomly starting with one determined from
				// above
				point = processPoint(point, grid);
			}			
		}
		

		return grid;
	}

	private Point processPoint(Point point, Pixel[][] grid) {

		if (grid[point.x][point.y].value == Pixel.EMPTY) {
			grid[point.x][point.y].value = Pixel.FILLED;
		}

		return getRandomNeighbouringPoint(point, grid);
	}

	private NeighbouringPoint getRandomNeighbouringPoint(Point point, Pixel[][] grid) {

		// top
		NeighbouringPoint pointTop = new NeighbouringPoint(point.x - 1, point.y, NeighbouringPointDirection.TOP);

		// bottom
		NeighbouringPoint pointBottom = new NeighbouringPoint(point.x + 1, point.y, NeighbouringPointDirection.BOTTOM);

		// left
		NeighbouringPoint pointLeft = new NeighbouringPoint(point.x, point.y - 1, NeighbouringPointDirection.LEFT);

		// right
		NeighbouringPoint pointRight = new NeighbouringPoint(point.x, point.y + 1, NeighbouringPointDirection.RIGHT);

		boolean pointTopValid = PixelGridUtils.isPointWithinGrid(pointTop, grid);
		boolean pointBottomValid = PixelGridUtils.isPointWithinGrid(pointBottom, grid);
		boolean pointLeftValid = PixelGridUtils.isPointWithinGrid(pointLeft, grid);
		boolean pointRightValid = PixelGridUtils.isPointWithinGrid(pointRight, grid);

		int noOfValidNeighbours = 0;

		if (pointTopValid) {
			noOfValidNeighbours++;
		}

		if (pointBottomValid) {
			noOfValidNeighbours++;
		}

		if (pointLeftValid) {
			noOfValidNeighbours++;
		}

		if (pointRightValid) {
			noOfValidNeighbours++;
		}

		NeighbouringPoint[] neighbours = new NeighbouringPoint[noOfValidNeighbours + 1];

		int index = 0;
		if (pointTopValid) {
			neighbours[index] = pointTop;
			index++;
		}

		if (pointBottomValid) {
			neighbours[index] = pointBottom;
			index++;
		}

		if (pointLeftValid) {
			neighbours[index] = pointLeft;
			index++;
		}

		if (pointRightValid) {
			neighbours[index] = pointRight;
			index++;
		}

		// go to a random neighbour
		NeighbouringPoint newPoint = null;

		while (newPoint == null) {
			int ri = RandomInt.anyRandomIntRange(0, neighbours.length - 1);

			if (neighbours[ri] != null) {
				newPoint = neighbours[ri];
			}
		}

		return newPoint;
	}
	
	private Point getRandomFilledPoint(Pixel[][] grid) {
		
		Point point = null;
		
		while (point == null) {
			
			int x = RandomInt.anyRandomIntRange(1, ROWS - 1);
			int y = RandomInt.anyRandomIntRange(1, COLS - 1);
			
			Pixel possiblePixel = grid[x][y];
			
			if (possiblePixel.value == Pixel.FILLED) {
				point = new Point();
				point.x = x;
				point.y = y;
			}
		}
		
		return point;
		
	}
}
