package contracts.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'create oauth2 user'
    description 'should return status 201 and UserDTO'
    request {
        method POST()
        url("/auth/oauth2")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(execute('assertSystemJwt($it)')),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg")
            )
        }
        body(
                "email": $(
                        consumer(email()),
                        producer("test_facebook@email.com")
                ),
                "username": $(
                        consumer(email()),
                        producer("test_facebook@email.com")
                ),
                "provider": $(
                        consumer(execute('assertProviders($it)')),
                        producer("FACEBOOK")
                ),
                "providerId": $(
                        consumer(anyNonBlankString()),
                        producer("test_provider_facebook")
                )
        )
    }
    response {
        status 201
        headers {
            contentType applicationJson()
        }
        body(
                "email": "test_facebook@email.com",
                "username": "test_facebook@email.com",
                "provider": "FACEBOOK",
                "providerId": "test_provider_facebook",
                "authorities": ["ROLE_USER"]
        )
    }
}
