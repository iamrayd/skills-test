// TestExpressions.java - Test the Expressions class
public class TestExpressions {
    static int passed = 0;
    static int failed = 0;

    // Check if two strings are equal
    static void checkEqual(String value1, String value2, String testName) {
        if (value1.equals(value2)) {
            System.out.println("✓ PASS: " + testName);
            passed++;
        } else {
            System.out.println("✗ FAIL: " + testName);
            System.out.println("  Expected: " + value2);
            System.out.println("  Got:      " + value1);
            failed++;
        }
    }

    // Test roundtrip conversions
    static void testRoundtrip() {
        System.out.println("\n=== Test: Roundtrip Conversions ===");

        String[] testCases = {
                "A * ( B + C ) - D / E",
                "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3",
                "-A + B",
                "A * -B + C",
                "-(A + B) * C"
        };

        for (String infix : testCases) {
            try {
                // Convert infix -> postfix -> infix -> postfix
                String postfix1 = Expressions.infixToPostfix(infix);
                String backToInfix = Expressions.postfixToInfix(postfix1);
                String postfix2 = Expressions.infixToPostfix(backToInfix);

                // The two postfix versions should be the same
                checkEqual(postfix2, postfix1, "Roundtrip: " + infix);
            } catch (Exception e) {
                System.out.println("✗ FAIL: Error with " + infix);
                System.out.println("  " + e.getMessage());
                failed++;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Running Expression Tests...");
        System.out.println("==================================================");

        testRoundtrip();

        System.out.println("\n==================================================");
        System.out.println("Tests Passed: " + passed);
        System.out.println("Tests Failed: " + failed);
        System.out.println("==================================================");

        if (failed == 0) {
            System.out.println("\n✓ All tests passed!");
        }
    }
}