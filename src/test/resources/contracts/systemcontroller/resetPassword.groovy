package contracts.system

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'reset password for user'
    description 'should return status 200'
    request {
        method POST()
        url("/api/system/reset-password")
        headers {
            contentType applicationJson()
            header 'Authorization': $(
                    consumer(containing("Bearer")),
                    producer("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg")
            )
        }
        body(
                "userId": $(
                        consumer(anyNonBlankString()),
                        producer("3")
                ),
                "password": anyNonBlankString(),
        )
    }
    response {
        status 200
    }
}
