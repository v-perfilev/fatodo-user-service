package contracts.custom.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method POST()
        url("/auth/oauth2")
        headers {
            contentType applicationJson()
            headers {
                header 'Authorization': absent()
            }
        }
        body('''
            {
              "email" : "test_2@email.com",
              "username" : "test_username_2",
              "provider": "GOOGLE",
              "providerId": "test_providerId"
            }
        ''')
    }
    response {
        status 401
    }
}
