package org.matsim.VL03DependencyInjection;

import com.google.inject.*;
import com.google.inject.Module;
import org.matsim.VL03DependencyInjection.alternative.HelperAlternative1Impl;
import org.matsim.VL03DependencyInjection.base.Helper;
import org.matsim.VL03DependencyInjection.base.HelperDefaultImpl;
import org.matsim.VL03DependencyInjection.base.Simulation;
import org.matsim.VL03DependencyInjection.base.SimulationDefaultImpl;

class RunSimulation{

	public static void main( String[] args ){
		// calssic way: dependenc< incetion via constructor
//		Helper helper = new HelperDefaultImpl();
//		Simulation simulation = new SimulationDefaultImpl( helper );
//		simulation.doStep();
		
//		Using guice

		Module module = new AbstractModule() { //Hier die Google-Variante nehmen, weil wir noch ohne MATSim arbeiten.
			@Override
			protected void configure() {
				this.bind(Simulation.class).to(SimulationDefaultImpl.class); //Binde Interface TO Implementation.
//				this.bind(Helper.class).to(HelperDefaultImpl.class); // das was vorher das war: Helper helper = new HelperDefaultImpl();
				//bind.toInstance(...) normalerweise nicht so sinnvoll, aber viel näher dran an dem was wir bisher haben... von daher durchaus für Übergang ok :)
				//binding to type (siehe davor) ist besser, weil flexibler für Anpassungen.
				Helper instance = new HelperDefaultImpl();
				this.bind(Helper.class).toInstance(instance);
			}
		};
		Injector injector = Guice.createInjector(module);
		Simulation simulation = injector.getInstance(Simulation.class);
		
		simulation.doStep();
	}

}
