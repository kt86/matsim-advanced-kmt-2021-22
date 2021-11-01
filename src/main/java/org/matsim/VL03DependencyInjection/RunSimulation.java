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
		// classic way: dependency injection via constructor
//		Helper helper = new HelperDefaultImpl();
//		Simulation simulation = new SimulationDefaultImpl( helper );
//		simulation.doStep();
		
//		Using guice

		Module module = new AbstractModule() { //Hier die Google-Variante nehmen, weil wir noch ohne MATSim arbeiten.
			@Override
			protected void configure() {
				this.bind(Simulation.class).to(SimulationDefaultImpl.class); //Binde Interface TO Implementation.
//				this.bind(Helper.class).to(HelperDefaultImpl.class); // das was vorher das war: Helper helper = new HelperDefaultImpl();
				this.bind(Helper.class).toProvider(new HelperProvider());
			}
		};
		Injector injector = Guice.createInjector(module);
		Simulation simulation = injector.getInstance(Simulation.class);
		
		simulation.doStep();
	}

	//Ist etwas wie die "Factories". Nur ohne Argumente.
	static class HelperProvider implements Provider<Helper>{
		//Hier kann man auch noch eingenes Zeug injecten wenn man möchte..
		// @Inject ....

		//get ist der Defaultname, was es geben/erstellen soll.
		@Override public Helper get(){
			return new HelperDefaultImpl();
		}
	}
}
