# Voice-Assistant Mercedes Benz

Your Task
Design and implement a REST API that will be part of the cloud backend for the
Mercedes Benz voice assistant and that covers both, the user story and the
technical requirements below.

User Story:

As a driver of a Mercedes Benz car, I want to use my car’s voice assistant to create,
query and remove personal reminders.

If I speak “Hey Mercedes, remind me to buy milk today” the reminder “buy milk”
should be added to my reminders list for today. Adding reminders for specific days
in the future as well as removing all reminders, all reminders for a day or a single
reminder should work similarly.

Later, when I ask “Hey Mercedes, what are my reminders for the 22nd of January?”,
I would expect a friendly response that contains a list of my reminders for that day
in the order I created them. A query for reminders without specifying a date and
limiting the number of returned reminders would be nice to have.
Time of day and reoccurring events are not required.

Technical requirements:

Client: The car will not directly communicate with our service but through the
Voice Assistant Platform (VAP). This will be the direct client of our service and
takes care of transcribing the user’s voice input as text (speech to text), as well
as transforming the response text into audible voice output (text to speech).
The VAP Team has left us to decide individually on two design choices regarding
where to put some of the business logic. Either our service OR the VAP should
take care about:

- detecting the user’s intended operation from the transcribed request, e.g.
- create a new reminder “buy milk”
- delete all reminders
- creating the response text for the user, e.g.
- Ok, I have added “buy milk” to your reminders
- Alright, I have deleted all your reminders

However we decide, we can assume that VAP is configured to correctly call our
API according to our specification and to process the response accordingly. It also
provides fallback answers in case our service does not return a valid response.

