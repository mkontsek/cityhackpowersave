package cityhack.powersave;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import cityhack.powersave.json.*;

public class CalculationMgr {

	public static List<Calculation> calculate(List<PowerSpike> powerspikes, List<Appliance> appliances) {
		List<Appliance> mustRunAppliances = new ArrayList<Appliance>(), canRunAppliances = new ArrayList<Appliance>();
		List<Calculation> calcs = new ArrayList<Calculation>();

		for (Appliance appl : appliances)
			if (appl.duration > 0)
				mustRunAppliances.add(appl);
			else
				canRunAppliances.add(appl);

		for (PowerSpike spike : powerspikes) {
			// energy to spend in Watts
			int energyToSpend = spike.power * spike.calcDuration() * 1000;
			
			List<Appliance> taken = new ArrayList<Appliance>();
			int energyThatCanBeSpent = KnapSack(energyToSpend, mustRunAppliances, mustRunAppliances.size(), taken);

			Calculation calc;
			DateTime energySavingTimeStart = new DateTime(spike.start);
			for (Appliance appl : taken) {
				calc = new Calculation();
				calc.appliance = appl;
				calc.start = spike.start;

				DateTime d1 = new DateTime(spike.start);
				calc.finish = d1.plusMinutes(appl.duration).toDate();
				calcs.add(calc);
			}
			for(Appliance appl : mustRunAppliances) {
				if(!taken.contains(appl)) {
					calc = new Calculation();
					calc.appliance = appl;
					calc.start = spike.finish;

					DateTime d1 = new DateTime(calc.start);
					calc.finish = d1.plusMinutes(appl.duration).toDate();
					calcs.add(calc);
				}
			}

			int restOfEnergy = energyToSpend - energyThatCanBeSpent;

			boolean ranFirstTime = false;
			for(Appliance appl : canRunAppliances) {
				if(!ranFirstTime) {
					calc = new Calculation();
					calc.appliance = appl;
					calc.finish = spike.finish;
						
					DateTime d1 = new DateTime(spike.finish);
					calc.start = d1.minusMinutes(restOfEnergy/calc.appliance.power).toDate();
					calcs.add(calc);
					continue;
				}
				calc = new Calculation();
				calc.appliance = appl;
				calc.start = spike.finish;
					
				DateTime d1 = new DateTime(spike.finish);
				calc.finish = d1.plusMinutes(calc.appliance.duration).toDate();
				calcs.add(calc);
			}
		}

		return calcs;
	}

	private static int KnapSack(int energyToSpend, List<Appliance> appliances, int numItems, List<Appliance> taken) {
		if (numItems == 0 || energyToSpend == 0)
			return 0;
		if (appliances.get(numItems - 1).calcEnergy() > energyToSpend)
			return KnapSack(energyToSpend, appliances, numItems - 1, taken);
		else {
			final int preTookSize = taken.size();
			final int took = 1 + KnapSack(energyToSpend - appliances.get(numItems - 1).calcEnergy(), appliances,
					numItems - 1, taken);

			final int preLeftSize = taken.size();
			final int left = KnapSack(energyToSpend, appliances, numItems - 1, taken);

			if (took > left) {
				if (taken.size() > preLeftSize)
					taken.subList(preLeftSize, taken.size()).clear();
				;
				taken.add(appliances.get(numItems - 1));
				return took;
			} else {
				if (preLeftSize > preTookSize)
					taken.subList(preTookSize, preLeftSize).clear();
				return left;
			}
		}
	}

}
