package org.endeavourhealth.imapi.model.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubDTO {
  @JsonIgnore
  private String url;
  @JsonIgnore
  private String html_url;
  @JsonIgnore
  private String assets_url;
  @JsonIgnore
  private String upload_url;
  @JsonIgnore
  private String tarball_url;
  @JsonIgnore
  private String zipball_url;
  @JsonIgnore
  private String id;
  @JsonIgnore
  private String node_id;
  private String tag_name;
  @JsonIgnore
  private String target_commitish;
  private String name;
  private String body;
  @JsonIgnore
  private String draft;
  @JsonIgnore
  private String prerelease;
  @JsonIgnore
  private String immutable;
  private String created_at;
  private String published_at;
  private GithubAuthorDTO author;
  @JsonIgnore
  private Object assets;
}
