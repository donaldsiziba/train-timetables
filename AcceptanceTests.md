# Acceptance Tests
To run the _Acceptance Test Suite_, type the following in command line:
```bash
mvn -P acceptance-tests clean verify
```

## Acceptance Test Reports
After the build has run, the Acceptance Test reports can be found in _target/site/serenity_. Use a browser to view the _index.html_ file.

### Test Results Page
![Test Results Page](src/test/resources/images/test-results.png)
### Find the Next Train Departure Time Story
![Find Next Train Departure Time Story](src/test/resources/images/find-next-train-depature-story.png)
### Calculate Arrival Times Story
![Calculate Arrival Times Story](src/test/resources/images/calculate-arrival-times-story.png)