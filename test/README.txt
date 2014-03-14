Unit tests usually try to run tests on single classes and avoid access to Excel whereever possible.
However, some of them open also up excel files, run conversions and check the results.
Contrary, Acceptance tests need to start a fitnesse instance and communicate with fitnesse to import data and check the results.
