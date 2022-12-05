package contracts.accountcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'update account settings'
    description 'should return status 200'
    request {
        method PUT()
        url("/api/account/settings")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4ZjlhN2NhZS03M2M4LTRhZDYtYjEzNS01YmQxMDliNTFkMmUiLCJ1c2VybmFtZSI6InRlc3RfdXNlciIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.Go0MIqfjREMHOLeqoX2Ej3DbeSG7ZxlL4UAvcxqNeO-RgrKUCrgEu77Ty1vgR_upxVGDAWZS-JfuSYPHSRtv-w")
            )
        }
        body(
                "language": $(
                        consumer(any()),
                        producer("RU")
                ),
                "timezone": $(
                        consumer(any()),
                        producer("Europe/Berlin")
                ),
                "timeFormat": $(
                        consumer(any()),
                        producer("H12")
                ),
                "dateFormat": $(
                        consumer(any()),
                        producer("MDY_SLASH")
                ),
                "emailReminders": $(
                        consumer(any()),
                        producer(true)
                ),
        )
    }
    response {
        status 200
    }
}
