package lightbeam.editor;

import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class Toolbar 
{
	private Editor editor					= null;
	private JToolBar toolBar				= null;
	private JButton btnSave					= null;
	private JButton btnNew					= null;
	private JButton btnOpen 				= null;
	private JButton btnClose				= null;

	public Toolbar( Editor editor ) 
	{
		this.editor				= editor;
        this.toolBar			= new JToolBar( "Toolbar", JToolBar.HORIZONTAL );
        this.toolBar.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT );        
        this.toolBar.setLayout( null );
        this.toolBar.setPreferredSize( new Dimension( editor.getFrame().getWidth(), 38 ) );
        
		// ToolBar füllen
		ImageIcon imgNew		= new ImageIcon( "src/fx/Toolbar/newMap.png" );
		ImageIcon imgOpen 		= new ImageIcon( "src/fx/Toolbar/open.png" );
		ImageIcon imgSave 		= new ImageIcon( "src/fx/Toolbar/save.png" );
		ImageIcon imgClose 		= new ImageIcon( "src/fx/Toolbar/close.png" );		
        
		this.btnNew 			= new JButton( imgNew );
		this.btnOpen 			= new JButton( imgOpen );
		this.btnSave 			= new JButton( imgSave );
		this.btnClose 			= new JButton( imgClose );

		this.btnNew.setBounds( new Rectangle( 16, 3, 32, 32 ) );
		this.btnOpen.setBounds( new Rectangle( this.btnNew.getBounds().x + this.btnNew.getBounds().width, 3, 32, 32 ) );
		this.btnSave.setBounds( new Rectangle( this.btnOpen.getBounds().x +this.btnOpen.getBounds().width, 3, 32, 32 ) );
		this.btnClose.setBounds( new Rectangle( this.toolBar.getPreferredSize().width - 50, 3, 32, 32 ) );

		this.btnNew.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnOpen.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnClose.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnSave.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		
		this.btnNew.setToolTipText( "Neues Spielfeld anlegen!" );
		this.btnOpen.setToolTipText( "Gespeichertes Spielfeld öffnen!" );
		this.btnSave.setToolTipText( "Aktuelles Spielfeld speichern!" );
		this.btnClose.setToolTipText( "Editor schließen!" );
		
		this.toolBar.add( this.btnNew );
		this.toolBar.add( this.btnOpen );
		this.toolBar.add( this.btnSave );
		this.toolBar.add( this.btnClose );
		
		// Neue map ertellen:
		this.btnNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.editor.newMap();
			}
		});		
		
		// Map laden/öffnen:
		this.btnOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.editor.loadMap();
			}
		});

		// Map speichern:
		this.btnSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.editor.saveMap();
			}
		});

		// Editor schließen:
		this.btnClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.editor.closeEditor();
			}
		});
        
	}
	
	public void refreshSize()
	{
		System.out.println("x");
        this.toolBar.setPreferredSize( new Dimension( editor.getFrame().getWidth(), 38 ) );
		this.btnClose.setBounds( new Rectangle( this.toolBar.getPreferredSize().width - 50, 3, 32, 32 ) );
	}
	
	public JToolBar get() { return this.toolBar; }
}
