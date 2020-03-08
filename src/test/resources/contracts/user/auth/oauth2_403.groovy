package contracts.user.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 403 cause wrong authority"
    request {
        method POST()
        url("/auth/local")
        headers {
            header 'Content-Type': 'application/json'
            header 'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwidXNlcm5hbWUiOiJ0ZXN0X3VzZXIiLCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.ggV38p_Fnqo2OZNtwR3NWKZhMXPd-vf4PrRxN0NmTWsHPrKwWZJSGO2dJBBPWXWs4OI6tjsNV2TM3Kf6NK92hw'
        }
        body('''
            {
              "email" : "test_2@email.com",
              "username" : "test_username_2",
              "provider": "GOOGLE",
              "providerId": "test_providerId"
            }
        ''')
    }
    response {
        status 403
    }
}
