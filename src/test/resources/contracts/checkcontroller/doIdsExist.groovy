package contracts.checkcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'check if ids exist'
    description 'should return status 200 and boolean value'
    request {
        method GET()
        url($(
                consumer(regex("/api/check/id\\?ids=.*")),
                producer("/api/check/id?ids=" + uuid().generate())
        ))
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body("false")
    }
}
