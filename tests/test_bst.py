import unittest
from src.bst import BST

class TestBST(unittest.TestCase):
    def test_insert_and_traversals(self):
        bst = BST()
        for x in [7, 3, 9, 1, 5, 8, 10, 5, 5]:
            bst.insert(x)
        self.assertEqual(bst.inorder(), [1,3,5,5,5,7,8,9,10])
        self.assertEqual(bst.preorder(), [7,3,1,5,5,5,9,8,10])
        self.assertEqual(bst.postorder(), [1,5,5,5,3,8,10,9,7])
        self.assertEqual(bst.level_order(), [7,3,9,1,5,5,5,8,10])

    def test_search_and_remove(self):
        bst = BST()
        for x in [5,3,7,2,4,6,8,5,5]:
            bst.insert(x)
        self.assertTrue(bst.search(7))
        self.assertFalse(bst.search(9))
        # remove one duplicate
        self.assertTrue(bst.remove(5))
        self.assertEqual(bst.inorder(), [2,3,4,5,5,6,7,8])
        # remove last duplicates
        self.assertTrue(bst.remove(5))
        self.assertTrue(bst.remove(5))
        self.assertFalse(bst.search(5))
        # remove root with two children
        self.assertTrue(bst.remove(6))
        self.assertIn(7, bst.inorder())

if __name__ == '__main__':
    unittest.main()
