package lightbeam.tiles;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.tilefactory.AbstractTileSet;
import custom.objects.ImgResize;



public class TileBuilding extends AbstractTileSet
{
	public TileBuilding() throws IOException	
	{
		super( "building", ImgResize.resize( ImageIO.read( new File( "./src/fx/Tiles/tile_building.png" ) ), 32, 32 ) );
	}
}
