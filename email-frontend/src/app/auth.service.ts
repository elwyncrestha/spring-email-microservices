import { Injectable } from '@angular/core';

@Injectable()
export class AuthService {

  private authHeader: string = "Basic YWNtZTphY21lc2VjcmV0";

  private clientId: string = "acme";
  private clientSecret: string = "acmesecret"
  private scope: string = "email"
  private grandType: string = "password"

  public SetAuthHeader(newHeader: string){
    this.authHeader = newHeader;
  }

  public GetAuthHeader() : string{
    return this.authHeader;
  }

  public GetClientId() : string {
    return this.clientId;
  }

  public GetClientSecret() : string {
    return this.clientSecret;
  }

  public GetScope() : string {
    return this.scope;
  }

  public GetGrandType() : string {
    return this.grandType;
  }
}
