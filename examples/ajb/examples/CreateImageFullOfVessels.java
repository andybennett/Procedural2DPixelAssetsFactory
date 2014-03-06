package ajb.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ajb.domain.Pixel;
import ajb.factory.VesselGeneratorFactory;
import ajb.random.RandomInt;
import ajb.utils.ImageUtils;
import ajb.utils.PixelGridUtils;

public class CreateImageFullOfVessels {

	public static void main(String[] args) {

		// get an instance of our vessel factory
		VesselGeneratorFactory factory = new VesselGeneratorFactory();

		// list to hold all the vessels we generate
		List<Pixel[][]> grids = new ArrayList<Pixel[][]>();

		// create X vessels and add to our list
		for (int i = 0; i < 500; i++) {

			Pixel[][] grid = factory.create();

			// add grid to list
			grids.add(grid);
		}

		//Blue - Color.decode("#79ABFF");
		//Red - Color.decode("#D25252");
		
		Color primaryColor = Color.LIGHT_GRAY; // Color.decode("#7FB347");
		Color secondaryColor = Color.decode("#D25252");
		Color tertiaryColor = Color.GRAY;

		// create image
		BufferedImage img = ImageUtils.outputAllToImage(grids, 1600, 1600, null, primaryColor, secondaryColor, tertiaryColor, null);

		// save image
		// replace with the path of wherever you want the image to go - if left
		// as is it will be in the root project folder
		ImageUtils.save(img, "png", "vessels");
	}

}
