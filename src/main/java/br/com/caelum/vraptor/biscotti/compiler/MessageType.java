package br.com.caelum.vraptor.biscotti.compiler;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageType {

	private final String path;
	private final Map<String, String> values = new HashMap<>();
	private final Map<String,MessageType> children = new HashMap<>();
	private final String name;

	public MessageType(String name, String path) {
		this.path = path;
		this.name = name;
	}

	public MessageType add(String key, String value) {
		checkExistence(key);
		values.put(key, value);
		return this;
	}

	private void checkExistence(String key) {
		if (values.containsKey(key)) {
			throw new IllegalArgumentException(path + "." + key
					+ " was already defined!");
		}
		if (children.containsKey(key)) {
			throw new IllegalArgumentException(path + "." + key
					+ " was already defined!");
		}
	}

	public MessageType add(MessageType child, String baseName) {
		checkExistence(baseName);
		this.children.put(baseName, child);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageType other = (MessageType) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageType [path=" + path + ", values=" + values
				+ ", children=" + children + "]";
	}

	public List<JavaCode> toJava() {
		List<JavaCode> codes = new ArrayList<>();
		String content = 
				values.keySet()
				.stream()
				.map(key -> "public String " + key + "() { return \"" + escape(values.get(key)) + "\"; }\n")
				.collect(joining());
		content += children.values().stream()
			.map(this::methodForKey)
			.collect(joining());
		codes.add(new JavaCode(name, path, content));

		List<JavaCode> subTypes = children.values().stream()
					.flatMap(c -> c.toJava().stream())
					.collect(Collectors.toList());
		codes.addAll(subTypes);
		return codes;
	}

	private String methodForKey(MessageType key) {
		return "public " + key.name + " " + key.getNameWithLower() + "() { return new " + key.name + "(); }\n";
	}

	private String escape(String value) {
		return value.replace("\"", "\\\"");
	}

	private String getNameWithLower() {
		if(name.length()==0) return name.toLowerCase();
		return name.substring(0,1).toLowerCase() + name.substring(1);
	}

}