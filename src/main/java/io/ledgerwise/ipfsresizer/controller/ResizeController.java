package io.ledgerwise.ipfsresizer.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import io.ledgerwise.ipfsresizer.service.ResizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@RequestMapping("/api/v1")
public class ResizeController {

   @Autowired
   private ResizeService resizeService;
   @Value(value = "${allowed-sizes:#{null}}")
   private List<Integer> allowedSizes;

   @Bean
   public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
      return new BufferedImageHttpMessageConverter();
   }

   @GetMapping(path = "/resized", produces = "image/png")
   @ResponseBody
   public ResponseEntity<?> resized(@RequestParam String cid, @RequestParam Integer size)
         throws IOException {
      final HttpHeaders jsonHttpHeaders = new HttpHeaders();
      jsonHttpHeaders.setContentType(MediaType.APPLICATION_JSON);
      final HttpHeaders pngHttpHeaders = new HttpHeaders();
      pngHttpHeaders.setContentType(MediaType.IMAGE_PNG);

      if (allowedSizes != null && !allowedSizes.contains(size)) {
         return new ResponseEntity<>("{\"error\": \"Size not allowed\"}", jsonHttpHeaders, HttpStatus.BAD_REQUEST);
      }

      try {
         return new ResponseEntity<>(resizeService.getResource(cid, size), pngHttpHeaders, HttpStatus.OK);
      } catch (Exception e) {
         return new ResponseEntity<>("{\"error\": \"%s\"}".formatted(e),
               jsonHttpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
      }

   }

   @GetMapping(path = "/allowed_sizes")
   @ResponseBody
   public List<Integer> getAllowedSizes() {
      return allowedSizes;
   }
}
