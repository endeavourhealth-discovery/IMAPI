import AWS from 'aws-sdk';

class CognitoService {
  private config = {
    region: (process.env.COGNITO_REGION || "eu-west-2")
  }

  private cognitoIdentity;

  constructor() {
    this.cognitoIdentity = new AWS.CognitoIdentityServiceProvider(this.config);
  }
}
