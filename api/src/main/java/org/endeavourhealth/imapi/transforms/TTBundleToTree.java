package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tree.TreeNode;
import org.endeavourhealth.imapi.model.tree.TreeNodeData;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.List;
import java.util.Map;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTBundleToTree {
  public List<TreeNode> buildDetails(TTBundle ttBundle, TTArray types) {
    TreeNode root = new TreeNode();
    buildTreeDataRecursively(root, ttBundle.getEntity(), ttBundle.getPredicates(), types);
    return root.getChildren();
  }

  private void buildTreeDataRecursively(TreeNode treeNode, Object entity, Map<String, String> predicates, TTArray types) {
    if (entity instanceof TTEntity ttEntity) {
      if (!ttEntity.getPredicateMap().isEmpty()) {
        for (TTIriRef key : ttEntity.getPredicateMap().keySet()) {
          processEntityKey(key.getIri(), treeNode, ttEntity, predicates, types);
        }
      }
    } else if (entity instanceof TTArray ttArray) {
      for (TTValue item : ttArray.getElements()) {
        addIriLink(treeNode, item,predicates);
      }
    } else if (entity instanceof String entityString) {
      addValueToLabel(treeNode, ": ", entityString);
    }
  }

  private void processEntityKey(String key, TreeNode treeNode, TTEntity entity, Map<String, String> predicates, TTArray types) {
    switch (key) {
      case IM.ROLE_GROUP -> addRoleGroup(treeNode, entity, predicates, key);
      case IM.HAS_TERM_CODE -> addTermCodes(treeNode, entity, predicates, key);
      case SHACL.PROPERTY -> addProperty(treeNode, entity, predicates, key);
      case SHACL.PARAMETER -> addParameter(treeNode, entity, predicates, key,types);
      case IM.BINDING -> addBinding(treeNode, entity, predicates, key);
      case IM.DEFINITION -> addDefinition(treeNode, entity, predicates, key,types);
      case IM.HAS_MAP -> addHasMapNode(treeNode, entity, predicates, key);
      default -> {
        TreeNode subTreeNode = new TreeNode().setKey(key).setLabel(predicates.get(key));
        treeNode.addChild(subTreeNode);
        buildTreeDataRecursively(subTreeNode, entity.get(iri(key)), predicates, types);
      }
    }
  }

  private void addValueToLabel(TreeNode treeNode, String divider, String value) {
    treeNode.setLabel(treeNode.getLabel() + divider + value);
  }

  private void addIriLink(TreeNode treeNode, TTValue value,Map<String, String> predicates) {
    if (value.isNode() && value.asNode().getIri().equals(IM.LOAD_MORE)) {
      String name = value.asNode().get(iri(RDFS.LABEL)).asLiteral().getValue();
      int totalCount = value.asNode().get(iri(IM.NAMESPACE + "totalCount")).asLiteral().intValue();
      treeNode.addChild(new TreeNode()
        .setKey(value.asNode().getIri())
        .setLabel(name).setType("loadMore")
        .setData(new TTNode()
          .set(iri(IM.NAMESPACE + "totalCount"), totalCount)
          .set(iri(IM.NAMESPACE + "predicate"), treeNode.getKey())
        ));
    } else if (value.isIriRef()) {
      treeNode.addChild(new TreeNode().setKey(value.asIriRef().getIri()).setLabel(value.asIriRef().getName()).setType("link"));
    } else if (value.isNode()) {
      addNode(treeNode, value.asNode(),predicates);
    }
  }

  private void addDefinition(TreeNode treeNode, TTEntity entity, Map<String, String> predicates, String key, TTArray types) {
    String label = predicates.get(key) != null ? predicates.get(key) : key;
    TreeNode definitionNode = new TreeNode().setKey(key).setLabel(label);
    treeNode.addChild(definitionNode);
  }

  private void addParameter(TreeNode treeNode, TTEntity entity, Map<String, String> predicates, String key, TTArray types) {
    String label = predicates.get(key) != null ? predicates.get(key) : key;
    TreeNode newTreeNode = new TreeNode().setKey(key).setLabel(label);
    treeNode.addChild(newTreeNode);
    if (!entity.get(iri(key)).isEmpty()) {
      for (TTValue value : entity.get(iri(key)).getElements()) {
        TreeNode parameterNode = new TreeNode()
          .setKey(key)
          .setLabel(value.asNode().get(iri(RDFS.LABEL)).asLiteral().getValue());
        newTreeNode.addChild(parameterNode);
        buildTreeDataRecursively(parameterNode,value,predicates,types);
      }
    }
  }

  private void addDefault(TreeNode treeNode, TTNode entity, Map<String, String> predicates) {
    for (TTIriRef key : entity.getPredicateMap().keySet()) {
      for (TTValue value : entity.get(key).getElements()) {
        if (value instanceof TTNode || value instanceof TTIriRef) addIriLink(treeNode, value,predicates);
        else addDefault(treeNode,value.asNode(),predicates);
      }
    }
  }

  private void addNode(TreeNode treeNode, TTNode entity,Map<String,String> predicates) {
    for (TTIriRef key : entity.getPredicateMap().keySet()) {
      for (TTValue value : entity.get(key).getElements()) {
        TreeNode newTreeNode = new TreeNode()
          .setKey(key.getIri())
          .setLabel(value.asNode().get(iri(RDFS.LABEL)).asLiteral().getValue());
        treeNode.addChild(newTreeNode);
        addDefault(newTreeNode,value.asNode(),predicates);
      }
    }
  }

  private void addTermCodes(TreeNode treeNode, TTNode entity, Map<String, String> predicates, String key) {
    TreeNode newTreeNode = new TreeNode().setKey(key).setLabel(predicates.get(key));
    treeNode.addChild(newTreeNode);
    if (!entity.get(iri(key)).isEmpty()) {
      for (TTValue termCode : entity.get(iri(key)).getElements()) {
        TreeNode termCodeNode = new TreeNode()
          .setKey(termCode.asNode().get(iri(IM.CODE)).asLiteral().getValue())
          .setLabel(termCode.asNode().get(iri(RDFS.LABEL)).asLiteral().getValue());
        newTreeNode.addChild(termCodeNode);
      }
    }
  }

  private void addRoleGroup(TreeNode treeNode, TTEntity entity, Map<String, String> predicates, String key) {
    TreeNode newTreeNode = new TreeNode().setKey(key).setLabel(predicates.get(key));
    treeNode.addChild(newTreeNode);
    if (!entity.get(iri(key)).isEmpty()) {
      for (TTValue roleGroup : entity.get(iri(key)).getElements()) {
        TreeNode roleGroupNode = new TreeNode()
          .setKey(IM.GROUP_NUMBER + roleGroup.asNode().get(iri(IM.GROUP_NUMBER)).asLiteral().getValue())
          .setLabel("role group" + roleGroup.asNode().get(iri(IM.GROUP_NUMBER)).asLiteral().getValue());
        newTreeNode.addChild(roleGroupNode);
        for (TTIriRef roleKey : roleGroup.asNode().getPredicateMap().keySet()) {
          if (!roleKey.getIri().equals(IM.GROUP_NUMBER)) {
            roleGroupNode.addChild(getRoleValue(predicates,roleGroup,roleKey,key));
          }
        }
      }
    }
  }

  private TreeNode getRoleValue(Map<String,String> predicates, TTValue roleGroup, TTIriRef roleKey, String key) {
    TreeNode roleValueNode = new TreeNode()
      .setKey(key + "." + roleKey.getIri())
      .setIri(roleKey.getIri())
      .setLabel(predicates.get(roleKey.getIri()))
      .setType("property")
      .setData(roleGroup.asNode().get(roleKey).asNode());
    if (roleGroup.asNode().get(roleKey.asIriRef()).size() == 1) {
      roleValueNode.setData(roleGroup.asNode().get(roleKey.asIriRef()).get(0));
    } else {
      for (TTValue valueChild : roleGroup.asNode().get(roleKey.asIriRef()).getElements()) {
        addIriLink(roleValueNode, valueChild, predicates);
      }
    }
    return roleValueNode;
  }

  private void addBinding(TreeNode treeNode, TTNode entity, Map<String, String> predicates, String key) {
    TreeNode newTreeNode = new TreeNode().setKey(key).setLabel(predicates.get(key));
    treeNode.addChild(newTreeNode);
    if (!entity.get(iri(key)).isEmpty()) {
      for (TTValue value : entity.get(iri(key)).getElements()) {
        TreeNode bindingNode = new TreeNode()
          .setKey(value.asNode().get(iri(SHACL.NODE)).get(0).asIriRef().getIri())
          .setLabel(value.asNode().get(iri(SHACL.NODE)).get(0).asIriRef().getName())
          .setType("link");
        newTreeNode.addChild(bindingNode);
      }
    }
  }

  private void addProperty(TreeNode treeNode, TTNode entity, Map<String, String> predicates, String key) {
    TreeNode newTreeNode = new TreeNode()
      .setKey(key)
      .setLabel(predicates.get(key));
    treeNode.addChild(newTreeNode);
  }

  private void addHasMapNode(TreeNode treeNode, TTNode entity, Map<String, String> predicates, String key) {
    TreeNode newTreeNode = new TreeNode().setKey(key).setLabel(predicates.get(key));
    treeNode.addChild(newTreeNode);
    if (!entity.get(iri(key)).isEmpty()) {
      for (TTValue child : entity.get(iri(key)).getElements()) {
        if (child.isNode()) {
          for (TTIriRef childKey : child.asNode().getPredicateMap().keySet()) {
            TreeNode childNode = new TreeNode()
              .setKey(childKey.getIri())
              .setLabel(predicates.get(childKey.getIri()));
            newTreeNode.addChild(childNode);
            for (TTValue grandChild : child.asNode().get(childKey.asIriRef()).getElements()) {
              TreeNode grandChildNode = new TreeNode()
                .setKey(grandChild.asNode().get(iri(IM.MAP_ADVICE)).asLiteral().getValue())
                .setLabel(grandChild.asNode().get(iri(IM.MAP_ADVICE)).asLiteral().getValue());
              childNode.addChild(grandChildNode);
              for (TTIriRef grandChildKey : grandChild.asNode().getPredicateMap().keySet()) {
                setChildNodeArrayCheck(grandChild, grandChildKey.getIri(),predicates,grandChildNode);
              }
            }
          }
        }
      }
    }
  }

  private void setChildNodeArrayCheck(TTValue children, String key, Map<String, String> predicates, TreeNode grandChildNode) {
    if (children.asNode().getPredicateMap().size() > 1) {
      TreeNode arrayNode = new TreeNode()
        .setKey(children.asNode().get(iri(IM.MAP_ADVICE)).asLiteral().getValue() + key)
        .setLabel(predicates.get(key));
      grandChildNode.addChild(arrayNode);
      for (TTValue child : children.asNode().get(iri(key)).getElements()) {
        addIriLink(arrayNode, child, predicates);
      }
    } else {
      TreeNode nonArrayNode = new TreeNode()
        .setKey(children.asNode().get(iri(IM.MAP_ADVICE)).asLiteral().getValue() + key)
        .setLabel(predicates.get(key) + " - " + children.asNode().get(iri(key)).asLiteral().getValue());
      grandChildNode.addChild(nonArrayNode);
    }
  }

}
