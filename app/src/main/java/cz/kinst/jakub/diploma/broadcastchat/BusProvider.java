package cz.kinst.jakub.diploma.broadcastchat;

import de.greenrobot.event.EventBus;

/**
 * Created by jakubkinst on 12/11/14.
 */
public class BusProvider {
	static private EventBus bus = new EventBus();

	public static EventBus get() {
		return bus;
	}
}
