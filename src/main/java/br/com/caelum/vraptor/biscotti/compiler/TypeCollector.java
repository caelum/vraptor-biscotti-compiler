package br.com.caelum.vraptor.biscotti.compiler;

import static java.lang.Character.toUpperCase;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TypeCollector {

	private final Map<String, MessageType> types = new HashMap<>();
	private final Transformer transformer = new DefaultTransformer();
	private final String name;

	public TypeCollector(String name) {
		this.name = name;
		types.put("Messages", new MessageType("Messages", "Messages"));
	}
	
	public void collect(Map<String, String> map) {
		map.forEach(this::collect);
	}
	
	public void collect(Reader reader) throws IOException {
		Properties p = new Properties();
		p.load(reader);
		p.forEach((k, v) -> collect((String) k, (String) v));
	}
	
	public void collect(String key, String value) {
		MessageType current = getRoot();
		String[] parts = key.split("\\.");
		String path = "Messages.";
		for (int i = 0; i < parts.length - 1; i++) {
			if (parts[i].isEmpty()) {
				throw new IllegalArgumentException(key + " is invalid");
			}
			String baseName = transformer.get(parts[i]);
			String typeName = upper(baseName);
			path += typeName + ".";
			current = typeForExtra(typeName, path, current, baseName);
		}
		String name = parts[parts.length - 1];
		String transformed = transformer.get(name);
		current.add(transformed, value);
	}

	public MessageType getRoot() {
		return types.get("Messages");
	}

	private MessageType typeForExtra(String name, String path, MessageType last, String baseName) {
		path = path.substring(0, path.length() - 1);
		if (types.containsKey(path)) {
			return types.get(path);
		}
		MessageType current = new MessageType(name, path);
		last.add(current, baseName);
		types.put(path, current);
		return current;
	}

	private String upper(String key) {
		if (key.length() == 1)
			return key.toUpperCase();
		return toUpperCase(key.charAt(0)) + key.substring(1);
	}

	Set<String> getTypeNames() {
		return types.keySet();
	}

	public List<MessageType> getTypes() {
		return new ArrayList<>(types.values());
	}

}
