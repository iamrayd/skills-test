// Node.java - Simple node for BST
public class Node {
  int data;
  Node left;
  Node right;
  int count; // for duplicates

  public Node(int data) {
    this.data = data;
    this.count = 1;
  }
}