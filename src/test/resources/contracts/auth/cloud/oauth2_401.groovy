package contracts.auth.custom

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause not authorized"
    request {
        method POST()
        url("/auth/oauth2")
        headers {
            contentType applicationJson()
            headers {
                header 'Authorization': absent()
            }
        }
        body(
                "email": "test_google@email.com",
                "username": "test_google@email.com",
                "provider": "GOOGLE",
                "providerId": "test_id_google"
        )
    }
    response {
        status 401
    }
}
