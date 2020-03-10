package contracts.custom.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 201 and UserDTO"
    request {
        method POST()
        url("/auth/local")
        headers {
            contentType applicationJson()
            header 'Authorization': value(consumer(anyNonBlankString()), producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg"))
        }
        body(
                "email": "test_2@email.com",
                "username": "test_username_2",
                "password": anyNonBlankString()
        )
    }
    response {
        status 201
        headers {
            contentType applicationJson()
        }
        body('''
            {
              "email" : "test_2@email.com",
              "username" : "test_username_2",
              "provider" : "LOCAL",
              "providerId" : null,
              "authorities" : [ "ROLE_USER" ]
            }
        ''')
    }
}
