package io.ledgerwise.ipfsresizer.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class ResizeService {
   @Autowired
   IPFSService ipfsService;
   @Autowired
   StorageService storageService;

   private byte[] imageToByteArray(BufferedImage image) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "png", baos);
      return baos.toByteArray();
   }

   BufferedImage resizeImage(BufferedImage originalImage, int maxSize) throws IOException {
      Integer currentWidth = originalImage.getWidth();
      Integer currentHeight = originalImage.getHeight();
      boolean isPortrait = currentHeight > currentWidth;

      Integer targetHeight = isPortrait ? currentHeight * maxSize / currentWidth : maxSize;
      Integer targetWidth = isPortrait ? maxSize : currentWidth * maxSize / currentHeight;

      BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
      Graphics2D graphics2D = resizedImage.createGraphics();
      graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
      graphics2D.dispose();
      return resizedImage;
   }

   public byte[] getResource(String cid, Integer size) throws IOException {
      String path = "%s_%s".formatted(cid, size);
      Optional<BufferedImage> existingImage = storageService.getImage(path);

      if (existingImage.isPresent())
         return imageToByteArray(existingImage.get());

      BufferedImage image = ipfsService.getImage(cid);
      BufferedImage scaledImage = resizeImage(image, size);

      storageService.saveImage(scaledImage, path);
      return imageToByteArray(scaledImage);
   }
}
