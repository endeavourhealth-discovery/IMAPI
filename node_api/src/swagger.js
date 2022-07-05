// import swaggerAutogen from 'swagger-autogen';
const swaggerAutogen = require('swagger-autogen')()

const doc = {
    info: {
        version: '1.0.0',
      title: 'IMAPI on Express.js',
      description: 'IMAPI backend API Swagger Documentation',
    },
    host: 'localhost:3000',
    schemes: ['http'],
  };
  
  const outputFile = './src/swagger_output.json';
  const endpointsFiles = ['./src/controllers/entityController.ts', './src/controllers/queryController.ts', './src/controllers/searchController.ts'];
  

  swaggerAutogen(outputFile, endpointsFiles).then(() => {
    require('./app.ts')
})
