package contracts.custom.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method GET()
        url("/auth/email/test_1@email.com")
        headers {
            header 'Authorization': absent()
        }
    }
    response {
        status 401
    }
}
