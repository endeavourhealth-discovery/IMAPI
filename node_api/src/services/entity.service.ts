import Env from "@/services/env.service";
import { TTBundle, TTIriRef } from "im-library/dist/types/interfaces/Interfaces";

import { Vocabulary } from "im-library/dist/api";
import { buildDetails } from "@/builders/entity/detailsBuilder";

const { IM, RDFS, RDF } = Vocabulary;
export default class EntityService {
  axios: any;

  constructor(axios: any) {
    this.axios = axios;
  }

  public async getPartialEntity(iri: string, predicates: string[]): Promise<any> {
    try {
      return await this.axios.get(Env.API + "api/entity/public/partial", {
        params: {
          iri: iri,
          predicates: predicates.join(",")
        }
      });
    } catch (error) {
      return {} as any;
    }
  }

  public async getBundleByPredicateExclusions(iri: string, predicates: string[]): Promise<TTBundle> {
    try {
      return (
        await this.axios.get(Env.API + "api/entity/public/bundleByPredicateExclusions", {
          params: { iri: iri, predicates: predicates.join(",") }
        })
      ).data;
    } catch (error) {
      return {} as TTBundle;
    }
  }

  public async getPartialEntities(typeIris: string[], predicates: string[]): Promise<any[]> {
    const promises: Promise<any>[] = [];
    typeIris.forEach(iri => {
      promises.push(this.getPartialEntity(iri, predicates));
    });
    try {
      return await Promise.all(promises);
    } catch (error) {
      return [];
    }
  }

  public async getDistillation(refs: TTIriRef[]): Promise<TTIriRef[]> {
    try {
      const response = await this.axios.post(Env.API + "api/entity/public/distillation", refs);
      return response.data;
    } catch (error) {
      return [] as TTIriRef[];
    }
  }

  public async getDetailsDisplay(iri: string): Promise<any[]> {
    try {
      const response = await this.getBundleByPredicateExclusions(iri, [IM.CODE, RDF.TYPE, RDFS.LABEL, IM.HAS_STATUS, RDFS.COMMENT]);
      return buildDetails(response);
    } catch (error) {
      return [] as any[];
    }
  }
}
