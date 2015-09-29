FamilyBiz
===============
Lastest version is **v1.2.0**

## Build

### env.properties
Create file **src/main/src/env.properties**, and set property **build.server.home** to your Apache Tomcat Home.

        build.server.home=/path/to/your/apache-tomcat

### profile.properties
Create file **src/main/src/profile.properties**, and set properties for different target profiles. Notice: the path splitor ('\') will be escaped on **Windows** filesystem, so append back-slash to escape it. (eg: '\\' will become to '\')

        dev.log.dir=/path/to/your/log4j.file.dir
        dev.sheet.font.file=/path/to/your/font.file
        prod.log.dir=/path/to/your/log4j.file.dir
        prod.sheet.font.file=/path/to/your/font.file

## Version History
* 2015/09/29 v1.2.0
* 2015/09/15 v1.1.1
* 2015/09/14 v1.1.0
* 2015/09/03 v1.0.1
* 2015/09/02 v1.0.0

