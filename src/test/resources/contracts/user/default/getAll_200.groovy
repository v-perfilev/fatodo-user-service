package contracts.user.custom

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return list of UserDTO"
    request {
        method GET()
        url("/users")
        headers {
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ0ZXN0X2FkbWluIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.RwSPieOfY1iwF5Tz8ZMw8tiWVZc-nGx4JGgVh08wzV_HrNYZelT9Auo2mcKp6L1PTIBc8cRRlcsvR7YjbiI9qA'
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body('''
            [
               {
                  "id":"test_id_local",
                  "email":"test_local@email.com",
                  "username":"test_username_local",
                  "provider":"LOCAL",
                  "providerId":null,
                  "authorities" : [ "ROLE_USER" ]
               },
               {
                  "id":"test_id_get",
                  "email":"test_get@email.com",
                  "username":"test_username_get",
                  "provider":"LOCAL",
                  "providerId":null,
                  "authorities" : [ "ROLE_USER" ]
               },
               {
                  "id":"test_id_update",
                  "email":"test_update@email.com",
                  "username":"test_username_update",
                  "provider":"LOCAL",
                  "providerId":null,
                  "authorities" : [ "ROLE_USER" ]
               },
               {
                  "id":"test_id_delete",
                  "email":"test_delete@email.com",
                  "username":"test_username_delete",
                  "provider":"LOCAL",
                  "providerId":null,
                  "authorities" : [ "ROLE_USER" ]
               }
            ]
        ''')
    }
}
