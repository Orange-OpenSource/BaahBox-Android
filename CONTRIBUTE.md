# How to contribute to this part of the project?


## Rule 0: Clean sources!

You should follow some naming conventions so as to have a clean code.
Please refer to the Kotlin style guide (https://developer.android.com/kotlin/style-guide), and avoid hungarian notation (https://jakewharton.com/just-say-no-to-hungarian-notation/).

Documentation in use is based on Dokka (https://github.com/Kotlin/dokka).
You have to write source documentation to keep them clear and understandable for the community.



## Rule 1: Use smartly Git branches

You should use feature branches for your work.
A common usage is to create a branch from the _dev_ branch for your feature, work on it, and once the job is done and the app is both building and running, merge to the _dev_ branch using a pull request.

Once the _dev_ branch is enough enriched, you should make a merge from _dev_ to _master_ so as to keep a strong, tested, building, running and well-working version of the sources.

Do not forge to squash commits.

Feel free to get more details about Gitflow workflow here: https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow



## Rule 2: Write clear commit messages

So as to be notified of each commit and brought evolutions, you should format your commit messages following a clear and simple pattern.

For example:
```shell
git commit -s -m "chore: #3 - I dislike pineapples"
```

The first item can be choosen within "chore", "feat", "fix", "test", "doc", "refactor" or "style".
Before the "-" symoble we can refer the issue or card we work on on.
After the "-" we can add more details.
Keep in mind commit messages should be quite short (~ 80 characters length), you can add more details with line breaks.

For example:
```shell
git commit -s -m $'chore: #3 - I dislike pineapples\n\nLorem ipsum foo bar wizz berlingots...'
```

Keep in mind your commit message has a title (~ 80 characters length) and a body (after the \n\n) where more details can be added.

Feel free to have a look on this link: https://gist.github.com/stephenparish/9941e89d80e2bc58a153. You can also use a GUI-based tool for your commis, but keep them clear and follow the same principles.



## Rule 3: Use pull requests

Once you have choosen an issue to work on, you should create a "Work In Progress" pull request prefixed by "WIP:". Thus you will have always a look on the state of your evolutions and your colleagues will be able to know on which things you work on. So as to update the _dev_ and _master_ branches, you have to make such pull requests. Thus you can ask colleagues for help so as to double-check your source code and be sure you won't break anything. These branches can be push-protected.



## Rule 4: About updates

Once your work is done, and the project is building and the app well running, you will update the _dev_ branch or the _master_ branch, and maybe create a tag.
Before doing such operations, you should run the command:

```shell
./prepare-release.sh
```

This command will increase by 1 the version code of the app, build the HTML documentation, check configuration file for the app and update the changelog with the last commits. Have a look on the changelog to ensure it's clean. It will also check if images have copyrights notices in metadata, run unit tests and run instrumented tests.

The version name follows the pattern X.Y.Z, where Z is a value to increment for fixes, Y for minor evolutions and X for major evolutions. This value should be modified manually by you when you completed a release or a merge in the _dev_ branch.

_Dokka_ is used for the source code documentation. You need to have a JDK installed so as to run the Gradle dedicated command.
To build the HTML documentation, run:

```shell
# For macOS
./gradlew dokka

# Same kind of command for other OS
## ...
```

Note Dokka 0.9.18 (in use here) has problems with last JDK versions, so HTML production may fail (https://github.com/Kotlin/dokka/issues/294).



## Rule 5: Configuration as code

So as to have more flexibility in the app, several elements are defined in the _assets/app_configuration.properties_ like if we can enable games or not, devices and sensors identifiers, or configuration elements.

This file avoid to have hard-coded values in our source code.

The configuration file is controlled by an open source Gradle plugin hosted in this project. It will compare the values of your file to another file containing rules (regular expressions). The plugin controls the plain text files. But if you want to add new configuration elements, do not forget to update files in the _tools/properties_ package: these elements are parsed and a managed in the Kotlin side.



## Rule 6: Tags and versions

Once we are sure a version of the sources and the app builds and runs correctly without bugs, we have to tag it with the version number. Thus we ensure to keep versions of working sources.  

Tags can follow this format "vX.Y.Z-versioncode", where _versioncode_ is the version code of the Android app, and _X.Y.Z_ is the version number.  

For version number _X.Y.Z._, the _X_ refers to the major update, the _Y_ to a minor update, and the _Z_ to the bug fix.  



## Rule 7: Slack

A Slack web hook is used to notify of new issues, pull requests and tags. Feel free to go to "android-cicd" channel on the "3Dhandz" workspace.

