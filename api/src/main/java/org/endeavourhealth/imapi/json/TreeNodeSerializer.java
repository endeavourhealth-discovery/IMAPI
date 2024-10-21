package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tree.TreeNode;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.io.IOException;
import java.util.List;

public class TreeNodeSerializer extends StdSerializer<TreeNode> {
  private String key;
  private String iri;
  private String label;
  private List<TreeNode> children;
  private String type;
  private TTValue data;

  public TreeNodeSerializer(Class<TreeNode> t, String key, String iri, String label, List<TreeNode> children, String type, TTValue data) {
    super(t);
    this.key = key;
    this.iri = iri;
    this.label = label;
    this.children = children;
    this.type = type;
    this.data = data;
  }

  public void serialize(TreeNode treeNode, JsonGenerator gen, SerializerProvider prov) throws IOException {
    // TODO
  }
}
