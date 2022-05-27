package io.ledgerwise.ipfsresizer.helper.icafe4j.test;

import java.io.IOException;

import io.ledgerwise.ipfsresizer.helper.icafe4j.image.png.IDATBuilder;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.png.IDATReader;
import io.ledgerwise.ipfsresizer.helper.icafe4j.string.StringUtils;

public class TestIDATReader extends TestBase {

	public TestIDATReader() {
	}

	public static void main(String[] args) throws IOException {
		new TestIDATReader().test();
	}

	public void test(String... args) throws IOException {
		IDATReader reader = new IDATReader();
		IDATBuilder builder = new IDATBuilder().data(new byte[] { 1, 2, 3 }).data(new byte[] { 4, 5, 6 });
		builder.setFinish(true);
		reader.addChunk(builder.build());
		logger.info(StringUtils.byteArrayToHexString(reader.getData()));
	}
}
