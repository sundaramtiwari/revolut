# revolut

A Simple RESTful API (including data model and the backing implementation) for money transfers between accounts.
Kept it simple and to the point. No additional features or use-case implemented except for the same of testing.
No Authentication implemented.
In-memory datastore used.
The final result is an executable jar and does not requires a pre-installed container/server as embedded jetty is used.
Demonstrated with test that the API works as expected.

Embedded jetty with jersey implementation.

# how to test
mvn clean install

This builds and executes the test case for money transfer API.
