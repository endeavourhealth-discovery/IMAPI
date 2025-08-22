package org.endeavourhealth.imapi.model.github;

import lombok.Getter;

import java.util.List;

@Getter
public class GithubRelease {
  String version;
  String title;
  String createdDate;
  String publishedDate;
  List<String> releaseNotes;
  String author;
  String url;

  public GithubRelease(String version, String title, String createdDate, String publishedDate, List<String> releaseNotes, String author, String url) {
    this.version = version;
    this.title = title;
    this.createdDate = createdDate;
    this.publishedDate = publishedDate;
    this.releaseNotes = releaseNotes;
    this.author = author;
    this.url = url;
  }

  public GithubRelease() {}

  public GithubRelease setVersion(String version) {
    this.version = version;
    return this;
  }

  public GithubRelease setTitle(String title) {
    this.title = title;
    return this;
  }

  public GithubRelease setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public GithubRelease setPublishedDate(String publishedDate) {
    this.publishedDate = publishedDate;
    return this;
  }

  public GithubRelease setReleaseNotes(List<String> releaseNotes) {
    this.releaseNotes = releaseNotes;
    return this;
  }

  public GithubRelease setAuthor(String author) {
    this.author = author;
    return this;
  }

  public GithubRelease setUrl(String url) {
    this.url = url;
    return this;
  }
}
