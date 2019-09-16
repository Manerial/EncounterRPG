package excel_manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {

	/**
	 * Open a workbook using it's name only.
	 * 
	 * @param filePath : the path to the excel file.
	 * @return the open workbook.
	 */
	public static Workbook openWorkbook(String filePath) {
		File file;
		try {
			file = new File(filePath);
			return WorkbookFactory.create(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Close a workbook.
	 * 
	 * @param workbook : the workbook to close.
	 */
	public static void closeWorkbook(Workbook workbook) {
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the value of the cell regardless of it type.
	 * 
	 * @param cell : the cell containing the value to read.
	 * @return the read value (can be boolean, numeric or string).
	 */
	public static Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case BOOLEAN:
			return Boolean.valueOf(cell.getBooleanCellValue());
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			}
			BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
			return bd;
		case STRING:
			return cell.getStringCellValue();
		case FORMULA:
			switch (cell.getCachedFormulaResultType()) {
			case NUMERIC:
				return cell.getNumericCellValue();
			case STRING:
				return cell.getStringCellValue();
			default:
				return "";
			}
		case ERROR:
			return "ERROR";
		default:
			return "";
		}
	}

	/**
	 * Display the cells of a row (csv like).
	 * 
	 * @param row : the row to display.
	 */
	public static void displayRow(Row row) {
		for (int cellNum = 0; cellNum < row.getLastCellNum() - 1; cellNum++) {
			System.out.print(getCellValue(row.getCell(cellNum)) + ";");
		}
		System.out.println(getCellValue(row.getCell(row.getLastCellNum() - 1)));
	}

	/**
	 * Display the whole workbook (csv like).
	 * 
	 * @param workbook : the workbook to display.
	 */
	public static void displayWorkbook(Workbook workbook) {
		for (int sheetNumber = 0; sheetNumber < workbook.getNumberOfSheets(); sheetNumber++) {
			Sheet sheet = workbook.getSheetAt(sheetNumber);
			Row headerRow = sheet.getRow(0);
			System.out.println("=====" + sheet.getSheetName() + "=====");
			displayRow(headerRow);

			for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
				Row valuesRow = sheet.getRow(rowNumber);
				displayRow(valuesRow);
			}
		}
		closeWorkbook(workbook);
	}
}
