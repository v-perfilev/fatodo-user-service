package contracts.user.common

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method PUT()
        url("/users")
        headers {
            contentType applicationJson()
            header 'Authorization': absent()
        }
        body(
                "id": "test_id_update",
                "email": "test_update@email.com",
                "username": "test_username_update",
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"]
        )
    }
    response {
        status 401
    }
}
