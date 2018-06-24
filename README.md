# Company management

This is a REST / JSON web service in Java using Spring MVC (RestController) with an API thatsupports the following:
* Create new company
* Get a list of all companies
* Get details about a company
* Able to update a company
* Able to add beneficial owner(s) of the company

## About

The application is developed with Spring Boot 2.0.2.RELEASE and maven as a building tool.
The database used is mysql and is connected to an instance of Amazon RDS.

## To run the application

Use one of the several ways of running a Spring Boot application.

1. Build using maven goal: `mvn clean package` and execute the resulting artifact as follows `java -jar company-management-1.0-SNAPSHOT.jar`
2. On Unix/Linux based systems: run `mvn clean package` then run the resulting jar as any other executable `./company-management-1.0-SNAPSHOT.jar`


## Running the integration tests

Run using maven goal: `mvn test`

## To manually test the application

I used POSTMAN and cURL to test the application. 

### Create new company

```
POST - http://localhost:8080/companies or http://34.209.73.115:8080/companies
{
    "name": "NewCompanyInIasi",
    "address": "Iasi",
    "city": "Iasi",
    "country" : "Romania",
    "phoneNumber": "123",
    "email": "a@a.com", 
    "beneficialOwners": [{"name": "NewOwnerInTown"}]
}
```
or 
```
Windows: curl -i -X POST -H "Content-Type:application/json" http://34.209.73.115:8080/companies/ -d "{"""name""":"""NewCompanyInBucharest""","""address""":"""Bucharest""","""city""":"""Bucharest""","""country""":"""Romania""","""phoneNumber""":"""1234""","""email""":"""a@a.com""","""beneficialOwners""":[{"""name""":"""NewOwnerInBucharest"""}]}"
```
Response: 200 OK
```
{
    "id": 1,
    "name": "NewCompany",
    "address": "Iasi",
    "city": "Iasi",
    "country": "Romania",
    "email": "a@a.com",
    "phoneNumber": "123",
    "beneficialOwners": [
        {
            "id": 51,
            "name": "NewOwner"
        }
    ]
}
```
Create company without mandatory fields
```
Windows: curl -i -X POST -H "Content-Type:application/json" http://34.209.73.115:8080/companies/ -d "{"""name""":"""NewCompanyInBucharest"""}"
```
Response: 400 BAD REQUEST

If a company with the same name already exists:
```
Response code: 409 Conflict
Message: "Company already exits"
```

If a beneficial owner is already assign to a company:
```
Response code: 409 Conflict
Message: "Beneficial owner already exits and it is assigned to a company."
```

### Get details about a company

```
GET - http://localhost:8080/companies/1 or http://34.209.73.115:8080/companies/1
'curl http://34.209.73.115:8080/companies/1'
```
Response: 200 OK
```
{
    "id": 1,
    "name": "NewCompany",
    "address": "Iasi",
    "city": "Iasi",
    "country": "Romania",
    "email": "a@a.com",
    "phoneNumber": "123",
    "beneficialOwners": [
        {
            "id": 51,
            "name": "NewOwner"
        }
    ]
}
```

If a company with does not exists:
```
Response code: 404 NOT FOUND
Message: "Company with id *** not found."
```

### Get a list of all companies

```
GET - http://localhost:8080/companies or http://34.209.73.115:8080/companies
'curl http://34.209.73.115:8080/companies'
```
Response: 200 OK
```
[
    {
        "id": 15,
        "name": "MyCompany99993",
        "address": "Iasi",
        "city": "Iasi",
        "country": "Romania",
        "email": "a@a.com",
        "phoneNumber": "123",
        "beneficialOwners": [
            {
                "id": 16,
                "name": "Stefan6"
            }
        ]
    },
    {
        "id": 17,
        "name": "MyCompany9",
        "address": "Iasi",
        "city": "Iasi",
        "country": "Romania",
        "email": "a@a.com",
        "phoneNumber": "123",
        "beneficialOwners": []
    }
]
```

### Update a company

```
PUT - http://localhost:8080/companies/1 or http://34.209.73.115:8080/companies/1
{
    "name": "NewCompanyInIasi",
    "address": "New Address",
    "city": "Iasi",
    "country" : "Romania",
    "phoneNumber": "123",
    "email": "a@a.com"
}
```
or 
```
Windows: curl -i -X PUT -H "Content-Type:application/json" http://34.209.73.115:8080/companies/1 -d "{"""name""":"""NewCompanyInLondon""","""address""":"""New AddressLondon""","""city""":"""London""","""country""":"""England""","""phoneNumber""":"""123""","""email""":"""a@a.com"""}"
```

Response: 200 OK
```
{
    "id": 1,
    "name": "NewCompanyInIasi",
    "address": "New Address",
    "city": "Iasi",
    "country": "Romania",
    "email": "a@a.com",
    "phoneNumber": "123",
    "beneficialOwners": null
}
```

If a company does not exists:
```
Response code: 404 NOT FOUND
Message: "Company does not exist."
```

### Add beneficial owners to a company

```
POST - http://localhost:8080/companies/1/owner or http://34.209.73.115:8080/companies/1/owner
[{
    "name": "SecondOwner"
}]
```
or 
```
Windows: curl -i -X POST -H "Content-Type:application/json" http://34.209.73.115:8080/companies/1/owner -d "[{"""name""":"""NewSecondOwner"""}]"
```
Response: 200 OK
```
{
    "id": 1,
    "name": "NewCompany",
    "address": "Iasi",
    "city": "Iasi",
    "country": "Romania",
    "email": "a@a.com",
    "phoneNumber": "123",
    "beneficialOwners": [
        {
            "id": 51,
            "name": "NewOwner"
        },
		{
            "id": 52,
            "name": "SecondOwner"
        }
    ]
}
```

If a company does not exists:
```
Response code: 404 NOT FOUND
Message: "Company does not exist."
```

If a beneficial owner is already assign to a company:
```
Response code: 409 Conflict
Message: "Beneficial owner already exits and it is assigned to a company."
```

## Authentication for the API

TODO - I would add to the REST service Oauth2 with jwt for security. 

## Deployment

The web service is deployed on AWS and can be accessed at 34.209.73.115

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Radu Stefan Lacatusu** - [radulacatusu](https://github.com/radulacatusu/)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details