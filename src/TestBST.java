
// TestBST.java - Test the BST class
import java.util.ArrayList;
import java.util.Arrays;

public class TestBST {
    static int passed = 0;
    static int failed = 0;

    // Check if two lists are the same
    static void checkEqual(ArrayList<Integer> list1, ArrayList<Integer> list2, String testName) {
        boolean same = true;
        if (list1.size() != list2.size()) {
            same = false;
        } else {
            for (int i = 0; i < list1.size(); i++) {
                if (!list1.get(i).equals(list2.get(i))) {
                    same = false;
                    break;
                }
            }
        }

        if (same) {
            System.out.println("✓ PASS: " + testName);
            passed++;
        } else {
            System.out.println("✗ FAIL: " + testName);
            System.out.println("  Expected: " + list2);
            System.out.println("  Got:      " + list1);
            failed++;
        }
    }

    // Check if boolean is true
    static void checkTrue(boolean value, String testName) {
        if (value) {
            System.out.println("✓ PASS: " + testName);
            passed++;
        } else {
            System.out.println("✗ FAIL: " + testName);
            failed++;
        }
    }

    // Check if boolean is false
    static void checkFalse(boolean value, String testName) {
        if (!value) {
            System.out.println("✓ PASS: " + testName);
            passed++;
        } else {
            System.out.println("✗ FAIL: " + testName);
            failed++;
        }
    }

    // Test 1: Insert and Traversals
    static void testInsertAndTraversals() {
        System.out.println("\n=== Test 1: Insert and Traversals ===");
        BST tree = new BST();

        // Insert: 7, 3, 9, 1, 5, 8, 10, 5, 5
        tree.insert(7);
        tree.insert(3);
        tree.insert(9);
        tree.insert(1);
        tree.insert(5);
        tree.insert(8);
        tree.insert(10);
        tree.insert(5);
        tree.insert(5);

        // Check inorder
        ArrayList<Integer> expectedInorder = new ArrayList<>(
                Arrays.asList(1, 3, 5, 5, 5, 7, 8, 9, 10));
        checkEqual(tree.inorder(), expectedInorder, "Inorder traversal");

        // Check preorder
        ArrayList<Integer> expectedPreorder = new ArrayList<>(
                Arrays.asList(7, 3, 1, 5, 5, 5, 9, 8, 10));
        checkEqual(tree.preorder(), expectedPreorder, "Preorder traversal");

        // Check postorder
        ArrayList<Integer> expectedPostorder = new ArrayList<>(
                Arrays.asList(1, 5, 5, 5, 3, 8, 10, 9, 7));
        checkEqual(tree.postorder(), expectedPostorder, "Postorder traversal");

        // Check level order
        ArrayList<Integer> expectedLevel = new ArrayList<>(
                Arrays.asList(7, 3, 9, 1, 5, 5, 5, 8, 10));
        checkEqual(tree.levelOrder(), expectedLevel, "Level-order traversal");
    }

    // Test 2: Search and Remove
    static void testSearchAndRemove() {
        System.out.println("\n=== Test 2: Search and Remove ===");
        BST tree = new BST();

        // Insert: 5, 3, 7, 2, 4, 6, 8, 5, 5
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(2);
        tree.insert(4);
        tree.insert(6);
        tree.insert(8);
        tree.insert(5);
        tree.insert(5);

        // Test search
        checkTrue(tree.search(7), "Search for 7 (exists)");
        checkFalse(tree.search(9), "Search for 9 (doesn't exist)");

        // Remove one duplicate of 5
        checkTrue(tree.remove(5), "Remove 5 (first time)");
        ArrayList<Integer> after1 = new ArrayList<>(
                Arrays.asList(2, 3, 4, 5, 5, 6, 7, 8));
        checkEqual(tree.inorder(), after1, "After removing one 5");

        // Remove more 5s
        checkTrue(tree.remove(5), "Remove 5 (second time)");
        checkTrue(tree.remove(5), "Remove 5 (third time)");
        checkFalse(tree.search(5), "Search for 5 after removing all");

        // Remove node with two children
        checkTrue(tree.remove(6), "Remove 6 (node with two children)");
        checkTrue(tree.inorder().contains(7), "Tree still has 7");
    }

    public static void main(String[] args) {
        System.out.println("Running BST Tests...");
        System.out.println("==================================================");

        testInsertAndTraversals();
        testSearchAndRemove();

        System.out.println("\n==================================================");
        System.out.println("Tests Passed: " + passed);
        System.out.println("Tests Failed: " + failed);
        System.out.println("==================================================");

        if (failed == 0) {
            System.out.println("\n✓ All tests passed!");
        }
    }
}