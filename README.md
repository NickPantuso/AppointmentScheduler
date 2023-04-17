# AppointmentScheduler
The user can log in by entering a user name and password from a database. Once logged in, there are two tables on the home screen.
The top table contains all customer records, the bottom contains all scheduled appointments. You can add, update, or delete any record/appointments
via the buttons below each table. You can select records to update or delete by simply clicking on them in the table. If you want to add a record to
either table, a form will open with completely empty fields that you will fill out and submit. If you want to update a record, a form will open
with all of its fields filled out with the record you selected to update, all fields are editable. You can cancel an addition or modification
via the cancel button, and submit via the submit button. If there are any issues with your inputs the program will notify you.
All appointment dates shown are in your local time and all dates are recorded to the database in UTC. Your local time is compared to the business's
EST time to ensure the appointments you schedule don't go outside of business hours (8AM-10PM). The report button on the home form will
bring you to a new form where you can view the schedules for each contact, as well as some other general information
about the appointments and customers.
(Every log in attempt is recorded to a text file.)

# What was learned?

* How to use a database with Java
* Java localization and date/time API
* Introduction to lambda expressions
* Using a properties file to change the language on a form.
