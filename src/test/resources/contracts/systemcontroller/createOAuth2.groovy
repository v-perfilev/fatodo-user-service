package contracts.systemcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'create oauth2 user'
    description 'should return status 201 and UserDTO'
    request {
        method POST()
        url("/api/system/oauth2")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ1c2VybmFtZSI6InRlc3Rfc3lzdGVtIiwiYXV0aG9yaXRpZXMiOiJST0xFX1NZU1RFTSIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.roNFKrM7NjEzXvRFRHlJXw0YxSFZ-4Afqvn7eFatpGF14olhXBvCvR9CkPkmlnlCAOYbpDO18krfi6SEX0tQ6Q")
            )
        }
        body(
                "email": $(
                        consumer(email()),
                        producer("test_google@email.com")
                ),
                "username": $(
                        consumer(email()),
                        producer("test_google@email.com")
                ),
                "provider": $(
                        consumer(execute('assertProviders($it)')),
                        producer("GOOGLE")
                ),
                "providerId": $(
                        consumer(anyNonBlankString()),
                        producer("test_provider_google")
                ),
                "language": $(
                        consumer(anyNonBlankString()),
                        producer("EN")
                ),
                "timezone": $(
                        consumer(anyNonBlankString()),
                        producer("Europe/Berlin")
                ),
        )
    }
    response {
        status 201
        headers {
            contentType applicationJson()
        }
        body(
                "email": "test_google@email.com",
                "username": "test_google@email.com",
                "password": null,
                "provider": "GOOGLE",
                "providerId": "test_provider_google",
                "authorities": ["ROLE_USER"],
                "activated": true,
                "info": [
                        "firstname"    : null,
                        "lastname"     : null,
                        "imageFilename": null,
                ],
                "settings": [
                        "language": "EN",
                ]
        )
    }
}
