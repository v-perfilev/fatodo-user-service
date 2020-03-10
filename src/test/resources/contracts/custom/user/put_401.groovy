package contracts.custom.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method PUT()
        url("/users")
        headers {
            contentType applicationJson()
            header 'Authorization': absent()
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
        status 401
    }
}
