package org.matsim.freight;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.vehicles.VehicleType;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import java.net.URL;
import java.util.concurrent.ExecutionException;


/**
 * @see org.matsim.contrib.freight
 */
public class RunFreightExample {

	public static void main(String[] args) throws ExecutionException, InterruptedException{

		// ### config stuff: ###

		Config config = ConfigUtils.loadConfig( IOUtils.extendUrl(ExamplesUtils.getTestScenarioURL( "freight-chessboard-9x9" ), "config.xml" ) );

		config.plans().setInputFile( null ); // remove passenger input

		//more general settings
		config.controler().setOutputDirectory("./output/freight" );

		config.controler().setLastIteration(0 );		// yyyyyy iterations currently do not work; needs to be fixed.

		//freight settings
		FreightConfigGroup freightConfigGroup = ConfigUtils.addOrGetModule( config, FreightConfigGroup.class ) ;
		freightConfigGroup.setCarriersFile( "singleCarrierFiveActivitiesWithoutRoutes.xml");
		freightConfigGroup.setCarriersVehicleTypesFile( "vehicleTypes.xml");

		// load scenario (this is not loading the freight material):
		Scenario scenario = ScenarioUtils.loadScenario( config );

		//load carriers according to freight config
		FreightUtils.loadCarriersAccordingToFreightConfig( scenario );

		// how to set the capacity of the "light" vehicle type to "1":
		FreightUtils.getCarrierVehicleTypes( scenario ).getVehicleTypes().get( Id.create("light", VehicleType.class ) ).getCapacity().setOther( 1 );

		// output before jsprit run (not necessary)
		new CarrierPlanXmlWriterV2(FreightUtils.getCarriers( scenario )).write( "output/jsprit_unplannedCarriers.xml" ) ;
		// (this will go into the standard "output" directory.  note that this may be removed if this is also used as the configured output dir.)

		//Solving the VRP (generate carrier's tour plans)
		FreightUtils.runJsprit( scenario );

		// output after jsprit run (not necessary)
		new CarrierPlanXmlWriterV2(FreightUtils.getCarriers( scenario )).write( "output/jsprit_plannedCarriers.xml" ) ;
		// (this will go into the standard "output" directory.  note that this may be removed if this is also used as the configured output dir.)

		//MATSim configuration:
		final Controler controler = new Controler( scenario ) ;
		Freight.configure( controler );

		// otfvis (if you want to use):
		OTFVisConfigGroup otfVisConfigGroup = ConfigUtils.addOrGetModule( config, OTFVisConfigGroup.class );
		otfVisConfigGroup.setLinkWidth( 10 );
		otfVisConfigGroup.setDrawNonMovingItems( false );
		config.qsim().setTrafficDynamics( QSimConfigGroup.TrafficDynamics.kinematicWaves );
		config.qsim().setSnapshotStyle( QSimConfigGroup.SnapshotStyle.kinematicWaves );
		controler.addOverridingModule( new OTFVisLiveModule() );

//		start of the MATSim-Run:
		controler.run();
	}

}

