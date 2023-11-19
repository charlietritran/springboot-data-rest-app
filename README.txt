https://www.bezkoder.com/spring-boot-jpa-crud-rest-api/

RUN: 
- add new port to application.properties: server.port=8083
- type in terminal: mvn spring-boot:run

TEST - POSTMAN:
+ POST: (ADD)
- url: http://localhost:8087/api/tutorials
- Content-type:application/json
- Body / raw:
{
	"title":"Try springboot-1",
	"description":"This is a test"

}

+ PUT: ( UPDATE)
- url: http://localhost:8087/api/tutorials/1
- Content-type:application/json
- Body / raw:
{
	"title":"Try springboot-2",
	"description":"This is a test 2"

}

+ GET:
- url: http://localhost:8087/api/tutorials ( find all)
- url: http://localhost:8087/api/tutorials/1 (Find tutorial at id = 1)
- url: http://localhost:8087/api/tutorials/title=book (Find all Tutorials which title contains ‘boot’)

+ DELETE: 
- url: http://localhost:8087/api/tutorials/1 (DELETE record at index = 1

