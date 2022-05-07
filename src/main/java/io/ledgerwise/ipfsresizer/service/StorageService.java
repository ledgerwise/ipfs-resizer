package io.ledgerwise.ipfsresizer.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class StorageService {

   @Value(value = "${output-dir}")
   private String outputDir;

   public void saveImage(BufferedImage image, String cid) throws IOException {
      String path = Paths.get(outputDir, cid).toString();
      File outputFile = new File(path);
      outputFile.getParentFile().mkdirs();
      ImageIO.write(image, "png", outputFile);
   }

   public Optional<BufferedImage> getImage(String path) {
      try {
         BufferedImage image = ImageIO.read(new File(path));
         return Optional.of(image);
      } catch (IOException e) {
         return Optional.empty();
      }
   }
}
