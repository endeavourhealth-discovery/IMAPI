package org.endeavourhealth.imapi.model

import org.endeavourhealth.imapi.model.tripletree.TTArray
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import java.io.Serializable
import java.util.*

class EntityReferenceNode : TTIriRef, Serializable {
  var parents: MutableList<EntityReferenceNode> = mutableListOf()
    private set
  var children: MutableList<EntityReferenceNode>? = null
    private set
  var moduleId: String? = null
    private set
  var hasChildren: Boolean? = null
    private set
  var hasGrandChildren: Boolean? = null
    private set
  var type: TTArray? = null
    private set
  var orderNumber: Int? = null
    private set
  var status: TTIriRef? = null
    private set
  var scheme: TTIriRef? = null
    private set

  constructor()

  constructor(iri: String?) : super(iri)

  constructor(iri: String?, name: String?, types: TTArray?) : super(iri, name) {
    setType(types)
  }

  constructor(iri: String?, name: String?) : super(iri, name)

  fun addType(type: TTIriRef): EntityReferenceNode {
    if (this.type == null) this.type = TTArray()
    this.type!!.add(type)
    return this
  }

  fun setStatus(status: TTIriRef?): EntityReferenceNode {
    this.status = status
    return this
  }

  fun setScheme(scheme: TTIriRef?): EntityReferenceNode {
    this.scheme = scheme
    return this
  }

  fun setParents(parents: MutableList<EntityReferenceNode>): EntityReferenceNode {
    this.parents = parents
    return this
  }

  fun addParent(parent: EntityReferenceNode): EntityReferenceNode {
    this.parents.add(parent)
    return this
  }

  fun setOrderNumber(order: Int?): EntityReferenceNode {
    this.orderNumber = order
    return this
  }

  fun setChildren(children: MutableList<EntityReferenceNode>?): EntityReferenceNode {
    this.children = children
    return this
  }

  fun addChild(parent: EntityReferenceNode): EntityReferenceNode {
    if (this.children == null) this.children = mutableListOf()
    this.children!!.add(parent)
    return this
  }

  fun setModuleId(moduleId: String?): EntityReferenceNode {
    this.moduleId = moduleId
    return this
  }

  fun setHasGrandChildren(hasGrandChildren: Boolean?): EntityReferenceNode {
    this.hasGrandChildren = hasGrandChildren
    return this
  }

  fun setHasChildren(hasChildren: Boolean?): EntityReferenceNode {
    this.hasChildren = hasChildren
    return this
  }

  fun setType(type: TTArray?): EntityReferenceNode {
    this.type = type
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    if (!super.equals(other)) return false
    val that = other as EntityReferenceNode
    return (
      hasChildren == that.hasChildren &&
        parents == that.parents &&
        children == that.children &&
        moduleId == that.moduleId &&
        type == that.type
      )
  }

  override fun hashCode(): Int {
    return Objects.hash(super.hashCode(), parents, children, moduleId, hasChildren, type)
  }
}
