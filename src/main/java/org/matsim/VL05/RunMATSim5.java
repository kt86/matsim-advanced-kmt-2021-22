package org.matsim.VL05;

import com.google.inject.Inject;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.router.MainModeIdentifier;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.ScoringFunctionFactory;
import org.matsim.core.scoring.SumScoringFunction;
import org.matsim.core.scoring.functions.*;

public class RunMATSim5 {

	public static void main (String[] args){
		Config config = ConfigUtils.loadConfig("scenarios/equil/config.xml");

		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(0);

		config.qsim().setSnapshotStyle(QSimConfigGroup.SnapshotStyle.kinematicWaves);
		config.qsim().setTrafficDynamics(QSimConfigGroup.TrafficDynamics.kinematicWaves);

		Scenario scenario = ScenarioUtils.loadScenario( config );

		Controler controler = new Controler(scenario);

		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
//				this.bindScoringFunctionFactory().to(MyScoringFunctionFactory.class);  // Aus dem Beispiel: RunScoringExample
				this.addEventHandlerBinding().toInstance( new MyEventHAndler1());  //Aus dem Beispiel: RunEventsHandlingWithControlerExample
				this.addEventHandlerBinding().toInstance( new MyEventHandler2());
			}
		});

		controler.addOverridingModule(new OTFVisLiveModule());

		controler.run();
	}

	private static class MyEventHAndler1 implements LinkEnterEventHandler {
		@Override
		public void handleEvent(LinkEnterEvent event) {
			System.out.println("LinkEnterEvent found:" + event.toString());
		}
	}

	public static class MyEventHandler2 implements LinkEnterEventHandler {
		@Inject Network network; // Injecte das Netzwerk anstelle es so im Constructor zu übergeben...

		@Override
		public void handleEvent(LinkEnterEvent event) {
			System.out.println("LinkEnterEvent found:" + event.toString());
			System.out.println("Der Link hat folgende Eigenschaften: " + network.getLinks().get(event.getLinkId()).toString());
		}
	}

	// Hinweis es gibt auch die ScoreEvents: Wenn man mal einfach Sachen z.B. bestrafen will, kann man die nehmen und die würden dann berückscihtigt.
	//Z.B. wenn beim Fahrrad man einen schlechten Untergrund bei einem Link berücksichtigen möchte.


	//Als ein Beispiel
	private static class MyScoringFunctionFactory implements ScoringFunctionFactory {
		@Inject private Network network;
		@Inject private ScoringParametersForPerson pparams ;
		@Inject MainModeIdentifier mainModeIdentifier ;

		@Override
		public ScoringFunction createNewScoringFunction(Person person) {
			final ScoringParameters params = pparams.getScoringParameters( person );

			SumScoringFunction ssf = new SumScoringFunction() ;
			ssf.addScoringFunction(new CharyparNagelActivityScoring( params ) );
			ssf.addScoringFunction(new CharyparNagelMoneyScoring(params));
			ssf.addScoringFunction(new CharyparNagelAgentStuckScoring(params));

			return ssf;
		}
	}
}

