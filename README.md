## Spring Boot Email micro services demo

This project use Spring Framework 5.0 (https://spring.io/) and is implemented using Kotlin language (https://kotlinlang.org/) using gradle build system.
Project is not completed (can't make it on time) but tried to focus on overall microservices architecure. I tried to experiment with newes Spring (first time accualy) that uses reactive web components (non-blocking I/O). 

Overall architecture brief

[clients-frontend(web/mobile/etc)] -> [email-client(architecture edge with micro proxy that handles oauth for downstream servers] -> [email-service(acctual downstream service)]

[eureka service(for discovery] [config-service(remote configuration)] [auth-service(handles security)] [hystrix-dashboard (for monitoring)]

Actuator is using for services monitoring on (/application/health in each service context)

For detail tracing we could use zipking for distributed tracing (not used in this demo, but possible)

Frontent (client-frontend) is not completed. I used angualr and faced some CORS problems with reactive web (didn't have time to investigate more). More about this in detalis below.

Project is not deployed anywhere. After completion I would use docker containers for microservices and deployed propably using Kubernetes

All microservices are working so it is possible to run them on local machine and test using CURL/Postman

Not all microservices has test and if -> not full covered. Best cover is in email-service

Validation and proper error handlings are not implemented

More details about system:

### config-server
This component provides configurations for microservices. To configre we need to setup git repository that will be used as a lookup.
Example is https://github.com/brzostek/spring-microservices-config. After clone, we should set proper uri in application.properties
spring.cloud.config.server.git.uri=/location/of/clonned/services/configs
Should be running before other services to provide those configurations

### email-client
This is a logical edge of the architecture. Responding to requests to actual clients like (html5,mobile,iot etc)

uses:
* zuulu micro proxy (reversed proxy) for setting routes for application based on services registered in service registery with client side load balancing and oauth support
* eureka client for service discovery
* cloud config for remote configs
* oauth2 for sercurity
* rabbitMQ for (in case undelaying service is not available) delivering data to email service (eventually) 
* feign for simpler rest client declaration 
* hystrix for circuit breaker implementation that provides fallback in case of email service down (gracefull degradation)
* actuator for service monitoring (/application/health)

tests:
* just one test, not fully covered

This use rabbitMQ in case of fail over of downstream email-service microservice. To use it https://www.rabbitmq.com/ need to be installed

### email-service
This is underlaying email micro service that handle email sends. It uses Send Grid or Mail Gun (in cave Send Grid returns error) to handle emails messages.

uses:
* eureka client for service discovery
* cloud config for remote configs
* actuator for service monitoring
* secured by oauth2

tests:
* about 50-60% coverage just for demo

### eureka-service
This component handles services registery for discovery
* cloud config for remote configs
* actuator for service monitoring

### auth-service
This component handles oauth2 email-client protection. For now just email-client is protected, all communication between micro-services should be!
It has resource server (this could/should be split to another service for resource authorization). No real authorization server, just mockuped users
JWT enabled.

* eureka client for service discovery
* cloud config for remote configs
* actuator for service monitoring

uses /uaa context for oauth

for getting access token call /uaa/oauth/token
clientId: acme
clientSecret: acmesecret

moked users[username/password]:
* pbrzostowski/fractal
* user/password

example body:

password:fractal
username:pbrzostowski
grant_type:password
scope:email
client_secret:acmesecret
client_id:acme

### hystrix-dashboard
This component allows to monitor our micro services (log streams). It exposes hystrix.stream at out edge
* eureka client for service discovery
* cloud config for remote configs
* actuator for service monitoring

### client-frontned
This should be an angular app client frontend. Didn't have time to finished :/. 
Is it possible to run. login form is working because underlying auth-service not using reactive web, so CORS was easy to setup (not ideal i know).
Email sending not working because of 401 on OPTIONS call from angular (CORS problem as said before).


