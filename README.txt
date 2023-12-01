###################################################################
#
# SPRINGBOOT RESRT API QA 9https://www.bezkoder.com/spring-boot-jpa-crud-rest-api/)
#
###################################################################

//-----------------------------------
A) RUN:
//-----------------------------------

- add new port to application.properties: server.port=8083
- type in terminal: mvn spring-boot:run

//-----------------------------------
B) TEST - POSTMAN:
//-----------------------------------

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

//-----------------------------------
C) APACHE PROXY URL REVERSE:
//-----------------------------------

- Ensure apache installed.
- Update config file for virtual-host: trantri@ubuntu:/etc/apache2/sites-available$ sudo gedit 000-default.conf
- restart apache: sudo systemctl restart apache2


<VirtualHost *:80>

    ProxyPreserveHost On

    # For testing only run by flask: trantri@ubuntu:~/SENS3_TUTORIALS/APACHE2$ FLASK_APP=backend2.py flask run --port=8081
    ProxyPass /backend1 http://127.0.0.1:8080/
    ProxyPassReverse /backend1 http://127.0.0.1:8080/

    # For testing only run by flask: trantri@ubuntu:~/SENS3_TUTORIALS/APACHE2$ FLASK_APP=backend1.py flask run --port=8080
    ProxyPass /backend2 http://127.0.0.1:8081/
    ProxyPassReverse /backend2 http://127.0.0.1:8081/
    
 	# ------------------------------
    # REST APIS: http://127.0.0.1:8087/api/people
    # ------------------------------
    <Location "/api">
	    # GET ALL PEOPLE (http://127.0.0.1/people)
	    ProxyPass http://127.0.0.1:8087/api
	    ProxyPassReverse http://127.0.0.1:8087/api
	    
	    # GET PERSON (http://127.0.0.1/person/123)
	    #ProxyPass /person http://127.0.0.1:8087/api/person
	    #ProxyPassReverse /person http://127.0.0.1:8087/api/person
    
	</Location>
    

    # ------------------------------
    # REACT
    # ------------------------------
	<Location "/react-bootstrap-demo">
    	ProxyPass  http://localhost:8085/react-bootstrap-demo
    	ProxyPassReverse  http://localhost:8085/react-bootstrap-demo
	</Location>

    
    # ------------------------------
    # SOLR : http://127.0.0.1/solr/historySearch/select?indent=true&q.op=OR&q=*%3A*
    # ------------------------------
    <Location "/solr">
        ProxyPass  http://127.0.0.1:8983/solr
    	ProxyPassReverse  http://127.0.0.1:8983/solr

    </Location>
    
</VirtualHost>


