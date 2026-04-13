package edu.vestrin.wigelltravels.Util;

import edu.vestrin.wigelltravels.exceptions.InvalidPhoneNumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for normalizing and formatting common user-provided String inputs.
 * <p>
 * This class contains static helper methods for cleaning and standardizing text values
 * such as names and phone numbers before storing them in the database.
 * </p>
 * <p>
 * This class cannot be instantiated.
 * </p>
 */
public final class StringNormalizer {
    private final static Logger logger = LoggerFactory.getLogger(StringNormalizer.class);

    private StringNormalizer() {}

    public static String name(String name) {
        logger.debug("name() - Försöker normalisera name: {}...", name);
        String cleaned = name.trim();

        String normalized = cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1).toLowerCase();
        logger.debug("name() - Normalisering lyckad: {}", normalized);

        return normalized;
    }

    /**Throws InvalidPhoneNumException if phone number doesn't match excepted format.
     * @param phoneNum phone number to normalize/format.
     * @return normalized phone number as String.
     */
    public static String phoneNumber(String phoneNum) {
        logger.debug("phoneNumber() - Försöker normalisera phoneNum: {}...", phoneNum);
        if (phoneNum == null || phoneNum.isBlank()) {
            logger.warn("phoneNumber() - phoneNum är 'null'.");
            return null;
        }

        String cleaned = phoneNum.replaceAll("[^0-9+]", "");

        if (cleaned.startsWith("07")) {
            logger.debug("phoneNumber() - phoneNum börjar med 07, ersätter med +46.");
            cleaned = "+46" + cleaned.substring(1);
        } else if (cleaned.startsWith("46")) {
            cleaned = "+" + cleaned;
        }

        if (!cleaned.matches("^\\+46\\d{9}$")) throw new InvalidPhoneNumException(cleaned);

        String normalized = String.format("%s %s %s %s %s",
                cleaned.substring(0, 3),  // +46
                cleaned.substring(3, 5),  // 70
                cleaned.substring(5, 8),  // 123
                cleaned.substring(8, 10), // 45
                cleaned.substring(10, 12) // 67
        );
        logger.debug("phoneNumber() - normalisering lyckad. Gammalt: {}, Nytt: {}", phoneNum, normalized);

        return normalized;
    }
}
