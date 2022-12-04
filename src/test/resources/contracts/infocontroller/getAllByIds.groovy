package contracts.infocontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get user info by ids'
    description 'should return status 200 and list of UserInfoDTOs'
    request {
        method GET()
        url($(
                consumer(regex("/api/info\\?ids=.*")),
                producer("/api/info?ids=8f9a7cae-73c8-4ad6-b135-5bd109b51d2e")
        ))
        headers {
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4ZjlhN2NhZS03M2M4LTRhZDYtYjEzNS01YmQxMDliNTFkMmUiLCJ1c2VybmFtZSI6InRlc3RfdXNlciIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.Go0MIqfjREMHOLeqoX2Ej3DbeSG7ZxlL4UAvcxqNeO-RgrKUCrgEu77Ty1vgR_upxVGDAWZS-JfuSYPHSRtv-w")
            )
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body([
                [
                        "id"      : "8f9a7cae-73c8-4ad6-b135-5bd109b51d2e",
                        "email"   : "current-name@email.com",
                        "username": "current-name",
                        "info"    : [
                                "firstname": "test_value",
                                "lastname" : "test_value",
                                "language" : "EN",
                        ]
                ]
        ])
    }
}
