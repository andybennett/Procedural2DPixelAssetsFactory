package ajb.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ajb.domain.Pixel;
import ajb.factory.StationGeneratorFactory;
import ajb.utils.ImageUtils;

public class CreateImageFullOfStations {

	public static void main(String[] args) {

		// get an instance of our factory
		StationGeneratorFactory factory = new StationGeneratorFactory();

		// list to hold all the stations we generate
		List<Pixel[][]> grids = new ArrayList<Pixel[][]>();

		// create X stations and add to our list
		for (int i = 0; i < 15; i++) {

			Pixel[][] grid = factory.create();

			// add grid to list
			grids.add(grid);
		}

		Color primaryColor = Color.decode("#2A2A2A");

		// create image
		BufferedImage img = ImageUtils.outputAllToImage(grids, 1000, 1000, null, primaryColor, null);

		// save image
		// replace with the path of wherever you want the image to go - if left
		// as is it will be in the root project folder
		ImageUtils.save(img, "png", "stations");
	}

}
