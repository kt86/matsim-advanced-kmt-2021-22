package org.matsim.VL03DependencyInjection.alternative;

import org.matsim.VL03DependencyInjection.base.Helper;

public class HelperAlternative1Impl implements Helper {
	public void help() {
		System.out.println("KMTs Alternative des Helpers wird genutzt.");
		System.out.println(this.getClass().getSimpleName() + " is helping");
	}
}
