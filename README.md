# Coding challenge from Quality Minds
To run all test cases in Firefox and Chrome simultaneously:
```mvn clean test site```

To see test result report open target/site/allure-maven.html in a browser. Test defects are listed under 'Categories' or 'Behaviurs'.
To see the screenshot for a failed test click on test name, expand 'Tear down' and 'closeWebDriver' and click on 'screenshot' link.