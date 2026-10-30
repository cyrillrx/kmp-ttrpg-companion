# sqlite-jdbc logs through SLF4J only when it is on the classpath, and loads its JNI classes by name.
-dontwarn org.slf4j.**
-keep class org.sqlite.** { *; }
