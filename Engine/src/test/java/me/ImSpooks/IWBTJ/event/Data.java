package me.ImSpooks.IWBTJ.event;

import java.lang.reflect.Method;

public class Data {

	public final Object source;
	public final Method target;
	public final byte priority;
	public final boolean ignoreCancelled;

	public Data(Object source, Method target, byte priority, boolean ignoreCancelled) {
		this.source = source;
		this.target = target;
		this.priority = priority;
		this.ignoreCancelled = ignoreCancelled;
	}

}
