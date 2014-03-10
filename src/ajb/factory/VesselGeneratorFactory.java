package ajb.factory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ajb.domain.NeighbouringPoint;
import ajb.domain.Pixel;
import ajb.enums.NeighbouringPointDirection;
import ajb.random.RandomInt;
import ajb.utils.PixelGridUtils;

public class VesselGeneratorFactory {

	private final int ROWS = 600;
	private final int COLS = 600;

	public Pixel[][] create() {

		Pixel[][] grid = createBaseGrid();
		addExtras(grid);

		grid = PixelGridUtils.floor(grid);
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.addBorders(grid);
		grid = PixelGridUtils.floor(grid);
		PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
		PixelGridUtils.addNoiseToFlatPixels(grid);	
		PixelGridUtils.setPixelDepth(grid);

		if (validateGrid(grid)) {		
			return grid;
		} else {
			return create();
		}
	}

	private boolean validateGrid(Pixel[][] grid) {
		
		boolean result = true;
		
		int noOfFilledPixels = 0;
		int noOfSecondaryPixels = 0;
		int noOfTertiaryPixels = 0;
		int noOfBorderPixels = 0;
		int noOfEmptyPixels = 0;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y].value == Pixel.FILLED) {
					noOfFilledPixels++;
				} else if (grid[x][y].value == Pixel.SECONDARY) {
					noOfSecondaryPixels++;
				} else if (grid[x][y].value == Pixel.TERTIARY) {
					noOfTertiaryPixels++;
				} else if (grid[x][y].value == Pixel.BORDER) {
					noOfBorderPixels++;
				} else if (grid[x][y].value == Pixel.EMPTY) {
					noOfEmptyPixels++;
				}
			}
		}
		
		//System.out.println("FILLED:" + noOfFilledPixels + " SECONDARY:" + noOfSecondaryPixels+ " TERTIARY:" + noOfTertiaryPixels + " BORDER:" + noOfBorderPixels + " EMPTY:" + noOfEmptyPixels);
		
		if (noOfSecondaryPixels == 0) {
			result = false;
			//System.out.println("REJECTED");
		}
		
		if (noOfSecondaryPixels > (noOfFilledPixels / 4)) {
			result = false;
			//System.out.println("REJECTED");
		}
		
		if (noOfTertiaryPixels > (noOfFilledPixels / 3)) {
			result = false;
			//System.out.println("REJECTED");
		}	
		
		return result;
	}
	
	private Pixel[][] createBaseGrid() {

		Pixel[][] grid = new Pixel[ROWS][COLS];
		PixelGridUtils.initEmptyGrid(grid, ROWS, COLS);

		Point point = new Point(ROWS / 2, COLS - 1);

		int steps = RandomInt.anyRandomIntRange(5, 50);
		int subSteps = RandomInt.anyRandomIntRange(5, 120);

		for (int i = 0; i < steps; i++) {

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

			for (int y = 0; y < subSteps; y++) {
				// now process points randomly starting with one determined from
				// above
				point = processPoint(point, grid);
			}

			point = null;
		}

		return grid;
	}

	private void addExtras(Pixel[][] grid) {

		int steps = RandomInt.anyRandomIntRange(0, 20);
		int subSteps = RandomInt.anyRandomIntRange(5, 50);

		for (int i = 0; i < steps; i++) {
			Point point = PixelGridUtils.getRandomFilledPoint(grid);

			for (int y = 0; y < subSteps; y++) {
				// now process points randomly starting with one determined from
				// above
				point = processPoint(point, grid);
			}
		}
	}

	private Point processPoint(Point point, Pixel[][] grid) {

		if (grid[point.x][point.y].value == Pixel.EMPTY) {
			grid[point.x][point.y].value = Pixel.FILLED;
			
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
			
			if (pointTopValid) {
				grid[point.x - 1][point.y].value = Pixel.FILLED;
			}
			
			if (pointBottomValid) {
				grid[point.x + 1][point.y].value = Pixel.FILLED;
			}
			
			if (pointLeftValid) {
				grid[point.x][point.y - 1].value = Pixel.FILLED;
			}
			
			if (pointRightValid) {
				grid[point.x][point.y + 1].value = Pixel.FILLED;
			}			
		}

		return PixelGridUtils.getRandomNeighbouringPoint(point, grid);
	}
}
