package contracts.auth.custom

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 201 and google UserDTO"
    request {
        method POST()
        url("/auth/oauth2")
        headers {
            contentType applicationJson()
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg'
        }
        body(
                "email": "test_google@email.com",
                "username": "test_google@email.com",
                "provider": "GOOGLE",
                "providerId": "test_id_google"
        )
    }
    response {
        status 201
        headers {
            contentType applicationJson()
        }
        body(
                "email": "test_google@email.com",
                "username": "test_google@email.com",
                "provider": "GOOGLE",
                "providerId": "test_id_google",
                "authorities": ["ROLE_USER"]
        )
    }
}
