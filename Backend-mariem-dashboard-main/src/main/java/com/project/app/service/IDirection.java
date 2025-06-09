package com.project.app.service;

import java.util.List;
import java.util.Set;

import com.project.app.model.Direction;



public interface IDirection {
	 public Direction ajouterDirection(Direction direction);
	 public List<Direction> getAllDirectionsnonArchivés();
	
	 public List<Direction> getAllDirectionsArchivés();
	
		public Direction updateDirection(Long id, Direction Details);
		

}
