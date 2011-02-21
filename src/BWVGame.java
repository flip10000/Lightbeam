import core.Core;

// Startklasse des Projekts "Lightbeam"
// mit Erweiterbarkeit weiterer Spiele:
public class BWVGame 
{
	public static void main( String args[] )
	{
		// Erzeugen des GameBuilders ( Screenerhintergrund kann individuell angepast werden):
		Core gameCore	= new Core( "./src/fx/Game/screener.gif" );
		// Spielepackage hinzufügen:
		gameCore.addGame( "Lightbeam" );	
		// GameBuilder starten:
		gameCore.load();
	}
}