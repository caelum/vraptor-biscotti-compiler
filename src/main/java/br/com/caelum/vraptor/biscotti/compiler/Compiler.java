package br.com.caelum.vraptor.biscotti.compiler;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Compiler {

	private final File i18nOutput;

	public Compiler(File baseDir) {
		this.i18nOutput = new File(baseDir, "i18n");
		i18nOutput.mkdirs();
	}

	public void compile(File file) throws IOException {
		if (file.isDirectory())
			compileDirectory(file);
		else
			compileFile(file);
	}

	private void compileDirectory(File folder) throws IOException {
		FileFilter isProperties = f -> f.getName().startsWith("messages") && f.getName().endsWith(".properties");
		File[] files = folder.listFiles(isProperties);
		if(files == null) return;
		List<String> languages = Arrays.stream(files).map(this::compileFile).collect(Collectors.toList());
		System.out.println(languages);
		createAllMessagesFor(languages);
	}

	private void createAllMessagesFor(List<String> languages) throws IOException {
		Path path = new File(i18nOutput, "AllMessages.java").toPath();
		List<String> lines = new ArrayList<>();
		lines.add("package i18n;\n");
		lines.add("import java.util.*;");
		lines.add("import javax.enterprise.context.ApplicationScoped;");
		lines.add("import br.com.caelum.vraptor.i18n.*;");
		lines.add("import br.com.caelum.vraptor.biscotti.*;");
		lines.add("import javax.enterprise.inject.Produces;");
		lines.add("import javax.inject.Inject;");

		lines.add("@ApplicationScoped");
		lines.add("public class AllMessages {");
		lines.add("	private Map<String, Messages> messages = new HashMap<>();");
		lines.add("	{");
		languages.stream()
				.map(l -> String.format("		messages.put(\"%s\", new Messages%s());", l, l.isEmpty() ? "" : "_" + l))
				.forEach(lines::add);
		lines.add("	}");
		lines.add("private CurrentLanguage language;");
		lines.add("/** @deprecated CDI only */");
		lines.add("AllMessages() {");
		lines.add("}");
		lines.add("@Inject");
		lines.add("public AllMessages(CurrentLanguage language) {");
		lines.add("this.language = language;");
		lines.add("}");
		lines.add("@Produces");
		lines.add("public Messages messages() {");
		lines.add("return messages.get(language.get());");
		lines.add("}");
		lines.add("}");
		Files.write(path, lines);
	}

	private String compileFile(File file) {
		try {
			System.out.println("Compiling " + file.getName());
			InputStreamReader reader = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			TypeCollector collector = new TypeCollector(file.getName());
			collector.collect(reader);
			List<JavaCode> javas = collector.getRoot().toJava();
			String language = extractLanguageFrom(file.getName());
			for (JavaCode java : javas) {
				save(java, language);
			}
			return language;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String extractLanguageFrom(String name) {
		if(name.equals("messages.properties")) return "";
		return name.substring("messages_".length(), name.length() - ".properties".length());
	}

	private void save(JavaCode code, String language) throws IOException {
		Path path = new File(i18nOutput, code.getFilename(language)).toPath();
		Files.write(path, asList(code.getJava(language)));
	}

}
