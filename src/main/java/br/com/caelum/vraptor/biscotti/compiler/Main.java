package br.com.caelum.vraptor.biscotti.compiler;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		File input = new File("src/main/resources");
		File baseDir = new File(".");
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-h")) {
				help();
				return;
			}
			if (i == args.length - 1) {
				help();
				return;
			}
			if (args[i].equals("-i")) {
				input = new File(args[++i]);
			} else if (args[i].equals("-d")) {
				baseDir = new File(args[++i]);
			}
		}
		new Compiler(baseDir).compile(input);
	}

	private static void help() {
		System.out.println("arguments:");
		System.out.println("-h				help");
		System.out
				.println("-i input_file	read from input file (default src/main/resources/messages.properties)");
		System.out
				.println("-o output_file	write to output folder (default src/main/java)");
	}

}
