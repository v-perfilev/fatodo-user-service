package contracts.user.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method GET()
        url("/auth/id/test_id_1")
    }
    response {
        status 401
    }
}
