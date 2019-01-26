A demonstration on getting a resource server to work with an oauth sever.

The grant type supported is `authroization_code`.

The resource server also provides an endpoint to be able to get an access code. This is intended for clients
that can't get access token due to inability to expose secrets.

When a client hits an api request without a access token, a 401 is returned.
The client can then access the request token via:
http://localhost:8090/example-oauth-server/oauth/authorize?client_id=example_client_id&redirect_uri=http://localhost:8091/example-resource-server/v1/auth/authenticated?client_redirect_uri=http://localhost:8091/example-resource-server/v1/sample/app-redirect&response_type=code&grant_type=authorization_code

The `client_redirect_uri` is the URI to which th client expects the access token to arrive via a request param.

A client can then add the access token as a Bearer token header when calling the api.

An example js client is available in the `resource-server/client` folder.

- Run both auth-server	and resource-server with their defaults
- Put the index.html and script.js to your web server context and access the index.html
- Clicking the `Generate UUID` button would attempt making a call to the UUID endpoing
- Failing which it would do a redirect to the auth url and thus authenticating and
  authorising the user. The user creds can be found in the auth-server
- The user would be brought back to the same page, but a `code` param will appear 
  returning url
- Clicking the `Generate UUID` button again will make a successful call to the api
  and show the generated UUID


