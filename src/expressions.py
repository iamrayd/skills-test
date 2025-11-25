"""
expressions.py — Infix <-> Postfix conversions (with file helpers)

- infix_to_postfix(expr: str) -> str
- postfix_to_infix(expr: str) -> str
Both handle unary minus by representing it as "~" in postfix form.

File helpers:
- convert_file(mode, in_path, out_path)
  mode in {"infix2postfix", "postfix2infix"}
  Reads one expression per line and writes converted form per line.
"""
from typing import List

# Precedence and associativity
precedence = {'~': 5, '^': 4, '*': 3, '/': 3, '+': 2, '-': 2}
right_associative = {'^', '~'}

def is_operator(tok: str) -> bool:
    return tok in precedence

def _tokenize(expr: str) -> List[str]:
    import re
    token_specification = [
        ('NUMBER',   r'\d+(\.\d+)?'),
        ('ID',       r'[A-Za-z_]\w*'),
        ('OP',       r'[\+\-\*\/\^\(\)]'),
        ('SKIP',     r'[ \t]+'),
        ('MISMATCH', r'.'),
    ]
    tok_regex = '|'.join('(?P<%s>%s)' % pair for pair in token_specification)
    tokens = []
    for mo in re.finditer(tok_regex, expr):
        kind = mo.lastgroup
        value = mo.group()
        if kind in ('NUMBER', 'ID', 'OP'):
            tokens.append(value)
        elif kind == 'SKIP':
            continue
        else:
            raise ValueError(f'Unexpected character {value!r} in expression: {expr}')
    return tokens

def infix_to_postfix(expr: str) -> str:
    tokens = _tokenize(expr)
    out: List[str] = []
    stack: List[str] = []  # operator stack
    prev_tok_type = None
    for tok in tokens:
        if tok.isdigit() or tok.replace('.', '', 1).isdigit() or tok[0].isalpha() or tok[0] == '_':
            out.append(tok)
            prev_tok_type = 'OPERAND'
        elif tok in ('+', '-', '*', '/', '^'):
            # detect unary minus
            o1 = '~' if tok == '-' and (prev_tok_type is None or prev_tok_type in ('OP', 'LPAREN')) else tok
            while stack and stack[-1] != '(' and (precedence[stack[-1]] > precedence[o1] or
                                                  (precedence[stack[-1]] == precedence[o1] and o1 not in right_associative)):
                out.append(stack.pop())
            stack.append(o1)
            prev_tok_type = 'OP'
        elif tok == '(':
            stack.append('(')
            prev_tok_type = 'LPAREN'
        elif tok == ')':
            while stack and stack[-1] != '(':
                out.append(stack.pop())
            if not stack:
                raise ValueError("Mismatched parentheses")
            stack.pop()  # '('
            prev_tok_type = 'RPAREN'
        else:
            raise ValueError(f"Unknown token: {tok}")
    while stack:
        if stack[-1] in ('(', ')'):
            raise ValueError("Mismatched parentheses")
        out.append(stack.pop())
    return ' '.join(out)

class _Node:
    __slots__ = ('expr','prec','arity')
    def __init__(self, expr: str, prec: int, arity: int = 0):
        self.expr = expr
        self.prec = prec
        self.arity = arity

def _maybe_paren(child: '_Node', parent_prec: int, is_right_child: bool, op: str) -> str:
    if child.arity == 0:
        return child.expr
    need = False
    if child.prec < parent_prec:
        need = True
    elif child.prec == parent_prec:
        if op in ('-','/'):
            if is_right_child:
                need = True
        elif op == '^':
            if not is_right_child:
                need = True
    return f'({child.expr})' if need else child.expr

def postfix_to_infix(postfix: str) -> str:
    tokens = postfix.split()
    stack: List[_Node] = []
    for tok in tokens:
        if is_operator(tok):
            if tok == '~':
                if len(stack) < 1:
                    raise ValueError("Insufficient operands for unary operator '~'")
                a = stack.pop()
                expr = f'-{_maybe_paren(a, precedence[tok], True, tok)}'
                stack.append(_Node(expr, precedence[tok], arity=1))
            else:
                if len(stack) < 2:
                    raise ValueError(f"Insufficient operands for operator {tok}")
                b = stack.pop()
                a = stack.pop()
                a_expr = _maybe_paren(a, precedence[tok], False, tok)
                b_expr = _maybe_paren(b, precedence[tok], True, tok)
                expr = f'{a_expr} {tok} {b_expr}'
                stack.append(_Node(expr, precedence[tok], arity=2))
        else:
            stack.append(_Node(tok, 100, arity=0))
    if len(stack) != 1:
        raise ValueError("Invalid postfix expression")
    return stack[0].expr

# ---------- File helpers ----------
def convert_file(mode: str, in_path: str, out_path: str):
    """Convert lines in a file.
    mode: 'infix2postfix' or 'postfix2infix'
    Each line is treated as a separate expression.
    """
    mode = mode.lower()
    if mode not in ('infix2postfix', 'postfix2infix'):
        raise ValueError("mode must be 'infix2postfix' or 'postfix2infix'")
    out_lines = []
    with open(in_path, 'r', encoding='utf-8') as fin:
        for line_no, line in enumerate(fin, 1):
            s = line.strip()
            if not s:
                out_lines.append('')
                continue
            try:
                if mode == 'infix2postfix':
                    out_lines.append(infix_to_postfix(s))
                else:
                    out_lines.append(postfix_to_infix(s))
            except Exception as e:
                out_lines.append(f'# Error on line {line_no}: {e}')
    with open(out_path, 'w', encoding='utf-8') as fout:
        fout.write('\n'.join(out_lines))

if __name__ == "__main__":
    # demo
    print("Demo infix -> postfix")
    for t in ["A * ( B + C ) - D / E", "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3", "-A + B"]:
        print(t, "->", infix_to_postfix(t))
    print("\nDemo postfix -> infix")
    for t in ["A B C + * D E / -", "3 4 2 * 1 5 - 2 3 ^ ^ / +", "A ~ B +"]:
        print(t, "->", postfix_to_infix(t))
