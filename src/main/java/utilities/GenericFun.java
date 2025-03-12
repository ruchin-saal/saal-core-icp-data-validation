package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenericFun {
    private static final Logger logger = LogManager.getLogger(GenericFun.class);

    public static void  setDesignTemplate() {
        System.out.println("\n#############################################################\n#" +
                "                                                           #");
        System.out.println("#        Welcome to World of Qa Automation Testing          #\n#" +
                "                                                           #");
        System.out.println("############################################################");
    }


    public static void printStatement(String value) {
        System.out.println("The Value is below \n"+value);
    }


    public static HashMap<String,String> getCurrentDate(){
        HashMap<String,String> hashMap = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        hashMap.put("day",dateFormat.format(date).split("-")[2]);
        hashMap.put("month",dateFormat.format(date).split("-")[1]);
        return hashMap;
    }

    public static String getCurrentDay(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        return dateFormat.format(date);
    }
    public static String getTimeStamp(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        return dateFormat.format(date);
    }

    public static String getCurrentTimeInUTCFormat(){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        return f.format(new Date());
    }

    public static ArrayList<String> createArrayListUsingAPIData(int size, String responseValue){
        ArrayList<String> uniqueList = new ArrayList<>();
        HashSet<String> uniqueSet = new HashSet<>();
        for (int i=0;i<size;i++) {
            uniqueSet.add(responseValue);
        }
        uniqueList.addAll(uniqueSet);
        Collections.sort(uniqueList);
        return uniqueList;
    }

    public static ArrayList<String> createGradesListUsingExcelData(String sheetName, int startColumnIndex, int endColumnIndex){
        ArrayList<String> excelList = new ArrayList<String>();
        for(int i=startColumnIndex;i<endColumnIndex;i++){
            String cellValue = Xlsx_Reader.getCellDataByColumnIndex(sheetName,1,i);
            excelList.add(cellValue);
        }
        ArrayList<String> finalExcelList = new ArrayList<>();
        for (String element : excelList) {
            if(element.contains(" ")){
                String[] parts = element.split(" ");
                finalExcelList.add(parts[1]);
            }
            else{
                finalExcelList.add(element);
            }
        }
        return finalExcelList;
    }

    public static ArrayList<String> createSubjectListUsingExcelData(String sheetName, int gradeColumnIndex) {
        ArrayList<String> excelList = new ArrayList<String>();
        for (int i = 2; i < Xlsx_Reader.getRowCount(sheetName); i++) {
            String subjectName = Xlsx_Reader.getCellData(sheetName, i, 1);
            String cellValue = Xlsx_Reader.getCellData(sheetName, i, gradeColumnIndex);
            if (cellValue.equalsIgnoreCase("TRUE")) {
                excelList.add(subjectName);
            }
        }
        return excelList;
    }

    public static boolean compareListsIgnoringCaseAndUnderscore(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            String str1 = list1.get(i).toLowerCase().replaceAll("_", " ");
            String str2 = list2.get(i).toLowerCase().replaceAll("_", " ");
            if (!str1.equals(str2)) {
                return false;
            }
        }
        return true;
    }

    public static List<String> getAPIUnmatchedElements(List<String> list1, List<String> list2) {
        List<String> apIUnmatchedElements = new ArrayList<>();

        // Remove underscores and convert to lowercase for both lists
        List<String> apiModifiedList1 = new ArrayList<>();
        List<String> excelModifiedList2 = new ArrayList<>();

        for (String element : list1) {
            apiModifiedList1.add(element.replace("_", " ").toLowerCase());
        }
        for (String element : list2) {
            excelModifiedList2.add(element.replace("_", " ").toLowerCase());
        }
        // Compare lists to find unmatched elements
        for (String element : apiModifiedList1) {
            if (!excelModifiedList2.contains(element)) {
                apIUnmatchedElements.add(element);
            }
        }
        if (!apIUnmatchedElements.isEmpty())
        { logger.info("Subject not present in excel: "+apIUnmatchedElements);}
        return apIUnmatchedElements;
    }

    public static List<String> getExcelUnmatchedElements(List<String> list1, List<String> list2) {
        List<String> excelUnmatchedElements = new ArrayList<>();

        // Remove underscores and convert to lowercase for both lists
        List<String> apiModifiedList1 = new ArrayList<>();
        List<String> excelModifiedList2 = new ArrayList<>();

        for (String element : list1) {
            apiModifiedList1.add(element.replace("_", " ").toLowerCase());
        }
        for (String element : list2) {
            excelModifiedList2.add(element.replace("_", " ").toLowerCase());
        }
        // Compare lists to find unmatched elements
        for (String element : excelModifiedList2) {
            if (!apiModifiedList1.contains(element)) {
                excelUnmatchedElements.add(element);
            }
        }
        if (!excelUnmatchedElements.isEmpty())
        { logger.info("Subject not present in API: "+excelUnmatchedElements);}
        return excelUnmatchedElements;
    }

    public static ArrayList<String> createListUsingExcelData(String sheetName, int startColumnIndex, int rowNumber){
        ArrayList<String> excelList = new ArrayList<String>();
        for(int i=startColumnIndex;i<Xlsx_Reader.getRowCount(sheetName);i++){
            String cellValue = Xlsx_Reader.getCellDataByColumnIndex(sheetName,rowNumber,i);
            i=i+2;
            if (!cellValue.isEmpty()){
            excelList.add(cellValue);
            }
            else {break;}
        }
        return excelList;
    }

    public static ArrayList<String> createColumnValueListUsingExcelData(String sheetName, int columnIndex, int rowNum) {
        ArrayList<String> excelList = new ArrayList<String>();
        for (int i = rowNum; i < Xlsx_Reader.getRowCount(sheetName); i++) {
            String cellValue = Xlsx_Reader.getCellData(sheetName, i, columnIndex);
            if (!cellValue.isEmpty()){
                excelList.add(cellValue);
            }
        }
        return excelList;
    }

    public static List<String> createSubListUsingExcelData(String sheetName, int startColumnIndex, int rowNumber) {
        ArrayList<String> excelList = new ArrayList<String>();
        String subCellValue = Xlsx_Reader.getCellDataByColumnIndex(sheetName, rowNumber, startColumnIndex);
        String classCellValue=Xlsx_Reader.getCellDataByColumnIndex(sheetName,rowNumber,startColumnIndex+2);
        String gradeCellValue = Xlsx_Reader.getCellData(sheetName,rowNumber,startColumnIndex+1);
        String[] grades = gradeCellValue.split(" ");
        String grade=grades[1];

        List<String> classList = new ArrayList<>();
        for (int i = 0; i < classCellValue.length(); i++) {
           String element = String.valueOf(classCellValue.charAt(i));
           classList.add("G"+grade+"-"+element);
        }
        return classList;
    }

    public static List<String> createStringListUsingLetters(String inputString){
        List<String> lettersList = new ArrayList<>();
        for (int i = 0; i < inputString.length(); i++) {
            lettersList.add(String.valueOf(inputString.charAt(i)));
        }
      return lettersList;
    }

    public static List<String> getUniqueList(List<String> list){
        Set<String> setWithoutDuplicates = new HashSet<>(list);
        List<String> listWithoutDuplicates = new ArrayList<>(setWithoutDuplicates);
        return listWithoutDuplicates;
    }

    public static String replaceAnyValue(String originalText, String textToReplace, String replaceWith){
        originalText=originalText.replace(textToReplace, replaceWith);
        return originalText;
    }

    public static ArrayList<String> listStringToDecimalConversion(List<String> stringList){
        ArrayList<Double> decimalList = new ArrayList<>();
        for (String value : stringList) {
            decimalList.add(Double.parseDouble(value));
        }
        ArrayList<String> decimalToStringList = new ArrayList<>();
        for (Double value : decimalList) {
            decimalToStringList.add(String.valueOf(value));
        }
        return decimalToStringList;
    }


    public static ArrayList<String> removeDuplicateDataFromList(List<String> originalData){
        Set<String> db_competitionIdRemoveDuplicate = new HashSet<>(originalData);
        ArrayList<String>duplicateRemoved=new ArrayList<String>(db_competitionIdRemoveDuplicate);
        return duplicateRemoved;
    }

    public static ArrayList<String> removeZeroFromList(List<String> originalData){
        originalData.removeIf(item -> item == "0");
        originalData.removeIf(item -> item.equals("0"));
        originalData.removeIf(item -> item == String.valueOf(0));
        return (ArrayList<String>) originalData;
    }


    public static ArrayList<String> changeDateFormat(ArrayList<String> inputDate){
        ArrayList<String> updatedList=new ArrayList<>();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < inputDate.size(); i++) {
            String dateStr = inputDate.get(i);
            // Remove the microseconds part
            if (dateStr.length() > 19) {
                dateStr = dateStr.substring(0, 19);
                updatedList.add(dateStr);
            }
            // Update the original list with the formatted date

        }
        return updatedList;
    }

    public static ArrayList<String> replaceTextInArrayListElement(ArrayList<String> list, String originalValue, String replaceValue) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(originalValue)) {
                list.set(i, replaceValue);
            }
        }
        return list;
    }

    public static ArrayList<String> removeTextAfterAnyValue(ArrayList<String> list, String value) {
        ArrayList<String> countryCodes = new ArrayList<>();
        for (String entry : list) {
            int dashIndex = entry.indexOf(value);
            if (dashIndex != -1) {
                String countryCode = entry.substring(0, dashIndex);
                countryCodes.add(countryCode);
            }
        }
        return countryCodes;
    }

    public static List<String> missingDataInSecondList(List<String> list1, List<String> list2) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);
        set1.removeAll(set2);
        List<String> missingElements = new ArrayList<>(set1);
        return missingElements;
    }

    public static String fetchTextAfterParticularText(String originalValue, String replacedValue) {
        // Split the string by "teamID>>"
        String[] parts = originalValue.split(replacedValue);

        // Check if the split was successful and print the value after "teamID>>"
        String extractedText="";
        if (parts.length > 1) {
            extractedText = parts[1];
        }
        return extractedText;
    }

    public static ArrayList<String> removeItemFromList(ArrayList<String> originalList, String removeListItemText) {
        // Remove the specific item if it exists in the list
        originalList.remove(removeListItemText);
        // Return the updated list
        return originalList;  // FIX: Removed incorrect originalList() call
    }
}
