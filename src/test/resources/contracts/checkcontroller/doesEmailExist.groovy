package contracts.checkcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'check if email exists'
    description 'should return status 200 and boolean value'
    request {
        method GET()
        url($(
                consumer(regex("/api/check/email/.+")),
                producer("/api/check/email/test_not_exists@email.com")
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
