package edu.vestrin.wigelltravels.unit;

import edu.vestrin.wigelltravels.Util.StringNormalizer;
import edu.vestrin.wigelltravels.exceptions.InvalidPhoneNumException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringNormalizerTest {

    @Test
    void phoneNumber_InvalidFormat_ThrowsException() {
        String invalidPhone = "123-456";

        assertThrows(InvalidPhoneNumException.class, () -> {
            StringNormalizer.phoneNumber(invalidPhone);
        });
    }

    @Test
    void name_IsNormalized_Uppercase() {
        String input = "kaLlE";

        String result = StringNormalizer.name(input);

        assertEquals("Kalle", result);
    }

    @Test
    void name_EmptyStringThrows() {
        String input = "    ";
        assertThrows(StringIndexOutOfBoundsException.class, () ->
                StringNormalizer.name(input));
    }
}
