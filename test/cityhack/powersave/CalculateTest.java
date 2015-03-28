package cityhack.powersave;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.*;

import cityhack.powersave.json.Appliance;
import cityhack.powersave.json.Calculation;
import cityhack.powersave.json.PowerSpike;

public class CalculateTest {

	@Test
	public void testKnapsackCalc() {
		Date date = new Date();
		DateTime d1 = new DateTime(date);
		
		List<PowerSpike> spikes = new ArrayList<PowerSpike>() {{
			add(new PowerSpike(){{ 
				_id = "1"; 
				power = 5; 
			}});
		}};
		spikes.get(0).start = date;
		spikes.get(0).finish = d1.plusHours(1).toDate();
		
		List<Appliance> appliances = new ArrayList<Appliance>() {{
			add(new Appliance(){{ power = 400; duration = 20;}});
			add(new Appliance(){{ power = 400; duration = 30;}});
			add(new Appliance(){{ power = 400; duration = 20;}});
		}};

		List<Calculation> calcs = CalculationMgr.calculate(spikes, appliances);
		assertNotNull(calcs);
		
		System.out.println("@Test - testEmptyCollection");
	}
}