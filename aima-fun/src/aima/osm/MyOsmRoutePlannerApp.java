package aima.osm;

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
    
    
}
