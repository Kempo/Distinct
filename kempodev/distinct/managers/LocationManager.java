package kempodev.distinct.managers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import kempodev.distinct.modules.misc.Location;

public class LocationManager {
	/**
	 * No need to make another add/remove getter for the ConcurrentHashMap "searchedBlocks", too lazy.
	 * ~ a-a-ron
	 */
	private CopyOnWriteArrayList<Location> locations = new CopyOnWriteArrayList<Location>();
	private ConcurrentHashMap<Integer,Integer> searchedBlocks = new ConcurrentHashMap<Integer,Integer>();
	public CopyOnWriteArrayList<Location> getLocations() {
		return locations;
	}
	public ConcurrentHashMap<Integer,Integer> getSearchedBlocks() {
		return searchedBlocks;
	}
	public void addLocation(Location e) {
		if(!locations.contains(e)) {
		locations.add(e);
		}
	}
	public void removeLocation(Location e) {
		if(locations.contains(e)) {
		locations.remove(locations.indexOf(e));
		}
	}
}
