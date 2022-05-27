package io.ledgerwise.ipfsresizer.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import io.ledgerwise.ipfsresizer.exception.NotFoundException;
import io.ledgerwise.ipfsresizer.exception.NotSupportedResourceException;
import io.ledgerwise.ipfsresizer.model.IPFSResource;
import io.ledgerwise.ipfsresizer.model.IPFSResourceType;
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

   private IPFSResourceType getResourceType(String url) throws IOException {
      HttpClient client = HttpClientBuilder.create().build();
      HttpHead request = new HttpHead(URLDecoder.decode(url, StandardCharsets.UTF_8).replaceAll(" ", "%20"));
      HttpResponse response = client.execute(request);

      if (response.getStatusLine().getStatusCode() != 200)
         throw new NotFoundException("Image not found", null);

      String contentType = response.getHeaders("Content-Type")[0].getValue();

      if (contentType.toLowerCase().startsWith("image")) {
         if (contentType.toLowerCase().startsWith("image/gif"))
            return IPFSResourceType.GIF;
         return IPFSResourceType.IMAGE;
      }

      if (contentType.toLowerCase().startsWith("video"))
         return IPFSResourceType.VIDEO;
      throw new NotSupportedResourceException("%s resource not supported".formatted(contentType), null);
   }

   public IPFSResource getResource(String cid) throws IOException {
      String url = URLDecoder.decode(ipfsEndpoint + cid, StandardCharsets.UTF_8).replaceAll(" ", "%20");

      HttpClient client = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(url);
      HttpResponse response = client.execute(request);
      HttpEntity entity = response.getEntity();
      return IPFSResource.builder().cid(cid).type(getResourceType(url))
            .content(entity.getContent().readAllBytes()).build();
   }
}
