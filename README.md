JaCoCo Toolbox
==============

Toolbox for working with [JaCoCo](http://jacoco.org/jacoco/index.html) code
coverage files from the commandline.

Using this JaCoCo toolbox, JaCoCo exec files can get:
* identified,
* [merged](#merging-exec-files),
* turned into [CSV reports](#generating-a-csv-report),
* turned into [XML reports](#generating-a-csv-report), and
* turned inte [HTML reports](#generating-a-csv-report)
from the command-line.

####Table of Contents
1. [Prerequisites](#prerequisites)
2. [Quickstart demo](#quickstart-demo)



Prerequisites
-------------

To run/compile the toolbox, you need to have
[Java](http://www.oracle.com/technetwork/java/index.html) and
[Maven](https://maven.apache.org/) installed. Distributions (Ubuntu,
Debian, ...) have them packaged, so you can just install them through your
distribution's package manager.



Quickstart demo
---------------

#### Merging exec files

```
./run_toolbox.sh merge \
  --input toolbox/src/test/resources/jacoco-foo.exec \
  --input toolbox/src/test/resources/jacoco-bar.exec \
  --output merged.exec
```

Then the file `merged.exec` holds the merge of `jacoco-foo.exec` and
`jacoco-bar.exec`.

#### Generating a CSV report

```
./run_toolbox.sh report-csv \
  --input merged.exec \
  --analyze-for toolbox/src/test/resources/TestDataGroupMerged.jar \
  --output report.csv

cat report.csv
```

#### Generating an XML report

```
./run_toolbox.sh report-xml \
  --input merged.exec \
  --analyze-for toolbox/src/test/resources/TestDataGroupMerged.jar \
  --output report.xml

cat report.xml
```

#### Generating an HTML report

```
./run_toolbox.sh report-html \
  --input merged.exec \
  --analyze-for toolbox/src/test/resources/TestDataGroupMerged.jar \
  --source test-data-merged/src/main/java \
  --output report-html

firefox report-html/index.html
```
