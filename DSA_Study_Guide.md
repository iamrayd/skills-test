# Data Structures & Algorithms: Skill Test Study Pack

**Topics covered**
1. Binary Search Trees (BST)
2. Tree traversals: **inorder, preorder, postorder, level-order**
3. Delete / **remove** in BST
4. Expression conversion: **infix ↔ postfix** (plus reading/writing from text files)

> Language: Python 3 (easy to translate to C++/Java).  
> Tip: Skim the “What/Why”, then practice with the **Exercises** at the end of each lesson.

---

## Lesson 1 — Binary Search Trees (BST)

### What & Why
A **BST** is a binary tree where for every node `x`:
- keys in `x.left` \< `x.key`
- keys in `x.right` \> `x.key`  
(We keep **duplicates** at the same node with a `count`.)

**Why use a BST?** For ordered data with fast **search / insert / delete**:  
- Average: `O(log n)` (near-balanced)
- Worst (degenerated chain): `O(n)`

### Core operations
- **insert(k)**: go left/right until a null child; if equal, bump `count`.
- **search(k)**: compare and go left/right.
- **remove(k)**: three cases  
  1) Leaf → remove.  
  2) One child → bypass.  
  3) Two children → replace node with its **in-order successor** (min of right subtree) and remove that successor.

### Sample code (BST with duplicates + traversals)
```python
from collections import deque

class BSTNode:
    def __init__(self, key):
        self.key = key; self.left = None; self.right = None; self.count = 1

class BST:
    def __init__(self, key_fn=lambda x: x):
        self.root = None; self.key_fn = key_fn; self._size = 0

    def insert(self, key):
        def _ins(node, key):
            if not node:
                self._size += 1; return BSTNode(key)
            k, nk = self.key_fn(key), self.key_fn(node.key)
            if k < nk:   node.left = _ins(node.left, key)
            elif k > nk: node.right = _ins(node.right, key)
            else:        node.count += 1
            return node
        self.root = _ins(self.root, key)

    def search(self, key):
        node, target = self.root, self.key_fn(key)
        while node:
            nk = self.key_fn(node.key)
            node = node.left if target < nk else node.right if target > nk else None; 
            if nk == target: return True
        return False

    # ... see src/bst.py for full implementation (traversals + remove)
```

### Worked example
Insert: `7, 3, 9, 1, 5, 8, 10, 5, 5`  
- Inorder → `[1, 3, 5, 5, 5, 7, 8, 9, 10]` (sorted)
- Preorder → `[7, 3, 1, 5, 5, 5, 9, 8, 10]`
- Postorder → `[1, 5, 5, 5, 3, 8, 10, 9, 7]`
- Level-order → `[7, 3, 9, 1, 5, 5, 5, 8, 10]`

### Exercises
1) Build a BST by inserting `50, 30, 70, 20, 40, 60, 80, 30`.  
   - a) Give **inorder / preorder / postorder / level-order**.  
   - b) What is the **height** of this tree?  
2) For the same tree, **search** for `60` and `25`. How many comparisons?  
3) Insert keys `15, 55` into your tree. Show the new **inorder**.

> Check `./src/bst.py` for a runnable class and write a small driver like:
```python
from src.bst import BST
bst = BST()
for x in [50,30,70,20,40,60,80,30]: bst.insert(x)
print("Inorder", bst.inorder())
```

---

## Lesson 2 — Tree Traversals

### Four patterns
- **Inorder (L, N, R)** → ascending order for BSTs.  
- **Preorder (N, L, R)** → copy/serialize a tree.  
- **Postorder (L, R, N)** → delete/free a tree bottom-up.  
- **Level-order (BFS)** → visit by depth, uses a queue.

### Code snippets
```python
def inorder(node):
    if node: yield from inorder(node.left); 
             for _ in range(node.count): yield node.key; 
             yield from inorder(node.right)

def preorder(node):
    if node: 
        for _ in range(node.count): yield node.key
        yield from preorder(node.left); yield from preorder(node.right)

def postorder(node):
    if node: yield from postorder(node.left); yield from postorder(node.right); 
             for _ in range(node.count): yield node.key

from collections import deque
def level_order(root):
    if not root: return
    q = deque([root])
    while q:
        node = q.popleft()
        for _ in range(node.count): yield node.key
        if node.left: q.append(node.left)
        if node.right: q.append(node.right)
```

