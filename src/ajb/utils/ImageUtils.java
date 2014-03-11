package ajb.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
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

	public static BufferedImage outputToImage(Pixel[][] grid, Color borderColor, Color primaryColor, Color secondaryColor) {
		
		if (secondaryColor == null) {
			secondaryColor = Color.decode(ColorUtils.getRandomColour());
		}
		
		BufferedImage baseImg = createImage(grid, borderColor, primaryColor, secondaryColor);
		BufferedImage layer1Img = createImage(grid, borderColor, primaryColor, secondaryColor);
		
		GaussianFilter filter = new GaussianFilter();
		filter.setRadius(12f);
		baseImg = filter.filter(baseImg, null);
		
		BufferedImage result = blend(baseImg, layer1Img);

		filter.setRadius(1.2f);
		result = filter.filter(result, null);
		
		return result;
	}
	
	public static BufferedImage createImage(Pixel[][] grid, Color borderColor, Color primaryColor, Color secondaryColor) {

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		int scaleFactor = 1;

		BufferedImage img = gc.createCompatibleImage(grid[0].length * scaleFactor, grid.length * scaleFactor, BufferedImage.TYPE_INT_ARGB);

		Graphics2D gr = (Graphics2D) img.getGraphics();

		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

					gr.setColor(ColorUtils.lighter(primaryColor, grid[r][c].depth * 0.05 > 6 ? 6 : grid[r][c].depth * 0.05));
					gr.fillRect(c * scaleFactor, r * scaleFactor, scaleFactor, scaleFactor);

				} else if (grid[r][c].value == Pixel.SECONDARY) {

					gr.setColor(ColorUtils.lighter(secondaryColor, grid[r][c].depth * 0.05));
					gr.fillRect(c * scaleFactor, r * scaleFactor, scaleFactor, scaleFactor);
				}
			}
		}

		gr.dispose();

		return img;
	}

	public static BufferedImage outputAllToImage(List<Pixel[][]> grids, int width, int height, Color borderColor, Color primaryColor, Color secondaryColor) {

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

			BufferedImage vesselImg = outputToImage(grid, borderColor, primaryColor, secondaryColor);

			if (x + (vesselImg.getWidth() + 10) > width) {
				x = 10;
				y += maxYForLine + 10;
				maxYForLine = 0;
			}

			if (y + (vesselImg.getHeight() + 10) > height) {
				continue;
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

	/**
	 * Blend the contents of two BufferedImages according to a specified weight.
	 * 
	 * @param bi1
	 *            first BufferedImage
	 * @param bi2
	 *            second BufferedImage
	 * @param weight
	 *            the fractional percentage of the first image to keep
	 * 
	 * @return new BufferedImage containing blended contents of BufferedImage
	 *         arguments
	 */

	public static BufferedImage blend(BufferedImage bi1, BufferedImage bi2) {
		if (bi1 == null)
			throw new NullPointerException("bi1 is null");

		if (bi2 == null)
			throw new NullPointerException("bi2 is null");

		int width = bi1.getWidth();
		if (width != bi2.getWidth())
			throw new IllegalArgumentException("widths not equal");

		int height = bi1.getHeight();
		if (height != bi2.getHeight())
			throw new IllegalArgumentException("heights not equal");

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D gr = (Graphics2D) img.getGraphics();

		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	
		
		Composite oldcomp = gr.getComposite();
		// draw first image fully opaque
		gr.drawImage(bi1, 0, 0, null);
		// change opacity of second image after each call to repaint()
		gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		gr.drawImage(bi2, 0, 0, null);
		gr.setComposite(oldcomp);
		
		gr.dispose();

		return img;
	}
}
