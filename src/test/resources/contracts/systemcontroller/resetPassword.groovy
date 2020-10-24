package contracts.system

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'reset password for user'
    description 'should return status 200'
    request {
        method POST()
        url("/api/system/reset-password")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ1c2VybmFtZSI6InRlc3Rfc3lzdGVtIiwiYXV0aG9yaXRpZXMiOiJST0xFX1NZU1RFTSIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.roNFKrM7NjEzXvRFRHlJXw0YxSFZ-4Afqvn7eFatpGF14olhXBvCvR9CkPkmlnlCAOYbpDO18krfi6SEX0tQ6Q")
            )
        }
        body(
                "userId": $(
                        consumer(uuid()),
                        producer("8f9a7cae-73c8-4ad6-b135-5bd109b51d2e")
                ),
                "password": anyNonBlankString(),
        )
    }
    response {
        status 200
    }
}
