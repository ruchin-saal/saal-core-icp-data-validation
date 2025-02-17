package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class Xlsx_Reader {

    private static final Logger logger = LogManager.getLogger(Xlsx_Reader.class);
    public static String excelPath1 = System.getProperty("user.dir") + "/data/Subjects Taught 23-24 _updated.xlsx";
    public static String excelPath2 = System.getProperty("user.dir") + "/data/ADS_Academic Staff Allocations and Roles 23-24.xlsx";
    static XSSFWorkbook workbook = null;
    static XSSFSheet sheet = null;

    List<String> filePaths = List.of(excelPath1, excelPath2);

    //Constructor to get sheet path and sheet Name
    public Xlsx_Reader(String fileName) {
        try {
            workbook = new XSSFWorkbook(fileName);
        } catch (Exception exp) {
            logger.info(exp.getMessage());
            logger.info(exp.getCause());
            exp.printStackTrace();
        }
    }

    // To get value from the Sheet
    public static String getCellData(String sheetName, int rowNum, int colNum) {
        sheet = workbook.getSheet(sheetName);
        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));
        return value;
    }

    // To get RowCount in sheet
    public static int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return 0;
        else {
            sheet = workbook.getSheetAt(index);
            int number = sheet.getLastRowNum() + 1;
            return number;
        }
    }

    public static int getSheetsCount() {
        int sheetCount = workbook.getNumberOfSheets();
        return sheetCount;

    }

    public static String getCellDataWithSheetIndex(int sheetIndex, int rowNum, int colNum) {
        sheet = workbook.getSheetAt(sheetIndex);
        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));
        return value;
    }

    public static String getCellDataByColumnIndex(String sheetName, int rowNum, int colNum) {
        sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        String cellValue = "";
        if (row != null) {
            DataFormatter dataFormatter = new DataFormatter();
            Cell cell = row.getCell(colNum);
            if (cell != null) {
                // Assuming you want to retrieve the value as a string
                cellValue = dataFormatter.formatCellValue(cell);
            } else {
                logger.info("Cell at Row " + (rowNum + 1)
                        + ", Column " + (colNum + 1) + " is empty");
            }
        } else {
            logger.info("Row " + (rowNum + 1) + " does not exist");
        }
        return cellValue;
    }


    public static int getColumnCount(String sheetName) {
        sheet = sheet = workbook.getSheet(sheetName);
        Row firstRow = sheet.getRow(1);
        // Get the number of columns in the first row
        int columnCount = firstRow.getPhysicalNumberOfCells();
        return columnCount;
    }

    public static void readMultipleExcelFiles(String filePath) {
            try {
                // Open the Excel file
                FileInputStream fileInputStream = new FileInputStream(new File(filePath));
                // Create an Excel workbook
                Workbook workbook = WorkbookFactory.create(fileInputStream);
                // Close the workbook and input stream
                workbook.close();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public static String getCellDataUntilValueExist(String sheetName, int rowNum, int colNum) {
        sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        String cellValue = "";
        if (row != null) {
            DataFormatter dataFormatter = new DataFormatter();
            Cell cell = row.getCell(colNum);
            cellValue = dataFormatter.formatCellValue(cell);
        } else {
            logger.info("Row " + (rowNum + 1) + " does not exist");
        }
        return cellValue;
    }

    public static void main(String[] args) {

    }

    }

