package org.matsim.VL03DependencyInjection;

import com.google.inject.*;
import com.google.inject.Module;

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
	interface Helper{
		void help();
	}
	interface Simulation{
		void doStep();
	}
	static class HelperDefaultImpl implements Helper{
		public void help(){
			System.out.println( this.getClass().getSimpleName() + " is helping" );
		}
	}
	static class SimulationDefaultImpl implements Simulation {
//		//A:) remove non-empty constructor to have a zero-argument constructor (for guice...) since not having exactly one @inject annotated constructor
//		@Inject private Helper helper;

//	B:	Alternative dazu wäre - ist oft in MATSim, wo wir die bisherigen Construcoren wieder nutzen.:
		private final Helper helper;
		@Inject SimulationDefaultImpl(Helper helper){
			this.helper = helper;
		}

		public void doStep() {
			System.out.println( "entering " + this.getClass().getSimpleName() );
			helper.help();
			System.out.println( "leaving " + this.getClass().getSimpleName() );
		}
	}
}
