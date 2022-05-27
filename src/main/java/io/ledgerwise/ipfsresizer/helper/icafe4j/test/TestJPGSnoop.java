package io.ledgerwise.ipfsresizer.helper.icafe4j.test;

import java.io.FileInputStream;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.jpeg.JPGTweaker;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.meta.Metadata;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.meta.MetadataType;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.meta.xmp.XMP;

public class TestJPGSnoop extends TestBase {

	public static void main(String[] args) throws Exception {
		new TestJPGSnoop().test(args);
	}

	public void test(String... args) throws Exception {
		FileInputStream fin = new FileInputStream(args[0]);
		Metadata meta = JPGTweaker.readMetadata(fin).get(MetadataType.XMP);
		if (meta != null) {
			XMP.showXMP((XMP) meta);
		}
		fin.close();
	}
}
