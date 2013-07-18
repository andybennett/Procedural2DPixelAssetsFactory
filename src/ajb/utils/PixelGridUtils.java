package ajb.utils;

import java.awt.Point;

import ajb.domain.Pixel;
import ajb.random.RandomInt;

public class PixelGridUtils {

	/**
	 * Returns true if the passed in point is within the boundaries of the
	 * passed in grid i.e. in a grid initialise with 10 rows and 10 columns
	 * points with x or y above 10 are not within the grid.
	 * 
	 * @param point {@link Point}
	 * @param grid {@link Pixel}[][]
	 * @return {@link boolean}
	 */
	public static boolean isPointWithinGrid(Point point, Pixel[][] grid) {

		try {
			@SuppressWarnings("unused")
			Pixel pixel = grid[point.x][point.y];
			return true;
		} catch (IndexOutOfBoundsException ioobe) {
			return false;
		}

	}

	/**
	 * Outputs the passed in grid to the console
	 * 
	 * @param grid {@link Pixel}[][]
	 */
	public static void outputGridAsAscii(Pixel[][] grid) {

		for (int x = 0; x < grid.length; x++) {

			StringBuilder strBld = new StringBuilder();

			for (int y = 0; y < grid[0].length; y++) {

				if (grid[x][y].value == Pixel.EMPTY) {
					strBld.append(" ");
				} else if (grid[x][y].value == Pixel.FILLED) {
					strBld.append(".");
				} else if (grid[x][y].value == Pixel.BORDER) {
					strBld.append("x");
				}
			}

			System.out.println(strBld.toString());
		}
	}

