package custom.objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class ImgResize 
{
	public static BufferedImage resize( BufferedImage image, int width, int height )
	{
		BufferedImage resizedImage 	= new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g 				= resizedImage.createGraphics();
		
		g.drawImage( image, 0, 0, width, height, null );
		g.dispose();
		
		return resizedImage;
	} 
}
