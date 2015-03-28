package cityhack.powersave.json;

import java.util.Date;

/**
 * Every appliance runs at least once per day. Default priority is >1 minutes,
 * then 0 minutes.
 * 
 * @author martin
 *
 */
public class Appliance {
	public String _id, _rev;

	// in Watts
	public int power;

	// in minutes, 0 minutes for always on, -1 minutes for never running
	public int duration;

	public Date assumedStart;

	// 1..100, 100 being lowest priority
	// public int priority;

	// public Date earliestStart, latestFinish;

	public int calcEnergy() {
		return power * duration;
	}
}
