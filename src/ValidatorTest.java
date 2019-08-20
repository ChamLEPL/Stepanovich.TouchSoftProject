import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

class ValidatorTest {

    @Test
    void validate_extraBrackets_true() {

        Set<String> actualSet = Validator.validate("{}}{}}");

        Set<String> expectedSet = new LinkedHashSet<>(Arrays.asList("{}{}", "{{}}"));

        Assertions.assertEquals(actualSet, expectedSet);
    }

    @Test
    void validate_extraBracketsTouchTest_true() {

        Set<String> actualSet = Validator.validate("{}}{{}}{");

        Set<String> expectedSet = new LinkedHashSet<>(Arrays.asList("{{}{}}", "{{}}{}", "{}{}{}", "{}{{}}", "{{{}}}"));

        Assertions.assertEquals(expectedSet, actualSet);
    }

    @Test
    void validate_extraBracketsWithXLetters_true() {

        Set<String> actualSet = Validator.validate("{}x}x}");

        Set<String> expectedSet = new LinkedHashSet<>(Arrays.asList("{x}x", "{xx}", "{}xx"));

        Assertions.assertEquals(actualSet, expectedSet);
    }

    @Test
    void validate_notValidInput_emptySet() {

        Set<String> actualSet = Validator.validate("{");

        Set<String> expectedSet = new LinkedHashSet<>(Arrays.asList(""));

        Assertions.assertEquals(actualSet, expectedSet);
    }

    @Test
    void validate_extraBracketsWithAbcSymbols_true() {

        Set<String> actualSet = Validator.validate("{{{{a}{b}c");

        Set<String> expectedSet = new LinkedHashSet<>(Arrays.asList(
                "{}{}abc", "{a}{}bc", "{ab}{}c",
                "{abc}{}", "{ab}c{}", "{ab}{c}",
                "{a}b{}c", "{a}{b}c", "{}a{}bc",
                "{}ab{}c", "{}abc{}", "{}ab{c}",
                "{}a{b}c", "{}{a}bc", "{}{ab}c",
                "{}{abc}",
                "{{}}abc", "{a{}}bc", "{ab{}}c",
                "{abc{}}", "{ab{c}}", "{ab{}c}",
                "{a{b}}c", "{a{}b}c", "{{a}}bc",
                "{{ab}}c", "{{abc}}", "{{ab}c}",
                "{{a}b}c", "{{}a}bc", "{{}ab}c",
                "{{}abc}"
        ));

        Assertions.assertEquals(actualSet, expectedSet);
    }

    @Test
    void validate_checkForNull_true() throws NullPointerException{
        Throwable thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            Validator.validate(null);
        });
    }

    @Test
    void validate_checkForEmpty_true() throws NumberFormatException{
        Throwable thrown = Assertions.assertThrows(NumberFormatException.class, () -> {
            Validator.validate("");
        });
    }

}