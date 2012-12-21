# Ant Addons

AntAddons is an external library that adds additional Ant task 

## Installation	

Load all tasks defined in AntAddons

```
<taskdef resource="org/datech/antaddons/antaddons.properties">
 <classpath>
<pathelement location="../dist/ant-addons.jar"/>
	  </classpath>
</taskdef>
```

## Task manual

### Diff Task

```
<diff from="${file.from}" to="${file.to}" output="${file.diff}" verbose="true"  />
```
