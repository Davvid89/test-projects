# Scope of the task



**For this project, I have included the process from adding a product to the basket through to payment as one of the most important functionalities,
and only this process will be covered by the requirements below. We will skip the rest and not create tests for other parts of the shop.**

**Below is a list of actions we want to verify with regression tests:**


### TestCarts
- User has the option to add the selected tour to the shopping cart from that tour's page,
- User has the option to add the selected tour to the basket from the category page,
- User has the option to add at least 10 tours to the basket (in total and in any combination),
- User has the option to select the number of tours they wish to purchase from the product page (e.g. for an order for several people),
- User has the option to add 10 different tours to the shopping basket,
- User has the option to change the quantity of the selected tour (single position) on the shopping cart page,
- User has the option of deleting a tour on the shopping cart page (entire position),

### PaymentTest
- User is informed of errors in the form on the payment page through appropriate messages,
- User has the possibility to log in to the payment page and make a payment as a logged user,
- User has the option to create an account on the payment page and make a payment at the same time,
- User has the possibility to make a purchase without creating an account,
- User who has an account can see his orders in his account,
- User, after making an order, can see a summary which includes the order number, the correct date, the amount, the payment method, the name and the 
  quantity of the purchased products.

## Assumptions and exclusions

### **Exclusions:**

- For the purpose of this exercise, we omit testing email notifications.
- Do not test all combinations of invalid postcodes. Just check that if this field is blank, the appropriate message is displayed when you attempt 
  to make a purchase.
- We skip testing discount coupons.
- We do not test the account creation process. Just confirm that you can order the product with the simultaneous creation of an account.
- We do not test stock levels (i.e. the number of products available).
- We do not test optional fields on the payment page.

### **Assumptions**

- We assume that the current operation of the site is correct, and in particular that the messages displayed are fine (unless something is 
untranslated).
- After each test where you create a new account, delete the created user (the "Delete Account" link is available from the cockpit in "My Account" 
  after logging in).
- Tests should run locally.