package ajb.factory;

import java.awt.Point;

import ajb.domain.Pixel;
import ajb.enums.AssetSize;
import ajb.random.RandomInt;
import ajb.utils.PixelGridUtils;

public class VesselGeneratorFactory {

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
		addExtras(grid, size);

		grid = PixelGridUtils.floor(grid);
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.addBorders(grid);
		grid = PixelGridUtils.floor(grid);
		PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
		PixelGridUtils.addNoiseToFlatPixels(grid);
		PixelGridUtils.setPixelDepth(grid);

		if (validateGrid(grid, size)) {
			return grid;
		} else {
			return create(size);
		}
	}

	private boolean validateGrid(Pixel[][] grid, AssetSize size) {

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

		Point point = new Point(rows / 2, cols - 1);

		int steps = RandomInt.anyRandomIntRange(calculateMinNoOfSteps(size), calculateMaxNoOfSteps(size));
		int subSteps = RandomInt.anyRandomIntRange(calculateMinNoOfSubSteps(size), calculateMaxNoOfSubSteps(size));

		for (int i = 0; i < steps; i++) {

			if (point == null) {
				// we are passed the first step lets find the lowest most pixel
				// that is closest to the middle, and go again from there...

				// top down
				for (int x = 0; x < rows; x++) {
					// left to right
					for (int y = 0; y < cols; y++) {
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

	private void addExtras(Pixel[][] grid, AssetSize size) {

		int steps = RandomInt.anyRandomIntRange(calculateMinNoOfSteps(size) - 10, calculateMaxNoOfSteps(size) - 10);
		int subSteps = RandomInt.anyRandomIntRange(calculateMinNoOfSubSteps(size) - 10, calculateMaxNoOfSubSteps(size) - 10);

		for (int i = 0; i < steps; i++) {
			Point point = PixelGridUtils.getRandomFilledPoint(grid);

			for (int y = 0; y < subSteps; y++) {
				point = processPoint(point, grid);
			}
		}
	}

	private Point processPoint(Point point, Pixel[][] grid) {

		if (grid[point.x][point.y].value == Pixel.EMPTY) {
			grid[point.x][point.y].value = Pixel.FILLED;
		}

		return PixelGridUtils.getRandomAdjacentPoint(point, grid);
	}

	private int calculateMinNoOfSteps(AssetSize size) {

		int result = 0;

		if (size.equals(AssetSize.RANDOM)) {
			result = 5;
		} else if (size.equals(AssetSize.SMALL)) {
			result = 5;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 10;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 20;
		}

		return result;
	}

	private int calculateMaxNoOfSteps(AssetSize size) {

		int result = 0;

		if (size.equals(AssetSize.RANDOM)) {
			result = 50;
		} else if (size.equals(AssetSize.SMALL)) {
			result = 15;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 30;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 50;
		}

		return result;
	}

	private int calculateMinNoOfSubSteps(AssetSize size) {

		int result = 1;

		if (size.equals(AssetSize.RANDOM)) {
			result = 5;
		} else if (size.equals(AssetSize.SMALL)) {
			result = 5;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 10;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 15;
		}

		return result;
	}

	private int calculateMaxNoOfSubSteps(AssetSize size) {

		int result = 0;

		if (size.equals(AssetSize.RANDOM)) {
			result = 50;
		} else if (size.equals(AssetSize.SMALL)) {
			result = 30;
		} else if (size.equals(AssetSize.MEDIUM)) {
			result = 40;
		} else if (size.equals(AssetSize.LARGE)) {
			result = 50;
		}

		return result;
	}
}
