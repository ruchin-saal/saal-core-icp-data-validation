
package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utilities.DataTypeConverter.convertOracleToTrino;

public class AssertHelpers {
    private static final Logger logger = LogManager.getLogger(AssertHelpers.class);
    static SoftAssert softAssert = new SoftAssert();

    public static void assertTrueCompareListElementWise(List apiList, List dbList) {
        apiList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == null);
        Collections.sort(apiList);
        Collections.sort(dbList);
        for (int i = 0; i < apiList.size(); i++) {
            Assert.assertTrue(apiList.get(i).equals(dbList.get(i)),
                    "Mismatch at index " + i + ": " + apiList.get(i) + " != " + dbList.get(i));
        }
    }

    public static void assertTrue(List apiList, List dbList) {
        apiList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == null);
        Collections.sort(apiList);
        Collections.sort(dbList);
        Assert.assertEquals(apiList, dbList, "List comparison failed.");
    }

    public static void softAssertTrue(List apiList, List dbList) {
        apiList.removeIf(item -> item == null || "null".equals(item));
        dbList.removeIf(item -> item == null || "null".equals(item));
        Collections.sort(apiList);
        Collections.sort(dbList);
        if (!apiList.equals(dbList)) {
            logger.info("Assertion Failed API data is not matching with DB data \nAPI DATA: \n" + apiList + "\nDB data: \n" + dbList);
            logger.info("Missing in API: " + GenericFun.missingDataInSecondList(dbList, apiList));
            logger.info("Missing in DB: " + GenericFun.missingDataInSecondList(apiList, dbList));
        }
        softAssert.assertEquals(apiList, dbList, "Soft assert list mismatch");
    }

    public static void softAssertTrueCompareListElementWise(List apiList, List dbList) {
        apiList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == null);
        Collections.sort(apiList);
        Collections.sort(dbList);
        for (int i = 0; i < apiList.size(); i++) {
            softAssert.assertEquals(apiList.get(i), dbList.get(i), "Mismatch at index " + i);
        }
    }

    public static void softAssertTrueCompareListElementWiseWithContains(List apiList, List dbList) {
        apiList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == null);
        Collections.sort(apiList);
        Collections.sort(dbList);
        softAssert.assertEquals(apiList, dbList, "List mismatch with contains");
    }

    public static void softAssertTrueToCheckDuplicateValue(List dbValue) {
        Assert.assertTrue(dbValue.size() == 0, "Duplicate values found: " + dbValue.size());
    }

    public static void softAssertTrueNumberOfRecordsVerification(List apiList, List dbList) {
        Assert.assertEquals(apiList.size(), dbList.size(), "Record count mismatch");
    }

    public static void softAssertMatchTwoList(List list1, List list2) {
        list1.removeIf(item -> item == null || "null".equals(item));
        list2.removeIf(item -> item == null || "null".equals(item));
        Collections.sort(list1);
        Collections.sort(list2);
        softAssert.assertEquals(list1, list2, "Lists mismatch");
    }

    public static void softAssertTrueForIntegerConvertedList(List apiList, List dbList) {
        apiList.removeIf(item -> item == null || "null".equals(item));
        dbList.removeIf(item -> item == null || "null".equals(item));
        List<Integer> updatedApiList = new ArrayList<>(apiList);
        List<Integer> updatedDbList = new ArrayList<>(dbList);
        Collections.sort(updatedApiList);
        Collections.sort(updatedDbList);
        softAssert.assertEquals(updatedApiList, updatedDbList, "Integer list mismatch");
    }

    public static void softAssertTrueOneByOneWithContainsMethod(List apiList, List dbList) {
        apiList.removeIf(item -> item == null || "null".equals(item));
        dbList.removeIf(item -> item == null || "null".equals(item));
        Collections.sort(apiList);
        Collections.sort(dbList);
        for (int i = 0; i < apiList.size(); i++) {
            softAssert.assertTrue(dbList.get(i).toString().contains(apiList.get(i).toString()),
                    "Mismatch at index " + i);
        }
    }

    public static void softAssertAll() {
        softAssert.assertAll();
    }

    public static void compareColumnNameAndDataTypeListElementWise(List<String> postgresList, List<String> trinoList) {
        SoftAssert localSoftAssert = new SoftAssert();
        List<String> convertedOracleList = convertOracleToTrino(postgresList);

        if (convertedOracleList.size() != trinoList.size()) {
            logger.error("❌ Lists are of different sizes! Oracle: " + convertedOracleList.size() + " Trino: " + trinoList.size());
            localSoftAssert.fail("List sizes do not match!");
        }
        //Comparing the column name and its data type
        for (int i = 0; i < convertedOracleList.size(); i++) {
            String expected = convertedOracleList.get(i);
            String actual = trinoList.get(i);
            localSoftAssert.assertEquals(actual, expected, "Mismatch at index " + i);
        }
        localSoftAssert.assertAll();
    }

    public static void assertTrue(int list1, int list2) {
        Assert.assertEquals(list1, list2, "Mismatch between values: " + list1 + " and " + list2);
    }

    public static void assertTrue(int value) {
        Assert.assertEquals(value, 0, "Having duplicate rows and count is: " + value);
    }

    public static void assertTrueSoft(int value) {
        softAssert.assertEquals(value, 0, "Duplicate rows found: " + value);
    }

    public static void validateNumberOfRecords(int list1, int list2) {
        softAssert.assertEquals(list1, list2, "Mismatch in number of records");
    }

    public static void assertTrueCompareListElementWiseSoftAssertion(ArrayList<Object> oracleList, ArrayList<Object> trinoList) {
        SoftAssert localSoftAssert = new SoftAssert();

        if (oracleList.size() != trinoList.size()) {
            localSoftAssert.fail("List size mismatch: Oracle=" + oracleList.size() + ", Trino=" + trinoList.size());
        }

        for (int i = 0; i < oracleList.size(); i++) {
            String oracleValue = getStringValue(oracleList.get(i));
            String trinoValue = getStringValue(trinoList.get(i));
            localSoftAssert.assertEquals(trinoValue, oracleValue, "Mismatch at index " + i);
        }

        localSoftAssert.assertAll();
    }

    private static String getStringValue(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof List) return ((List<?>) obj).toString();
        return obj.toString();
    }
}
