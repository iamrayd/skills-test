/*
convert_file.py — Command-line interface for converting expressions in a text file.

Usage:
  # Convert infix (one per line) to postfix:
  python convert_file.py --mode infix2postfix --input exercises/sample_input/infix.txt --output out_postfix.txt

  # Convert postfix (one per line) to infix:
  python convert_file.py --mode postfix2infix --input exercises/sample_input/postfix.txt --output out_infix.txt
*/

// ConvertFile.java - Convert expression files
public class ConvertFile {

  public static void main(String[] args) {
    String mode = null;
    String input = null;
    String output = null;

    // Read command line arguments
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--mode") && i + 1 < args.length) {
        mode = args[i + 1];
        i++;
      } else if (args[i].equals("--input") && i + 1 < args.length) {
        input = args[i + 1];
        i++;
      } else if (args[i].equals("--output") && i + 1 < args.length) {
        output = args[i + 1];
        i++;
      }
    }

    // Check if all arguments are provided
    if (mode == null || input == null || output == null) {
      System.out.println("Usage: java ConvertFile --mode <mode> --input <file> --output <file>");
      System.out.println("  mode: infix2postfix or postfix2infix");
      System.out.println("  input: input file path");
      System.out.println("  output: output file path");
      return;
    }

    // Check mode
    if (!mode.equals("infix2postfix") && !mode.equals("postfix2infix")) {
      System.out.println("Error: mode must be 'infix2postfix' or 'postfix2infix'");
      return;
    }

    // Convert file
    Expressions.convertFile(mode, input, output);
    System.out.println("Done. Wrote converted expressions to " + output);
  }
}
