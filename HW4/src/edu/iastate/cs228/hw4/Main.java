package edu.iastate.cs228.hw4;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


/**
 * @author Sam Richter
 */
public class Main {


	/**
	 * Parse the archive file into an encoding string and encoded message string.
	 * 
	 * @param file - the file to parse
	 * @return a String[] of length 2 -- the first element is the encoding string and the second is the message string
	 * @throws FileNotFoundException if the filename is invalid
	 */
	private static String[] parseArch(File file) throws FileNotFoundException {
		final String[] lines = new String[2];
		String ext = "";
		final Scanner s = new Scanner(file, "UTF-8").useDelimiter("\n");
		try {
			// always at least 2 lines
			for(int l = 0; s.hasNextLine() && l < lines.length; l++) {
				lines[l] = s.nextLine();
			}
			// handle possible 3rd line
			if(s.hasNextLine()) {
				ext = s.nextLine();
			}
		} catch(Exception e) {} finally {
			s.close();
		}
		if(!ext.isEmpty()) {
			// need to add back the newline
			lines[0] += '\n' + lines[1];
			lines[1] = ext;
		}
		return lines;
	}

	/**
	 * Run the program's user IO.
	 * 
	 * @param root - the root MsgTree node to act upon
	 */
	private static void runIO(MsgTree root) {

		final Scanner s = new Scanner(System.in);
		try {

			System.out.print("Please enter filename to decode: ");
			String fname = s.nextLine();
			System.out.println();
			// extract encoding string and code
			final String[] lines = parseArch(new File(fname));
			// build the tree
			MsgTree.buildTree(root, lines[0]);
			// print the codes
			MsgTree.printCodes(root);
			// decode and print
			System.out.println("\nMESSAGE:");
			System.out.println(MsgTree.decode(root, lines[1]));

		} catch(FileNotFoundException e) {
			System.out.printf("File not found :(\n>> %s\n\n", e.toString());
		} catch(InvalidFormatException e) {
			System.out.println("Invalid or ill-formed encoding encountered :(\n\n");
		} catch(Exception e) {
			System.out.printf("Internal failure occurred :(\n>> %s\n\n", e.toString());
		} finally {
			s.close();
		}

	}


	public static void main(String... args) {

		MsgTree root = new MsgTree();
		runIO(root);

	}


}
