package br.com.caelum.vraptor.biscotti.compiler;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.biscotti.compiler.MessageType;
import br.com.caelum.vraptor.biscotti.compiler.TypeCollector;

public class TypeCollectorTest {

	private TypeCollector collector;

	@Before
	public void setup() {
		this.collector = new TypeCollector("");
	}

	@Test
	public void shouldAddToSimpleType() {
		collector.collect("welcome", "1");
		assertList(Arrays.asList("Messages"), collector.getTypeNames());
	}

	@Test
	public void shouldAddToSubType() {
		collector.collect("welcome.home", "1");
		assertList(asList("Messages", "Messages.Welcome"), collector.getTypeNames());
	}

	@Test
	public void shouldSupportDeeperNesting() {
		collector.collect("welcome.home.parents", "1");
		assertList(asList("Messages", "Messages.Welcome", "Messages.Welcome.Home"),
				collector.getTypeNames());
	}

	@Test
	public void shouldIgnoreDuplicatedTypes() {
		collector.collect("home", "1");
		collector.collect("office", "1");
		collector.collect("welcome.home", "1");
		collector.collect("welcome.office", "1");
		assertList(asList("Messages", "Messages.Welcome"), collector.getTypeNames());
	}

	private <T> void assertList(Collection<T> a, Collection<T> b) {
		assertTrue(a + " != " + b, a.containsAll(b));
		assertTrue(a + " != " + b, b.containsAll(a));
	}

	@Test
	public void shouldGenerateWellDefinedTypes() {
		collector.collect("home", "1");
		collector.collect("office", "2");
		collector.collect("welcome.home", "1");
		collector.collect("welcome.restaurant", "2");
		MessageType welcome = new MessageType("Welcome", "Messages.Welcome").add("home", "1").add(
				"restaurant","2");
		MessageType msgs = new MessageType("Messages", "Messages").add("home", "1")
				.add("office", "2").add(welcome, "welcome");
		assertEquals(msgs, collector.getRoot());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSupportTypeAndMessageWithTheSameName() {
		collector.collect("welcome", "1");
		collector.collect("welcome.home", "1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSupportTheSameValueTwice() {
		collector.collect("welcome.home", "1");
		collector.collect("welcome.home", "1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSupportDoubleDots() {
		collector.collect("welcome..home", "1");
	}

}
