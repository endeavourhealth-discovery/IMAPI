// import swaggerAutogen from 'swagger-autogen';
const swaggerAutogen = require('swagger-autogen')()

const doc = {
    info: {
      title: 'IMAPI on Express.js',
      description: 'IMAPI backend API Swagger Documentation',
    },
    host: 'localhost:3000',
    schemes: ['http'],
  };
  
  const outputFile = './swagger_output.json';
  const endpointsFiles = ['./src/controllers/entityController.ts', './src/controllers/queryController.ts', './src/controllers/searchController.ts'];
  

  swaggerAutogen(outputFile, endpointsFiles).then(() => {
    require('./app.ts')
})