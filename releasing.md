Release
========

To do a `JaCoCo Toolbox` release, run

```
mvn release:prepare -Prelease,test-data-modules -Dresume=false && \
mvn release:perform -Prelease,test-data-modules
```
