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

  private int prec(char c) {
    if (c == '+' || c == '-')
      return 1;
    if (c == '*' || c == '/')
      return 2;
    return 0;
  }

  // -----------------------------
  // INFIX → POSTFIX
  // -----------------------------
  public String infixToPostfix(String infix) {

    StringBuilder out = new StringBuilder();
    Stack<Character> st = new Stack<>();

    for (char c : infix.toCharArray()) {

      if (Character.isLetterOrDigit(c)) {
        out.append(c);
      } 
      else if (c == '(') {
        st.push(c);
      } 
      else if (c == ')') {
        while (st.peek() != '(') {
          out.append(st.pop());
        }
        st.pop();
      } 
      else { 
        while (!st.isEmpty() && prec(st.peek()) >= prec(c)) {
          out.append(st.pop());
        }
        st.push(c);
      }
    }

    while (!st.isEmpty()) {
      out.append(st.pop());
    }

    return out.toString();
  }

  // -----------------------------
  // POSTFIX → INFIX
  // -----------------------------
  public String postfixToInfix(String postfix) {

    Stack<String> st = new Stack<>();

    for (char c : postfix.toCharArray()) {

      if (Character.isLetterOrDigit(c)) {
        st.push("" + c);
      }
      else {
        String b = st.pop();
        String a = st.pop();
        st.push("(" + a + c + b + ")");
      }
    }

    return st.pop();
  }


  // -----------------------------
  // INFIX → PREFIX  (Added)
  // -----------------------------
  public String infixToPrefix(String infix) {

    // Step 1: Reverse the infix expression
    StringBuilder sb = new StringBuilder(infix).reverse();
    char[] arr = sb.toString().toCharArray();

    // Step 2: Swap parentheses
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == '(') arr[i] = ')';
      else if (arr[i] == ')') arr[i] = '(';
    }

    String reversed = new String(arr);

    // Step 3: Convert reversed expression to postfix
    String postfix = infixToPostfix(reversed);

    // Step 4: Reverse postfix → prefix
    return new StringBuilder(postfix).reverse().toString();
  }

  // -----------------------------
  // MAIN
  // -----------------------------
  public static void main(String[] args) {
    ExpressionConverter ec = new ExpressionConverter();

    String infix = "(A+B)*C";
    String postfix = ec.infixToPostfix(infix);
    String prefix = ec.infixToPrefix(infix);

    String postfixx = "AB+CD-+";
    String infixx = ec.postfixToInfix(postfixx);

    System.out.println("Infix: " + infix);
    System.out.println("Postfix: " + postfix);
    System.out.println("Prefix: " + prefix);

    System.out.println("Postfix: " + postfixx);
    System.out.println("Infix: " + infixx);
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
