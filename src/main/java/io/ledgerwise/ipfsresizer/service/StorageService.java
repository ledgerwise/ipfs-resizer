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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Configuration
@Slf4j
public class StorageService {

   @Value(value = "${output-dir}")
   private String outputDir;

   public void saveContent(byte[] content, String cid) throws IOException {
      log.debug(cid);
      String contentPath = "%s%s".formatted(outputDir, cid);
      log.debug(contentPath);
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
      log.info("Saving resource %s of type %s".formatted(resource.getCid(), resource.getType()));
      switch (resource.getType()) {
         case IMAGE:
         case GIF:
         case VIDEO:
            log.debug("here");
            saveContent(resource.getContent(), resource.getCid());
            break;
         default:
            throw new NotSupportedResourceException("%s resource type not supported".formatted(resource.getType()),
                  null);
      }
   }
}
