package contracts.user.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method POST()
        url("/auth/local")
        headers {
            contentType applicationJson()
        }
        body('''
            {
              "email" : "test_2@email.com",
              "username" : "test_username_2",
              "password" : "test_password"
            }
        ''')
    }
    response {
        status 401
    }
}
