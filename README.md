# Spring Auth API

Example Spingboot API demonstrating an Authenticatin and Authorisation layer.

This service will allow users to authenticate (login) using JWT, as well as check a users authorisation connecting to an exernal Cerbos service.



**Improvements**
Consider using `spring-boot-starter-oauth2-resource-server` to handle JWT authentication. This will mean a lot of the JWT logic is handled for us, along with the securty setup in the SecurtyConfig.java file.