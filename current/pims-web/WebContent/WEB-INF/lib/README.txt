Put jar files in here for external components you use
which do need to be in the delivered .war file.
This is on the local classpath for your project.
Components that are not needed at run time by the
web appplication should be in lib/
instead.

Where possible, the jars are managed by Ivy. These have "-ivy-" as part of the name.

The exceptions are jars that are not in an Ivy repository,
and some where Ivy mysteriously does not work.

TODO replace jstl.jar and standard.jar with more recent versions.

TODO where only one class has a dependency, use a private classpath.