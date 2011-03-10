import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

import core.Core;



// Startklasse des Projekts "Lightbeam"
// mit Erweiterbarkeit weiterer Spiele:
public class BWVGame 
{
    private static void initLookAndFeel() {
        SynthLookAndFeel lookAndFeel = new SynthLookAndFeel();
      

           try {
           	
           	// SynthLookAndFeel load() method throws a checked exception
           	// (java.text.ParseException) so it must be handled
           	lookAndFeel.load(BWVGame.class.getResourceAsStream("look.xml"), BWVGame.class);
               UIManager.setLookAndFeel(lookAndFeel);
           } 
           
           catch (Exception e) {
               System.err.println("Couldn't get specified look and feel ("
                                  + lookAndFeel
                                  + "), for some reason.");
               System.err.println("Using the default look and feel.");
               e.printStackTrace();
           }
       
   }
    
	public static void main( String args[] )
	{
		//initLookAndFeel();
		// Erzeugen des GameBuilders ( Screenerhintergrund kann individuell angepast werden):
		Core gameCore	= new Core( "./src/fx/Game/screener.gif" );
		// Spielepackage hinzuf�gen:
		gameCore.addGame( "Lightbeam" );
		gameCore.addGame( "Lightbeam" );
		gameCore.addGame( "Lightbeam" );
		gameCore.addGame( "Lightbeam" );
		gameCore.addGame( "Lightbeam" );
		// GameBuilder starten:
		gameCore.load();
	}
}