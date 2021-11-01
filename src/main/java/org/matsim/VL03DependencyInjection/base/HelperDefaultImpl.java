package org.matsim.VL03DependencyInjection.base;

public class HelperDefaultImpl implements Helper {
	public void help() {
		System.out.println(this.getClass().getSimpleName() + " is helping");
	}
}
