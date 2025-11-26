/*
bst.py — Binary Search Tree (BST) implementation

Features
- insert(key) with duplicate handling (counts)
- search(key) -> bool
- remove(key) -> bool (deletes one occurrence; returns False if not found)
- Traversals: inorder(), preorder(), postorder(), level_order()
- __len__ for number of *distinct* keys (nodes), not total duplicates

Usage
>>> from bst import BST
>>> bst = BST()
>>> for x in [7, 3, 9, 1, 5, 8, 10, 5, 5]:
...     bst.insert(x)
>>> bst.inorder()
[1, 3, 5, 5, 5, 7, 8, 9, 10]
>>> bst.remove(7)
True
>>> bst.inorder()
[1, 3, 5, 5, 5, 8, 9, 10]
*/

// BST.java - Simple Binary Search Tree with duplicates
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BST {
    Node root;
    int size; // number of nodes

    public BST() {
        root = null;
        size = 0;
    }

    // Insert a number
    public void insert(int data) {
        root = insertHelper(root, data);
    }

    private Node insertHelper(Node root, int data) {
        if (root == null) {
            size++;
            return new Node(data);
        }

        if (data < root.data) {
            root.left = insertHelper(root.left, data);
        } else if (data > root.data) {
            root.right = insertHelper(root.right, data);
        } else {
            // duplicate - just increase count
            root.count++;
        }
        return root;
    }

    // Search for a number
    public boolean search(int data) {
        return searchHelper(root, data);
    }

    private boolean searchHelper(Node root, int data) {
        if (root == null) {
            return false;
        } else if (root.data == data) {
            return true;
        } else if (data < root.data) {
            return searchHelper(root.left, data);
        } else {
            return searchHelper(root.right, data);
        }
    }

    // Remove one occurrence of a number
    public boolean remove(int data) {
        if (!search(data)) {
            return false;
        }
        root = removeHelper(root, data);
        return true;
    }

    private Node removeHelper(Node root, int data) {
        if (root == null) {
            return null;
        }

        if (data < root.data) {
            root.left = removeHelper(root.left, data);
        } else if (data > root.data) {
            root.right = removeHelper(root.right, data);
        } else {
            // found it!
            if (root.count > 1) {
                // has duplicates, just decrease count
                root.count--;
                return root;
            }

            // only 1 occurrence, need to remove node
            if (root.left == null && root.right == null) {
                // leaf node
                size--;
                return null;
            } else if (root.right != null) {
                // has right child, use successor
                root.data = successor(root);
                root.count = 1;
                root.right = removeHelper(root.right, root.data);
            } else {
                // only has left child, use predecessor
                root.data = predecessor(root);
                root.count = 1;
                root.left = removeHelper(root.left, root.data);
            }
        }
        return root;
    }

    private int successor(Node root) {
        root = root.right;
        while (root.left != null) {
            root = root.left;
        }
        return root.data;
    }

    private int predecessor(Node root) {
        root = root.left;
        while (root.right != null) {
            root = root.right;
        }
        return root.data;
    }

    // INORDER: Left -> Node -> Right (sorted order)
    public ArrayList<Integer> inorder() {
        ArrayList<Integer> list = new ArrayList<>();
        inorderHelper(root, list);
        return list;
    }

    private void inorderHelper(Node root, ArrayList<Integer> list) {
        if (root != null) {
            inorderHelper(root.left, list);
            // add the number 'count' times
            for (int i = 0; i < root.count; i++) {
                list.add(root.data);
            }
            inorderHelper(root.right, list);
        }
    }

    // PREORDER: Node -> Left -> Right
    public ArrayList<Integer> preorder() {
        ArrayList<Integer> list = new ArrayList<>();
        preorderHelper(root, list);
        return list;
    }

    private void preorderHelper(Node root, ArrayList<Integer> list) {
        if (root != null) {
            for (int i = 0; i < root.count; i++) {
                list.add(root.data);
            }
            preorderHelper(root.left, list);
            preorderHelper(root.right, list);
        }
    }

    // POSTORDER: Left -> Right -> Node
    public ArrayList<Integer> postorder() {
        ArrayList<Integer> list = new ArrayList<>();
        postorderHelper(root, list);
        return list;
    }

    private void postorderHelper(Node root, ArrayList<Integer> list) {
        if (root != null) {
            postorderHelper(root.left, list);
            postorderHelper(root.right, list);
            for (int i = 0; i < root.count; i++) {
                list.add(root.data);
            }
        }
    }

    // LEVEL ORDER: Level by level (BFS)
    public ArrayList<Integer> levelOrder() {
        ArrayList<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.remove();
            for (int i = 0; i < current.count; i++) {
                list.add(current.data);
            }
            if (current.left != null) {
                queue.add(current.left);
            }
            if (current.right != null) {
                queue.add(current.right);
            }
        }
        return list;
    }

    // Display tree (inorder)
    public void display() {
        System.out.println(inorder());
    }

    // Get size
    public int getSize() {
        return size;
    }

    // Main - demo
    public static void main(String[] args) {
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

        System.out.println("Inorder  : " + tree.inorder());
        System.out.println("Preorder : " + tree.preorder());
        System.out.println("Postorder: " + tree.postorder());
        System.out.println("Level    : " + tree.levelOrder());
        System.out.println("Remove 7 : " + tree.remove(7) + " -> " + tree.inorder());
        System.out.println("Remove 5 : " + tree.remove(5) + " -> " + tree.inorder());
    }
}