package contracts.user.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method POST()
        url("/users")
        headers {
            contentType applicationJson()
            header 'Authorization': absent()
        }
        body('''
            {
              "email":"test_3@email.com",
              "username":"test_username_3",
              "provider":"LOCAL",
              "providerId":null,
              "authorities" : [ "ROLE_USER" ]
            }
        ''')
    }
    response {
        status 401
    }
}
