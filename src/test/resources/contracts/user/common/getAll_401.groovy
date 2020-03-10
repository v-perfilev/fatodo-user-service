package contracts.user.common

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method GET()
        url("/users")
        headers {
            header 'Authorization': absent()
        }
    }
    response {
        status 401
    }
}
