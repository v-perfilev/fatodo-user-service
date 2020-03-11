package contracts.check

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'check if username exists'
    description 'should return status 200 and boolean value'
    request {
        method GET()
        url($(
                consumer(regex('\\/check\\/username\\/[\\w-]+\\/unique')),
                producer("/check/username/test_username_not_exists/unique")
        ))
    }
    response {
        status 200
        body("true")
    }
}
