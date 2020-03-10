package contracts.user.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 400 cause nonunique"
    request {
        method PUT()
        url("/users")
        headers {
            contentType applicationJson()
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ0ZXN0X2FkbWluIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.RwSPieOfY1iwF5Tz8ZMw8tiWVZc-nGx4JGgVh08wzV_HrNYZelT9Auo2mcKp6L1PTIBc8cRRlcsvR7YjbiI9qA'
        }
        body('''
            {
              "id":"test_id_1",
              "email":"test_2@email.com",
              "username":"test_username_1",
              "provider":"LOCAL",
              "providerId":null,
              "authorities" : [ "ROLE_USER" ]
            }
        ''')
    }
    response {
        status 400
    }
}
