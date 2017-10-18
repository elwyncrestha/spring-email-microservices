## Spring Boot Email micro services demo

This project use Spring Framework 5.0 (https://spring.io/) and is implemented using Kotlin language (https://kotlinlang.org/)

For detail tracing we could use zipking for distributed tracing


### email-client
This is a logical edge of the architecture. Responding to requests to actual clients like (html5,mobile,iot etc)

uses:
* zuulu micro proxy (reversed proxy) for setting routes for application based on services registered in service registery with client side load balancing
* eureka client for service discovery
* cloud config for remote configs
* oauth2 for sercurity
* rabbitMQ for (in case undelaying service is not available) delivering data to email service (eventually) 
* feign for simpler rest client declaration 
* hystrix for circuit breaker implementation that provides fallback in case of email service down (gracefull degradation)
* actuator for service monitoring

tests:
* just one test, not fully covered

## email-service
This is underlaying email micro service that handle email sends. It uses Send Grid or Mail Gun (in cave Send Grid returns error) to handle emails messages.

uses:
* eureka client for service discovery
* cloud config for remote configs
* actuator for service monitoring

tests:
* about 50-60% coverage just for demo

## config-service
This component provides configiration for our micro services. Enables hot/remote configuration

## eureka-service
This component handles services registery for discovery

## auth-service
This component handles oauth2 email-client protection. For now just email-client is protected, all communication between micro-services should be!

## hystrix-dashboard
This component allows to monitor our micro services (log streams). It exposes hystrix.stream at out edge

