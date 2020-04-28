package contracts.userresource

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'update user'
    description 'should return status 200 and UserDTO'
    request {
        method PUT()
        url("/api/users")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
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
                        consumer(any()),
                        producer("LOCAL")
                ),
                "providerId": null,
                "authorities": $(
                        consumer(any()),
                        producer(["ROLE_USER"])
                )
        )
        bodyMatchers {
            jsonPath('$.provider', byType {
                minOccurrence(1)
            })
            jsonPath('$.authorities', byType {
                minOccurrence(1)
            })
        }
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
