package br.com.caelum.vraptor.biscotti.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.vraptor.biscotti.compiler.DefaultTransformer;
import br.com.caelum.vraptor.biscotti.compiler.Transformer;

public class TransformerTest {
	
	private Transformer transformer = new DefaultTransformer();
	
	@Test
	public void shouldTransform() {
		assertEquals("welcome", transformer.get("welcome"));
	}

	@Test
	public void shouldSkipByTransformingHifen() {
		assertEquals("welcome_home", transformer.get("welcome-home"));
		assertEquals("welcome_home", transformer.get("welcome_home"));
	}

}
