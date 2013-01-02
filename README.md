# Ant Addons

AntAddons is an external library that adds additional Ant task 

AntAddons currently contains only one task - Diff

## Installation	

Load all tasks defined in AntAddons. You have explicitly to point the location of Ant Addons jar:

```xml
<taskdef resource="org/datech/antaddons/antaddons.properties">
 <classpath>
<pathelement location="../dist/ant-addons.jar"/>
	  </classpath>
</taskdef>
```

## Task manual

### Diff task

This task is based on [[java-diff |http://www.incava.org/projects/577828189]] API for file comparison.

**Options:**

* from - From(source) file
* to - To(target) file
* output - Output file
* verbose - Print diff on stdout  (true|false). Default is false.

```xml
<diff from="file.from" to="file.to" output="file.diff" />
<diff from="file.from" to="file.to" output="file.diff" verbose="true"/>
```
