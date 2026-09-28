package org.endeavourhealth.imapi.model

import org.endeavourhealth.imapi.model.iml.Concept

class SetDiffObject {
  var membersA: MutableList<Concept> = mutableListOf()
    private set
  var membersB: MutableList<Concept> = mutableListOf()
    private set
  var sharedMembers: MutableList<Concept> = mutableListOf()
    private set

  fun setMembersA(membersA: MutableList<Concept>): SetDiffObject {
    this.membersA = membersA
    return this
  }

  fun addMemberA(memberA: Concept): SetDiffObject {
    this.membersA.add(memberA)
    return this
  }

  fun setMembersB(membersB: MutableList<Concept>): SetDiffObject {
    this.membersB = membersB
    return this
  }

  fun addMemberB(memberB: Concept): SetDiffObject {
    this.membersB.add(memberB)
    return this
  }

  fun setSharedMembers(sharedMembers: MutableList<Concept>): SetDiffObject {
    this.sharedMembers = sharedMembers
    return this
  }

  fun addSharedMember(sharedMember: Concept): SetDiffObject {
    this.sharedMembers.add(sharedMember)
    return this
  }
}
