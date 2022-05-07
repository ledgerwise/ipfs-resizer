package io.ledgerwise.ipfsresizer.service;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.ledgerwise.ipfsresizer.exception.NotAndImageException;
import io.ledgerwise.ipfsresizer.exception.NotFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class IPFSService {

   @Value(value = "${ipfs-endpoint}")
   private String ipfsEndpoint;

   private boolean isImage(String url) throws IOException {
      System.out.println(url);
      HttpClient client = HttpClientBuilder.create().build();
      HttpHead request = new HttpHead(url);
      HttpResponse response = client.execute(request);

      if (response.getStatusLine().getStatusCode() != 200)
         throw new NotFoundException("Image not found", null);

      String contentType = response.getHeaders("Content-Type")[0].getValue();
      if (contentType.toLowerCase().startsWith("image"))
         return true;

      throw new NotAndImageException("Not an image", null);
   }

   public BufferedImage getImage(String cid) throws IOException {
      String url = ipfsEndpoint + cid;
      isImage(url);

      HttpClient client = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(url);
      HttpResponse response = client.execute(request);
      HttpEntity entity = response.getEntity();
      return ImageIO.read(entity.getContent());
   }
}
