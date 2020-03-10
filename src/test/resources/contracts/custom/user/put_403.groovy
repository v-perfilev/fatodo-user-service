package contracts.custom.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 403 cause wrong authority"
    request {
        method PUT()
        url("/users")
        headers {
            contentType applicationJson()
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg'
        }
        body('''
            {
              "id":"test_id_1",
              "email":"test_1@email.com",
              "username":"test_username_3",
              "provider":"LOCAL",
              "providerId":null,
              "authorities" : [ "ROLE_USER" ]
            }
        ''')
    }
    response {
        status 403
    }
}
