A demonstration on getting a resource server to work with an oauth sever.

The grant type supported is `authroization_code`.

The resource server also provides an endpoint to be able to get an access code. This is intended for clients
that can't get access token due to inability to expose secrets.

When a client hits an api request without a access token, a 401 is returned.
The client can then access the request token via:
http://localhost:8090/example-oauth-server/oauth/authorize?client_id=example_client_id&redirect_uri=http://localhost:8091/example-resource-server/v1/auth/authenticated?client_redirect_uri=http://localhost:8091/example-resource-server/v1/sample/app-redirect&response_type=code&grant_type=authorization_code

The `client_redirect_uri` is the URI to which th client expects the access token to arrive via a request param.

A client can then add the access token as a Bearer token header when calling the api.


