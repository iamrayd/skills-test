"""
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
"""

from collections import deque
from typing import Optional, Any, List, Callable

class BSTNode:
    __slots__ = ('key','left','right','count')
    def __init__(self, key: Any):
        self.key = key
        self.left: Optional['BSTNode'] = None
        self.right: Optional['BSTNode'] = None
        self.count = 1
        
class BST:
    def __init__(self, key_fn: Callable[[Any], Any] = lambda x: x):
        """Create an empty BST. Optional key_fn extracts a sortable key from stored items."""
        self.root: Optional[BSTNode] = None
        self.key_fn = key_fn
        self._size = 0  # number of distinct nodes
    
    def __len__(self):
        return self._size
    
    # ---------- Core operations ----------
    def insert(self, key: Any):
        """Insert key into the BST. Duplicates are counted at the same node."""
        def _insert(node: Optional[BSTNode], key: Any) -> BSTNode:
            if node is None:
                self._size += 1
                return BSTNode(key)
            k = self.key_fn(key)
            nk = self.key_fn(node.key)
            if k < nk:
                node.left = _insert(node.left, key)
            elif k > nk:
                node.right = _insert(node.right, key)
            else:
                node.count += 1
            return node
        self.root = _insert(self.root, key)
    
    def search(self, key: Any) -> bool:
        """Return True iff key is found in the BST."""
        node = self.root
        target = self.key_fn(key)
        while node:
            nk = self.key_fn(node.key)
            if target < nk:
                node = node.left
            elif target > nk:
                node = node.right
            else:
                return True
        return False
    
    # ---------- Traversals ----------
    def inorder(self) -> List[Any]:
        """Left, Node, Right — ascending order for BSTs."""
        res: List[Any] = []
        def _in(node: Optional[BSTNode]):
            if node:
                _in(node.left)
                res.extend([node.key]*node.count)
                _in(node.right)
        _in(self.root)
        return res
    
    def preorder(self) -> List[Any]:
        """Node, Left, Right — useful for copying trees."""
        res: List[Any] = []
        def _pre(node: Optional[BSTNode]):
            if node:
                res.extend([node.key]*node.count)
                _pre(node.left)
                _pre(node.right)
        _pre(self.root)
        return res
    
    def postorder(self) -> List[Any]:
        """Left, Right, Node — useful for deleting/freeing trees."""
        res: List[Any] = []
        def _post(node: Optional[BSTNode]):
            if node:
                _post(node.left)
                _post(node.right)
                res.extend([node.key]*node.count)
        _post(self.root)
        return res
    
    def level_order(self) -> List[Any]:
        """Breadth-first (by level), also known as BFS traversal."""
        res: List[Any] = []
        if not self.root:
            return res
        q = deque([self.root])
        while q:
            node = q.popleft()
            res.extend([node.key]*node.count)
            if node.left:
                q.append(node.left)
            if node.right:
                q.append(node.right)
        return res
    
    # ---------- Deletion (remove) ----------
    def remove(self, key: Any) -> bool:
        """Remove ONE occurrence of key. Returns True if removed, False if not found.
        If a node has duplicates (count>1), this decrements the count only.
        """
        removed = False
        def _remove(node: Optional[BSTNode], key: Any) -> Optional[BSTNode]:
            nonlocal removed
            if node is None:
                return None
            target = self.key_fn(key)
            nk = self.key_fn(node.key)
            if target < nk:
                node.left = _remove(node.left, key)
            elif target > nk:
                node.right = _remove(node.right, key)
            else:
                # found
                if node.count > 1:
                    node.count -= 1
                    removed = True
                    return node
                # node.count == 1
                if node.left is None:
                    removed = True
                    self._size -= 1
                    return node.right
                if node.right is None:
                    removed = True
                    self._size -= 1
                    return node.left
                # Two children: replace with in-order successor (min of right subtree)
                succ_parent = node
                succ = node.right
                while succ.left:
                    succ_parent = succ
                    succ = succ.left
                # Copy successor's content into node
                node.key = succ.key
                node.count = succ.count
                # Ensure the successor node is removed entirely
                succ.count = 1
                if succ_parent.left is succ:
                    succ_parent.left = _remove(succ_parent.left, succ.key)
                else:
                    succ_parent.right = _remove(succ_parent.right, succ.key)
                removed = True
            return node
        self.root = _remove(self.root, key)
        return removed
    

if __name__ == "__main__":
    # demo
    bst = BST()
    for x in [7, 3, 9, 1, 5, 8, 10, 5, 5]:
        bst.insert(x)
    print("Inorder  :", bst.inorder())
    print("Preorder :", bst.preorder())
    print("Postorder:", bst.postorder())
    print("Level    :", bst.level_order())
    print("Remove 7 :", bst.remove(7), "->", bst.inorder())
    print("Remove 5 :", bst.remove(5), "->", bst.inorder())
