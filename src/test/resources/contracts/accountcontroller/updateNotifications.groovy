package contracts.accountcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'update account notifications'
    description 'should return status 200'
    request {
        method PUT()
        url("/api/account/notifications")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4ZjlhN2NhZS03M2M4LTRhZDYtYjEzNS01YmQxMDliNTFkMmUiLCJ1c2VybmFtZSI6InRlc3RfdXNlciIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSIiwiaWF0IjowLCJleHAiOjMyNTAzNjc2NDAwfQ.Go0MIqfjREMHOLeqoX2Ej3DbeSG7ZxlL4UAvcxqNeO-RgrKUCrgEu77Ty1vgR_upxVGDAWZS-JfuSYPHSRtv-w")
            )
        }
        body(
                "pushNotifications": $(
                        consumer(any()),
                        producer(["CHAT_MESSAGE_CREATE"])
                ),
                "emailNotifications": $(
                        consumer(any()),
                        producer(["REMINDER"])
                ),
        )
    }
    response {
        status 200
    }
}
