package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tree.TreeNode;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.io.IOException;
import java.util.Iterator;

public class TreeNodeDeserializer extends StdDeserializer<TreeNode> {

  protected TreeNodeDeserializer(Class<TreeNode> vc) {
    super(vc);
  }

  @Override
  public TreeNode deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    return getNodeAsTreeNode(node);
  }

  private TreeNode getNodeAsTreeNode(JsonNode node) {
    TreeNode treeNode = new TreeNode();
    treeNode.setKey(node.get("key").asText());
    treeNode.setIri(node.get("iri").asText());
    treeNode.setType(node.get("type").asText());
    treeNode.setLabel(node.get("label").asText());
    processChildren(node.get("children"),treeNode);
    processData(node.get("data"),treeNode);
    return treeNode;
  }

  private void processChildren(JsonNode node, TreeNode treeNode) {
    ArrayNode arrayNode = (ArrayNode) node;
    Iterator<JsonNode> items = arrayNode.elements();
    while (items.hasNext()) {
      JsonNode item = items.next();
      treeNode.addChild(getNodeAsTreeNode(item));
    }
  }

  private void processData(JsonNode node, TreeNode treeNode) {
    ObjectMapper om = new ObjectMapper();
    if (node.isObject()) {
      TTValue ttValue = om.convertValue(node, TTValue.class);
      if (ttValue.isNode()) {
        treeNode.setData(ttValue.asNode());
      } else if (ttValue.isIriRef()) {
        treeNode.setData(ttValue.asIriRef());
      } else if (ttValue.isLiteral()) {
        treeNode.setData(ttValue.asLiteral());
      }
    }
  }
}
