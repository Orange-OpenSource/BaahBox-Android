#!/bin/bash

#    Orange Baah Box
#    Copyright (C) 2017 – 2019 Orange SA
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program. If not, see <http://www.gnu.org/licenses/>.

#
# Script which will increase by 1 the version code of the app.
#

GRADLE_PATH="../project"
GRADLE_COMMAND="gradlew"
GRADLE_BUILD="$GRADLE_COMMAND build"

echo -e   "\n* ************************* *"
echo 		"* Incrementing version code *"
echo  -e    "* ************************* *\n"

APP_GRADLE_BUILD_FILE="../project/app/build.gradle"
LINE_FOR_BUILD_VALUE=45

TEMP_FILE="build.gradle-temp"

# Extract value of the build
oldBuildLine=`sed "${LINE_FOR_BUILD_VALUE}q;d" $APP_GRADLE_BUILD_FILE`
oldBuildVersion=`echo $oldBuildLine | sed 's/[^0-9]*//g'`

# Update build value
newBuildVersion=$((oldBuildVersion+1))
newBuildLine="        versionCode $newBuildVersion"

# Write the new value in app/build.gradle
awk "{ if (NR == $LINE_FOR_BUILD_VALUE) print \"$newBuildLine\"; else print \$0}" $APP_GRADLE_BUILD_FILE > $TEMP_FILE
cat $TEMP_FILE > $APP_GRADLE_BUILD_FILE
rm $TEMP_FILE

echo -e "\n>>>>> New version code will be: $newBuildVersion"

# Building project
#cd $GRADLE_PATH
#./$GRADLE_BUILD
#cd ..

echo -e "\n>>>>> Completed!"