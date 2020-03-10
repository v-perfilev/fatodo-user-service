package contracts.user.main

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 415 cause wrong Content-Type"
    request {
        method POST()
        url("/users")
        headers {
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ0ZXN0X2FkbWluIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.RwSPieOfY1iwF5Tz8ZMw8tiWVZc-nGx4JGgVh08wzV_HrNYZelT9Auo2mcKp6L1PTIBc8cRRlcsvR7YjbiI9qA'
        }
        body(
                "email": "test_create@email.com",
                "username": "test_username_create",
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"]
        )
    }
    response {
        status 415
    }
}
