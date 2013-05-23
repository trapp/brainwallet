Brain Wallet Calculator
========================

This offline java application enables you to create an Armory offline wallet from a custom secret you supply.

Combine the power of Armorys deterministic address generation with a brain wallet.

Features
========================

Supply a custom secret and generate from it:

- A valid Armory paper backup code.

or

- A single bitcoin address and private key.

Using the secret to generate a Armory wallet should always be preferred to generating a single bitcoin address. When spending funds from a bitcoin address most clients create a new "change" address which is not covered by your secret.

Using Armory is way superior as its deterministic address generation makes sure that every new address can be recovered by your secret.

Attention: Use a strong secret with dozens of characters! Otherwise theft is very likely.
