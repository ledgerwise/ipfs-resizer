package io.ledgerwise.ipfsresizer.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.imageio.ImageIO;

import io.ledgerwise.ipfsresizer.exception.NotSupportedResourceException;
import io.ledgerwise.ipfsresizer.model.IPFSResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class StorageService {

   @Value(value = "${output-dir}")
   private String outputDir;

   public void saveContent(byte[] content, String cid, String suffix) throws IOException {
      String contentPath = "%s%s_%s".formatted(outputDir, cid, suffix);
      Path path = Paths.get(contentPath);
      Files.write(path, content);
   }

   public Optional<BufferedImage> getImage(String path) {
      try {
         BufferedImage image = ImageIO.read(new File(path));
         return Optional.of(image);
      } catch (IOException e) {
         return Optional.empty();
      }
   }

   public void saveResource(IPFSResource resource) throws IOException {
      switch (resource.getType()) {
         case IMAGE:
            saveContent(resource.getContent(), resource.getCid(), "png");
            break;
         case GIF:
            saveContent(resource.getContent(), resource.getCid(), "gif");
            break;
         case VIDEO:
            saveContent(resource.getContent(), resource.getCid(), "mp4");
            break;
         default:
            throw new NotSupportedResourceException("%s resource type not supported".formatted(resource.getType()),
                  null);
      }
   }
}
