Find out what time the next trains for my destination station leave

@Meta:
@issue ITIN-122
@tags component:itinerary

Narrative:
In order to to plan my trips more effectively
As a commuter
I want to know the next trains going to my destination

Scenario: Find the optimal itinerary between stations on the same line
Given North-South line trains from Hatfield leave Midrand
for Park at 7:55, 8:05, 8:15, 8:25, 8:35, 8:45, 8:55, 9:05
When I want to travel from Midrand to Park at 8:00
Then I should be told about the trains at: 8:05, 8:15, 8:25, 8:35