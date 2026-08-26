package org.endeavourhealth.imapi.model.github

class GithubRelease {
  var version: String? = null
    private set
  var title: String? = null
    private set
  var createdDate: String? = null
    private set
  var publishedDate: String? = null
    private set
  var releaseNotes: MutableList<String>? = null
    private set
  var author: String? = null
    private set
  var url: String? = null
    private set

  constructor(
    version: String?,
    title: String?,
    createdDate: String?,
    publishedDate: String?,
    releaseNotes: MutableList<String>?,
    author: String?,
    url: String?
  ) {
    this.version = version
    this.title = title
    this.createdDate = createdDate
    this.publishedDate = publishedDate
    this.releaseNotes = releaseNotes
    this.author = author
    this.url = url
  }

  constructor()

  fun setVersion(version: String?): GithubRelease {
    this.version = version
    return this
  }

  fun setTitle(title: String?): GithubRelease {
    this.title = title
    return this
  }

  fun setCreatedDate(createdDate: String?): GithubRelease {
    this.createdDate = createdDate
    return this
  }

  fun setPublishedDate(publishedDate: String?): GithubRelease {
    this.publishedDate = publishedDate
    return this
  }

  fun setReleaseNotes(releaseNotes: MutableList<String>?): GithubRelease {
    this.releaseNotes = releaseNotes
    return this
  }

  fun setAuthor(author: String?): GithubRelease {
    this.author = author
    return this
  }

  fun setUrl(url: String?): GithubRelease {
    this.url = url
    return this
  }
}
