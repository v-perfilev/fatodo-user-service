package contracts.accountcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get user principal by id'
    description 'should return status 200 and UserPrincipalDTO'
    request {
        method GET()
        url("/api/account/current")
        headers {
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwidXNlcm5hbWUiOiJ0ZXN0X3VzZXIiLCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.ggV38p_Fnqo2OZNtwR3NWKZhMXPd-vf4PrRxN0NmTWsHPrKwWZJSGO2dJBBPWXWs4OI6tjsNV2TM3Kf6NK92hw")
            )
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "id": "3",
                "email": "test_current@email.com",
                "username": "test_username_current",
                "authorities": ["ROLE_USER"],
                "imageFilename": "test_image_filename",
                "language": "en",
                "provider": "LOCAL",
                "providerId": null,
        )
    }
}
