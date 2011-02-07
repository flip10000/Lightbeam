package core;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GameEditor extends GameObjects implements IGameEditor 
{
	protected JFrame frame						= new JFrame();

	public GameEditor()
	{
		// Fenster schliessen
		this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(WindowEvent winEvt) {
		        // Perhaps ask user if they want to save any unsaved files first.
				GameEditor.this.frame.setVisible( false );
				GameEditor.this.game.closeEditor();
		    }
		});	
	}
	
	public void showEditor() 
	{
		this.frame.setVisible( true );
	}
}
