package contracts.infocontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get user summary by username or email'
    description 'should return status 200 and UserInfoDTO'
    request {
        method GET()
        url($(
                consumer(regex("/api/info/.+/username-or-email")),
                producer("/api/info/current-name/username-or-email")
        ))
        headers {
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ1c2VybmFtZSI6InRlc3Rfc3lzdGVtIiwiYXV0aG9yaXRpZXMiOiJST0xFX1NZU1RFTSIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.roNFKrM7NjEzXvRFRHlJXw0YxSFZ-4Afqvn7eFatpGF14olhXBvCvR9CkPkmlnlCAOYbpDO18krfi6SEX0tQ6Q")
            )
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "id": "8f9a7cae-73c8-4ad6-b135-5bd109b51d2e",
                "username": "current-name",
                "firstname": "test_value",
                "lastname": "test_value",
                "gender": "FEMALE",
                "imageFilename": "test_value"
        )
    }
}
