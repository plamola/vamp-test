# vamp-test
Integration tests for vamp components

## Prerequisites

To run the integration tests, you'll need the following:

- A fully configured & running VAMP environment, including Core, Pulse, Router & CLI
- SBT 0.13.9
- Java 8

## How to use

Configure the test, by pointing it at your VAMP install.
You'll need to edit `src/test/resources/application.conf` and setup the correct endpoints & version.

Once that has been done, simply run:

`sbt test`

To run individual test, use the `test-only` command.

For example:

`sbt test-only io.vamp.test.scenario.sava.implementation.SavaTutorialRestSuite`

or 

`sbt test-only io.vamp.test.cli.CliGenericSuite`