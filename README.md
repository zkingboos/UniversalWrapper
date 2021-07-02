# UniversalWrapper

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/1abf5f28f9d44d1281619cd726cdd658)](https://www.codacy.com/manual/zkingboos/UniversalWrapper?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=zkingboos/UniversalWrapper&amp;utm_campaign=Badge_Grade)

A universal jdbc wrapper, helps you to make queries easier, with the simplicity of the functional interfaces of java!

## Summary

* [Installation](#installation)
* [Modules](#modules)
* [Documentation](#documentation)
* [Usage](#usage)
* [Contributing](#contributing)
* [Contributors](#contributors)

## Installation

[![](https://jitpack.io/v/zkingboos/UniversalWrapper.svg)](https://jitpack.io/#zkingboos/UniversalWrapper)

Using jitpack:

> Maven

<details>
<summary>Click here to show the snippet</summary>

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!--just for sql wrapper (only compile sql not mysql, as in the previous version) (heavy)-->
    <dependency>
        <groupId>com.github.zkingboos.universalwrapper</groupId>
	    <artifactId>sql-wrapper</artifactId>
	    <version>VERSION</version>
    </dependency>

    <!--just for sql wrapper (only compile sql not mysql, as in the previous version) (light)-->
    <dependency>
        <groupId>com.github.zkingboos.universalwrapper</groupId>
	    <artifactId>sql-wrapper-bukkit</artifactId>
	    <version>VERSION</version>
    </dependency>
    
    <!--just mysql wrapper (heavy)-->
    <dependency>
        <groupId>com.github.zkingboos.universalwrapper</groupId>
        <artifactId>mysql-wrapper</artifactId>
        <version>VERSION</version>
    </dependency>

    <!--just mysql wrapper (light)-->
    <dependency>
        <groupId>com.github.zkingboos.universalwrapper</groupId>
        <artifactId>mysql-wrapper-bukkit</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```
</details>

> Gradle

<details>
<summary>Click here to show the snippet</summary>

```gradle
repositories {
    maven { url = 'https://jitpack.io' }
}

dependencies {
    // Just sql-wrapper (Heavy)
    implementation 'com.github.zkingboos.universalwrapper:sql-wrapper:VERSION'

    // Just sql-wrapper (Light)
    implementation 'com.github.zkingboos.universalwrapper:sql-wrapper-bukkit:VERSION'
    
    // Just mysql-wrapper (Heavy)
    implementation 'com.github.zkingboos.universalwrapper:mysql-wrapper:VERSION'
    
    // Just mysql-wrapper (Light)
    implementation 'com.github.zkingboos.universalwrapper:mysql-wrapper-bukkit:VERSION'
}
```
</details>

<!--From https://github.com/LorenzooG/jplank/-->

## Modules

| Name                                             | Description                                                                	|
| ------------------------------------------------ | -------------------------------------------------------------------------- 	|
| [shared](shared)                                 | All shared sources for linking the components                              	|
| [sql-wrapper](sql-wrapper)                       | Sql provider compiled with all dependencies                        		|
| [sql-wrapper-bukkit](sql-wrapper-bukkit)         | Sql provider for bukkit applications, only includes the necessary dependencies    	|
| [mysql-wrapper](mysql-wrapper)                   | Mysql provider compiled with all dependencies                    			| 
| [mysql-wrapper-bukkit](mysql-wrapper-bukkit)     | Mysql provider for bukkit applications, only includes the necessary dependencies   |
| [docs](docs)                                     | The project documentation for github-pages                        			|
| [examples](examples)                             | Examples for the most abstract cases                        			|
| [sqlreader-extension](sqlreader-extension)	   | Use sql script files path instead raw sql queries					|

## Documentation

You can see project's documentation [here](https://zkingboos.github.io/UniversalWrapper).

## Usage

Look [here](examples) for detailed examples!

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## Contributors

[<img alt="zkingboos" src="https://avatars3.githubusercontent.com/u/42500187?v=4&s=117&width=117">](https://github.com/zkingboos) |
:---:|
[zkingboos](https://github.com/zkingboos)|
