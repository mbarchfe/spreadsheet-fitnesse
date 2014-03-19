## spreadsheet-fitnesse

Convert spreadsheets into FitNesse wiki pages, run them as test suites and view the results in html.

There are many variants of the original FIT library available now and also different projects built around them. One of them is the FitLibrary project which already provides the spreadsheet format as an alternative to HTML pages to specify FIT tests. Unfortunately, it does not support the SLIM table style, which allows to run tests on many environments like Ruby, Coffeescript, .NET and many more. 

This project aims at filling that gap: specifying tests in spreadsheets while using SLIM Fixtures. It does so by creating FitNesse wiki pages. This approach is experimental, it might turn out that the conversion to wiki pages is too cumbersome. I have used it for two small projects with only a couple of spreadsheets. For larger projects it might be preferrable to use the SLIM functionality directly without the intermediate wiki format. As opposed to the creation of wiki pages, which is achieved via FitNesse's Rest API, that would require tighter coupling to the FitNesse project.


### Motivation: Why Spreadsheets?

There are some reasons why you would like to write FIT Tests in a spreadsheet. FitNesse already allows to copy and paste between wiki pages and spreadsheets. This approach goes beyond with the following reasoning:

* people with different roles should collaborate on writing the specification and for many non-developers a spreadsheets is more familiar than a wiki
* calculations can be done directly in the FIT test, e.g. calculations on dates, amounts, whatever
* no need to copy and paste data from the spreadsheet to FitNesse pages over and over again
* sheets can be run parameterized, similar to Slim scenario tables, but with all spreadsheet functionality in place, see [Macro sheets](doc/MacroSheets.md)

But there are disadvantages, too:

* There is a media break between input format (spreadsheet) and the generated output (HTML). Effort must be put into maintaining the readability of the test results.
* Test cases might become over-sophisticated. With the usage of variables and formulas the area of specifiation by example might be left and some kind of prototyping with spreadsheet might happen.

Therefore, when a project decides to specify in spreadsheets a guideline should als be developed on what features should be used or not.