### Exercises
1) For the BST from Lesson 1 example, trace **level-order** and list the queue contents at each step.  
2) Given the following traversals of a BST:  
   - Inorder: `1, 3, 4, 6, 7, 8, 10, 13, 14`  
   - Preorder: `8, 3, 1, 6, 4, 7, 10, 14, 13`  
   Reconstruct the tree (draw it) and verify the **postorder**.  
3) If all keys were inserted in **ascending** order, what is the time complexity of inorder traversal? Why?

---

## Lesson 3 — Deleting from a BST (remove)

### Cases
1) **Leaf** → replace by `None`.  
2) **One child** → replace node by its child.  
3) **Two children** → copy **in-order successor** (min of right subtree) into the node, then delete the successor node.

> Our implementation treats **duplicates** by keeping a `count`. Removing a key first **decrements** the count; the actual node disappears when count hits 0.

### Gotchas
- Always handle **mismatched** comparisons consistently (use a single `key_fn`).  
- When replacing by successor, be careful not to **double-decrement** the size.  
- Test removing: (a) leaf, (b) node with 1 child, (c) node with 2 children, (d) keys with duplicates.

### Exercises
1) Start with the tree from Lesson 1. **Remove 7** (root with two children). Show the new inorder.  
2) Remove `5` **three times**. What are the inorder results after each removal?  
3) Insert `6, 6, 6` and then remove `6` twice. What is `search(6)` after each removal?

---

## Lesson 4 — Expression conversion: infix ↔ postfix

### Why postfix (Reverse Polish Notation)?
- No parentheses needed when operator precedences are respected.
- Easy to evaluate with a stack.

### Infix → Postfix (Shunting-yard algorithm)
**Idea:** output operands immediately; push operators to a stack; pop higher/equal precedence operators when needed.  
- Precedence: `^` \> `*` `/` \> `+` `-`  
- `^` and unary minus are **right-associative**.  
- Parentheses **force** order.

### Postfix → Infix
**Idea:** scan tokens; push operands; for each operator, pop 2 (or 1 for unary), combine with parentheses when required by precedence/associativity.

### Sample code
See `src/expressions.py` for complete implementations:
```python
from src.expressions import infix_to_postfix, postfix_to_infix
print(infix_to_postfix("A * ( B + C ) - D / E"))
# -> A B C + * D E / -
print(postfix_to_infix("A B C + * D E / -"))
# -> A * (B + C) - D / E
```

**Unary minus** is encoded as `"~"` in postfix:
- `-A + B`  → `A ~ B +`
- `A * -B + C` → `A B ~ * C +`

### File I/O helper (batch conversion)
Use the CLI wrapper to convert one expression **per line**:
```bash
# Infix -> Postfix
python src/convert_file.py --mode infix2postfix --input exercises/sample_input/infix.txt --output out_postfix.txt

# Postfix -> Infix
python src/convert_file.py --mode postfix2infix --input exercises/sample_input/postfix.txt --output out_infix.txt
```

### Exercises
Convert the following (show your stack contents as you go):

**A. Infix → Postfix**
1) `A + B * C`  
2) `(A + B) * (C + D)`  
3) `A ^ B ^ C`  
4) `-A + (B - C) * D`  
5) `3 + 4 * 2 / (1 - 5) ^ 2 ^ 3`

**B. Postfix → Infix**
1) `A B + C D + *`  
2) `A B C * + D E / -`  
3) `A B ~ * C +`  
4) `x y z + ^ a b / -`

---

## How to run tests (optional)
```bash
# From the pack's root folder:
python -m unittest discover -s tests -v
```

## Quick checklist for the skill test
- [ ] I can explain each traversal and its **order of visits**.  
- [ ] I can delete nodes in all **three removal cases**.  
- [ ] I know average vs worst-case BST time complexities.  
- [ ] I can run **infix ↔ postfix** conversion by hand and in code.  
- [ ] I can do file-based batch conversion with the provided script.
