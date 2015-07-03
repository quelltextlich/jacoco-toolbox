JaCoCo Toolbox
==============

Toolbox for working with [JaCoCo](http://jacoco.org/jacoco/index.html) code
coverage files from the commandline.

Using this JaCoCo toolbox, JaCoCo exec files can get:
* identified,
* [merged](#merging-exec-files),
* turned into [CSV reports](#generating-a-csv-report),
* turned into [XML reports](#generating-a-xml-report), and
* turned inte [HTML reports](#generating-a-html-report)

right from the command-line. No Java coding needed.

####Table of Contents
1. [Quickstart](#quickstart)
2. [More demos](#more-demos)
3. [Building JaCoCo Toolbox on your own](#building-jacoco-toolbox-on-your-own)
4. [FAQ](#faq)



Quickstart
----------

1. Get [`Java`](http://www.oracle.com/technetwork/java/index.html).

   Distributions (Ubuntu, Debian, ...) have `Java` packaged, so you can just
   install it through your distribution's package manager.

2. Download a [prebuilt JaCoCo Toolbox artifact](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4103.1/jacoco-toolbox-0.4103.1.jar).

3. Run it!
   ```
   java -jar jacoco-toolbox-0.4103.1.jar
   ```

   The above command shows the help screen, which exposes all available
   commands along with a short description. Each command provides its own,
   more detailed help screen through the `--help` argument. E.g.:

   ```
   java -jar jacoco-toolbox-0.4103.1.jar report-csv --help
   ```

   In the [demo section](#more-demos), you'll find more examples on how to
   merge `exec` files or generate reports.

   If you just want to toy around using some sample files, you can clone
   this repo, and find sample `exec` files underneath
   [`toolbox/src/test/resources`](https://github.com/quelltextlich/jacoco-toolbox/tree/format-4103/toolbox/src/test/resources).



More demos
----------

`cd` into the directory you cloned JaCoCo Toolbox to.

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

This command builds a CSV report for the class files in
`[...]/TestDataGroupMerged.jar` from the runtime data that got previously
collected in `merged.exec`.


#### Generating an XML report

```
./run_toolbox.sh report-xml \
  --input merged.exec \
  --analyze-for toolbox/src/test/resources/TestDataGroupMerged.jar \
  --output report.xml

cat report.xml
```

This command builds an XML report for the class files in
`[...]/TestDataGroupMerged.jar` from the runtime data that got previously
collected in `merged.exec`.

#### Generating a HTML report

```
./run_toolbox.sh report-html \
  --input merged.exec \
  --analyze-for toolbox/src/test/resources/TestDataGroupMerged.jar \
  --source test-data-merged/src/main/java \
  --output report-html

firefox report-html/index.html
```

This command builds a HTML report for the class files in
`[...]/TestDataGroupMerged.jar` from the runtime data that got previously
collected in `merged.exec`. To render source file coverage, the source files from `test-data-merged/src/main/java` get used.



Building JaCoCo Toolbox on your own
-----------------------------------

To compile the toolbox, you need to have
* [Java](http://www.oracle.com/technetwork/java/index.html) and
* [Maven](https://maven.apache.org/) installed.

Distributions (Ubuntu, Debian, ...) have them packaged, so you can just
install them through your distribution's package manager.

Once you have them installed, just use JaCoCo Toolbox through the `run_toolbox.sh` command, which takes care of building for you (see the [demo section](#more-demos)), or you can build by hand by running

```
mvn clean package
```

Once that command passes, you'll find the built artifact in
`toolbox/target/jacoco-toolbox-0.4103.1.jar`.

You can now move on to the [demo section](#more-demos) to see how to merge
`exec` files or generate reports from them.



FAQ
---

1. [Why those ridiculously high version numbers like `4102`, or `4103`?](#why-those-ridiculously-high-version-numbers-like-4102-or-4103)
2. [What's the `Incompatible version 1006` (or `[...] 1007`) error?](#whats-the-incompatible-version-1006-or--1007-error)

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

| JaCoCo version              | exec version | JaCoCo Toolbox branch | Prebuilt JaCoCo Toolbox `.jar` |
|:---------------------------:|:------------:|:---------------------:| :----------------------------: |
| `jacoco-0.7.5.201505241946` | `4103`       | `format-4103`         | [`jacoco-toolbox-0.4103.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4103.1/jacoco-toolbox-0.4103.1.jar) |
| `jacoco-0.7.4.201502262128` | `4102`       | `format-4102`         | [`jacoco-toolbox-0.4102.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4102.1/jacoco-toolbox-0.4102.1.jar) |
| `jacoco-0.7.3.201502191951` | `4102`       | `format-4102`         | [`jacoco-toolbox-0.4102.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4102.1/jacoco-toolbox-0.4102.1.jar) |
| `jacoco-0.7.2.201409121644` | `4102`       | `format-4102`         | [`jacoco-toolbox-0.4102.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4102.1/jacoco-toolbox-0.4102.1.jar) |
| `jacoco-0.7.1.201405082137` | `4102`       | `format-4102`         | [`jacoco-toolbox-0.4102.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4102.1/jacoco-toolbox-0.4102.1.jar) |
| `jacoco-0.7.0.201403182114` | `4102`       | `format-4102`         | [`jacoco-toolbox-0.4102.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4102.1/jacoco-toolbox-0.4102.1.jar) |
| `jacoco-0.6.5.201403032054` | `4102`       | `format-4102`         | [`jacoco-toolbox-0.4102.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4102.1/jacoco-toolbox-0.4102.1.jar) |
| `[...]`                     | `[...]`      | `[...]`               | `[...]` |

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

| Hex      | Decimal | JaCoCo Toolbox branch | Prebuilt JaCoCo Toolbox `.jar` |
| :------: | :-----: | :-------------------: | :----------------------------: |
| `0x1007` | `4103`  | `format-4103`         | [`jacoco-toolbox-0.4103.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4103.1/jacoco-toolbox-0.4103.1.jar) |
| `0x1006` | `4102`  | `format-4102`         | [`jacoco-toolbox-0.4102.1.jar`](http://search.maven.org/remotecontent?filepath=at/quelltextlich/jacoco/jacoco-toolbox/0.4102.1/jacoco-toolbox-0.4102.1.jar) |


So the `Incompatible version 1006` error means that you're trying to
read a exec file in version `4102` (`0x1006`) with a JaCoCo version
that does not know how to read a file of that version.

JaCoCo Toolbox exists for several versions of JaCoCo, and by switching
to the corresponding `format-*` branch of JaCoCo Toolbox, and
rebuilding the JaCoCo Toolbox, you should get a JaCoCo Toolbox that
can read the exec.