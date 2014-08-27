package br.com.caelum.vraptor.biscotti.compiler;

import br.com.caelum.vraptor.biscotti.compiler.Transformer;

public class NullTransformer implements Transformer {

	@Override
	public String get(String key) {
		return key;
	}

}
