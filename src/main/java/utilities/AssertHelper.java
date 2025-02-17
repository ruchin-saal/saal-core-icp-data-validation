package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class AssertHelper {

    private static final Logger logger = LogManager.getLogger(AssertHelper.class);

    public static void assertTrue(boolean status) {
        Assertions.assertTrue(status);
    }

    public static void assertTrue(boolean status, String message) {
        Assertions.assertTrue(status, message);
    }

    public static void fail(String message) {
        Assertions.fail(message);
    }

    public static void assertFalse(boolean status) {
        Assertions.assertFalse(status);
    }

    public static void doAssertion(String msg, boolean status) {
        Assertions.assertTrue(status, msg);
    }


    public static void doAssertion(String msg, String actual, String expected) {
        if (actual == null || expected == null) {
            logger.error("Actual - " + actual + " & Expected - " + expected + " validation can't be performed because of null values");
            throw new RuntimeException("Actual - " + actual + " & Expected - " + expected + " validation can't be performed because of null values");
        }
        else
            Assertions.assertEquals(expected.toLowerCase(), actual.toLowerCase(), msg);
    }

    public static void doAssertion(String msg, Object actual, Object expected) {
        if (actual == null || expected == null)
            throw new RuntimeException("Actual - " + actual + " & Expected - " + expected + " validation can't be performed because of null values;");
        else
            Assertions.assertEquals(expected, actual, msg);
    }

    public static void assertNotEqual(String msg, String actual, String expected) {
        Assertions.assertNotEquals(expected, actual, msg);
    }

    public static void checkNotNull(Object obj) {
        Assertions.assertNotNull(obj);
    }

    public static void isNull(Object object, String message) {
        Assertions.assertNull(object, message);
    }

    public static void isNotNull(Object object, String message) {
        Assertions.assertNotNull(object, message);
    }

    public static void containsStr(String actualStr, String subStr) {
        assertThat(actualStr, Matchers.containsString(subStr));
    }

    public static void propertyWithValue(Object object, String propertyName, String value) {
        assertThat(object, Matchers.hasProperty(propertyName, equalTo(value)));
    }

    public static void givenValueInList(String str, List<String> list) {
        assertThat(list, Matchers.hasItem(str));
    }

    public static void haveKey(String keyName, Map<String, String> maps) {
        assertThat(maps, Matchers.hasKey(keyName));
    }

    public static void haveValue(String keyValue, Map<String, String> maps) {
        assertThat(maps, Matchers.hasValue(keyValue));
    }

    public static void haveEntry(String keyName, String keyValue, Map<String, String> maps) {
        assertThat(maps, Matchers.hasEntry(keyName, keyValue));
    }

    public static void assertListItems(String message, List list1, List list2) {
        Assert.assertEquals(String.valueOf(list1), String.valueOf(list1), message);

    }
}
