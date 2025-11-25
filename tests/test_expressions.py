import unittest
from src.expressions import infix_to_postfix, postfix_to_infix

class TestExpressions(unittest.TestCase):
    def test_roundtrip(self):
        cases = [
            "A * ( B + C ) - D / E",
            "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3",
            "-A + B",
            "A * -B + C",
            "-(A + B) * C"
        ]
        for inf in cases:
            pf = infix_to_postfix(inf)
            self.assertEqual(postfix_to_infix(pf), postfix_to_infix(pf))  # dummy equality
            # The exact parenthesis layout may differ; checking converting back to postfix
            self.assertEqual(infix_to_postfix(postfix_to_infix(pf)), pf)

if __name__ == '__main__':
    unittest.main()
