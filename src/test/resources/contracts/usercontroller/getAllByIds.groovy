package contracts.usercontroller

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'get user summaries by ids'
    description 'should return status 200 and list of UserSummaryDTOs'
    request {
        method POST()
        url("/api/user/get-all-by-ids")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwidXNlcm5hbWUiOiJ0ZXN0X3VzZXIiLCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.ggV38p_Fnqo2OZNtwR3NWKZhMXPd-vf4PrRxN0NmTWsHPrKwWZJSGO2dJBBPWXWs4OI6tjsNV2TM3Kf6NK92hw")
            )
        }
        body($(
                consumer(regex(".+")),
                producer(["3"])
        ))
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body([
                "id"         : "3",
                "username"   : "test_username_current",
                "imageFilename"   : "test_image_filename",
        ])
    }
}
