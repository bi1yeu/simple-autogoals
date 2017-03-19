# simple-autogoals

This program expands the flexibility of [Simple](https://www.simple.com/)'s [Goals](https://www.simple.com/features/goals) feature.

When a new goal is created in Simple, there are two saving options: (a) Save until a date and (b) Save now.

Saving until a date is useful for long-term goals that are slowly funded. Saving now is useful for funding a goal with a set amount from the current total balance.

`simple-autogoals` offers support for another, slightly different use case: Save a fixed amount automatically whenever the program runs.

Ideally this program would run on a cron schedule that matches a users's paycheck schedule. E.g. if a user gets a paycheck direct-deposited twice a month, this program could be run bi-weekly, automatically transferring fixed amounts from Safe-to-Spend® to each of the specified goals. If her rent is $500, she would set her rent goal's memo to `auto_transfer=250` to save that much from each paycheck.

Such a saving scheme would be well-suited for monthly bills whose amounts don't change from month to month.

A naive use of the program would be to simply run it manually whenever one wishes to update one's auto goals. In the future I may add support for  detecting paycheck/income transactions and only updating the auto goals when that happens.

## Usage

### Creating Simple auto goals

Within Simple, create a new goal. It can have any initial funding amount. Click 'Save now' button. Then edit the goal's details, setting the memo field to contain the automatic transfer amount.

e.g.

```
auto_transfer=5.50
```

Now, each time the program runs, that amount will be transferred from Simple's Safe-to-Spend® balance to the goal.

### Running `simple-autogoals`

Create an `.env` file containing your Simple username and password:

```
export SIMPLE_USERNAME=nedflanders
export SIMPLE_PASSWORD="maude7"
```

Then run `./run.sh`.

For each goal with the `auto_transfer=<amt>` memo set, the specified amount will be transferred.

![pristine-goals](http://i.imgur.com/PXhj8be.png)

```bash
$ ./run.sh
Signing-in nedflanders
Getting goals
2 goals to update
Transferring $4.44 to "Bike"
Transferring $1.00 to "Fancy dinner"
Done
```

![updated-goals](http://i.imgur.com/dvlipko.png)

## Disclaimer

Neither I nor this project is any way associated with Simple Finance Technology Corp. Use this software at your own risk.


