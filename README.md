# Spring Auth API

Example Spingboot API demonstrating an Authentication and Authorisation layer.

This service will allow users to authenticate (login) using JWT, as well as check a users authorisation connecting to an exernal Cerbos service.

Document how to use the service...

## Setup

- To start the service, firstly install Maven dependancies:
```
make install
```

- Set the `JWT_SECRET` in the `.env` file. To create a jwt secret the following command can be used:
```
make jwt-secret
```

- Set the `POLICY_SERVICE_URL` in the `.env` file. This can either be a locally running Cerbos Policy service, or it can be pointed to a public URL. The default localhost URL for a Cerbos Policy service is:
```
http://localhost:3592
```


- Finally start the service:
```
make dev
```

Once the service is running, the API can be accessed in the browser at the following url:
http://localhost:8080/swagger-ui/index.html#/


## Usage
The service offers both the functionality of Authenticating a user, and checking a user's Authorization. However in order to use the Authorization endpoints, you must be authenticated.

This is possible using the Swagger UI (Open API) via the browser.

There is only one **Resource Type** setup, which is called `post`. More could be setup, however this would need to be handled in the Policy Service, and then more endpoints could be created here for each action.


**In Memory Database - For Demonstration Purposes**
Although the service uses the standard Springboot Repository functionality to query the database, the database has been populated with fake data, as this service is just for demonstration. The logic for this is handled in the [`InMemoryUserRepository`](https://github.com/joepk90/spring-auth-api/blob/a67de4f40f4bb92024df1e6c5080a45360435d25/src/main/java/com/springauthapi/authservice/database/InMemoryUserRepository.java).

### Authentication (Login)
To login, use the `/auth/login` under the `AuthController`.

Using the In Memory Data, the following users are can be used to login:

```
# user id: 1 | role: user
{
  "email": "johnsmith@gmail.com",
  "password": "userpass"
}
```

```
# user id: 2 | role: agent
{
  "email": "johndoe@gmail.com",
  "password": "agentpass"
}
```

```
# user id: 3 | role: manager
{
  "email": "janesmith@gmail.com",
  "password": "managerpass"
}
```

```
# user id: 4 | role: admin
{
  "email": "janedoeh@gmail.com",
  "password": "adminpass"
}
```

```
# user id: 5 | role: owner
{
  "email": "charlesburns@gmail.com",
  "password": "ownerpass"
}
```

Then using the `jwt` token in the response, you can login using the **Authorize** button in the top right of the Swagger UI.

<i>Note: Although it is also possible to register new users, it isn't possible to update a User's Role, which is critical for the Authorization configuration.</i>


### Authorization
The Authorization endpoints are listed under the **Resource Controller**.

**The Root Resource Endpoint (POST)**
The root Resource endpoint is the most open, and can be used to check a User's permissions against any numer of actions of that resource.

<i>Note: only one **Resource Type** has been setup</i> 

**The Specific Resource Action Endpoint**
Each **Action Type** have their own endpoint under the post resource, to more easily check if a user is able to perform a certain action on a resource - These are all `GET` requests.

There is the option of checking if a user has permission to perform a certain action against a specific record of that resource - These are the `POST` requests.

In order for the resource request to succeed and return `EFFECT_ALLOW` (`EFFECT_DENY` would mean the user does not have permission), the `resourceOwnerId` must match the ID of the user. So if you were to authenticate as `johnsmith@gmail.com`, the `resourceOwnerId` would need to be `1`. This is to simulate how another service might interact with this Auth Service. The other service can pass provide the Resource ID, but only this service can check User ID of the authenticated user.

The post resource type endpoints are setup as:
- `/post/view`
- `/post/edit`
- `/post/delete`
- `/post/create`


#### Derived Roles
The User ID must match the Resource Owner ID.

## Examples

### Authorization Failure (EFFECT_DENY)
- Login as User 1 (johnsmith@gmail.com)
- User the `/resource/post/edit` (POST) endpoint

The following response is returned because the `User` role does not have permission to edit records of the `post` resource type:
```
{
  "action": "User is not Authorized to VIEW the POST resource.",
  "isAuthorized": false
}
```

### Authorization Success (EFFECT_ALLOW)
- Login as User 5 (charlesburns@gmail.com)
- User the `/resource/post/edit` (POST) endpoint

The following response is returned because the `Owner` Role does have permission to edit records of the `post` resource type:
```
{
  "action": "User is Authorized to EDIT the POST resource.",
  "isAuthorized": true
}
```

### Authorization Success using the Derived Role (EFFECT_ALLOW)
- Login as User 1 (johnsmith@gmail.com)
- User the `/resource/post/edit` (POST) endpoint
- Populate the `resourceOwnerId` property on the request body with the value of `1`

The following response is returned because User `johnsmith@gmail.com` has the user ID `1`, which matches the id passed to the `resourceOwnerId` property on the request body, effectively promoting the user to the Derived Role of `owner` for this specific Resource record.
```
{
  "action": "User is Authorized to EDIT the POST resource.",
  "isAuthorized": true
}
```


## Authorization Policies (Cerbos Policies)
The Cerbos Policies can be found here:
[ithub.com/joepk90/cerbos-example](https://github.com/joepk90/cerbos-example).


## Potential Improvements
- Better usage of the roles could be setup. Currently `View` doesn't exist, and a lot of the other roles aren't used in the Policy Service.
- Consider using `spring-boot-starter-oauth2-resource-server` to handle JWT authentication. This will mean a lot of the JWT logic is handled for us, along with the securty setup in the SecurtyConfig.java file.