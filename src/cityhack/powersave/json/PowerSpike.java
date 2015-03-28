package cityhack.powersave.json;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class PowerSpike {
	public String _id, _rev;
	
	public Date start, finish;
	
	//in kW
	public int power;	
	
	public int calcDuration(){
		DateTime d1 = new DateTime(start)
		, d2 = new DateTime(finish);
		
		return Minutes.minutesBetween(d1,d2).getMinutes();
	}
}
