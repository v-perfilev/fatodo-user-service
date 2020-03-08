package contracts.user.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method POST()
        url("/auth/oauth2")
        headers {
            header 'Content-Type': 'application/json'
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
