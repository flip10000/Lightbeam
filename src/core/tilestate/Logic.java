package core.tilestate;

import java.util.ArrayList;
import java.util.List;

public class Logic {
	
	int cols, rows;
	Tile[][] tiles, tiles_copy; 
		
	public Tile[][] copyTileArray(boolean edit_mode){
		Tile[][] output = new Tile[rows][cols];
		for (int y = 0; y < rows; y++){
			for (int x = 0; x < cols; x++){
				output[y][x] = new Tile(this.tiles[y][x].parent(),this.tiles[y][x].row(),this.tiles[y][x].col());
				output[y][x].type(this.tiles[y][x].type());
				output[y][x].strength(this.tiles[y][x].strength());
				
				if (edit_mode && "beam".equals(this.tiles[y][x].type())){
					output[y][x].type("field");
					output[y][x].parent(null);
				}
			}
		}
		return output;
	}
	
	public boolean tileArrayComplete(Tile[][] tiles_array, int cols, int rows){
	boolean complete = true;
	for (int y = 0; y < rows; y++){
		for (int x = 0; x < cols; x++){
			if ("field".equals(tiles_array[y][x].type())){
				complete = false;
			}
		}
	}
	return complete;
	}
	
	
	
	
	
	
	
	
	public void switchToBeam(int x, int y,Tile beamsource){
		this.tiles_copy[y][x].type("beam");
		if("beam".equals(beamsource.type())){
			this.tiles_copy[y][x].parent(beamsource.parent());
		} else {
			this.tiles_copy[y][x].parent(beamsource);
		}
	}

	
	public boolean tileArrayUseful(Tile[][] tiles, int cols, int rows, boolean edit_mode){
		this.rows = rows;
		this.cols = cols;
		this.tiles = tiles;
		this.tiles_copy = this.copyTileArray(edit_mode);
		
		boolean useful = true;
				
		while(useful){
			useful = false;
		
			for (int y = 0; y < rows; y++){
				for (int x = 0; x < cols; x++){
					if ("field".equals(this.tiles_copy[y][x].type())){
						// each direction's next beamsource
						List<Tile> availableBeamsources = this.getAvailableBeamsources(y,x);
						// filtered to logicly possible beamsources
						List<Tile> possibleBeamsources = this.getPossibleBeamsources(availableBeamsources,y,x);
						if(possibleBeamsources.size() == 1){
							
							// beam einsetzen/ziehen
							if (x == possibleBeamsources.get(0).col()){
								if (y > possibleBeamsources.get(0).row()){
									for (int y_tmp = possibleBeamsources.get(0).row()+1; y_tmp <= y; y_tmp++){
										this.switchToBeam(x,y_tmp,possibleBeamsources.get(0));
									}
								} else {
									for (int y_tmp = possibleBeamsources.get(0).row()-1; y_tmp >= y; y_tmp--){
										this.switchToBeam(x,y_tmp,possibleBeamsources.get(0));
									}
								}
							} else {
								if (x > possibleBeamsources.get(0).col()){
									for (int x_tmp = possibleBeamsources.get(0).col()+1; x_tmp <= x; x_tmp++){
										this.switchToBeam(x_tmp,y,possibleBeamsources.get(0));
									}
								} else {
									for (int x_tmp = possibleBeamsources.get(0).col()-1; x_tmp >= x; x_tmp--){
										this.switchToBeam(x_tmp,y,possibleBeamsources.get(0));
									}
								}
							}
							
							
							useful = true;
						}
					}
				}
			}
		}	
		if (this.tileArrayComplete(this.tiles_copy, cols, rows)){
				useful = true;
		}
		return useful;
	}
	
