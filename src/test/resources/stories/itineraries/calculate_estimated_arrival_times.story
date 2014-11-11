Tell travellers when they will arrive at their destination

@Meta:
@issue ITIN-123
@tags component:itinerary

Narrative:
In order to plan my voyage more effectively
As a commuter
I want to know what time I will arrive at my destination

Scenario: Calculate arrival times
Given I want to go from <departure> to <destination>
And the next train is scheduled to leave at <departure-time> on the <line> line
When I ask for my arrival time
Then the estimated arrival time should be <arrival-time>
Examples:
| departure   | destination  | departure-time | line        | arrival-time |
| Midrand     | Park         | 8:05		      | North-South | 8:26         |
| Rhodesfield | Sandton      | 8:10			  | East-West   | 8:22         |
| Malboro     | OR Tambo	 | 8:15			  | Airport     | 8:28         |


