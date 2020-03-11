package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'update user'
    description 'should return status 200 and UserDTO'
    request {
        method PUT()
        url("/users")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(execute('assertAdminJwt($it)')),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ0ZXN0X2FkbWluIiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.RwSPieOfY1iwF5Tz8ZMw8tiWVZc-nGx4JGgVh08wzV_HrNYZelT9Auo2mcKp6L1PTIBc8cRRlcsvR7YjbiI9qA")
            )
        }
        body(
                "id": $(
                        consumer(regex('[\\w-]+')),
                        producer("test_id_local")
                ),
                "email": $(
                        consumer(email()),
                        producer("test_updated@email.com")
                ),
                "username": $(
                        consumer(regex(".{5,50}")),
                        producer("test_username_updated")
                ),
                "provider": $(
                        consumer(execute('assertProviders($it)')),
                        producer("LOCAL")
                ),
                "providerId": null,
                "authorities": $(
                        consumer(execute('assertAuthorities($it)')),
                        producer(["ROLE_USER"])
                )
        )
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "id": "test_id_local",
                "email": "test_updated@email.com",
                "username": "test_username_updated",
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"]
        )
    }
}
