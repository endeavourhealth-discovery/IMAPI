import QueryRunner from "./queryRunner";

export default class QUeryBuilder extends QueryRunner { 

    constructor(){
        super();
    }

    protected async populateLabels(queryIri: string): boolean {

        const query = await this.getDefinition(queryIri);

        


    }

}