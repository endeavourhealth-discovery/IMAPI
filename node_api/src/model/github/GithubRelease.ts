export default class GithubRelease {
    version: string;
    title: string;
    createdDate: Date;
    publishedDate: Date;
    releaseNotes: string[];
    author: string;
    url: string

    constructor(version: string, title: string, createdDate: Date, publishedDate: Date, releaseNotes: string, author: string, url: string) {
        this.version = version;
        this.title = title;
        this.createdDate = createdDate;
        this.publishedDate = publishedDate;
        this.releaseNotes = this.processReleaseNotes(releaseNotes);
        this.author = author;
        this.url = url;
    }

    processReleaseNotes(releaseNotes: string): string[] {
      return releaseNotes.split('\r\n').map(item => item.replaceAll("-   ", "").trim());
    }
}