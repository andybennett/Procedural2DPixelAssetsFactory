package ajb.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ajb.domain.Pixel;
import ajb.factory.VesselGeneratorFactory;
import ajb.utils.ImageUtils;
import ajb.utils.PixelGridUtils;

public class CreateImageFullOfVessels {

	public static void main(String[] args) {

		// get an instance of our vessel factory
		VesselGeneratorFactory factory = new VesselGeneratorFactory();

		// list to hold all the vessels we generate
		List<Pixel[][]> grids = new ArrayList<Pixel[][]>();

		factory.ROWS = 500; // height
		factory.COLS = 25; // width
		factory.STEPS = 35; // how many iterations
		factory.SUB_STEPS = 200; // how many steps within each iteration

		// create a 100 vessels and add to our list
		for (int i = 0; i < 500; i++) {
			Pixel[][] grid = factory.create();

			// @TODO Wrap all this up within the factory?
			grid = PixelGridUtils.floor(grid);
			grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
			grid = PixelGridUtils.addBorders(grid);
			grid = PixelGridUtils.floor(grid);
			PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
			PixelGridUtils.addNoiseToFlatPixels(grid);

			// add grid to list
			grids.add(grid);
		}

		Color primaryColor = Color.LIGHT_GRAY; // Color.decode("#7FB347");
		Color secondaryColor = Color.decode("#79ABFF");
		Color tertiaryColor = Color.GRAY;

		// create image
		BufferedImage img = ImageUtils.outputAllToImage(grids, 800, 800, null, primaryColor, secondaryColor, tertiaryColor, null);

		// save image
		// replace with the path of wherever you want the image to go - if left
		// as is it will be in the root project folder
		ImageUtils.save(img, "png", "vessels");
	}

}
