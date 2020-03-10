package contracts.user.custom

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return UserDTO"
    request {
        method GET()
        url("/users/test_id_get")
        headers {
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ0ZXN0X2FkbWluIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.RwSPieOfY1iwF5Tz8ZMw8tiWVZc-nGx4JGgVh08wzV_HrNYZelT9Auo2mcKp6L1PTIBc8cRRlcsvR7YjbiI9qA'
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "id": "test_id_get",
                "email": "test_get@email.com",
                "username": "test_username_get",
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"]
        )
    }
}
