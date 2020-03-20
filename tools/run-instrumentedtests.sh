#!/bin/bash

#    Orange Baah Box
#    Copyright (C) 2017 – 2020 Orange SA
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
# Script which check if legal mentions have been defined in images
#

GRADLE_PATH="../project"
INSTRUMENTED_TESTS_REPORTS="app/build/reports/androidTests/connected/flavors"
GRADLE_COMMAND="gradlew"
GRADLE_TEST="$GRADLE_COMMAND connectedAndroidTest"

SCORE_CONFIDENCE=100

echo -e   "\n* ************************** *"
echo 		"* Running instrumented tests *"
echo  -e    "* ************************** *\n"

echo -e "\n>>>>> Running instrumented tests"
cd $GRADLE_PATH
./$GRADLE_TEST > /dev/null

echo -e "\n>>>>> Checking results"
ALL_GOOD=true
for flavorsReportFolder in `ls $INSTRUMENTED_TESTS_REPORTS`; do
	for testReportFolder in $flavorsReportFolder; do
		echo "Processing $testReportFolder..."
		report_file="$INSTRUMENTED_TESTS_REPORTS/$flavorsReportFolder/index.html "
		echo "Opening HTML report file: $report_file"
		open $report_file
		percent_score=`cat $report_file | grep percent | sed 's/[^0-9]*//g'`
		if [ $percent_score -ne $SCORE_CONFIDENCE ]; then
			ALL_GOOD=false
			echo "❌ 👎 This test score is not expected. It should be $SCORE_CONFIDENCE instead of $percent_score"
		else
			echo "✅ 👌 This test score is equal to $SCORE_CONFIDENCE. Good."
		fi;
	done
done

cd ..

echo -e "\n>>>>> Completed!"

if $ALL_GOOD; then
	exit 0
else
	exit 1
fi;
