public class Node {
  int data;
  Node left;
  Node right;

  public Node(int data) {
    this.data = data;
  }
}



public class Main {
  public static void main(String[] args) {
    GraphTraversal g = new GraphTraversal(7);

    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 5);
    g.addEdge(2, 6);

    g.dfs(0);
    g.bfs(0);
  }
}

public class Main {
  public static void main(String[] args) {

    ExpressionConverter converter = new ExpressionConverter();

    String infix = "(A+B)*C";
    String postfix = converter.infixToPostfix(infix);

    System.out.println("INFIX: " + infix);
    System.out.println("POSTFIX: " + postfix);

    String convertedBack = converter.postfixToInfix(postfix);
    System.out.println("BACK TO INFIX: " + convertedBack);
  }
}

public class Main {
  public static void main(String[] args) {

    BinarySearchTree tree = new BinarySearchTree();

    tree.insert(new Node(3));
    tree.insert(new Node(5));
    tree.insert(new Node(6));
    tree.insert(new Node(1));

    System.out.println("INORDER");
    tree.inorder();

    System.out.println("PREORDER");
    tree.preorder();

    System.out.println("POSTORDER");
    tree.postorder();

  }
}

import java.util.*;

public class GraphTraversal {

  private ArrayList<ArrayList<Integer>> graph;
  private int vertices;

  public GraphTraversal(int v) {
    vertices = v;
    graph = new ArrayList<>();

    for (int i = 0; i < v; i++) {
      graph.add(new ArrayList<>());
    }
  }

  // Add an edge (undirected)
  public void addEdge(int u, int v) {
    graph.get(u).add(v);
    graph.get(v).add(u);
  }

  // ------------ DFS (Recursive) ------------
  public void dfs(int start) {
    boolean[] visited = new boolean[vertices];
    System.out.print("DFS: ");
    dfsHelper(start, visited);
    System.out.println();
  }

  private void dfsHelper(int node, boolean[] visited) {
    visited[node] = true;
    System.out.print(node + " ");

    for (int neighbor : graph.get(node)) {
      if (!visited[neighbor]) {
        dfsHelper(neighbor, visited);
      }
    }
  }

  // ------------ BFS (Using Queue) ------------
  public void bfs(int start) {
    boolean[] visited = new boolean[vertices];
    Queue<Integer> queue = new LinkedList<>();

    visited[start] = true;
    queue.add(start);

    System.out.print("BFS: ");

    while (!queue.isEmpty()) {
      int node = queue.poll();
      System.out.print(node + " ");

      for (int neighbor : graph.get(node)) {
        if (!visited[neighbor]) {
          visited[neighbor] = true;
          queue.add(neighbor);
        }
      }
    }

    System.out.println();
  }

}

import java.util.*;

public class ExpressionConverter {

    // ---------------------------------------
    //  UTILITY: PRECEDENCE
    // ---------------------------------------
    static int precedence(char c) {
        switch (c) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            case '^': return 3;
        }
        return -1;
    }

    // ---------------------------------------
    //  1. INFIX → POSTFIX
    // ---------------------------------------
    public static String infixToPostfix(String exp) {

        Stack<Character> stack = new Stack<>();
        String result = "";

        for (char c : exp.toCharArray()) {

            // If operand (A, B, C, 1,2,3...)
            if (Character.isLetterOrDigit(c)) {
                result += c;
            }
            // If (
            else if (c == '(') {
                stack.push(c);
            }
            // If )
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result += stack.pop();
                }
                stack.pop();  // remove (
            }
            // Operator
            else {
                while (!stack.isEmpty() &&
                       precedence(c) <= precedence(stack.peek())) {
                    result += stack.pop();
                }
                stack.push(c);
            }
        }

        // pop remaining operators
        while (!stack.isEmpty()) {
            result += stack.pop();
        }

        return result;
    }

    // ---------------------------------------
    //  2. INFIX → PREFIX
    // ---------------------------------------
    public static String infixToPrefix(String exp) {

        // Step 1: Reverse
        String reversed = new StringBuilder(exp).reverse().toString();

        // Step 2: Swap parentheses
        char[] arr = reversed.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '(') arr[i] = ')';
            else if (arr[i] == ')') arr[i] = '(';
        }
        reversed = new String(arr);

        // Step 3: Convert reversed to postfix
        String postfix = infixToPostfix(reversed);

        // Step 4: Reverse postfix → prefix
        return new StringBuilder(postfix).reverse().toString();
    }

    // ---------------------------------------
    //  3. POSTFIX → INFIX
    // ---------------------------------------
    public static String postfixToInfix(String exp) {
        Stack<String> stack = new Stack<>();

        for (char c : exp.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                stack.push("" + c);
            } else {
                String b = stack.pop();
                String a = stack.pop();
                stack.push("(" + a + c + b + ")");
            }
        }
        return stack.pop();
    }

    // ---------------------------------------
    //  MAIN (TESTING)
    // ---------------------------------------
    public static void main(String[] args) {

        String infix = "(A+B)*C";
        String postfix = "AB+C*";

        System.out.println("INFIX: " + infix);
        System.out.println("POSTFIX: " + infixToPostfix(infix));
        System.out.println("PREFIX: " + infixToPrefix(infix));

        System.out.println("\nPOSTFIX → INFIX");
        System.out.println(postfix + " = " + postfixToInfix(postfix));
    }
}



public class BinarySearchTree {
  Node root;

  public void insert(Node node) {
    root = insertHelper(root, node);
  }

  private Node insertHelper(Node root, Node node) {
    int data = node.data;

    if (root == null) {
      root = node;
      return root;
    } else if (data < root.data) {
      root.left = insertHelper(root.left, node);
    } else {
      root.right = insertHelper(root.right, node);
    }

    return root;
  }

  public void inorder() {
    inorderHelper(root);
  }

  private void inorderHelper(Node root) {
    if (root != null) {
      inorderHelper(root.left);
      System.out.println(root.data);
      inorderHelper(root.right);
    }
  }

  public void preorder() {
    preorderHelper(root);
  }

  private void preorderHelper(Node root) {
    if (root != null) {
      System.out.println(root.data);
      preorderHelper(root.left);
      preorderHelper(root.right);
    }

  }

  public void postorder() {
    postorderHelper(root);
  }

  private void postorderHelper(Node root) {
    if (root != null) {
      postorderHelper(root.left);
      postorderHelper(root.right);
      System.out.println(root.data);
    }
  }

  public boolean search(int data) {
    return searchHelper(root, data);
  }

  private boolean searchHelper(Node root, int data) {

    if (root == null) {
      return false;
    } else if (root.data == data) {
      return true;
    } else if (root.data > data) {
      return searchHelper(root.left, data);
    } else {
      return searchHelper(root.right, data);
    }
  }

  public void remove(int data) {
    if (search(data)) {
      removeHelper(root, data);
    } else {
      System.out.println(data + "cannot be found");
    }
  }

  private Node removeHelper(Node root, int data) {
    if (root == null) {
      return root;
    } else if (data < root.data) {
      root.left = removeHelper(root.left, data);
    } else if (data > root.data) {
      root.right = removeHelper(root.right, data);
    } else {
      if (root.left == null && root.right == null) {
        root = null;
      } else if (root.right != null) {
        root.data = successor(root);
        root.right = removeHelper(root.right, data);
      } else {
        root.data = predecessor(root);
        root.left = removeHelper(root.left, data);
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
}
