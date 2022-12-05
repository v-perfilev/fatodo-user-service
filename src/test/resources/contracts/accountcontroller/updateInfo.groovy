package contracts.accountcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'update account info'
    description 'should return status 200'
    request {
        method PUT()
        url("/api/account/info")
        headers {
            contentType multipartFormData()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4ZjlhN2NhZS03M2M4LTRhZDYtYjEzNS01YmQxMDliNTFkMmUiLCJ1c2VybmFtZSI6InRlc3RfdXNlciIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.Go0MIqfjREMHOLeqoX2Ej3DbeSG7ZxlL4UAvcxqNeO-RgrKUCrgEu77Ty1vgR_upxVGDAWZS-JfuSYPHSRtv-w")
            )
        }
        multipart(
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
                "gender": $(
                        consumer(any()),
                        producer("MALE")
                ),
                "imageFilename": $(
                        consumer(any()),
                        producer("test_value")
                ),
        )
    }
    response {
        status 200
    }
}
