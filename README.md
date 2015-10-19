# vendingma
Vending Ma - simple vending maching implementation

Work in progress

TODO (further improvements):

- Create base Activity for common code (set up toolbar, back navigation)
- UI tests!!!
- Pull out some android independent logic from the activities - refactor to use the MVP pattern as in https://github.com/zfoltin/twittererer
- Store stock
- Improve UI
- Eliminate hard-coded string from [VendingMachineImpl.java]( https://github.com/zfoltin/vendingma/blob/master/app/src/main/java/com/zedeff/vendingma/services/VendingMachineImpl.java) - use an enum for errors and map it to strings from resources on the UI

