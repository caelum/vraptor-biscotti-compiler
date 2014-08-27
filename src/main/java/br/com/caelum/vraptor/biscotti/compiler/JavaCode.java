package br.com.caelum.vraptor.biscotti.compiler;

public class JavaCode {

	private final String content;
	private final String path;
	private final String name;

	public JavaCode(String name, String path, String content) {
		this.name = name;
		this.path = path;
		this.content = content;
	}

	public String getJava(String language) {
		return "package i18n;\n@javax.enterprise.inject.Vetoed\n"
				+ "public class " + getTypeNameForLanguage(language) + getExtension(language) + " {\n" + content + "}";
	}

	private String getExtension(String language) {
		if(language.isEmpty()) return "";
		return " extends " + name;
	}

	private String getTypeNameForLanguage(String language) {
		if(language.isEmpty()) return name;
		return name + "_" + language;
	}

	public String getFilename(String language) {
		return getTypeNameForLanguage(language) + ".java";
	}

}
