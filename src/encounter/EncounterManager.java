package encounter;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import excel_manager.ExcelReader;

/**
 * This class is used to create an encounter map using an excel sheet.
 * It makes easier the usage of the encounter table.
 * 
 * @author JHER
 *
 */
public class EncounterManager {
	/**
	 * Creates a map containing the encounter table using a sheet.
	 * 
	 * @param sheet : The sheet to read, that contains the encounter table.
	 * @return a map containing the encounter table.
	 */
	public static Map<String, Encounter> getEncountersFromSheet(Sheet sheet) {
		Map<String, Encounter> encounterMap = new TreeMap<String, Encounter>();
		for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
			Row row = sheet.getRow(rowNumber);
			String name = (String) ExcelReader.getCellValue(row.getCell(0));
			BigDecimal creatureNumberIn100km = (BigDecimal) ExcelReader.getCellValue(row.getCell(1));
			BigDecimal percentDay = (BigDecimal) ExcelReader.getCellValue(row.getCell(2));
			BigDecimal percentNight = (BigDecimal) ExcelReader.getCellValue(row.getCell(3));
			Encounter encounter = new Encounter(creatureNumberIn100km.multiply(percentDay), creatureNumberIn100km.multiply(percentNight));
			encounterMap.put(name, encounter);
		}
		return encounterMap;
	}
	
	/**
	 * Use the result of a double dice (1.000000) to get an encounter.
	 * 
	 * @param encounterMap : the map containing the encounter table.
	 * @param dice : the dice used to get the encounter from the encounter table.
	 * @param isDay : is day or not.
	 * @return : the name of the encountered creature.
	 */
	public static String getEncounter(Map<String, Encounter> encounterMap, Double dice, boolean isDay) {
		BigDecimal cumulatedPercent = BigDecimal.ZERO;
		BigDecimal totalPopulation = getTotalPopulation(encounterMap, isDay);
		BigDecimal bigDice = new BigDecimal(dice);
		Iterator<String> keys_I = encounterMap.keySet().iterator();
		while(keys_I.hasNext()) {
			String key = (String) keys_I.next();

			BigDecimal population = (isDay) ? encounterMap.get(key).getDay() : encounterMap.get(key).getNight();
			BigDecimal percentage = population.divide(totalPopulation, 10, BigDecimal.ROUND_HALF_UP);
			cumulatedPercent = cumulatedPercent.add(percentage);
			if(cumulatedPercent.compareTo(bigDice) == 1) {
				return key;
			}
		}
		return null;
	}
	
	/**
	 * Use the result of a double dice (1.000000) see if there is an encounter or not.
	 * 
	 * @param encounterMap : the map containing the encounter table.
	 * @param isDay : is day or not.
	 * @param dice : the dice used to get the encounter from the encounter table.
	 * @return : the name of the encountered creature.
	 */
	public static boolean isEncounter(Map<String, Encounter> encounterMap, boolean isDay, double dice) {
		BigDecimal totalPopulation = getTotalPopulation(encounterMap, isDay);
		BigDecimal encounterPercent = totalPopulation.divide(new BigDecimal(1000000));
		BigDecimal random = new BigDecimal(dice);
		return encounterPercent.compareTo(random) == 1;
	}
	
	/**
	 * Return the total population of the encounterMap.
	 * 
	 * @param encounterMap : the map containing the encounter table.
	 * @param isDay : is day or not.
	 * @return the total amount of creatures
	 */
	public static BigDecimal getTotalPopulation(Map<String, Encounter> encounterMap, boolean isDay) {
		BigDecimal totalPopulation = new BigDecimal(0);
		Iterator<String> keys_I = encounterMap.keySet().iterator();
		while(keys_I.hasNext()) {
			String key = (String) keys_I.next();
			BigDecimal population = (isDay) ? encounterMap.get(key).getDay() : encounterMap.get(key).getNight();
			totalPopulation = totalPopulation.add(population);
		}
		return totalPopulation;
	}
}
