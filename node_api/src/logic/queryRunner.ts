import {MysqlService} from '../services/mysql.service';
import {GraphdbService, iri} from '../services/graphdb.service';

export default class QueryRunner {
  private mysql: MysqlService;
  private graph: GraphdbService;

  constructor() {
    this.mysql = new MysqlService();
    this.graph = new GraphdbService();
  }

  public async runQuery(queryIri: string): Promise<any> {
    try {
      console.log("Loading " + queryIri);
      const rs = await this.graph.execute("SELECT * WHERE { ?s <http://endhealth.info/im#definition> ?def } LIMIT 1", { s: iri(queryIri) });

      if (rs.length != 1)
        return;

      const definition = JSON.parse(rs[0].def);

      console.log(definition);
      return await this.mysql.test();
    } catch (e) {
      console.error("***** ERROR!!");
      console.log(e);
      return {};
    }
  }
}
