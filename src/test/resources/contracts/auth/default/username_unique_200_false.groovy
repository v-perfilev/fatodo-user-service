package contracts.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 200 with false"
    request {
        method GET()
        url("/auth/username/test_username_local/unique")
        headers {
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg'
        }
    }
    response {
        status 200
        body("false")
    }
}
