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
      String imagePath = "%s_png".formatted(cid);
      String path = Paths.get(outputDir, imagePath).toString();
      File outputFile = new File(path);
      outputFile.getParentFile().mkdirs();
      ImageIO.write(image, "png", outputFile);
   }

   public Optional<BufferedImage> getImage(String path) {
      String imagePath = "%s_png".formatted(path);
      try {
         BufferedImage image = ImageIO.read(new File(imagePath));
         return Optional.of(image);
      } catch (IOException e) {
         return Optional.empty();
      }
   }
}
