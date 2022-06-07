import Graphdb from 'graphdb';

const { ServerClientConfig, ServerClient } = Graphdb.server;
const { RDFMimeType } = Graphdb.http;
const { RepositoryClientConfig } = Graphdb.repository;
const { GetQueryPayload, QueryType, UpdateQueryPayload } = Graphdb.query;
const { QueryContentType } = Graphdb.http;
const { SparqlJsonResultParser } = Graphdb.parser;

export class GraphdbService {
  private serverConfig;
  private server;
  private repoConfig;
  private repo;

  public async update(sparql: string): Promise<boolean> {
    try {
      const client = await this.getRepo();

      client.registerParser(new SparqlJsonResultParser());

      const stmt = new UpdateQueryPayload()
        .setQuery(sparql)
        .setContentType(QueryContentType.X_WWW_FORM_URLENCODED)
        .setInference(true)
        .setTimeout(5);

      return client.update(stmt).then(() => {
        return true;
      });

    } catch (e) {
      console.error("********* ERROR!!")
      console.error(e);
      return false;
    }
  }

  public async execute(sparql: string, bindings?: any): Promise<any[]> {
    try {
      const client = await this.getRepo();

      const stmt = new GetQueryPayload()
        .setQuery(sparql)
        .setQueryType(QueryType.SELECT)
        .setResponseType(RDFMimeType.SPARQL_RESULTS_JSON);

      if (bindings) {
        for (const key of Object.keys(bindings)) {
          stmt.addBinding('$' + key, bindings[key]);
        }
      }

      const rs = await client.query(stmt);

      const result: any[] = [];
      rs.on('data', (binding) => {
        result.push(binding);
      });
      await new Promise(done => rs.on('end', done));

      return result;
    } catch (e) {
      console.error("********* ERROR!!")
      console.error(e);
      return [];
    }
  }

  private async getRepo() {
    if (this.repo == null) {
      await this.connect();
      this.repo.registerParser(new SparqlJsonResultParser());
    }

    return this.repo;
  }

  private async connect() {
    const timeout = process.env.GRAPH_TIMEOUT || 30000;
    this.serverConfig = new ServerClientConfig(process.env.GRAPH_HOST)
      .setTimeout(timeout)
      .setHeaders({
        'Accept': RDFMimeType.SPARQL_RESULTS_JSON
      })
      .setKeepAlive(true);

    this.server = new ServerClient(this.serverConfig);

    this.repoConfig = new RepositoryClientConfig(process.env.GRAPH_HOST)
      .setEndpoints([process.env.GRAPH_HOST + '/repositories/' + process.env.GRAPH_REPO])
      .setReadTimeout(timeout)
      .setWriteTimeout(timeout);

    this.repo = await this.server.getRepository(process.env.GRAPH_REPO, this.repoConfig);
  }

}

export function iri(url: string) {
  return '<' + url + '>';
}
