### Shopping Cart

#### Requirements

- You should be able to add items to the shopping cart ○ This includes multiple items of the same kind
- You should be able to remove items from the shopping cart
- An item represents a product you’d put in a shopping cart, e.g. cornflakes, bread, pasta, etc
- You should be able to produce an itemised receipt for the items in the
shopping cart. Think of the type of receipt you would get in the supermarket.
- You should be able to apply an offer to the shopping cart
- The offer you should be able to apply is a 2-for-1 offer (i.e. If you have two boxes of cornflakes in your basket and
the 2-for1 offer applies to cornflakes you should get one of the boxes for free)
- The offer also applies for any multiples of 2 (i.e. if there are four items of the applicable item in the cart then
they should get two of those for free and so on)
- You don’t have to worry about implementing an interactive UI, API endpoints, logging or anything like that. Domain
functions and running/passing tests is all that is required.

#### Decisions

- I am using a functional approach to not mutate the state of the cart
- I am also using Arrow library to implement a functional error handling approach so that expected domain errors are
  forced to be handled explicitly