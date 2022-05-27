package io.ledgerwise.ipfsresizer.helper.icafe4j.test;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import io.ledgerwise.ipfsresizer.helper.icafe4j.ImageIO;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.ImageColorType;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.ImageParam;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.ImageType;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.options.JPGOptions;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.options.PNGOptions;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.options.TIFFOptions;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.png.Filter;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.quant.DitherMethod;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.quant.QuantMethod;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.reader.ImageReader;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.tiff.TiffFieldEnum.Compression;
import io.ledgerwise.ipfsresizer.helper.icafe4j.image.tiff.TiffFieldEnum.PhotoMetric;
import io.ledgerwise.ipfsresizer.helper.icafe4j.io.ByteOrder;
import io.ledgerwise.ipfsresizer.helper.icafe4j.io.PeekHeadInputStream;

/**
 * Temporary class for testing image readers
 */
public class TestImageReader extends TestBase {

	public static void main(String args[]) throws Exception {
		new TestImageReader().test(args);
	}

	public void test(String... args) throws Exception {
		long t1 = System.currentTimeMillis();

		FileInputStream fin = new FileInputStream(new File(args[0]));
		PeekHeadInputStream peekHeadInputStream = new PeekHeadInputStream(fin, ImageIO.IMAGE_MAGIC_NUMBER_LEN);
		ImageReader reader = ImageIO.getReader(peekHeadInputStream);
		BufferedImage img = reader.read(peekHeadInputStream);
		peekHeadInputStream.close();

		if (img == null) {
			logger.error("Failed reading image!");
			return;
		}

		logger.info("Total frames read: {}", reader.getFrameCount());

		logger.info("Color model: {}", img.getColorModel());
		logger.info("Raster: {}", img.getRaster());
		logger.info("Sample model: {}", img.getSampleModel());

		long t2 = System.currentTimeMillis();

		logger.info("decoding time {}ms", (t2 - t1));

		final JFrame jframe = new JFrame("Image Reader");

		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				jframe.dispose();
				System.exit(0);
			}
		});

		ImageType imageType = ImageType.TIFF;

		FileOutputStream fo = new FileOutputStream("NEW." + imageType.getExtension());

		ImageParam.ImageParamBuilder builder = ImageParam.getBuilder();

		switch (imageType) {
			case TIFF:// Set TIFF-specific options
				TIFFOptions tiffOptions = new TIFFOptions();
				tiffOptions.setByteOrder(ByteOrder.LITTLE_ENDIAN);
				tiffOptions.setApplyPredictor(true);
				tiffOptions.setTiffCompression(Compression.LZW);
				tiffOptions.setJPEGQuality(60);
				tiffOptions.setPhotoMetric(PhotoMetric.SEPARATED);
				tiffOptions.setWriteICCProfile(true);
				tiffOptions.setDeflateCompressionLevel(6);
				tiffOptions.setXResolution(96);
				tiffOptions.setYResolution(96);
				builder.imageOptions(tiffOptions);
				break;
			case PNG:
				PNGOptions pngOptions = new PNGOptions();
				pngOptions.setApplyAdaptiveFilter(false);
				pngOptions.setCompressionLevel(6);
				pngOptions.setFilterType(Filter.NONE);
				builder.imageOptions(pngOptions);
				break;
			case JPG:
				JPGOptions jpegOptions = new JPGOptions();
				jpegOptions.setQuality(90);
				jpegOptions.setColorSpace(JPGOptions.COLOR_SPACE_YCbCr);
				jpegOptions.setWriteICCProfile(true);
				builder.imageOptions(jpegOptions);
				break;
			default:
		}

		t1 = System.currentTimeMillis();
		ImageIO.write(img, fo, imageType, builder.quantMethod(QuantMethod.WU_QUANT).colorType(ImageColorType.INDEXED)
				.applyDither(true).ditherMethod(DitherMethod.FLOYD_STEINBERG).hasAlpha(true).build());
		t2 = System.currentTimeMillis();

		fo.close();

		logger.info("{} writer (encoding time {}ms)", imageType, (t2 - t1));

		JLabel theLabel = new JLabel(new ImageIcon(img));
		jframe.getContentPane().add(new JScrollPane(theLabel));
		jframe.setSize(400, 400);
		jframe.setVisible(true);
	}
}
