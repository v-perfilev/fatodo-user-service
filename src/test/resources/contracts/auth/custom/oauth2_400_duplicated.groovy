package contracts.auth.custom

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 400 cause duplicated key"
    request {
        method POST()
        url("/auth/oauth2")
        headers {
            contentType applicationJson()
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg'
        }
        body(
                "email": "test_duplicated@email.com",
                "username": "test_duplicated@email.com",
                "provider": "GOOGLE",
                "providerId": "test_id_duplicated"
        )
    }
    response {
        status 400
    }
}
