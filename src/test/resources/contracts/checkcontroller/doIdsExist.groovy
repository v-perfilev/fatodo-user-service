package contracts.checkcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'check if ids exist'
    description 'should return status 200 and boolean value'
    request {
        method POST()
        url("/api/check/id")
        headers {
            contentType applicationJson()
        }
        body($(
                consumer(regex(".+")),
                producer([
                        uuid().generate()
                ])
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
