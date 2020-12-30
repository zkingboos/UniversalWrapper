# UniversalWrapper

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/1abf5f28f9d44d1281619cd726cdd658)](https://www.codacy.com/manual/zkingboos/UniversalWrapper?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=zkingboos/UniversalWrapper&amp;utm_campaign=Badge_Grade)

A universal jdbc wrapper, helps you to make queries easier, with the simplicity of the functional interfaces of java!

## Summary

* [Installation](#installation)
* [Documentation](#documentation)
* [Usage](#usage)
* [Contributing](#contributing)

## Installation

[![](https://jitpack.io/v/zkingboos/UniversalWrapper.svg)](https://jitpack.io/#zkingboos/UniversalWrapper)

Using jitpack:

> Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
	<url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!--just for sql wrapper (only compile sql not mysql, as in the previous version)-->
    <dependency>
        <groupId>com.github.zkingboos.universalwrapper</groupId>
	<artifactId>sql-wrapper</artifactId>
	<version>VERSION</version>
    </dependency>
    
    <!--just mysql wrapper-->
    <dependency>
        <groupId>com.github.zkingboos.universalwrapper</groupId>
        <artifactId>mysql-wrapper</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

> Gradle

```groovy
repositories {
    maven { url = 'https://jitpack.io' }
}

dependencies {
    // Just sql-wrapper
    implementation 'com.github.zkingboos.universalwrapper:sql-wrapper:VERSION'
    
    // Just mysql-wrapper
    implementation 'com.github.zkingboos.universalwrapper:mysql-wrapper:VERSION'
}
```

## Documentation

You can see project's documentation [here](https://zkingboos.github.io/UniversalWrapper).

## Usage

Look [here](https://github.com/zkingboos/UniversalWrapper/tree/master/examples) for detailed examples!

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.
