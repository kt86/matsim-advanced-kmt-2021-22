package org.matsim.VL03DependencyInjection.base;

import com.google.inject.Inject;

public class SimulationDefaultImpl implements Simulation {
	//A:) remove non-empty constructor to have a zero-argument constructor (for guice...) since not having exactly one @inject annotated constructor
	@Inject
	private Helper helper;

////	B:	Alternative dazu wäre - ist oft in MATSim, wo wir die bisherigen Construcoren wieder nutzen.:
//		private final Helper helper;
//		@Inject SimulationDefaultImpl(Helper helper){
//			this.helper = helper;
//		}

	public void doStep() {
		System.out.println("entering " + this.getClass().getSimpleName());
		helper.help();
		System.out.println("leaving " + this.getClass().getSimpleName());
	}
}
