package launcher;

import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;

import encounter.EncounterManager;
import excel_manager.ExcelReader;

public class Launcher {
	private static String RESOURCES_PATH = System.getProperty("user.dir") + "\\resources\\";
	private static final String FILE_NAME = "Encounter_Table.xlsx";
	private static EncounterManager encounterManager = new EncounterManager();
	
	private static String zone = "VILLES";
	private static int number = 10;

	public static void main(String[] args) {
		String message = parseArgs(args);

		if(message != null) {
			System.out.println(message);
			return;
		} else {
			EncounterCreatures();
		}
	}

	private static String parseArgs(String[] args) {
		String message = null;
		for(int i = 0; i < args.length; i++) {
			switch(args[i]) {
			case "-z" :
				zone = args[i+1];
				break;
			case "-zl":
				message = getZones();
				break;
			case "-n" :
				number = Integer.parseInt(args[i+1]);
				break;
			case "-h":
				message = String.join("\r\n",
						"",
						"Available arguments: ",
						"",
						"-z <zone>\t: Set zone of encounters",
						"-zl \t: Zone list",
						"-n <number>\t: Number of encounters",
						"-h*\t\t: Help",
						"",
						"* : Using this argument will avoid file generation");
				break;
			}
		}
		
		return message;
	}

	/**
	 * Display a workbook using the ExcelReader.
	 */
	public static String displayWorkbook() {
		Workbook workbook = ExcelReader.openWorkbook(RESOURCES_PATH + FILE_NAME);
		String workbookContent = ExcelReader.workbookToString(workbook);
		ExcelReader.closeWorkbook(workbook);
		return workbookContent;
	}

	/**
	 * Display a workbook using the ExcelReader.
	 */
	public static String getZones() {
		Workbook workbook = ExcelReader.openWorkbook(RESOURCES_PATH + FILE_NAME);
		encounterManager.setEncounters(workbook);
		ExcelReader.closeWorkbook(workbook);
		Set<String> zones = encounterManager.getKeySet();
		return zones.toString();
	}

	/**
	 * Display a random encounter.
	 * 
	 * @param zone : the zone used to read the encounter table.
	 * @param number : the number of kilometers to walk.
	 * @param isDay : is day or not.
	 */
	public static void EncounterCreatures() {
		Workbook workbook = ExcelReader.openWorkbook(RESOURCES_PATH + FILE_NAME);
		encounterManager.setEncounters(workbook);
		
		for(int i = 0; i < number; i++) {
			String encounter = encounterManager.getRandomEncounter(zone);
			System.out.println(encounter);
		}
		ExcelReader.closeWorkbook(workbook);
	}
}