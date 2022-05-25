package io.ledgerwise.ipfsresizer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IPFSResource {
   private String cid;
   private byte[] content;
   private IPFSResourceType type;
}
