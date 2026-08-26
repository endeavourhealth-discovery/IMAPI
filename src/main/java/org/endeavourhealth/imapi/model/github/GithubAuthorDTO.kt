package org.endeavourhealth.imapi.model.github

import com.fasterxml.jackson.annotation.JsonIgnore

class GithubAuthorDTO {
  val login: String? = null

  @JsonIgnore
  val id: String? = null

  @JsonIgnore
  val node_id: String? = null

  @JsonIgnore
  val avatar_url: String? = null

  @JsonIgnore
  val gravatar_id: String? = null

  @JsonIgnore
  val url: String? = null

  @JsonIgnore
  val html_url: String? = null

  @JsonIgnore
  val followers_url: String? = null

  @JsonIgnore
  val following_url: String? = null

  @JsonIgnore
  val gists_url: String? = null

  @JsonIgnore
  val starred_url: String? = null

  @JsonIgnore
  val subscriptions_url: String? = null

  @JsonIgnore
  val organizations_url: String? = null

  @JsonIgnore
  val repos_url: String? = null

  @JsonIgnore
  val events_url: String? = null

  @JsonIgnore
  val received_events_url: String? = null

  @JsonIgnore
  val type: String? = null

  @JsonIgnore
  val user_view_type: String? = null

  @JsonIgnore
  val site_admin = false
}
