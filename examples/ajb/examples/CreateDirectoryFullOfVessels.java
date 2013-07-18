package ajb.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ajb.domain.Pixel;
import ajb.factory.VesselGeneratorFactory;
import ajb.utils.ImageUtils;
import ajb.utils.PixelGridUtils;

public class CreateDirectoryFullOfVessels {

	public static void main(String[] args) {

		// get an instance of our vessel factory
		VesselGeneratorFactory factory = new VesselGeneratorFactory();

		factory.ROWS = 1000; // height
		factory.COLS = 10; // width
		factory.STEPS = 10; // how many iterations
		factory.SUB_STEPS = 100; // how many steps within each iteration

		// create a 100 vessels and add to our list
		for (int i = 0; i < 100; i++) {
			Pixel[][] grid = factory.create();
			grid = PixelGridUtils.floor(grid);
			grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
			grid = PixelGridUtils.addBorders(grid);
			grid = PixelGridUtils.floor(grid);
			grid = PixelGridUtils.extendGrid(grid, 8);
			PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
			
			BufferedImage img = ImageUtils.outputToImage(grid, null, Color.decode("#79ABFF"), Color.decode("#79ABFF"), null, null);
			
			// i.e. c://<directory>//<directory>//<file name prefix>
			ImageUtils.save(img, "png", "<directory path and file name prefix here - must already exist>" + i);
		}
	}

}
