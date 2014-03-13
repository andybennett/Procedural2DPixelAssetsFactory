package ajb.factory;

import ajb.domain.Pixel;
import ajb.utils.PixelGridUtils;

public class ConsoleGeneratorFactory {

	private final int ROWS = 19;
	private final int COLS = 19;

	public Pixel[][] create() {

		Pixel[][] grid = createBaseGrid();
		
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.mirrorCopyGridVertically(grid);
		grid = PixelGridUtils.addBorders(grid);		
		PixelGridUtils.setPixelDepth(grid);		
		
		return grid;
	}
	
	private Pixel[][] createBaseGrid() {

		Pixel[][] grid = new Pixel[ROWS][COLS];
		PixelGridUtils.initEmptyGrid(grid, ROWS, COLS);

		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				grid[x][y].value = Pixel.FILLED;
			}
		}

		return grid;
	}
}
