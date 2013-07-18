package ajb.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import ajb.domain.Pixel;
import ajb.random.RandomColor;

public class ImageUtils {

	public static BufferedImage outputToImage(Pixel[][] grid, Color borderColor, Color primaryColor, Color secondaryColor,
			Color tertiaryColor, Color emptyColor) {

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		int scaleFactor = 1;

		BufferedImage img = gc.createCompatibleImage(grid[0].length * scaleFactor, grid.length * scaleFactor, BufferedImage.TYPE_INT_ARGB);

		Graphics2D gr = (Graphics2D) img.getGraphics();

		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Color firstColor = null;

		if (primaryColor != null) {
			firstColor = primaryColor;
		} else {
			firstColor = RandomColor.anyRandomColor();
		}

		Color secondColor = null;

		if (secondaryColor != null) {
			secondColor = secondaryColor;
		} else {
			secondColor = RandomColor.anyRandomColor();
		}

		Color thirdColor = null;

		if (tertiaryColor != null) {
			thirdColor = tertiaryColor;
		} else {
			thirdColor = RandomColor.anyRandomColor();
		}

		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {

				if (grid[r][c].value == Pixel.BORDER) {

					if (borderColor == null) {
						gr.setColor(Color.BLACK);
					} else {
						gr.setColor(borderColor);
					}

					gr.fillRect(c * scaleFactor, r * scaleFactor, scaleFactor, scaleFactor);

				} else if (grid[r][c].value == Pixel.FILLED) {

					gr.setColor(firstColor);
					gr.fillRect(c * scaleFactor, r * scaleFactor, scaleFactor, scaleFactor);

				} else if (grid[r][c].value == Pixel.SECONDARY) {

					gr.setColor(secondColor);
					gr.fillRect(c * scaleFactor, r * scaleFactor, scaleFactor, scaleFactor);
				} else if (grid[r][c].value == Pixel.TERTIARY) {

					gr.setColor(thirdColor);
					gr.fillRect(c * scaleFactor, r * scaleFactor, scaleFactor, scaleFactor);
				} else if (grid[r][c].value == Pixel.EMPTY) {

					if (emptyColor != null) {
						gr.setColor(emptyColor);
						gr.fillRect(c * scaleFactor, r * scaleFactor, scaleFactor, scaleFactor);
					}
				}
			}
		}

		gr.dispose();

		return img;
	}

	public static BufferedImage outputAllToImage(List<Pixel[][]> grids, int width, int height, Color borderColor, Color primaryColor,
			Color secondaryColor, Color tertiaryColor, Color emptyColor) {

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		BufferedImage img = gc.createCompatibleImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D gr = (Graphics2D) img.getGraphics();

		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fill background
		gr.setColor(Color.decode("#1E1E1E"));
		gr.fillRect(0, 0, width, height);

		int x = 10;
		int y = 10;
		int maxYForLine = 0;

		for (Pixel[][] grid : grids) {

			BufferedImage vesselImg = outputToImage(grid, borderColor, primaryColor, secondaryColor, tertiaryColor, emptyColor);

			if (x + (vesselImg.getWidth() + 10) > width) {
				x = 10;
				y += maxYForLine + 10;
				maxYForLine = 0;
			}

			if (y + (vesselImg.getHeight() + 10) > height) {
				break;
			}

			gr.drawImage(vesselImg, x, y, null);

			x += vesselImg.getWidth() + 10;

			if (vesselImg.getHeight() > maxYForLine) {
				maxYForLine = vesselImg.getHeight();
			}
		}

		gr.dispose();

		return img;
	}

	public static void save(BufferedImage image, String ext, String fileName) {

		File file = new File(fileName + "." + ext);

		try {
			ImageIO.write(image, ext, file);
		} catch (IOException e) {
			System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
		}
	}

}
