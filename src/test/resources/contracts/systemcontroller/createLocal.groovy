package contracts.systemcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'create local user'
    description 'should return status 201 and UserDTO'
    request {
        method POST()
        url("/api/system/local")
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
                        producer("test_new@email.com")
                ),
                "username": $(
                        consumer(regex(".{5,50}")),
                        producer("test_username_new")
                ),
                "password": anyNonBlankString(),
                "language": $(
                        consumer(anyNonBlankString()),
                        producer("en")
                ),
        )
    }
    response {
        status 201
        headers {
            contentType applicationJson()
        }
        body(
                "id": uuid().generate(),
                "email": "test_new@email.com",
                "username": "test_username_new",
                "password": anyNonBlankString(),
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"],
                "language": "en",
                "activated": false
        )
    }
}
