package contracts.accountcontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'update account'
    description 'should return status 200 and UserDTO'
    request {
        method POST()
        url("/api/account/update")
        headers {
            contentType multipartFormData()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwidXNlcm5hbWUiOiJ0ZXN0X3VzZXIiLCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.ggV38p_Fnqo2OZNtwR3NWKZhMXPd-vf4PrRxN0NmTWsHPrKwWZJSGO2dJBBPWXWs4OI6tjsNV2TM3Kf6NK92hw")
            )
        }
        multipart(
                "id": $(
                        consumer(any()),
                        producer("3")
                ),
                "username": $(
                        consumer(any()),
                        producer("test_username_new")
                ),
                "language": $(
                        consumer(any()),
                        producer("test_language_new")
                ),
        )
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body(
                "id": "3",
                "username": "test_username_new",
                "language": "test_language_new",
        )
    }
}
