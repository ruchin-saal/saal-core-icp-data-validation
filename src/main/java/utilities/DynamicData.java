package utilities;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class deals with creation of dynamic data
 */
public class DynamicData {

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static int generateRandomNumber(int maxSize) {
        int number = 0;
        Random random;
        random = new Random();
        if (maxSize > 0)
            number = random.nextInt(maxSize);

        return number;
    }

    public static int generateRandomNumber(int minSize, int maxSize) {
        Random random = new Random();
        return random.ints(minSize, maxSize)
                .findFirst()
                .getAsInt();
    }

    public static long generateRandomOf13Digit() {
        Random random = new Random();
        long randomNumber = random.nextLong() % 10000000000000L;
        if (randomNumber < 0) {
            randomNumber *= -1;
        }

        return randomNumber;
    }

    public static String generateRandomAlphaNumeric(int length) {
        Random random = new Random();
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        return random.ints(length, 0, alphabet.length())
                .mapToObj(alphabet::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    // ******************** Random Data generation  By Faker ***************************
    public static String getFirstName() {
        Faker faker = new Faker();
        return faker.name().firstName();
    }

    public static String getFirstName(String localType) {
        Faker faker = new Faker(new Locale(localType));
        return faker.name().firstName();
    }

    public static String getLastName() {
        Faker faker = new Faker();
        return faker.name().lastName();
    }

    public static String getEmailAddress() {
        Faker faker = new Faker();
        return faker.internet().safeEmailAddress();
    }

    public static String getEmailAddressWithInternet() {
        Faker faker = new Faker();
        return faker.internet().emailAddress();
    }

    public static String getSignatureId() {
        Faker faker = new Faker();
        return "Sig" + faker.random().hex(9).toLowerCase();
    }

    public static String getAvatarUrl() {
        Faker faker = new Faker();
        return faker.internet().avatar();
    }

    public static String getAuthorId() {
        String authorId;
        Faker faker = new Faker();
        authorId = "Author" + faker.idNumber().valid().replace("-", "");

        return authorId;
    }

    public static String getAuthorName() {
        String authorName;
        Faker faker = new Faker(Locale.FRENCH);
        authorName = faker.book().author();

        return authorName;
    }

    public static String getBookName() {
        Faker faker = new Faker();
        return faker.book().title().replace("'s", "");
    }

    public static String description() {
        Faker faker = new Faker();
        return "";
    }
}
