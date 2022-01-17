package org.matsim.VL06;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.router.costcalculators.TravelDisutilityModule;
import org.matsim.core.scenario.ScenarioUtils;

public class RunMATSim7 {

	public static void main(String[] args){

		Config config = ConfigUtils.createConfig();

		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(1);

		Scenario scenario = ScenarioUtils.loadScenario(config);

//		ControlerUtils.createAdhocInjector(config, scenario) // dies kann man als Anfangspunktnehmen... -> und inlinen :)

		Controler controler = new Controler(scenario);
		controler.addOverridingModule(new TravelDisutilityModule());

		controler.run();
	}
}
