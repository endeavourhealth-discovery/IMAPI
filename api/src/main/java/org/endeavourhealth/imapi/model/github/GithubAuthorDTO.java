package org.endeavourhealth.imapi.model.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubAuthorDTO {
  private String login;
  @JsonIgnore
  private String id;
  @JsonIgnore
  private String node_id;
  @JsonIgnore
  private String avatar_url;
  @JsonIgnore
  private String gravatar_id;
  @JsonIgnore
  private String url;
  @JsonIgnore
  private String html_url;
  @JsonIgnore
  private String followers_url;
  @JsonIgnore
  private String following_url;
  @JsonIgnore
  private String gists_url;
  @JsonIgnore
  private String starred_url;
  @JsonIgnore
  private String subscriptions_url;
  @JsonIgnore
  private String organizations_url;
  @JsonIgnore
  private String repos_url;
  @JsonIgnore
  private String events_url;
  @JsonIgnore
  private String received_events_url;
  @JsonIgnore
  private String type;
  @JsonIgnore
  private boolean site_admin;


}
