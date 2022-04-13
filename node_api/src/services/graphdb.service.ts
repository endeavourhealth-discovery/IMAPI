import Graphdb from 'graphdb';

const {ServerClientConfig, ServerClient} = Graphdb.server;
const {RDFMimeType} = Graphdb.http;
const {RepositoryClientConfig} = Graphdb.repository;
const {GetQueryPayload, QueryType} = Graphdb.query;
const {SparqlXmlResultParser} = Graphdb.parser;

export class GraphdbService {
  private serverConfig;
  private server;
  private repoConfig;
  private repo;

  constructor() {
    this.serverConfig = new ServerClientConfig('http://localhost:7200')
      .setTimeout(5000)
      .setHeaders({
        'Accept': RDFMimeType.SPARQL_RESULTS_JSON
      })
      .setKeepAlive(true);

    this.server = new ServerClient(this.serverConfig);

    this.repoConfig = new RepositoryClientConfig('http://localhost:7200')
      .setEndpoints(['http://localhost:7200/repositories/im'])
      .setReadTimeout(30000)
      .setWriteTimeout(30000);

    this.repo = this.server.getRepository('im', this.repoConfig);
  }

  public async execute(sparql: string): Promise<any[]> {
    const client = await this.repo;

    client.registerParser(new SparqlXmlResultParser());

    const stmt = new GetQueryPayload()
      .setQuery(sparql)
      .setQueryType(QueryType.SELECT)
      .setResponseType(RDFMimeType.SPARQL_RESULTS_XML);

    const rs = await client.query(stmt);

    const bindings: any[] = [];
    rs.on('data', (binding) => bindings.push(binding));
    await new Promise(done => rs.on('end', done));

    return bindings;
  }
}
