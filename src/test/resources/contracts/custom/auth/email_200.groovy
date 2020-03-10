package contracts.custom.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return UserPrincipalDTO"
    request {
        method GET()
        url("/auth/email/test_1@email.com")
        headers {
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg'
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body('''
            {
              "id" : "test_id_1",
              "email" : "test_1@email.com",
              "username" : "test_username_1",
              "password" : "$2a$10$GZrq9GxkRWW1Pv7fKJHGAe4ebib6113zhlU4nZlCtH/ylebR9rkn6",
              "provider" : "LOCAL",
              "providerId" : null,
              "authorities" : [ "ROLE_USER" ]
            }
        ''')
    }
}
