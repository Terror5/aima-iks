package aima.osm;

import java.text.DecimalFormat;
import java.util.List;

import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;
import aimax.osm.gui.fx.applications.OsmRoutePlannerApp;
import aimax.osm.routing.RouteCalculator;

public class MyOsmRoutePlannerApp extends OsmRoutePlannerApp {
	
    public static void main(String[] args) {
        launch(args);
    }

	@Override
	protected RouteCalculator createRouteCalculator() {
		return new MyRouteCalculator();
	}

	@Override
	protected String getTrackInfo(Track track) {
		StringBuffer details = new StringBuffer(super.getTrackInfo(track));
		
        List<MapNode> nodes = track.getNodes();
        DecimalFormat f1 = new DecimalFormat("#0.00");
		
		details.append(" ETA: ");
		details.append(f1.format(calculateETA(nodes)));
		
		return details.toString();
	}

	private static double calculateETA(List<MapNode> nodes){
		double result = 0.0d;
		MapWay way;
		for (int i = 1; i < nodes.size(); i++) {
			double distance = 0.0d;
			MapNode n1 = nodes.get(i - 1);
			MapNode n2 = nodes.get(i);		
			distance = Position.getDistKM(n1.getLat(), n1.getLon(), n2.getLat(), n2
					.getLon());
			//cross reference ways and calculate ETA
			//result += TimeCarStepCostFunction.getETA(way, distance);
		}		
		return result;
	}
	
	
    
}
