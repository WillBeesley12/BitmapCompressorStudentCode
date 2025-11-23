/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author William Beesley
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {
        short sequence_length = 0;
        // Make true represent 0s, false represent 1s
        boolean current = true;
        // Go until the end of the bitmap
        while (!BinaryStdIn.isEmpty()) {
            boolean bit = BinaryStdIn.readBoolean();
            if (current) {
                if (!bit) {
                    sequence_length++;
                }
                else {
                    BinaryStdOut.write(sequence_length, 16);
                    // Set sequence length to 1 to count the bit we are looking at right now
                    sequence_length = 1;
                    // Swap between 1s and 0s
                    current = false;
                }
            }
            else {
                if (bit) {
                    sequence_length++;
                }
                else {
                    BinaryStdOut.write(sequence_length, 16);
                    sequence_length = 1;
                    current = true;
                }
            }
        }
        BinaryStdOut.write(sequence_length, 16);
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        // Data starts with 0s
        boolean isOne = false;
        // Don't need to sorry about padding bits because the Shorts have bit sizes that are multiples of 4.
        while (!BinaryStdIn.isEmpty()) {
            // Read in the data 16 bits at a time, the length of a short
            short runLength = (short) BinaryStdIn.readInt(16);
            for (int i = 0; i < runLength; i++) {
                if (isOne) {
                    BinaryStdOut.write(1, 1);
                }
                else {
                    BinaryStdOut.write(0, 1);
                }
            }
            // Change between 0s and 1s
            isOne = !isOne;
        }
        BinaryStdOut.close();
    }

    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}