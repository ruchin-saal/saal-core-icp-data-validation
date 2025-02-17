package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssertHelpers {
    private static final Logger logger = LogManager.getLogger(AssertHelpers.class);
    static SoftAssertions softAssert=new SoftAssertions();

    public static void assertTrueCompareListElementWise(List apiList, List dbList){
        apiList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == null);
        Collections.sort(apiList);
        Collections.sort(dbList);
        for (int i = 0; i < apiList.size(); i++) {
            Assertions.assertTrue(apiList.get(i).equals(dbList.get(i)));
        }
    }

    public static void assertTrue(List apiList, List dbList){
        apiList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == null);
        Collections.sort(apiList);
        Collections.sort(dbList);
        Assertions.assertTrue(apiList.equals(dbList));

    }


    public static void softAssertTrue(List apiList, List dbList){
        apiList.removeIf(item -> item == null);
        apiList.removeIf(item -> item == "null");
        dbList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == "null");
        Collections.sort(apiList);
        Collections.sort(dbList);
        if(apiList.equals(dbList)) {
            logger.info("Assertion Passed number of records in API column is: "+apiList.size()+" number of records in DB is: "+dbList.size());
        }
        if(!apiList.equals(dbList)) {
            logger.info("Assertion Failed API data is not matching with DB data \nAPI DATA: \n" + apiList + "\nDB data: \n"+ dbList+"\n number of records in API column is: "+apiList.size()+" number of records in DB is: "+dbList.size());
            logger.info("This is value is not in API"+GenericFun.missingDataInSecondList(dbList,apiList));
            logger.info("This is value is not in DB"+GenericFun.missingDataInSecondList(apiList, dbList));
        }
//        Assertions.assertTrue(apiList.equals(dbList));
    }

    public static void softAssertTrueCompareListElementWise(List apiList, List dbList){
        apiList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == null);
        Collections.sort(apiList);
        Collections.sort(dbList);
        for (int i = 0; i < apiList.size(); i++) {
            Assertions.assertTrue(apiList.get(i).equals(dbList.get(i)));
        }
    }

    public static void softAssertTrueCompareListElementWiseWithContains(List apiList, List dbList) {
        apiList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == null);
        Collections.sort(apiList);
        Collections.sort(dbList);


            if (!apiList.equals(dbList)) {
                logger.info("Assertion Failed due to Data Mismatch \n\n\n\n\nAPI DATA: \n" + apiList + "\n\n\n\n\nDB data: " + dbList + "\n\n\n\n\n number of records in API column is: " + apiList.size() + " number of records in DB is: " + dbList.size());

        }
        Assertions.assertTrue(apiList.equals(dbList));



//
//        for (int i = 0; i < dbList.size(); i++) {
//            if (!apiList.get(i).equals(dbList.get(i))) {
//                logger.info("Data Mismatch \nAPI DATA: " + apiList.get(i).toString() + "\nDB data: " + dbList.get(i).toString() + "\n number of records in API column is: " + apiList.size() + " number of records in DB is: " + dbList.size());
//            }
//        }
//
//        for (int i = 0; i < dbList.size(); i++) {
//            softAssert.assertTrue(apiList.get(i).equals(dbList.get(i)),
//                    "\nAPI data: " + apiList.get(i) + " is not matching with \nDB data: "
//                            + dbList.get(i) + " number of records in API column is: " + apiList.size() + " number of records in DB is: " + dbList.size());
//        }
    }

    public static void softAssertTrueToCheckDuplicateValue(List dbValue){
        Assertions.assertTrue(dbValue.size()==0);
            if(dbValue.size()==0){
                logger.info("DUPLICATE RECORD NOT FOUND");
            }
    }

    public static void softAssertTrueNumberOfRecordsVerification(List apiList, List dbList){
//        apiList.removeIf(item -> item == null);
//        dbList.removeIf(item -> item == null);
//        Collections.sort(apiList);
//        Collections.sort(dbList);
//        if(!apiList.equals(dbList)) {
//            logger.info("Failed API data is not matching with DB data \nAPI DATA: \n" + apiList + "\nDB data: \n"+ dbList+"\n number of records in API column is: "+apiList.size()+" number of records in DB is: "+dbList.size());
//        }
        Assertions.assertTrue(apiList.size() == dbList.size());
    }

    public static void softAssertMatchTwoList(List list1, List list2){
        list1.removeIf(item -> item == null);
        list1.removeIf(item -> item == "null");
        list2.removeIf(item -> item == null);
        list2.removeIf(item -> item == "null");
        Collections.sort(list1);
        Collections.sort(list2);
        if(list1.equals(list2)) {
            logger.info("Assertion Passed number of records in API column is: "+list1.size()+" number of records in DB is: "+list2.size());
        }
        if(!list1.equals(list2)) {
            logger.info("Assertion Failed API data is not matching with DB data \nAPI DATA: \n" + list1 + "\nDB data: \n"+ list2+"" +
                    "\n number of records in API column is: "+list1.size()+" number of records in DB is: "+list2.size());
        }
        Assertions.assertTrue(list1.equals(list2));
    }

    public static void softAssertTrueForIntegerConvertedList(List apiList, List dbList){
        apiList.removeIf(item -> item == null);
        apiList.removeIf(item -> item == "null");
        dbList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == "null");
        List<Integer> updatedApiList = new ArrayList<>();
        updatedApiList.addAll(apiList);
        List<Integer> updatedDbList = new ArrayList<>();
        updatedDbList.addAll(dbList);

        Collections.sort(updatedApiList);
        Collections.sort(updatedDbList);
        if(updatedApiList.equals(updatedDbList)) {
            logger.info("Assertion Passed number of records in API column is: "+apiList.size()+" number of records in DB is: "+dbList.size());
        }
        if(!updatedApiList.equals(updatedDbList)) {
            logger.info("Assertion Failed API data is not matching with DB data \nAPI DATA: \n" + apiList + "\nDB data: \n"+ dbList+"\n number of records in API column is: "+apiList.size()+" number of records in DB is: "+dbList.size());
        }
        Assertions.assertTrue(updatedApiList.equals(updatedDbList));
    }

    public static void softAssertTrueOneByOneWithContainsMethod(List apiList, List dbList){
        apiList.removeIf(item -> item == null);
        apiList.removeIf(item -> item == "null");
        dbList.removeIf(item -> item == null);
        dbList.removeIf(item -> item == "null");
        Collections.sort(apiList);
        Collections.sort(dbList);
        Boolean flag=Boolean.FALSE;
        for(int i=0;i<apiList.size();i++){
            if(dbList.get(i).toString().contains(apiList.get(i).toString())) {
                flag=Boolean.TRUE;
            }else {
                logger.info("Assertion Failed API value is: "+apiList.get(i)+" not matching with DB value: "+dbList.get(i)+
                        "\n total record in api list: "+apiList.size()+" total record in db list"+dbList.size());
                flag=Boolean.FALSE;
            }
        }
        if (flag=Boolean.TRUE){
            logger.info("Assertion Passed");
        }

        for(int i=0;i<apiList.size();i++){
                Assertions.assertTrue(dbList.get(i).toString().contains(apiList.get(i).toString()));
            }
    }
    public static void softAssertAll(){
        softAssert.assertAll();
    }
}
