package easylink.dto;

public class Location {
	Float lat;
	Float lon;
	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	public Float getLon() {
		return lon;
	}
	public void setLon(Float lon) {
		this.lon = lon;
	}
	@Override
	public String toString() {
		return "Location [" + (lat != null ? "lat=" + lat + ", " : "") + (lon != null ? "lon=" + lon : "") + "]";
	}
}
