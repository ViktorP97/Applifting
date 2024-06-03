# Endpoints Monitoring Service
This is a REST API JSON Java microservice that allows you to monitor particular HTTP/HTTPS URLs. It provides functionalities to create, edit, and delete monitored URLs, list them for a particular user, monitor URLs in the background, log status codes and the returned payload, and list the last 10 monitored results for each monitored URL.

# Setup
1. Clone the repository
2. Create env file and set up the MySQL database
   - add the following configuration parameters to the .env file:
     DB_URL=
     DB_USERNAME=
     DB_PASSWORD=
     HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
     HIBERNATE_DDL=update
     - set the values of DB_URL, DB_USERNAME, and DB_PASSWORD with your MySQL database connection URL, username, and password
3. Build the project
4. Run the application

# Usage

# Authentication
Authentication is done in the HTTP header using the accessToken provided. The user must include their accessToken in the header of each request.

# Predefined Users
Two users are automatically created upon starting the application:

1. User: Applifting

Name: Applifting
Email: info@applifting.cz
AccessToken: 93f39e2f-80de-4033-99ee-249d92736a25

2. User: Batman

Name: Batman
Email: batman@example.com
AccessToken: dcb20f8a-5657-4f1b-9f7f-ce65739b359e

# Endpoints
1. POST /api/endpoints
 Create a monitored endpoint. The request body should contain the details of the endpoint to be created.
Example Request Body:

{
    "name": "My Endpoint",
    "url": "http://example.com",
    "monitoredInterval": 45
}

2. GET /api/endpoints.
 Retrieve all monitored endpoints for the authenticated user.

3. PUT /api/endpoints/{id}
 Update a monitored endpoint with the specified ID. The request body should contain the updated details of the endpoint.
Example Request Body:
{
    "name": "My Updated Endpoint",
    "url": "http://updated-example.com",
    "monitoredInterval": 60
}

4. DELETE /api/endpoints/{id}
 Delete a monitored endpoint with the specified ID.

5. GET /api/results/{id}
Retrieve the last 10 monitored results for the endpoint with the specified ID.
