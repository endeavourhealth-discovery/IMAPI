package org.endeavourhealth.imapi.model.github

import com.fasterxml.jackson.annotation.JsonIgnore

class GithubDTO {
  @JsonIgnore
  val url: String? = null
  val html_url: String? = null

  @JsonIgnore
  val assets_url: String? = null

  @JsonIgnore
  val upload_url: String? = null

  @JsonIgnore
  val tarball_url: String? = null

  @JsonIgnore
  val zipball_url: String? = null

  @JsonIgnore
  val id: String? = null

  @JsonIgnore
  val node_id: String? = null
  val tag_name: String? = null

  @JsonIgnore
  val target_commitish: String? = null
  val name: String? = null
  val body: String? = null

  @JsonIgnore
  val draft: String? = null

  @JsonIgnore
  val prerelease: String? = null

  @JsonIgnore
  val immutable: String? = null
  val created_at: String? = null
  val published_at: String? = null
  val author: GithubAuthorDTO? = null

  @JsonIgnore
  val assets: Any? = null
}
