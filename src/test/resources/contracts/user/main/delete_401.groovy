package contracts.user.main

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method DELETE()
        url("/users/test_id_delete")
        headers {
            header 'Authorization': absent()
        }
    }
    response {
        status 401
    }
}
