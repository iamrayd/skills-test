"""
convert_file.py — Command-line interface for converting expressions in a text file.

Usage:
  # Convert infix (one per line) to postfix:
  python convert_file.py --mode infix2postfix --input exercises/sample_input/infix.txt --output out_postfix.txt

  # Convert postfix (one per line) to infix:
  python convert_file.py --mode postfix2infix --input exercises/sample_input/postfix.txt --output out_infix.txt
"""
import argparse
from expressions import convert_file

def main():
    p = argparse.ArgumentParser()
    p.add_argument('--mode', required=True, choices=['infix2postfix', 'postfix2infix'])
    p.add_argument('--input', required=True, help='path to input .txt file (one expression per line)')
    p.add_argument('--output', required=True, help='path to output .txt file')
    args = p.parse_args()
    convert_file(args.mode, args.input, args.output)
    print(f"Done. Wrote converted expressions to {args.output}")

if __name__ == "__main__":
    main()
