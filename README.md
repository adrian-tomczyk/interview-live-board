# Decisions
- At first I created a solution that was a bit complicated. What i wanted to achieve is solution which will be really easy to maintain, extend later on and provide potentially faster sorting algorithm for this case. But since in task description stands "As simple as possible" I decided to scrap the solution I had before.
- LiveFootballWorldCupScoreBoardTest class exists as a kind of Facade. Before it had more functionality. I left it as is, because it provides a readable result of changing scoreboard. I didn't want to put displaying logic into LiveMatches class.
- 