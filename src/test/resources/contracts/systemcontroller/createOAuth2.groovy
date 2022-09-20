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
                        producer("test_facebook@email.com")
                ),
                "username": $(
                        consumer(email()),
                        producer("test_facebook@email.com")
                ),
                "provider": $(
                        consumer(execute('assertProviders($it)')),
                        producer("FACEBOOK")
                ),
                "providerId": $(
                        consumer(anyNonBlankString()),
                        producer("test_provider_facebook")
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
                "email": "test_facebook@email.com",
                "username": "test_facebook@email.com",
                "password": null,
                "provider": "FACEBOOK",
                "providerId": "test_provider_facebook",
                "authorities": ["ROLE_USER"],
                "activated": true,
                "info": [
                        "firstname"    : null,
                        "lastname"     : null,
                        "language"     : "EN",
                        "imageFilename": null,
                ]
        )
    }
}
