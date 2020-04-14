package contracts.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'create local user'
    description 'should return status 201 and UserDTO'
    request {
        method POST()
        url("/api/auth/local")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg")
            )
        }
        body(
                "email": $(
                        consumer(email()),
                        producer("test_new@email.com")
                ),
                "username": $(
                        consumer(regex(".{5,50}")),
                        producer("test_username_new")
                ),
                "password": anyNonBlankString()
        )
    }
    response {
        status 201
        headers {
            contentType applicationJson()
        }
        body(
                "id": $(
                        producer(anyNonBlankString()),
                        consumer("test_id_local"))
                ,
                "email": "test_new@email.com",
                "username": "test_username_new",
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"]
        )
    }
}
