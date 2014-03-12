package ajb.factory;

import java.awt.Point;

import ajb.domain.Pixel;
import ajb.utils.PixelGridUtils;

public class AsteroidGeneratorFactory {

	private final int ROWS = 300;
	private final int COLS = 300;

	public Pixel[][] create() {

		Pixel[][] grid = createBaseGrid();
		grid = PixelGridUtils.floor(grid);
		grid = PixelGridUtils.addBorders(grid);
		grid = PixelGridUtils.floor(grid);
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

		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y].value == Pixel.FILLED) {
					noOfFilledPixels++;
				} else if (grid[x][y].value == Pixel.SECONDARY) {
					noOfSecondaryPixels++;
				}
			}
		}

		if (noOfSecondaryPixels == 0) {
			result = false;
		}

		if (noOfSecondaryPixels > (noOfFilledPixels / 4)) {
			result = false;
		}

		if (grid.length > 100 || grid[0].length > 100) {
			result = false;
		}

		return result;
	}

	private Pixel[][] createBaseGrid() {

		Pixel[][] grid = new Pixel[ROWS][COLS];
		PixelGridUtils.initEmptyGrid(grid, ROWS, COLS);

		Point point = new Point(ROWS / 2, COLS / 2);

		int steps = 100;
		int subSteps = 100;

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

		return PixelGridUtils.getRandomAdjacentPoint(point, grid);
	}
}
