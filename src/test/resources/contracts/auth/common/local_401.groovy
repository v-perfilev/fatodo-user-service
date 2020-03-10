package contracts.auth.common

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method POST()
        url("/auth/local")
        headers {
            contentType applicationJson()
            header 'Authorization': absent()
        }
    }
    response {
        status 401
    }
}
