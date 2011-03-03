package lightbeam.editor;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class MapName 
{
	final static Cursor CURSOR_HAND				= new Cursor( Cursor.HAND_CURSOR );
	final static Cursor CURSOR_DEFAULT			= new Cursor( Cursor.DEFAULT_CURSOR );
	
	private JPanel panelMapName		= new JPanel();
	
	private JLabel lblName			= new JLabel( "Kartenname:" );
	private JTextField txtName		= new JTextField();
	private JButton btnApply		= new JButton( "Ok" );
	private Editor editor			= null; 
	private String strName			= null;
	
	public MapName( Editor editor )
	{
		this.editor		= editor;
		
		this.panelMapName.setBorder( new TitledBorder( "Karte:" ) );
		this.panelMapName.setLayout( null );
		
		this.lblName.setBounds( 15, 15, 80, 16 );
		this.txtName.setBounds( 15, 35, 100, 16 );
		this.btnApply.setBounds( 15, 55, 100, 16 );
		
		this.btnApply.setCursor( MapName.CURSOR_HAND );
		
		this.btnApply.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e)
		{ 
			MapName.this.strName	= MapName.this.txtName.getText();

			MapName.this.editor.saveMapName( MapName.this.strName );
		}});
		
		this.panelMapName.add( this.lblName );
		this.panelMapName.add( this.txtName );
		this.panelMapName.add( this.btnApply );
		
		this.panelMapName.setBounds( 5, 5, 128, 80 );	
	}
	
	public void setName( String name ) 
	{
		this.strName = name;
		this.txtName.setText( name );
	}
	
	public String getName()	{ return this.strName; 		}
	public JPanel panel() 	{ return this.panelMapName; }
}
