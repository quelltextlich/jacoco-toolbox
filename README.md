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
3. [FAQ](#faq)



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



FAQ
---

#### Why those ridiculously high version numbers like `4102`, or `4103`?

JaCoCo's own exec files store a version number in them. And JaCoCo itself
can only operate on a specific version of exec files. Currently, as of
summer 2015, mostly two exec file versions are used in the wild `4102`, and
`4103`. JaCoCo Toolbox cam be built against either of those versions.

* The JaCoCo Toolbox `format-4102` branch builds `0.4102.*` toolboxes that
  can read the `4102` exec format.
* The JaCoCo Toolbox `format-4103` branch builds `0.4103.*` toolboxes that
  can read the `4103` exec format.

Here is a list of version numbers:

| JaCoCo version              | exec version | JaCoCo Toolbox branch |
|:---------------------------:|:------------:|:---------------------:|
| `jacoco-0.7.5.201505241946` | `4103`       | `format-4103`         |
| `jacoco-0.7.4.201502262128` | `4102`       | `format-4102`         |
| `jacoco-0.7.3.201502191951` | `4102`       | `format-4102`         |
| `jacoco-0.7.2.201409121644` | `4102`       | `format-4102`         |
| `jacoco-0.7.1.201405082137` | `4102`       | `format-4102`         |
| `jacoco-0.7.0.201403182114` | `4102`       | `format-4102`         |
| `jacoco-0.6.5.201403032054` | `4102`       | `format-4102`         |
| `[...]`                     | `[...]`      | `[...]`               |

#### What's the `Incompatible version 1006` (or `[...] 1007`) error?

##### Short answer:
If you get the `Incompatible version 1006` error, switch to using
JaCoCo toolbox's `format-4102` branch and rebuild the toolbox.

If you get the `Incompatible version 1007` error, switch to using
JaCoCo toolbox's `format-4103` branch and rebuild the toolbox.

##### Long answer:
That's an error message coming straight from JaCoCo itself. The `1006`
and `1007` are the exec file version numbers, and the error means that
you're trying to read a, say, version `0x1006` exec file with a JaCoCo
that cannot read a version `0x1006` exec file.

(Noticed, that I added a `0x` in front of the version numbers? The
numbers in JaCoCo's error message are actually hex numbers, but lack
the hex marker :-/ Please chime in on jacoco/jacoco#319)

When writing those hex version numbers as plain decimals, they map to
JaCoCo Toolbox's `format-*` branches.

| Hex      | Decimal | JaCoCo Toolbox branch |
| :------: | :-----: | :-------------------: |
| `0x1007` | `4103`  | `format-4103`         |
| `0x1006` | `4102`  | `format-4102`         |


So the `Incompatible version 1006` error means that you're trying to
read a exec file in version `4102` (`0x1006`) with a JaCoCo version
that does not know how to read a file of that version.

JaCoCo Toolbox exists for several versions of JaCoCo, and by switching
to the corresponding `format-*` branch of JaCoCo Toolbox, and
rebuilding the JaCoCo Toolbox, you should get a JaCoCo Toolbox that
can read the exec.