package contracts.userresource

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get user'
    description 'should return status 200 and UserDTO'
    request {
        method GET()
        url($(
                consumer(regex("/api/users/" + uuid().toString())),
                producer("/api/users/8f9a7cae-73c8-4ad6-b135-5bd109b51d2e")
        ))
        headers {
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmMjkxYmFkMy1iYzFhLTQwZjYtODhlNC1iODFkNDVmNWY4YzgiLCJ1c2VybmFtZSI6InRlc3RfYWRtaW4iLCJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.ln5MMyCUx1EjdgVWn1MODz6_34lvzHLNNyzWfP2i4LKTNDYaWw_PC-6WxksP2o8UvpeuFjIlZkNhM_wrkYAV3w")
            )
        }
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "id": "8f9a7cae-73c8-4ad6-b135-5bd109b51d2e",
                "email": "current-name@email.com",
                "username": "current-name",
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"]
        )
    }
}
