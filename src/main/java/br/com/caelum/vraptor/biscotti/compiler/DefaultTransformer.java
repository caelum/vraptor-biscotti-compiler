package br.com.caelum.vraptor.biscotti.compiler;

public class DefaultTransformer implements Transformer {

	@Override
	public String get(String key) {
		return key.replace("-", "_");
	}

}
