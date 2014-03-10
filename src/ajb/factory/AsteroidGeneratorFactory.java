package ajb.factory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ajb.domain.NeighbouringPoint;
import ajb.domain.Pixel;
import ajb.enums.NeighbouringPointDirection;
import ajb.random.RandomInt;
import ajb.utils.PixelGridUtils;

public class AsteroidGeneratorFactory {

	private final int ROWS = 600;
	private final int COLS = 600;

	public Pixel[][] create() {

		Pixel[][] grid = createBaseGrid();

		grid = PixelGridUtils.floor(grid);
		grid = PixelGridUtils.addBorders(grid);
		PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
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

		Point point = new Point(ROWS / 2, COLS / 2);

		int steps = 50;
		int subSteps = 300;

		for (int i = 0; i < steps; i++) {
			for (int y = 0; y < subSteps; y++) {
				point = processPoint(point, grid);
			}
		}

		return grid;
	}

	private Point processPoint(Point point, Pixel[][] grid) {

		if (grid[point.x][point.y].value == Pixel.EMPTY) {
			grid[point.x][point.y].value = Pixel.FILLED;
		}

		return PixelGridUtils.getRandomNeighbouringPoint(point, grid);
	}
}
