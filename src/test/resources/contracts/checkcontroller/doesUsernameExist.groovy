package contracts.checkcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'check if username exists'
    description 'should return status 200 and boolean value'
    request {
        method GET()
        url($(
                consumer(regex("/api/check/username/.+")),
                producer("/api/check/username/test_username_not_exists")
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
