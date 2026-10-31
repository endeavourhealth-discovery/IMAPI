package org.endeavourhealth.imapi.model.workflow

import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.workflow.entityApproval.ApprovalType

class EntityApproval(
  var entityIri: TTIriRef? = null,
  var approvalType: ApprovalType? = null
) : Task()
