package io.ledgerwise.ipfsresizer.helper.icafe4j.test;

public class InvokeMain {
	public static void main(String... args) {
		try {
			io.ledgerwise.ipfsresizer.helper.icafe4j.util.LangUtils.invokeMain(args);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
