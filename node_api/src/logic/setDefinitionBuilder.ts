import { Services, Vocabulary } from "im-library/dist/api";
import axios from "axios";
import { buildQueryDisplayFromQuery } from "./queryBuilder/displayBuilder";

const { RDFS, IM } = Vocabulary;
const { EntityService } = Services;
const entityService = new EntityService(axios);

export async function buildSetDefinition(iri: string) {
  const response = await entityService.getPartialEntity(iri, [
    IM.DEFINITION,
    IM.HAS_SUBSET,
    IM.IS_SUBSET_OF,
    IM.HAS_MEMBER,
    IM.IS_CONTAINED_IN,
    RDFS.SUBCLASS_OF
  ]);
  const entity = response.data;
  if (entity[IM.DEFINITION]) entity.queryDisplay = await buildQueryDisplayFromQuery(entity[IM.DEFINITION]);
  return entity;
}
