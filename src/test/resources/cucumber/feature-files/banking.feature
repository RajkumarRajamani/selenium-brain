#
#
#TC01: User should be able to create a new back account with valid info for mandatory fields
#Test Data:
#Name
#Address
#Age
#Sex
#PAN
#Contact Email:
#Phone:
#
#Steps:
#1. Launch online banking web portal
#2. Click create new account
#3. Provide all valid details for all mandatory fields
#4. Hit create account
#5. Account must be created successfully
#
#Expected Outcome:
#User should be able to login with the registered user account details at any time
#
#Priority : High [as it is the core scenario of req]
#
#TC02: User should be able to create a new back account with valid info for all available fields
#Test Data:
#Name
#Address
#Age
#Sex
#PAN
#Contact Email:
#Phone:
#
#Steps:
#1. Launch online banking web portal
#2. Click create new account
#3. Provide all valid details for all fields
#4. Hit create account
#5. Account must be created successfully
#
#Expected Outcome:
#User should be able to login with the registered user account details at any time
#
#Priority : High [as it is the core scenario of req]
#
#TC03: User should not be able to create a new back account while missing any mandatory info
#Test Data:
#Name
#Address
#Age
#Sex
#PAN
#Contact Email:
#Phone:
#
#Steps:
#1. Launch online banking web portal
#2. Click create new account
#3. Provide all valid details for all mandatory fields except any one mandatory field [Ex., Phone]
#4. Hit create account
#5. Account should not be created, instead an error would be popped up on UI with validation message
#
#Expected Outcome:
#Account should not be created in banking system
#
#Priority : High [as it is the core scenario of req]
#
#TC04: User should be able to transfer amount of Rs. 50000 [Assuming max limit as 50000]
#Test Data:
#Amount to transfer
#Beneficiary Account to transfer Money
#
#
#Steps:
#1. login into banking portal
#2. Go to Transfer Page
#3. Select beneficiary and enter amount to be transferred [equal to max limit]
#4. select acknowledgement checkbox and submit
#5. Enter OTP and Submit
#6. Verify if amount has been debited from logged in account
#7. Verify if amount has been received at recipient account
#
#Expected Outcome:
#As user has provided max allowed limit, money should be transferred to recipient
#
#Priority : High [as it is the core scenario of req]
#
#TC05: User should be able to transfer minimum amount of Rs. 100 [Assuming min transfer limit as 100]
#
#as same as TC04
#
#TC06: User should not be able to transfer amount of Rs. 50001 [Assuming max limit as 50000] as it is more than allowed limit
#Test Data:
#Amount to transfer
#Beneficiary Account to transfer Money
#
#
#Steps:
#1. login into banking portal
#2. Go to Transfer Page
#3. Select beneficiary and enter amount to be transferred [more than max limit]
#4. select acknowledgement checkbox and submit
#5. Transfer should not be allowed as it exceeds the limit
#6. Amount should not be debited from logged in account
#
#Expected Outcome:
#As user has provided more than allowed limit, transfer should not be allowed
#
#Priority : High [as it is the core scenario of req]
#
#TC07: User should not be able to transfer amount any negative amount
#
#  as same as TC06
#
#TC06: user should be able to view the transaction history for max up to past 3 months
#  Test Data: Start Date, End Date, Report Type [PDF, Excel or Browser]
#TC07: user should not be able to view the transaction history beyond 3 months in past
#  Test Data: Start Date, End Date, Report Type [PDF, Excel or Browser]
#TC07: user should not be able to select range in future as it never existed
#  Test Data: Start Date, End Date, Report Type [PDF, Excel or Browser]
#
