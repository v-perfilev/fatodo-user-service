package contracts.usercontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get all users'
    description 'should return status 200 and list of UserDTOs'
    request {
        method GET()
        url("/api/user")
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
        body([
                [
                        "id"         : anyUuid(),
                        "email"      : "current-name@email.com",
                        "username"   : "current-name",
                        "provider"   : "LOCAL",
                        "providerId" : null,
                        "authorities": ["ROLE_USER"],
                        "info"       : [
                                "firstname"    : "test_value",
                                "lastname"     : "test_value",
                                "imageFilename": "test_value",
                        ],
                        "settings"   : [
                                "language": "EN",
                        ]
                ],
                [
                        "id"         : anyUuid(),
                        "email"      : "local-name@email.com",
                        "username"   : "local-name",
                        "provider"   : "GOOGLE",
                        "providerId" : null,
                        "authorities": ["ROLE_USER"],
                        "info"       : [
                                "firstname"    : "test_value",
                                "lastname"     : "test_value",
                                "imageFilename": "test_value",
                        ],
                        "settings"   : [
                                "language": "EN",
                        ]
                ],
                [
                        "id"         : anyUuid(),
                        "email"      : "google-name@email.com",
                        "username"   : "google-name",
                        "provider"   : "GOOGLE",
                        "providerId" : null,
                        "authorities": ["ROLE_USER"],
                        "info"       : [
                                "firstname"    : "test_value",
                                "lastname"     : "test_value",
                                "imageFilename": "test_value",
                        ],
                        "settings"   : [
                                "language": "EN",
                        ]
                ]
        ])
    }
}
