package br.com.caelum.vraptor.biscotti.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.vraptor.biscotti.compiler.MessageType;

public class MessageTypeTest {
	
	@Test
	public void shouldSerializeOne() {
		MessageType msgs = createMsgs();
		String expected = expectedMessages();
		assertEquals(expected, msgs.toJava().get(0).getJava(""));
		assertEquals(1, msgs.toJava().size());
	}

	private String expectedMessages() {
		return header()
				+ "public class Messages {\n"
				+ "public String office() { return \"2\"; }\n"
				+ "public String home() { return \"1\"; }\n"
				+ "}";
	}

	private String header() {
		return "package i18n;\n@javax.enterprise.inject.Vetoed\n";
	}

	private MessageType createMsgs() {
		return new MessageType("Messages", "Messages")
				.add("home", "1")
				.add("office", "2");
	}

	@Test
	public void shouldSerializeChild() {
		MessageType welcome = createWelcome();
		String expected = expectWelcome();
		assertEquals(expected, welcome.toJava().get(0).getJava(""));
		assertEquals(1, welcome.toJava().size());
	}

	private String expectWelcome() {
		return header()
				+ "public class Welcome {\n"
				+ "public String restaurant() { return \"2\"; }\n"
				+ "public String home() { return \"1\"; }\n"
				+ "}";
	}

	private MessageType createWelcome() {
		return new MessageType("Welcome", "Messages.Welcome").add("home", "1").add(
				"restaurant","2");
	}

	@Test
	public void shouldSerializeBeautifully() {
		MessageType welcome = createWelcome();
		MessageType msgs = createMsgs().add(welcome, "welcome");

		String messagesWithChild = header() + 
				"public class Messages {\n"
				+ "public String office() { return \"2\"; }\n"
				+ "public String home() { return \"1\"; }\n"
				+ "public Welcome welcome() { return new Welcome(); }\n"
				+ "}";

		assertEquals(2, msgs.toJava().size());
		assertEquals(expectWelcome(), msgs.toJava().get(1).getJava(""));
		assertEquals(messagesWithChild, msgs.toJava().get(0).getJava(""));
	}
	
	@Test
	public void shouldEscape() {
		String expected = header()
				+ "public class Messages {\n"
				+ "public String home() { return \"\\\"\"; }\n"
				+ "}";
		MessageType msgs = new MessageType("Messages", "Messages").add("home", "\"");
		assertEquals(expected, msgs.toJava().get(0).getJava(""));
	}

}
