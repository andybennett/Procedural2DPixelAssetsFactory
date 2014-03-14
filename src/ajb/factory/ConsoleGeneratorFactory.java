package ajb.factory;

import java.awt.Point;

import ajb.domain.Pixel;
import ajb.random.RandomInt;
import ajb.utils.PixelGridUtils;

public class ConsoleGeneratorFactory {

	private final int ROWS = 9;
	private final int COLS = 9;

	public Pixel[][] create() {

		Pixel[][] grid = createBaseGrid();

		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.mirrorCopyGridVertically(grid);
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.addBorders(grid);
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

		int noOfSecondaryPixels = 0;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y].value == Pixel.FILLED) {
					if (grid[x - 1][y].value == Pixel.EMPTY || 
							grid[x + 1][y].value == Pixel.EMPTY || 
							grid[x][y - 1].value == Pixel.EMPTY || 
							grid[x][y + 1].value == Pixel.EMPTY) {
						return false;
					}
				} else if (grid[x][y].value == Pixel.SECONDARY) {
					noOfSecondaryPixels++;
					
					if (grid[x - 1][y].value == Pixel.EMPTY || 
							grid[x + 1][y].value == Pixel.EMPTY || 
							grid[x][y - 1].value == Pixel.EMPTY || 
							grid[x][y + 1].value == Pixel.EMPTY) {
						return false;
					}
				}
			}
		}

		if (noOfSecondaryPixels == 0) {
			return false;
		}		
		
		return result;
	}
	
	private Pixel[][] createBaseGrid() {

		Pixel[][] grid = new Pixel[ROWS][COLS];
		PixelGridUtils.initEmptyGrid(grid, ROWS, COLS);

		Point point = new Point(ROWS -1, COLS - 1);

		int steps = 5;
		int subSteps = 10;

		for (int i = 0; i < steps; i++) {

			if (point == null) {
				// down top
				for (int x = ROWS - 1; x > 0; x--) {
					// left to right
					for (int y = 0; y < COLS; y++) {
						if (grid[x][y].value == Pixel.FILLED) {
							point = new Point(x, y);
						}
					}
				}
			}

			for (int y = 0; y < subSteps; y++) {
				point = processPoint(point, grid);
			}

			point = null;
		}

		return grid;
	}

	private Point processPoint(Point point, Pixel[][] grid) {

		if (grid[point.x][point.y].value == Pixel.EMPTY) {
			grid[point.x][point.y].value = Pixel.FILLED;
			grid[point.y][point.x].value = Pixel.FILLED;
		}

		return PixelGridUtils.getRandomAdjacentPoint(point, grid);
	}
}
