package contracts.usercontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'update user'
    description 'should return status 200 and UserDTO'
    request {
        method PUT()
        url("/api/user")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmMjkxYmFkMy1iYzFhLTQwZjYtODhlNC1iODFkNDVmNWY4YzgiLCJ1c2VybmFtZSI6InRlc3RfYWRtaW4iLCJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.ln5MMyCUx1EjdgVWn1MODz6_34lvzHLNNyzWfP2i4LKTNDYaWw_PC-6WxksP2o8UvpeuFjIlZkNhM_wrkYAV3w")
            )
        }
        body(
                "id": $(
                        consumer(uuid()),
                        producer("8f9a7cae-73c8-4ad6-b135-5bd109b51d2e")
                ),
                "email": $(
                        consumer(email()),
                        producer("test_updated@email.com")
                ),
                "username": $(
                        consumer(regex(".{5,50}")),
                        producer("test_username_updated")
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
                                "firstname"    : "test_firstname",
                                "lastname"     : "test_lastname",
                                "gender"       : "MALE",
                                "imageFilename": "test_image",
                        ])
                ),
                "settings": $(
                        consumer(any()),
                        producer([
                                "language": "EN"
                        ])
                ),
                "notifications": $(
                        consumer(any()),
                        producer([
                                "pushNotifications" : [],
                                "emailNotifications": []
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
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "id": "8f9a7cae-73c8-4ad6-b135-5bd109b51d2e",
                "email": "test_updated@email.com",
                "username": "test_username_updated",
                "provider": "LOCAL",
                "providerId": null,
                "authorities": ["ROLE_USER"],
                "activated": true,
                "info": [
                        "firstname"    : "test_firstname",
                        "lastname"     : "test_lastname",
                        "imageFilename": "test_image",
                ],
                "settings": [
                        "language": "EN",
                ]
        )
    }
}
