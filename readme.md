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

  // ---------------------------
  // INFIX TO POSTFIX
  // ---------------------------
  public String infixToPostfix(String infix) {
    StringBuilder result = new StringBuilder();
    Stack<Character> stack = new Stack<>();

    for (char c : infix.toCharArray()) {

      // 1. If operand → add to output
      if (Character.isLetterOrDigit(c)) {
        result.append(c);
      }

      // 2. If '(' → push
      else if (c == '(') {
        stack.push(c);
      }

      // 3. If ')' → pop until '('
      else if (c == ')') {
        while (!stack.isEmpty() && stack.peek() != '(') {
          result.append(stack.pop());
        }
        stack.pop(); // remove '('
      }

      // 4. Operator
      else {
        while (!stack.isEmpty() &&
            precedence(stack.peek()) >= precedence(c)) {
          result.append(stack.pop());
        }
        stack.push(c);
      }
    }

    // 5. Pop remaining
    while (!stack.isEmpty()) {
      result.append(stack.pop());
    }

    return result.toString();
  }

  // Operator precedence
  private int precedence(char c) {
    return switch (c) {
      case '+', '-' -> 1;
      case '*', '/' -> 2;
      case '^' -> 3;
      default -> -1;
    };
  }

  // ---------------------------
  // POSTFIX TO INFIX
  // ---------------------------
  public String postfixToInfix(String postfix) {
    Stack<String> stack = new Stack<>();

    for (char c : postfix.toCharArray()) {

      // Operand → push
      if (Character.isLetterOrDigit(c)) {
        stack.push(String.valueOf(c));
      }

      // Operator → pop 2, build new string
      else {
        String right = stack.pop();
        String left = stack.pop();
        String expr = "(" + left + c + right + ")";
        stack.push(expr);
      }
    }

    // Final element = complete expression
    return stack.pop();
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
