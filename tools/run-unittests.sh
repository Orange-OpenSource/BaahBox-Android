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
# Script which check if legal mentions have been defined in images
#

GRADLE_PATH="../project"
UNIT_TESTS_REPORTS="app/build/reports/tests"
GRADLE_COMMAND="gradlew"
GRADLE_CHECK="$GRADLE_COMMAND test"

SCORE_CONFIDENCE=100

echo -e   "\n* ****************** *"
echo 		"* Running unit tests *"
echo  -e    "* ****************** *\n"

echo -e "\n>>>>> Running unit tests"
cd $GRADLE_PATH
./$GRADLE_CHECK

echo -e "\n>>>>> Checking results"
ALL_GOOD=true
for testReportFolder in `ls $UNIT_TESTS_REPORTS`; do
	echo "Processing $testReportFolder..."
	report_file="$UNIT_TESTS_REPORTS/$testReportFolder/index.html "
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

cd ..

echo -e "\n>>>>> Completed!"

if $ALL_GOOD; then
	exit 0
else
	exit 1
fi;
