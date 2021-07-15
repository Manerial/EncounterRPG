package encounter;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import excel_manager.ExcelReader;

/**
 * This class is used to create an encounter map using an excel sheet.
 * It makes easier the usage of the encounter table.
 * 
 * @author JHER
 *
 */
public class EncounterManager {
	private Map<String, Map<String, BigDecimal>> encounters;
	private static Random random = new Random();
	
	/**
	 * Creates a map containing the encounter table using a sheet.
	 * 
	 * @param sheet : The sheet to read, that contains the encounter table.
	 * @return a map containing the encounter table.
	 */
	public void setEncounters(Workbook workbook) {
		encounters = new TreeMap<String, Map<String, BigDecimal>>();

		Sheet sheet = workbook.getSheetAt(0);
		
		for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
			Row row = sheet.getRow(rowNumber);
			
			String zone = (String) ExcelReader.getCellValue(row.getCell(0));
			String name = (String) ExcelReader.getCellValue(row.getCell(1));
			BigDecimal population = (BigDecimal) ExcelReader.getCellValue(row.getCell(2));
			Map<String, BigDecimal> encounter;

			if(encounters.containsKey(zone)) {
				encounter = encounters.get(zone);
			} else {
				encounter = new TreeMap<String, BigDecimal>();
			}
			
			encounter.put(name, population);
			encounters.put(zone, encounter);
		}
		
	}
	
	/**
	 * Use the result of a double dice (1.000000) to get an encounter.
	 * 
	 * @param encounterMap : the map containing the encounter table.
	 * @param dice : the dice used to get the encounter from the encounter table.
	 * @param isDay : is day or not.
	 * @return : the name of the encountered creature.
	 */
	public String getRandomEncounter(String zone) {
		Map<String, BigDecimal> zoneOfEncounter = encounters.get(zone);
		
		BigDecimal totalPopulation = getTotalPopulation(zoneOfEncounter);
		BigDecimal rand = new BigDecimal(random.nextDouble());
		BigDecimal index = totalPopulation.multiply(rand);
		BigDecimal curentIndex = BigDecimal.ZERO;
		
		Iterator<String> keys = zoneOfEncounter.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();

			curentIndex = curentIndex.add(zoneOfEncounter.get(key));
			if(curentIndex.compareTo(index) >= 0) {
				return key;
			}
		}
		return null;
	}

	private BigDecimal getTotalPopulation(Map<String, BigDecimal> zoneOfEncounter) {
		BigDecimal totalPopulation = BigDecimal.ZERO;
		
		Iterator<String> keys = zoneOfEncounter.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			totalPopulation = totalPopulation.add(zoneOfEncounter.get(key));
		}
		return totalPopulation;
	}
}
