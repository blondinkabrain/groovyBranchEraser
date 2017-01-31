# groovyBranchEraser
Groovy branch eraser

In our company each bug/feature you add\fix have to be in the new branch. And every branch we start from current version number.
For example I fix bug in version 3.6.1, and make pull-request in 3.6.1(current release version) and development. Good name for branch is 3.6.1/bugfix/bad-styles-in-head. By the time version is released I have a lot of brances in 3.6.1 in "local branches tree". So I've decided to remove them very fast and easy. I only run cmd and insert parametres. 

example config run-  groovy branchEraser f:\groovy\groovyBranchEraser\ 3.6.1
example config run-  groovy branchCreator f:\groovy\groovyBranchEraser\ 3.6.1

in cmd:
 groovy branchEraser [wayToRepository] [startBranchNameWith]
