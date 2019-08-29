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
# Script which will trigger other scripts to as to prepare the project and its sources for push to the repo or release
# You ned to have an installed JDK so as to run Gradle through command line.
#
# Will do the following actions:
#	- Check the app configuration details
#	- Check if images do not contain legal notices in metadata
#	- Check if unit tests are all passing
#	- Increment the version code number
#	- Build Dokka-vased documentation using Gradle
#	- Update changelog using Gradle (still need to read and modify it manually after for now)
#

#. *******************************************
# Check the configuration elements for the app
#. *******************************************

./check-appconfiguration.sh

# ********************************
# Check legal mentions in pictures
# ********************************

./check-imagecredits.sh

# ##############
# Run unit tests
# ##############

./run-unittests.sh

unittests_status=$?
if [ $unittests_status -ne 0 ]; then
	echo "🚨 Something went wrong with unit tests, not going further!"
	exit 1
fi;

# *********************
# Increase version code
# *********************

./update-versioncode.sh

# *******************
# Build documentation
# *******************

./update-documentation.sh

# ****************
# Update changelog
# ****************

./update-changelog.sh

exit 0

