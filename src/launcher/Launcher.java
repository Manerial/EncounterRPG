package launcher;

import java.util.Map;
import java.util.Random;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import encounter.Encounter;
import encounter.EncounterManager;
import encounter.Zones;
import excel_manager.ExcelReader;

public class Launcher {
	private static String RESOURCES_PATH = System.getProperty("user.dir") + "\\resources\\";
	private static Random	random						= new Random();
	private static final String FILE_NAME = "Encounter_Table.xlsx";

	public static void main(String[] args) {
		// displayWorkbook();
		Zones zone = Zones.VILLES;
		int kilometers = 10;
		boolean isDay = false;
		EncounterCreaturesOnDistance(zone, kilometers, isDay);
	}

	/**
	 * Display a workbook using the ExcelReader.
	 */
	public static void displayWorkbook() {
		Workbook workbook = ExcelReader.openWorkbook(RESOURCES_PATH + FILE_NAME);
		ExcelReader.displayWorkbook(workbook);
		ExcelReader.closeWorkbook(workbook);
	}

	/**
	 * Display a random encounter.
	 * 
	 * @param zone : the zone used to read the encounter table.
	 * @param isDay : check if this is the day.
	 */
	public static void EncounterCreatures(Zones zone, boolean isDay) {
		Workbook workbook = ExcelReader.openWorkbook(RESOURCES_PATH + FILE_NAME);
		Sheet sheet = workbook.getSheetAt(zone.ordinal());
		Map<String, Encounter> encounterMap = EncounterManager.getEncountersFromSheet(sheet);
		if(EncounterManager.isEncounter(encounterMap, isDay, random.nextDouble())) {
			String encounter = EncounterManager.getEncounter(encounterMap, random.nextDouble(), isDay);
			System.out.println(encounter);
		} else {
			System.out.println("No encounter");
		}
		ExcelReader.closeWorkbook(workbook);
	}

	/**
	 * Display a random encounter.
	 * 
	 * @param zone : the zone used to read the encounter table.
	 * @param kilometers : the number of kilometers to walk.
	 * @param isDay : is day or not.
	 */
	public static void EncounterCreaturesOnDistance(Zones zone, int kilometers, boolean isDay) {
		Workbook workbook = ExcelReader.openWorkbook(RESOURCES_PATH + FILE_NAME);
		Sheet sheet = workbook.getSheetAt(zone.ordinal());
		Map<String, Encounter> encounterMap = EncounterManager.getEncountersFromSheet(sheet);
		for(int i = 0; i < kilometers * 10; i++) {
			if(EncounterManager.isEncounter(encounterMap, isDay, random.nextDouble())) {
				String encounter = EncounterManager.getEncounter(encounterMap, random.nextDouble(), isDay);
				System.out.println(encounter);
			}
		}
		ExcelReader.closeWorkbook(workbook);
	}
}