package ajb.factory;

import java.awt.Point;

import ajb.domain.Pixel;
import ajb.random.RandomInt;
import ajb.utils.PixelGridUtils;

public class TileGeneratorFactory {

	private final int ROWS = 19;
	private final int COLS = 19;

	public Pixel[][] create() {

		Pixel[][] grid = createBaseGrid();
		
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.mirrorCopyGridVertically(grid);
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

		int noOfSecondaryPixels = 0;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y].value == Pixel.SECONDARY) {
					noOfSecondaryPixels++;
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

		int steps = 15;
		int subSteps = 50;

		for (int i = 0; i < steps; i++) {

			Point point = new Point(ROWS -1, COLS - 1);

			for (int y = 0; y < subSteps; y++) {
				point = processPoint(point, grid);
			}

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
