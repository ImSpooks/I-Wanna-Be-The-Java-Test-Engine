package me.ImSpooks.IWBTJ.event;

import me.ImSpooks.IWBTJ.Main;

import java.lang.reflect.InvocationTargetException;

public abstract class Event {

	private boolean cancelled;
	private boolean stop;

	public enum State {
		PRE("PRE", 0), POST("POST", 1);
		private State(String string, int number) {
		}
	}

	public Event call() {
		this.cancelled = false;
		call(this);
		return this;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {

		this.cancelled = cancelled;
	}

	private static void call(Event event) {
		ArrayHelper<Data> dataList = Main.getInstance().getEventManager().get(event.getClass());
		if (dataList != null) {
			for (Data data : dataList) {
				if (event.isCancelled() && !data.ignoreCancelled) {
					return;
				}

				try {
					data.target.invoke(data.source, event);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stop() {
		this.stop = true;
	}
}