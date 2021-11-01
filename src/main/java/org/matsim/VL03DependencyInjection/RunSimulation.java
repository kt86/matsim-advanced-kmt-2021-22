package org.matsim.VL03DependencyInjection;

class RunSimulation{

	public static void main( String[] args ){
		Helper helper = new HelperDefaultImpl();
		Simulation simulation = new SimulationDefaultImpl( helper );
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
		private final Helper helper;
		SimulationDefaultImpl( Helper helper ) {
			this.helper = helper;
		}
		public void doStep() {
			System.out.println( "entering " + this.getClass().getSimpleName() );
			helper.help();
			System.out.println( "leaving " + this.getClass().getSimpleName() );
		}
	}
}
