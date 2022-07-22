package contracts.usercontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'create user'
    description 'should return status 201 and UserDTO'
    request {
        method POST()
        url("/api/user")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmMjkxYmFkMy1iYzFhLTQwZjYtODhlNC1iODFkNDVmNWY4YzgiLCJ1c2VybmFtZSI6InRlc3RfYWRtaW4iLCJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.ln5MMyCUx1EjdgVWn1MODz6_34lvzHLNNyzWfP2i4LKTNDYaWw_PC-6WxksP2o8UvpeuFjIlZkNhM_wrkYAV3w")
            )
        }
        body(
                "email": $(
                        consumer(email()),
                        producer("test_new@email.com")
                ),
                "username": $(
                        consumer(regex(".{5,50}")),
                        producer("test_username_new")
                ),
                "provider": $(
                        consumer(any()),
                        producer("LOCAL")
                ),
                "providerId": null,
                "authorities": $(
                        consumer(any()),
                        producer(["ROLE_USER"])
                ),
                "activated": $(
                        consumer(anyBoolean()),
                        producer(false)
                ),
                "info": $(
                        consumer(any()),
                        producer([
                                "language": "en"
                        ])
                )
        )
        bodyMatchers {
            jsonPath('$.provider', byType {
                minOccurrence(1)
            })
            jsonPath('$.authorities', byType {
                minOccurrence(1)
            })
        }
    }
    response {
        status 201
        headers {
            contentType applicationJson()
        }
        body(
                "email": "test_new@email.com",
                "username": "test_username_new",
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"],
                "activated": false,
                "info": [
                        "firstname"    : null,
                        "lastname"     : null,
                        "language"     : "en",
                        "imageFilename": null,
                ]
        )
    }
}
