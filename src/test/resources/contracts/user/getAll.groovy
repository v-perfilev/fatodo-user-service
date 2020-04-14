package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get all users'
    description 'should return status 200 and list of UserDTOs'
    request {
        method GET()
        url("/api/users")
        headers {
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ0ZXN0X2FkbWluIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.RwSPieOfY1iwF5Tz8ZMw8tiWVZc-nGx4JGgVh08wzV_HrNYZelT9Auo2mcKp6L1PTIBc8cRRlcsvR7YjbiI9qA")
            )
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body([
                [
                        "id"         : "test_id_local",
                        "email"      : "test_local@email.com",
                        "username"   : "test_username_local",
                        "provider"   : "LOCAL",
                        "providerId" : null,
                        "authorities": ["ROLE_USER"]
                ],
                [
                        "id"         : "test_id_google",
                        "email"      : "test_google@email.com",
                        "username"   : "test_google@email.com",
                        "provider"   : "GOOGLE",
                        "providerId" : null,
                        "authorities": ["ROLE_USER"]
                ]
        ])
    }
}
