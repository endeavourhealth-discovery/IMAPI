package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tree.TreeNode;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.transforms.testData.TTBundleToTreeTestData;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TTBundleToTreeTest {
  TTBundleToTree ttBundleToTree = new TTBundleToTree();
  ObjectMapper mapper = new ObjectMapper();

  @Test
  public void buildsDetailsForEntityWithDefinition() throws JsonProcessingException {
    TTBundle bundle = mapper.readValue(TTBundleToTreeTestData.definitionEntityJson,TTBundle.class);
    List<TreeNode> expected = mapper.readValue(TTBundleToTreeTestData.definitionTreeJson,new TypeReference<List<TreeNode>>(){});
    assertThat(ttBundleToTree.buildDetails(bundle,null)).isEqualTo(expected);
  }

  @Test
  public void buildsDetailsForEntityWithMUltiplePredicates() throws JsonProcessingException {
    TTBundle bundle = mapper.readValue(TTBundleToTreeTestData.multiplePredicatesEntityJson, TTBundle.class);
    List<TreeNode> treeNode = mapper.readValue(TTBundleToTreeTestData.multiplePredicatesEntityJson, new TypeReference<List<TreeNode>>(){});
    assertThat(ttBundleToTree.buildDetails(bundle, null)).isEqualTo(treeNode);
  }

  @Test
  public void buildDetailsForEntityWithHasMaps() throws JsonProcessingException {
    TTBundle bundle = mapper.readValue(TTBundleToTreeTestData.hasMapsEntityJson, TTBundle.class);
    List<TreeNode> treeNode = mapper.readValue(TTBundleToTreeTestData.hasMapsTreeJson, new TypeReference<List<TreeNode>>(){});
    assertThat(ttBundleToTree.buildDetails(bundle, null)).isEqualTo(treeNode);
  }

  @Test
  public void buildDetailsForEntityWithProperties() throws JsonProcessingException {
    TTBundle bundle = mapper.readValue(TTBundleToTreeTestData.propertiesEntityJson, TTBundle.class);
    List<TreeNode> treeNode = mapper.readValue(TTBundleToTreeTestData.propertiesTreeJson, new TypeReference<List<TreeNode>>(){});
    assertThat(ttBundleToTree.buildDetails(bundle, null)).isEqualTo(treeNode);
  }

  @Test
  public void buildDetailsForEntityWithParameters() throws JsonProcessingException {
    TTBundle bundle = mapper.readValue(TTBundleToTreeTestData.parameterEntityJson, TTBundle.class);
    List<TreeNode> treeNode = mapper.readValue(TTBundleToTreeTestData.parameterTreeJson, new TypeReference<List<TreeNode>>(){});
    assertThat(ttBundleToTree.buildDetails(bundle, null)).isEqualTo(treeNode);
  }
}