	/**
	 * Simply populates the passed in grid with {@link Pixel} objects
	 * 
	 * @param grid {@link Pixel}[][]
	 * @param rows {@link int}
	 * @param cols {@link int}
	 */
	public static void initEmptyGrid(Pixel[][] grid, int rows, int cols) {

		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				grid[x][y] = new Pixel();
			}
		}
	}

	/**
	 * Takes the passed in grid and returns a grid that has been floored i.e.
	 * reduced so that rows and column with no filled pixels on them are removed
	 * 
	 * @param grid {@link Pixel}[][]
	 * @return {@link Pixel}[][]
	 */
	public static Pixel[][] floor(Pixel[][] grid) {

		Pixel[][] flooredGrid = null;

		int lastFilledRow = 0;
		int lastFilledCol = 0;
		int firstFilledRow = grid.length;
		int firstFilledColumn = grid[0].length;

		for (int r = 0; r < grid.length; r++) {
			boolean empty = true;

			for (int c = 0; c < grid[0].length; c++) {
				boolean colEmpty = true;

				if (grid[r][c].value != Pixel.EMPTY) {
					if (firstFilledRow > r) {
						firstFilledRow = r;
					}

					if (firstFilledColumn > c) {
						firstFilledColumn = c;
					}

					empty = false;
					colEmpty = false;
				}

				if (!colEmpty && c > lastFilledCol) {
					lastFilledCol = c;
				}
			}

			if (!empty) {
				lastFilledRow = r;
			}
		}

		flooredGrid = new Pixel[lastFilledRow - (firstFilledRow - 1)][lastFilledCol - (firstFilledColumn - 1)];

		int newRow = 0;

		for (int r = firstFilledRow; r < lastFilledRow + 1; r++) {
			int newCol = 0;

			for (int c = firstFilledColumn; c < lastFilledCol + 1; c++) {
				flooredGrid[newRow][newCol] = grid[r][c];
				newCol++;
			}

			newRow++;
		}

		return flooredGrid;
	}

	/**
	 * Takes the passed in grid and returns a grid that contains the original
	 * plus a mirrored copy
	 * 
	 * @param halfGrid {@link Pixel}[][]
	 * @return {@link Pixel}[][]
	 */
	public static Pixel[][] mirrorCopyGridHorizontally(Pixel[][] halfGrid) {

		int rows = halfGrid.length;
		int cols = (halfGrid[0].length * 2);

		Pixel[][] fullGrid = new Pixel[rows][cols];

		// Copy left to right
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < (cols / 2); c++) {
				fullGrid[r][c] = halfGrid[r][c];
				fullGrid[r][(cols - 1) - c] = halfGrid[r][c];
			}
		}

		return fullGrid;
	}

	/**
	 * Takes the passed in grid extends it by 2 then puts a border pixel next to
	 * every edge pixel i.e. pixels that do not have another filled pixel next
	 * to them
	 * 
	 * @param grid {@link Pixel}[][]
	 * @return {@link Pixel}[][]
	 */
	public static Pixel[][] addBorders(Pixel[][] grid) {

		Pixel[][] gridWithBorders = PixelGridUtils.extendGrid(grid, 2);

		for (int r = 0; r < gridWithBorders.length; r++) {
			for (int c = 0; c < gridWithBorders[0].length; c++) {
				if (gridWithBorders[r][c].value == Pixel.FILLED) {

					// Top
					if (gridWithBorders[r == 0 ? 0 : r - 1][c].value != Pixel.FILLED) {
						gridWithBorders[r == 0 ? 0 : r - 1][c].value = Pixel.BORDER;
					}

					// Left
					if (gridWithBorders[r][c == 0 ? 0 : c - 1].value != Pixel.FILLED) {
						gridWithBorders[r][c == 0 ? 0 : c - 1].value = Pixel.BORDER;
					}

					// Right
					if (gridWithBorders[r][c == (gridWithBorders[0].length / 2) - 1 ? (gridWithBorders[0].length / 2) - 1 : c + 1].value != Pixel.FILLED) {
						gridWithBorders[r][c == (gridWithBorders[0].length / 2) - 1 ? (gridWithBorders[0].length / 2) - 1 : c + 1].value = Pixel.BORDER;
					}

					// Bottom
					if (gridWithBorders[r == gridWithBorders.length - 1 ? gridWithBorders.length - 1 : r + 1][c].value != Pixel.FILLED) {
						gridWithBorders[r == gridWithBorders.length - 1 ? gridWithBorders.length - 1 : r + 1][c].value = Pixel.BORDER;
					}
				}
			}
		}

		return gridWithBorders;
	}

	/**
	 * Takes the passed in grid and extends it by the passed in amount
	 * 
	 * @param grid {@link Pixel}[][]
	 * @param extendAmount {@link int}
	 * @return {@link Pixel}[][]
	 */
	public static Pixel[][] extendGrid(Pixel[][] grid, int extendAmount) {
		Pixel[][] extendedGrid = new Pixel[grid.length + extendAmount][grid[0].length + extendAmount];
		initEmptyGrid(extendedGrid, grid.length + extendAmount, grid[0].length + extendAmount);

		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				extendedGrid[r + (extendAmount / 2)][c + (extendAmount / 2)] = grid[r][c];
			}
		}

		return extendedGrid;
	}

	/**
	 * Loops through each pixel in the grid and works out if they are surrounded
	 * by filled pixels i.e. a straight path through other pixels until it hits a 
	 * pixel of value Pixel.FILLED
	 * 
	 * @param grid {@link Pixel}[][]
	 */
	public static void fillEmptySurroundedPixelsInGrid(Pixel[][] grid) {

		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {

				if (grid[r][c].value == Pixel.EMPTY) {

					boolean filledPixelAbove = false;
					boolean filledPixelBelow = false;
					boolean filledPixelOnTheLeft = false;
					boolean filledPixelOnTheRight = false;

					for (int r1 = r - 1; r1 > 0; r1--) {
						if (grid[r1][c].value == Pixel.FILLED) {
							filledPixelAbove = true;
							break;
						}
					}

					for (int r1 = r + 1; r1 < grid.length; r1++) {
						if (grid[r1][c].value == Pixel.FILLED) {
							filledPixelBelow = true;
							break;
						}
					}

					for (int c1 = c - 1; c1 > 0; c1--) {
						if (grid[r][c1].value == Pixel.FILLED) {
							filledPixelOnTheLeft = true;
							break;
						}
					}

					for (int c1 = c + 1; c1 < grid[0].length; c1++) {
						if (grid[r][c1].value == Pixel.FILLED) {
							filledPixelOnTheRight = true;
							break;
						}
					}

					if (filledPixelAbove && filledPixelBelow && filledPixelOnTheLeft && filledPixelOnTheRight) {
						grid[r][c].value = Pixel.SECONDARY;
					}
				}
			}
		}
	}
	
	/**
	 * Loops through each pixel in the grid and works out if they are surrounded
	 * by filled pixels i.e. a straight path through other pixels until it hits a 
	 * pixel of value Pixel.FILLED without going over any pixels with value Pixel.EMPTY
	 * 
	 * @param grid {@link Pixel}[][]
	 */	
	public static void addNoiseToFlatPixels(Pixel[][] grid) {

		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {

				if (grid[r][c].value == Pixel.SECONDARY) {

					boolean filledPixelAbove = false;
					boolean filledPixelBelow = false;
					boolean filledPixelOnTheLeft = false;
					boolean filledPixelOnTheRight = false;

					for (int r1 = r - 1; r1 > 0; r1--) {
						if (grid[r1][c].value == Pixel.EMPTY) {
							filledPixelAbove = false;
							break;
						} else if (grid[r1][c].value == Pixel.FILLED) {
							filledPixelAbove = true;
							break;
						}
					}

					for (int r1 = r + 1; r1 < grid.length; r1++) {
						if (grid[r1][c].value == Pixel.EMPTY) {
							filledPixelBelow = false;
							break;
						} else if (grid[r1][c].value == Pixel.FILLED) {
							filledPixelBelow = true;
							break;
						}
					}

					for (int c1 = c - 1; c1 > 0; c1--) {
						if (grid[r][c1].value == Pixel.EMPTY) {
							filledPixelOnTheLeft = false;
							break;
						} else if (grid[r][c1].value == Pixel.FILLED) {
							filledPixelOnTheLeft = true;
							break;
						}
					}

					for (int c1 = c + 1; c1 < grid[0].length; c1++) {
						if (grid[r][c1].value == Pixel.EMPTY) {
							filledPixelOnTheLeft = false;
							break;
						} else if (grid[r][c1].value == Pixel.FILLED) {
							filledPixelOnTheRight = true;
							break;
						}
					}

					if (filledPixelAbove && filledPixelBelow && filledPixelOnTheLeft && filledPixelOnTheRight) {
						
						grid[r][c].value = Pixel.TERTIARY;
						
						int random = RandomInt.anyRandomIntRange(1, 100);
						
						if (random < 10) {
							grid[r][c].value = Pixel.BORDER;
						} else if (random > 80) {
							grid[r][c].value = Pixel.FILLED;
						}
					}
				}
			}
		}
	}
}
