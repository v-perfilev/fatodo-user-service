package contracts.user.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 400 cause invalidity"
    request {
        method POST()
        url("/auth/local")
        headers {
            header 'Content-Type': 'application/json'
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg'
        }
        body('''
            {
              "email" : "",
              "username" : "",
              "password" : ""
            }
        ''')
    }
    response {
        status 400
    }
}
