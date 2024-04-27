## Commonly Used MAVEN Commands

#### 1. To find if maven is installed or not along with its version and its associated Java version

```shell
$ mvn -version

# OR

$ mvn -verbose
```

#### 2. To find newer available versions over the current version of dependencies that we have in pom.xml

```shell
$ mvn versions:display-dependency-updates
```

#### 3. To print complete maven logs on console while running the project.

Generally, use this `-X` or `-e` flag to understand more about the error thrown at Maven side

```shell
# add -X flag to print complete maven logs
$ mvn clean test -X

# OR

$ mvn clean test -e
```