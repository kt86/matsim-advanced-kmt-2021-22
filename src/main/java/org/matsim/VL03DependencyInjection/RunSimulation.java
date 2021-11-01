package org.matsim.VL03DependencyInjection;

import com.google.inject.*;
import com.google.inject.Module;
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
				this.bind(Helper.class).to(HelperDefaultImpl.class); // das was vorher das war: Helper helper = new HelperDefaultImpl();
 			}
		};
		Injector injector = Guice.createInjector(module);
		Simulation simulation = injector.getInstance(Simulation.class);
		
		simulation.doStep();
	}

}
