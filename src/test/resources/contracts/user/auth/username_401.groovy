package contracts.user.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method GET()
        url("/auth/username/test_username_1")
        headers {
            header 'Authorization': absent()
        }
    }
    response {
        status 401
    }
}
