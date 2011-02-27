package custom.objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//Klasse zum anpassen der Gr��e von Bilder
public final class ImgResize 
{
	/**
	 * 
	 * Die Methode {@code rezise(BufferedImage, int, int)} passt die Gr��e einer Grafik an
	 * 
	 * @param image
	 * 			Das Bild, dessen Gr��e angepasst werden soll
	 * @param width
	 * 			Die Ziel-Breite des Bildes
	 * @param height
	 * 			Die Ziel-H�he des Bildes
	 * @return resizedImage
	 * 			Das Bild in der gew�nschten Gr��e
	 * 
	 */
	public static BufferedImage resize( BufferedImage image, int width, int height )
	{
		BufferedImage resizedImage 		= new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g 					= resizedImage.createGraphics();
		
		g.drawImage( image, 0, 0, width, height, null );
		g.dispose();
		
		return resizedImage;
	}
}