	public List<Tile> getAvailableBeamsources(int y, int x){
		List<Tile> availableBeamsources = new ArrayList<Tile>();
		int tmp_x, tmp_y;
		
		// beamsources suchen
		
		// nach links suchen
		if(x > 0){	
			tmp_x = x;
			tmp_y = y;
			do {
				tmp_x--;
			} while (tmp_x > 0 && "field".equals(this.tiles_copy[tmp_y][tmp_x].type()));			
			if (("beamsource".equals(this.tiles_copy[tmp_y][tmp_x].type()))||("beam".equals(this.tiles_copy[tmp_y][tmp_x].type()) && tmp_y == this.tiles_copy[tmp_y][tmp_x].parent().row())){
				availableBeamsources.add(this.tiles_copy[tmp_y][tmp_x]);
			}
		}
		
		// nach oben suchen
		if(y > 0){	
			tmp_x = x;
			tmp_y = y;
			do {
				tmp_y--;
			} while (tmp_y > 0 && "field".equals(this.tiles_copy[tmp_y][tmp_x].type()));			
			if (("beamsource".equals(this.tiles_copy[tmp_y][tmp_x].type()))||("beam".equals(this.tiles_copy[tmp_y][tmp_x].type()) && tmp_x == this.tiles_copy[tmp_y][tmp_x].parent().col())){
				availableBeamsources.add(this.tiles_copy[tmp_y][tmp_x]);
			}
		}
		
		// nach rechts suchen
		if(x < (cols-1)){	
			tmp_x = x;
			tmp_y = y;
			do {
				tmp_x++;
			} while (tmp_x < (cols-1) && "field".equals(this.tiles_copy[tmp_y][tmp_x].type()));			
			if (("beamsource".equals(this.tiles_copy[tmp_y][tmp_x].type()))||("beam".equals(this.tiles_copy[tmp_y][tmp_x].type()) && tmp_y == this.tiles_copy[tmp_y][tmp_x].parent().row())){
				availableBeamsources.add(this.tiles_copy[tmp_y][tmp_x]); 
			}
		}
		
		// nach unten suchen
		if(y < (rows-1)){	
			tmp_x = x;
			tmp_y = y;
			do {
				tmp_y++;
			} while (tmp_y < (rows-1) && "field".equals(this.tiles_copy[tmp_y][tmp_x].type()));			
			if (("beamsource".equals(this.tiles_copy[tmp_y][tmp_x].type()))||("beam".equals(this.tiles_copy[tmp_y][tmp_x].type()) && tmp_x == this.tiles_copy[tmp_y][tmp_x].parent().col())){
				availableBeamsources.add(this.tiles_copy[tmp_y][tmp_x]);
			}
		}
		
		return availableBeamsources;
		
	}
	
	public List<Tile> getPossibleBeamsources(List<Tile> beamsources,int y, int x){
		List<Tile> possibleBeamsources = new ArrayList<Tile>();
		int numberBeamsAvailable;
		for (int i = 0; i < beamsources.size(); i++){
			if ("beam".equals(beamsources.get(i).type())){
				numberBeamsAvailable = beamsources.get(i).parent().strength();
				// zugehörige beams zählen
				numberBeamsAvailable -= this.countBeams(beamsources.get(i).parent().col(),beamsources.get(i).parent().row());
			} else {
				numberBeamsAvailable = beamsources.get(i).strength();
				// zugehörige beams zählen
				numberBeamsAvailable -= this.countBeams(beamsources.get(i).col(),beamsources.get(i).row());
			}
			
			int distanceFieldToBeamsource;
			if (beamsources.get(i).row() == y){
				distanceFieldToBeamsource = beamsources.get(i).col() - x;
			} else {
				distanceFieldToBeamsource = beamsources.get(i).row() - y;
			}
			if (distanceFieldToBeamsource < 0){
				distanceFieldToBeamsource = distanceFieldToBeamsource * (-1);
			}
			
			if (numberBeamsAvailable >= distanceFieldToBeamsource){
				possibleBeamsources.add(beamsources.get(i));
			}
		}
		return possibleBeamsources;
	}
	
	public int countBeams(int x, int y){
		int output = 0;
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				if ("beam".equals(this.tiles_copy[i][j].type()) && this.tiles_copy[i][j].parent() != null && this.tiles_copy[i][j].parent().col() == x && this.tiles_copy[i][j].parent().row() == y ){
					output++;
				}
			}
		}
		return output;
	}
	
	
	
	
	
	
	
	
	
	

}
 