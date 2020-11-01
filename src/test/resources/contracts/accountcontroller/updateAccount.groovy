package contracts.accountcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'update account'
    description 'should return status 200 and UserDTO'
    request {
        method POST()
        url("/api/account/update")
        headers {
            contentType multipartFormData()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4ZjlhN2NhZS03M2M4LTRhZDYtYjEzNS01YmQxMDliNTFkMmUiLCJ1c2VybmFtZSI6InRlc3RfdXNlciIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.Go0MIqfjREMHOLeqoX2Ej3DbeSG7ZxlL4UAvcxqNeO-RgrKUCrgEu77Ty1vgR_upxVGDAWZS-JfuSYPHSRtv-w")
            )
        }
        multipart(
                "id": $(
                        consumer(any()),
                        producer("8f9a7cae-73c8-4ad6-b135-5bd109b51d2e")
                ),
                "username": $(
                        consumer(any()),
                        producer("test_username_new")
                ),
                "firstname": $(
                        consumer(any()),
                        producer("test_firstname_new")
                ),
                "lastname": $(
                        consumer(any()),
                        producer("test_lastname_new")
                ),
                "language": $(
                        consumer(any()),
                        producer("test_language_new")
                ),
                "imageFilename": $(
                        consumer(any()),
                        producer("test_value")
                ),
        )
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "id": "8f9a7cae-73c8-4ad6-b135-5bd109b51d2e",
                "email": "current-name@email.com",
                "username": "test_username_new",
                "authorities": ["ROLE_USER"],
                "provider": "LOCAL",
                "providerId": null,
                "info": [
                        "firstname"    : "test_firstname_new",
                        "lastname"     : "test_lastname_new",
                        "language"     : "test_language_new",
                        "imageFilename": "test_value",
                ]
        )
    }
}
