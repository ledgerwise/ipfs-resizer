package io.ledgerwise.ipfsresizer.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.imageio.ImageIO;

import io.ledgerwise.ipfsresizer.helper.icafe4j.image.gif.GIFTweaker;
import io.ledgerwise.ipfsresizer.exception.ImageConversionException;
import io.ledgerwise.ipfsresizer.exception.NotSupportedResourceException;
import io.ledgerwise.ipfsresizer.helper.GifDecoder;
import io.ledgerwise.ipfsresizer.helper.GifDecoder.GifImage;
import io.ledgerwise.ipfsresizer.model.IPFSResource;
import io.ledgerwise.ipfsresizer.model.IPFSResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
@Slf4j
public class ResizeService {
   @Autowired
   IPFSService ipfsService;
   @Autowired
   StorageService storageService;
   @Value(value = "${output-dir}")
   private String outputDir;

   private byte[] imageToByteArray(BufferedImage image) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "png", baos);
      return baos.toByteArray();
   }

   private BufferedImage byteArrayToImage(byte[] imageData) {
      ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
      try {
         return ImageIO.read(bais);
      } catch (IOException e) {
         throw new ImageConversionException(e.toString(), null);
      }
   }

   byte[] resizeImage(byte[] imageBytes, int maxSize) throws IOException {
      BufferedImage originalImage = byteArrayToImage(imageBytes);
      Integer currentWidth = originalImage.getWidth();
      Integer currentHeight = originalImage.getHeight();
      boolean isLandscape = currentHeight < currentWidth;

      Integer targetHeight = isLandscape ? currentHeight * maxSize / currentWidth : maxSize;
      Integer targetWidth = isLandscape ? maxSize : currentWidth * maxSize / currentHeight;

      BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
      Graphics2D graphics2D = resizedImage.createGraphics();
      graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
      graphics2D.dispose();
      return imageToByteArray(resizedImage);
   }

   byte[] resizeGif(byte[] imageBytes, int maxSize) throws Exception {
      final GifImage gif = GifDecoder.read(imageBytes);
      final int frameCount = gif.getFrameCount();
      BufferedImage[] images = new BufferedImage[frameCount];
      int[] delays = new int[images.length];

      for (int i = 0; i < frameCount; i++) {
         images[i] = byteArrayToImage(resizeImage(imageToByteArray(gif.getFrame(i)), maxSize));
         delays[i] = gif.getDelay(i) * 10;
      }

      ByteArrayOutputStream output = new ByteArrayOutputStream();
      GIFTweaker.writeAnimatedGIF(images, delays, output);
      return output.toByteArray();
   }

   private Optional<byte[]> getCachedFile(String path) throws IOException {
      // Check if image cached
      File imageFile = new File(path);
      if (imageFile.exists()) {
         Path _path = Paths.get(path);
         return Optional.ofNullable(Files.readAllBytes(_path));
      }

      return Optional.ofNullable(null);
   }

   public IPFSResource getResource(String cid, Integer size) throws Exception {
      String path = "%s%s_%s".formatted(outputDir, cid, size);
      String imagePath = "%s_%s".formatted(path, "png");
      String gifPath = "%s_%s".formatted(path, "gif");
      String videoPath = "%s_%s".formatted(path, "mp4");
      String imageCid = "%s_%s_%s".formatted(cid, size, "png");
      String gifCid = "%s_%s_%s".formatted(cid, size, "gif");
      String videoCid = "%s_%s_%s".formatted(cid, size, "mp4");

      Optional<byte[]> imageContent = getCachedFile(imagePath);
      if (imageContent.isPresent()) {
         log.info("Returning cached file %s".formatted(imagePath));
         return IPFSResource.builder().cid(cid).content(imageContent.get()).type(IPFSResourceType.IMAGE).build();
      }

      Optional<byte[]> gifContent = getCachedFile(gifPath);
      if (gifContent.isPresent()) {
         log.info("Returning cached file %s".formatted(gifPath));
         return IPFSResource.builder().cid(cid).content(gifContent.get()).type(IPFSResourceType.GIF).build();
      }

      Optional<byte[]> videoContent = getCachedFile(videoPath);
      if (videoContent.isPresent()) {
         log.info("Returning cached file %s".formatted(videoPath));
         return IPFSResource.builder().cid(cid).content(videoContent.get()).type(IPFSResourceType.GIF).build();
      }

      IPFSResource resource = ipfsService.getResource(cid);
      switch (resource.getType()) {
         case IMAGE:
            resource.setContent(resizeImage(resource.getContent(), size));
            resource.setCid(imageCid);
            break;
         case GIF:
            resource.setContent(resizeGif(resource.getContent(), size));
            resource.setCid(gifCid);
            break;
         default:
            throw new NotSupportedResourceException("%s resource type not supported".formatted(resource.getType()),
                  null);
      }
      System.out.println(resource.getCid());
      storageService.saveResource(resource);
      return resource;
   }

}
