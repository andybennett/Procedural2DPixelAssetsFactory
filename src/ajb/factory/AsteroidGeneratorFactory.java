package ajb.factory;

import java.awt.Point;

import ajb.domain.Pixel;
import ajb.enums.AssetSize;
import ajb.utils.PixelGridUtils;

public class AsteroidGeneratorFactory {

	private int rows = 0;
	private int cols = 0;

	public Pixel[][] create(AssetSize size) {

		if (size.equals(AssetSize.RANDOM)) {
			rows = 300;
			cols = 300;
		} else if (size.equals(AssetSize.SMALL)) {
			rows = 100;
			cols = 100;
		} else if (size.equals(AssetSize.MEDIUM)) {
			rows = 200;
			cols = 200;
		} else if (size.equals(AssetSize.LARGE)) {
			rows = 300;
			cols = 300;
		}		
		
		Pixel[][] grid = createBaseGrid(size);
		grid = PixelGridUtils.floor(grid);
		grid = PixelGridUtils.addBorders(grid);
		grid = PixelGridUtils.floor(grid);
		PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
		PixelGridUtils.setPixelDepth(grid);

		if (validateGrid(grid)) {
			return grid;
		} else {
			return create(size);
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

		return result;
	}

	private Pixel[][] createBaseGrid(AssetSize size) {

		Pixel[][] grid = new Pixel[rows][cols];
		PixelGridUtils.initEmptyGrid(grid, rows, cols);

		Point point = new Point(rows / 2, cols / 2);

		int steps = calculateNoOfSteps(size);
		int subSteps = calculateNoOfSubSteps(size);

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
	
	private int calculateNoOfSteps(AssetSize size) {

		int result = 0;

		if (size.equals(AssetSize.SMALL)) {
			result = 50;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 80;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 120;
		}

		return result;
	}

	private int calculateNoOfSubSteps(AssetSize size) {

		int result = 1;

		if (size.equals(AssetSize.SMALL)) {
			result = 50;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 130;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 180;
		}

		return result;
	}

}
